package com.github.severinnitsche.cards.core.controller;

import com.github.severinnitsche.cards.core.card.Color;
import com.github.severinnitsche.cards.core.card.Hand;
import com.github.severinnitsche.cards.core.card.Stack;

/**
 * The information a player receives at every turn
 * @param drawCards the number of cards to draw or 0
 * @param wish the color to play or null
 * @param stack the stack of cards played
 * @param hand the player's hand
 * @param numberOfCards every player's number of cards
 * @param playerNumber the index of the current player
 * @param mustWish whether the next action must be a wish
 */
public record Information(int drawCards, Color wish, Stack stack, Hand hand,
                          int[] numberOfCards, int playerNumber, boolean mustWish) {
}
