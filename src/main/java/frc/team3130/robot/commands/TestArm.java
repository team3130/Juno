package frc.team3130.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Arm;

public class TestArm extends Command {
    public TestArm() {
        //Put in the instance of whatever subsystem u need here
        requires(Arm.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Arm.setWristRelativeAngle(Preferences.getInstance().getDouble("Wrist Test", 180.0));
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        //Arm.runWrist(Preferences.getInstance().getDouble("Wrist Test", 0.0));

        Arm.runWrist(Preferences.getInstance().getDouble("Wrist Test", 0.0));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {

        Arm.runWrist(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
