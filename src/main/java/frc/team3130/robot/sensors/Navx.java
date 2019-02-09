package frc.team3130.robot.sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import frc.team3130.robot.RobotMap;
import frc.team3130.robot.subsystems.Chassis;

public class Navx {
    //Instance Handling
    private static Navx m_pInstance;
    public static Navx GetInstance() {
        if (m_pInstance == null) m_pInstance = new Navx();
        return m_pInstance;
    }

    //Create necessary objects
    private static AHRS m_navX;


    //Create and define all standard data types needed
    private static boolean m_bNavXPresent;

    private Navx(){
        /**
         * Constructor:
         * Define and configure your defined objects (ie. talons, vars)
         *
         * _talon.configFactoryDefault();
         * resets hardware defaults that could have been configured on talon before
         *
         */
        try{
            //Connect to navX Gyro on MXP port.
            m_navX = new AHRS(SPI.Port.kMXP);
            m_bNavXPresent = true;
        } catch(Exception ex){
            //If connection fails log the error and fall back to encoder based angles.
            String str_error = "Error instantiating navX from MXP: " + ex.getLocalizedMessage();
            DriverStation.reportError(str_error, true);
            m_bNavXPresent = false;
        }

    }
    public static double getAngle()
    {

        if(m_bNavXPresent)
        {
            //Angle use wants a faster, more accurate, but drifting angle, for quick use.
            //System.out.println(m_navX.getAngle());
            return m_navX.getAngle();
        }else {
            //Means that angle use wants a driftless angle measure that lasts.
            return ( Chassis.getDistanceR() - Chassis.getDistanceL() ) * 180 / (RobotMap.kChassisWidth * Math.PI);
            /* Angle is 180 degrees times encoder difference over Pi * the distance between the wheels
             * Made from geometry and relation between angle fraction and arc fraction with semicircles.
             */
        }
    }

    /**
     * Returns the current rate of change of the robots heading
     *
     * <p> getRate() returns the rate of change of the angle the robot is facing,
     * with a return of negative one if the gyro isn't present on the robot,
     * as calculating the rate of change of the angle using encoders is not currently being done.
     * @return the rate of change of the heading of the robot.
     */
    public static double getRate()
    {
        if(m_bNavXPresent) return m_navX.getRate();
        return -1;
    }

    public static boolean getNavxPresent(){
        return m_bNavXPresent;
    }
}



