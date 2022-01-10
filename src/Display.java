import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.*;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel {
    private static final long serialVersionUID = 7148504528835036003L;
    private static final double BORDER_WIDTH = 60;
    private static final double POCKET_DIAMETER = 45;
    private Game game;
    private Line2D line;
    private Line2D line2;
    private Point2D origin;
    private Point2D center;

    public Display() {
        super();
        game = new Game();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (center == null)
            center = new Point2D.Double(getWidth() / 2, getHeight() / 2);
        if (origin == null)
            origin = new Point2D.Double(center.getX() - game.table.dimensions[0] / 2, center.getY() - game.table.dimensions[1] / 2);

        // Draw border
        g2.setColor(new Color(78, 53, 36));
        RoundRectangle2D border = new RoundRectangle2D.Double(center.getX() - (game.table.dimensions[0] + BORDER_WIDTH) / 2,
                                                              center.getY() - (game.table.dimensions[1] + BORDER_WIDTH) / 2,
                                                              game.table.dimensions[0] + BORDER_WIDTH,
                                                              game.table.dimensions[1] + BORDER_WIDTH,
                                                            game.table.dimensions[0] / 30,
                                                            game.table.dimensions[0] / 30);
        g2.fill(border);
        // Draw table
        g2.setColor(game.table.color);
        Rectangle2D middle = new Rectangle2D.Double(origin.getX(),
                                                    origin.getY(),
                                                    game.table.dimensions[0],
                                                    game.table.dimensions[1]);
        g2.fill(middle);
        g2.setColor(Color.black);
        // Draw pockets
        int i = 0;
        for (double[] pocket : game.table.pockets) {
            double x = origin.getX() + pocket[0] - POCKET_DIAMETER / 2;
            double y = origin.getY() + pocket[1];

            switch (i) {
                case 0:
                    x += 8;
                    y -= POCKET_DIAMETER / 2 - 10;
                    break;
                case 1:
                    y -= POCKET_DIAMETER / 2 + 6;
                    break;
                case 2:
                    x -= 8;
                    y -= POCKET_DIAMETER / 2 - 10;
                    break;
                case 3:
                    x += 8;
                    y -= POCKET_DIAMETER / 2 + 5;
                    break;
                case 4:
                    y -= POCKET_DIAMETER / 2 - 7;
                    break;
                case 5:
                    x -= 8;
                    y -= POCKET_DIAMETER / 2 + 5;
                    break;
            }

            g2.fill(new Ellipse2D.Double(x, y, POCKET_DIAMETER, POCKET_DIAMETER));
            i++;
        }
        // Draw balls
        for (Ball ball : game.table.balls) {
            if (!ball.pocketed || ball.color == Color.white) {
                g2.setColor(ball.color);
                g2.fill(new Ellipse2D.Double(origin.getX() + ball.position[0] - ball.radius, origin.getY() + ball.position[1] - ball.radius, ball.radius * 2, ball.radius * 2));
                g2.setColor(Color.white);
                if (ball.num != -1) {
                    g2.fill(new Ellipse2D.Double(origin.getX() + ball.position[0] - ball.radius / 2 - 1, origin.getY() + ball.position[1] - ball.radius / 2 - 1, ball.radius + 2, ball.radius + 2));
                    if (ball.striped) {
                        g2.fill(new Rectangle2D.Double(origin.getX() + ball.position[0] - ball.radius, origin.getY() + ball.position[1] - 5, 2 * ball.radius, 10));
                    }
                    g2.setColor(Color.black);
                    g2.drawString(Integer.toString(ball.num), (float) (origin.getX() + ball.position[0] - ball.radius / 2 + (ball.num < 10 ? 3 : 1)), (float) (origin.getY() + ball.position[1] + ball.radius / 2 - 2.5));
                }
            }
        }
        // Draw aiming line
        if (line != null) {
            g2.setColor(Color.white);
            g2.draw(line2);
            Stroke defaultStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(5));
            if (game.players[game.curPlayerIndex].stripes)
                g2.setColor(new Color(78, 53, 36));
            else
                g2.setColor(Color.red.darker());
            g2.draw(line);
            g2.setStroke(defaultStroke);
        }
        // Play game and repaint
        if (game.gameOver) {
            System.out.println(game.players[game.winnerIndex]);
        } else {
            for (Ball ball: game.table.balls) {
                ball.checkCollision(game.table.balls, game.table.dimensions);
                ball.checkPockets(game.table.pockets, POCKET_DIAMETER);
                ball.updatePosition();
            }
            removeAll();
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Display panel = new Display();
            panel.setBackground(Color.gray);
            JFrame frame = new JFrame("8 Ball Pool");

            frame.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    boolean play = true;
                    for (Ball ball: panel.game.table.balls)
                        if (!(ball.velocity[0] == 0 || ball.velocity[1] == 0)) {
                            play = false;
                            break;
                        }
                    if (play) {
                        if (panel.game.ballInHand)
                            panel.game.ballInHand = false;
                        double[] position = panel.game.table.balls.get(0).position;
                        double radius = panel.game.table.balls.get(0).radius;
                        double x = (position[0] + panel.origin.getX() - e.getX() + radius / 2) / 30;
                        double y = (position[1] + panel.origin.getY() - e.getY() + 2 * radius) / 30;
                        double ratio = y / x;
                        x = Math.min(x, 50);
                        y = x * ratio;
                        panel.game.play(x, y);
                        panel.line = null;
                    }
                }
            });
            frame.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    boolean play = true;
                    for (Ball ball: panel.game.table.balls)
                        if (!(ball.velocity[0] == 0 || ball.velocity[1] == 0)) {
                            play = false;
                            break;
                        }
                    if (play) {
                        double radius = panel.game.table.balls.get(0).radius;
                        if (panel.game.ballInHand) {
                            panel.game.placeCue(new double[] {e.getX() - panel.origin.getX() - radius / 2, e.getY() - panel.origin.getY() - 2 * radius});
                        } else {
                            double[] position = panel.game.table.balls.get(0).position;
                            panel.line = new Line2D.Double(panel.origin.getX() + position[0], panel.origin.getY() + position[1],
                                    3 * (panel.origin.getX() + position[0]) - 2 * e.getX() + radius, 3 * (panel.origin.getY() + position[1]) - 2 * e.getY() + 4 * radius);
                            panel.line2 = new Line2D.Double(panel.origin.getX() + position[0], panel.origin.getY() + position[1],
                                    41 * (panel.origin.getX() + position[0]) - 40 * e.getX() + 20 * radius, 41 * (panel.origin.getY() + position[1]) - 40 * e.getY() + 80 * radius);
                        }
                    }
                }
            });
            frame.setSize(1400, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
