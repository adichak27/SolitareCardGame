package cs3500.freecell.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Testing class for Card.
 */
public class CardTest {

  private Card fiveD; // five of diamonds
  private Card twoC; // two of clubs
  private Card aceS; // ace of spades
  private Card kingH; // king of hearts
  private Card jackH; // jack of hearts
  private Card sevenC; // seven of clubs
  private Card invalid; // not valid card

  @Before
  public void initializeData() {
    this.fiveD = new Card(5, Suit.DIAMONDS);
    twoC = new Card(2, Suit.CLUBS);
    aceS = new Card(1, Suit.SPADES);
    kingH = new Card(13, Suit.HEARTS);
    jackH = new Card(11, Suit.HEARTS);
    sevenC = new Card(7, Suit.CLUBS);


  }

  @Test (expected = IllegalArgumentException.class)
  public void testCardConstructor() {
    Card invalid = new Card(0, Suit.HEARTS);
    Card nullCard = new Card(10, null);
    Card invalid2 = new Card(14, Suit.CLUBS);
  }

  @Test
  public void validCardArguements() {
    assertEquals(true, fiveD.validCardArguments(5, Suit.DIAMONDS));
    assertEquals(true, aceS.validCardArguments(1, Suit.SPADES));
    assertEquals(true, sevenC.validCardArguments(1, Suit.SPADES));

  }


  @Test
  public void isValidTest() {
    initializeData();
    assertEquals(true, fiveD.isValid());
    assertEquals(true, twoC.isValid());
    assertEquals(true, aceS.isValid());
    assertEquals(true, kingH.isValid());

  }

  @Test
  public void getSuitTest() {
    initializeData();
    assertEquals(Suit.DIAMONDS, fiveD.getSuit());
    assertEquals(Suit.SPADES, aceS.getSuit());
    assertEquals(Suit.CLUBS, twoC.getSuit());
    assertEquals(Suit.HEARTS, kingH.getSuit());

  }

  @Test
  public void getValue() {
    initializeData();
    assertEquals(5, fiveD.getValue());
    assertEquals(1, aceS.getValue());
    assertEquals(2, twoC.getValue());
    assertEquals(13, kingH.getValue());

  }

  @Test
  public void getColor() {
    initializeData();
    assertEquals("red", fiveD.getColor());
    assertEquals("black", aceS.getColor());
    assertEquals("black", twoC.getColor());
    assertEquals("red", kingH.getColor());
  }

  @Test
  public void sameColor() {
    assertEquals(false, fiveD.sameColor(aceS));
    assertEquals(true, fiveD.sameColor(fiveD));
    assertEquals(true, twoC.sameColor(aceS));
    assertEquals(false, aceS.sameColor(kingH));

  }

  @Test
  public void sameSuit() {
    assertEquals(false, fiveD.sameSuit(aceS));
    assertEquals(true, fiveD.sameSuit(fiveD));
    assertEquals(false, twoC.sameSuit(aceS));
    assertEquals(true, jackH.sameSuit(kingH));
  }

  @Test
  public void testToString() {
    assertEquals("A♠", aceS.toString());
    assertEquals("5♦", fiveD.toString());
    assertEquals("2♣", twoC.toString());
    assertEquals("J♥", jackH.toString());
  }
  // Test hashcode
}