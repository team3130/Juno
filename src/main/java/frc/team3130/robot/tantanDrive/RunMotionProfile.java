package frc.team3130.robot.tantanDrive;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.tantanDrive.Paths.Path;
import frc.team3130.robot.tantanDrive.Paths.PathStore;

/**
 *
 */
public class RunMotionProfile extends Command {
    private double[][] leftPath;
    private double[][] rightPath;

    public RunMotionProfile(Path thisPath) {
        //Put in the instance of whatever subsystem u need here
        requires(Chassis.GetInstance());
        leftPath = thisPath.getLeft();
        rightPath = thisPath.getRight();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Chassis.configMP(20);
        Chassis.GetInstance().mLeftMPController.setProfile(leftPath);
        Chassis.GetInstance().mRightMPController.setProfile(rightPath);
        Chassis.reset();

        Chassis.GetInstance().setControlState(0);
        Chassis.GetInstance().mLeftMPController.startMotionProfile();
        Chassis.GetInstance().mRightMPController.startMotionProfile();


    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Chassis.GetInstance().mLeftMPController.getSetValue() == SetValueMotionProfile.Hold && Chassis.GetInstance().mRightMPController.getSetValue() == SetValueMotionProfile.Hold;
    }

    // Called once after isFinished returns true
    protected void end() {
        Chassis.GetInstance().setControlState(1);
        Chassis.GetInstance().mLeftMPController.reset();
        Chassis.GetInstance().mRightMPController.reset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
