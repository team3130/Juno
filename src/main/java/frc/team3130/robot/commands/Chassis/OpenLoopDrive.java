package frc.team3130.robot.commands.Chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Chassis;

/**
 *
 */
public class OpenLoopDrive extends Command {

    private double startTime;
    private double left, right, duration;

    /**
     * Drive the chassis motors in open loop for a given duration.
     * @param leftVbus Percent output of the left drive motor
     * @param rightVbus Percent output of the right drive motor
     * @param duration How long to drive in seconds
     */
    public OpenLoopDrive(double leftVbus, double rightVbus, double duration) {
        //Put in the instance of whatever subsystem u need here
        requires(Chassis.GetInstance());
        left = leftVbus;
        right = rightVbus;
        this.duration = duration;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Chassis.driveTank(left, right, false);
        startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime > duration;
    }

    // Called once after isFinished returns true
    protected void end() {
        Chassis.driveTank(0, 0, false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
