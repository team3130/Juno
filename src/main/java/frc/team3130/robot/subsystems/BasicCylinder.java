/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team3130.robot.subsystems;

import frc.team3130.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Add your docs here.
 */
public class BasicCylinder extends Subsystem {

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public void initDefaultCommand() {
      // Set the default command for a subsystem here.
      //setDefaultCommand(new MySpecialCommand());
  }
  
  private Solenoid pnm_actuator;
  
  public BasicCylinder(int PNM_port, String subsystem, String item)
  {
    pnm_actuator = new Solenoid(RobotMap.CAN_PNMMODULE, PNM_port);
  }
  
  /**
   * Actuates a cylinder
   * <p>Controls the movement of the cylinder. The value passed to the function changes the state of the cylinder, 
   * with true extending the cylinder, and false retracting it.
   * </p>
   * @param extend to extend the cylinder or not, true extends, false retracts
   */
  public void actuate(boolean extend)
  {
    pnm_actuator.set(extend);
  }
  
  /**
   * Returns the current state of the solenoid
   * <p>Returns true when the cylinder is extended, and false when it is retracted</p>
   * @return the solenoid state
   */
  public boolean getState()
  {
    return pnm_actuator.get();
  }
  
  /**
   * Sets the solenoid to the state it currently isn't in.
   * <p>Gets the current state of the solenoid and sets it to the opposite of that.</p>
   */
  public void toggleState()
  {
    pnm_actuator.set(!pnm_actuator.get());
  }
}
