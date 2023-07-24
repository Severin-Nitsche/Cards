package com.github.severinnitsche.cards.card;

import java.util.Random;

/**
 * This class represents a stack of cards.
 */
public class Stack {

  private Card[] stack;
  private int topPtr; // top card + 1

  /**
   * Creates a new stack from the fully filled cards array
   * @param cards a card array without null entries
   */
  public Stack(Card[] cards) {
    this.stack = cards;
    this.topPtr = cards.length;
  }

  /**
   * Creates a new stack with a maximum capacity of size
   * @param size the capacity
   */
  public Stack(int size) {
    this.stack = new Card[size];
  }

  /**
   * @return the maximum stack capacity
   */
  public int capacity() {
    return stack.length;
  }

  /**
   * Pop the top card from the stack
   * @return the first card
   */
  public Card draw() {
    return stack[--topPtr];
  }

  /**
   * Peek the first card of the stack
   * @return the first card
   */
  public Card peek() {
    return stack[topPtr-1];
  }

  /**
   * Add a card onto the stack
   * @param card the card to add
   */
  public void add(Card card) {
    stack[topPtr++] = card;
  }

  /**
   * @return the number of cards in the stack
   */
  public int cards() {
    return topPtr;
  }

  /**
   * Shuffle the stack
   * @param random the random source to use
   */
  public void shuffle(Random random) {
    for (int i = 0; i < topPtr; i++) {
      int j = random.nextInt(topPtr);
      Card temp = stack[i];
      stack[i] = stack[j];
      stack[j] = temp;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (int i = 0; i < topPtr - 1; i++) {
      builder.append(stack[i]);
      builder.append(", ");
    }
    if (topPtr != 0) {
      builder.append(stack[topPtr-1]);
    }
    builder.append("]");
    return builder.toString();
  }

}
