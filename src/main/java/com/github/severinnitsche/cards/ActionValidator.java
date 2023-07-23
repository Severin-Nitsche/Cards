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
      boolean wish = false;
      for (Card card : information.hand()) {
        if (card.color == information.wish() || card.type == Type.JACK) {
          wish = true;
          break;
        }
      }
      return  (!wish || ((action instanceof Action.Play play
          && (play.card().color == information.wish() || play.card().type == Type.JACK)
          && information.hand().holds(play.card()))
          || (action instanceof Action.PlayRemaining && information.hand().onlyJacks())))
          && (wish || (action instanceof Action.Draw draw && draw.number() == 1));
    }
    if (!information.canWish()) {
      boolean fitting = false;
      for (Card card : information.hand()) {
        if (card.color == information.stack().peek().color
            || card.type == information.stack().peek().type
            || card.type == Type.JACK) {
          fitting = true;
          break;
        }
      }
      return (!fitting || ((action instanceof Action.Play play
          && (play.card().color == information.stack().peek().color || play.card().type == information.stack().peek().type
          || play.card().type == Type.JACK)
          && information.hand().holds(play.card()))
          || (action instanceof Action.PlayRemaining && information.hand().onlyJacks())))
          && (fitting || (action instanceof Action.Draw draw && draw.number() == 1));
    }
    return action instanceof Action.Wish;
  }

}
