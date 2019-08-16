package de.tautenhahn.spelling;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Finds typos in different kind of text files, possibly of mixed language, using a black list. This will not ensure
 * correct spelling but may find some typos before others do. Contrary to spell checkers this test explicitly targets
 * expressions which are not meant to be a text in natural language.
 */
public class TestSpelling
{

    private static final Logger LOG = LoggerFactory.getLogger(TestSpelling.class);

    // NO-SPELLCHECK
    private static final String[] ALLOWED_PHRASES = {"Sass"};

    /**
     * Check for typos before they become embarrassing.
     *
     * @throws IOException in case of problem reading files
     */
    @Test
    public void findTypos() throws IOException
    {
        String dir = System.getProperty("basedir", ".");
        TypoFinder finder = new TypoFinder();
        finder.addAllowedPhrases(ALLOWED_PHRASES);

        new HandleDirTree(finder).checkAllFiles(Paths.get(dir));

        LOG.debug("Found misspelled words: {}", finder.getWords());
        Collection<Finding> typoList = finder.getFindings();
        typoList.forEach(f -> LOG.warn(f.toString()));
        assertThat(typoList).as("found typos").isEmpty();
    }
}
