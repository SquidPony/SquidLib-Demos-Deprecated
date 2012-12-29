package squidpony.squidgrid;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import squidpony.squidgrid.gui.SGTextPanel;

/**
 * Demonstrates the use of SGKeyListener
 *
 * @author Eben Howard - http://squidpony.com - eben@squidpony.com
 */
public class SGKeyEchoDemo {

    private JFrame frame;
    private SGTextPanel display;
    private int width = 50, height = 20;
    private int x = 0, y = 1;
    private String input = "";

    public static void main(String[] args) {
        new SGKeyEchoDemo();
    }

    private SGKeyEchoDemo() {
        frame = new JFrame("Key Echo Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display = new SGTextPanel(width, height, new Font("Sans Serif", Font.BOLD, 18));
        frame.add(display);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);//center the frame on the screen
        display.placeHorizontalString(0, 0, "Type input please.");
        display.refresh();

        frame.addKeyListener(new KeyEchoListener());
    }

    private void addChar(char c) {
        input += c;
        if (x >= width) {
            x = 0;
            y++;
        }

        if (y < height) {
            //everything's fine, print the character
            display.placeCharacter(x, y, c);
            x++;
            display.refresh();
        }
    }

    private void removeChar() {
        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
        }
        if (x > 0) {
            x--;
        } else if (y > 1) {
            y--;
            x = width - 1;
        }

        display.placeCharacter(x, y, ' ');//clear space
        display.refresh();
    }

    private void clearDisplay() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                display.placeCharacter(i, j, ' ');
            }
        }
        display.refresh();
    }

    private class KeyEchoListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {//cover both windows and unix style line returns
                clearDisplay();
                System.out.println(input);
                display.placeHorizontalString(0, 0, "Text: " + input);
                display.refresh();
                System.out.println("Full text entered: " + input);
                input = "";
                x = 0;
                y = 1;
            } else if (e.getKeyChar() == '\b') {//backspace character
                removeChar();
            } else {
                addChar(e.getKeyChar());
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
