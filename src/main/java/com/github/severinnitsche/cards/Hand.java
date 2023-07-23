package com.github.severinnitsche.cards;

import java.util.Arrays;
import java.util.Iterator;

/**
 * This class represents the hand of a player.
 */
public class Hand implements Iterable<Card> {

  private Card[] hand;
  private int cards;

  /**
   * Creates an empty hand
   */
  public Hand() {
    this.hand = new Card[32];
    this.cards = 0;
  }

  /**
   * Removes the card
   * @param card the card to play
   */
  public void play(Card card) {
    var iterator = iterator();
    while (iterator.hasNext()) {
      Card held = iterator.next();
      if (held == card) {
        iterator.remove();
        return;
      }
    }
    throw new IllegalArgumentException("Cannot play card: "+card);
  }

  /**
   * Removes all cards from this hand
   */
  public void removeAll() {
    Arrays.fill(hand, null);
    this.cards = 0;
  }

  /**
   * Adds the specified card to the deck
   * @param card the card to take
   */
  public void draw(Card card) {
    cards++;
    for (int i = 0; i < hand.length; i++) {
      if (hand[i] == null) {
        hand[i] = card;
        return;
      }
    }
    // There is not enough room in the hand
    Card[] temp = this.hand;
    this.hand = new Card[this.hand.length+1];
    System.arraycopy(temp, 0, this.hand, 0, temp.length);
    this.hand[temp.length] = card;
  }

  /**
   * @return the number of cards in the hand
   */
  public int cards() {
    return cards;
  }

  /**
   * Convenience method to determine whether a given card is held in the hand
   * @param card the card to check
   * @return whether the provided card is held
   */
  public boolean holds(Card card) {
    for (Card hold : this) {
      if (hold == card) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return true, if there are only Jacks lefts, false otherwise
   */
  public boolean onlyJacks() {
    for (Card hold : this) {
      if (hold.type != Type.JACK) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Iterator<Card> iterator() {
    return new Iterator<Card>() {
      int i = 0;

      @Override
      public boolean hasNext() {
        while (i < hand.length && hand[i] == null) i++;
        return i < hand.length;
      }

      @Override
      public Card next() {
        return hand[i++];
      }

      @Override
      public void remove() {
        hand[--i] = null;
        cards--;
      }
    };
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("[ ");
    for (Card card : this) {
      builder.append(card);
      builder.append(" ");
    }
    builder.append("]");
    return builder.toString();
  }
}
