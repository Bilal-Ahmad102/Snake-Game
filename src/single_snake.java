
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
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

public class single_snake extends JPanel implements ActionListener {


    private final int B_WIDTH = 600;
    private final int B_HEIGHT = 600;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;
    private long lastTime = System.nanoTime();
    private final long flickerInterval = 500 * 1000000;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;



    private boolean showPressSpaceMessage = true;
    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = true;

    


    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    String dot_path = "src/resources/dot.png";
    String head_path = "src/resources/head.png";
    String apple_path = "src/resources/apple.png";


    public single_snake() {
        initBoard();
    }
    
    private void initBoard() {
        addKeyListener(new TAdapter());

        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
        
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon(dot_path);
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon(apple_path);
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon(head_path);
        head = iih.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 300 - z * 10;
            y[z] = 300;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();  
            // Synchronizes the graphics state across different platforms to ensure smooth 
            // rendering

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        String gameOverMsg = "Game Over";
        String pressSpaceMsg = "Press Space to Restart";
        Font smallFont = new Font("Vibes", Font.BOLD, 30);
        Font pressSpaceFont = new Font("Vibes", Font.PLAIN, 20); // Adjust the font size as needed
        FontMetrics metrics = getFontMetrics(smallFont);
    
        // Draw the "Game Over" message
        g.setColor(Color.red);
        g.setFont(smallFont);
        g.drawString(gameOverMsg, (B_WIDTH - metrics.stringWidth(gameOverMsg)) / 2, B_HEIGHT / 2);
    
        // Calculate time elapsed since last flicker change
        long now = System.nanoTime();
        long elapsed = now - lastTime;
    
        if (elapsed > flickerInterval) {
            showPressSpaceMessage = !showPressSpaceMessage;
            lastTime = now;
        }
    
        if (showPressSpaceMessage) {
            g.setFont(pressSpaceFont);
            FontMetrics pressSpaceMetrics = getFontMetrics(pressSpaceFont);
            g.drawString(pressSpaceMsg, (B_WIDTH - pressSpaceMetrics.stringWidth(pressSpaceMsg)) / 2,
                    (B_HEIGHT / 2) + pressSpaceMetrics.getHeight() + 10); // Adjust the position as needed
        }
    
        repaint(); // Trigger repaint to update the display

    }
    

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

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

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            
            if (!inGame && key == KeyEvent.VK_SPACE) {
                inGame = true;
                initGame(); // Restart the game when space is pressed after Game Over
            }
        
            // Set the directions based on key presses
            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;


            } else if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;


            } else if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;


            } else if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;


            }
            if((key == KeyEvent.VK_E) && (!leftDirection && !downDirection) ){
              rightDirection = true;
              upDirection = true;

            }
            
            if((key == KeyEvent.VK_Q) && (!rightDirection && !downDirection) ){
              leftDirection = true;
              upDirection = true;

            }

            if((key == KeyEvent.VK_A) && (!rightDirection && !upDirection) ){
              leftDirection = true;
              downDirection = true;

            }

            if((key == KeyEvent.VK_D) && (!leftDirection && !upDirection) ){
              rightDirection = true;
              downDirection = true;

            }
            // Check for simultaneous right and up key press

        }

                
    }
}
