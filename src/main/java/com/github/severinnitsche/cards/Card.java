package com.github.severinnitsche.cards;

import static com.github.severinnitsche.cards.Color.*;
import static com.github.severinnitsche.cards.Type.*;

/**
 * This enum contains all 32 cards in a deck with their respective values.
 */
public enum Card {
  SEVEN_OF_HEARTS(7, HEART, SEVEN), EIGHT_OF_HEARTS(8, HEART, EIGHT),
  NINE_OF_HEARTS(9, HEART, NINE), TEN_OF_HEARTS(10, HEART, TEN),
  JACK_OF_HEARTS(20, HEART, JACK), QUEEN_OF_HEARTS(10, HEART, QUEEN),
  KING_OF_HEARTS(10, HEART, KING), ACE_OF_HEARTS(11, HEART, ACE),
  SEVEN_OF_DIAMONDS(7, DIAMONDS, SEVEN), EIGHT_OF_DIAMONDS(8, DIAMONDS, EIGHT),
  NINE_OF_DIAMONDS(9, DIAMONDS, NINE), TEN_OF_DIAMONDS(10, DIAMONDS, TEN),
  JACK_OF_DIAMONDS(20, DIAMONDS, JACK), QUEEN_OF_DIAMONDS(10, DIAMONDS, QUEEN),
  KING_OF_DIAMONDS(10, DIAMONDS, KING), ACE_OF_DIAMONDS(11, DIAMONDS, KING),
  SEVEN_OF_CLUBS(7, CLUBS, SEVEN), EIGHT_OF_CLUBS(8, CLUBS, EIGHT),
  NINE_OF_CLUBS(9, CLUBS, NINE), TEN_OF_CLUBS(10, CLUBS, TEN),
  JACK_OF_CLUBS(20, CLUBS, JACK), QUEEN_OF_CLUBS(10, CLUBS, QUEEN),
  KING_OF_CLUBS(10, CLUBS, KING), ACE_OF_CLUBS(11, CLUBS, ACE),
  SEVEN_OF_SPADES(7, SPADES, SEVEN), EIGHT_OF_SPADES(8, SPADES, EIGHT),
  NINE_OF_SPADES(9, SPADES, NINE), TEN_OF_SPADES(10, SPADES, TEN),
  JACK_OF_SPADES(20, SPADES, JACK), QUEEN_OF_SPADES(10, SPADES, QUEEN),
  KING_OF_SPADES(10, SPADES, KING), ACE_OF_SPADES(11, SPADES, ACE);

  public final int value;
  public final Color color;
  public final Type type;

  Card(int value, Color color, Type type) {
    this.value = value;
    this.color = color;
    this.type = type;
  }
}
