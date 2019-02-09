/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team3130.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	/**
	 *Constants
	 */
		//Chassis
		//TODO: Get the actual values, using janky values bots right now
		public static double kChassisWidth = 23.25; //Distance between the left and right middle wheels
		public static double kLWheelDiameter = 3.0; //Center wheel
		public static double kRWheelDiameter = 3.0;	//Center wheel
		public static double kDriveCodesPerRev = 2048.0;

		//Intake
		public static double armLength = 7.70652; //Distance from centers of pivot points

		//Elevator
		//TODO: Actually find these values
		public static double kElevatorBias = 0.0;
		public static double kElevatorSlowZone = 0.0;

		public static double kElevatorP = 0.0;
		public static double kElevatorI = 0.0;
		public static double kElevatorD = 0.0;
		public static double kElevatorF = 0.0;

		public static double kElevatorTicksPerInch = (1024.0) / (2.0 * 2.0 * Math.PI); //1024 ticks per revolution of shaft

		//Limelight
		//TODO: Figure this out with CAD
		public static double kLimelightTiltAngle = 82.0;
		public static double kLimelightHeight = 0.0; //Height of Limelight's camera aperture from the ground

	/**
	 * Field parameters
	 */
	public static final double HATCHVISIONTARGET = 28.5;
	public static final double PORTVISIONTARGET = 36.125;
	
	public static final double PORTBOTTOM = 27.5;
	public static final double PORTMIDDLE = 55.5;
	public static final double PORTTOP = 83.5;
	public static final double HATCHBOTTOM = 19;
	public static final double HATCHMIDDLE = 47;
	public static final double HATCHTOP = 75;
	
	/**
	 * CAN IDs
	 */
	public static final int CAN_PNMMODULE = 1;

    public static final int CAN_LEFTMOTORFRONT = 2;
	public static final int CAN_LEFTMOTORREAR = 3;
	public static final int CAN_RIGHTMOTORFRONT = 4;
	public static final int CAN_RIGHTMOTORREAR = 5;

	public static final int CAN_BALLMOTOR = 6;
	public static final int CAN_HATCHMOTOR = 7;

	public static final int CAN_ARMELBOW = 8;
	public static final int CAN_ARMWRIST =	 9;

	public static final int CAN_ELEVATOR1 = 10;
	public static final int CAN_ELEVATOR2 = 11;


	/**
	 * Pneumatics ports
	 */
	public static final int PNM_SHIFT = 0;
	public static final int PNM_CLIMBPISTON1 = 1;
	public static final int PNM_CLIMBPISTON2 = 2;
	public static final int PNM_INTAKEPISTON1 = 3;
	public static final int PNM_INTAKEPISTON2 = 4;

	/**
	 * Gamepad Button List
	 * TODO: Figure out which buttons should do what and code for them
	 */
	public static final int LST_BTN_A = 1;
	public static final int LST_BTN_B = 2;
	public static final int LST_BTN_X = 3;
	public static final int LST_BTN_Y = 4;
	public static final int LST_BTN_LBUMPER = 5;
	public static final int LST_BTN_RBUMPER = 6;
	public static final int LST_BTN_BACK = 7;
	public static final int LST_BTN_START = 8;
	public static final int LST_BTN_RJOYSTICKPRESS = 9;
	public static final int LST_BTN_LJOYSTICKPRESS = 10;

	/**
	 * Gamepad Axis List
	 * TODO: 0-4 are to be coded for
	 */
	public static final int LST_AXS_LJOYSTICKX = 0;
	public static final int LST_AXS_LJOYSTICKY = 1;
	public static final int LST_AXS_LTRIGGER = 2;
	public static final int LST_AXS_RTRIGGER = 3;
	public static final int LST_AXS_RJOYSTICKX = 4;
	public static final int LST_AXS_RJOYSTICKY = 5;


}