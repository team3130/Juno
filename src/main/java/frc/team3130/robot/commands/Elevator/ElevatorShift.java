package frc.team3130.robot.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;

public class ElevatorShift extends Command {
    public ElevatorShift() {
        //Put in the instance of whatever command u need here
        //requires();
        requires(Elevator.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        boolean thisShift = Elevator.getShift();
        Elevator.shift(!thisShift);

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
