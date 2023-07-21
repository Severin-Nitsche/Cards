package com.github.severinnitsche.cards;

import java.util.Random;

/**
 * This class represents an actual round of cards.
 */
public class Round {

  private final Random source;
  private Card[] deck; // the unseen cards
  private int deckPtr; // the top card in the deck
  private int stackPtr; // the next card position in the stack
  private Card[] stack; // the played cards
  public final int players; // the number of players
  private Hand[] player; // the hands of the players
  private int current; // the current player

  public Round(Random source, int players, int beginner) {
    this.source = source;
    this.players = players;
    this.player = new Hand[players];
    this.current = beginner;

    this.deck = Card.values();
    this.stack = new Card[deck.length];
    this.deckPtr = deck.length - 1;
    this.stackPtr = 0;
    for (int i = 0; i < deck.length; i++) {
      int j = source.nextInt(deck.length);
      Card temp = deck[i];
      deck[i] = deck[j];
      deck[j] = temp;
    }
    this.stack[stackPtr++] = this.deck[deckPtr--];

    for (int c = 0; c < 6; c++) {
      for (int i = 0; i < players; i++) {
        this.player[(beginner+i)%players].draw(this.deck[deckPtr--]);
      }
    }
    this.player[beginner].draw(this.deck[deckPtr--]);
  }

}
