
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class snake_2 extends JPanel implements ActionListener {

    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    public final int DELAY = 140;

    public final int x[] = new int[ALL_DOTS];
    public final int y[] = new int[ALL_DOTS];

    public int dots;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = true;



    public boolean inGame = true;
    private Timer timer;
    private Image ball;
    private Image head;

    String dot_path = "Snake-Game/src/resources/dot.png";
    String head_path = "Snake-Game/src/resources/head.png";

    public snake_2() {
        initBoard();
    }

    private void initBoard() {
        loadImages();
        initGame();
    }
    
    public void setTimer(Timer t) {
        this.timer = t;
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon(dot_path);
        ball = iid.getImage();

        ImageIcon iih = new ImageIcon(head_path);
        head = iih.getImage();
    }

    private void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 400;
        }
        

        timer = new Timer(DELAY, this);
        timer.start();
        
    }


    public void doDrawing(Graphics g) {
        if (inGame) {
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();  
            // Synchronizes the graphics state across different platforms to ensure smooth rendering
        }
    }

    public void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        if (y[0] >= B_HEIGHT) {
            y[0] = 0;
        }
        if (y[0] < 0) {
            y[0] = B_HEIGHT;
        }
        if (x[0] >= B_WIDTH) {
            x[0] = 0;
        }
        if (x[0] < 0) {
            x[0] = B_WIDTH;
        }
        if (!inGame) {
            timer.stop();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkCollision();
            
        }
        repaint();
    }

    public class TAdapter_1 extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_J) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;

            } else if ((key == KeyEvent.VK_L) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;

            } else if ((key == KeyEvent.VK_I) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;

            } else if ((key == KeyEvent.VK_K) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;

            }
            if ((key == KeyEvent.VK_O) && (!leftDirection && !downDirection)) {
                rightDirection = true;
                upDirection = true;
            }
            if ((key == KeyEvent.VK_U) && (!rightDirection && !downDirection)) {
                leftDirection = true;
                upDirection = true;
            }
            if ((key == KeyEvent.VK_H) && (!rightDirection && !upDirection)) {
                leftDirection = true;
                downDirection = true;
            }
            if ((key == KeyEvent.VK_SEMICOLON) && (!leftDirection && !upDirection)) {
                rightDirection = true;
                downDirection = true;
            }
        }

    }
}
