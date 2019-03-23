package frc.team3130.robot.sensors;

import edu.wpi.first.wpilibj.Timer;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;
import frc.team3130.robot.subsystems.Chassis;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;
import frc.team3130.robot.util.Epsilon;
import frc.team3130.robot.vision.Limelight;

public class SensorHandler {
    //Instance Handling
    private static SensorHandler m_pInstance;
    public static SensorHandler GetInstance() {
        if (m_pInstance == null) m_pInstance = new SensorHandler();
        return m_pInstance;
    }

    private static boolean lastHatch;
    private static boolean lastBall;
    private static boolean lastElevator;
    private static boolean isLast;
    private static boolean isLastE;
    private static double startTime;

    public SensorHandler(){
        lastBall = false;
        lastHatch = false;
        lastElevator = false;
        isLast = false;
        isLastE = false;
    }

    public static void updateSensors(){
        //Intake
        boolean ballSensor = false/*ball sensor*/; //TODO: read in sensors
        boolean hatchSensor = false /*hatch sensor*/;
        if(ballSensor && ballSensor != lastBall){
            Intake.GetInstance().setState(Intake.IntakeState.HasBall);
            lastBall = true;
            lastHatch = false;
        }else if(hatchSensor && hatchSensor != lastHatch){
            Intake.GetInstance().setState(Intake.IntakeState.HasHatch);
            lastHatch = true;
            lastBall = false;
        }else{
            Intake.GetInstance().setState(Intake.IntakeState.Empty);
            lastHatch = false; lastBall = false;
        }

        //Elevator
        if(Elevator.isRevLimitClosed()){
            if(!lastElevator){
                Elevator.zeroSensors();
                Elevator.setZeroedState(true);
                lastElevator = true;
            }
        }
        else{
            if(lastElevator)
                lastElevator = false;
        }

        //Wrist
        if(!Arm.hasBeenZeroed() && Arm.isRevLimitClosed()){
                Arm.zeroSensors(RobotMap.kWristHomingAngle);
                Arm.setZeroedState(true);
        }

        if(Epsilon.epsilonEquals(OI.driverGamepad.getRawAxis(1), 0.0, RobotMap.kDriveDeadband) && !isLast){
            startTime = Timer.getFPGATimestamp();
            isLast = true;
        }
        if(Chassis.GetInstance().getSpeedR() > 115.0 && !isLastE){
            Chassis.GetInstance().maxAccel = Timer.getFPGATimestamp() - startTime;
            isLastE = true;
        }
        Elevator.GetInstance().readPeriodicInputs();
        Arm.GetInstance().readPeriodicInputs();
        OI.GetInstance().checkTriggers();
        Limelight.updateData();
    }
}
