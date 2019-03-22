package frc.team3130.robot.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;

/**
 *
 */
public class RunWrist extends Command {
    private boolean holdAngle = false;

    public RunWrist() {
        requires(Arm.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        holdAngle = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double stick = OI.weaponsGamepad.getRawAxis(RobotMap.LST_AXS_LJOYSTICKY);
        if (Math.abs(stick) >= RobotMap.kWristManualDeadband ){
            double moveSpeed = RobotMap.kWristManualMultipler * stick * Math.abs(stick);
            Arm.runWrist(moveSpeed);
            holdAngle = true;
        } else if (holdAngle){
            Arm.holdAngleWrist();
            holdAngle = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Arm.resetArm();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
