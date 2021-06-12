import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {

    /*CONSTANTS*/

    private static final int BOARD_WIDTH = 300;             // Width of the playing field
    private static final int BOARD_HEIGHT = 300;            // Height of the playing field
    private static final int MAX_DOTS = 900;                // Max length of the snake
    private static final int SIZE_DOT = 10;                 // Size of Dot image in pixels
    private static final int RAND_POS = 29;                 // Used for randomizing location of apple
    private static final int X_OFFSET =10;                  // For positioning vertical borders
    private static final int Y_OFFSET = 10;                 // For positioning horizontal borders
    private final int [] dotsXcord = new int[MAX_DOTS];     // X coordinates of the snake's body starting from head
    private final int [] dotsYcord = new int[MAX_DOTS];     // Y coordinates of the snake's body starting from head
    private static final int SCALE = 2;                     // to Scale up or down the JPanel size
    private static final String FONTFAMILY = "Helvetica";   // Font for displaying messages


    private  int delay = 70;                                // delay between snake movements
    private int dots;                                       // number of current dots
    private int appleX;                                     // X coordinate of apple
    private int appleY;                                     // Y coordinate of apple
    private int timeSteps =0;                               // number of timeSteps passed calculated as (Number of snake movements)/10
    private boolean leftDirection = false;                  // Movement variables
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean askDifficulty = true;
    private Timer timer;                                    // for controlling snake movement and game speed
    private transient Image ball;                           // image used for ball (snake body)
    private transient Image apple;                          // image used for apple 
    private transient Image head;                           // image used for snake head

    public Board() {
        initBoard();                                        // initialize Board variables
    }
    private void initBoard() {
        addKeyListener(new TAdapter());                     // add Keyboard listener
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension((BOARD_WIDTH+2*X_OFFSET)*SCALE,(Y_OFFSET+BOARD_HEIGHT+50)*SCALE));
        loadImages();                                       // load images of ball,apples and snake head
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon("src/images/dot.png");
        ball = iid.getImage().getScaledInstance(10*SCALE, 10*SCALE, java.awt.Image.SCALE_SMOOTH);

        ImageIcon iia = new ImageIcon("src/images/apple-black.png");
        apple = iia.getImage().getScaledInstance(10*SCALE, 10*SCALE, java.awt.Image.SCALE_SMOOTH);

        ImageIcon iih = new ImageIcon("src/images/head.png");
        head = iih.getImage().getScaledInstance(10*SCALE, 10*SCALE, java.awt.Image.SCALE_SMOOTH);
    }

    private void initGame() {
        dots = 3;
        

        // sets snake's initial position
        for (int z=0;z<dots;z++){                  
            dotsXcord[z] = 50-z*10;
            dotsYcord[z] = 50;
        }

        // creates an apple at a random location
        locateApple();                             

        
        timer = new Timer(delay,this);
        timer.start();

    }

    // for displaying the graphics 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if(askDifficulty) {
            chooseDifficulty(g);    
        }
        else if (inGame) {
            g.drawImage(apple,(appleX+X_OFFSET)*SCALE,(appleY+Y_OFFSET)*SCALE,this); 

            for (int z=0;z<dots;z++) {
                if(z==0){
                    g.drawImage(head,(dotsXcord[z]+X_OFFSET)*SCALE,(dotsYcord[z]+Y_OFFSET)*SCALE,this);
                } else {
                    g.drawImage(ball, (dotsXcord[z]+X_OFFSET)*SCALE, (dotsYcord[z]+Y_OFFSET)*SCALE, this);
                }
            }
            Font small = new Font(FONTFAMILY,Font.BOLD,14*SCALE);   //Font for displaying time and score
            g.setFont(small);
            g.setColor(Color.white);
            g.drawRect(5, 5, (BOARD_WIDTH+X_OFFSET)*SCALE, (BOARD_HEIGHT + Y_OFFSET)*SCALE);
            g.drawString("TIME: "+timeSteps/10, (50)*SCALE, (BOARD_HEIGHT+40)*SCALE);
            g.drawString("SCORE: "+(dots-3)*10, (BOARD_WIDTH-100)*SCALE, (BOARD_HEIGHT+40)*SCALE);
            Toolkit.getDefaultToolkit().sync();                     // makes sure display is up to date
        } else {
            gameOver(g);
        }


    }
    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font(FONTFAMILY,Font.BOLD,14*SCALE);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg))/2,((BOARD_HEIGHT+Y_OFFSET)/2)*SCALE);
        Font sub = new Font(FONTFAMILY,Font.BOLD,12*SCALE);
        String msg2 = "Press R to restart or Esc to exit";
        g.setFont(sub);
        g.drawString(msg2,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg2))/2,((40+BOARD_HEIGHT+Y_OFFSET)/2)*SCALE);
        timer.stop();
    }
    private void chooseDifficulty(Graphics g) {
        String msg = "Choose your Difficulty";
        Font small = new Font(FONTFAMILY,Font.BOLD,14*SCALE);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg))/2,((BOARD_HEIGHT+Y_OFFSET)/2)*SCALE);
        String msg1 = "[E] Easy";
        g.drawString(msg1,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg))/2,((BOARD_HEIGHT+Y_OFFSET)/2 + 20)*SCALE);
        msg1 = "[M] Medium";
        g.drawString(msg1,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg))/2,((BOARD_HEIGHT+Y_OFFSET)/2 + 40)*SCALE);
        msg1 = "[H] Hard";
        g.drawString(msg1,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg))/2,((BOARD_HEIGHT+Y_OFFSET)/2 + 60)*SCALE);
    }
    private void checkApple() {
        // if snake collided with apple increase snake length and create new apple
        if((dotsXcord[0]==appleX) && (dotsYcord[0] == appleY)) {
            dots++;
            locateApple();
        }
    }

    private void move() {
        timeSteps++;
        for (int z= dots;z>0;z--) {
            dotsXcord[z] = dotsXcord[(z-1)];
            dotsYcord[z] = dotsYcord[(z-1)];
        }

        if (leftDirection) {
            dotsXcord[0] -= SIZE_DOT;
        } else if(rightDirection) {
            dotsXcord[0] += SIZE_DOT;
        } else if(upDirection) {
            dotsYcord[0] -= SIZE_DOT;
        } else if(downDirection){
            dotsYcord[0] += SIZE_DOT;
        }
    }

    private void checkCollision() {
        
        for (int z = dots;z>0;z--) {
            if((dotsXcord[0]==dotsXcord[z]) && (dotsYcord[0] == dotsYcord[z])) {
                inGame = false;
            }
        }
        // checking for collisions with borders
        if (dotsYcord[0] >= BOARD_HEIGHT) {
            inGame = false;
        } else if(dotsYcord[0]<0) {
            inGame = false;
        } else if (dotsXcord[0]>=BOARD_WIDTH) {
            inGame = false;
        } else if(dotsXcord[0] < 0) {
            inGame = false;
        }
        if(!inGame) {
            timer.stop();
        }
    }

    //function for creating a new apple at a random location
    private void locateApple() {
        int r = (int) (Math.random()* RAND_POS);
        appleX = (r*SIZE_DOT);

        r= (int) (Math.random()*RAND_POS);
        appleY = (r*SIZE_DOT);
    }

    // function that is executed each time timer goes off
    @Override
    public void actionPerformed(ActionEvent e){
        if(inGame && !askDifficulty) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    // function for getting keyboard inputs
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if(!inGame && key == KeyEvent.VK_R){
                initBoard();
                inGame = true;
                rightDirection = true;
                leftDirection= false;
                upDirection = false;
                downDirection = false;
                timeSteps = 0;
                askDifficulty = true;
            }
            else if(!inGame && key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            if(askDifficulty && key == KeyEvent.VK_H) {
                askDifficulty = false;
                delay = 70;
                timer.setDelay(delay);
            } else if (askDifficulty && key == KeyEvent.VK_M) {
                askDifficulty = false;
                delay = 140;
                timer.setDelay(delay);
            } else if (askDifficulty && key == KeyEvent.VK_E) {
                askDifficulty = false;
                delay = 210;
                timer.setDelay(delay);
            }
            if((key == KeyEvent.VK_LEFT)&&(!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key==KeyEvent.VK_RIGHT)&&(!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if((key == KeyEvent.VK_UP)&&(!downDirection)){
                upDirection  = true;
                rightDirection = false;
                leftDirection = false;
            }
            if((key==KeyEvent.VK_DOWN)&&(!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

}
