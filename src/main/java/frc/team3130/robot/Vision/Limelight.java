package frc.team3130.robot.Vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Limelight {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");

    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);

    double targetOffsetAngle_Horizontal = table.getNumber("tx", 0);
    double targetOffsetAngle_Vertical = table.getNumber ("ty", 0);
    double targetArea = table.getNumber ("ta", 0);
    double targetSkew = table.getNumber ("ts", 0);
    
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").getDouble(0);

    NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").setNumber(<value>);

}
