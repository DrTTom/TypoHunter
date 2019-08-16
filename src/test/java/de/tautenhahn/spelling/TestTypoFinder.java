package de.tautenhahn.spelling;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Unit test for typo finder.
 *
 * @author TT
 */
public class TestTypoFinder
{

    /**
     * Asserts that typos can be found in a line of text.
     */
    @Test
    public void handleOneLine()
    {
        TypoFinder systemUnderTest = new TypoFinder();
        systemUnderTest.addAllowedPhrases("tetex");
        // NO-SPELLCHECK
        String line = "This is a text with some StrangeFormattedStuff and a typo, namely: aaachen";
        systemUnderTest.check(null, 1, line);
        assertThat(systemUnderTest.getFindings()).extracting(Object::toString).
            contains("   aaachen      in line   1: " + line + " in file null");
    }

    /**
     * Simple smoke test just to keep coverage up.
     *
     * @throws IOException in case of problem reading files
     */
    @Test
    public void callMain() throws IOException
    {
        Main.main(".");
    }
}
