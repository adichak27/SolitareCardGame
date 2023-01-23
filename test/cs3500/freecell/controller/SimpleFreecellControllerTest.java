package cs3500.freecell.controller;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.Suit;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

import static org.junit.Assert.assertEquals;


/**
 * Class to test the methods in the controller class.
 */
public class SimpleFreecellControllerTest {

  private final Readable stringReader = new StringReader("");
  private final Readable stringReader2 = new StringReader("C2 q 14");
  private final Readable stringReader3 = new StringReader("q");
  private final Readable stringReader4 = new StringReader("B2 djsf C3 q");
  private final Readable stringReader5 = new StringReader("C1 13 O8 q");
  private final Readable stringReader6 = new StringReader("C1 13 O845");
  private final Readable stringReader7 = new StringReader("C1 hello F8 q");
  private final Readable stringReader8 = new StringReader("C2 11 q");
  private final Readable stringReader9 = new StringReader("adk hello O8");
  private final Readable stringReader10 = new StringReader("adk hello O8");
  private final Readable stringReader11 = new StringReader("C1 13 F1 C1 12 F1 C1 11 F1 C1 10 F1" +
          " C1 9 F1"
          + " C1 8 F1 C1 7 F1 C1 6 F1 C1 5 F1 C1 4 F1 C1 3 F1 C1 2 F1 C1 1 F1"
          + " C2 13 F2 C2 12 F2 C2 11 F2 C2 10 F2 C2 9 F2"
          + " C2 8 F2 C2 7 F2 C2 b3333333333 6 F2 C2 5 F2 C2 4 F2 C2 3 F2 C2 2 F2 C2 1 F2"
          + " C3 13 F3 C3 12 F3 C3 11 F3 C3 10 F3 C3 9 F3"
          + " C3 8 F3 C3 7 F3 C3 6 F3 C3 5 lafhv F3 C3 4 F3 C3 3 F3 C3 2 F3 C3 1 F3"
          + " C4 13 F4 C4 12 F4 C4 11 F4 C4 10 F4 C4 9 F4"
          + " er4 C4 8 F4 C4 7 F4 C4 6 F4 C4 5 F4 C4 4 F4 C4 3 F4 C4 2 F4 C4 1 F4");

  SimpleFreecellModel model2;

  SimpleFreecellModel model;

  StringBuffer appendable1;
  FreecellView view;

  List<Card> deck;

  SimpleFreecellController controller1;
  SimpleFreecellController controller2;
  SimpleFreecellController controller3;
  SimpleFreecellController controller4;
  SimpleFreecellController controller5;
  SimpleFreecellController controller6;
  SimpleFreecellController controller7;
  SimpleFreecellController controller8;
  SimpleFreecellController controller9;
  SimpleFreecellController controller10;
  SimpleFreecellController controller11;


  private final ArrayList<Card> reverseDeck = new ArrayList<Card>();
  private final Suit[] suitArray = new Suit[]{Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES};

