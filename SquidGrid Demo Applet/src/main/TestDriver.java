package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JFrame;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.SGDisplay;
import squidpony.squidgrid.SGPanel;

/**
 * Creates a simple JFrame to display the arbitrary font usability and 
 * colorization capabilities of SquidGrid.
 *
 * @author Eben
 */
public class TestDriver {
    SGDisplay display;
    JFrame frame;
    ControlPanel control;
    int x = 33, y = 22;
    Random rng = new Random();
    SColor foreground, background;

    public TestDriver() {
        display = new SGPanel(x, y, new Font("Ariel", Font.PLAIN, 16));
        frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(display.getComponent(), BorderLayout.CENTER);

        control = new ControlPanel(x, y);
        frame.getContentPane().add(control, BorderLayout.NORTH);
        frame.setVisible(true);

        display.initDisplayByCell(x, y, control.getFontFace());
        changeDisplay();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.repaint();

        control.updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                display.initDisplayByCell(control.getXSize(), control.getYSize(), control.getFontFace());
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
        for (int i = 0; i < display.getColumns(); i++) {
            for (int k = 0; k < display.getRows(); k++) {
                if (control.colorizeToggleButton.isSelected()) {
                    foreground = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                    background = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                }
                if (rng.nextBoolean()) {
                    display.setBlock(k, i, (char) ('A' + (i + k) % 26), foreground, background);
                } else {
                    display.setBlock(k, i, (char) ('a' + (i + k) % 26), foreground, background);
                }
            }
        }
    }

    public static void main(String args[]) {
        new TestDriver();
    }
}
