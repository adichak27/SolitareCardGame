package cs3500.freecell;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;

import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import java.util.List;

import cs3500.freecell.model.PileType;
import cs3500.freecell.model.Suit;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

/**
 * This class tests all of the methods implemented in the MultiMoveModel class and ensures that
 * all of the methods in the class work with the controller as well.
 */
public class MultiMoveModelTest {

  private FreecellModelCreator creator1;
  private FreecellModel multiModel;
  private List<Card> deck;
  private ArrayList<Card> reverseDeck = new ArrayList<Card>();
  private final Suit[] suitArray = new Suit[]{Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES};
  private FreecellView textView;

  private final Readable rd1 = new StringReader("C1 7 O2 C1 6 C4 C4 6 C3 q");
  private final Readable rd2 = new StringReader("C2 15 C4 q");
  private final Readable rd3 = new StringReader("C1 5 O2 C1 q");
  private final Readable rd4 = new StringReader("C4 4 C3 q");
  private final Readable rd5 = new StringReader("C1 2 O1 C2 2 O2 C3 2 O3 C4 2 O4 C1 1 F1");

  private final StringBuffer ap1 = new StringBuffer();
  private FreecellController controller1;
  private FreecellController controller2;
  private FreecellController controller3;
  private FreecellController controller4;
  private FreecellController controller5;





