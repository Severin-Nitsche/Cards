package com.github.severinnitsche.cards;

/**
 * This class represents the game state of a card game that includes multiple rounds.
 */
public class Game {

  public final int rounds;
  public final int players;
  private int round;
  private final int[] scores;

  /**
   * Creates a new tournament for the specified number of players and the given number of rounds
   * @param rounds the number of rounds to play
   * @param players the number of participants
   */
  public Game(int rounds, int players) {
    this.rounds = rounds;
    this.players = players;
    this.round = 1;
    this.scores = new int[players];
  }

  /**
   * @return the current round
   */
  public int round() {
    return round;
  }

  /**
   * Determine the penalty score of a given player
   * @param player the player to get the score of
   * @return the player's score
   */
  public int scoreOf(int player) {
    return scores[player];
  }

  /**
   * Set the scores after a round is played
   * @param scores the scores of every player
   */
  public void setScores(int... scores) {
    if (scores.length != players) {
      throw new IllegalArgumentException(
          String.format("Expected %d scores but got %d instead", players, scores.length));
    }
    round++;
    for (int i = 0; i < players; i++) {
      this.scores[i] += scores[i];
    }
  }
}
