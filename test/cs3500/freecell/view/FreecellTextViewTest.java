package cs3500.freecell.view;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

import cs3500.freecell.model.SimpleFreecellModel;

/**
 * Class to test the to String.
 */
public class FreecellTextViewTest {

  FreecellModelState<?> model;
  FreecellTextView view;

  /**
   * Initilize key variables.
   */
  private void initializeData() {
    model = new SimpleFreecellModel();
    view = new FreecellTextView(model);

  }

  @Test
  public void testToString() {
    initializeData();
    assertEquals("", view.toString());
  }
}