package com.github.severinnitsche.cards.core.card;

/**
 * This enum represents the allowed Color values in a game of cards
 */
public enum Color {
  HEART, DIAMONDS, CLUBS, SPADES;

  @Override
  public String toString() {
    return switch (this) {
      case CLUBS -> "♣️";
      case DIAMONDS -> "♦️";
      case HEART -> "♥️";
      case SPADES -> "♠️";
    };
  }
}
