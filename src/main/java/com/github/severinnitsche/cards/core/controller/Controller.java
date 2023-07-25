package com.github.severinnitsche.cards.core.controller;

import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.card.Hand;

import java.util.Random;

/**
 * This class controls the interactions during a game and may receive commands from varying sources.
 */
public class Controller {

  private Game game;
  private Round current;
  private Random source;

  /**
   * Sets up a new Controller for a game of the specified number of rounds and players
   * @param rounds the number of rounds to play
   * @param players the number of players
   * @param source the source of randomness used for shuffling
   */
  public Controller(int rounds, int players, Random source) {
    this.game = new Game(rounds, players);
    this.source = source;
  }

  /**
   * Starts the next round if possible
   */
  public void nextRound() {
    if (current == null) {
      current = new Round(source, game.players, game.round() % game.players);
      return;
    }
    if (current.hasTerminated() && game.round() <= game.rounds) {
      game.setScores(current.scores());
      current = new Round(source, game.players, game.round() % game.players);
    }
  }

  /**
   * @return whether the current round has terminated
   */
  public boolean hasRoundTerminated() {
    return current == null || current.hasTerminated();
  }

  /**
   * @return the current round (1 indexed)
   */
  public int round() {
    return game.round();
  }

  /**
   * @return the number of rounds
   */
  public int rounds() {
    return game.rounds;
  }

  /**
   * @return whether the game has terminated
   */
  public boolean hasGameTerminated() {
    return game.round() > game.rounds;
  }

  /**
   * Get the cards of the specified player
   * @param player the player
   * @return their cards
   */
  public Hand handOf(int player) {
    return current.handOf(player);
  }

  /**
   * Determine the penalty score of a given player
   * @param player the player to get the score of
   * @return the player's score
   */
  public int scoreOf(int player) {
    return game.scoreOf(player);
  }

  /**
   * @return an array of scores
   */
  public int[] scores() {
    int[] scores = new int[game.players];
    for (int i = 0; i < game.players; i++) {
      scores[i] = game.scoreOf(i);
    }
    return scores;
  }

  /**
   * Applies the given action to the game.
   * @param action the chosen action
   * @return whether the action was applied or not
   */
  public boolean apply(Action action) {
    if (current == null || current.hasTerminated() || hasGameTerminated()) {
      throw new IllegalStateException("Cannot apply an action without a running round!");
    }
    return current.act(action);
  }

  /**
   * @return the information for the next player
   */
  public Information turnInfo() {
    if (current == null || current.hasTerminated() || hasGameTerminated()) {
      throw new IllegalStateException("Cannot fetch turn information without a running round!");
    }
    return current.info();
  }

}
