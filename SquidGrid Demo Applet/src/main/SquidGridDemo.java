package main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.ActionEvent;
import squidpony.squidgrid.ConcreteSGTextPanel;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JApplet;
import javax.swing.JFrame;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.SGTextDisplay;

/**
 *
 * @author SquidPony
 */
public class SquidGridDemo extends JApplet {
    SGTextDisplay display;
    ControlPanel control;
    int columns = 35, rows = 12;
    Random rng = new Random();
    SColor foreground, background;

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    @Override
    public void init() {
        // TODO start asynchronous download of heavy resources
    }
    // TODO overwrite start(), stop() and destroy() methods

    @Override
    public void start() {
        control = new ControlPanel(rows, columns);
        getContentPane().add(control, BorderLayout.NORTH);

        display = new ConcreteSGTextPanel(rows, columns, control.getFontFace());
        getContentPane().add(display.getComponent(), BorderLayout.SOUTH);
        resize(getContentPane().getPreferredSize());

        changeDisplay();
        repaint();

        control.updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                display.initialize(control.getRows(), control.getColumns(), control.getFontFace());
                changeDisplay();
                resize(getContentPane().getPreferredSize());
                repaint();
            }
        });

    }

    @Override
    public void stop() {
        control.updateButton.removeActionListener(control.updateButton.getAction());
    }

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
}
