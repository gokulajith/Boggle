package edu.brown.cs.gajith.boggle;

import java.util.List;
import java.util.Random;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;

/** A six-sided die, with a character (letter) on each side.  It can
 * be "rolled" by calling pick(), yielding one of the six sides.
 *
 * @author John Jannotti
 */

public class Die {
  /* 'sides' is final.  What does that mean? With that in mind, if
   * 'sides' were public, could callers to the Die abstraction be able
   * to change it, or its contents in any way? If not, why don't we
   * just make it public? */

  private static final int SIDES = 6;
  private final String sides;   // A 'SIDES' character string, storing
                                // the letters for each side.

  private static final CharMatcher LETTERS = CharMatcher.inRange('a', 'z');

  /** Construct a Die using the lower-case letters extracted from 's'.
   * 's' must contain exactly 6 such letters.
   * @param  s  A String containing 6 letters from 'a'..'z'
   */

  public Die(String s) {
    sides = LETTERS.retainFrom(s.toLowerCase());
    // Read up on the 'assert' statement.  In this context, throwing
    // an IllegalArgumentException might make sense as well.  How
    // would you decide?
    assert sides.length() == SIDES;
  }

  /* The 'ALL' List is public but it, and its contents, are immutable,
   * so it's safe.  */
  /* The standard dice in a game of Boggle.
   */
  public static final List<Die> ALL =
    ImmutableList.of(new Die("t o e s s i"),
                     new Die("a s p f f k"),
                     new Die("n u i h m q"),
                     new Die("o b j o a b"),
                     new Die("l n h n r z"),
                     new Die("a h s p c o"),
                     new Die("r y v d e l"),
                     new Die("i o t m u c"),
                     new Die("l r e i x d"),
                     new Die("t e r w h v"),
                     new Die("t s t i y d"),
                     new Die("w n g e e h"),
                     new Die("e r t t y l"),
                     new Die("o w t o a t"),
                     new Die("a e a n e g"),
                     new Die("e i u n e s"));

  /* 'R' is "static". What does that mean?  Suppose we removed the
   * "static" keyword.  Would the callers of the 'Die' abstraction
   * care?  What changes? */
  private static final Random R = new Random();

  /** Select a random face from the Die.
   * @return The character on a random side of the die.
   */
  public char pick() {
    return sides.charAt(R.nextInt(sides.length()));
  }
}
