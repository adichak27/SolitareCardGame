package cs3500.freecell.model;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.freecell.view.FreecellTextView;

import static org.junit.Assert.assertEquals;

/**
 * This test class is exactly the same as SimpleFreecellModelTest but uses a MultiModeModel instead
 * of a SimpleFreecellModel. This verifies backward regression and all previous tests still pass.
 */
public class SingleMoveTestsWithMultiModel {

  private MultiMoveModel model;
  private FreecellTextView textView;
  private Card card1;
  private Card card2;
  private Card card4;
  private Card fourD;
  private List<Card> testDeck;
  private List<Card> validDeck;
  private List<Card> invalidDeck; // not 52 cards
  private List<Card> reverseDeck; // deck where aces are the last card in

  @Before
  public void initializeData() {
    card1 = new Card(1, Suit.SPADES);
    card2 = new Card(4, Suit.CLUBS);
    card4 = new Card(4, Suit.DIAMONDS);
    fourD = new Card(4, Suit.DIAMONDS);

    model = new MultiMoveModel();
    textView = new FreecellTextView(model);
    validDeck = new ArrayList<Card>();
    testDeck = new ArrayList<Card>();
    invalidDeck = new ArrayList<Card>(Arrays.asList(card1));
    validDeck = model.getDeck();

    reverseDeck = new ArrayList<Card>();
    final Suit[] suitArray = new Suit[]{Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES};

    for (int i = 13; i >= 1; i--) {
      for (int j = 0; j < 4; j++) {
        Suit suit = suitArray[j];
        Card newCard = new Card(i, suit);
        reverseDeck.add(newCard);
      }
    }
  }

  @Test
  public void testGetDeck() {
    initializeData();
    SimpleFreecellModel tempModel = new SimpleFreecellModel();

    assertEquals(0, testDeck.size());
    testDeck = tempModel.getDeck();
    assertEquals(52, testDeck.size());
    assertEquals(true, model.isDeckValid(validDeck));
  }

  @Test
  public void testIsDeckValid() {
    initializeData();
    assertEquals(true, model.isDeckValid(validDeck));

  }

  @Test
  public void testMyTest() {
    initializeData();
    model.startGame(validDeck, 4, 4, false);

    assertEquals(4, model.getNumCascadePiles());

    initializeData();
    model.startGame(validDeck, 4, 4, true);
    assertEquals(4, model.getNumCascadePiles());

  }

  // exceptions for isDeckValid()
  @Test (expected = IllegalArgumentException.class)
  public void testIsDeckValidException() {
    initializeData();
    // not 52 cards
    model.isDeckValid(invalidDeck);

  }

  @Test
  public void startGame() {
    initializeData();

    List<Card> unshuffledDeck = model.getDeck();
    // valid deck is currently unshuffled, but we shuffle it
    model.startGame(validDeck, 8, 4, true);

    boolean shuffled = false;
    for (Card c : unshuffledDeck) {
      for (Card c2 : validDeck) {
        if (!c2.equals(c)) {
          shuffled = true;
        }
      }
    }
    assertEquals(true, shuffled);
    assertEquals(8, model.getNumCascadePiles());
    assertEquals(4, model.getNumOpenPiles());
  }

