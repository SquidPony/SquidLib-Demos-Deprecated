package squidpony.squidgrid.gui.swing.animation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.gui.swing.SwingPane;
import squidpony.squidgrid.util.DirectionIntercardinal;

/**
 * Demonstrates the use of animations in SGTextPanel.
 *
 * Change which animations are commented out in the internal Spawner class at
 * the bottom of the file to see the different animations.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class MovingCharactersDemo {

    private SwingPane display;//uses SGTextAndImagePanel instead of SGTextPanel in order to show it's usable as an in-place replacement/extension
    private JFrame frame;
    private static char[][] map = new char[][]{//in order to be in line with GUI coordinate pairs, this appears to be sideways in this style constructor.
        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
        {'#', '.', '.', '.', ' ', '.', '.', '.', ' ', '.', '.', '#'},
        {'#', '.', '.', '.', 's', '.', '.', '.', 'S', '.', '.', '#'},
        {'#', '.', '.', '.', 'q', '.', '.', '.', 'q', '.', '.', '#'},
        {'#', '.', '.', '.', 'u', '.', '.', '.', 'u', '.', '.', '#'},
        {'#', '.', '.', '.', 'i', '.', '.', '.', 'i', '.', '.', '#'},
        {'#', '.', '.', '.', 'd', '.', '.', '.', 'd', '.', '.', '#'},
        {'#', '.', '.', '.', 'p', '.', '.', '.', 'L', '.', '.', '#'},
        {'#', '.', '.', '.', 'o', '.', '.', '.', 'i', '.', '.', '#'},
        {'#', '.', '.', '.', 'n', '.', '.', '.', 'b', '.', '.', '#'},
        {'#', '.', '.', '.', 'y', '.', '.', '.', ' ', '.', '.', '#'},
        {'#', '.', '.', '.', '.', '.', '.', '.', 'A', '.', '.', '#'},
        {'#', '.', '.', '.', 'c', '.', '.', '.', 'n', '.', '.', '#'},
        {'#', '.', '.', '.', 'o', '.', '.', '.', 'i', '.', '.', '#'},
        {'#', '.', '.', '.', 'm', '.', '.', '.', 'm', '.', '.', '#'},
        {'#', '.', '.', '.', ' ', '.', '.', '.', ' ', '.', '.', '#'},
        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };
    private int width = map.length, height = map[0].length;
    private Random rng = new Random();
    private boolean spawning = false;
    private Point start = new Point();

    public static void main(String... args) {
        new MovingCharactersDemo().go();
    }

    private MovingCharactersDemo() {

        frame = new JFrame("SquidGrid Moving Objects Demonstration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        display = new SwingPane();
        display.initialize(width, height, new Font("Ariel", Font.BOLD, 20));
        display.setText(map);
        display.refresh();
        frame.getContentPane().add(display, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.createBufferStrategy(2);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.repaint();

        JOptionPane.showMessageDialog(frame, "Click inside the window to start the animation example.\nClick again to stop the animation.");

        Timer spawner = new Timer(100, new Spawner());
        spawner.start();
    }

    private void go() {
        display.addMouseListener(new DemoInputListener());
    }

    private SColor getRandomColor() {
        return SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
    }

    private void slide() {
        start.x = rng.nextInt(display.getGridWidth());
        start.y = rng.nextInt(display.getGridHeight());
        display.placeCharacter(start.x, start.y, '@', getRandomColor());
        display.slide(start, new Point(rng.nextInt(display.getGridWidth()), rng.nextInt(display.getGridHeight())));
        display.placeCharacter(start.x, start.y, map[start.x][start.y]);//replaces the background as the object moves off of it

        display.refresh();
    }

    private void wiggle() {
        start.x = rng.nextInt(display.getGridWidth());
        start.y = rng.nextInt(display.getGridHeight());
        display.placeCharacter(start.x, start.y, '@', getRandomColor());

        display.wiggle(start);

        display.refresh();
    }

    private void bump() {
        start.x = rng.nextInt(display.getGridWidth());
        start.y = rng.nextInt(display.getGridHeight());
        display.placeCharacter(start.x, start.y, '@', getRandomColor());

        display.bump(start, DirectionIntercardinal.values()[rng.nextInt(DirectionIntercardinal.values().length)]);

        display.refresh();
    }

    private class DemoInputListener implements MouseInputListener {

        @Override
        public void mouseClicked(MouseEvent me) {
            spawning = !spawning;
        }

        @Override
        public void mousePressed(MouseEvent me) {
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    private class Spawner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (spawning) {

                //uncomment these to have these animations happen
                slide();
                // wiggle();
                // bump();
            }
        }
    }
}
