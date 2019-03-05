package frc.team3130.robot.autoCommands;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.tantanDrive.CubicPath;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.vision.Limelight;

public class AimAssist extends Command {
    private BufferedTrajectoryPointStream pointStreamLeft = new BufferedTrajectoryPointStream();
    private BufferedTrajectoryPointStream pointStreamRight = new BufferedTrajectoryPointStream();

    public AimAssist() {
        requires(Chassis.GetInstance());
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize()
    {
        double maxAcceleration = 30 * RobotMap.kAccelerationToEncoder;
        double cruiseVelocity = 100 * RobotMap.kVelocityToEncoder;

        Limelight.updateData();
        double goStraight = Limelight.getDistanceToTarget(true) - RobotMap.kLimelightBumper;
        double angularOffset = -Math.toRadians(Limelight.getDegHorizontalOffset());
        double goLeft = Math.tan(angularOffset) * goStraight - RobotMap.kLimelightOffset;
        double goSlope = Limelight.getTargetRotationTan();

        // Magic boost. To be deleted when(if) a real rotation is implemented
        // A linear approximation from the data collected with the software robot
        //goSlope *= (0.52/32)*(goStraight+RobotMap.kLimelightBumper-18)+1.238;

        System.out.format("Robot is going to Go %8.3f'' straight and left %8.3f with slope %8.3f%n",
                goStraight, goLeft, goSlope);

        // Basic sanity check
        if(goStraight <= 0 || goStraight >= 144 || goLeft <= -144 || goLeft >= 144) return;

        /* Convert distances from inches to encoder units */
        goStraight *= RobotMap.kDistanceToEncoder;
        goLeft *= RobotMap.kDistanceToEncoder;

        Chassis.configMP();
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

        /* Start motion profiles */
        Chassis.getFrontL().startMotionProfile(pointStreamLeft, 5, ControlMode.MotionProfile);
        Chassis.getFrontR().startMotionProfile(pointStreamRight, 5, ControlMode.MotionProfile);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute()
    {
        //Chassis.printVelocity();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return Chassis.getFrontL().isMotionProfileFinished() &&
                Chassis.getFrontR().isMotionProfileFinished() &&
                !OI.startAiming.get();
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
