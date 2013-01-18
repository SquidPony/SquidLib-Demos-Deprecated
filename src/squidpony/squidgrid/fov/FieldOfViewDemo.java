package squidpony.squidgrid.fov;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;
import squidpony.squidcolor.SColor;
import squidpony.squidcolor.SColorFactory;
import squidpony.squidgrid.gui.awt.event.SGMouseListener;
import squidpony.squidgrid.gui.swing.SwingPane;
import squidpony.squidgrid.util.Direction;
import static squidpony.squidgrid.util.Direction.*;

/**
 * Demonstrates the use of the Field of View and Line of Sight algorithms.
 *
 * @author Eben Howard - http://squidpony.com - howard@squidpony.com
 */
public class FieldOfViewDemo {

    private SwingPane display;//uses SGTextAndImagePanel instead of SGTextPanel in order to show it's usable as an in-place replacement/extension
    private JFrame frame;
    private static final String[] DEFAULT_MAP = new String[]{//in order to be in line with GUI coordinate pairs, this appears to be sideways in this style constructor.
        "øøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøø########################################øøøøøøøøøøøøøøøøøøøøøøøøø",
        "øøøøøøøøøøøøøøøøøø#########øøøøøøøø#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#..m.mmmmmmmmmmmmmm..m...ø",
        "øøøøøøøøøøø########.......##øøøøøøø#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#.mmTmmmmmmmmmmmmmm......ø",
        "øøøøø#######.......₤.......###øøøøø#¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸,TTT¸,,¸¸¸¸¸¸m.TmmmmmmmmmTmmmmmmm..m..ø",
        "øøø###₤₤₤₤₤..................#øøøøø#¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸,TTT,,¸¸¸¸¸¸mmmmm≈≈≈mm..mmmmmmmmm....ø",
        "øøø#₤₤₤₤₤.₤₤....₤............##øøøø#¸¸¸¸¸¸¸¸¸¸¸TTT¸¸¸¸¸¸¸¸¸¸¸¸¸,¸¸,,¸¸¸¸¸¸mmm≈≈≈≈≈mm.m.mmmmmmm.....ø",
        "øø##.₤₤₤₤₤₤₤₤.................####ø#¸¸¸¸¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸¸¸c,¸,,¸¸¸¸¸¸mm≈≈m≈mmmmmmmmm≈≈≈m..m...ø",
        "øø#..₤₤₤₤₤₤₤.....................###¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸¸¸ct¸ctc,,,¸¸¸¸¸¸m≈≈mmmmmmmTmmm≈≈≈≈≈≈≈≈...ø",
        "øø#...₤₤₤₤₤............₤............¸¸¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸ctt¸c¸¸,,¸¸¸¸¸¸mm≈≈mmmmmTmmm≈≈≈m≈≈mmmm..ø",
        "øø#.₤₤₤₤₤₤₤...........₤₤............¸¸TTTTT¸¸TTT¸¸¸¸¸¸¸¸¸¸¸¸cc¸¸¸¸,,¸¸¸¸¸¸mmm≈mmmmmmmmm≈≈mTm≈≈mmm..ø",
        "ø##₤₤₤₤₤₤₤₤₤.......................###############################,,¸¸¸¸S¸#mmmm≈m≈≈≈mm≈≈≈≈mmmmm≈mmmø",
        "ø#₤₤₤₤₤₤₤₤₤₤..₤....................#.....#.....#.....#.....#.....#+/#¸¸¸S¸#.T.mm≈≈≈≈≈≈≈≈≈mmmTmmmmm.ø",
        "ø#₤₤₤₤..₤₤₤₤₤......................#.....#.....#.....#.....#.....#..#¸¸¸¸¸#.TT.mmm≈≈≈≈≈≈≈≈mmm.mmT.mø",
        "ø#.₤₤₤.₤₤₤₤₤₤......₤...............#.....#.....#.....#.....#.....#..#######.TTTTmmm≈≈≈≈≈≈mmT..mmm.mø",
        "ø#₤₤₤₤₤₤₤₤₤........₤...............#.....#.....#.....#.....#.....#/+#.....#..TTmmmm≈≈≈≈≈≈TTmTmmTmmmø",
        "ø#₤₤₤₤₤₤₤₤₤₤.......................#.....#.....#.....#.....#.....#..#.....#..T.mmmm≈≈≈≈≈≈≈mmTmTm≈≈≈ø",
        "ø#₤₤₤₤₤₤₤₤₤₤.......................#.....###+#####+#####/#####+###..+.....#...Tmmmmm≈≈≈≈≈≈≈≈mmmmT≈≈ø",
        "##₤₤₤₤₤₤₤₤₤₤.......................#######..........................#.....#mT..mmmmmm≈≈≈≈≈≈≈mmm≈≈≈mø",
        "#₤₤..₤₤₤₤₤₤₤₤......................#.....#..........................#.....#m..mmmmmmm≈≈≈≈≈≈≈Tm≈≈≈mmø",
        "#₤..₤₤₤₤₤₤₤₤₤......................#.....#..........................#######..mmmmmmmmm≈≈≈≈≈≈m≈≈mmmmø",
        "#..................................#.....#...####################...#...#E#..mmm.mmmmm≈≈≈≈≈≈≈≈mmmmmø",
        "#..................................#.....#...+..E#..............#.../.../.#.......mmmm≈≈≈≈≈mmmmmmmmø",
        "#..................................#.....#...#####..............#...#...#E#........mm≈≈≈≈≈mmmmmmmmmø",
        "#..................................#.....#...#..................#...#######...m.....m≈≈≈≈mmmmmmmmmmø",
        "#..................................#.....#...#..................#.........+......mmmm≈≈mmm....mm≈≈mø",
        "#..................................#.....#...#..................#.........+...uu...um≈≈mu.....m≈≈≈mø",
        "#..................................#.....#...#..................#...#+###+#..uuuuuuuu≈≈uu.u.ummmmuuø",
        "#..................................#.....#...#.................##...#..#c.#uuuuuuuuuu≈≈uuuAuuuuuuuuø",
        "#..................................#.....#...#................#.#...#E.#t.#uuuuuAuuA≈≈≈≈≈uuuuuuuuuuø",
        "#..................................#.....#...#...............#..#...#E<#c.#uuAuAuuu≈≈≈≈≈≈≈AuAAuuAuuø",
        "#..................................#.....#...#.............##...#...#######uAuAAA≈≈≈≈≈≈≈≈≈≈AAAAAAAuø",
        "#..................................#.....#...#............#.....#...#.....#AAAuA≈≈≈≈≈≈≈≈≈≈≈AAAAAAAAø",
        "#..................................#.....#...#............#.....#...#.....#AAAA≈≈≈≈≈≈≈≈≈≈≈≈≈AAAAAAAø",
        "#..................................#.....#...####################...#.....#AAAAu≈≈≈≈≈≈≈≈≈≈≈≈≈≈AAAAAø",
        "#..................................#.....#.......EEEEEEEEEEE........#.....#AAAAuu.≈≈≈≈mmm≈≈≈≈≈AAuAAø",
        "#..................................#.....#..........................#.....#AAAuuuu≈≈≈≈≈mm≈≈≈≈AAuuAAø",
        "#..................................#.....#..........................#.....#AAAAuuuu≈≈≈≈≈≈≈≈≈AAuuuAAø",
        "#..................................#.....####+###+#####+#####+#####+#.....#AAAAAAAuu..≈≈≈≈≈AAAAuAAAø",
        "#..................................#.....#E.+.#.....#.....#.....#.........#AAAAAAAAA.AAA≈≈AAAAAAAAAø",
        "#..................................#.....####.#.....#.....#.....#tttt+#...#AAAAAAAA..AAAuuu.uAAAAAAø",
        "#..................................#.....#E.+.#.....#.....#.....#..c..#...#AAAAAAA....AAAAu..uAAAAAø",
        "#..................................#.....####.#.....#.....#.....###..E#...#AAAAAA...AAAAAuuu.uAAAAAø",
        "#..................................#.....#E.+.#.....#.....#.....#E+.EE#...#AAAAAAAAAAAAAAAAAuuAAAAAø",
        "###########################################################################AAAAAAAAAAAAAAAAAAAAAAAAø"
    };
    private DemoCell[][] map;
    private float[][] light;
    private float[][] resistances;
    private int width = DEFAULT_MAP[0].length(), height = DEFAULT_MAP.length;
    private int cellWidth, cellHeight, locx, locy;
    private LOSSolver los;
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

