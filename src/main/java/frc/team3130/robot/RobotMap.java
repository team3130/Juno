/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team3130.robot;

import edu.wpi.first.wpilibj.Preferences;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static final int CAN_PNMMODULE = 1;

    public static final int CAN_LEFTMOTORFRONT = 2;
	public static final int CAN_LEFTMOTORREAR = 3;
	public static final int CAN_RIGHTMOTORFRONT = 4;
	public static final int CAN_RIGHTMOTORREAR = 5;

	public static final int CAN_TESTINTAKE1 = 6;
	public static final int CAN_TESTINTAKE2 = 7;
	
}