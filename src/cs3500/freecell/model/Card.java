package cs3500.freecell.model;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a card in a standard deck of 52 cards.
 */
public class Card {
  private final int value;
  private final Suit suit;
  private final HashMap<Integer, String> cardVals;

  /**
   * Constructs a playing card with a value and a suit.
   * @param value the numerical value of the card (1-13)
   * @param suit the suit type of the card as a string: spades, hearts, clubs, diamonds
   */
  public Card(int value, Suit suit) {
    if (!validCardArguments(value, suit)) {
      throw new IllegalArgumentException("Card arguements are not valid");
    } else {
      this.value = value;
      this.suit = suit;
      this.cardVals = new HashMap<>();

      this.cardVals.put(1, "A");
      this.cardVals.put(11, "J");
      this.cardVals.put(12, "Q");
      this.cardVals.put(13, "K");

      for (int i = 2; i < 11; i++) {
        this.cardVals.put(i, Integer.toString(i));
      }
    }
  }

  /**
   * Verifies that the value and suit of the card can be found in a typical 52 deck of playing
   * cards. Value should be between 1 and 13. Suit should be one of Hearts, Diamonds, Clubs, Spades.
   * @param value integer value of the card
   * @param suit One of the four possible suits
   * @return true if card inputs are valid, false otherwise
   */
  public boolean validCardArguments(int value, Suit suit) {
    return suit != null && value <= 13 && value >= 1;
  }

  /**
   * Verifies that this card is valid and can be found in a typical 52 deck of playing cards.
   * @return true if the cards arguments are valid and throws an error if not
   */
  public boolean isValid() {
    return this.validCardArguments(getValue(), getSuit());
  }

  /**
   * Gets the suit type from this card.
   * @return the suit of the card
   */
  public Suit getSuit() {
    return this.suit;
  }

  /**
   * Gets the numerical value of this card.
   * @return the value of card
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Gets the color of this card: either black or red.
   * @return String representation of the color of this card
   */
  public String getColor() {
    String color = "";

    switch (suit) {
      case CLUBS:
        color = "black";
        break;
      case SPADES:
        color = "black";
        break;
      case HEARTS:
        color = "red";
        break;
      case DIAMONDS:
        color = "red";
        break;
      default:
        throw new RuntimeException("Suit is invalid");
    }
    return color;
  }

  /**
   * Determines if the given card is the same color than this one. Suit does not necessarily
   * matter.
   * @param c some other card to be compared with this one.
   * @return true if the same color, false if colors are different
   */
  public boolean sameColor(Card c) {
    return this.getColor().equals(c.getColor());
  }

  /**
   * Determines if the given card has the same suit as this one.
   *
   * @param c card to be compared to
   * @return true if the suits are the same and false otherwise
   */
  public boolean sameSuit(Card c) {
    return this.getSuit().equals(c.getSuit());
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Card) {
      Card c = (Card) o;
      return this.sameSuit(c) && this.value == c.getValue();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.suit) + Objects.hash(this.value);
  }

  /**
   * Converts this card's information into string format.
   * @return returns short string containing the cards value and suit
   */
  public String toString() {
    String suit;

    switch (this.suit) {
      case CLUBS:
        suit = "♣";
        break;
      case HEARTS:
        suit = "♥";
        break;
      case SPADES:
        suit = "♠";
        break;
      case DIAMONDS:
        suit = "♦";
        break;
      default: throw new RuntimeException("Suit is not valid");
    }
    return this.cardVals.get(this.value) + suit;
  }
}
