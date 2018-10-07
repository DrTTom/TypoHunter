package de.tautenhahn.spelling;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;


/**
 * Command line interface for spell checker.
 *
 * @author TT
 */
public class Main
{

  static final PrintStream DEST = System.out;

  /**
   * Call typo finder for specified directory tree. Write findings to standard out.
   *
   * @param args base directory, optional, defaults to current directory
   * @throws IOException
   */
  public static void main(String[] args) throws IOException
  {
    String base = ".";
    if (args.length > 0)
    {
      base = args[0];
    }
    TypoFinder finder = new TypoFinder();
    new HandleDirTree(finder).checkAllFiles(Paths.get(base));
    finder.getFindings().forEach(DEST::println);
    DEST.println("\n Found " + finder.getFindings().size() + " potential typos in " + base);
  }
}
