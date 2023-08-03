package com.github.severinnitsche.cards.core.action;

import com.github.severinnitsche.cards.core.card.Hand;
import com.github.severinnitsche.cards.core.controller.Information;
import com.github.severinnitsche.cards.core.card.Card;
import com.github.severinnitsche.cards.core.card.Color;
import com.github.severinnitsche.cards.core.card.Type;

public class ActionValidator {

  public static boolean validate(Action action, Information information) {
    if (information.canWish()) {
      return action instanceof Action.Wish;
    }
    if (information.drawCards() > 1) {
      if (information.hand().hasSeven()) {
        return action instanceof Action.Play play
            && play.card().type == Type.SEVEN
            && information.hand().holds(play.card());
      }
      return action instanceof Action.Draw draw && draw.number() == information.drawCards();
    }
    if (information.hand().onlyJacks()) {
      if (action instanceof Action.PlayRemaining) {
        return true;
      }
    }
    if (hasFittingCard(information.hand(), information.stack().peek(), information.wish())) {
      return action instanceof Action.Play play
          && isFittingCard(play.card(), information.stack().peek(), information.wish());
    }
    return action instanceof Action.Draw draw && draw.number() == 1;
  }

  /**
   * Determines, if there is a fitting card
   * @param hand the hand to search a fitting card in
   * @param top the top card of the stack
   * @param wish the wish to please, or null
   * @return whether there is a fitting card in the hand
   */
  public static boolean hasFittingCard(Hand hand, Card top, Color wish) {
    if (wish == null) {
      return fittingCard(hand, top);
    }
    return fittingCardWish(hand, wish);
  }

  /**
   * Determines, if there is a fitting card for the wish in the hand
   * @param hand the hand to search a fitting card in
   * @param wish the wish to please
   * @return whether there is a fitting card in the hand
   */
  private static boolean fittingCardWish(Hand hand, Color wish) {
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
  private static boolean fittingCard(Hand hand, Card top) {
    for (Card card : hand) {
      if (card.color == top.color
          || card.type == top.type
          || card.type == Type.JACK) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines, if the card fits
   * @param card the card to test
   * @param top the top card of the stack
   * @param wish the wish to please, or null
   * @return whether there is a fitting card in the hand
   */
  public static boolean isFittingCard(Card card, Card top, Color wish) {
    if (wish == null) {
      return fitsNoWish(card, top);
    }
    return fitsWish(card, wish);
  }

  /**
   * Determines, if the card fits the wish
   * @param card the card to test
   * @param wish the wish to please
   * @return whether there is a fitting card in the hand
   */
  private static boolean fitsWish(Card card, Color wish) {
    return card.color == wish || card.type == Type.JACK;
  }

  /**
   * Determines, if the card fits
   * @param card the card to test
   * @param top the top card of the stack
   * @return whether there is a fitting card in the hand
   */
  private static boolean fitsNoWish(Card card, Card top) {
    return card.color == top.color
        || card.type == top.type
        || card.type == Type.JACK;
  }

}
