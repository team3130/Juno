package frc.team3130.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;
import frc.team3130.robot.subsystems.Elevator;

/**
 *
 */
public class RunArm extends Command {
    private boolean changeElbow = false;
    private double oldWrist = 180.0;

    public RunArm() {
        requires(Arm.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        changeElbow = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double stick = OI.weaponsGamepad.getRawAxis(RobotMap.LST_AXS_RJOYSTICKY);
        if (Math.abs(stick) >= 0.05 ){
            if(!changeElbow){
                oldWrist = Arm.getAbsoluteWristAngle();
            }
            double moveSpeed = 0.8 * stick;
            Arm.runElbow(moveSpeed);
            changeElbow = true;
        } else if (changeElbow){
            Arm.holdAngleElbow();
            changeElbow = false;
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
