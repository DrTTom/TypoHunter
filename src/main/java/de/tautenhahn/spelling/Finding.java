package de.tautenhahn.spelling;

import java.nio.file.Path;

/**
 * Data object wrapping a found typo.
 * @author TT
 */
public class Finding
{
    private final String typo;
    private final int lineNumber;
    private final String line;
    private final Path source;

    Finding(String typo, String line, Path source, int lineNumber)
    {
        this.typo = typo;
        this.line = line;
        this.source = source;
        this.lineNumber=lineNumber;
    }

    Path getSource()
    {
        return source;
    }

    @Override
    public String toString()
    {
        return getFindingLine() +" in file "+source;
    }

    String getFindingLine()
    {
        return String.format("   %-12s in line %3d: %s", typo, lineNumber, line.trim());
    }
}
