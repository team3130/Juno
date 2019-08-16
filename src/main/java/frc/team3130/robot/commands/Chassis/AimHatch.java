package frc.team3130.robot.commands.Chassis;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Chassis;

/**
 *
 */
public class AimHatch extends Command {
    public AimHatch() {
        requires(Chassis.GetInstance()) ;
        //requires();
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
        //if(OI.driverGamepad.getRawButton(RobotMap.BTN_AIM_HATCH))

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Chassis.driveStraight(0);
        Chassis.holdAngle(0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(!OI.driverGamepad.getRawButton(RobotMap.BTN_AIM_HATCH) == true)
        {return true;}
        return !OI.driverGamepad.getRawButton(RobotMap.BTN_AIM_HATCH);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
