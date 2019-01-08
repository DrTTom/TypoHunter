package de.tautenhahn.spelling;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Command line interface for spell checker.
 *
 * @author TT
 */
public final class Main
{

  static final PrintStream DEST = System.out;

  private Main()
  {
    // no instance needed
  }

  /**
   * Call typo finder for specified directory tree. Write findings to standard out.
   *
   * @param args base directory, optional, defaults to current directory
   * @throws IOException guess why
   */
  public static void main(String... args) throws IOException
  {
    String base = ".";
    if (args.length > 0)
    {
      base = args[0];
    }
    TypoFinder finder = new TypoFinder();
    Path baseDir = Paths.get(base);
    new HandleDirTree(finder).checkAllFiles(baseDir);
    finder.getFindings().forEach(DEST::println);
    DEST.println("------------------------------------------------------------------------------------");
    DEST.println("Checked " + finder.getNumberCheckedFiles() + " files in " + baseDir.toRealPath());
    DEST.println("Found " + finder.getFindings().size() + " potential typos");
  }
}
