package squidbridge;

/**
 * Enum that represents the possible types of tiles in the standard grid of a
 * Hashiwokakero game.
 *
 * @author Eben Howard - http://squidpony.com
 */
public enum HashiTile {

    EMPTY, NODE, VERTICAL_LINE, HORIZONTAL_LINE;

    @Override
    public String toString() {
        return this.name().toLowerCase().substring(0, 1);
    }
}
