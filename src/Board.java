import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    private final int MAX_DOTS = 900;
    private final int SIZE_DOT = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 70;
    private final int X_OFFSET =10;
    private final int Y_OFFSET = 10;
    private final int x[] = new int[MAX_DOTS];
    private final int y[] = new int[MAX_DOTS];
    private final int SCALE = 2;
    private int dots;
    private int apple_x;
    private int apple_y;
    private int time_steps =0;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        initBoard();
    }
    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension((BOARD_WIDTH+2*X_OFFSET)*SCALE,(Y_OFFSET+BOARD_HEIGHT+50)*SCALE));
        loadImages();
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

        for (int z=0;z<dots;z++){
            x[z] = 50-z*10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY,this);
        timer.start();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple,(apple_x+X_OFFSET)*SCALE,(apple_y+Y_OFFSET)*SCALE,this);

            for (int z=0;z<dots;z++) {
                if(z==0){
                    g.drawImage(head,(x[z]+X_OFFSET)*SCALE,(y[z]+Y_OFFSET)*SCALE,this);
                } else {
                    g.drawImage(ball, (x[z]+X_OFFSET)*SCALE, (y[z]+Y_OFFSET)*SCALE, this);
                }
            }
            Font small = new Font("Helvetica",Font.BOLD,14*SCALE);
            g.setFont(small);
            g.setColor(Color.white);
            // g.drawLine(5, 5, BOARD_WIDTH, 5);
            // g.drawLine(x1, y1, x2, y2);
            // g.drawLine(5, BOARD_HEIGHT + Y_OFFSET+5, BOARD_WIDTH+5,BOARD_HEIGHT+Y_OFFSET+5);
            g.drawRect(5, 5, (BOARD_WIDTH+X_OFFSET)*SCALE, (BOARD_HEIGHT + Y_OFFSET)*SCALE);
            g.drawString("TIME: "+time_steps/10, (50)*SCALE, (BOARD_HEIGHT+40)*SCALE);
            g.drawString("SCORE: "+(dots-3)*10, (BOARD_WIDTH-100)*SCALE, (BOARD_HEIGHT+40)*SCALE);
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }


    }
    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica",Font.BOLD,14*SCALE);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg))/2,((BOARD_HEIGHT+Y_OFFSET)/2)*SCALE);
        Font sub = new Font("Helvetica",Font.BOLD,12*SCALE);
        String msg2 = "Press R to restart or Esc to exit";
        g.setFont(sub);
        g.drawString(msg2,((2*X_OFFSET+BOARD_WIDTH)*SCALE-metr.stringWidth(msg2))/2,((40+BOARD_HEIGHT+Y_OFFSET)/2)*SCALE);
        // initBoard();
        // inGame = true;
        
        // setBackground(Color.white);
        
    }

    private void checkApple() {
        if((x[0]==apple_x) && (y[0] == apple_y)) {
            dots++;
            locateApple();
        }
    }

    private void move() {
        time_steps++;
        for (int z= dots;z>0;z--) {
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }

        if (leftDirection) {
            x[0] -= SIZE_DOT;
        } else if(rightDirection) {
            x[0] += SIZE_DOT;
        } else if(upDirection) {
            y[0] -= SIZE_DOT;
        } else if(downDirection){
            y[0] += SIZE_DOT;
        }
    }

    private void checkCollision() {
        
        for (int z = dots;z>0;z--) {
            if((dots>4) &&(x[0]==x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        if (y[0] >= BOARD_HEIGHT) {
            inGame = false;
        } else if(y[0]<0) {
            inGame = false;
        } else if (x[0]>=BOARD_WIDTH) {
            inGame = false;
        } else if(x[0] < 0) {
            inGame = false;
        }
        if(!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random()* RAND_POS);
        apple_x = ((r*SIZE_DOT));

        r= (int) (Math.random()*RAND_POS);
        apple_y = ((r*SIZE_DOT));
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if(!inGame && key == KeyEvent.VK_R){
                initBoard();
                inGame = true;
            }
            else if(!inGame && key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
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
