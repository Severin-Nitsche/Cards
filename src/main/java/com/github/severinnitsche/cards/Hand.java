package com.github.severinnitsche.cards;

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
   * Removes the card at the specified index and plays it
   * @param index the index of the index
   * @return the played index
   */
  public Card play(int index) {
    Card card = hand[index];
    hand[index] = null;
    if (card == null) {
      throw new IllegalArgumentException("Cannot play nothing");
    }
    cards--;
    return card;
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
}
