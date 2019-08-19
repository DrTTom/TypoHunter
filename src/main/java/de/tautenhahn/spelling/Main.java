package de.tautenhahn.spelling;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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
        writeOutput(finder, baseDir);
    }

    private static void writeOutput(TypoFinder finder, Path baseDir) throws IOException
    {
        Path currentFile = null;
        for (Finding f : finder.getFindings())
        {
            if (!Objects.equals(f.getSource(), currentFile))
            {
                currentFile = f.getSource();
                DEST.println(currentFile);
            }
            DEST.println(f.getFindingLine());
        }
        DEST.println("------------------------------------------------------------------------------------");
        DEST.println("Checked " + finder.getNumberCheckedFiles() + " files in " + baseDir.toRealPath());
        DEST.println("Found " + finder.getFindings().size() + " potential typos");
    }
}
