package frc.team3130.robot.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Limelight {

    private static NetworkTableEntry tv;

    private static NetworkTableEntry tx; //x angle offset from crosshair, range of -27 to 27
    private static NetworkTableEntry ty; //y angle offset from crosshari, range of -20.5 to 20.5
    private static NetworkTableEntry ta;

    private static double x = 0.0;
    private static double y = 0.0;
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
            x = tx.getDouble(0.0);
            y = ty.getDouble(0.0);
            area = ta.getDouble(0.0);
        }else{
            //there is no valid target so set all values to 0.0
            x = 0.0;
            y = 0.0;
            area = 0.0;
        }
    }

    /*
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);

    double targetOffsetAngle_Horizontal = table.getNumber("tx", 0);
    double targetOffsetAngle_Vertical = table.getNumber ("ty", 0);
    double targetArea = table.getNumber ("ta", 0);
    double targetSkew = table.getNumber ("ts", 0);

    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").getDouble(0);

    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").setNumber(<value>);
    */
    public static void outputToSmartDash(){
            SmartDashboard.putNumber("LimelightX", x);
            SmartDashboard.putNumber("LimelightY", y);
            SmartDashboard.putNumber("LimelightArea", area);
    }

}
