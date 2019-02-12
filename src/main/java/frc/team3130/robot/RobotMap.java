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
		public static double kLWheelDiameter = 6.0; //Center wheel
		public static double kRWheelDiameter = 6.0;	//Center wheel
		public static double kDriveCodesPerRev = 2048.0;

		//PID Preferences Defaults
		public static double kChassisHighP = 0.02; //0.018
		public static double kChassisHighI = 0;
		public static double kChassisHighD = 0.09; //0.062

		public static double kChassisLowP = 0.03;
		public static double kChassisLowI = 0;
		public static double kChassisLowD = 0.11;



		//Intake

		//Arm
		public static double kArmLength = 7.70652; //Distance from centers of pivot points

			//Wrist
			public static double kWristP = 0.0;
			public static double kWristI = 0.0;
			public static double kWristD = 0.0;
			public static double kWristF = 0.0;
			public static int kWristMaxAcc = 0; // 1024
			public static int kWristMaxVel = 0; // 1024

			public static double kWristTicksPerDeg = (4096.0) / (2.0*Math.PI * 1.0); //1024 ticks per revolution of gearbox output shaft

			//Elbow
			public static double kElbowP = 0.0;
			public static double kElbowI = 0.0;
			public static double kElbowD = 0.0;
			public static double kElbowF = 0.0;
			public static int kElbowMaxAcc = 0; // 1024
			public static int kElbowMaxVel = 0; // 1024

			public static double kElbowTicksPerDeg = (4096.0) / (2.0*Math.PI * 1.0); //1024 ticks per revolution of gearbox output shaft


		//Elevator
		//TODO: Actually find these values
		public static double kElevatorSlowZone = 0.0;

		public static double kElevatorP = 0.0;
		public static double kElevatorI = 0.0;
		public static double kElevatorD = 0.0;
		public static double kElevatorF = 0.0;
		public static int kElevatorMaxAcc = 0;
		public static int kElevatorMaxVel = 0;

		public static double kElevatorFFEmpty = 0.0;
		public static double kElevatorFFWIthHatch = 0.0;
		public static double kElevatorFFWithBall = 0.0;

		public static double kElevatorHeightEpsilon = 1.0; //min height is 1 inch off ground
		public static double kElevatorHomingHeight = 0.0; //height of elevator off ground when at home position

		public static double kElevatorTicksPerInch = (4096.0 / 3.0) / (2.0*Math.PI * 0.65); //1024 ticks per revolution of encoder shaft which runs 3 times faster than the output shaft

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
	public static final double HATCHBOTTOM = 19.0;
	public static final double HATCHMIDDLE = 47.0;
	public static final double HATCHTOP = 75.0;
	
	/**
	 * CAN IDs
	 */
	public static final int CAN_PNMMODULE = 1;

    public static final int CAN_LEFTMOTORFRONT = 2;
	public static final int CAN_LEFTMOTORREAR = 3;
	public static final int CAN_RIGHTMOTORFRONT = 4;
	public static final int CAN_RIGHTMOTORREAR = 5;



	public static final int CAN_ARMELBOW = 6;
	public static final int CAN_ARMWRIST =	 7;

	public static final int CAN_BALLMOTOR = 8;
	public static final int CAN_HATCHMOTOR = 9;

	public static final int CAN_ELEVATOR1 = 10;
	public static final int CAN_ELEVATOR2 = 11;

	public static final int CAN_PISTON = 12;


	/**
	 * Pneumatics ports
	 */
	public static final int PNM_SHIFT = 0;
	public static final int PNM_ELEVATORSHIFT = 1;
	public static final int PNM_CLIMBPISTON1 = 2;
	public static final int PNM_INTAKEPISTON = 3;

	/**
	 * Gamepad Button List
	 *
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