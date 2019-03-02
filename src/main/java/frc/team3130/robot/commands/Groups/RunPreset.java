package frc.team3130.robot.commands.Groups;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;

/**
 *
 */
public class RunPreset extends Command {

    private RobotMap.Presets thisPreset;

    public RunPreset(RobotMap.Presets thisPreset) {
        //Put in the instance of whatever subsystem u need here
        requires(Elevator.GetInstance());
        requires(Intake.GetInstance());
        this.thisPreset = thisPreset;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Arm.setWristRelativeAngle(thisPreset.getAngle());
        Elevator.setSimpleMotionMagic(thisPreset.getHeight());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Elevator.getHeightOffGround()- thisPreset.getHeight()) < RobotMap.kElevatorFinishDeadband && Math.abs(Arm.getRelativeWristAngle() - thisPreset.getAngle()) < RobotMap.kWristFinishDeadband;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
