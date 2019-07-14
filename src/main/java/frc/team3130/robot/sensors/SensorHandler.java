package frc.team3130.robot.sensors;

import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Elevator;
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

        Elevator.GetInstance().readPeriodicInputs();
        Limelight.updateData();
    }
}