  private void initData() {
    model = new SimpleFreecellModel();
    model2 = new SimpleFreecellModel();
    appendable1 = new StringBuffer();
    controller1 = new SimpleFreecellController(
            model, stringReader, appendable1);
    controller2 = new SimpleFreecellController(
            model, stringReader2, appendable1);
    controller3 = new SimpleFreecellController(
            model, stringReader3, appendable1);
    controller4 = new SimpleFreecellController(
            model, stringReader4, appendable1);
    controller5 = new SimpleFreecellController(
            model, stringReader5, appendable1);
    controller6 = new SimpleFreecellController(
            model, stringReader6, appendable1);
    controller7 = new SimpleFreecellController(
            model, stringReader7, appendable1);
    controller8 = new SimpleFreecellController(
            model, stringReader8, appendable1);
    controller9 = new SimpleFreecellController(
            model, stringReader9, appendable1);
    controller10 = new SimpleFreecellController(
            model, stringReader10, appendable1);
    controller11 = new SimpleFreecellController(
            model2, stringReader11, appendable1);

    view = new FreecellTextView(model, appendable1);
    deck = model.getDeck();

    for (int i = 13; i >= 1; i--) {
      for (int j = 0; j < 4; j++) {
        Suit suit = suitArray[j];
        Card newCard = new Card(i, suit);
        reverseDeck.add(newCard);

      }
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidReadable() {
    FreecellController invalidController1 = new SimpleFreecellController(model, stringReader, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidAppendable2() {
    initData();
    FreecellController invalidController2 = new SimpleFreecellController(model, null, appendable1);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullDeck() {
    initData();
    controller1.playGame(null, 8, 4, true);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNullModel() {
    initData();
    FreecellController invalidController3 = new SimpleFreecellController(
            null, stringReader, appendable1);
  }

  @Test
  public void testStartGame() {
    initData();
    controller1.playGame(deck, -1, 2, true);

    assertEquals("Could not start game.", appendable1.toString());

  }

  @Test (expected = IllegalArgumentException.class)
  public void testStartGame2() {
    initData();
    controller1.playGame(null, 5, 0, true);
  }


  @Test (expected = NumberFormatException.class)
  public void testIsValidCardIndex() {
    initData();
    controller1.isValidCardIndex("r");
  }

  @Test (expected = NumberFormatException.class)
  public void testIsValidCardIndex2() {
    initData();
    controller1.isValidCardIndex(null);
  }

  @Test
  public void testIsValidCardIndex3() {
    initData();
    assertEquals(true, controller1.isValidCardIndex("4"));
    assertEquals(true, controller1.isValidCardIndex("0"));
    assertEquals(true, controller1.isValidCardIndex("-11"));

  }

  @Test
  public void testIsValidPile() {
    initData();
    assertEquals(true, controller1.isValidPile("C1"));
    assertEquals(true, controller1.isValidPile("O9"));
    assertEquals(true, controller1.isValidPile("F9"));

    assertEquals(false, controller1.isValidPile("C1whoe"));
    assertEquals(false, controller1.isValidPile("O2 4"));
    assertEquals(false, controller1.isValidPile("F"));

  }

  @Test
  public void testGetPileType() {
    initData();
    assertEquals(PileType.CASCADE, controller1.getPileType("C1"));
    assertEquals(PileType.OPEN, controller1.getPileType("O4"));
    assertEquals(PileType.FOUNDATION, controller1.getPileType("F2"));

  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetPileType2() {
    initData();
    controller1.getPileType("j");
  }

  @Test
  public void testGetIndex() {
    initData();
    assertEquals(4, controller1.getIndex("C4"));
    assertEquals(12, controller1.getIndex("12"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetIndexException() {
    initData();
    controller1.getIndex("bc");
  }

  // test null deck
  @Test (expected = IllegalArgumentException.class)
  public void playGameExceptions() {
    initData();
    controller4.playGame(null,4,4, false);
  }

  @Test
  public void playGameInvalidOpenAndCascade() {
    initData();
    controller4.playGame(deck,3,4, false);
    assertEquals("Could not start game.", appendable1.toString());

    initData();

    controller4.playGame(deck,4,0, false);
    assertEquals("Could not start game.", appendable1.toString());

  }

  @Test (expected = IllegalStateException.class)
  public void testRenderException() {
    initData();
    Appendable failAppend = new FailingAppendable();

    SimpleFreecellController ctrl = new SimpleFreecellController<>(model, stringReader, failAppend);

    // calls render board and catches IOException and throws IllegalStateException
    ctrl.playGame(deck, 4, 4, false);

  }

  @Test
  public void testRenders() {
    initData();

    try {
      view.renderMessage("hi");
      assertEquals("hi", appendable1.toString());
    } catch (IOException e) {
      throw new IllegalStateException();
    }

    try {
      view.renderBoard();
      assertEquals("hi", appendable1.toString());
    } catch (IOException e) {
      throw new IllegalStateException();
    }

  }

  @Test
  public void playGameInvalidCardOrPileindex() {
    initData();


    controller7.playGame(deck, 4, 4, false);
    assertEquals("F1:\n"
            +
            "F2:\n"
            +
            "F3:\n"
            +
            "F4:\n"
            +
            "O1:\n"
            + "O2:\n"
            + "O3:\n"
            + "O4:\n"
            + "C1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n"
            + "C2: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n"
            + "C3: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n"
            + "C4: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n"
            + "\n"
            + "card Index is not correctly formatted\n"
            + "\n"
            + "card Index is not correctly formatted\n"
            + "Game quit prematurely.", appendable1.toString());

    initData();
    controller5.playGame(deck, 4, 4, false);
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
            "destPileNumber out of bounds\n" +
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
            "Game quit prematurely.", appendable1.toString());

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
            "Invalid source pile.\n" +
            "Game quit prematurely.", appendable1.toString());
  }

  // test quit for first pile, index, and destination pile
  @Test
  public void testQuit() {
    initData();
    controller3.playGame(deck, 4, 4, false);
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
            "Game quit prematurely.", this.appendable1.toString());

    initData();
    controller2.playGame(deck, 4, 4, false);
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
            "Game quit prematurely.", appendable1.toString());
    initData();
    controller8.playGame(deck, 4, 4, false);
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
            "Game quit prematurely.", appendable1.toString());


  }

  @Test
  public void testIsGameOver() {
    initData();

    // this controller puts all the cascade cards into a foundation pile through valid moves
    controller11.playGame(reverseDeck, 4, 4, false);
    String finalString = appendable1.toString();
    int finalStringLength = appendable1.toString().length();
    assertEquals("Game over.",
            finalString.substring(finalStringLength - 10, finalStringLength));
  }

  private class FailingAppendable implements Appendable {

    @Override
    public Appendable append(CharSequence csq) throws IOException {
      throw new IOException("Fail!");
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
      throw new IOException("Fail!");
    }

    @Override
    public Appendable append(char c) throws IOException {
      throw new IOException("Fail!");
    }
  }
}


