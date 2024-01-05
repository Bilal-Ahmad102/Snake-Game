
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class three_player extends JPanel implements ActionListener {

    private Snake_1 snake_1;
    private Snake_1.TAdapter_1 kAdapter_1;

    private snake_2 snake_2;
    private snake_2.TAdapter_1 kAdapter_2;

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

    private JLabel scoreLabel1;
    private JLabel scoreLabel2;
    private JLabel scoreLabel3;

    private int score1 = 0;
    private int score2 = 0;
    private int score3 = 0;
    


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


    public  three_player() {

        // Initialize the score labels
        scoreLabel1 = new JLabel("Snake 1: " + score1);
        scoreLabel2 = new JLabel("Snake 2: " + score2);
        scoreLabel3 = new JLabel("Snake 3: " + score3);

        // Set the location and font for score labels
        scoreLabel1.setForeground(Color.WHITE);
        scoreLabel1.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel1.setBounds(10, 10, 100, 30);

        scoreLabel2.setForeground(Color.WHITE);
        scoreLabel2.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel2.setBounds(200, 10, 150, 30);

        scoreLabel3.setForeground(Color.WHITE);
        scoreLabel3.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel3.setBounds(400, 10, 150, 30);

        // Add score labels to the board
        add(scoreLabel1);
        add(scoreLabel2);
        add(scoreLabel3);
        
        snake_1 = new Snake_1();
        kAdapter_1 = snake_1.new TAdapter_1();

        snake_2 = new snake_2(); // Initialize snake_3
        kAdapter_2 = snake_2.new TAdapter_1(); // Initialize the adapter for snake_3

        initBoard();

        

    }


    
    private void initBoard() {
        addKeyListener(new TAdapter());
        addKeyListener(kAdapter_1);
        addKeyListener(kAdapter_2); // This is your imported TAdapter from Snake_1

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
    public void setTimer(Timer t) {
        this.timer = t;
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 300 - z * 10;
            y[z] = 300;
        }
        locateApple();

        timer = new Timer(DELAY,this);
        timer.start();
        setTimer(timer);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
        snake_1.doDrawing(g);
        snake_2.doDrawing(g);
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
            updateScore1(score1 + 1);
            locateApple();
        }

        if ((snake_1.x[0] == apple_x) && (snake_1.y[0] == apple_y)) {
            snake_1.dots++;
            updateScore2(score2 + 1);
            locateApple();
        }
        
        if ((snake_2.x[0] == apple_x) && (snake_2.y[0] == apple_y)) {
            snake_2.dots++;
            updateScore3(score3 + 1);
            locateApple();
        }
    }

        // Method to update the score for Snake 1
    public void updateScore1(int score) {
        score1 = score;
        scoreLabel1.setText("Snake 1: " + score1);
    }

    // Method to update the score for Snake 2
    public void updateScore2(int score) {
        score2 = score;
        scoreLabel2.setText("Snake 2: " + score2);
    }

    // Method to update the score for Snake 3
    public void updateScore3(int score) {
        score3 = score;
        scoreLabel3.setText("Snake 3: " + score3);
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
    public boolean checkSnakeCollision() {
        for (int i = 0; i < dots; i++) {
            if ((x[0] == snake_1.x[i] && y[0] == snake_1.y[i]) || (x[0] == snake_2.x[i] && y[0] == snake_2.y[i])) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkSnake_1Collision() {
        for (int i = 0; i < snake_1.dots; i++) {
            if ((snake_1.x[0] == x[i] && snake_1.y[0] == y[i]) || (snake_1.x[0] == snake_2.x[i] && snake_1.y[0] == snake_2.y[i])) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkSnake_2Collision() {
        for (int i = 0; i < snake_2.dots; i++) {
            if ((snake_2.x[0] == x[i] && snake_2.y[0] == y[i]) || (snake_2.x[0] == snake_1.x[i] && snake_2.y[0] == snake_1.y[i])) {
                return true;
            }
        }
        return false;
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
            if(checkSnakeCollision() ){
                updateScore1(score1 - 5);
            }
            if(checkSnake_1Collision() ){
                updateScore2(score2 - 5);
            }
            if(checkSnake_2Collision() ){
                updateScore3(score3 - 5);
            }

            
            
            checkApple();
            checkCollision();
            snake_1.move();
            snake_2.move();
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
                snake_1.inGame = true;
                snake_2.inGame = true;
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
            if((key == KeyEvent.VK_O) && (!leftDirection && !downDirection) ){
              rightDirection = true;
              upDirection = true;

            }
            
            if((key == KeyEvent.VK_I) && (!rightDirection && !downDirection) ){
              leftDirection = true;
              upDirection = true;

            }

            if((key == KeyEvent.VK_K) && (!rightDirection && !upDirection) ){
              leftDirection = true;
              downDirection = true;

            }

            if((key == KeyEvent.VK_SEMICOLON) && (!leftDirection && !upDirection) ){
              rightDirection = true;
              downDirection = true;

            }

        }

                
    }
}
