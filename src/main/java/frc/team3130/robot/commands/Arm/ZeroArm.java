package frc.team3130.robot.commands.Arm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;

/**
 *
 */
public class ZeroArm extends Command {

    private double startTime;

    public ZeroArm() {
        //Put in the instance of whatever subsystem u need here
        requires(Arm.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        startTime = Timer.getFPGATimestamp();
        Arm.setZeroedState(false);
        Arm.runWrist(-0.2);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Arm.hasBeenZeroed() || (Timer.getFPGATimestamp() - startTime) > RobotMap.kWristZeroTimeout;
    }

    // Called once after isFinished returns true
    protected void end() {
        if(!Arm.hasBeenZeroed()){ //Limit switch was never triggered, assume the Wrist made it all the way back to 90 degrees
            Arm.zeroSensors(RobotMap.kWristHomingAngle);
        }
        Arm.setWristRelativeAngle(RobotMap.kWristHomingAngle); //Go to stowed position
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
