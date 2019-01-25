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

	//Constants

	//Elevator
	//TODO: Actually find these values
	public static double kElevatorBias = 0.0;
	public static double kElevatorSlowZone = 0.0;

	public static double kElevatorP = 0.0;
	public static double kElevatorI = 0.0;
	public static double kElevatorD = 0.0;
	public static double kElevatorF = 0.0;

	public static double kElevatorTicksPerInch = (4.0 * 1024.0) / (4.0 * Math.PI); //1024 ticks per revolution of shaft



	//CAN IDs
	public static final int CAN_PNMMODULE = 1;

    public static final int CAN_LEFTMOTORFRONT = 2;
	public static final int CAN_LEFTMOTORREAR = 3;
	public static final int CAN_RIGHTMOTORFRONT = 4;
	public static final int CAN_RIGHTMOTORREAR = 5;

	public static final int CAN_TESTINTAKE1 = 6;
	public static final int CAN_TESTINTAKE2 = 7;

	public static final int CAN_CLIMBER1 = 8;
	public static final int CAN_CLIMBER2 = 9;
	public static final int CAN_CLIMBER3 = 10;

	public static final int CAN_ELEVATOR1 = 11;
	public static final int CAN_ELEVATOR2 = 12;


}