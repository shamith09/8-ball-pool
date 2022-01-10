import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;


public class Ball {
    private static final double friction = 0.001;
    public Color color;
    public boolean striped;
    public boolean pocketed;
    public double[] position;
    public int num;
    public double[] velocity;
    public double radius;

    public Ball(Color color, boolean striped, int num, double[] position, double radius) {
        this.color = color;
        this.striped = striped;
        this.position = position;
        this.num = num;
        this.radius = radius;
        this.velocity = new double[] {0, 0};
    }

    public void hitWall(boolean isVertical, double[] dimensions) {
        velocity[isVertical ? 0 : 1] *= -0.9;
        if (isVertical && position[0] > dimensions[0] / 2)
            position[0] = dimensions[0] - radius;
        else if (isVertical)
            position[0] = radius;
        else if (position[1] > dimensions[1] / 2)
            position[1] = dimensions[1] - radius;
        else
            position[1] = radius;
    }

    public void updatePosition() {
        if (!pocketed) {
            position[0] += velocity[0];
            position[1] += velocity[1];
            double hyp = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]);
            if (hyp != 0) {
                if (velocity[0] > 0)
                    velocity[0] = Math.max(0, velocity[0] - friction * velocity[0] / hyp);
                else
                    velocity[0] = Math.min(0, velocity[0] - friction * velocity[0] / hyp);
                if (velocity[1] > 0)
                    velocity[1] = Math.max(0, velocity[1] - friction * velocity[1] / hyp);
                else
                    velocity[1] = Math.min(0, velocity[1] - friction * velocity[1] / hyp);
            }
        }
    }

    public void checkPockets(double[][] pockets, double pocketDiameter) {
        for (double[] pocket : pockets)
            if (Math.sqrt(Math.pow(position[0] - pocket[0], 2) + Math.pow(position[1] - pocket[1], 2)) <= pocketDiameter / 2 + 5) {
                pocketed = true;
                if (color != Color.white) {
                    position[0] = -100;
                    position[1] = -100;
                }
                velocity[0] = 0;
                velocity[1] = 0;
            }
    }

    public void checkCollision(ArrayList<Ball> balls, double[] dimensions) {
        for (int i = 0; i < 360; i++) {
            double x = 2 * radius * Math.cos(i);
            double y = 2 * radius * Math.sin(i);
            for (Ball ball : balls)
                if (ball != this && !ball.pocketed && Math.abs(ball.position[0] - position[0]) <= x && Math.abs(ball.position[1] - position[1]) <= y)
                    collide(ball, x, y);
        }
        if (position[0] + radius >= dimensions[0] || position[0] - radius <= 0)
            hitWall(true, dimensions);
        else if (position[1] + radius >= dimensions[1] || position[1] - radius <= 0)
            hitWall(false, dimensions);
    }

    public void collide(Ball other, double x, double y) {
        double angle = Math.atan2(other.position[1] - this.position[1], other.position[0] - this.position[0]);
        if (other.position[1] < this.position[1])
            angle = -angle;
        double[] rot1 = rotatedVector(this.velocity, -angle);
        double[] rot2 = rotatedVector(other.velocity, -angle);
        double temp = rot1[0];
        rot1[0] = rot2[0];
        rot2[0] = temp;
        other.velocity = rotatedVector(rot2, angle);
        this.velocity = rotatedVector(rot1, angle);

        if (other.position[0] > position[0])
            other.position[0] = position[0] + x;
        else
            position[0] = other.position[0] + x;

        if (other.position[1] > position[1])
            other.position[1] = position[1] + y;
        else
            position[1] = other.position[1] + y;
    }
    private double[] rotatedVector(double[] vector, double angle) {
        return new double[] {
            Math.cos(angle) * vector[0] - Math.sin(angle) * vector[1],
            Math.sin(angle) * vector[0] + Math.cos(angle) * vector[1]
        };
    }
}
