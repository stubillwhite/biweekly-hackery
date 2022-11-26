package snakegame.internal;

import com.google.common.collect.ImmutableMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import static snakegame.internal.Direction.*;

public class TextUI extends JPanel {

    public interface KeyPressListener {
        void handleKeyPress(Direction direction);
    }

    private final JTextArea textArea;

    public TextUI(int width, int height, KeyPressListener keyPressListener) {
        super(new GridBagLayout());

        textArea = new JTextArea(height, width);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        add(textArea, constraints);

        textArea.addKeyListener(new KeyAdapter() {
            final Map<Integer, Direction> controlMap = ImmutableMap.of(
                    37, LEFT,
                    38, UP,
                    39, RIGHT,
                    40, DOWN);

            @Override
            public void keyPressed(KeyEvent e) {
                final int keyCode = e.getKeyCode();
                if (controlMap.containsKey(keyCode)) {
                    keyPressListener.handleKeyPress(controlMap.get(keyCode));
                }
            }
        });
    }

    public void setContent(String text) {
        textArea.setText(text);
    }

    public static TextUI createTextUI(int width, int height, KeyPressListener userInputHandler) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final TextUI textUI = new TextUI(width, height, userInputHandler);

        frame.add(textUI);
        frame.pack();
        frame.setVisible(true);

        return textUI;
    }
}