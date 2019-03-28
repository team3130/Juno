package frc.team3130.robot.tantanDrive;


import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Notifier;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.tantanDrive.Paths.Path;

public class MotionProfileController {

    /**
     * The status of the motion profile executer and buffer inside the Talon.
     * Instead of creating a new one every time we call getMotionProfileStatus,
     * keep one copy.
     */
    private MotionProfileStatus _status = new MotionProfileStatus();

    private WPI_TalonSRX _talon;
    /**
     * State machine to make sure we let enough of the motion profile stream to
     * talon before we fire it.
     */
    private int _state = 0;

    /**
     * State machine timeout. Set to -1 to disable. Set to nonzero to count
     * down to '0' which will print an error message. Getting time-stamps would
     * certainly work too, this is just simple (prevents issue of timer overflow).
     */
    private int _loopTimeout = -1;

    /**
     * If start() gets called, this flag is set and in the control() we will
     * service it.
     */
    private boolean _bStart = false;

    /**
     * Motion Profiling control mode handle
     */
    private SetValueMotionProfile _setValue = SetValueMotionProfile.Disable;

    /**
     * A state timeout to make sure we don't get stuck anywhere. Each loop
     * is about 20ms.
     */
    private final int kNumLoopsTimeout = 10;

    private Path profile;

    private int totalCnt;

    private boolean isLeft;

    /*
     * The only routines handled in this class....
     *
     * changeMotionControlFramePeriod
     *
     * getMotionProfileStatus
     * clearMotionProfileHasUnderrun     to get status and potentially clear the error flag.
     *
     * pushMotionProfileTrajectory
     * clearMotionProfileTrajectories
     * processMotionProfileBuffer        to push/clear, and process the trajectory points.
     *
     * getControlMode                    to check if we are in Motion Profile Control mode.
     *
     * Example of advanced features not demonstrated here...
     * [1] Calling pushMotionProfileTrajectory() continuously while the Talon executes the motion profile, thereby keeping it going indefinitely.
     * [2] Instead of setting the sensor position to zero at the start of each MP, the program could offset the MP's position based on current position.
     */

    /**
     * Periodic runnable to push points to MP buffer
     *
     * Needs to keep pace with the motion profiler executer.
     * Thus called at twice as fast as the duration of trajectory points to keep up.
     *
     * If they are firing every 20ms, runnable should call every 10ms.
     */
    class PeriodicRunnable implements Runnable {
        public void run() {  _talon.processMotionProfileBuffer();    }
    }
    Notifier _notifer = new Notifier(new PeriodicRunnable());


    /**
     *
     * @param talon the talon to use
     * @param fireRate firing rate of the Motion Profile in ms
     */
    public MotionProfileController(WPI_TalonSRX talon, int fireRate, boolean isLeft) {
        _talon = talon;

        //is this the left side?
        this.isLeft = isLeft;

        // Set control framerate to half of MP fire rate
        _talon.changeMotionControlFramePeriod(fireRate / 2);
        double nf = (double)(fireRate/2) * 0.001;
        _notifer.startPeriodic(nf);
    }

    /**
     * Called to clear Motion profile buffer and reset state info during
     * disabled and when Talon is not in MP control mode.
     */
    public void reset() {
        _talon.clearMotionProfileTrajectories();

        //When we do re-enter motionProfile control mode, stay disabled.
        _setValue = SetValueMotionProfile.Disable;

        //Reset state machine to preMP state
        _state = 0;

        _loopTimeout = -1;

        // Reset start bool
        _bStart = false;
    }


