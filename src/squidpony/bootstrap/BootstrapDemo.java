package squidpony.bootstrap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import squidpony.annotation.Beta;
import squidpony.snowman.Monster;
import squidpony.snowman.Tile;
import squidpony.snowman.Treasure;
import squidpony.squidgrid.fov.FOVTranslator;
import squidpony.squidgrid.fov.ShadowFOV;
import squidpony.squidgrid.util.Direction;
import squidpony.squidmath.RNG;

/**
 * This shows how to use the bootstrap package to quickly create a basic UI for a roguelike game.
 *
 * @author Eben Howard - http://squidpony.com - howard@squidpony.com
 */
@Beta
public class BootstrapDemo implements GameLogic {

    private BootStrapFrame frame;
    private static final int width = 80;
    private static final int height = 50;
    private static final int minimumRoomSize = 3;
    private static final char[] CHARS_USED = new char[]{'☃', '☺', '▒', '.', 'X', 'y'};
    private final FOVTranslator fov = new FOVTranslator(new ShadowFOV());
    private final Random rng = new RNG();
    private Monster player;
    private int playerStrength = 7;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<Treasure> treasuresFound = new ArrayList<>();
    private LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();
    private Tile[][] map;

    public static void main(String... args) {
        new BootstrapDemo().go();
    }

    private void go() {
        final BootstrapDemo demo = this;
        frame = new BootStrapFrame(width, height, demo);
    }

    @Override
    public void acceptKeyboardInput(char key) {
        frame.output("Pressed: " + key);
    }

    @Override
    public void acceptDirectionInput(Direction dir) {
        boolean success = tryToMove(Direction.RIGHT);

        //update all end of turn items
        if (success) {
            updateMap();
            moveAllMonsters();
            updateMap();
            player.causeDamage(1);//health drains each turn!
            updateStats();
        }
    }

    @Override
    public void acceptMoustInput(int x, int y) {
    }

    @Override
    public void beginGame() {
        player = new Monster(Monster.PLAYER);

        createMap();
        updateMap();
        updateStats();
        frame.output("Welcome to the Snowman Bootstrap Demo game!\nEnjoy and feel free to modify the source for your own game.");
    }

    /**
     * Attempts to move in the given direction.If a monster is in that direction then the player
     * attacks the monster.
     *
     * Returns false if there was a wall in the direction and so no action was taken.
     *
     * @param dir
     * @return
     */
    private boolean tryToMove(Direction dir) {
        Tile tile = map[player.x + dir.deltaX][player.y + dir.deltaY];
        if (tile.isWall()) {
            return false;
        }

        Monster monster = tile.getMonster();
        if (monster == null) {//move the player
            map[player.x][player.y].setMonster(null);
            frame.slide(new Point(player.x, player.y), dir);
            frame.waitForAnimations();
            player.x += dir.deltaX;
            player.y += dir.deltaY;
            map[player.x][player.y].setMonster(player);
            return true;
        } else {//attack!
            frame.bump(new Point(player.x, player.y), dir);
            frame.waitForAnimations();
            boolean dead = monster.causeDamage(playerStrength);
            if (dead) {
                monsters.remove(monster);
                map[player.x + dir.deltaX][player.y + dir.deltaY].setMonster(null);//no more monster
                frame.output("Killed the " + monster.getName());
            }
            return true;
        }

    }

