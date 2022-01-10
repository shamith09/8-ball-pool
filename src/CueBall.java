import java.awt.Color;

public class CueBall extends Ball {
    public CueBall(double[] position, double radius) {
        super(Color.white, false, -1, position, radius);
    }
    public void hit(double x, double y) {
        velocity[0] = x;
        velocity[1] = y;
    }
}
