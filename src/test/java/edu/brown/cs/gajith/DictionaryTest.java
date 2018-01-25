package edu.brown.cs.gajith.boggle;

import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryTest  {
  @Test
  public void testNew() {
    Dictionary d = new Dictionary();
  }

  @Test
  public void testContains() {
    Dictionary d = new Dictionary("src/main/resources/ospd4.txt");
    assertTrue(d.contains("dog"));
    assertFalse(d.contains("voltron"));
  }

  @Test
  public void testPrefix() {
    Dictionary d = new Dictionary("src/main/resources/ospd4.txt");
    assertTrue(d.containsPrefix("do"));
    assertFalse(d.containsPrefix("dogew"));
  }
}
