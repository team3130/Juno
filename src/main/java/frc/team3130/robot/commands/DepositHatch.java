package frc.team3130.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Arm;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;

/**
 *
 */
public class DepositHatch extends Command {
    public DepositHatch() {
        //Put in the instance of whatever subsystem u need here
        requires(Arm.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Intake.openClamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Elevator.rawElevator(-0.3);
        Arm.runWrist(0.3);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
        Intake.closeClamp();
        Arm.holdAngleWrist();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
