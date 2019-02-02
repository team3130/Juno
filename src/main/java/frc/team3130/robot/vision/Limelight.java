package frc.team3130.robot.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3130.robot.RobotMap;


public class Limelight {
    //Instance Handling
    private static Limelight m_pInstance;

    public static Limelight GetInstance() {
        if (m_pInstance == null) m_pInstance = new Limelight();
        return m_pInstance;
    }

    private static NetworkTableEntry tv;

    private static NetworkTableEntry tx; //x angle offset from crosshair, range of -27 to 27
    private static NetworkTableEntry ty; //y angle offset from crosshair, range of -20.5 to 20.5
    private static NetworkTableEntry ta;

    private static double x_targetOffsetAngle = 0.0;
    private static double y_targetOffsetAngle = 0.0;
    private static double area = 0.0;

    public Limelight() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        tv = table.getEntry("tv");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
    }


    //read values periodically
    public static void updateData() {
        //Check if limelight sees a target
        if(tv.getDouble(0.0) == 1.0){
            x_targetOffsetAngle = tx.getDouble(0.0);
            y_targetOffsetAngle = ty.getDouble(0.0);
            area = ta.getDouble(0.0);
        }else{
            //there is no valid target so set all values to 0.0
            x_targetOffsetAngle = 0.0;
            y_targetOffsetAngle = 0.0;
            area = 0.0;
        }
    }

    public static double getdegHorizontalOffset(){
        return x_targetOffsetAngle;
    }

    public static double getDistanceToTarget(boolean isHatch){
        double angle = RobotMap.kLimelightTiltAngle + y_targetOffsetAngle;
        double hLimelight = RobotMap.kLimelightHeight;

        double hTarget;
        if(isHatch){
            hTarget = RobotMap.HATCHHEIGHT;
        }else{
            hTarget = RobotMap.PORTHEIGHT;
        }

        return (hTarget - hLimelight) / Math.tan(angle);
    }

    /*
    How to set a parameter value (ie. pipeline to use)
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<PUT VARIABLE NAME HERE>").setNumber(<TO SET VALUE>);
    */

    public static void outputToSmartDashboard(){
            SmartDashboard.putNumber("LimelightX", x_targetOffsetAngle);
            SmartDashboard.putNumber("LimelightY", y_targetOffsetAngle);
            SmartDashboard.putNumber("LimelightArea", area);
    }

}
