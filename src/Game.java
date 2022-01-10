import java.awt.Color;

public class Game {
    public Player[] players;
    public int curPlayerIndex;
    public Table table;
    public boolean ballInHand;
    public int winnerIndex;
    public boolean gameOver;

    public Game() {
        this.table = new Table(Color.green.darker(), new double[] {1200, 600});
        this.players = new Player[2];
        this.players[0] = new Player(1);
        this.players[1] = new Player(2);
        this.curPlayerIndex = 0;
    }

    public void play(double x, double y) {
        ((CueBall) (table.balls.get(0))).hit(x, y);
        boolean changeTurn = true;
        for (Ball ball: table.balls) {
            if (ball.pocketed) {
                if (players[0].stripes == players[1].stripes) {
                    players[curPlayerIndex].stripes = ball.striped;
                    players[1 - curPlayerIndex].stripes = !ball.striped;
                    changeTurn = false;
                } else if (ball instanceof CueBall) {
                    ballInHand = true;
                } else if (ball.color == Color.black) {
                    gameOver = true;
                    if (players[curPlayerIndex].ballsLeft.isEmpty())
                        winnerIndex = curPlayerIndex;
                    else
                        winnerIndex = 1 - curPlayerIndex;
                    break;
                } else if (ball.striped == players[curPlayerIndex].stripes) {
                    players[curPlayerIndex].pocket(ball);
                    changeTurn = false;
                } else {
                    players[1 - curPlayerIndex].pocket(ball);
                }
            }
        }
        if (changeTurn)
            curPlayerIndex = 1 - curPlayerIndex;
    }

    public void placeCue(double[] newPos) {
        table.balls.get(0).position = newPos;
    }
}
