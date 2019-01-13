package frc.team3130.robot.autoCommands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

/**
 *
 */
public class AutoTurn extends Command {

	private double angle;
	private double thresh;
	
    public AutoTurn() {
    	requires(Chassis.GetInstance());
    }

    /**
     * Sets the turn parameters
     * @param angle in degrees
     * @param threshold in degrees
     */
    public void setParam(double angle, double threshold){
    	this.angle=(Math.PI/180)*angle;
    	thresh=threshold;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	DriverStation.reportWarning("AutoTurn.java command started", false);
    	Chassis.ReleaseAngle();
    	Chassis.HoldAngle(angle);
    	Chassis.GetInstance().setAbsoluteTolerance(thresh);
    	Chassis.DriveStraight(0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Chassis.GetInstance().onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Chassis.ReleaseAngle();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
