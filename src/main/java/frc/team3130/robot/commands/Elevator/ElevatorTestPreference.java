package frc.team3130.robot.commands.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Elevator;

/**
 *
 */
public class ElevatorTestPreference extends Command {


    public ElevatorTestPreference() {
    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Elevator.setSimpleMotionMagic(Preferences.getInstance().getDouble("Elevator Test", 8.5));
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Elevator.getHeightOffGround()- Preferences.getInstance().getDouble("Elevator Test", 8.5)) < RobotMap.kElevatorFinishDeadband;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
