package frc.team3130.robot.autoCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoDelay extends Command {
    private double delay;
    Timer timer = new Timer();
    public AutoDelay(double delay) {
        this.delay = delay;
    }

    public void setParam(double delay){
        this.delay = delay;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer.reset();
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() > delay;
    }

    // Called once after isFinished returns true
    protected void end() {
        timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
