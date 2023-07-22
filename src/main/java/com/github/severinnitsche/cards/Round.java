package com.github.severinnitsche.cards;

import java.util.Random;

/**
 * This class represents an actual round of cards.
 */
public class Round {

  private final Random source; // a source of randomness

  private Stack deck; // the stash of cards to draw
  private Stack stack; // the stack of cards played

  public final int players; // the number of players

  private Hand[] player; // the hands of the players
  private int current; // the current player

  private int draws; // the number of cards to draw
  private Color wish; // the color of an active wish

  public Round(Random source, int players, int beginner) {
    this.source = source;

    this.deck = new Stack(Card.values());
    this.stack = new Stack(deck.capacity());
    deck.shuffle(source);
    stack.add(deck.draw());

    this.players = players;
    this.current = beginner;
    this.player = new Hand[players];

    for (int i = 0; i < players; i++) {
      this.player[i] = new Hand();
    }

    for (int c = 0; c < 6; c++) {
      for (int i = 0; i < players; i++) {
        this.player[(beginner+i)%players].draw(this.deck.draw());
      }
    }
    this.player[beginner].draw(this.deck.draw());
  }

  /**
   * @return the information for the next player
   */
  public Information info() {
    int[] numOfCards = new int[players];
    for (int i = 0; i < players; i++) {
      numOfCards[i] = player[i].cards();
    }
    return new Information(draws, wish, stack, player[current], numOfCards, current);
  }

  /**
   * Applies the given action to the game.
   * @param action the chosen action
   */
  public void act(Action action) {
    // TODO: Write Validator (externally)
    // TODO: Apply action (locally)
  }

}
