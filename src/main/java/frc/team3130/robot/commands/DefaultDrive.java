/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team3130.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.OI;
import frc.team3130.robot.subsystems.Chassis;

public class DefaultDrive extends Command {
  public DefaultDrive() {
    // Use requires() here to declare subsystem dependencies
    requires(Chassis.GetInstance());
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double moveSpeed = -OI.gamepad.getRawAxis(1); //joystick's y axis is inverted
    double turnSpeed = OI.gamepad.getRawAxis(4); //arcade drive has left as positive, but we want right to be positive
    
    double turnThrottle = (0.5 * OI.stickR.getRawAxis(3)-0.5);
    Chassis.DriveArcade(moveSpeed, turnSpeed, false);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
