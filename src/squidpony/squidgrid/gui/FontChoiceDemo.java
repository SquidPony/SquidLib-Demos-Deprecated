package squidpony.squidgrid.gui;

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
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.gui.swing.SwingPane;

/**
 * Demonstrates some of the capabilities of the squidpony.squidgrid package.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class FontChoiceDemo {

    private SwingPane display;
    private JMenu menu;
    private JFrame frame;
    private FontChoiceControlPanel control;
    private int width = 16, height = 16;
    private Random rng = new Random();
    private SColor foreground, background;

    public FontChoiceDemo() {
        frame = new JFrame("SquidGrid and SquidColor Font Choice Demonstration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //build menu
        JMenuBar bar = new JMenuBar();
        menu = new JMenu("Tools");
        bar.add(menu);
        JMenuItem tempItem = new JMenuItem("Save Image");
        tempItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        menu.add(tempItem);
        frame.setJMenuBar(bar);

        control = new squidpony.squidgrid.gui.FontChoiceControlPanel(width, height);
        frame.getContentPane().add(control, BorderLayout.NORTH);

        display = new SwingPane(width, height, control.getFontFace());
        frame.getContentPane().add(display, BorderLayout.SOUTH);

        String text = "";
        for (char c = 33; c <= 125; c++) {
            text += c;
        }
        control.inputTextArea.setText(text);

        control.updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                control.validateInput();
                char[] chars = control.inputTextArea.getText().toCharArray();
                if (chars.length > 0) {
                    display.ensureFits(chars);
                }
                display.getTextFactory().setAntialias(control.antialiasBox.isSelected());
                display.getTextFactory().setPadding(control.getLeftPad(), control.getRightPad(), control.getTopPad(), control.getBottomPad());
                if (control.cellSizeBox.isSelected()) {
                    display.initialize(control.getCellWidth(), control.getCellHeight(), control.getGridWidth(), control.getGridHeight(), control.getFontFace());
                } else {
                    display.initialize(control.getGridWidth(), control.getGridHeight(), control.getFontFace());
                }
                changeDisplay();
            }
        });

        control.validateInput();
        control.updateButton.doClick();

        frame.setVisible(true);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.repaint();
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
        control.fontSizeField.setText("" + display.getTextFactory().getFont().getSize());
        control.cellWidthField.setText("" + display.getCellDimension().width);
        control.cellHeightField.setText("" + display.getCellDimension().height);
        foreground = new SColor(control.foreColorPanel.getBackground());
        background = new SColor(control.backColorPanel.getBackground());
        String oldText = control.inputTextArea.getText();
        String text = "";
        for (char c : oldText.toCharArray()) {
            if (!Character.isISOControl(c) && c != '\n' && c != '\r') {
                text += c;
            }
        }
        char[] chars = text.toCharArray();
        if (chars.length > 0) {
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
