import java.awt.Color;
import java.util.ArrayList;

public class Table {
    private static int DEFAULT_TABLE_WIDTH = 88;
    private static int DEFAULT_TABLE_HEIGHT = 44;

    public Color color;
    public ArrayList<Ball> balls;
    public double[] dimensions;
    public double[][] pockets;

    public Table() {
        this.color = Color.green.darker();
        this.dimensions = new double[] {DEFAULT_TABLE_WIDTH, DEFAULT_TABLE_HEIGHT};
        double r = dimensions[0] / 85;

        this.balls = new ArrayList<Ball>(15);
        this.balls.add(new CueBall(new double[] {dimensions[0] / 4, dimensions[1] / 2}, r));
        for (int i = 1; i <= 15; i++) {
            double[] add = addPosition(i, r);
            this.balls.add(new NumberedBall(i, new double[] {dimensions[0] * 0.75 + add[0] + 1, dimensions[1] / 2 + add[1] + 1}, r));
        }
        this.pockets = new double[6][2];
        for (int i = 0; i < 3; i++) {
            this.pockets[i][0] = dimensions[0] / 2 * i;
            this.pockets[i][1] = 0;
        }
        for (int i = 0; i < 3; i++) {
            this.pockets[i+3][0] = dimensions[0] / 2 * i;
            this.pockets[i+3][1] = dimensions[1];
        }
    }
    public Table(Color color, double[] dimensions) {
        this.color = color;
        this.dimensions = dimensions;
        double r = dimensions[0] / 85;

        this.balls = new ArrayList<Ball>(15);
        this.balls.add(new CueBall(new double[] {dimensions[0] / 4, dimensions[1] / 2}, r));
        for (int i = 1; i <= 15; i++) {
            double[] add = addPosition(i, r);
            this.balls.add(new NumberedBall(i, new double[] {dimensions[0] * 0.75 + add[0] + 1, dimensions[1] / 2 + add[1] + 1}, r));
        }
        this.pockets = new double[6][2];
        for (int i = 0; i < 3; i++) {
            this.pockets[i][0] = dimensions[0] / 2 * i;
            this.pockets[i][1] = 0;
        }
        for (int i = 0; i < 3; i++) {
            this.pockets[i+3][0] = dimensions[0] / 2 * i;
            this.pockets[i+3][1] = dimensions[1];
        }
    }

    private double[] addPosition(int num, double r) {
        switch (num) {
            case 2:
                return new double[] {4 * Math.sqrt(3) * r, -2 * r};
            case 3:
                return new double[] {Math.sqrt(3) * r, -r};
            case 4:
                return new double[] {3 * Math.sqrt(3) * r, r};
            case 5:
                return new double[] {4 * Math.sqrt(3) * r, 2 * r};
            case 6:
                return new double[] {2 * Math.sqrt(3) * r, -2 * r};
            case 7:
                return new double[] {4 * Math.sqrt(3) * r, -4 * r};
            case 8:
                return new double[] {2 * Math.sqrt(3) * r, 0};
            case 9:
                return new double[] {3 * Math.sqrt(3) * r, 3 * r};
            case 10:
                return new double[] {4 * Math.sqrt(3) * r, 0};
            case 11:
                return new double[] {Math.sqrt(3) * r, r};
            case 12:
                return new double[] {4 * Math.sqrt(3) * r, 4 * r};
            case 13:
                return new double[] {3 * Math.sqrt(3) * r, -3 * r};
            case 14:
                return new double[] {2 * Math.sqrt(3) * r, 2 * r};
            case 15:
                return new double[] {3 * Math.sqrt(3) * r, -r};
            default:
                return new double[] {0, 0};
        }
    }
}
