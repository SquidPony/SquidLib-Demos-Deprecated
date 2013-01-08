package squidpony.squidgrid.fov;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;
import squidpony.squidcolor.SColor;
import squidpony.squidcolor.SColorFactory;
import squidpony.squidgrid.gui.swing.SwingPane;
import squidpony.squidgrid.gui.swing.listener.SGMouseListener;

/**
 * Demonstrates the use of the Field of View and Line of Sight algorithms.
 *
 * @author Eben Howard - http://squidpony.com - eben@squidpony.com
 */
public class FieldOfViewDemo {

    private SwingPane display;//uses SGTextAndImagePanel instead of SGTextPanel in order to show it's usable as an in-place replacement/extension
    private JFrame frame;
    private static final String[] DEFAULT_MAP = new String[]{//in order to be in line with GUI coordinate pairs, this appears to be sideways in this style constructor.
        "########################################",
        "#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#",
        "#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#",
        "#¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸,TTT¸,,¸¸¸¸¸¸#",
        "#¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸,TTT,,¸¸¸¸¸¸#",
        "#¸¸¸¸¸¸¸¸¸¸¸TTT¸¸¸¸¸¸¸¸¸¸¸¸¸,¸¸,,¸¸¸¸¸¸#",
        "#¸¸¸¸¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸¸¸c,¸,,¸¸¸¸¸¸#",
        "#¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸¸¸ct¸ctc,,,¸¸¸¸¸¸#",
        "#¸¸¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸ctt¸c¸¸,,¸¸¸¸¸¸#",
        "#¸¸TTTTT¸¸TTT¸¸¸¸¸¸¸¸¸¸¸¸cc¸¸¸¸,,¸¸¸¸¸¸#",
        "###############################..¸¸¸¸S¸#",
        "#.....#.....#.....#.....#.....#+/#¸¸¸S¸#",
        "#.....#.....#.....#.....#.....#..#¸¸¸¸¸#",
        "#.....#.....#.....#.....#.....#..#######",
        "#.....#.....#.....#.....#.....#/+#.....#",
        "#.....#.....#.....#.....#.....#..#.....#",
        "#.....###+#####+#####/#####+###..+.....#",
        "#######..........................#.....#",
        "#.....#..........................#.....#",
        "#.....#..........................#######",
        "#.....#...####################...#...#E#",
        "#.....#...+..E#..............#.../.../.#",
        "#.....#...#####..............#...#...#E#",
        "#.....#...#..................#...#######",
        "#.....#...#..................#.........#",
        "#.....#...#..................#.........#",
        "#.....#...#..................#...#+###+#",
        "#.....#...#.................##...#..#c.#",
        "#.....#...#................#.#...#E.#t.#",
        "#.....#...#...............#..#...#E<#c.#",
        "#.....#...#.............##...#...#######",
        "#.....#...#............#.....#...#.....#",
        "#.....#...#............#.....#...#.....#",
        "#.....#...####################...#.....#",
        "#.....#.......EEEEEEEEEEE........#.....#",
        "#.....#..........................#.....#",
        "#.....#..........................#.....#",
        "#.....####+###+#####+#####+#####+#.....#",
        "#.....#E.+.#.....#.....#.....#.........#",
        "#.....####.#.....#.....#.....#tttt+#...#",
        "#.....#E.+.#.....#.....#.....#..c..#...#",
        "#.....####.#.....#.....#.....###..E#...#",
        "#.....#E.+.#.....#.....#.....#E+.EE#...#",
        "########################################"
    };
    private DemoCell[][] map;
    private float[][] light;
    private float[][] resistances;
    private int width = DEFAULT_MAP[0].length(), height = DEFAULT_MAP.length;
    private int cellWidth, cellHeight;
    private LOSSolver los = new BresenhamLOS();
    private SColor litNear, litFar, replaceForeground = SColor.COBALT;
    private boolean lightBackground = false;//if true then the defaultForeground will be used for objects and the background will be lit
    private float lightForce; //controls how far the light will spread
    private FOVDemoPanel panel;

    public static void main(String... args) {
        new FieldOfViewDemo();
    }

    private FieldOfViewDemo() {
        map = new DemoCell[width][height];
        resistances = new float[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                char c = DEFAULT_MAP[y].charAt(x);
                map[x][y] = buildCell(c);
                resistances[x][y] = map[x][y].resistance;
            }
        }

