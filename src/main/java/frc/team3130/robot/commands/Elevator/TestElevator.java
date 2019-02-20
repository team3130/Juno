package frc.team3130.robot.commands.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Elevator;

public class TestElevator extends Command {
    public TestElevator() {
        //Put in the instance of whatever subsystem u need here
        requires(Elevator.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Elevator.rawElevator(Preferences.getInstance().getDouble("Elevator Test", 0.0));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Elevator.rawElevator(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
