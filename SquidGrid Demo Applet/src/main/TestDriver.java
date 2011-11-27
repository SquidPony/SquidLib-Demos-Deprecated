package main;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JFrame;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.SGTextDisplay;
import squidpony.squidgrid.ConcreteSGTextPanel;

/**
 * Creates a simple JFrame to display the arbitrary font usability and 
 * colorization capabilities of SquidGrid.
 *
 * @author Eben
 */
public class TestDriver {
    SGTextDisplay display;
    JFrame frame;
    ControlPanel control;
    int columns = 35, rows = 12;
    Random rng = new Random();
    SColor foreground, background;

    public TestDriver() {
        frame = new JFrame("SquidGrid and SquidColor Demonstration");
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage("SquidPony SP Icon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        control = new ControlPanel(rows, columns);
        frame.getContentPane().add(control, BorderLayout.NORTH);
        
        display = new ConcreteSGTextPanel(rows, columns, control.getFontFace());
        frame.getContentPane().add(display.getComponent(), BorderLayout.SOUTH);
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
        for (int column = 0; column < display.getColumns(); column++) {
            for (int row = 0; row < display.getRows(); row++) {
                if (control.colorizeToggleButton.isSelected()) {
                    foreground = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                    background = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                }
                if (rng.nextBoolean()) {
                    display.setBlock(column, row, (char) ('A' + (column + row) % 26), foreground, background);
                } else {
                    display.setBlock(column, row, (char) ('a' + (column + row) % 26), foreground, background);
                }
            }
        }
    }

    public static void main(String args[]) {
        new TestDriver();
    }
}
