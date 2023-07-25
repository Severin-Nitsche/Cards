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
  private boolean canWish; // Is a wish possible?

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
    return new Information(draws, wish, stack, player[current], numOfCards, current, canWish);
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
    Information recentInformation = info();
    if (!ActionValidator.validate(action, recentInformation)) {
      return false;
    }
    if (action instanceof Action.Draw draw) {
      for (int i = 0; i < draw.number(); i++) {
        recentInformation.hand().draw(deck.draw());
        if (deck.cards() == 0) {
          var temp = deck;
          deck = stack;
          stack = temp;
          stack.add(deck.draw());
          deck.shuffle(source);
        }
      }
      if (wish == null) {
        if (draw.number() == 1 && !ActionValidator.fittingCard(recentInformation.hand(), stack.peek())) {
          current = (current + 1) % players;
        }
      } else {
        if (draw.number() == 1 && !ActionValidator.fittingCardWish(recentInformation.hand(), wish)) {
          current = (current + 1) % players;
        }
      }
      draws = 0;
      return true;
    }
    if (action instanceof Action.Wish wishAction) {
      canWish = false;
      wish = wishAction.color();
      current = (current + 1) % players;
      return true;
    }
    if (action instanceof Action.Play play) {
      recentInformation.hand().play(play.card());
      stack.add(play.card());
      if (play.card().type == Type.SEVEN) {
        draws += 2;
      }
      canWish = wish == null && play.card().type == Type.JACK;
      if (play.card().type != Type.JACK) {
        wish = null;
      }
      if (!canWish) {
        if (play.card().type == Type.ACE) {
          current = (current + 2) % players;
        } else {
          current = (current + 1) % players;
        }
      }
      if (recentInformation.hand().cards() == 0) {
        current = -1;
      }
      return true;
    }
    if (action instanceof Action.PlayRemaining) {
      recentInformation.hand().removeAll();
      current = -1;
      return true;
    }
    throw new IllegalStateException("Did not expect action to be of type: "+action.getClass());
  }

}
