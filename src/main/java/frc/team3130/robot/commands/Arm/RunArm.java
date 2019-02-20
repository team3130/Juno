package frc.team3130.robot.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;
import frc.team3130.robot.subsystems.Elevator;

/**
 *
 */
public class RunArm extends Command {

    private double oldWrist = 180.0;

    public RunArm() {
        requires(Arm.GetInstance());
    }

    // Called just before this Command runs the first time


    // Called repeatedly when this Command is scheduled to run
    protected void execute() {


    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Elevator.resetElevator();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
