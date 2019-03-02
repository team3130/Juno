package frc.team3130.robot.commands.Groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team3130.robot.autoCommands.AutoDelay;
import frc.team3130.robot.commands.Elevator.ElevatorToHeight;
import frc.team3130.robot.commands.Intake.TongueToggle;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;

public class TongueHatch extends CommandGroup {

    private TongueToggle            toggleTongue;
    private ElevatorToHeight        elevatorMovement;
    private AutoDelay               delay;

    public TongueHatch() {
        requires(Intake.GetInstance());
        requires(Elevator.GetInstance());

        toggleTongue = new TongueToggle();
        if(Intake.getTongue()) {
            elevatorMovement = new ElevatorToHeight(Elevator.getHeightOffGround() - 5.0);
            delay = new AutoDelay(0.05);
            addSequential(toggleTongue, 0.3);
            addSequential(delay, 0.05);
            addSequential(elevatorMovement, 1);
        }else{
            elevatorMovement = new ElevatorToHeight(Elevator.getHeightOffGround() + 5.0);
            delay = new AutoDelay(0.6);
            addSequential(elevatorMovement, 1);
            addSequential(delay, 1.0);
            addSequential(toggleTongue, 0.3);
        }


    }

    @Override
    protected void initialize() {
        if(Intake.getTongue()) {
            elevatorMovement.setParam(Elevator.getHeightOffGround() + 5.0);
            delay.setParam(0.6);
        }else {
            elevatorMovement.setParam(Elevator.getHeightOffGround() - 5.0);
            delay.setParam(0.05);
        }


    }

}

