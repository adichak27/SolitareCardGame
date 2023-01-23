package cs3500.freecell.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Class representing freecell model. It only allows user to move one card at the time and works
 * hand in hand with the SimpleFreecell Controller. The three fields represents the three different
 * types of piles in a freecell game.
 */
public class SimpleFreecellModel implements FreecellModel<Card> {
  private ArrayList<ArrayList<Card>> openPiles;
  private ArrayList<ArrayList<Card>> cascadePiles;
  private ArrayList<ArrayList<Card>> foundationPiles;
  private boolean hasGameStarted;


  /**
   * Constructs the three main pile types.
   */
  public SimpleFreecellModel() {
    openPiles = new ArrayList<>();
    cascadePiles = new ArrayList<>();
    foundationPiles = new ArrayList<>();
    hasGameStarted = false;
  }

  @Override
  public List<Card> getDeck() {
    // complete deck of cards
    List<Card> finalDeck = new ArrayList<>();
    final Suit[] suitArray = new Suit[]{Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES};

    for (int i = 1; i <= 13; i++) {
      for (int j = 0; j < 4; j++) {
        Suit suit = suitArray[j];
        Card newCard = new Card(i, suit);
        finalDeck.add(newCard);
      }
    }
    return finalDeck;
  }


  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle) {
    if (!isDeckValid(deck)) {
      throw new IllegalArgumentException("Deck is not valid");
    }
    if (numCascadePiles < 4) {
      throw new IllegalArgumentException("Must have between four and eight cascade piles");
    }
    if (numOpenPiles < 1) {
      throw new IllegalArgumentException("Must have between 1 and 4 open piles");
    }
    if (shuffle) {
      Collections.shuffle(deck);
    }
    initilizePiles(numCascadePiles, numOpenPiles);
    deckDeal((ArrayList<Card>) deck);

  }

  /**
   * Ensures piles are valid and card to be moved is at the end of the pile.
   * @param source An ArrayList of ArrayList of cards (the source pile) the card is coming from
   * @param pileNumber the pile number for the pile type the card is coming from
   * @param cardIndex the index of the card that is moving
   * @param destination An ArrayList of ArrayList of cards (the destination pile) the card is coming
   *                   from
   * @param destPileNumber the pile number for the pile type the card is moving to
   */
  protected void isMoveInputsValid(ArrayList<ArrayList<Card>> source, int pileNumber, int cardIndex,
                      ArrayList<ArrayList<Card>> destination, int destPileNumber) {

    if (pileNumber < 0 || pileNumber >= source.size()) {
      throw new IllegalArgumentException("Pile number out of bounds");
    }
    if (destPileNumber < 0 || destPileNumber >= destination.size()) {
      throw new IllegalArgumentException("destPileNumber out of bounds");
    }
    if (cardIndex != source.get(pileNumber).size() - 1) {
      throw new IllegalArgumentException("Card is not at end of Pile");
    }
    // check if pile is empty

  }

  /**
   * Checks if the card can move to another pile and moves the card if possible.
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source
   *                       pile, starting at 0
   * @param destination    the type of the destination pile (see
   * @param destPileNumber the pile number of the given type, starting at 0
   */
  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game hasn't started");
    }
    HashMap<PileType,ArrayList<ArrayList<Card>>> piles = new HashMap<>();

    piles.put(PileType.CASCADE, this.cascadePiles);
    piles.put(PileType.OPEN, this.openPiles);
    piles.put(PileType.FOUNDATION, this.foundationPiles);

    ArrayList<ArrayList<Card>> sourcePiles = piles.get(source);
    ArrayList<ArrayList<Card>> destinationPiles = piles.get(destination);

    isMoveInputsValid(sourcePiles, pileNumber, cardIndex,destinationPiles, destPileNumber);

    ArrayList<Card> srcPile = sourcePiles.get(pileNumber);
    ArrayList<Card> destPile = destinationPiles.get(destPileNumber);

    Card cardToBeMoved = piles.get(source).get(pileNumber).remove(srcPile.size() - 1);


    switch (destination) {
      case OPEN:
        // if the open pile is empty then
        isOpenPileEmpty(destPileNumber);
        piles.get(destination).get(destPileNumber).add(cardToBeMoved);
        break;
      case FOUNDATION:
        // if the pile is empty check if the card to be moved is an ace
        if (foundationPiles.get(destPileNumber).isEmpty()) {
          if (cardToBeMoved.getValue() == 1) {
            piles.get(destination).get(destPileNumber).add(cardToBeMoved);
          } else {
            throw new IllegalArgumentException("Only Ace can be placed at bottom of foundation"
                    + "pile; Attempted to place" + cardToBeMoved);
          }
          // if it not empty check if the cards are oppsite colors and value is one less
        } else {
          Card lastCard = destPile.get(destPile.size() - 1);
          // checks if the suits are the same and value is one less than card to be moved

          isMoveValid(cardToBeMoved,lastCard);
          piles.get(destination).get(destPileNumber).add(cardToBeMoved);
        }
        break;
      case CASCADE:
        if (cascadePiles.get(destPileNumber).isEmpty()) {
          piles.get(destination).get(destPileNumber).add(cardToBeMoved);
        } else {
          Card lastCard = destPile.get(destPile.size() - 1);
          // check if the last card is opposite colors and value is one less. throws error if not
          // valid
          isMoveValid(cardToBeMoved, lastCard);
          piles.get(destination).get(destPileNumber).add(cardToBeMoved);
        }
        break;
      default:
        throw new IllegalArgumentException("Destination type is invalid");
    }
  }

  /**
   * This method is called when users want to move card to an empty pile. It throws an exceptions
   * if the open pile is not empty.
   * @param pileIndex an int representing the pileIndex of the openPile that is being examined.
   */
  public void isOpenPileEmpty(int pileIndex) {
    if (!openPiles.get(pileIndex).isEmpty()) {
      throw new IllegalArgumentException("cannot move card to non-empty openPile");
    }
  }

  /**
   * Ensures that the card to be moved is the opposite color of the last card in the destination
   * pile. Also ensures that the value of the card to be moved is one less than the card in the
   * destination pile.
   * @param cardToBeMoved the card to be moved
   * @param lastCardDestPile the final card in the destination pile
   */
  public void isMoveValid(Card cardToBeMoved, Card lastCardDestPile) {
    if (!cardToBeMoved.sameColor(lastCardDestPile)
            && cardToBeMoved.getValue() != lastCardDestPile.getValue() - 1) {
      throw new IllegalArgumentException("Card must be opposite color and value one less than"
              + " the bottom card");
    }
  }

  /**
   * Determines if the game has ended or not.
   * @return true if game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    // keeps track of total cards in all foundation pile
    int totalCards = 0;
    for (int i = 0; i < foundationPiles.size(); i++) {
      totalCards += getNumCardsInFoundationPile(i);
    }
    return totalCards == 52;
  }

  /**
   * Gets the number of cards in the given foundation pile.
   * @param index the index of the foundation pile, starting at 0
   * @return an integer representing the number of cards in the desired foundation pile
   */
  @Override
  public int getNumCardsInFoundationPile(int index) {
    if (index > 4 || index < 0) {
      throw new IllegalArgumentException("Index is out of range");
    }
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    return foundationPiles.get(index).size();
  }

  /**
   * Gets the number of cascade piles.
   * @return the number of cascade piles or -1 if game hasn't started
   */
  @Override
  public int getNumCascadePiles() {
    if (!hasGameStarted) {
      return -1;
    }
    return cascadePiles.size();
  }

  /**
   * Gets the number of cards in the desired cascade pile.
   * @param index the index of the cascade pile, starting at 0
   * @return the number of cards in the desired cascade pile
   */
  @Override
  public int getNumCardsInCascadePile(int index) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (index < 0 || index > getNumCascadePiles() - 1) {
      throw new IllegalArgumentException("The index is invalid");
    }
    return cascadePiles.get(index).size();
  }

  /**
   * Gets the number of cards in the desired open pile.
   * @param index the index of the open pile, starting at 0
   * @return the number of cards in the desired open pile. Either a zero or o
   */
  @Override
  public int getNumCardsInOpenPile(int index) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (index > getNumOpenPiles() - 1 || openPiles.get(index).size() > 1 || index < 0) {
      throw new IllegalArgumentException("Can't have more than one card in open pile");
    }
    if (openPiles.get(index) == null) {
      throw new IllegalArgumentException("Open pile is null");

    }

    return openPiles.get(index).size();
  }

  /**
   * Gets the number of open piles.
   * @return the number of open piles
   */
  @Override
  public int getNumOpenPiles() {
    if (!hasGameStarted) {
      return -1;
    }

    return openPiles.size();
  }

  /**
   * Gets the card in the desired foundation pile given the card index.
   * @param pileIndex the index of the foundation pile, starting at 0
   * @param cardIndex the index of the card in the above foundation pile, starting at 0
   * @return the card in the desired foundation pile given the card index
   */
  @Override
  public Card getFoundationCardAt(int pileIndex, int cardIndex) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (pileIndex > foundationPiles.size() - 1 || pileIndex < 0) {
      throw new IllegalArgumentException("Pile Index is out of bounds");
    }
    if (cardIndex > foundationPiles.get(pileIndex).size() - 1 || cardIndex < 0) {
      throw new IllegalArgumentException("Card Index is out of bounds");
    }

    return foundationPiles.get(pileIndex).get(cardIndex);
  }

  /**
   * Used for testing. Returns this cascadePile
   * @return An array list of an array list of cards.
   * @throws IllegalArgumentException if game has not started
   */
  protected ArrayList<ArrayList<Card>> getCascadePiles() {
    if (!hasGameStarted) {
      throw new IllegalArgumentException("Game has not started");
    }
    return this.cascadePiles;
  }

  /**
   * Used for testing. Returns this cascadePile
   * @return An array list of an array list of cards.
   * @throws IllegalArgumentException if game has not started
   */
  protected ArrayList<ArrayList<Card>> getOpenPiles() {
    if (!hasGameStarted) {
      throw new IllegalArgumentException("Game has not started");
    }
    return this.openPiles;
  }

  protected ArrayList<ArrayList<Card>> getFoundationPiles() {
    if (!hasGameStarted) {
      throw new IllegalArgumentException("Game has not started");
    }
    return this.foundationPiles;
  }

  /**
   * Gets the card in the desired cascade pile give the card index.
   * @param pileIndex the index of the cascade pile, starting at 0
   * @param cardIndex the index of the card in the above cascade pile, starting at 0
   * @return the card in the desired cascade pile give the card index
   */
  @Override
  public Card getCascadeCardAt(int pileIndex, int cardIndex) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (pileIndex > getNumCascadePiles() - 1 || pileIndex < 0 || cascadePiles.isEmpty()) {
      throw new IllegalArgumentException("Pile Index is out of bounds");
    }
    if (cardIndex > cascadePiles.get(pileIndex).size() - 1 || cardIndex < 0) {
      throw new IllegalArgumentException("Card Index is out of bounds");
    }


    return cascadePiles.get(pileIndex).get(cardIndex);
  }

  /**
   * Gets the card in the open pile given the pile number.
   * @param pileIndex the index of the open pile, starting at 0
   * @return the card in the open pile
   */

  @Override
  public Card getOpenCardAt(int pileIndex) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game hasn't started");
    }
    if (pileIndex < 0 || pileIndex > getNumOpenPiles() - 1) {
      throw new IllegalArgumentException("The pile index is invalid");
    }
    if (openPiles.get(pileIndex).size() == 0) {
      return null;
    }
    return openPiles.get(pileIndex).get(0);
  }

  /**
   * Determines if the given deck is valid. This means the deck isn't null, the size is 52, and
   * there are no duplicate cards.
   * @param d deck of cards
   * @return true if the deck is valid, false otherwise
   */
  public static boolean isDeckValid(List<Card> d) {
    if (d == null) {
      throw new IllegalArgumentException("Deck cannot be null");
    }
    if (d.size() != 52) {
      throw new IllegalArgumentException("Deck size is not 52");
    }

    for (int i = 0; i < 52; i++) {
      int duplicateCards = 0;
      Card card1 = d.get(i);
      for (int j = 0; j < 52; j++) {
        Card card2 = d.get(j);
        if (card1.equals(card2)) {
          duplicateCards++;
        }
        if (!card1.isValid()) {
          throw new IllegalArgumentException("Deck contains invalid card");
        }
        // should only be 1 duplicate card
        if (duplicateCards > 1) {
          throw new IllegalArgumentException("Deck contains duplicate cards");
        }
      }
    }
    return true;
  }

  /**
   * Initilizes the cascade piles and open piles.
   * @param numCascadePiles number of cascade piles the user wants to start with (usually 8)
   * @param numOpenPiles number of open piles user wants to start with (usually 4)
   */
  protected void initilizePiles(int numCascadePiles, int numOpenPiles) {
    // initilize cascade piles
    openPiles = new ArrayList<>();
    cascadePiles = new ArrayList<>();
    foundationPiles = new ArrayList<>();

    for (int i = 0; i < numCascadePiles; i++) {
      ArrayList<Card> newCascadePile = new ArrayList<>();
      cascadePiles.add(newCascadePile);
    }
    // initilize open piles
    for (int i = 0; i < numOpenPiles; i++) {
      ArrayList<Card> newOpenPile = new ArrayList<>();
      openPiles.add(newOpenPile);
    }
    for (int i = 0; i < 4; i++) {
      ArrayList<Card> emptyList = new ArrayList<>();
      foundationPiles.add(emptyList);
    }
    hasGameStarted = true;
  }

  /**
   * Deals the card in a round robin fashion among the cascade piles.
   * @param deck deck of cards to be dealt to the cascade piles
   */
  protected void deckDeal(ArrayList<Card> deck) {
    for (int i = 0; i < getNumCascadePiles(); i++) {
      for (int j = i; j < deck.size(); j += getNumCascadePiles()) {
        cascadePiles.get(i).add(deck.get(j));
      }
    }
  }
}
