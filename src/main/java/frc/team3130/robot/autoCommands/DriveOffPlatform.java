package frc.team3130.robot.autoCommands;

import frc.team3130.robot.commands.ElevatorToHeight;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveOffPlatform {
    /**
     * Colin Drive system to pass the platform
     */
    public class OffPlatform extends CommandGroup {

        private AutoDriveStraightToPoint  driveForward;
        private ElevatorToHeight          elevatorUp;

        public OffPlatform() {
            requires(Chassis.GetInstance());
            requires(Elevator.GetInstance());

            driveForward = new AutoDriveStraightToPoint();
            elevatorUp   = new ElevatorToHeight(4.0);

            addSequential(elevatorUp, 2);
            addSequential(driveForward, 5);
        }

        @Override
        protected void initialize() {
            //System.out.println("Running PB");
            driveForward.SetParam(
                    Preferences.getInstance().getDouble("BaseDistance", 200.0),
                    20,
                    Preferences.getInstance().getDouble("BaseForwardSpeed", .5),
                    false);
            elevatorUp.setParam(32);
        }

    }

}
