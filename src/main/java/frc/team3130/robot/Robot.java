package frc.team3130.robot;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.autoCommands.DriveOffPlatform;
import frc.team3130.robot.sensors.SensorHandler;
import frc.team3130.robot.subsystems.*;
import frc.team3130.robot.vision.Limelight;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  Command autonomousCommand = null;
  private SendableChooser<String> chooser  = new SendableChooser<String>();
  public static SendableChooser<String> startPos = new SendableChooser<String>();


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    //Instantiate driver station input interface
    OI.GetInstance();

    //Instantiate subsystems
    Chassis.GetInstance();
    Intake.GetInstance();
    Climber.GetInstance();
    Elevator.GetInstance();

    //Instantiate sensors
    SensorHandler.GetInstance();
    Limelight.GetInstance();

    //Auton mode chooser
    chooser.setDefaultOption("Default Auto", "Default Auto");
    chooser.addOption("Drive Off Platform", "Drive Off Platform");
    chooser.addOption("No Auton", "No Auton");

    SmartDashboard.putData("Auto mode", chooser);

    //Starting position of robot
    //      NOTE: If hardcoding required, manually choose fieldSide below
    startPos.setDefaultOption("Left Start Pos", "Left");
    startPos.addOption("Right Start Pos", "Right");

    SmartDashboard.putData("Starting position", startPos);

    Thread t = new Thread(() -> {
      while (!Thread.interrupted()) {
        try {
          Thread.sleep(200);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        outputToSmartDashboard();
      }
    });
    t.start();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    Elevator.resetElevator();
  }

  @Override
  public void disabledPeriodic() {
    SensorHandler.updateSensors();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() { }


  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    Elevator.resetElevator();
    Arm.resetArm();
    //determine the auton to run
    //determineAuton();
    //start that command
    /*
    if (autonomousCommand != null) {
      autonomousCommand.start();
    }*/
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
    SensorHandler.updateSensors();
  }


  /**
   * This function is run once when teleop is first started.
   */
  @Override
  public void teleopInit() {
    /* This makes sure that the autonomous stops running when teleop starts running. If you want the autonomous to
       continue until interrupted by another command, remove this line or comment it out.
    */
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
    Elevator.resetElevator();
    Arm.resetArm();
  }


  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    SensorHandler.updateSensors();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  private void determineAuton() {
    autonomousCommand = null;

    String start = startPos.getSelected();
    if(start == null) {
      DriverStation.reportError("startPos selector returned NULL", false);
      return;
    }
    String theChosenOne = chooser.getSelected();
    if(theChosenOne == null) {
      DriverStation.reportError("Auton chooser returned NULL", false);
      return;
    }

    switch(theChosenOne){
      case "Drive Off Platform":
        autonomousCommand = new DriveOffPlatform();
        break;
        /*
      case "Cargo":
        if(start.equals("Left")) {
          autonomousCommand = new CargoLeft();
        }
        else {
          autonomousCommand = new CargoRight();
        }
        break;
      case "Rocket":
        if(start.equals("Left")) {
          autonomousCommand = new RocketLeft();
        }
        else{
          autonomousCommand = new RocketRight();
        }
        */
      case "No Auto":
        autonomousCommand = null;
        break;

      default:
        autonomousCommand = null;

    }
  }


  public void outputToSmartDashboard(){
    Limelight.outputToSmartDashboard();
    Elevator.outputToSmartDashboard();
    Arm.GetInstance().outputToSmartDashboard();
    Chassis.outputToSmartDashboard();
  }
}
