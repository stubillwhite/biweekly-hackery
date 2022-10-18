import java.awt.event.*;
import javax.swing.*;

public class Scratchpad extends JFrame {

    public Scratchpad() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                System.out.println(keyCode);
            }
        });

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Scratchpad f = new Scratchpad();
            f.setFocusable(true);
            f.setVisible(true);
            f.setSize(200, 200);
        });
    }


}