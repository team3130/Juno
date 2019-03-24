package frc.team3130.robot.commands.Groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team3130.robot.autoCommands.AutoDelay;
import frc.team3130.robot.commands.Elevator.ElevatorToHeight;
import frc.team3130.robot.commands.Intake.ExtendTongue;
import frc.team3130.robot.commands.Intake.TongueToggle;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;

public class DepositHatch extends CommandGroup {

    private ExtendTongue            extendTongue;
    private ElevatorToHeight        elevatorMovement;
    private AutoDelay               delay;

    public DepositHatch() {
        requires(Intake.GetInstance());
        requires(Elevator.GetInstance());

        extendTongue = new ExtendTongue();
        delay = new AutoDelay(0.5);
        elevatorMovement = new ElevatorToHeight(Elevator.getHeightOffGround() - 3.0);

        addSequential(extendTongue, 0.2);
        addSequential(delay, 0.5);
        addSequential(elevatorMovement, 1);
    }

    @Override
    protected void initialize() {

    }

}

