package squidpony.squidgrid.fov;

import squidpony.squidcolor.SColor;

/**
 * A simple demonstration implementation of FOVCell. Represents a unit of air
 * with full transparency and no resistance or an object with full resistance
 * and no transparency.
 *
 * The key is ignored in this implementation.
 *
 * @author Eben Howard - http://squidpony.com - eben@squidpony.com
 */
public class DemoCell implements FOVCell {

    float resistance, light;
    char representation;
    SColor color;

    /**
     * Creates a new cell which has minimal properties needed to represent it.
     *
     * @param resistance
     * @param light
     * @param representation
     */
    public DemoCell(float resistance, float light, char representation, SColor color) {
        this.resistance = resistance;
        this.light = light;
        this.representation = representation;
        this.color = color;
    }

    @Override
    public float getResistance(String key) {
        return resistance;
    }

    @Override
    public float getCurrentLight(String key) {
        return light;
    }
}
