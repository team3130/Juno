package frc.team3130.robot.commands.Climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Climber;

/**
 *
 */
public class LegDrive extends Command {

    public LegDrive(){
        requires(Climber.GetInstance());
    }


    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Climber.driveLeg(OI.weaponsGamepad.getRawAxis(RobotMap.AXS_DRIVE_LEG));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Climber.driveLeg(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
