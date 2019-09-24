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
     * Asserts that a typo in some obviously hex or base64 expression is ignored.
     */
    @Test
    public void ignoreNumeric()
    {
        TypoFinder systemUnderTest = new TypoFinder();
        String hex = "463724562fabe23ffabced";
        systemUnderTest.check(null, 1, hex);
        assertThat(systemUnderTest.getFindings()).isEmpty();

        String base64 = "0Mxcafabe===";
        systemUnderTest.check(null, 2, base64);
        assertThat(systemUnderTest.getFindings()).isEmpty();

        systemUnderTest.check(null, 3, "463724562fabe23ffabced fabe");
        assertThat(systemUnderTest.getFindings()).hasSize(1);
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
