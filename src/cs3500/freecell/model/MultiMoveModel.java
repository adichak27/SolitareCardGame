package cs3500.freecell.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is a multiMoveModel which inherits many methods from the SimpleFreecellModel class. This
 * class allows the user to move multiple cards at once and works with the Freecell controller.
 * Inherits three piles as fields from the super class.
 */
public class MultiMoveModel extends SimpleFreecellModel {

  public MultiMoveModel() {
    super();
  }

  @Override
  protected void isMoveInputsValid(ArrayList<ArrayList<Card>> source, int pileNumber, int cardIndex,
                                   ArrayList<ArrayList<Card>> destination, int destPileNumber) {
    if (pileNumber < 0 || pileNumber >= source.size()) {
      throw new IllegalArgumentException("Pile number out of bounds");
    }
    if (destPileNumber < 0 || destPileNumber >= destination.size()) {
      throw new IllegalArgumentException("destPileNumber out of bounds");
    }
    if (cardIndex > source.get(pileNumber).size() - 1 || cardIndex < 0) {
      throw new IllegalArgumentException("Card Index out of bounds");
    }
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) {
    if (source == null || destination == null) {
      throw new IllegalArgumentException("Piletype must be non-null");
    }

    HashMap<PileType, ArrayList<ArrayList<Card>>> allPiles = new
            HashMap<PileType, ArrayList<ArrayList<Card>>>();
    getHashMapPiles(allPiles);
    ArrayList<ArrayList<Card>> src = allPiles.get(source);
    ArrayList<ArrayList<Card>> dest = allPiles.get(destination);

    isMoveInputsValid(src, pileNumber, cardIndex,dest, destPileNumber);

    ArrayList<Card> srcPile = src.get(pileNumber);
    ArrayList<Card> destPile = dest.get(destPileNumber);
    List<Card> cardsToBeMoved = srcPile.subList(cardIndex, srcPile.size());

    if (cardIndex == srcPile.size() - 1) {
      super.move(source, pileNumber, cardIndex, destination, destPileNumber);
    } else {
      // checks that source and destination are both cascade piles and theres enough free slots
      validBuildMove(src, pileNumber, cardIndex, dest, destPileNumber, source, destination,
              destPile);
      // checks if the build is valid
      isBuildValid(src.get(pileNumber), cardIndex);
      multiCardMove(cardIndex, pileNumber, destPileNumber, cardsToBeMoved);
    }
  }

  private void getHashMapPiles(HashMap<PileType, ArrayList<ArrayList<Card>>> allPiles) {
    allPiles.put(PileType.OPEN, getOpenPiles());
    allPiles.put(PileType.FOUNDATION, getFoundationPiles());
    allPiles.put(PileType.CASCADE, getCascadePiles());
  }

  // checks if the build from cardIndex onwards is valid
  private void isBuildValid(ArrayList<Card> cascadePiles, int cardIndex) {

    Card firstCard;
    Card secondCard;
    for (int i = cardIndex; i < cascadePiles.size() - 1; i ++) {
      firstCard = cascadePiles.get(i);
      secondCard = cascadePiles.get(i + 1);

      // throws error if the first and second cards are the same color or value isn't one less
      isMoveValid(secondCard, firstCard);
    }

  }

  // checks
  private void validBuildMove(ArrayList<ArrayList<Card>> srcPiles, int srcPileNumber,
                                int cardIndex, ArrayList<ArrayList<Card>> destPiles,
                                int destPileNumber, PileType source, PileType dest,
                              ArrayList<Card> destPile) {
    isMoveInputsValid(srcPiles, srcPileNumber, cardIndex, destPiles, destPileNumber);

    int cardsToBeMoved = srcPiles.get(srcPileNumber).size() - cardIndex;

    if (cardsToBeMoved > 1 && source != PileType.CASCADE) {
      throw new IllegalArgumentException("Can only move multiple cards from cascade pile.");
    }
    if (cardsToBeMoved > 1 && (dest == PileType.FOUNDATION || dest == PileType.OPEN)) {
      throw new IllegalArgumentException("Can only move multiple cards to cascade pile.");
    }
    if (!enoughSlotsToMove(cardsToBeMoved, destPile)) {
      throw new IllegalArgumentException("Not enough empty slots to move " + cardsToBeMoved +
              " cards");
    }

  }

  // already promise that the source and destination type is cascade by calling validBuildMove.
  // assumes build is already valid because we call isBuildValid() before this method.
  private void multiCardMove(int cardIndex, int pileNumber, int destPileNumber,
                             List<Card> cardsToBeMoved) {
    HashMap<PileType, ArrayList<ArrayList<Card>>> allPiles = new
            HashMap<PileType, ArrayList<ArrayList<Card>>>();
    getHashMapPiles(allPiles);

    ArrayList<ArrayList<Card>> cascadePiles = allPiles.get(PileType.CASCADE);
    ArrayList<Card> srcPile = cascadePiles.get(pileNumber);
    ArrayList<Card> destPile = cascadePiles.get(destPileNumber);
    Card lastCardInPile = destPile.get(destPile.size() - 1);

    isMoveValid(srcPile.get(cardIndex), lastCardInPile);

    destPile.addAll(cardsToBeMoved);
    srcPile.removeAll(cardsToBeMoved);

  }

  private boolean enoughSlotsToMove(int i, ArrayList<Card> destinationPile) {
    int k = 0; // number of free cascade piles
    int n = 0; // number of free open piles

    for (ArrayList<Card> pile : getCascadePiles()) {
      if (pile.isEmpty()) {
        k++;
      }
    }
    for (ArrayList<Card> pile : getOpenPiles()) {
      if (pile.isEmpty()) {
        n++;
      }
    }

    int maxNumOfCards = (n + 1) * (int) Math.pow(2, k);
    if (destinationPile.isEmpty()) {
      k--;
    }
    return i <= maxNumOfCards;
  }
}
