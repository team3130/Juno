package frc.team3130.robot.autoCommands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team3130.robot.commands.Chassis.OpenLoopDrive;
import frc.team3130.robot.commands.Groups.DepositHatch;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;
import frc.team3130.robot.tantanDrive.Paths.RightCargoF;
import frc.team3130.robot.tantanDrive.RunMotionProfile;

/**
 *
 */
public class RightFrontCargoshipHatch extends CommandGroup {

	private RunMotionProfile toCargoship;
	private DepositHatch depositHatch;
	private OpenLoopDrive backUp;

	public RightFrontCargoshipHatch() {
		requires(Chassis.GetInstance());
		requires(Intake.GetInstance());
		requires(Elevator.GetInstance());
		

		toCargoship = new RunMotionProfile(new RightCargoF());
		depositHatch = new DepositHatch();
		backUp = new OpenLoopDrive(-0.5,-0.5, 0.7);
		
		addSequential(toCargoship, 5);
		addSequential(depositHatch, 2);
		addSequential(backUp, 1);
	}

	@Override
	protected void initialize(){

	}
}

