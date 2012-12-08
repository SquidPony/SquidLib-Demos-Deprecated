package squidbridge;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import squidpony.squidgrid.util.Direction;

/**
 * Generates and contains information for a single instance of a Hashiwokakero
 * (also known as Bridges) puzzle.
 *
 * @author SquidPony
 */
public class Hashiwokakero {
    HashiTile board[][];
    HashiTile guessedBoard[][];
    int boardQuantity[][];
    int guessedQuantity[][];
    private boolean[][] locked;
    int width, height, totalNodes;
    Random rng = new Random();
    ArrayList<Point> nodes = new ArrayList<Point>();//holds a quick reference to created nodes
    static final double stopChance = 0.20;
    static final int singleLineChance = 70;//out of 100

    public Hashiwokakero(int width, int height) {
        this.width = width;
        this.height = height;
        board = new HashiTile[width][height];
        boardQuantity = new int[width][height];
        guessedBoard = new HashiTile[width][height];
        guessedQuantity = new int[width][height];
        locked = new boolean[width][height];
        totalNodes = 0;
        buildBoard();
    }

    private void buildBoard() {
        //initialize board
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board[x][y] = HashiTile.EMPTY;
                boardQuantity[x][y] = 0;
                guessedBoard[x][y] = HashiTile.EMPTY;
                guessedQuantity[x][y] = 0;
            }
        }

        //place a random node
        int x = rng.nextInt(width);
        int y = rng.nextInt(height);
        board[x][y] = HashiTile.NODE;//mark it as a starting node
        nodes.add(new Point(x, y));
        totalNodes++;

        drawRandomLines();

        //copy nodes into guessed board

        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                if (board[x][y] == HashiTile.NODE) {
                    guessedBoard[x][y] = HashiTile.NODE;
                    guessedQuantity[x][y] = boardQuantity[x][y];
                }
            }
        }
    }

    private void drawRandomLines() {
        do {
            Point node = nodes.get(rng.nextInt(nodes.size()));

            //determine if node has any open edges (has to have two open spaces)
            int usedEdges = 0;
            if (node.x <= 1 || board[node.x - 1][node.y] != HashiTile.EMPTY || board[node.x - 2][node.y] != HashiTile.EMPTY) {
                usedEdges++;
            }
            if (node.y <= 1 || board[node.x][node.y - 1] != HashiTile.EMPTY || board[node.x][node.y - 2] != HashiTile.EMPTY) {
                usedEdges++;
            }
            if (node.x >= width - 2 || board[node.x + 1][node.y] != HashiTile.EMPTY || board[node.x + 2][node.y] != HashiTile.EMPTY) {
                usedEdges++;
            }
            if (node.y >= height - 2 || board[node.x][node.y + 1] != HashiTile.EMPTY || board[node.x][node.y + 2] != HashiTile.EMPTY) {
                usedEdges++;
            }

            if (usedEdges >= 4 || (nodes.size() > 15 && rng.nextDouble() < 0.5)) {//only attempt to add if there's possible room
                nodes.remove(node);
            } else {

                //determine thickness of bridge
                int thickness;
                if (rng.nextInt(100) < singleLineChance) {
                    thickness = 1;
                } else {
                    thickness = 2;
                }

                //determine random direction
                switch (rng.nextInt(4)) {
                    case 0:
                        if (node.y <= 1 || board[node.x][node.y - 1] != HashiTile.EMPTY
                                || board[node.x][node.y - 2] != HashiTile.EMPTY) {//ensure validity
                            break;
                        } else {
                            boardQuantity[node.x][node.y] += thickness;
                            placeLine(node.x, node.y - 1, Direction.UP, thickness, true);
                        }
                        break;
                    case 1:
                        if (node.y >= height - 2 || board[node.x][node.y + 1] != HashiTile.EMPTY
                                || board[node.x][node.y + 2] != HashiTile.EMPTY) {//ensure validity
                            break;
                        } else {
                            boardQuantity[node.x][node.y] += thickness;
                            placeLine(node.x, node.y + 1, Direction.DOWN, thickness, true);
                        }
                        break;
                    case 2:
                        if (node.x <= 1 || board[node.x - 1][node.y] != HashiTile.EMPTY
                                || board[node.x - 2][node.y] != HashiTile.EMPTY) {//ensure validity
                            break;
                        } else {
                            boardQuantity[node.x][node.y] += thickness;
                            placeLine(node.x - 1, node.y, Direction.LEFT, thickness, true);
                        }
                        break;
                    case 3:
                        if (node.x >= width - 2 || board[node.x + 1][node.y] != HashiTile.EMPTY
                                || board[node.x + 2][node.y] != HashiTile.EMPTY) {//ensure validity
                            break;
                        } else {
                            boardQuantity[node.x][node.y] += thickness;
                            placeLine(node.x + 1, node.y, Direction.RIGHT, thickness, true);
                        }
                        break;
                }
            }
        } while (!nodes.isEmpty());//keep adding lines as long as there are any possible nodes

    }

    private void placeLine(int x, int y, Direction direction, int thickness, boolean forced) {
        if (x + direction.deltaX >= 0 && x + direction.deltaX < width
                && y + direction.deltaY >= 0 && y + direction.deltaY < height
                && board[x + direction.deltaX][y + direction.deltaY] == HashiTile.EMPTY
                && (forced || rng.nextDouble() > stopChance)) {//place a line
            boardQuantity[x][y] = thickness;

            if (direction == Direction.DOWN || direction == Direction.UP) {
                board[x][y] = HashiTile.VERTICAL_LINE;
            } else {
                board[x][y] = HashiTile.HORIZONTAL_LINE;
            }

            placeLine(x + direction.deltaX, y + direction.deltaY, direction, thickness, false);
        } else {//place a node
            board[x][y] = HashiTile.NODE;
            boardQuantity[x][y] = thickness;
            nodes.add(new Point(x, y));
            totalNodes++;
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        char c;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (board[x][y]) {
                    case EMPTY:
                        c = ' ';
                        break;
                    case NODE:
                        c = String.valueOf(boardQuantity[x][y]).charAt(0);
                        break;
                    case VERTICAL_LINE:
                        switch (boardQuantity[x][y]) {
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
                        switch (boardQuantity[x][y]) {
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
                out.append(c);
            }
            out.append("\n");
        }
        return out.toString();
    }

    /**
     * Returns the type of tile that is guessed to be at the given location.
     *
     * @param x
     * @param y
     * @return
     */
    public HashiTile getGuessedTile(int x, int y) {
        return guessedBoard[x][y];
    }

    public HashiTile getRealTile(int x, int y) {
        return board[x][y];
    }

    /**
     * Returns the type of tile that is part of the solution at the given
     * location.
     *
     * @param x
     * @param y
     * @return
     */
    public HashiTile getTile(int x, int y) {
        return board[x][y];
    }

    public boolean isNode(int x, int y) {
        return board[x][y] == HashiTile.NODE;
    }

    /**
     * Returns true if the given location is currently locked.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isLocked(int x, int y) {
        return locked[x][y];
    }

    /**
     * Sets the node and all bridges attached to it to the passed in boolean
     * value.
     *
     * @param startX The node's x coordinate
     * @param startY The node's y coordinate
     * @param lock The target locked state
     */
    private void setNodeLock(int startX, int startY, boolean lock) {
        if (guessedBoard[startX][startY] == HashiTile.NODE) {//only do this lock if start is a node
            locked[startX][startY] = !locked[startX][startY];
            for (Direction dir : Direction.values()) {//check all directions
                int x = startX;
                int y = startY;
                while (x + dir.deltaX >= 0 && x + dir.deltaX < width && y + dir.deltaY >= 0 && y + dir.deltaY < height
                        && (((dir == Direction.UP || dir == Direction.DOWN) && guessedBoard[x + dir.deltaX][y + dir.deltaY] == HashiTile.VERTICAL_LINE)
                        || ((dir == Direction.LEFT || dir == Direction.RIGHT) && guessedBoard[x + dir.deltaX][y + dir.deltaY] == HashiTile.HORIZONTAL_LINE))) {
                    x += dir.deltaX;
                    y += dir.deltaY;
                    locked[x][y] = lock;
                }
            }
        }
    }

    /**
     * Switches the locked state of the node and all attached bridges. The
     * attached bridges will be placed into the same state as the provided node,
     * regardless of their state prior.
     *
     * If the passed in location is not a node, no changes are made.
     *
     * @param x
     * @param y
     */
    public void flipNodeLock(int x, int y) {
        if (guessedBoard[x][y] == HashiTile.NODE) {
            setNodeLock(x, y, !locked[x][y]);
        }
    }

    /**
     * Attempts to add a guessed bridge from the given node that travels in the
     * given direction. Adding a bridge is a mod 3 action, so the "third" add
     * will reset to no bridge.
     *
     * Will return true if the operation made a change, and false if the
     * operation was for some reason illegal.
     *
     * @param start
     * @param direction
     * @return
     */
    public boolean guessBridge(Point start, Direction direction) {
        if(start.x + direction.deltaX < 0 || start.x+direction.deltaX >= width || start.y + direction.deltaY <0 || start.y+direction.deltaY >= height){
            return false;//can't go off the edge of the map
        }
        if (isNode(start.x, start.y)) {
            //check for open path to next node in given direction
            if (guessedBoard[start.x + direction.deltaX][start.y + direction.deltaY] == HashiTile.NODE) {
                return false;//no space for a bridge between those two nodes
            }
            boolean foundNode = false;
            searchLoop:
            for (int i = 1; start.x + direction.deltaX * i >= 0 && start.x + direction.deltaX * i < width
                    && start.y + direction.deltaY * i >= 0 && start.y + direction.deltaY * i < height; i++) {

                //see if a node was hit
                if (guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY * i] == HashiTile.NODE) {
                    foundNode = true;
                    break searchLoop;
                }

                //check for easy case, empty
                if (guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY * i] != HashiTile.EMPTY) {
                    //not empty, check for conflicting line
                    if (direction == Direction.DOWN || direction == Direction.UP) {
                        if (guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY* i] != HashiTile.VERTICAL_LINE) {
                            return false;
                        }
                    } else {
                        if (guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY * i] != HashiTile.HORIZONTAL_LINE) {
                            return false;
                        }
                    }
                }
            }
            if (!foundNode) {
                return false;//hit the edge of the board with no node found
            }

            //add a bridge (mod 3)
            int newQuantity = (guessedQuantity[start.x + direction.deltaX][start.y + direction.deltaY] + 1) % 3;
            for (int i = 1; guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY * i] != HashiTile.NODE; i++) {
                guessedQuantity[start.x + direction.deltaX * i][start.y + direction.deltaY * i] = newQuantity;
                if (newQuantity > 0) {
                    if (direction == Direction.UP || direction == Direction.DOWN) {
                        guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY * i] = HashiTile.VERTICAL_LINE;
                    } else {
                        guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY * i] = HashiTile.HORIZONTAL_LINE;
                    }
                } else {
                    guessedBoard[start.x + direction.deltaX * i][start.y + direction.deltaY * i] = HashiTile.EMPTY;
                }
            }

            return true;//operation finished successfully
        } else {
            return false;//can't start from something that's not a node
        }
    }
}
