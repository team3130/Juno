package frc.team3130.robot.autoCommands;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.tantanDrive.CubicPath;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.util.Matrix;
import frc.team3130.robot.vision.Limelight;

public class AimAssist extends Command {
    private BufferedTrajectoryPointStream pointStreamLeft = new BufferedTrajectoryPointStream();
    private BufferedTrajectoryPointStream pointStreamRight = new BufferedTrajectoryPointStream();
    private boolean isStreamReady = false;
    private boolean isRunning = false;

    public AimAssist() {
        requires(Chassis.GetInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize()
    {
        isRunning = false;
        isStreamReady = false;
        new Thread(() -> { isStreamReady = calculate(); }).start();
    }

    private boolean calculate()
    {
        double maxAcceleration = 30 * RobotMap.kAccelerationToEncoder;
        double cruiseVelocity = 100 * RobotMap.kVelocityToEncoder;

        Limelight.updateData();
        Matrix target = Limelight.getTargetVector(true);
        while (target == null) {
            try { Thread.sleep(9); }
            catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }

        double goSlope = Limelight.getTargetRotationTan();
        double rotation = Math.atan(goSlope);
        double goStraight = -target.get(0, 1);
        goStraight -= (RobotMap.kLimelightBumper+RobotMap.kLimelightForward) * Math.cos(rotation);
        goStraight += RobotMap.kLimelightForward;
        double goLeft = -target.get(0, 0);
        goLeft -= (RobotMap.kLimelightBumper+RobotMap.kLimelightForward) * Math.sin(rotation);
        goLeft -= RobotMap.kLimelightOffset;

        System.out.format("Robot is going to Go %8.3f'' straight and left %8.3f with slope %8.3f%n",
                goStraight, goLeft, goSlope);

        // Basic sanity check
        if(goStraight <= 0 || goStraight >= 144 || goLeft <= -144 || goLeft >= 144) return false;

        /* Convert distances from inches to encoder units */
        goStraight *= RobotMap.kDistanceToEncoder;
        goLeft *= RobotMap.kDistanceToEncoder;

        Chassis.configMP(10);
        double currentVelocity = 0.5*(Chassis.getRawSpeedL()+Chassis.getRawSpeedR());
        CubicPath path = new CubicPath( maxAcceleration, cruiseVelocity)
                .withDuration(0.1) // 10ms = 0.1 * 100ms
                .withEnterVelocity(currentVelocity)
                .withExitVelocity(0)
                .generateSequence(goStraight, goLeft, goSlope)
                .generateProfiles(RobotMap.kChassisWidth * RobotMap.kDistanceToEncoder);
        int totalCnt = path.getSize();

        /* create an empty point */
        TrajectoryPoint point = new TrajectoryPoint();
        point.headingDeg = 0; /* future feature - not used in this example*/
        point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
        point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */

        /* Fill up the motion profile's way point streams.
         TODO: Do this inside the path generator instead of populating the profile buffers, maybe? */
        pointStreamLeft.Clear();
        pointStreamRight.Clear();
        for (int i = 0; i < totalCnt; ++i) {
            point.zeroPos = i == 0;
            point.isLastPoint = (i + 1) == totalCnt;

            /* for each point, fill our structure and pass it to API */
            point.position = path.profileLeft[i][0];
            point.velocity = path.profileLeft[i][1];
            point.timeDur = 10;
            pointStreamLeft.Write(point);

            /* for each point, fill our structure and pass it to API */
            point.position = path.profileRight[i][0];
            point.velocity = path.profileRight[i][1];
            point.timeDur = 10;
            pointStreamRight.Write(point);
        }
        return true;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute()
    {
        if (!isRunning) {
            if (isStreamReady) {
                isRunning = true;
                /* Start motion profiles */
                Chassis.getFrontL().startMotionProfile(pointStreamLeft, 5, ControlMode.MotionProfile);
                Chassis.getFrontR().startMotionProfile(pointStreamRight, 5, ControlMode.MotionProfile);
            }
        }
        //Chassis.printVelocity();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (!OI.startAiming.get()) return true;
        if (isRunning) {
            return Chassis.getFrontL().isMotionProfileFinished() &&
                    Chassis.getFrontR().isMotionProfileFinished();
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Chassis.getFrontL().set(ControlMode.PercentOutput, 0);
        Chassis.getFrontR().set(ControlMode.PercentOutput, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

}
