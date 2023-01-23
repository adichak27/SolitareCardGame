package cs3500.freecell.view;


import java.io.IOException;

/**
 * This class serves as the text view of the freecell game. It's printed as a string using the
 * toString method. It also renders the board and helps print any errors messages by appending it
 * to the appendable. Fields include an appendable and a freecellModelState
 */
public class FreecellTextView implements FreecellView {

  private final FreecellModelState<?> model;
  private Appendable out;

  /**
   * Initiates model field.
   * @param model this is the game model to be passed in
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable ap) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    this.model = model;
    this.out = ap;
  }

  /**
   * Constructor for the freecell text view class.
   * @param model Takes in a simple freecell model that can't be null
   */
  public FreecellTextView(FreecellModelState<?> model) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    this.model = model;
    this.out = null;
  }

  /**
   * This method iterates through every pile and depicts each card as a string to portray the
   * solitaire game.
   * @return One long string which represents the view of Freecell gam
   */
  public String toString() {
    String stringView = "";
    if (model.getNumOpenPiles() == -1) { // game hasn't started
      return stringView;
    }

    // cycles through the foundation piles
    for (int i = 0; i < 4; i++) {
      if (i != 0 ) {
        stringView += "\n";
      }
      stringView += "F" + (i + 1) + ":";
      if (model.getNumCardsInFoundationPile(i) != 0) {
        for (int j = 0; j < model.getNumCardsInFoundationPile(i); j++) {
          if (j != 0) {
            stringView += ",";
          }
          stringView += " " + model.getFoundationCardAt(i, j).toString();
        }
      }
    }
    // cycles through the open piles
    for (int i = 0; i < model.getNumOpenPiles(); i++) {
      stringView += "\n" + "O" + (i + 1) + ":";
      if (model.getNumCardsInOpenPile(i) != 0) {
        stringView += " " + model.getOpenCardAt(i);
      }
    }
    // cycles through the cascade piles
    for (int i = 0; i < model.getNumCascadePiles(); i++) {
      stringView += "\n" + "C" + (i + 1)  + ":";

      for (int j = 0; j < model.getNumCardsInCascadePile(i); j++) {
        if (j != 0) {
          stringView += ",";
        }
        stringView += " " + model.getCascadeCardAt(i, j).toString();
      }
    }
    return stringView;
  }

  @Override
  public void renderBoard() throws IOException {
    out.append(this.toString());
    return;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    out.append(message);
    return;
  }
}