        frame = new JFrame("SquidGrid Field of View Demonstration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        panel = new FOVDemoPanel();
        frame.add(panel, BorderLayout.NORTH);

        panel.clearBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        display = new SwingPane();
        display.initialize(width, height, new Font("Ariel", Font.BOLD, 20));
        clear();
        frame.add(display, BorderLayout.SOUTH);
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

    private void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                display.placeCharacter(x, y, map[x][y].representation, map[x][y].color, SColor.BLACK);
            }
        }
        display.refresh();
    }

    /**
     * Builds a cell based on the character in the map.
     *
     * @param c
     * @return
     */
    private DemoCell buildCell(char c) {
        float resistance = 0f;//default is transparent
        SColor color;
        switch (c) {
            case '.':
                color = SColor.SLATE_GRAY;
                break;
            case '¸':
                color = SColor.GREEN;
                break;
            case ',':
                color = SColor.STOREROOM_BROWN;
                break;
            case 'c':
                color = SColor.SEPIA;
                break;
            case '/':
                color = SColor.BROWNER;
                break;
            case '<':
            case '>':
                color = SColor.SLATE_GRAY;
                break;
            case 't':
                color = SColor.BROWNER;
                resistance = 0.3f;
                break;
            case 'T':
                color = SColor.FOREST_GREEN;
                resistance = 0.7f;
                break;
            case 'E':
                color = SColor.SILVER;
                resistance = 0.8f;
                break;
            case 'S':
                color = SColor.BREWED_MUSTARD_BROWN;
                resistance = 0.9f;
                break;
            case '#':
                color = SColor.SLATE_GRAY;
                resistance = 1f;
                break;
            case '+':
                color = SColor.BROWNER;
                resistance = 1f;
                break;
            default://opaque items
                resistance = 1f;//unknown is opaque
                color = SColor.DEEP_PINK;
        }
        return new DemoCell(resistance, c, color);
    }

    /**
     * Performs the Field of View process
     *
     * @param startx
     * @param starty
     */
    private void doFOV(int startx, int starty) {
        //manually set the radius to equal the force
        lightForce = panel.radiusSlider.getValue();
        litNear = SColorFactory.getSColor(panel.castColorPanel.getBackground().getRGB());
        litFar = SColorFactory.getSColor(panel.fadeColorPanel.getBackground().getRGB());
        light = panel.getFOVSolver().calculateFOV(resistances, startx, starty, 1f, 1 / lightForce, panel.simplifyButton.isSelected());
        SColorFactory.emptyCache();
        SColorFactory.addPallet("light", SColorFactory.getGradient(litNear, litFar));

        //repaint the level with new light map -- Note that in normal use you'd limit this to just elements that changed
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (light[x][y] > 0f) {
                    if (lightBackground) {
                        display.placeCharacter(x, y, map[x][y].representation, replaceForeground, SColorFactory.getFromPallet("light", 1 - light[x][y]));
                    } else {
                        double radius = Math.sqrt((x - startx) * (x - startx) + (y - starty) * (y - starty));
                        float bright = 1 - light[x][y];
                        SColor cellLight = SColorFactory.getFromPallet("light", bright);
                        SColor objectLight = SColorFactory.blend(map[x][y].color, cellLight, getTint(radius));
                        display.placeCharacter(x, y, map[x][y].representation, objectLight, SColor.BLACK);
                    }
                } else {
                    display.placeCharacter(x, y, ' ', display.getForeground(), display.getBackground());//clear unlit cells
                }
            }
        }

        //put the player at the origin of the FOV
        float bright = 1 - light[startx][starty];
        SColor cellLight = SColorFactory.getFromPallet("light", bright);
        SColor objectLight = SColorFactory.blend(SColor.ALICE_BLUE, cellLight, getTint(0f));
        if (lightBackground) {
            display.placeCharacter(startx, starty, '@', replaceForeground, objectLight);
        } else {
            display.placeCharacter(startx, starty, '@', objectLight, SColor.BLACK);
        }
        display.refresh();
    }

    /**
     * Custom method to determine tint based on radius as well as general tint
     * factor.
     *
     * @param radius
     * @return
     */
    public float getTint(double radius) {
        float tint = panel.tintSlider.getValue() / 100f;
        tint = (float) (0f + tint * radius);//adjust tint based on distance
        return tint;
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
        SColor fore = SColor.WHITE;

        //run the LOS calculation
        boolean visible = los.isReachable(resistances, startx, starty, endx, endy, lightForce);
        Queue<Point> path = los.getLastPath();

        //draw out background for path followed
        for (Point p : path) {
            c = map[p.x][p.y].representation;
            display.placeCharacter(p.x, p.y, c, fore, SColor.BLUE_GREEN_DYE);
        }

        //mark the start location
        c = map[startx][starty].representation;
        display.placeCharacter(startx, starty, c, fore, SColor.AMBER_DYE);

        //mark end point
        if (visible) {
            c = map[endx][endy].representation;
            display.placeCharacter(endx, endy, c, fore, SColor.GREEN);
        } else {
            display.placeCharacter(endx, endy, ' ', fore, SColor.RED_PIGMENT);
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

    private class DemoCell {

        float resistance;
        char representation;
        SColor color;

        /**
         * Creates a new cell which has minimal properties needed to represent
         * it.
         *
         * @param resistance
         * @param light
         * @param representation
         */
        public DemoCell(float resistance, char representation, SColor color) {
            this.resistance = resistance;
            this.representation = representation;
            this.color = color;
        }
    }
}
