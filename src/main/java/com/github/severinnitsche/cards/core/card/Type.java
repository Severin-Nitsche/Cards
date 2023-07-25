package com.github.severinnitsche.cards.core.card;

public enum Type {
  SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

  @Override
  public String toString() {
    return switch (this) {
      case ACE -> "A";
      case TEN -> "10";
      case JACK -> "J";
      case KING -> "K";
      case NINE -> "9";
      case EIGHT -> "8";
      case QUEEN -> "Q";
      case SEVEN -> "7";
    };
  }
}
