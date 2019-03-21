/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team3130.robot.commands.Arm;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.PIDCommand;
import frc.team3130.robot.OI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Arm;

public class WristManual extends PIDCommand {
  public WristManual() {
    super("Wrist Manual" ,
      Preferences.getInstance().getDouble("Wrist P", 1),
      Preferences.getInstance().getDouble("Wrist I", 0),
      Preferences.getInstance().getDouble("Wrist D", 0));
    requires(Arm.GetInstance());
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    getPIDController().enable();
    setSetpoint(Arm.getPos());
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double val = -OI.weaponsGamepad.getRawAxis(RobotMap.AXS_WRIST);

    if(Math.abs(val) < Preferences.getInstance().getDouble("Wrist Deadband", 0.1)){
      getPIDController().enable();
      setSetpoint(Arm.getPos());
    }else{
      getPIDController().disable();
      Arm.runWristPVbus(0.8*val);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    getPIDController().disable();
    Arm.runWristPVbus(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void usePIDOutput(double i){
      Arm.runWristPVbus(0.8*i);
  }  

  @Override
  protected double returnPIDInput(){
      return Arm.getPos();
  }
}
