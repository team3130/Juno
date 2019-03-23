package frc.team3130.robot.commands.Elevator;

import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.util.Util;

/**
 *
 */
public class RunElevator extends Command {
    private boolean changeHeight = false;

    public RunElevator() {
        requires(Elevator.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        changeHeight = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double stick = -OI.weaponsGamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY);
        if (Math.abs(stick) >= RobotMap.kElevatorManualDeadband ){
            double moveSpeed = RobotMap.kElevatorManualMultipler * Util.applyDeadband(stick, RobotMap.kElevatorManualDeadband);
            Elevator.runElevator(moveSpeed);
            changeHeight = true;
        } else if (changeHeight){
            Elevator.holdHeight();
            changeHeight = false;
        }
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
