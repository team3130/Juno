package frc.team3130.robot.tantanDrive;
/**
 * The only routines we call on Talon are....
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


import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Notifier;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.util.Instrumentation;

public class MotionProfile {

    /**
     * The status of the motion profile executer and buffer inside the Talon.
     * Instead of creating a new one every time we call getMotionProfileStatus,
     * keep one copy.
     */
    private MotionProfileStatus _status = new MotionProfileStatus();

    /** cache for holding the active trajectory point */
    double _pos=0,_vel=0,_heading=0;

    /**
     * reference to the talon we plan on manipulating. We will not changeMode()
     * or call set(), just get motion profile status and make decisions based on
     * motion profile.
     */
    private WPI_TalonSRX _talon;
    /**
     * State machine to make sure we let enough of the motion profile stream to
     * talon before we fire it.
     */
    private int _state = 0;
    /**
     * Any time you have a state machine that waits for external events, its a
     * good idea to add a timeout. Set to -1 to disable. Set to nonzero to count
     * down to '0' which will print an error message. Counting loops is not a
     * very accurate method of tracking timeout, but this is just conservative
     * timeout. Getting time-stamps would certainly work too, this is just
     * simple (no need to worry about timer overflows).
     */
    private int _loopTimeout = -1;
    /**
     * If start() gets called, this flag is set and in the control() we will
     * service it.
     */
    private boolean _bStart = false;

    /**
     * Since the CANTalon.set() routine is mode specific, deduce what we want
     * the set value to be and let the calling module apply it whenever we
     * decide to switch to MP mode.
     */
    private SetValueMotionProfile _setValue = SetValueMotionProfile.Disable;
    /**
     * How many trajectory points do we wait for before firing the motion
     * profile.
     */
    private static final int kMinPointsInTalon = 5;
    /**
     * Just a state timeout to make sure we don't get stuck anywhere. Each loop
     * is about 20ms.
     */
    private static final int kNumLoopsTimeout = 10;

    private static double[][] profile;

    private static int totalCnt;


    /**
     * Lets create a periodic task to funnel our trajectory points into our talon.
     * It doesn't need to be very accurate, just needs to keep pace with the motion
     * profiler executer.  Now if you're trajectory points are slow, there is no need
     * to do this, just call _talon.processMotionProfileBuffer() in your teleop loop.
     * Generally speaking you want to call it at least twice as fast as the duration
     * of your trajectory points.  So if they are firing every 20ms, you should call
     * every 10ms.
     */
    class PeriodicRunnable implements Runnable {
        public void run() {  _talon.processMotionProfileBuffer();    }
    }
    Notifier _notifer = new Notifier(new PeriodicRunnable());


    /**
     *
     * @param talon talon to use
     * @param fireRate firing rate of the Motion Profile in ms
     */
    public MotionProfile(WPI_TalonSRX talon, int fireRate) {
        _talon = talon;
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
        /* When we do re-enter motionProfile control mode, stay disabled. */
        _setValue = SetValueMotionProfile.Disable;
        /* When we do start running our state machine start at the beginning. */
        _state = 0;
        _loopTimeout = -1;
        // If application wanted to start an MP before, ignore and wait for next button press

        _bStart = false;
    }

    //TODO: remove instrumentation once done
    /**
     * Called every loop.
     */
    public void control() {
        /* Get the motion profile status every loop */
        _talon.getMotionProfileStatus(_status);

        /*
         * track time, this is rudimentary but that's okay
         */
        if (_loopTimeout < 0) {
            /* do nothing, timeout is disabled */
        } else {
            /* our timeout is nonzero */
            if (_loopTimeout == 0) {
                /*
                 * something is wrong. Talon is not present, unplugged, breaker
                 * tripped
                 */
                Instrumentation.OnNoProgress();
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
                case 0: /* wait for application to tell us to start an MP */
                    if (_bStart) {
                        _bStart = false;

                        _setValue = SetValueMotionProfile.Disable;
                        startFilling();
                        /*
                         * MP is being sent to CAN bus, wait a small amount of time
                         */
                        _state = 1;
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case 1: /*
                 * wait for MP to stream to Talon, really just the first few
                 * points
                 */
                    /* do we have a minimum numberof points in Talon */
                    if (_status.btmBufferCnt > kMinPointsInTalon) {
                        /* start (once) the motion profile */
                        _setValue = SetValueMotionProfile.Enable;
                        /* MP will start once the control frame gets scheduled */
                        _state = 2;
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case 2: /* check the status of the MP */
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
                         * because we set the last point's isLast to true, we will
                         * get here when the MP is done
                         */
                        _setValue = SetValueMotionProfile.Hold;
                        _state = 0;
                        _loopTimeout = -1;
                    }
                    break;
            }

            /* Get the motion profile status every loop */
            _talon.getMotionProfileStatus(_status);
            _heading = 0.0; //TODO: depreciated
            _pos = _talon.getActiveTrajectoryPosition();
            _vel = _talon.getActiveTrajectoryVelocity();

            /* printfs and/or logging */
            Instrumentation.process(_status, _pos, _vel, _heading);
        }
    }

    /** Start filling the MPs to all of the involved Talons. */
    private void startFilling() {
        /* since this example only has one talon, just update that one */
        if(totalCnt != 0) {
            startFilling(profile, totalCnt);
        }
    }

    public void setProfile(double[][] profile, int totalCnt){
        this.profile = profile;
        this.totalCnt = totalCnt;
    }

    private void startFilling(double[][] profile, int totalCnt) {

        /* create an empty point */
        TrajectoryPoint point = new TrajectoryPoint();

        /* did we get an underrun condition since last time we checked ? */
        if (_status.hasUnderrun) {
            /* better log it so we know about it */
            Instrumentation.OnUnderrun();
            /*
             * clear the error. This flag does not auto clear, this way
             * we never miss logging it.
             */
            _talon.clearMotionProfileHasUnderrun(0);
        }
        /*
         * just in case we are interrupting another MP and there is still buffer
         * points in memory, clear it.
         */
        _talon.clearMotionProfileTrajectories();

        /* set the base trajectory period to zero, use the individual trajectory period below */
        _talon.configMotionProfileTrajectoryPeriod(0, 30);

        /* This is fast since it's just into our TOP buffer */
        for (int i = 0; i < totalCnt; ++i) {
            double positionInches = profile[i][0];
            double velocityRPM = profile[i][1];
            // for each point, fill our structure and pass it to API
            //TODO: convert to be feed in profile of distance in inches and velocity in inch/s
            point.position = positionInches * RobotMap.kChassisTicksPerInch; //Convert Revolutions to Units
            point.velocity = velocityRPM * 4096 / 10.0; //Convert RPM to Units/100ms
            point.headingDeg = 0; /* future feature - not used in this example*/
            point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
            point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */
            point.timeDur = (int)profile[i][2];
            point.zeroPos = false;
            if (i == 0)
                point.zeroPos = true; /* set this to true on the first point */

            point.isLastPoint = false;
            if ((i + 1) == totalCnt)
                point.isLastPoint = true; /* set this to true on the last point  */

            _talon.pushMotionProfileTrajectory(point);
        }
    }
    /**
     * Called by application to signal Talon to start the buffered MP (when it's
     * able to).
     */
    public void startMotionProfile() {
        _bStart = true;
    }

    /**
     *
     * @return the output value to pass to Talon's set() routine. 0 for disable
     *         motion-profile output, 1 for enable motion-profile, 2 for hold
     *         current motion profile trajectory point.
     */
    public SetValueMotionProfile getSetValue() {
        return _setValue;
    }
}