    /**
     * Updates the map display to show the current view
     */
    private void updateMap() {
        doFOV();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
//                map[x][y].setSeen(true);//uncomment this to see the fully generated map rather than the player's view
                frame.placeCharacter(x, y, map[x][y].getSymbol(), map[x][y].getColor());
            }
        }

        frame.refresh();
    }

    /**
     * Updates the stats display to show current values
     */
    private void updateStats() {
        frame.updateStat("Health", player.getHealth());
    }

    /**
     * Calculates the Field of View and marks the maps spots seen appropriately.
     */
    private void doFOV() {
        boolean[][] walls = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                walls[x][y] = map[x][y].isWall();
            }
        }
        fov.calculateFOV(walls, player.x, player.y, width + height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y].setSeen(fov.isLit(x, y));
            }
        }
    }

    /**
     * Creates the map to contain random bits of wall
     */
    private void createMap() {
        map = new Tile[width][height];

        //make all the edges into walls
        for (int x = 0; x < width; x++) {
            map[x][0] = new Tile(true);
            map[x][height - 1] = new Tile(true);
        }
        for (int y = 0; y < height; y++) {
            map[0][y] = new Tile(true);
            map[width - 1][y] = new Tile(true);
        }

        //fill the rest in with floor
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                map[x][y] = new Tile();
            }
        }

        //randomly place some chunks of wall
        placeWallChunk();
        placeWallChunk();
        placeWallChunk();
        placeWallChunk();
        placeWallChunk();
        placeWallChunk();
        placeWallChunk();
        placeWallChunk();
        placeWallChunk();

        //randomly place the player
        placeMonster(player);

        //randomly place some monsters
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));
        placeMonster(new Monster(Monster.SNOWMAN));

        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Chocolate Coin", 1));
        placeTreasure(new Treasure("Coal", 0));
        placeTreasure(new Treasure("Coal", 0));
        placeTreasure(new Treasure("Coal", 0));
        placeTreasure(new Treasure("Coal", 0));

    }

    /**
     * Randomly places a group of walls in the map. This replaces whatever was in that location
     * previously.
     */
    private void placeWallChunk() {
        int spread = 5;
        int centerX = rng.nextInt(width);
        int centerY = rng.nextInt(height);

        for (int placeX = centerX - spread; placeX < centerX + spread; placeX++) {
            for (int placeY = centerY - spread; placeY < centerY + spread; placeY++) {
                if (rng.nextDouble() < 0.2 && placeX > 0 && placeX < width - 1 && placeY > 0 && placeY < height - 1) {
                    map[placeX][placeY] = new Tile(true);
                }
            }
        }
    }

    /**
     * Places the provided monster into an open tile space.
     *
     * @param monster
     */
    private void placeMonster(Monster monster) {
        int x = rng.nextInt(width - 2) + 1;
        int y = rng.nextInt(height - 2) + 1;
        if (map[x][y].isWall() || map[x][y].getMonster() != null) {
            placeMonster(monster);//try again recursively
        } else {
            map[x][y].setMonster(monster);
            monster.x = x;
            monster.y = y;

            if (!monster.equals(Monster.PLAYER)) {
                monsters.add(monster);
            }
        }
    }

    /**
     * Places the provided monster into an open tile space.
     *
     * @param treasure
     */
    private void placeTreasure(Treasure treasure) {
        int x = rng.nextInt(width - 2) + 1;
        int y = rng.nextInt(height - 2) + 1;
        if (map[x][y].isWall() || map[x][y].getTreasure() != null) {
            placeTreasure(treasure);//try again recursively
        } else {
            map[x][y].setTreasure(treasure);
        }
    }

    /**
     * Moves the monster given if possible. Monsters will not move into walls, other monsters, or
     * the player.
     *
     * @param monster
     */
    private void moveMonster(Monster monster) {
        Direction dir = Direction.CARDINALS[rng.nextInt(Direction.CARDINALS.length)];//get a random direction
        Tile tile = map[monster.x + dir.deltaX][monster.y + dir.deltaY];
        if (!tile.isWall() && tile.getMonster() == null) {
            map[monster.x][monster.y].setMonster(null);
            if (tile.isSeen()) {//only show animation if within sight
                frame.slide(new Point(monster.x, monster.y), dir);
                frame.waitForAnimations();
            }
            monster.x += dir.deltaX;
            monster.y += dir.deltaY;
            map[monster.x][monster.y].setMonster(monster);
        } else if (tile.isSeen()) {//only show animation if within sight
            frame.bump(new Point(monster.x, monster.y), dir);
            frame.waitForAnimations();
        }
    }

    /**
     * Moves all the monsters, one at a time.
     */
    private void moveAllMonsters() {
        for (int i = 0; i < monsters.size(); i++) {
            moveMonster(monsters.get(i));
        }
    }
}
