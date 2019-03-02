package frc.team3130.robot.commands.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Elevator;

/**
 *
 */
public class ElevatorToHeight extends Command {

	private double dist;
	
    public ElevatorToHeight(double dist) {
    	this.dist = dist;
        requires(Elevator.GetInstance());
    }

    public void setParam(double dist){
    	this.dist = dist;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Elevator.setSimpleMotionMagic(dist);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Elevator.getHeightOffGround()-dist) < RobotMap.kElevatorFinishDeadband;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
