package edu.brown.cs.gajith.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest  {

  @Test
  public void testRandomConstruction() {
    Board b = new Board();
    assertNotNull(b);
  }

  @Test
  public void testSpecificConstruction() {
    Board b = new Board("john,quit,done,here");
    assertNotNull(b);
    assertEquals(b.get(0,0), "j");
    assertEquals(b.get(1,0), "qu");
  }

  @Test
  public void testToString() {
    Board b = new Board("john,isnt,done,here");
    b.toString();
    b.toString(',');
  }


  @Test
  public void testPlay() {
  }

  @Test
  public void testScore() {
  }
}
