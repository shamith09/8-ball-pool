import java.awt.Color;

public class NumberedBall extends Ball {
    private static Color[] colors = {
        Color.black,
        Color.yellow,
        Color.blue,
        Color.red,
        new Color(102,0, 153),
        Color.orange,
        new Color(0, 102, 0),
        new Color(128, 0, 0),
    };
    public NumberedBall(int num, double[] position, double radius) {
        super(colors[num % 8], num < 8, num, position, radius);
    }
}
