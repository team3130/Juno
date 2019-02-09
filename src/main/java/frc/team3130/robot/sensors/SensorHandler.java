package frc.team3130.robot.sensors;

import frc.team3130.robot.subsystems.Elevator;
import frc.team3130.robot.subsystems.Intake;

public class SensorHandler {
    //Instance Handling
    private static SensorHandler m_pInstance;
    public static SensorHandler GetInstance() {
        if (m_pInstance == null) m_pInstance = new SensorHandler();
        return m_pInstance;
    }

    private static boolean lastHatch;
    private static boolean lastBall;

    public SensorHandler(){
        lastBall = false;
        lastHatch = false;
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
            if(!Elevator.hasBeenZeroed()){
                Elevator.zeroSensors();
            }
        }
        else{
            if(Elevator.hasBeenZeroed())
                Elevator.setZeroedState(false);
        }
    }
}
