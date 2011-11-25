package main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionEvent;
import squidpony.squidgrid.SGDisplay;
import squidpony.squidgrid.SGPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JApplet;

/**
 *
 * @author SquidPony
 */
public class SquidGridDemo extends JApplet {
    SGDisplay display;
    ControlPanel control;
    int c = 0, x = 50, y = 30;

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
        display = new SGPanel(x, y, new Font("Ariel", Font.PLAIN, 16));
        add(display.getComponent(), BorderLayout.CENTER);

        control = new ControlPanel();

        add(control, BorderLayout.NORTH);

        resize(980, 980);

        changeDisplay();
        repaint();

        control.updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                display.initDisplayByCell(control.getXSize(), control.getYSize(), control.getFontFace());
                changeDisplay();
                repaint();
            }
        });
    }

    @Override
    public void stop(){
        control.updateButton.removeActionListener(control.updateButton.getAction());
    }
    
    private void changeDisplay() {
        Random rng = new Random();
        c++;
        for (int i = 0; i < display.getColumns(); i++) {
            for (int k = 0; k < display.getRows(); k++) {
//                display.setBlock(k, i, (char) ('A' + c % 26));
                if (rng.nextBoolean()) {
//                    display.setBlock(k, i, (char) ('A' + rng.nextInt(26)));
                    display.setBlock(k, i, (char) ('A' + (i + k) % 26));
                } else {
//                    display.setBlock(k, i, (char) ('a' + rng.nextInt(26)));
                    display.setBlock(k, i, (char) ('a' + (i + k) % 26));
                }
            }
        }
        display.refresh();
    }
}
