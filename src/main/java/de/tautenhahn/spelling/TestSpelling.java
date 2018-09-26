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


/**
 * Finds typos in different kind of text files, possibly of mixed language, using a black list. This will not
 * ensure correct spelling but may find some typos before others do. Contrary to spell checkers this test
 * explicitly targets expressions which are not meant to be a text in natural language.
 */
public class TestSpelling
{

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
         .forEach(finder::check);
    return finder.getFindings();
  }


  private boolean toBeChecked(Path p)
  {
    String fullName = p.toString();
    List<String> extensions = Arrays.asList(".java",
                                            ".txt",
                                            ".properties",
                                            ".jsf",
                                            ".xml",
                                            ".json",
                                            ".html",
                                            ".htm",
                                            ".xhtml",
                                            ".js");
    List<String> ignored = Arrays.asList("/build/", "/node_modules/", "/help_de/");
    return extensions.stream().anyMatch(s -> fullName.endsWith(s))
           && ignored.stream().noneMatch(n -> fullName.contains(n));
  }

  /**
   * Check for typos before they become embarrassing.
   *
   * @throws IOException
   */
  @Test
  public void test() throws IOException
  {
    List<String> typoList = checkAllFiles(Paths.get("."));
    assertThat("Findings", typoList, empty());
  }

}
