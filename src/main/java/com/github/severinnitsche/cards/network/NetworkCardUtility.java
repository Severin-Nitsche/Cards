package com.github.severinnitsche.cards.network;

import com.github.severinnitsche.cards.core.card.Card;
import com.github.severinnitsche.cards.core.card.Color;
import com.github.severinnitsche.cards.core.card.Type;

/**
 * This class helps convert from hex to color/card and back
 */
public class NetworkCardUtility {

  /**
   * Get the color code
   * @param color the color
   * @return the corresponding code
   */
  public static int code(Color color) {
    return switch (color) {
      case DIAMONDS -> 0x31;
      case HEART -> 0x41;
      case CLUBS -> 0x15;
      case SPADES -> 0x92;
    };
  }

  /**
   * Get the color from its code
   * @param code the color code
   * @return the corresponding color
   */
  public static Color color(int code) {
    return switch (code) {
      case 0x31 -> Color.DIAMONDS;
      case 0x41 -> Color.HEART;
      case 0x15 -> Color.CLUBS;
      case 0x92 -> Color.SPADES;
      default -> throw new IllegalArgumentException("Unknown code: "+code);
    };
  }

  /**
   * Get the type code
   * @param type the type
   * @return the corresponding code
   */
  public static int code(Type type) {
    return switch (type) {
      case SEVEN -> 0x7;
      case EIGHT -> 0x8;
      case NINE -> 0x9;
      case TEN -> 0xa;
      case JACK -> 0xb;
      case QUEEN -> 0xc;
      case KING -> 0xd;
      case ACE -> 0xe;
    };
  }

  /**
   * Get the type from its code
   * @param code the type code
   * @return the corresponding type
   */
  public static Type type(int code) {
    return switch (code) {
      case 0x7 -> Type.SEVEN;
      case 0x8 -> Type.EIGHT;
      case 0x9 -> Type.NINE;
      case 0xa -> Type.TEN;
      case 0xb -> Type.JACK;
      case 0xc -> Type.QUEEN;
      case 0xd -> Type.KING;
      case 0xe -> Type.ACE;
      default -> throw new IllegalArgumentException("Unknown code: "+code);
    };
  }

  /**
   * Get the card from two codes
   * @param colorCode the color code of the card
   * @param typeCode the type code of the card
   * @return the corresponding card
   */
  public static Card card(int colorCode, int typeCode) {
    Color color = color(colorCode);
    Type type = type(typeCode);
    return switch (color) {
      case HEART -> switch (type) {
        case SEVEN -> Card.SEVEN_OF_HEARTS;
        case EIGHT -> Card.EIGHT_OF_HEARTS;
        case NINE -> Card.NINE_OF_HEARTS;
        case TEN -> Card.TEN_OF_HEARTS;
        case JACK -> Card.JACK_OF_HEARTS;
        case QUEEN -> Card.QUEEN_OF_HEARTS;
        case KING -> Card.KING_OF_HEARTS;
        case ACE -> Card.ACE_OF_HEARTS;
      };
      case DIAMONDS -> switch (type) {
        case SEVEN -> Card.SEVEN_OF_DIAMONDS;
        case EIGHT -> Card.EIGHT_OF_DIAMONDS;
        case NINE -> Card.NINE_OF_DIAMONDS;
        case TEN -> Card.TEN_OF_DIAMONDS;
        case JACK -> Card.JACK_OF_DIAMONDS;
        case QUEEN -> Card.QUEEN_OF_DIAMONDS;
        case KING -> Card.KING_OF_DIAMONDS;
        case ACE -> Card.ACE_OF_DIAMONDS;
      };
      case CLUBS -> switch (type) {
        case SEVEN -> Card.SEVEN_OF_CLUBS;
        case EIGHT -> Card.EIGHT_OF_CLUBS;
        case NINE -> Card.NINE_OF_CLUBS;
        case TEN -> Card.TEN_OF_CLUBS;
        case JACK -> Card.JACK_OF_CLUBS;
        case QUEEN -> Card.QUEEN_OF_CLUBS;
        case KING -> Card.KING_OF_CLUBS;
        case ACE -> Card.ACE_OF_CLUBS;
      };
      case SPADES -> switch (type) {
        case SEVEN -> Card.SEVEN_OF_SPADES;
        case EIGHT -> Card.EIGHT_OF_SPADES;
        case NINE -> Card.NINE_OF_SPADES;
        case TEN -> Card.TEN_OF_SPADES;
        case JACK -> Card.JACK_OF_SPADES;
        case QUEEN -> Card.QUEEN_OF_SPADES;
        case KING -> Card.KING_OF_SPADES;
        case ACE -> Card.ACE_OF_SPADES;
      };
    };
  }

}
