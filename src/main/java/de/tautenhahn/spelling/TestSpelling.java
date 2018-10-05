package de.tautenhahn.spelling;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Finds typos in different kind of text files, possibly of mixed language, using a black list. This will not
 * ensure correct spelling but may find some typos before others do. Contrary to spell checkers this test
 * explicitly targets expressions which are not meant to be a text in natural language.
 */
public class TestSpelling
{

  private static final Logger LOG = LoggerFactory.getLogger(TestSpelling.class);

  private static final List<String> EXTENSIONS = Arrays.asList(".java",
                                                               ".md",
                                                               ".txt",
                                                               ".properties",
                                                               ".jsf",
                                                               ".xml",
                                                               ".json",
                                                               ".html",
                                                               ".htm",
                                                               ".xhtml",
                                                               ".js");

  private static final List<String> IGNORED = Arrays.asList("/build/", "/node_modules/", "/help_de/");

  // NO-SPELLCHECK
  private static final String[] ALLOWED_PHRASES = {"Sass"};

  /**
   * Call the test for all suitable files
   *
   * @throws IOException
   */
  private List<String> checkAllFiles(Path baseDir) throws IOException
  {
    TypoFinder finder = new TypoFinder();
    finder.addAllowedPhrases(ALLOWED_PHRASES);
    Files.walk(baseDir)
         .filter(this::toBeChecked)
         .filter(p -> !p.toFile().isDirectory())
         .parallel()
         .forEach(finder::check);
    LOG.debug("Found misspelled words: {}", finder.getWords());
    return finder.getFindings();
  }

  /**
   * Returns true if file should be checked.
   *
   * @param p
   */
  protected boolean toBeChecked(Path p)
  {
    String fullName = p.toString();
    return EXTENSIONS.stream().anyMatch(s -> fullName.endsWith(s))
           && IGNORED.stream().noneMatch(n -> fullName.contains(n)) && Files.isRegularFile(p);
  }

  /**
   * Check for typos before they become embarrassing.
   *
   * @throws IOException
   */
  @Test
  public void findTypos() throws IOException
  {
    String dir = System.getProperty("basedir", ".");
    List<String> typoList = checkAllFiles(Paths.get(dir));
    typoList.forEach(LOG::info);
    assertThat("Found problems", typoList, empty());
  }

}
