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
  KING_OF_DIAMONDS(10, DIAMONDS, KING), ACE_OF_DIAMONDS(11, DIAMONDS, ACE),
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

  /**
   * Transforms Card Mnemonics into Cards
   * @param mnemonic the mnemonic
   * @return the corresponding card
   */
  public static Card fromMnemonic(String mnemonic) {
    return switch (mnemonic.toLowerCase()) {
      case "h7", "♥️7" -> SEVEN_OF_HEARTS;
      case "h8", "♥️8" -> EIGHT_OF_HEARTS;
      case "h9", "♥️9" -> NINE_OF_HEARTS;
      case "h10", "♥️10" -> TEN_OF_HEARTS;
      case "hb", "♥️b" -> JACK_OF_HEARTS;
      case "hd", "♥️d" -> QUEEN_OF_HEARTS;
      case "hk", "♥️k" -> KING_OF_HEARTS;
      case "ha", "♥️a" -> ACE_OF_HEARTS;
      case "k7", "♦️7" -> SEVEN_OF_DIAMONDS;
      case "k8", "♦️8" -> EIGHT_OF_DIAMONDS;
      case "k9", "♦️9" -> NINE_OF_DIAMONDS;
      case "k10", "♦️10" -> TEN_OF_DIAMONDS;
      case "kb", "♦️b" -> JACK_OF_DIAMONDS;
      case "kd", "♦️d" -> QUEEN_OF_DIAMONDS;
      case "kk", "♦️k" -> KING_OF_DIAMONDS;
      case "ka", "♦️a" -> ACE_OF_DIAMONDS;
      case "x7", "♣️7" -> SEVEN_OF_CLUBS;
      case "x8", "♣️8" -> EIGHT_OF_CLUBS;
      case "x9", "♣️9" -> NINE_OF_CLUBS;
      case "x10", "♣️10" -> TEN_OF_CLUBS;
      case "xb", "♣️b" -> JACK_OF_CLUBS;
      case "xd", "♣️d" -> QUEEN_OF_CLUBS;
      case "xk", "♣️k" -> KING_OF_CLUBS;
      case "xa", "♣️a" -> ACE_OF_CLUBS;
      case "p7", "♠️7" -> SEVEN_OF_SPADES;
      case "p8", "♠️8" -> EIGHT_OF_SPADES;
      case "p9", "♠️9" -> NINE_OF_SPADES;
      case "p10", "♠️10" -> TEN_OF_SPADES;
      case "pb", "♠️b" -> JACK_OF_SPADES;
      case "pd", "♠️d" -> QUEEN_OF_SPADES;
      case "pk", "♠️k" -> KING_OF_SPADES;
      case "pa", "♠️a" -> ACE_OF_SPADES;
      default -> throw new IllegalArgumentException("Unknown Mnemonic");
    };
  }

  @Override
  public String toString() {
    return this.color.toString()+this.type.toString();
  }

}
