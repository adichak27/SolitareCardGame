package cs3500.freecell.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;


/**
 * Serves as the controller for the freecell game. Implements all the methods in the Freecell
 * interface. Controller works with both a single move freecell game and multimove move freecell
 * game. Controller takes in three inputs: FreecellModel, a readable, and an appendable. All three
 * inputs have to be non null.
 * @param <Card> My card class
 */
public class SimpleFreecellController<Card> implements FreecellController<Card> {

  private final FreecellModel<Card> model;
  private final Readable in;
  private final Appendable out;

  /**
   * Constructor for the controller class.
   * @param model Freecell model
   * @param rd readable
   * @param ap appendable
   */
  public SimpleFreecellController(FreecellModel<Card> model, Readable rd, Appendable ap) {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Could not start game.");
    }

    this.model = model;
    this.in = rd;
    this.out = ap;

  }

  /**
   * Checks if the given card index is valid (greater than one and a number).
   * @param s the string version of the card index
   * @return true if valid card index
   */
  protected boolean isValidCardIndex(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new NumberFormatException("\n" + "card Index is not correctly formatted");
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Invalid card index. Try again");
    }
    return true;
  }

  /**
   * Returns the pileType of the inputted string. String must be a length of 2
   * @param s the given string
   * @return a specific piletype
   */
  protected PileType getPileType(String s) {
    String firstChar = s.substring(0,1);
    PileType result = null;
    switch (firstChar) {
      case "C":
        result = PileType.CASCADE;
        break;
      case "F":
        result = PileType.FOUNDATION;
        break;
      case "O":
        result = PileType.OPEN;
        break;
      default:
        throw new IllegalArgumentException("Pile does not begin with O F or C. Try again");
    }
    return result;
  }

  /**
   * Checks if the given string is an integer string.
   *
   * @param s a string
   * @return true if the string can be parsed as an integer, false otherwise.
   */
  protected boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException | NullPointerException e) {
      return false;
    }
    // only got here if we didn't return false
    return true;
  }

  /**
   * Gets the index of the inputted string. String must be length 2. Example string include "C1" or
   * "12".
   * @param s String to be checked as an int
   * @return an int representing the index
   */
  protected int getIndex(String s) {
    // checks if first char is an int.
    if (isInteger(Character.toString(s.charAt(0)))) {
      return Integer.parseInt(s);
    }
    // returns only the second char which must be an integer
    return Integer.parseInt(s.substring(1,2));
  }

  /**
   * Tests to see if this string a valid pile. First character must be "O", "C", or "F" and second
   * character must be an int.
   * @param s a string of size 2.
   * @return true if the string is a valid pile
   */
  protected boolean isValidPile(String s) {
    if (s.length() != 2) {
      return false;
    }
    String stringIndex = s.substring(1);
    this.getPileType(s);
    this.isValidCardIndex(stringIndex);

    return true;
  }

  @Override
  public void playGame(List<Card> deck, int numCascades, int numOpens, boolean shuffle) {
    FreecellView view = new FreecellTextView(model, out);
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }


    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException e) {
      try {
        view.renderMessage("Could not start game.");
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      return;
    }

    Scanner myScanner = new Scanner(in);
    ArrayList<String> commandSeq = new ArrayList<>();

    try {
      view.renderBoard();
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }


    while (myScanner.hasNext()) {

      String s = myScanner.next();


      if (s.toUpperCase().startsWith("Q")) {

        try {
          view.renderMessage("\n" + "Game quit prematurely.");
        } catch (IOException e) {
          throw new IllegalStateException(e.getMessage());
        }
        return;
      }
      // check if they are piles
      if (commandSeq.size() == 0 || commandSeq.size() == 2) {
        try {
          if (isValidPile(s)) {

            commandSeq.add(s);
          }
        } catch (IllegalArgumentException e) {
          if (commandSeq.size() == 2) {
            try {
              view.renderMessage("\n" + "Invalid destination pile.");
            } catch (IOException ex) {
              throw new IllegalStateException(e.getMessage());
            }
          } else {
            try {
              view.renderMessage("\n" + "Invalid source pile.");
            } catch (IOException ex) {
              throw new IllegalStateException(e.getMessage());
            }
          }
        }
      } else if (commandSeq.size() == 1) {
        try {
          if (isValidCardIndex(s)) {
            commandSeq.add(s);
          }
        } catch (IllegalArgumentException e) {
          try {
            view.renderMessage("\n" + e.getMessage());
          } catch (IOException ex) {
            throw new IllegalStateException(e.getMessage());
          }
        }
      }
      if (commandSeq.size() == 3) {
        PileType sourcePile = getPileType(commandSeq.get(0));
        int sourcePileIndex = getIndex(commandSeq.get(0)) - 1;
        int cardIndex = getIndex(commandSeq.get(1)) - 1;
        PileType destination = getPileType(commandSeq.get(2));
        int destinationIndex = getIndex(commandSeq.get(2)) - 1;

        try {
          model.move(sourcePile, sourcePileIndex, cardIndex, destination, destinationIndex);
        } catch (IllegalArgumentException e) {
          try {
            view.renderMessage("\n" + e.getMessage() + "\n");
          } catch (IOException ex) {
            throw new IllegalStateException(e.getMessage());
          }
        }

        try {
          view.renderBoard();
        } catch (IOException e) {
          throw new IllegalStateException(e.getMessage());
        }
        commandSeq.clear();
      }
    }
    if (model.isGameOver()) {
      try {
        view.renderMessage("\n" + "Game over."); // add to.string method
      } catch (IOException e) {
        throw new IllegalStateException(e.getMessage());
      }
      return;
    }
    if (!myScanner.hasNext()) {
      throw new IllegalStateException("Nothing left to read");
    }
  }
}
