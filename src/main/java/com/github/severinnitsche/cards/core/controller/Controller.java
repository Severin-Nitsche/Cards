package com.github.severinnitsche.cards.core.controller;

import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.card.Hand;

/**
 * This class controls the interactions during a game and may receive commands from varying sources.
 */
public interface Controller {

  /**
   * The player controllable by this controller (-1 for all)
   * @return the controllable player
   */
  int preferred();

  /**
   * Starts the next round if possible
   */
  void nextRound();

  /**
   * @return whether the current round has terminated
   */
  boolean hasRoundTerminated();

  /**
   * @return the current round (1 indexed)
   */
  int round();

  /**
   * @return the number of rounds
   */
  int rounds();

  /**
   * @return whether the game has terminated
   */
  boolean hasGameTerminated();

  /**
   * Get the cards of the specified player
   * @param player the player
   * @return their cards
   */
  Hand handOf(int player);

  /**
   * Determine the penalty score of a given player
   * @param player the player to get the score of
   * @return the player's score
   */
  int scoreOf(int player);

  /**
   * @return an array of scores
   */
  int[] scores();

  /**
   * Applies the given action to the game.
   * @param action the chosen action
   * @return whether the action was applied or not
   */
  boolean apply(Action action);

  /**
   * Retrieves and applies a foreign action
   * @return the action
   */
  Action fetchAndApply();

  /**
   * @return the information for the next player
   */
  Information turnInfo();

}
