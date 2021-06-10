import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    private final int DOTS = 900;
    private final int SIZE_DOT = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[DOTS];
    private final int y[] = new int[DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

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

        setPreferredSize(new Dimension(BOARD_WIDTH,BOARD_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon("src/images/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/images/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/images/head.png");
        head = iih.getImage();
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
            g.drawImage(apple,apple_x,apple_y,this);

            for (int z=0;z<dots;z++) {
                if(z==0){
                    g.drawImage(head,x[z],y[z],this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            
        }


    }

}
