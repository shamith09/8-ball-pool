import java.util.ArrayList;

public class Player {
    public boolean stripes;
    public ArrayList<Integer> ballsLeft;
    public String name;

    public Player(int i) {
        this.ballsLeft = new ArrayList<Integer>(7);
        this.name = "Player " + i;
    }

    public void setStripes(boolean stripes) {
        this.stripes = stripes;
        if (stripes)
            for (int i = 9; i <= 15; i++)
                ballsLeft.add(i);
        else
            for (int i = 1; i <= 7; i++)
                ballsLeft.add(i);
    }

    public void pocket(Ball ball) {
        if (ballsLeft.contains(ball.num))
            ballsLeft.remove(ball.num);
    }

    public String toString() {
        return name;
    }
}
