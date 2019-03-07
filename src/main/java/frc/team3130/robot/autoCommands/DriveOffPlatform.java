package frc.team3130.robot.autoCommands;

import frc.team3130.robot.commands.Chassis.OpenLoopDrive;
import frc.team3130.robot.commands.Elevator.ElevatorToHeight;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveOffPlatform extends CommandGroup {

    private OpenLoopDrive driveForward;
    private ElevatorToHeight          elevatorUp;

    public DriveOffPlatform() {
        requires(Chassis.GetInstance());
        requires(Elevator.GetInstance());

        driveForward = new OpenLoopDrive(-0.3, -0.3, 4.0);

        addSequential(driveForward, 5.0); //TODO: tune auton routine
    }

    @Override
    protected void initialize() {
    }

}

