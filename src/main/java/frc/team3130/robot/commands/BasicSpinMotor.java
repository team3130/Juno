/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team3130.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team3130.robot.subsystems.BasicTalonSRX;

/**
 *
 */
public class BasicSpinMotor extends Command {

	private double percent;
	private BasicTalonSRX motor;
	
	/**
	 * Spins a motor
	 * 
	 * <p>Takes a BasicCANTalon and a percentage, and drives the motor at that percentage. 
	 * It keeps spinning it until another command sets the motor to off</p>
	 * @param motor the BasicCANTalon to work with
	 * @param percentage the percentage of the voltage supplied to the talon to pass onto the motor
	 */
    public BasicSpinMotor(BasicTalonSRX motor, double percentage) {
        requires(motor);
        percent = percentage;
        this.motor=motor;
    }
    
    public void ChangePercent(double percentage)
    {
    	percent = percentage;
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    	motor.spinMotor(percent);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
	    motor.spinMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}