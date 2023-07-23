package com.github.severinnitsche.cards;

public class ActionValidator {

  public static boolean validate(Action action, Information information) {
    if (information.drawCards() > 0) {
      boolean seven = false;
      for (Card card : information.hand()) {
        if (card.type == Type.SEVEN) {
          seven = true;
          break;
        }
      }
      return (!seven || (action instanceof Action.Play play
          && play.card().type == Type.SEVEN && information.hand().holds(play.card())))
          && (seven || (action instanceof Action.Draw draw && draw.number() == information.drawCards()));
    }
    if (information.wish() != null) {
      boolean wish = fittingCardWish(information.hand(), information.wish());
      return  (!wish || ((action instanceof Action.Play play
          && (play.card().color == information.wish() || play.card().type == Type.JACK)
          && information.hand().holds(play.card()))
          || (action instanceof Action.PlayRemaining && information.hand().onlyJacks())))
          && (wish || (action instanceof Action.Draw draw && draw.number() == 1));
    }
    if (!information.canWish()) {
      boolean fitting = fittingCard(information.hand(), information.stack().peek());
      return (!fitting || ((action instanceof Action.Play play
          && (play.card().color == information.stack().peek().color || play.card().type == information.stack().peek().type
          || play.card().type == Type.JACK)
          && information.hand().holds(play.card()))
          || (action instanceof Action.PlayRemaining && information.hand().onlyJacks())))
          && (fitting || (action instanceof Action.Draw draw && draw.number() == 1));
    }
    return action instanceof Action.Wish;
  }

  /**
   * Determines, if there is a fitting card for the wish in the hand
   * @param hand the hand to search a fitting card in
   * @param wish the wish to please
   * @return whether there is a fitting card in the hand
   */
  public static boolean fittingCardWish(Hand hand, Color wish) {
    for (Card card : hand) {
      if (card.color == wish || card.type == Type.JACK) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines, if there is a fitting card
   * @param hand the hand to search a fitting card in
   * @param top the top card of the stack
   * @return whether there is a fitting card in the hand
   */
  public static boolean fittingCard(Hand hand, Card top) {
    for (Card card : hand) {
      if (card.color == top.color
          || card.type == top.type
          || card.type == Type.JACK) {
        return true;
      }
    }
    return false;
  }

}
