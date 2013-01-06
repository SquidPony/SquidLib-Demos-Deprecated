package squidbridge;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import squidpony.squidcolor.SColor;
import squidpony.squidcolor.SColorFactory;
import squidpony.squidgrid.gui.ImageCellMap;
import squidpony.squidgrid.gui.SwingPane;
import squidpony.squidgrid.util.Direction;

/**
 * A frame that starts up a game of SquidBridge.
 *
 * @author Eben Howard - http://squidpony.com
 */
public class SquidBridges extends JFrame implements MouseListener, MouseMotionListener {

    private SwingPane display;
    private Hashiwokakero board = new Hashiwokakero(40, 20);
    private boolean imagesLoaded = true; //allows a text only version if images fail to load
    private ArrayList<Character> imageReplacements = new ArrayList<Character>();//used to know when an image is available. A String would be better in this case, but this example shows the generic case

    /**
     * Creates new form SquidBridges and initializes the display and board
     */
    public SquidBridges() {

        initComponents();
        BufferedImage singlevert = null;
        try {
            singlevert = ImageIO.read(new File("./images/singlevertical.png"));
        } catch (IOException ex) {
            imagesLoaded = false;
        }

        display = new SwingPane();
        if (!imagesLoaded) {
            display.initialize(board.width, board.height, new Font("Serif", Font.BOLD, 24));//initialize just for text
        } else {
            display.initialize(singlevert.getWidth(), singlevert.getHeight(), board.width, board.height, new Font("Serif", Font.BOLD, 34));//initialize for graphic tiles
        }

        //load up the image cell factory with the desired images
        if (imagesLoaded) {//only check if the first one already worked
            try {
                ImageCellMap imageMap = new ImageCellMap(new Dimension(singlevert.getWidth(), singlevert.getHeight()));
                imageMap.addImage("|", singlevert);
                imageMap.addImage("H", ImageIO.read(new File("./images/doublevertical.png")));
                imageMap.addImage("-", ImageIO.read(new File("./images/singlehorizontal.png")));
                imageMap.addImage("=", ImageIO.read(new File("./images/doublehorizontal.png")));
                imageReplacements.add('|');
                imageReplacements.add('H');
                imageReplacements.add('-');
                imageReplacements.add('=');
                for (int i = 1; i <= 8; i++) {
                    imageMap.addImage(String.valueOf(i), ImageIO.read(new File("./images/island" + i + ".png")));
                    imageReplacements.add((char) ('0' + i));
                }
                display.setImageCellMap(imageMap);
            } catch (IOException ex) {
                imagesLoaded = false;
            }
        }

        //set up and show the frame
        setTitle("SquidBridges");
        add(display);

        drawGuessedBoard();
        int w = display.getPreferredSize().width + this.getInsets().left + this.getInsets().right;
        int h = display.getPreferredSize().height + this.getInsets().top + this.getInsets().bottom;
        setPreferredSize(new Dimension(w, h));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        attachListeners();
    }

    private void drawGuessedBoard() {
        Color back, front;
        char c;
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                back = SColor.BEIGE;
                if (board.isLocked(x, y)) {
                    front = SColor.AZUL;
                } else {
                    front = SColor.BLACK;
                }
                switch (board.getGuessedTile(x, y)) {
                    case EMPTY:
                        c = ' ';
                        break;
                    case NODE:
                        c = String.valueOf(board.guessedQuantity[x][y]).charAt(0);
                        break;
                    case VERTICAL_LINE:
                        switch (board.guessedQuantity[x][y]) {
                            case 0:
                                c = ' ';
                                break;
                            case 1:
                                c = '|';
                                break;
                            case 2:
                                c = 'H';
                                break;
                            default:
                                c = 'X';//error
                        }
                        break;
                    case HORIZONTAL_LINE:
                        switch (board.guessedQuantity[x][y]) {
                            case 0:
                                c = ' ';
                                break;
                            case 1:
                                c = '-';
                                break;
                            case 2:
                                c = '=';
                                break;
                            default:
                                c = 'X';//error
                        }
                        break;
                    default:
                        c = 'X';//error

                }
                if (imagesLoaded && imageReplacements.contains(c)) {
                    if (board.isLocked(x, y)) {
                        back = SColorFactory.blend(SColorFactory.lighter(SColor.BURNT_UMBER), SColor.BEIGE, 0.7);
                    }
                    display.placeImage(x, y, String.valueOf(c), back);
                } else {
                    display.placeCharacter(x, y, c, front, back);
                }
            }
        }
        display.refresh();
        repaint();
    }

    private void attachListeners() {
        display.addMouseListener(this);
        display.addMouseMotionListener(this);
    }
    //
    //variables related to listening to the mouse
    Point startMouse = new Point(0, 0);
    Point endMouse = new Point(0, 0);
    boolean wasDragged = false;

    @Override
    public void mouseClicked(MouseEvent me) {
        int x = me.getX() / display.getCellDimension().width;
        int y = me.getY() / display.getCellDimension().height;
        if (board.isNode(x, y)) {
            board.flipNodeLock(x, y);
            drawGuessedBoard();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON1) {
            startMouse = me.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (wasDragged) {
            wasDragged = false;
            endMouse = me.getPoint();

            Point nodePicked = new Point(startMouse.x / display.getCellDimension().width, startMouse.y / display.getCellDimension().height);
            Direction direction;
            if (board.isNode(nodePicked.x, nodePicked.y)) {
                if (Math.abs(startMouse.x - endMouse.x) > Math.abs(startMouse.y - endMouse.y)) {//mostly horizontal
                    if (startMouse.x < endMouse.x) {//went right
                        direction = Direction.RIGHT;
                    } else {//went left
                        direction = Direction.LEFT;
                    }
                } else {//mostly vertical
                    if (startMouse.y < endMouse.y) {//went down
                        direction = Direction.DOWN;
                    } else {//went up
                        direction = Direction.UP;
                    }
                }
                board.guessBridge(nodePicked, direction);
                drawGuessedBoard();
                repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //nothing special happens
    }

    @Override
    public void mouseExited(MouseEvent me) {
        wasDragged = false;//cancel drag event
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        wasDragged = true;
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        //nothing special happens
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));

        pack();
    }

    public static void main(String args[]) {
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SquidBridges().setVisible(true);
            }
        });
    }
}
