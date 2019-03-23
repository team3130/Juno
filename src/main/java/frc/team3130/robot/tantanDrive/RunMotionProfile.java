package frc.team3130.robot.tantanDrive;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.tantanDrive.Paths.Path;
import frc.team3130.robot.tantanDrive.Paths.PathStore;

/**
 *
 */
public class RunMotionProfile extends Command {
    private Path leftPath;
    private Path rightPath;

    public RunMotionProfile(PathStore.Paths thisSet) {
        //Put in the instance of whatever subsystem u need here
        requires(Chassis.GetInstance());
        leftPath = new Path(thisSet.getLeft());
        rightPath = new Path(thisSet.getRight());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Chassis.GetInstance().mLeftMPController.setProfile(leftPath);
        Chassis.GetInstance().mRightMPController.setProfile(rightPath);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
