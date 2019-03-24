package frc.team3130.robot.commands.Intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Intake;

/**
 *
 */
public class RetractTongue extends Command {
    public RetractTongue() {
        //Put in the instance of whatever subsystem u need here
        requires(Intake.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Intake.retractTongue();
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
