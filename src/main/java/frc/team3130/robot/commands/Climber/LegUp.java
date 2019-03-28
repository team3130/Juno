package frc.team3130.robot.commands.Climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Climber;

/**
 *
 */
public class LegUp extends Command {
    public LegUp() {
        //Put in the instance of whatever subsystem u need here
        requires(Climber.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(Climber.isClimbEnabled()) Climber.downLeg(-0.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Climber.downLeg(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
