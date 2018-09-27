package de.tautenhahn.spelling;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.junit.Test;


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
    // NO-SPELLCHECK
    String line = "This is a text with some StrangeFormattedStuff and a typo, namely: aaachen";
    systemUnderTest.check(null, 1, line);
    assertThat("findings",
               systemUnderTest.getFindings(),
               contains("Typo 'aaachen' in null, line 1: " + line));
  }
}