  // test for startGame exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameExceptions() {
    initializeData();
    // invalid number of open piles
    model.startGame(validDeck, 2, 5, true);
  }

  // test for startGame exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidPiles() {

    initializeData();
    // invalid number of cascade piles
    model.startGame(validDeck, 3, 0, true);

  }

  // test for startGame exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameExceptionsInvalidDeck() {

    initializeData();
    // invalid deck to start with
    model.startGame(invalidDeck, 6, 1, true);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testIsMoveInputsValid() {
    initializeData();
    model.startGame(validDeck, 4, 4, false);
    ArrayList<ArrayList<Card>> sourcePiles = model.getCascadePiles();
    ArrayList<ArrayList<Card>> destinationPiles = model.getCascadePiles();
    ArrayList<ArrayList<Card>> nullPile = null;
    // pile number is greater than pile size
    model.isMoveInputsValid(sourcePiles, 7, 12, destinationPiles,
            1);
    // destinationPileNumber is greater than size of destination Pile Type
    model.isMoveInputsValid(sourcePiles, 1, 12, destinationPiles,
            10);
    // source and destination piles are null so it should throw error
    model.isMoveInputsValid(nullPile, 1, 12, nullPile,
            10);
    // card index is greater than the number of cards in pile
    model.isMoveInputsValid(sourcePiles, 1, 17, destinationPiles,
            10);

  }

  @Test
  public void move() {
    initializeData();
    model.startGame(reverseDeck, 4, 4, false);
    // test move from cascade to open
    model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    // test move from open to foundation
    model.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 0);
    initializeData();

    model.startGame(validDeck, 4, 4, false);
    // king of diamonds is last card
    assertEquals(new Card(13, Suit.DIAMONDS), model.getCascadeCardAt(1, 12));
    // move the king of diamonds to open pile
    model.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    assertEquals(12, model.getNumCardsInCascadePile(1));


    // checks if king of spades is at the end of pile 3
    assertEquals(new Card(13, Suit.SPADES), model.getCascadeCardAt(3,12));
    assertEquals(13, model.getNumCardsInCascadePile(3));
    // checks if queen of diamonds is last card in pile 1
    assertEquals(new Card(12, Suit.DIAMONDS), model.getCascadeCardAt(1,11));

    // move queen of diamonds in pile 1 to pile 3
    model.move(PileType.CASCADE, 1, 11, PileType.CASCADE, 3);

    // ensures card was removed from the first pile
    assertEquals(11, model.getNumCardsInCascadePile(1));

    // checks that queen of diamonds was added to pile 3
    assertEquals(14, model.getNumCardsInCascadePile(3));
    assertEquals(new Card(12, Suit.DIAMONDS), model.getCascadeCardAt(3,13));

    initializeData();
    // aces are at bottom of every cascade pile
    model.startGame(reverseDeck, 4, 4, false);

    // no cards in the second foundation pile
    assertEquals(0, model.getNumCardsInFoundationPile(1));
    // moves ace of diamonds to foundation pile
    model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);
    assertEquals(new Card(1, Suit.DIAMONDS), model.getFoundationCardAt(1,0));

    // move ace of clubs to foundations
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    assertEquals(new Card(1, Suit.CLUBS), model.getFoundationCardAt(0,0));

    // move two of clubs to foundation pile
    model.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
    assertEquals(new Card(2, Suit.CLUBS), model.getFoundationCardAt(0,1));

    model.move(PileType.CASCADE, 0, 10, PileType.OPEN, 0);
    assertEquals(new Card(3, Suit.CLUBS), model.getOpenCardAt(0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testIsOpenPileEmptyExceptions() {
    initializeData();
    model.startGame(validDeck, 4, 4, true);
    // move card to first open pile
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    // should throw exception because pile is no longer empty
    model.isOpenPileEmpty(0);
  }


  @Test (expected = IllegalArgumentException.class)
  public void testIsMoveValidExceptions() {
    initializeData();
    // suits are different, but value is not one less
    model.isMoveValid(card1, card4);
  }

  @Test
  public void testinitilizePiles() {
    initializeData();

    assertEquals(-1, model.getNumCascadePiles());
    assertEquals(-1, model.getNumOpenPiles());
    model.initilizePiles(8,4);
    assertEquals(8, model.getNumCascadePiles());
    assertEquals(4, model.getNumOpenPiles());
  }

  @Test
  public void testDeckDeal() {
    initializeData();
    model.initilizePiles(8, 4);
    model.deckDeal((ArrayList<Card>) validDeck);
    // testing to see if each cascade pile has the right number of cards
    assertEquals(7, model.getNumCardsInCascadePile(1));
    assertEquals(7, model.getNumCardsInCascadePile(2));
    assertEquals(6, model.getNumCardsInCascadePile(5));
    // if cards were distributed in round robin format than the last card would be pile 3 card 6
    assertEquals(true,
            validDeck.get(51).equals(model.getCascadeCardAt(3,6)));

  }

  // tests if the controller can get through an entire game with invalid inputs, and moving builds.
  @Test
  public void isGameOverWithMultiMoves() {
    initializeData();
    model.startGame(reverseDeck,4, 4, false);

    assertEquals(false, model.isGameOver());

    for (int i = 0; i < 4; i++) {
      for (int j = model.getNumCardsInCascadePile(i) - 1; j >= 0; j--) {
        model.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i);
      }
    }
    assertEquals(true, model.isGameOver());
  }

  @Test
  public void getNumCardsInFoundationPile() {
    initializeData();

    model.startGame(validDeck, 8, 4, false);
    assertEquals(0, model.getNumCardsInFoundationPile(1));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetNumCardsInFoundationPileExceptions() {
    initializeData();
    model.startGame(validDeck,6,4,true);
    //only 4 foundation piles so should throw exception
    model.getNumCardsInFoundationPile(5);
  }

  @Test
  public void getNumCascadePiles() {
    initializeData();
    assertEquals(-1, model.getNumCascadePiles());
    model.startGame(validDeck, 6, 4, true);
    assertEquals(6, model.getNumCascadePiles());

    initializeData();
    assertEquals(-1, model.getNumCascadePiles());
    model.startGame(validDeck, 8, 4, true);
    assertEquals(8, model.getNumCascadePiles());
  }

  @Test
  public void getNumCardsInCascadePile() {
    initializeData();
    model.startGame(validDeck, 7, 4, false);
    assertEquals(8, model.getNumCardsInCascadePile(1));
    assertEquals(7, model.getNumCardsInCascadePile(6));
    initializeData();
    model.startGame(validDeck, 5, 4, false);
    assertEquals(11, model.getNumCardsInCascadePile(0));
    assertEquals(10, model.getNumCardsInCascadePile(4));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetNumCardsInCascadePileException() {
    initializeData();
    model.startGame(validDeck, 7, 4, false);
    model.getNumCardsInCascadePile(-1);
    model.getNumCardsInCascadePile(-7);

  }

  @Test
  public void getNumCardsInOpenPile() {
    initializeData();
    model.startGame(validDeck, 4, 4, false);
    assertEquals(0, model.getNumCardsInOpenPile(0));
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(1, model.getNumCardsInOpenPile(0));

    initializeData();
    model.startGame(validDeck, 4, 3, false);
    assertEquals(0, model.getNumCardsInOpenPile(2));
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 2);
    assertEquals(1, model.getNumCardsInOpenPile(2));

  }

  @Test
  public void getNumOpenPiles() {
    initializeData();
    assertEquals(-1, model.getNumOpenPiles());
    model.startGame(validDeck, 8, 4, true);
    assertEquals(4, model.getNumOpenPiles());

    initializeData();
    model.startGame(validDeck,8,2,false);
    assertEquals(2, model.getNumOpenPiles());
  }

  @Test(expected = IllegalStateException.class)
  public void testGameHasNotStartedExceptions() {
    initializeData();
    model.getNumCardsInFoundationPile(1);
    model.getNumCardsInCascadePile(1);
    model.getNumCardsInOpenPile(1);
    model.getFoundationCardAt(1,1);

  }

  @Test
  public void getFoundationCardAt() {
    initializeData();
    model.startGame(reverseDeck,4, 4, false);

    for (int i = 0; i < 4; i++) {
      for (int j = model.getNumCardsInCascadePile(i) - 1; j >= 0; j--) {
        model.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i);
      }
    }
    assertEquals(new Card(7, Suit.CLUBS), model.getFoundationCardAt(0,6));
    assertEquals(new Card(13, Suit.DIAMONDS), model.getFoundationCardAt(1,12));
    assertEquals(new Card(8, Suit.SPADES), model.getFoundationCardAt(3,7));

  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetFoundationCardAt() {
    initializeData();
    model.startGame(validDeck, 8, 4, true);
    // only 4 foundation piles so it will through exception
    model.getFoundationCardAt(6,2);
    // there is less than two cards so it should throw exception
    model.getFoundationCardAt(4,2);

  }

  @Test
  public void getCascadeCardAt() {
    initializeData();
    model.startGame(validDeck,4,4,false);

    assertEquals(new Card(1, Suit.CLUBS), model.getCascadeCardAt(0,0));
    assertEquals(new Card(8, Suit.DIAMONDS), model.getCascadeCardAt(1,7));
    assertEquals(new Card(12, Suit.SPADES), model.getCascadeCardAt(3,11));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCascadeCardAt() {
    initializeData();
    model.startGame(validDeck,4,4,false);

    // pile is out bounds
    model.getCascadeCardAt(5, 1);

    // index is out of bounds
    initializeData();
    model.startGame(validDeck,4,4,false);
    model.getCascadeCardAt(5, 1);


  }

  @Test
  public void getOpenCardAt() {
    initializeData();
    model.startGame(validDeck,4,4,false);

    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(new Card(13, Suit.CLUBS), model.getOpenCardAt(0));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetOpenCardAtExceptions() {
    initializeData();
    model.startGame(validDeck,4,4,false);

    // pile is out bounds
    model.getOpenCardAt(5);
    // no cards in this pile
    model.getOpenCardAt(0);
  }

  @Test
  public void testHashCode() {
    initializeData();

    // these cards are equal
    assertEquals(true,card4.hashCode() == fourD.hashCode());
    // these two cards are not equal
    assertEquals(false,card2.hashCode() == fourD.hashCode());

  }

  @Test
  public void  testToString() {
    initializeData();
    assertEquals("", textView.toString());

    model.startGame(reverseDeck, 4, 4, false);
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",textView.toString());
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);

    assertEquals("F1:\n" +
            "F2: A♦\n" +
            "F3:\n" +
            "F4:\n" +
            "O1: A♣\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦\n" +
            "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠",textView.toString());
  }
}
