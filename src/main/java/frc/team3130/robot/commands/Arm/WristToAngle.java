package frc.team3130.robot.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;

public class WristToAngle extends Command {

    private double angle;

    public WristToAngle(double angle) {
        //Put in the instance of whatever subsystem u need here
        requires(Arm.GetInstance());
        this.angle = angle;
    }

    public void setParam(double angle){
        this.angle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Arm.setWristRelativeAngle(angle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Arm.getRelativeWristAngle() - angle) < RobotMap.kWristFinishDeadband;
    }

    // Called once after isFinished returns true
    protected void end() {

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
