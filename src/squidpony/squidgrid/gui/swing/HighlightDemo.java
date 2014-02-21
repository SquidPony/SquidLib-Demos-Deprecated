package squidpony.squidgrid.gui.swing;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * Demonstrates the use of SGKeyListener
 *
 * @author Eben Howard - http://squidpony.com - eben@squidpony.com
 */
public class HighlightDemo {

    private JFrame frame;
    private SwingPane display;
    private int width = 20, height = 20;
    private Point highlight = new Point(10, 10);

    public static void main(String[] args) {
        new HighlightDemo();
    }

    private HighlightDemo() {
        frame = new JFrame("Highlight Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display = new SwingPane();
        display.getTextFactory().setPadding(0, 0, 3, 0);//adds a bit of vertical padding
        display.getTextFactory().setAntialias(true);//make sure characters are smooth
        display.initialize(width, height, new Font("Ariel", Font.BOLD, 32));
        frame.add(display);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);//center the frame on the screen

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                display.placeCharacter(x, y, 'G');
            }
        }

        display.highlight(highlight.x, highlight.y);
        display.refresh();

        frame.addKeyListener(new KeyEchoListener());
    }

    private class KeyEchoListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (highlight.x > 0) {
                        highlight.x--;
                        display.highlight(highlight.x, highlight.y);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (highlight.x < width - 1) {
                        highlight.x++;
                        display.highlight(highlight.x, highlight.y);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (highlight.y > 0) {
                        highlight.y--;
                        display.highlight(highlight.x, highlight.y);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (highlight.y < height - 1) {
                        highlight.y++;
                        display.highlight(highlight.x, highlight.y);
                    }
                    break;
                default:
                    display.placeCharacter(highlight.x, highlight.y, e.getKeyChar());
            }
            display.refresh();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
