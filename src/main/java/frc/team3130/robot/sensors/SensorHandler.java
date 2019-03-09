package frc.team3130.robot.sensors;

import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;
import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;
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

    public SensorHandler(){
        lastBall = false;
        lastHatch = false;
        lastElevator = false;
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
        if(!Arm.hasBeenZeroed() && Arm.isFwdLimitClosed()){
                Arm.zeroSensors(RobotMap.kWristHomingAngle);
                Arm.setZeroedState(true);
        }

        Elevator.GetInstance().readPeriodicInputs();
        Arm.GetInstance().readPeriodicInputs();
        OI.GetInstance().checkTriggers();
        Limelight.updateData();
    }
}
