package edu.brown.cs.gajith.boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.CharMatcher;

/**
 * A single configuration of dice in a 4 x 4 layout. A Board contains four rows
 * and columns, numbered from 0,0 at the top left.
 */
public class Board {
  /**
   * A Dictionary based on the Online Scrabble Players' Dictionary.
   */
  private static final Dictionary OSPD = new Dictionary(
      "src/main/resources/ospd4.txt");

  private static final int SIZE = 4;

  /*
   * Suppose we also wanted to keep track of the orientation of a letter, so we
   * could provide a more faithful UI that showed some letters rotated. What
   * changes would you make to represent this addition information? What changes
   * would be made to the interface Board provides?
   */
  private final List<List<Character>> spots = new ArrayList<>();

  /**
   * Create a new, random board by rolling the dice in a standard Boggle set.
   */
  public Board() {
    List<Die> dice = new ArrayList<>(Die.ALL);
    Collections.shuffle(dice);
    for (int r = 0; r < SIZE; r++) {
      spots.add(new ArrayList<>());
      for (int c = 0; c < SIZE; c++) {
        spots.get(r).add(dice.get(SIZE * r + c).pick());
      }
    }
  }

  private static final CharMatcher LETTERS = CharMatcher.inRange('a', 'z');

  /**
   * Create a specific board specified by the letters in s.
   *
   * @param s
   *          A string containing 16 letters in the range 'a'..'z' which are
   *          placed in the board, row by row, starting from the left. Any other
   *          letters are discarded. 'q' implies 'qu' for that position.
   */
  public Board(String s) {
    s = LETTERS.retainFrom(s.toLowerCase());
    assert s.length() == SIZE * SIZE;
    for (int r = 0; r < SIZE; r++) {
      spots.add(new ArrayList<>());
      for (int c = 0; c < SIZE; c++) {
        spots.get(r).add(s.charAt(SIZE * r + c));
      }
    }
  }

  /**
   * Return a human readable version of this Board.
   *
   * @return 4 lines of 4 characters, representing the 4 rows of letters shown
   *         on the dice in this Board.
   */
  @Override
  public String toString() {
    return toString('\n');
  }

  /**
   * Return a human readable version of this Board.
   *
   * @param separator
   *          A character used to separate the four rows.
   * @return 4 string of 4 characters, joined by separator
   */
  public String toString(char separator) {
    StringBuilder sb = new StringBuilder();
    for (List<Character> row : spots) {
      for (Character c : row) {
        sb.append(c);
      }
      sb.append(separator);
    }
    return sb.toString();
  }

  /**
   * Return the String fragment represented by the Die face at the given row and
   * column. It is a String because the letter 'q' yields 'qu'.
   *
   * @param r
   *          The row, running from 0 at the top, to 3 at the bottom.
   * @param c
   *          The column, running from 0 on the left to 3 on the right.
   * @return The part of a word (usually one letter) that available at r, c, or
   *         the empty string if r,c is off the board.
   */
  public String get(int r, int c) {
    if (r < 0 || c < 0) {
      return "";
    }
    if (r >= spots.size()) {
      return "";
    }
    List<Character> line = spots.get(r);
    if (c >= line.size()) {
      return "";
    }
    char letter = line.get(c);
    return letter == 'q' ? "qu" : Character.toString(letter);
  }

  /**
   * Find all words that can found in this Board according to boggle rules.
   * Those rules allow movement horizontally, vertically, and diagonally,
   * without revisting the same space twice in one word.
   *
   * @return A set containing the words (which may include non-scoring words,
   *         such as "to").
   */
  public Set<String> play() {
    Set<String> all = new TreeSet<>();
    String str = "";
    int[][] visited = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
        { 0, 0, 0, 0 } };

    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {

        findWords(all, str, visited, i, j);

      }
    }

    return all;
  }

  private void findWords(Set<String> all, String str, int[][] visited, int row,
      int col) {
    // TODO Auto-generated method stub

    str = str + spots.get(row).get(col);
    visited[row][col] = 1;

    if (OSPD.contains(str)) {
      all.add(str);
    }

    for (int i = row - 1; i <= row + 1 && i < SIZE; i++) {
      for (int j = col - 1; j <= col + 1 && j < SIZE; j++) {
        if (i >= 0 && j >= 0 && visited[i][j] == 0) {

          findWords(all, str, visited, i, j);

        }
      }
    }

    visited[row][col] = 0;
    str.replace(str.substring(str.length() - 1), "");
  }

  /**
   * Score a word according to standard Boggle rules.
   *
   * @param word
   *          The word to be scored. The word is presumed legal.
   * @return The score.
   */
  public static int score(String word) {
    int len = word.length();
    assert len > 0;

    if (len >= SCORES.length) {
      len = SCORES.length - 1;
    }
    return SCORES[len];
  }

  private static final int[] SCORES = new int[] { 0, 0, 0, 1, 1, 2, 3, 5, 11 };
}
