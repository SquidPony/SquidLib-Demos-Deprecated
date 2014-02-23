package squidpony.squidtext;

import squidpony.squidtext.nolithiusgen.WeightedLetterNamegen;

/**
 * Based on work by Nolithius available at the following two sites
 * https://github.com/Nolithius/weighted-letter-namegen
 * http://code.google.com/p/weighted-letter-namegen/
 *
 * @author Eben Howard - http://squidpony.com - howard@squidpony.com
 */
public class NamegenDemo {

    private WeightedLetterNamegen namegen;

    public static void main(String... args) {
        new NamegenDemo().go();
    }

    public void go() {
        String[] text = new String[]{ "Andor", "Baatar", "Drogo", "Grog", "Gruumsh", "Grunt", "Hodor", "Hrothgar", "Hrun", "Korg", "Lothar", "Odin", "Thor", "Yngvar", "Xandor"};
        namegen = new WeightedLetterNamegen(text);
        generate();
    }

    public void generate() {
        String[] names = namegen.generate(10);
        for (String name : names) {
            System.out.println(name);
        }
    }
}
