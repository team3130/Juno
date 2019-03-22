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
		public static double kChassisWidth = 23.5; //Checked 2/12 Distance between the left and right middle wheels
		public static double kLWheelDiameter = 6.0; //Center wheel
		public static double kRWheelDiameter = 6.0; //Center wheel
		public static double kDriveCodesPerRev = 4096;
		public static double kDistanceToEncoder = kDriveCodesPerRev / (Math.PI * 0.5*(kLWheelDiameter+kRWheelDiameter));
		public static double kVelocityToEncoder = kDistanceToEncoder / 10.0; 		// Per 100ms
		public static double kAccelerationToEncoder = kVelocityToEncoder / 10.0; 	// Per 100ms


		public static double kChassisShiftWait = 0.07;

		public static double kChassisHighP = 0.02; //0.018
		public static double kChassisHighI = 0;
		public static double kChassisHighD = 0.09; //0.062

		public static double kChassisLowP = 0.03;
		public static double kChassisLowI = 0;
		public static double kChassisLowD = 0.11;

		public static double kDriveDeadband = 0.02;

	//Intake
		public static double kIntakeTriggerDeadband = 0.4;

	//Climber
		public static double kLandingGearMultiplier = 0.2; //FIXME

	//Arm
		public static double kArmLength = 7.70652; //Distance from centers of pivot points

	//Wrist
		//Manual
		public static double kWristManualDeadband = 0.1;
		public static double kWristManualMultipler = 0.7;

		public static double kWristHomingAngle = 91.0; //FIXME Checked 3/21

		public static double kWristP = 3.0; //Checked 3/21
		public static double kWristI = 0.001; //Checked 3/21
		public static double kWristD = 1.5; //Checked 3/21
		public static double kWristF = 0.0; //Checked 2/17
		public static int kWristMaxAcc = 1400; // 1024
		public static int kWristMaxVel = 3400; // 1024

		public static double kWristFFEmpty = 0.05; //Checked 3/8
		public static double kWristFFBall = 0.0;
		public static double kWristFFHatch = 0.0;

		public static double kWristFinishDeadband = 1.0;

		public static double kWristZeroTimeout = 5.0;

		public static double kWristKaWithBall = 0.008;
		public static double kWristKaEmpty = 0.006;
		public static double kWristKaWithHatch = 0.013;

		public static double kWristTicksPerDeg = 4096.0 / 360.0 * 44.0 / 18.0 ; //4096 ticks per revolution of gearbox output shaft

	//Placement Presets
		public enum Presets{
			LowestHatch (15.0 , 90.0),
			MiddleHatch (53.0, 90.0),
			HighestHatch(86.0,90.0),
			LowestTongue(23.0, 90.0),
			MiddleTongue(61.0, 90.0),
			HighestTongue(93.0, 90.0),
			Station(19.5, 90.0), //Checked 3/21
			LowestPort ( 20.0, 120.0),
			MiddlePort (55.0, 120.0),
			HighestPort (90.0,120.0),
			Cargoship(35.0, 170.0); //FIXME

			private double height;
			private double angle;

			Presets(double height, double angle){
				this.height = height;
				this.angle = angle;
			}

			public double getHeight(){
				return this.height;
			}

			public double getAngle(){
				return this.angle;
			}
		}

		public static double kPresetTriggerDeadband = 0.6;


	//Elevator
		public static double kElevatorSlowZone = 11.0;

		public static double kElevatorStagePickup1 = 27.28; //Checked 2/12
		public static double kElevatorFFAddition1 = 0.0;

		public static double kElevatorStagePickup2 = 49.25; //Checked 2/12
		public static double kElevatorFFAddition2 = 0.0;

		//Manual
		public static double kElevatorManualDeadband = 0.06;
		public static double kElevatorManualMultipler = 0.8;

		public static double kElevatorP = 1.2; //Checked 3/21
		public static double kElevatorI = 0.0; //Checked 3/21
		public static double kElevatorD = 0.1; //Checked 3/21
		public static double kElevatorF = 0.04; //Checked 3/21
		public static int kElevatorMaxAcc = 1900; //Checked 3/21
		public static int kElevatorMaxVel = 2300; //Checked 3/21

		public static double kElevatorFinishDeadband = 1.0;

		public static double kElevatorFFEmpty = 0.04; //Checked 3/21
		public static double kElevatorFFWithHatch = 0.09; //Checked 3/21
		public static double kElevatorFFWithBall = 0.07; //Checked 3/21

		public static double kElevatorHeightEpsilon = 6.0; //Checked 2/17 min height is 1 inch off ground
		public static double kElevatorHomingHeight = 7.5; //Checked 3/21 height of elevator off ground when at home position
		public static double kElevatorMaxHeight = 60.1; //FIXME Checked 3/21

		public static double kElevatorTicksPerInch =  (4096.0)/ (2.0*Math.PI * 0.66); //Updated 3/21 || 4096 ticks per revolution of output shaft

	//Limelight
	public static double kLimelightTiltAngle = -30.0; //Checked 2/19
	public static double kLimelightHeight = 38.875; // Height of camera aperture from the ground
	public static double kLimelightCalibrateDist = 36;	// Exact horizontal distance between hatch target and lens
	public static double kLimelightOffset = 10.375;    // Offset to the right side (inches)
	public static double kLimelightBumper = 12;   // Depth of the camera from the front bumper (inches)
	public static double kLimelightForward = 25;   // Depth of the camera from the front bumper (inches)

	/**
	 * Field parameters
	 */
	public static final double HATCHVISIONTARGET = 31.25;
	public static final double PORTVISIONTARGET = 38.75;
	
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

	public static final int CAN_RIGHTMOTORFRONT = 2;
	public static final int CAN_RIGHTMOTORREAR = 3;

    public static final int CAN_LEFTMOTORFRONT = 4;
	public static final int CAN_LEFTMOTORREAR = 5;

	//public static final int (nada yet) = 6;
	public static final int CAN_ARMWRIST = 7;

	public static final int CAN_BALLMOTOR = 8;

	public static final int CAN_ELEVATOR1 = 10;
	public static final int CAN_ELEVATOR2 = 11;

	public static final int CAN_CLIMBERLEG = 12;
	public static final int CAN_CLIMBERDRIVE = 13;

	/**
	 * Pneumatics ports
	*/
	public static final int PNM_SHIFT = 0;
	public static final int PNM_TONGUEPISTON = 3;
	public static final int PNM_CLIMBARMS = 1;

	/**
	 * Gamepad Button List
	 */
	public static final int LST_BTN_A = 1;
	public static final int LST_BTN_B = 2;
	public static final int LST_BTN_X = 3;
	public static final int LST_BTN_Y = 4;
	public static final int LST_BTN_LBUMPER = 5;
	public static final int LST_BTN_RBUMPER = 6;
	public static final int LST_BTN_WINDOW = 7;
	public static final int LST_BTN_MENU = 8;
	public static final int LST_BTN_LJOYSTICKPRESS = 9;
	public static final int LST_BTN_RJOYSTICKPRESS = 10;

	/**
	 * Gamepad POV List
	 */
	public static final int LST_POV_UNPRESSED = -1;
	public static final int LST_POV_N = 0;
	public static final int LST_POV_NE = 45;
	public static final int LST_POV_E = 90;
	public static final int LST_POV_SE = 135;
	public static final int LST_POV_S = 180;
	public static final int LST_POV_SW = 225;
	public static final int LST_POV_W = 270;
	public static final int LST_POV_NW = 315;


	/**
	 * Gamepad Axis List
	 */
	public static final int LST_AXS_LJOYSTICKX = 0;
	public static final int LST_AXS_LJOYSTICKY = 1;
	public static final int LST_AXS_LTRIGGER = 2;
	public static final int LST_AXS_RTRIGGER = 3;
	public static final int LST_AXS_RJOYSTICKX = 4;
	public static final int LST_AXS_RJOYSTICKY = 5;

	/**
	 * Climber Button names
	 */
	public static final int BTN_CONFIRM_WEAPONS = LST_BTN_MENU;
	public static final int BTN_DROP_ARMS = LST_BTN_WINDOW;
	public static final int BTN_UP_LEG = LST_BTN_RBUMPER;
	public static final int AXS_DROP_LEG = LST_AXS_LTRIGGER;
	public static final int AXS_DRIVE_LEG = LST_AXS_RTRIGGER;
}
