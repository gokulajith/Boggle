package edu.brown.cs.gajith.boggle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/** A set of words that can be queried for membership.  A Dictionary
 * can also report whether a given string is the prefix (beginning
 * part) of any word in the Dictionary.
 *
 * @author John Jannotti
 */

public class Dictionary  {
  /* 'words' and 'prefixes' are not only private but DO NOT have
     getters (like getWords()).  This prevents callers from grabbing
     the words and potentially manipulating them. contains() and
     containsPrefix() let callers check membership, and that's good
     enough.

     If you felt it was needed, how would you provide safe access to
     all of the words in the Dictionary?
  */

  private final Set<String> words = new HashSet<>();
  private final Set<String> prefixes = new HashSet<>();

  /** Construct an empty Dictionary. */
  public Dictionary() {
  }

  /** Build a dictionary from individual words
   * @param input The words to put in the dictionary
   */
  public Dictionary(Iterable<String> input) {
    addAll(input);
  }

  /** Build a dictionary from a file containing one word per line.
   * @param filename The name of the file to read words from.
   */
  public Dictionary(String filename) {
    try (Stream<String>  lines = Files.lines(new File(filename).toPath())) {
      addAll(lines::iterator);
    } catch (IOException e) {
      throw new IllegalArgumentException(filename, e);
    }
  }

  /** Add all of the provided words to the Dictionary.
   * @param input The words to put in the dictionary
   */
  public void addAll(Iterable<String> input) {
    for (String word : input) {
      words.add(word);
      for (int i = 1; i < word.length(); i++) {
        prefixes.add(word.substring(0, i));
      }
    }
  }

  /** Reports on the existence of word in this Dictionary.
   * @param word The string to test
   * @return true if word is in this Dictionary, else false.
   */
  public boolean contains(String word) {
    return words.contains(word);
  }

  /** Reports on whether a string is a prefix of any word in this
   * Dictionary. A word is a prefix of itself.
   * @param pref The string to test
   * @return true if pref is the beginning of some word in this
   *          Dictionary, else false.
   */
  public boolean containsPrefix(String pref) {
    return prefixes.contains(pref);
  }
}
