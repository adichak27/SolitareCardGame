package cs3500.freecell.model;


/**
 * This class serves as the model creator where the user can either create a singelMove game or a
 * multiMove game using the create method. It also has an enum field which is either SINGLEMOVE or
 * MULTIMOVE to represent the users desired game type.
 */
public class FreecellModelCreator {

  /**
   * Represents the game type the user wants to play. Either a single move game or a multimove game.
   * note even if the user chooses multimove they can still make single moves.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE
  }

  /**
   * Method allows the user to choose the game type they want to play (Single or multi-move).
   * @param type either singlemove or multimove depending on what the user want to play
   * @return returns either a SimpleFreecellModel or MultiMoveModel depending on parameter
   */
  public static FreecellModel create(GameType type) {
    if (type.equals(GameType.SINGLEMOVE)) {
      return new SimpleFreecellModel();
    } else {
      return new MultiMoveModel();
    }
  }
}
