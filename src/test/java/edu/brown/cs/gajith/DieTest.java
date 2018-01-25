package edu.brown.cs.gajith.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

public class DieTest {
  @Test
  public void testNew() {
    Die d = new Die("abcdef");
  }

  @Test
  public void testPick() {
    Die a = new Die("aaaaaa");
    assertEquals(a.pick(), 'a');
    assertEquals(a.pick(), 'a');
    assertEquals(a.pick(), 'a');

    Die ab = new Die("ababab");
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
    assertTrue("ab".indexOf(ab.pick()) >= 0);
  }
}
