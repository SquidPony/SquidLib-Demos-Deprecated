package squidpony.squidgrid;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.gui.SGTextPanel;

/**
 * Demonstrates some of the capabilities of the squidpony.squidgrid package.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class FontChoiceDemo {

    private SGTextPanel display;
    private String text = "";
    private JMenu menu;
    private JFrame frame;
    private FontChoiceControlPanel control;
    private int width = 35, height = 12;
    private Random rng = new Random();
    private SColor foreground, background;

    public FontChoiceDemo() {
        frame = new JFrame("SquidGrid and SquidColor Font Choice Demonstration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //build menu
        JMenuBar bar = new JMenuBar();
        menu = new JMenu("Tools");
        bar.add(menu);
        JMenuItem tempItem = new JMenuItem("Input Text");
        tempItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getText();
            }
        });
        menu.add(tempItem);

        tempItem = new JMenuItem("Save Image");
        tempItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        menu.add(tempItem);
        frame.setJMenuBar(bar);

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
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (control.cellSzieBox.isSelected()) {
                    display.initialize(control.getCellWidth(), control.getCellHeight(), control.getRows(), control.getColumns(), control.getFontFace());
                } else {
                    display.initialize(control.getRows(), control.getColumns(), control.getFontFace());
                }
                changeDisplay();
            }
        });

    }

    /**
     * Gets user input text from a popup dialog.
     */
    private void getText() {
        text = (String) JOptionPane.showInputDialog(frame, "Input Text", "Customize Text", JOptionPane.PLAIN_MESSAGE, null, null, "");
        changeDisplay();
    }

    /**
     * Saves the current display panel as an image.
     */
    private void saveImage() {
        BufferedImage image = (BufferedImage) display.createImage(display.getWidth(), display.getHeight());
        Graphics2D g = image.createGraphics();
        display.paint(g);
        g.dispose();
        try {
            ImageIO.write(image, "png", new File("text.png"));
        } catch (IOException ex) {
            Logger.getLogger(FontChoiceDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Uses the information in the control panel to update the display.
     */
    private void changeDisplay() {
        foreground = SColor.BLACK;
        background = SColor.WHITE;
        if (text.length() > 0) {
            char[] chars = text.toCharArray();
            display.ensureFits(chars, control.whiteSpaceBox.isSelected());
            int position = 0;
            for (int y = 0; y < display.getGridHeight(); y++) {
                for (int x = 0; x < display.getGridWidth(); x++) {
                    if (control.colorizeToggleButton.isSelected()) {
                        foreground = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                        background = SColor.FULL_PALLET[rng.nextInt(SColor.FULL_PALLET.length)];
                    }
                    if (position < chars.length) {
                        display.placeCharacter(x, y, chars[position], foreground, background);
                        position++;
                    } else {
                        display.placeCharacter(x, y, ' ', foreground, background);
                    }
                }
            }
        } else {
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
        }
        display.refresh();
        frame.pack();
        frame.repaint();
    }

    public static void main(String... args) {
        new FontChoiceDemo();
    }
}
