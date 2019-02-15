package frc.team3130.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class BlinkinInterface extends Subsystem {
	private Spark blinkin1;
	
	
    private static BlinkinInterface m_pInstance;
    public static BlinkinInterface GetInstance(){
    	if(m_pInstance == null) m_pInstance = new BlinkinInterface();
		return m_pInstance;
    }
    
    private BlinkinInterface(){
    	blinkin1 = new Spark(0);
    	blinkin1.set(0.67);
    }
    
    public void defaultPattern(){
    	GetInstance().blinkin1.set(0.97/*Preferences.getInstance().getDouble("LEDtest", 0.19)*/);
    }
    
    public void setPattern(double pattern){
    	blinkin1.set(pattern);
    }
    
    public static void showRange(double range) {
    	if (range < 0) {
    		GetInstance().setPattern(-0.97);
    	}
    	else if (range > 200) {
    		GetInstance().defaultPattern();
    	}
    	else if (range > 140) {
    		GetInstance().setPattern(0.61);
    	}
    	else {
    		GetInstance().setPattern(0.77);
    	}
    }

    /*
    public static void gotCube(){
    	for (int i = 0; i < 3; i++){
    		GetInstance().blinkin1.set(0.77);
	    	try{
	    		Thread.sleep(300);
	    	}catch(Exception e){}
	    	GetInstance().blinkin1.set(0.99);
	    	try{
	    		Thread.sleep(300);
	    	}catch(Exception e){}
    	}
    	defaultPattern();
    }
    */
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new LEDtest());
    }
}

