package frc.team3130.robot.autoCommands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.subsystems.Chassis;
import org.usfirst.frc.team3130.robot.subsystems.Chassis;

/**
 * From Hiawatha
 */
public class AutoDriveStraightToPoint extends PIDCommand {
	
	private double m_distance;
	private double m_threshold;
	private double m_speed;
	private boolean m_shiftLow;
	
    public AutoDriveStraightToPoint() {
		super(0.1, 0, 0);	//TODO: Tune Pid Numbers
		
        requires(Chassis.GetInstance());
    }

    /**
     * Sets the command's parameters
     * 
     * @param setpoint    distance to travel (inches)
     * @param threshold   how many inches within the setpoint 
     * @param speed		  percentVBus to drive at
     * @param shiftLow	  whether the bot is in high gear or not
     */
    public void SetParam(double setpoint, double threshold, double speed, boolean shiftLow){
    	//System.out.println("Param Set");
    	m_distance = setpoint;
    	m_threshold = threshold;
    	m_speed = speed;
    	m_shiftLow = shiftLow;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	//System.out.println("Entered Drive Straight--------------------------------------------------------------------------------");
    	//System.out.println("dist"+m_distance);
    	//System.out.println("Set Setpoint "+m_distance+Chassis.GetDistance());
    	DriverStation.reportWarning("AutoDriveStraightToPoint.java command started", false);
    	getPIDController().reset();
    	
    	Chassis.ShiftDown(m_shiftLow);
    	Chassis.HoldAngle(0);
    	getPIDController().setSetpoint(m_distance + Chassis.GetDistance());
    	getPIDController().setAbsoluteTolerance(m_threshold);
    	setPID();
        Chassis.TalonsToCoast(false);
    	
    	getPIDController().enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putNumber("Distance", returnPIDInput());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    	SmartDashboard.putBoolean("OnTarget", getPIDController().onTarget());
        if (getPIDController().onTarget()){
   //     		&& Math.abs(Chassis.GetSpeed()) < .25){
        	//System.out.println("FINISHED!");
        	return true;
        }return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//System.out.println("End");
    	Chassis.ReleaseAngle();
    	Chassis.DriveTank(0, 0);
    	getPIDController().disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//System.out.println("Interupted");
    	end();
    }

	@Override
	protected double returnPIDInput() {
		return Chassis.GetDistance();
	}

	@Override
	protected void usePIDOutput(double output) {

		//System.out.println(output);
		//System.out.println("Setpoint"+getPIDController().getSetpoint());
		if(output > m_speed) output = m_speed;
		else if(output < -m_speed) output = -m_speed;
		
		Chassis.DriveStraight(output);
		
		//System.out.println("Using PID");
	}
	
	private void setPID()
	{
		//System.out.println("Set PID");
		getPIDController().setPID(
				Preferences.getInstance().getDouble("DriveStraightP", 0.02), 
				Preferences.getInstance().getDouble("DriveStraightI", 0),
				Preferences.getInstance().getDouble("DriveStraightD", 0.07)
			); /*
		if(!m_shiftHigh){
			getPIDController().setPID(
					0.01, 
					0,
					0.062
				); 
		}else{
			getPIDController().setPID(
					Preferences.getInstance().getDouble("HighGear Auton Drive P", 0.001), 
					Preferences.getInstance().getDouble("HighGear Auton Drive I", 0), 
					Preferences.getInstance().getDouble("HighGear Auton Drive D", 0)
				); 
		}*/
	}
}
