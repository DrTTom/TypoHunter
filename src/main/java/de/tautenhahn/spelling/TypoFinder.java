package de.tautenhahn.spelling;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


/**
 * Finds typos in different kind of text files, possibly of mixed language, using a black list. This will not
 * ensure correct spelling but may find some typos before others do. Contrary to spell checkers, this class
 * explicitly targets expressions which are not meant to be a text in natural language.
 */
public class TypoFinder
{

  private static final String STOP_WORD = "NO-SPELLCHECK";


  private static final Set<String> BLACKSET = new HashSet<>();

  static
  {
    readTypoList("typos_de.list", BLACKSET);
    readTypoList("typos_en.list", BLACKSET);
    readTypoList("own_findings.list", BLACKSET);
  }

  private final List<String> allowedPhrases = new ArrayList<>();

  private final List<String> findings = new ArrayList<>();

  private final Collection<String> words = new TreeSet<>();

  /**
   * Assuming that lines longer than that are nor intended to be ever read by humans.
   */
  private static final int LINE_LIMIT = 300;

  private static void readTypoList(String name, Collection<String> typos)
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
        check(path, lineNumber, line);
      }
    }
    catch (IOException e)
    {
      throw new IllegalArgumentException("problem reading file", e);
    }
  }

  /** Just the states while finding words */
  enum State
  {
    /** not in a word */
    OUTSIDE_WORD,
    /** in word with upper cases so far */
    IN_UPPERCASE,
    /** in word with lower case letter seen last */
    IN_LOWERCASE
  }

  void check(Path path, int lineNumber, String line)
  {
    int pos = 0;
    int[] wordStart = new int[1];
    State state = State.OUTSIDE_WORD;
    while (pos < line.length())
    {
      char c = line.charAt(pos);
      switch (state)
      {
        case OUTSIDE_WORD:
          state = startWord(pos, wordStart, c);
          break;
        case IN_UPPERCASE:
          state = continueWord(path, lineNumber, line, pos, wordStart, c);
          break;
        case IN_LOWERCASE:
          state = continueLowerCaseWord(path, lineNumber, line, pos, wordStart, c);
          break;
        default:
          throw new IllegalStateException("unimplemented case: " + state);
      }
      pos++;
    }
    if (state != State.OUTSIDE_WORD)
    {
      checkWord(path, lineNumber, line, wordStart[0], pos);
    }
  }

  private State continueLowerCaseWord(Path path,
                                      int lineNumber,
                                      String line,
                                      int pos,
                                      int[] wordStart,
                                      char c)
  {
    if (Character.isLowerCase(c))
    {
      return State.IN_LOWERCASE;
    }
    checkWord(path, lineNumber, line, wordStart[0], pos);
    wordStart[0] = pos;
    return Character.isUpperCase(c) ? State.IN_UPPERCASE : State.OUTSIDE_WORD;
  }

  private State continueWord(Path path, int lineNumber, String line, int pos, int[] wordStart, char c)
  {
    if (Character.isLetter(c))
    {
      return nextState(c);
    }
    checkWord(path, lineNumber, line, wordStart[0], pos);
    return State.OUTSIDE_WORD;
  }

  private State startWord(int pos, int[] wordStart, char c)
  {
    if (Character.isLetter(c))
    {
      wordStart[0] = pos;
      return nextState(c);
    }
    return State.OUTSIDE_WORD;
  }

  private State nextState(char c)
  {
    return Character.isUpperCase(c) ? State.IN_UPPERCASE : State.IN_LOWERCASE;
  }

  private void checkWord(Path p, int lineNumber, String line, int wordStart, int pos)
  {
    String word = line.substring(wordStart, pos).toLowerCase(Locale.GERMAN); // covers English letters too
    if (BLACKSET.contains(word))
    {
      for ( String allowed : allowedPhrases )
      {
        int apos = line.indexOf(allowed);
        if (apos != -1 && apos <= wordStart && apos + allowed.length() >= pos)
        {
          return;
        }
      }
      findings.add("Typo '" + word + "' in " + p + ", line " + lineNumber + ": " + line);
      words.add(word);
    }

  }

  /**
   * Returns the list of typos found so far.
   */
  public List<String> getFindings()
  {
    return Collections.unmodifiableList(findings);
  }


  /**
   * Returns a set of found words which are probably misspelled.
   */
  public Collection<String> getWords()
  {
    return words;
  }
}
