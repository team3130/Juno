package frc.team3130.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Intake;

public class HatchIn extends Command {
    public HatchIn() {
        //Put in the instance of whatever command u need here
        //requires();
        requires(Intake.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Intake.runHatchIntake(-0.9);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Intake.runHatchIntake(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}