        //put start location at middle of map
        locx = width / 2;
        locy = height / 2;

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
//        SColorFactory.setRGBFloorValue(25);//test of the flooring function to reduce total number of colors made
        
        display.initialize(width, height, new Font("Ariel", Font.BOLD, 18));
        clear();
        frame.add(display, BorderLayout.SOUTH);
        frame.setVisible(true);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.repaint();

        cellWidth = display.getCellDimension().width;
        cellHeight = display.getCellDimension().height;

        DemoInputListener dil = new DemoInputListener();
        MouseInputListener mil = new SGMouseListener(cellWidth, cellHeight, dil);
        display.addMouseListener(mil);//listens for clicks and releases
        display.addMouseMotionListener(mil);//listens for movement based events
        frame.addKeyListener(dil);
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
            case '.'://stone ground
                color = SColor.SLATE_GRAY;
                break;
            case '¸'://grass
                color = SColor.GREEN;
                break;
            case ','://pathway
                color = SColor.STOREROOM_BROWN;
                c = '.';
                break;
            case 'c':
                color = SColor.SEPIA;
                break;
            case '/':
                color = SColor.BROWNER;
                break;
            case '≈':
                color = SColor.AZUL;
                break;
            case '<':
            case '>':
                color = SColor.SLATE_GRAY;
                break;
            case 't':
                color = SColor.BROWNER;
                resistance = 0.3f;
                break;
            case 'm':
                color = SColor.BAIKO_BROWN;
                resistance = 0.1f;
                break;
            case 'u':
                color = SColor.TAN;
                resistance = 0.2f;
                break;
            case 'T':
            case '₤':
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
            case 'A':
                color = SColor.ALICE_BLUE;
                resistance = 1f;
                break;
            case 'ø':
                c = ' ';
                color = SColor.BLACK;
                resistance = 1f;
                break;
            default://opaque items
                resistance = 1f;//unknown is opaque
                color = SColor.DEEP_PINK;
        }
        return new DemoCell(resistance, c, color);
    }

    private void move(Direction dir) {
        int x = locx + dir.deltaX;
        int y = locy + dir.deltaY;

        //check for legality of move based solely on map boundary
        if (x >= 0 && x < width && y >= 0 && y < height) {
            locx = x;
            locy = y;
            doFOV(x, y);
        }
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
        light = panel.getFOVSolver().calculateFOV(resistances, startx, starty, 1f, 1 / lightForce, panel.getStrategy());
//        SColorFactory.emptyCache();//uncomment to check perfomance difference
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
                        display.placeCharacter(x, y, map[x][y].representation, objectLight);
                    }
                } else {
                    display.clearCell(x, y);
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
            display.placeCharacter(startx, starty, '@', objectLight);
        }
        display.refresh();
        System.out.println("Colors: " + SColorFactory.getQuantityCached());
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
        los = panel.getLOSSolver();

        //working variables
        char c;
        SColor fore = SColor.WHITE;

        //run the LOS calculation
        boolean visible = los.isReachable(resistances, startx, starty, endx, endy);
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
    private class DemoInputListener implements MouseInputListener, KeyListener {

        int startx, starty;
        boolean dragged = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            dragged = false;
            locx = e.getX();
            locy = e.getY();
            doFOV(e.getX(), e.getY());
            frame.requestFocusInWindow();
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

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getExtendedKeyCode();
            move(getDirection(code));
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        private Direction getDirection(int code) {
            switch (code) {
                case VK_LEFT:
                case VK_NUMPAD4:
                    return LEFT;
                case VK_RIGHT:
                case VK_NUMPAD6:
                    return RIGHT;
                case VK_UP:
                case VK_NUMPAD8:
                    return UP;
                case VK_DOWN:
                case VK_NUMPAD2:
                    return DOWN;
                case VK_NUMPAD1:
                    return DOWN_LEFT;
                case VK_NUMPAD3:
                    return DOWN_RIGHT;
                case VK_NUMPAD7:
                    return UP_LEFT;
                case VK_NUMPAD9:
                    return UP_RIGHT;
                default:
                    return NONE;
            }
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
