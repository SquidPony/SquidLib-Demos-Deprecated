package squidpony.bootstrap;

import squidpony.squidgrid.util.Direction;

/**
 * This shows how to use the bootstrap package to quickly create a basic UI for a roguelike game.
 *
 * @author Eben Howard - http://squidpony.com - howard@squidpony.com
 */
public class BootstrapDemo implements GameLogic {

    private SFrame frame;
    private int width = 80;
    private int height = 50;

    public static void main(String... args) {
        new BootstrapDemo().go();
    }

    /**
     * Starts an instance of this class which demonstrates how to use the bootstrap package.
     */
    public void go() {
        frame = new SFrame(width, height, this);
    }

    @Override
    public void acceptKeyboardInput(char key) {
        frame.output("Pressed: " + key);
    }

    @Override
    public void acceptDirectionInput(Direction dir) {
        frame.output("Direction: " + dir);
    }

    @Override
    public void acceptMoustInput(int x, int y) {
    }
}
