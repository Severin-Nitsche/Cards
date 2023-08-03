package com.github.severinnitsche.cards.core.action;

import com.github.severinnitsche.cards.core.card.Card;
import com.github.severinnitsche.cards.core.card.Color;

/**
 * This interface describes the single actions that might occur during a turn.
 */
public sealed interface Action {
  /**
   * This action is played when the actor deals a single card.
   * @param card the played card
   */
  record Play(Card card) implements Action {}

  /**
   * This action is played when the actor draws a number of cards.
   * @param number the number of cards to draw
   */
  record Draw(int number) implements Action {}

  /**
   * This action is played at the end of a game when all Jacks are played.
   */
  record PlayRemaining() implements Action {}

  /**
   * This action is played after a wish-able Jack was played.
   * @param color the wished color
   */
  record Wish(Color color) implements Action {}
}