    /**
     * Controller loop to determine setpoint status and handle the MP functionality
     */
    public void control() {
        // Get the motion profile status every loop
        _talon.getMotionProfileStatus(_status);

        //time tracking
        if (_loopTimeout < 0) {
            // do nothing, timeout is disabled
        } else {
            // Timeout is nonzero
            if (_loopTimeout == 0) {
                //something is wrong. Talon is not present, unplugged, breaker tripped
            } else {
                --_loopTimeout;
            }
        }

        if (_talon.getControlMode() != ControlMode.MotionProfile) {
            // we are not in MP mode.
            _state = 0;
            _loopTimeout = -1;
        } else {
            /*
             * we are in MP control mode. That means: starting Mps, checking Mp
             * progress, and possibly interrupting MPs if thats what you want to
             * do.
             */
            switch (_state) {
                case 0: //Wait for application to tell us to start an MP
                    if (_bStart) {
                        _bStart = false;

                        _setValue = SetValueMotionProfile.Disable;

                        //Start sending the motion profile to the talon buffer
                        startFilling();

                        //MP is being sent to CAN bus, wait a small amount of time
                        _state = 1;
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case 1:
                /*
                 * wait for MP to stream to Talon, really just the first few
                 * points
                 */
                    //Check to see if we have a minimum number of points in the buffer
                    if (_status.btmBufferCnt > RobotMap.kChassisMinPointsInBuffer) {
                        // start (once) the motion profile
                        _setValue = SetValueMotionProfile.Enable;
                        // MP will start once the control frame gets scheduled
                        _state = 2;
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case 2: // wait on end status
                    /*
                     * if talon is reporting things are good, keep adding to our
                     * timeout. Really this is so that you can unplug your talon in
                     * the middle of an MP and react to it.
                     */
                    if (_status.isUnderrun == false) {
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    /*
                     * If we are executing an MP and the MP finished, start loading
                     * another. We will go into hold state so robot servo's
                     * position.
                     */
                    if (_status.activePointValid && _status.isLast) {
                        /*
                         * We set the last point's isLast to true, so we will
                         * get here when the MP is done
                         */
                        _setValue = SetValueMotionProfile.Hold;
                        _state = 0;
                        _loopTimeout = -1;
                    }
                    break;
            }
        }
    }

    /**
     * Set the path profile that the motion profile controller should use
     * @param profile Path object the MP controller should use
     */
    public void setProfile(Path profile){
        this.profile = profile;
        this.totalCnt = profile.kNumPoints;
    }

    /**
     * Start filling the MPs to the talon.
     */
    private void startFilling() {
        if(totalCnt != 0) {
            startFilling(profile.Points, totalCnt);
        }
    }

    private void startFilling(double[][] profile, int totalCnt) {
        // create an empty point
        TrajectoryPoint point = new TrajectoryPoint();

        // did we get an underrun condition since last time we checked?
        if (_status.hasUnderrun) {
            //clear the error. This flag does not auto clear, logging already handles this
            _talon.clearMotionProfileHasUnderrun(0);
        }

        // clear MP buffer in case there are old points
        _talon.clearMotionProfileTrajectories();

        // set the base trajectory period to zero, use the individual trajectory period below
        _talon.configMotionProfileTrajectoryPeriod(0, 30);

        // Ticks per inch of the left or right side
        double ticksPerInch = isLeft ? RobotMap.kLChassisTicksPerInch : RobotMap.kRChassisTicksPerInch;

        for (int i = 0; i < totalCnt; ++i) {
            double positionInches = profile[i][0];
            double velocityInches = profile[i][1];
            // for each point, fill our structure and pass it to API
            point.position = positionInches * ticksPerInch; //Convert inch distance to encoder ticks
            point.velocity = velocityInches * ticksPerInch / 10.0; //Convert inches/s to Units/100ms
            point.headingDeg = 0; //not used
            point.profileSlotSelect0 = 0; // which set of gains would you like to use [0,3]?
            point.profileSlotSelect1 = 0; // future feature **DO NOT USE** cascaded PID [0,1], leave zero
            point.timeDur = (int)profile[i][2];
            point.zeroPos = false;
            if (i == 0)
                point.zeroPos = true;

            point.isLastPoint = false;
            if ((i + 1) == totalCnt)
                point.isLastPoint = true;

            _talon.pushMotionProfileTrajectory(point);
        }
    }

    /**
     * Called by application to signal Talon to start the buffered MP.
     */
    public void startMotionProfile() {
        _bStart = true;
    }

    /**
     * @return the output value to pass to Talon's set() routine. 0 for disable
     *         motion-profile output, 1 for enable motion-profile, 2 for hold
     *         current motion profile trajectory point.
     */
    public SetValueMotionProfile getSetValue() {
        return _setValue;
    }
}