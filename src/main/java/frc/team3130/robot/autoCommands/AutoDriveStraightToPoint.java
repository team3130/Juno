package frc.team3130.robot.autoCommands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.team3130.robot.subsystems.Chassis;


public class AutoDriveStraightToPoint extends PIDCommand {

    private double m_distance;
    private double m_threshold;
    private double m_speed;
    private boolean m_shiftLow;

    public AutoDriveStraightToPoint() {
        super(0.1, 0, 0);
        requires(Chassis.GetInstance());
    }

    public void SetParam(double setpoint, double threshold, double speed, boolean shiftLow){
        //System.out.println("Param Set");
        m_distance = setpoint;
        m_threshold = threshold;
        m_speed = speed;
        m_shiftLow = shiftLow;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        getPIDController().reset();

        Chassis.shift(m_shiftLow);
        Chassis.holdAngle(0);
        Chassis.talonsToCoast(false);
        setPID();
        getPIDController().setSetpoint(m_distance + Chassis.getDistance());
        getPIDController().setAbsoluteTolerance(m_threshold);

        getPIDController().enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (getPIDController().onTarget()){
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

    @Override
    protected double returnPIDInput() {
        return Chassis.getDistance();
    }

    @Override
    protected void usePIDOutput(double output) {
        if(output > m_speed) output = m_speed;
        else if(output < -m_speed) output = -m_speed;

    }

    private void setPID()
    {
        getPIDController().setPID(
                Preferences.getInstance().getDouble("DriveStraightP", 0.02),
                Preferences.getInstance().getDouble("DriveStraightI", 0),
                Preferences.getInstance().getDouble("DriveStraightD", 0.07)
        );
    }

}