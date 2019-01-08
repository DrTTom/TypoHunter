package de.tautenhahn.spelling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


/**
 * Applies a test for all appropriate files within a directory tree.
 *
 * @author TT
 */
public class HandleDirTree
{

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

  private final Consumer<Path> consumer;

  /**
   * Creates instance.
   *
   * @param consumer will be calles with each method
   */
  public HandleDirTree(Consumer<Path> consumer)
  {
    this.consumer = consumer;
  }

  /**
   * Call the test for all suitable files
   *
   * @throws IOException guess why
   */
  public void checkAllFiles(Path baseDir) throws IOException
  {
    Files.walk(baseDir)
         .filter(this::toBeChecked)
         .filter(p -> p.toFile().isFile())
         .parallel()
         .forEach(consumer);
  }

  /**
   * Returns true if file should be checked.
   *
   * @param p path to check or not
   */
  protected boolean toBeChecked(Path p)
  {
    String fullName = p.toString();
    return EXTENSIONS.stream().anyMatch(fullName::endsWith)
           && IGNORED.stream().noneMatch(fullName::contains) && Files.isRegularFile(p);
  }


}
