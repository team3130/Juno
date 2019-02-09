package frc.team3130.robot.autoCommands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.commands.ElevatorToHeight;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveOffPlatform extends CommandGroup {

    private AutoDriveStraightToPoint  driveForward;
    private ElevatorToHeight          elevatorUp;

    public DriveOffPlatform() {
        requires(Chassis.GetInstance());
        requires(Elevator.GetInstance());

        driveForward = new AutoDriveStraightToPoint();
        elevatorUp   = new ElevatorToHeight(4.0);

        addSequential(elevatorUp, 2);
        addSequential(driveForward, 5); //TODO: tune auton routine
    }

    @Override
    protected void initialize() {
        driveForward.SetParam(
                Preferences.getInstance().getDouble("BaseDistance", 200.0),
                20,
                Preferences.getInstance().getDouble("BaseForwardSpeed", .5),
                false);
        elevatorUp.setParam(32);
    }

}

