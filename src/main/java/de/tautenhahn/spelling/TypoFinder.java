package de.tautenhahn.spelling;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


/**
 * Finds typos in different kind of text files, possibly of mixed language, using a black list. This will not
 * ensure correct spelling but may find some typos before others do. Contrary to spell checkers, this class
 * explicitly targets expressions which are not meant to be a text in natural language.
 */
public class TypoFinder
{

  private static final String STOP_WORD = "NO-SPELLCHECK";

  private static final List<String> BLACKLIST;

  private final List<String> allowedPhrases = new ArrayList<>();

  private final List<String> findings = new ArrayList<>();

  private static int LINE_LIMIT = 300;

  static
  {
    List<String> typos = new ArrayList<>();
    readTypoList("/typos_de.list", typos);
    readTypoList("/typos.list", typos);
    BLACKLIST = Collections.unmodifiableList(typos);
  }


  private static void readTypoList(String name, List<String> typos)
  {
    try (InputStream ins = TypoFinder.class.getResourceAsStream(name); Scanner s = new Scanner(ins, "UTF-8"))
    {
      while (s.hasNext())
      {
        typos.add(s.next());
      }
    }
    catch (IOException e)
    {
      throw new IllegalStateException("cannot find internal typo list", e);
    }
  }

  /**
   * Specifies phrases which are treated as correct even if containing forbidden words.
   *
   * @param allowed must match exactly
   */
  public void addAllowedPhrases(String... allowed)
  {
    allowedPhrases.addAll(Arrays.asList(allowed));
  }

  /**
   * Checks a given file for known typos.
   *
   * @param path must represent a text file
   */
  public void check(Path path)
  {
    try (Scanner s = new Scanner(path, "UTF-8"))
    {

      for ( int lineNumber = 1 ; s.hasNext() ; lineNumber++ )
      {
        String line = s.nextLine();
        if (line.length() > LINE_LIMIT)
        {
          continue;
        }
        if (line.contains(STOP_WORD))
        {
          break;
        }
        String lower = line.toLowerCase(Locale.GERMAN); // for English texts OK as well.
        for ( String typo : BLACKLIST )
        {
          assertNotPresent(typo, line, lower, path, lineNumber);
        }
      }
    }
    catch (IOException e)
    {
      throw new IllegalArgumentException("problem reading file", e);
    }
  }

  private void assertNotPresent(String typo, String line, String lowerLine, Path p, int lineNumber)
  {
    int pos = lowerLine.indexOf(typo);
    if (pos != -1)
    {
      if (isWord(pos, typo.length(), line))
      {
        for ( String allowed : allowedPhrases )
        {
          int apos = line.indexOf(allowed);
          if (apos != -1 && apos <= pos && apos + allowed.length() >= pos + typo.length())
          {
            return;
          }
        }
        findings.add("Typo '" + typo + "' in " + p + ", line " + lineNumber + ": " + line);
      }
    }
  }

  private boolean isWord(int pos, int length, String line)
  {
    char prev = pos > 0 ? line.charAt(pos - 1) : ' ';
    boolean startsWithBoundary = !Character.isLetter(prev)
                                 || Character.isLowerCase(prev) && Character.isUpperCase(line.charAt(pos));
    if (!startsWithBoundary)
    {
      return false;
    }
    int next = pos + length;
    char following = next < line.length() ? line.charAt(next) : ' ';
    boolean endsWithBoundary = !Character.isLetter(following) || Character.isLowerCase(line.charAt(next - 1))
                                                                 && Character.isUpperCase(following);
    if (!endsWithBoundary)
    {
      return false;
    }
    boolean lowerCaseInWord = false;
    for ( int i = pos ; i < pos + length ; i++ )
    {
      if (Character.isLowerCase(line.charAt(i)))
      {
        lowerCaseInWord = true;
      }
      else if (lowerCaseInWord)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the list of typos found so far.
   */
  public List<String> getFindings()
  {
    return Collections.unmodifiableList(findings);
  }
}
