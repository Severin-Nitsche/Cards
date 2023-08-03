package com.github.severinnitsche.cards.core.controller;

import com.github.severinnitsche.cards.core.card.Hand;
import com.github.severinnitsche.cards.core.card.Stack;
import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.action.ActionValidator;
import com.github.severinnitsche.cards.core.card.Card;
import com.github.severinnitsche.cards.core.card.Color;
import com.github.severinnitsche.cards.core.card.Type;

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
  private boolean wishInProgress; // whether a wish is currently in progress
  private boolean mustWish; // Is a wish possible?

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
   * This method returns the hand of the given player
   * @param player the player to get the hand of
   * @return the player's hand
   */
  public Hand handOf(int player) {
    return this.player[player];
  }

  /**
   * @return the information for the next player
   */
  public Information info() {
    int[] numOfCards = new int[players];
    for (int i = 0; i < players; i++) {
      numOfCards[i] = player[i].cards();
    }
    return new Information(draws, wish, stack, player[current], numOfCards, current, mustWish);
  }

  /**
   * @return the scores for every player
   */
  public int[] scores() {
    int[] scores = new int[players];
    for (int i = 0; i < players; i++) {
      for (Card card : player[i]) {
        scores[i] += card.value;
      }
    }
    return scores;
  }

  /**
   * @return true, if this round has terminated, false otherwise
   */
  public boolean hasTerminated() {
    return current == -1;
  }

  /**
   * Applies the given action to the game.
   * @param action the chosen action
   * @return whether the action was applied or not
   */
  public boolean act(Action action) {
    // setup local variables
    Information info = info();
    Hand player = this.player[current];

    // validate actions
    if (!ActionValidator.validate(action, info)) {
      return false;
    }

    // execute actions
    if (action instanceof Action.Draw draw) {
      draws = 0;
      for (int i = 0; i < draw.number(); i++) {
        if (deck.capacity() > 0) {
          player.draw(deck.draw());
        } else {
          var temp = deck;
          deck = stack;
          stack = temp;
          stack.add(deck.draw());
          deck.shuffle(this.source);
        }
      }
      if (draw.number() == 1) {
        current = (current + 1) % players;
      }
    }
    if (action instanceof Action.Play play) {
      stack.add(player.play(play.card()));
      if (play.card().type == Type.ACE) {
        current = (current + 2) % players;
        wishInProgress = false;
        wish = null;
      } else if (play.card().type == Type.JACK) {
        if (wishInProgress) {
          wishInProgress = false;
          current = (current + 1) % players;
        } else {
          mustWish = true;
          wish = null;
        }
      } else if (play.card().type == Type.SEVEN) {
        draws += 2;
        current = (current + 1) % players;
        wishInProgress = false;
        wish = null;
      } else {
        current = (current + 1) % players;
        wishInProgress = false;
        wish = null;
      }
    }
    if (action instanceof Action.Wish wish) {
      wishInProgress = true;
      mustWish = false;
      this.wish = wish.color();
      current = (current + 1) % players;
    }
    if (action instanceof Action.PlayRemaining) {
      player.removeAll();
    }

    // check for the end of round
    if (player.cards() == 0) {
      current = -1;
    }
    return true;
  }

}
