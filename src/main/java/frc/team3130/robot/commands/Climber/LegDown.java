package frc.team3130.robot.commands.Climber;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Climber;

/**
 *
 */
public class LegDown extends Command {
    private boolean default1;

    public LegDown(){
        default1=false;
        requires(Climber.GetInstance());
    }

    public LegDown(boolean def) {
        this.default1=def;
        requires(Climber.GetInstance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(default1){
            Climber.downLeg(-.5);
        }else{
            Climber.downLeg(OI.weaponsGamepad.getRawAxis(RobotMap.AXS_DROP_LEG));
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Climber.downLeg(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
