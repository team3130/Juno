package frc.team3130.robot.tantanDrive.Paths;

/**
 *
 */
public class Path {
    public double[][] Points;
    public int kNumPoints = 0;

    public Path(double[][] points) {
        this.Points = points;
        this.kNumPoints = points.length;
    }
}
