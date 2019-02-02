package frc.team3130.robot;


import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.subsystems.MotorizedClimber;
import frc.team3130.robot.subsystems.PistonClimber;
import frc.team3130.robot.subsystems.Intake;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //public static final LimeLight _limelight = new LimeLight();
  //public static final LimeLight _limelight = new LimeLight("NetworkTable Key");  //If you renamed your limelight ex: limelight-custome

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    OI.GetInstance();
  
    Chassis.GetInstance();
    Intake.GetInstance();
    PistonClimber.GetInstance();
    MotorizedClimber.GetInstance();

    CameraServer.getInstance().addAxisCamera("10.31.30.12");
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
  public void robotPeriodic() {
  }/*
  //Getters
        SmartDashboard.putBoolean("Target Found", _limelight.getIsTargetFound());
        SmartDashboard.putNumber("Deg Rotation to Target", _limelight.getdegRotationToTarget());
  //.....and MORE

  //Setters
        _limelight.setPipeline(1);
        _limelight.setLEDMode(LedMode.kforceOff);
        _limelight.setCamMode(CamMode.kdriver);
        _limelight.setSnapshot(LocatorEx.Snapshot.kon);
        _limelight.setStream(StreamType.kPiPMain);
        */
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
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopInit() {
    
  }

  /**
   * This function is called periodically during operator control.
   */

  
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
