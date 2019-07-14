/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team3130.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Add your docs here.
 */
public class BasicTalonSRX extends Subsystem {

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

private WPI_TalonSRX mc_spinnyMotor;

  public void initDefaultCommand() {
      // Set the default command for a subsystem here.
      //setDefaultCommand(new MySpecialCommand());
  }
  
  public BasicTalonSRX(int CAN_id, String subsystem, String item)
  {
    mc_spinnyMotor = new WPI_TalonSRX(CAN_id);
    //LiveWindow.addActuator(subsystem, item, mc_spinnyMotor);
  }
  
  /**
   * Spins a motor
   * <p>Controls the speed of the motor, taking a value from -1 to 1 as a percentage of available power to pass on</p>
   * @param percentage the percentage of the voltage being fed to the controlers to pass on to the motors.
   */
  public void spinMotor(double percentage)
  {
    mc_spinnyMotor.set(percentage);
  }
  
  /**
   * Returns the speed of the motor
   * 
   * <p>Returns the same value that is set by spinMotor, the percentage of voltage getting through the CANTalon</p>
   * @return the percentage of the voltage being fed to the controler that is passed to the motor.
   */
  public double getPercent()
  {
    return mc_spinnyMotor.get();
  }
  
  /**
   * Returns the current of the motor
   * 
   *
   * @return the percentage of the current being fed to the controller that is passed to the motor.
   */
  public double getCurrent()
  {
    return mc_spinnyMotor.getOutputCurrent();
  }
}
