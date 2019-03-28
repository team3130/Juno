package frc.team3130.robot.autoCommands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.commands.Chassis.OpenLoopDrive;
import frc.team3130.robot.commands.Elevator.ElevatorToHeight;
import frc.team3130.robot.commands.Groups.DepositHatch;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;
import frc.team3130.robot.tantanDrive.Paths.PathStore;
import frc.team3130.robot.tantanDrive.RunMotionProfile;

/**
 *
 */
public class RightFrontCargoshipHatch extends CommandGroup {

	private ElevatorToHeight elevatorToHeight;
	private RunMotionProfile toCargoship;
	private DepositHatch depositHatch;
	private OpenLoopDrive backUp;

	public RightFrontCargoshipHatch() {
		requires(Chassis.GetInstance());
		requires(Intake.GetInstance());
		requires(Elevator.GetInstance());
		
		elevatorToHeight = new ElevatorToHeight(RobotMap.Presets.LowestTongue.getHeight());
		toCargoship = new RunMotionProfile(PathStore.Paths.RightCargoF);
		depositHatch = new DepositHatch();
		backUp = new OpenLoopDrive(-0.5,-0.5, 0.7);

		addParallel(elevatorToHeight, 3);
		addSequential(toCargoship, 5);
		addSequential(depositHatch, 2);
		addSequential(backUp, 1);
	}

	@Override
	protected void initialize(){

	}
}

