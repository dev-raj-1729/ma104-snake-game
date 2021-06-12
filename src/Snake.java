import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame {
    public Snake() {
        initUI();
    }

    private void initUI() {
        add(new Board());       //add Board JPanel to JFrame
        setResizable(false);        
        pack();                 //conforms JPanel to the size of JFrame

        setTitle("Snake");      
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
           
        });
    }
}