  private void initData() {
    creator1 = new FreecellModelCreator();
    multiModel = creator1.create(FreecellModelCreator.GameType.MULTIMOVE);
    deck = multiModel.getDeck();
    textView = new FreecellTextView(multiModel);
    controller1 = new SimpleFreecellController(multiModel, rd1, ap1);
    controller2 = new SimpleFreecellController(multiModel, rd2, ap1);
    controller3 = new SimpleFreecellController(multiModel, rd3, ap1);
    controller4 = new SimpleFreecellController(multiModel, rd4, ap1);

    for (int i = 13; i >= 1; i--) {
      for (int j = 0; j < 4; j++) {
        Suit suit = suitArray[j];
        Card newCard = new Card(i, suit);
        reverseDeck.add(newCard);

      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSingleMoveGame() {
    initData();
    FreecellModel simpleModel = creator1.create(FreecellModelCreator.GameType.SINGLEMOVE);
    simpleModel.startGame(deck, 4, 4, false);


    // attempts to move multple cards
    simpleModel.move(PileType.CASCADE, 1, 8, PileType.OPEN, 1);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testGetMultiMoveGame() {
    initData();
    FreecellModel multiModel = creator1.create(FreecellModelCreator.GameType.MULTIMOVE);
    multiModel.startGame(deck, 4, 4, false);

    // is not valid build and will throw error
    multiModel.move(PileType.CASCADE, 1, 8, PileType.OPEN, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalidPile() {
    initData();
    multiModel.startGame(deck, 4, 4, false);
    multiModel.move(PileType.CASCADE, 5, 1, PileType.OPEN, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalidCardIndex() {
    initData();
    multiModel.startGame(deck, 4, 4, false);
    multiModel.move(PileType.CASCADE, 4, 17, PileType.OPEN, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalidCardIndexNegativeNumber() {
    initData();
    multiModel.startGame(deck, 4, 4, false);
    multiModel.move(PileType.CASCADE, 4, -1, PileType.OPEN, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalidSource() {
    initData();
    multiModel.startGame(deck, 4, 4, false);
    multiModel.move(PileType.OPEN, 4, 17, PileType.OPEN, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMoveInvalidDestination() {
    initData();
    multiModel.startGame(deck, 4, 4, false);
    multiModel.move(PileType.CASCADE, 4, 17,
            PileType.FOUNDATION, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAlternateColorsWhenMovingBuild() {
    initData();
    multiModel.startGame(reverseDeck, 4, 4, false);
    multiModel.move(PileType.CASCADE, 4, 11,
            PileType.FOUNDATION, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValuesAreNotOneLess() {
    initData();
    multiModel.startGame(deck, 13, 4, false);
    multiModel.move(PileType.CASCADE, 4, 12,
            PileType.FOUNDATION, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEnoughSlots() {
    initData();
    multiModel.startGame(deck, 8, 4, false);
    multiModel.move(PileType.CASCADE, 0, 6, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 1, 6, PileType.OPEN, 1);
    multiModel.move(PileType.CASCADE, 2, 6, PileType.OPEN, 2);
    multiModel.move(PileType.CASCADE, 4, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 5, 5, PileType.CASCADE, 3);

    // not enough slots to move a build of two cards
    multiModel.move(PileType.CASCADE, 3, 6, PileType.CASCADE, 3);

  }

  @Test
  public void testMoveBuild() {
    initData();
    multiModel.startGame(deck, 8, 4, false);

    // moves a single cascade card using the MultiMoveModel class
    multiModel.move(PileType.CASCADE, 0, 6, PileType.OPEN, 3);
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4: K♣\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠", textView.toString());
    multiModel.move(PileType.CASCADE, 0, 5, PileType.CASCADE, 5);

    //multiple card move
    multiModel.move(PileType.CASCADE, 5, 5, PileType.CASCADE, 3);
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4: K♣\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠, Q♦, J♣\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠", textView.toString());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildException() {
    initData();
    multiModel.startGame(deck, 8, 4, false);

    multiModel.move(PileType.CASCADE, 0, 6, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 0, 5, PileType.CASCADE, 5);

    //multiple card move but the last card in destination pile is not correct color/value
    multiModel.move(PileType.CASCADE, 5, 5, PileType.CASCADE, 4);
  }


  @Test
  public void testMultiMoveWithController() {
    initData();

    controller1.playGame(deck, 8, 4, false);

    //private final Readable rd1 = new StringReader("C1 6 O2 C1 5 C4 C4 5 C3 q");

    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2: K♣\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2: K♣\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠, J♣\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
            "Card must be opposite color and value one less than the bottom card\n" +
            "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2: K♣\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠, J♣\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
            "Game quit prematurely.", ap1.toString());

  }


  @Test
  public void testMultiMoveCardIndexOutOfBounds() {
    initData();

    controller2.playGame(deck, 8, 4, false);
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
            "Card Index out of bounds\n" +
            "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
            "Game quit prematurely.", ap1.toString());
  }

  @Test
  public void testMoveToNonCascadePileWController() {
    initData();
    controller3.playGame(deck, 8, 4, false);
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
            "Can only move multiple cards to cascade pile.\n" +
            "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 3♣, 5♣, 7♣, 9♣, J♣, K♣\n" +
            "C2: A♦, 3♦, 5♦, 7♦, 9♦, J♦, K♦\n" +
            "C3: A♥, 3♥, 5♥, 7♥, 9♥, J♥, K♥\n" +
            "C4: A♠, 3♠, 5♠, 7♠, 9♠, J♠, K♠\n" +
            "C5: 2♣, 4♣, 6♣, 8♣, 10♣, Q♣\n" +
            "C6: 2♦, 4♦, 6♦, 8♦, 10♦, Q♦\n" +
            "C7: 2♥, 4♥, 6♥, 8♥, 10♥, Q♥\n" +
            "C8: 2♠, 4♠, 6♠, 8♠, 10♠, Q♠\n" +
            "Game quit prematurely.", ap1.toString());
  }

  @Test
  public void testMoveTooManyCardsWController() {
    initData();
    controller4.playGame(deck, 4, 4, false);
    assertEquals("F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n" +
            "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n" +
            "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n" +
            "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n" +
            "Not enough empty slots to move 10 cards\n" +
            "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n" +
            "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n" +
            "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n" +
            "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n" +
            "Game quit prematurely.", ap1.toString());
  }
}
