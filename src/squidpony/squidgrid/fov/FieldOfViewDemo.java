package squidpony.squidgrid.fov;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;
import squidpony.squidcolor.SColor;
import squidpony.squidcolor.SColorFactory;
import squidpony.squidgrid.gui.SGTextAndImagePanel;
import squidpony.squidgrid.gui.listener.SGMouseListener;

/**
 * Demonstrates the use of the Field of View and Line of Sight algorithms.
 *
 * @author Eben Howard - http://squidpony.com - eben@squidpony.com
 */
public class FieldOfViewDemo {

    private SGTextAndImagePanel display;//uses SGTextAndImagePanel instead of SGTextPanel in order to show it's usable as an in-place replacement/extension
    private JFrame frame;
    private static final char[][] DEFAULT_MAP = new char[][]{//in order to be in line with GUI coordinate pairs, this appears to be sideways in this style constructor.
        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
        {'#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '#', 's', '#', '.', '.', 'S', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'q', '.', '.', '.', 'q', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'u', '.', '.', '.', 'u', '.', '.', '.', '#', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'i', '.', '.', '.', 'i', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'd', '.', '.', '.', 'd', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'p', '.', '.', '.', 'L', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '#', '.', '.', 'o', '.', '.', '.', 'i', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'n', '.', '.', '.', 'b', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'y', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#', '#'},
        {'#', '.', '.', '.', '.', '.', '.', '.', 'F', '.', '.', '.', '.', '#', '.', '.', '.', '#'},
        {'#', '.', '#', '.', 'c', '.', '.', '.', 'O', '.', '.', '.', '.', '.', '#', '#', '.', '#'},
        {'#', '.', '.', '.', 'o', '.', '#', '.', 'V', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', 'm', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
    };
    private DemoCell[][] map;
    private int width = DEFAULT_MAP.length, height = DEFAULT_MAP[0].length;
    private int cellWidth, cellHeight;
    private FOVSolver fov = new ShadowFOV();
    private LOSSolver los = new BresenhamLOS();
    private SColor litNear = SColorFactory.lightest(SColor.LIGHT_YELLOW_DYE),
            litFar = SColor.ORANGUTAN,
            replaceForeground = SColor.COBALT;
    private boolean lightBackground = false;//if true then the defaultForeground will be used for objects and the background will be lit
    private float lightForce = 1.1f, //controls how far the light will spread
            lightAdjustment = 0.4f; //controls how much the light will change the appearance of objects

    public static void main(String... args) {
        new FieldOfViewDemo();
    }

    private FieldOfViewDemo() {
        map = new DemoCell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float resistance = 1.0f;//default is opaque
                char c = DEFAULT_MAP[x][y];
                SColor color = SColor.WHITE;
                if (c == '.') {//if a floor tile set to fully transparent
                    resistance = 0f;
                    color = SColor.SLATE_GRAY;
                } else if (c == '#') {
                    resistance = 1f;//if a wall set to fully opaque
                    color = SColor.SLATE_GRAY;
                }
                map[x][y] = new DemoCell(resistance, 0f, c, color);
            }
        }

        frame = new JFrame("SquidGrid Field of View Demonstration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        display = new SGTextAndImagePanel();
        display.initialize(width, height, new Font("Ariel", Font.BOLD, 20));
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                display.placeCharacter(x, y, map[x][y].representation, map[x][y].color);
            }
        }
        display.refresh();
        frame.getContentPane().add(display, BorderLayout.SOUTH);
        frame.setVisible(true);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.repaint();

        cellWidth = display.getCellDimension().width;
        cellHeight = display.getCellDimension().height;

        JOptionPane.showMessageDialog(frame, "Click inside the window to calculate Field of View from the point clicked.\n"
                + "Hold and drag between two points to draw a Line of Sight line.");


        MouseInputListener mil = new SGMouseListener(cellWidth, cellHeight, new DemoInputListener());
        display.addMouseListener(mil);//listens for clicks and releases
        display.addMouseMotionListener(mil);//listens for movement based events
    }

    /**
     * Performs the Field of View process
     *
     * @param startx
     * @param starty
     */
    private void doFOV(int startx, int starty) {
        //manually set the radius to 10
        float[][] light = fov.calculateFOV(map, startx, starty, lightForce, (1.0f / 6.0f), true, "");

        //copy returned light map into the cells
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y].light = light[x][y];
            }
        }

        //repaint the level with new light map -- Note that in normal use you'd limit this to just elements that changed
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y].light > 0f) {
                    if (lightBackground) {
                        display.placeCharacter(x, y, map[x][y].representation, replaceForeground, SColorFactory.blend(litNear, litFar, 1 - map[x][y].getCurrentLight("")));
                    } else {
                        SColor cellLight = SColorFactory.blend(litNear, litFar, 1 - map[x][y].getCurrentLight(""));
                        SColor objectLight = SColorFactory.blend(map[x][y].color, cellLight, lightAdjustment);
                        display.placeCharacter(x, y, map[x][y].representation, objectLight , SColor.BLACK);
                    }
                } else {
                    display.placeCharacter(x, y, ' ', display.getForeground(), display.getBackground());//clear unlit cells
                }
            }
        }

        //put the player at the origin of the FOV
        if (lightBackground) {
            display.placeCharacter(startx, starty, '@', replaceForeground, SColorFactory.blend(litNear, litFar, 1 - map[startx][starty].getCurrentLight("")));
        } else {
            display.placeCharacter(startx, starty, '@', SColorFactory.blend(litNear, litFar, 1 - map[startx][starty].getCurrentLight("")), SColor.BLACK);
        }
        display.refresh();
    }

    /**
     * Performs the Line of Sight calculation and paints target square with a
     * green background if it can be reached or a red if not.
     *
     * @param startx
     * @param starty
     * @param endx
     * @param endy
     */
    private void doLOS(int startx, int starty, int endx, int endy) {
        //working variables
        char c;
        SColor fore;

        //run the LOS calculation
        boolean visible = los.isReachable(map, startx, starty, endx, endy, lightForce, "");
        Queue<Point> path = los.getLastPath();

        //draw out background for path followed
        for (Point p : path) {
            fore = SColor.BLACK;
            c = ' ';
            if (map[p.x][p.y].getCurrentLight("") > 0f) {
                fore = SColorFactory.blend(litNear, litFar, 1 - map[p.x][p.y].getCurrentLight(""));
                c = map[p.x][p.y].representation;
            }
            display.placeCharacter(p.x, p.y, c, fore, SColor.BLUE_GREEN_DYE);
        }

        //mark the start location
        c = ' ';
        if (map[startx][starty].getCurrentLight("") > 0f) {
            c = map[startx][starty].representation;
        }
        display.placeCharacter(startx, starty, c, litNear, SColor.AMBER_DYE);

        //mark end point
        if (visible) {
            c = ' ';
            if (map[endx][endy].getCurrentLight("") > 0f) {
                c = map[endx][endy].representation;
            }
            display.placeCharacter(endx, endy, c, litNear, SColor.GREEN);
        } else {
            display.placeCharacter(endx, endy, ' ', litNear, SColor.RED_PIGMENT);
        }
        display.refresh();
    }

    /**
     * A simple input listener.
     */
    private class DemoInputListener implements MouseInputListener {

        int startx, starty;
        boolean dragged = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            dragged = false;
            doFOV(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            dragged = false;
            startx = e.getX();
            starty = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragged) {//only do if a drag even preceeded letting go of the button
                doLOS(startx, starty, e.getX(), e.getY());
            }
            dragged = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //nothing special happens
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //nothing special happens
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            dragged = true;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //nothing special happens
        }
    }
}
