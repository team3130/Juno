package frc.team3130.robot.commands.Arm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;

/**
 *
 */
public class ZeroArm extends Command {

    private Timer timer;

    public ZeroArm() {
        //Put in the instance of whatever subsystem u need here
        requires(Arm.GetInstance());

        timer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer.reset();
        Arm.setZeroedState(false);
        timer.start();
        Arm.runWrist(-0.15);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Arm.hasBeenZeroed() || timer.get() >= RobotMap.kWristZeroTimeout;
    }

    // Called once after isFinished returns true
    protected void end() {
        timer.stop();
        if(!Arm.hasBeenZeroed()){ //Limit switch was never triggered, assume the Wrist made it all the way back to 90 degrees
            Arm.zeroSensors(RobotMap.kWristBackwardMax);
        }
        Arm.setWristRelativeAngle(90.0); //Go to stowed position
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
