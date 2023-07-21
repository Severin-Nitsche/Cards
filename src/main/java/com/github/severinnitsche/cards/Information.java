package com.github.severinnitsche.cards;

/**
 * The information a player receives at every turn
 * @param stack the stack of cards played
 * @param hand the player's hand
 * @param numberOfCards every player's number of cards
 * @param playerNumber the index of the current player
 */
public record Information(Card[] stack, Hand hand, int[] numberOfCards, int playerNumber) {
}
