package squidpony.squidgrid;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JFrame;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.gui.SGTextPanel;

/**
 * Demonstrates some of the capabilities of the squidpony.squidgrid package.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class FontChoiceDemo {
    private SGTextPanel display;
    private JFrame frame;
    private FontChoiceControlPanel control;
    private int width = 35, height = 12;
    private Random rng = new Random();
    private SColor foreground, background;

    public FontChoiceDemo() {
        frame = new JFrame("SquidGrid and SquidColor Font Choice Demonstration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        control = new squidpony.squidgrid.FontChoiceControlPanel(width, height);
        frame.getContentPane().add(control, BorderLayout.NORTH);

        display = new SGTextPanel(width, height, control.getFontFace());
        frame.getContentPane().add(display, BorderLayout.SOUTH);
        frame.setVisible(true);

        changeDisplay();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.repaint();

        control.updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                display.initialize(control.getRows(), control.getColumns(), control.getFontFace());
                changeDisplay();
                frame.pack();
                frame.repaint();
            }
        });

    }

    /**
     * Uses the information in the control panel to update the display.
     */
    private void changeDisplay() {
        foreground = SColor.BLACK;
        background = SColor.WHITE;
        for (int x = 0; x < display.getGridWidth(); x++) {
            for (int y = 0; y < display.getGridHeight(); y++) {
                if (control.colorizeToggleButton.isSelected()) {
                    foreground = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                    background = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                }
                if (rng.nextBoolean()) {
                    display.placeCharacter(x, y, (char) ('A' + (x + y) % 26), foreground, background);
                } else {
                    display.placeCharacter(x, y, (char) ('a' + (x + y) % 26), foreground, background);
                }
            }
        }
        display.refresh();
    }

    public static void main(String... args) {
        new FontChoiceDemo();
    }
}
