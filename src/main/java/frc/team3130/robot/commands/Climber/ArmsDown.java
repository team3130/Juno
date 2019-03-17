package frc.team3130.robot.commands.Climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.subsystems.Climber;
import frc.team3130.robot.RobotMap;

public class ArmsDown extends Command {
    public ArmsDown() {
        //Put in the instance of whatever command u need here
        requires(Climber.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(OI.driverGamepad.getRawButton(RobotMap.BTN_CONFIRM_DRIVER) && OI.weaponsGamepad.getRawButton(RobotMap.BTN_CONFIRM_WEAPONS))
            Climber.deployArms();
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
