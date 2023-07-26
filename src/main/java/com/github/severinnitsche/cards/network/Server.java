package com.github.severinnitsche.cards.network;

import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.controller.Controller;
import com.github.severinnitsche.cards.core.controller.Information;
import com.github.severinnitsche.cards.local.LocalController;
import com.github.severinnitsche.cards.network.utility.NetworkUtility;

import javax.net.ServerSocketFactory;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Random;

/**
 * This class is used to host games.
 */
public class Server implements Closeable {

  private Controller controller;
  private ServerSocket listener;

  private final int players;

  private String[] identifiers;
  private NetworkUtility[] player;

  public Server(int rounds, int players, long seed, int port) throws IOException {
    if (seed == 0) {
      this.controller = new LocalController(rounds, players, new Random());
    } else {
      this.controller = new LocalController(rounds, players, new Random(seed));
    }
    this.listener = ServerSocketFactory.getDefault().createServerSocket(port);
    this.identifiers = new String[players];
    this.player = new NetworkUtility[players];
    this.players = players;
  }

  /**
   * This method waits for all players to connect.
   * This is a blocking operation.
   */
  public void start() throws IOException {
    for (int i = 0; i < players; i++) {
      var socket = listener.accept();
      player[i] = new NetworkUtility(socket.getInputStream(), socket.getOutputStream());
      identifiers[i] = player[i].receiveString(Message.CLIENT_ID);
      System.out.println(identifiers[i]+" joined the game!");
      player[i].send(i);
    }
  }

  /**
   * This method manages the full game
   */
  public void play() throws IOException {
    while (!controller.hasGameTerminated()) {
      nextRound();
      System.out.println(Arrays.toString(controller.scores()));
      for (int i = 0; i < players; i++) {
        player[i].send(Message.SERVER_RESULT, controller.scores());
      }
    }
  }

  /**
   * This method manages the next round
   */
  private void nextRound() throws IOException {
    controller.nextRound();
    for (int i = 0; i < players; i++) {
      player[i].send(Message.SERVER_ROUND, controller.round(), controller.rounds());
    }
    for (int i = 0; i < players; i++) {
      player[i].send(Message.SERVER_DEAL, controller.handOf(i));
    }
    while (!controller.hasRoundTerminated() && !controller.hasGameTerminated()) {
      nextTurn();
    }
  }

  /**
   * This method manages the next turn
   */
  private void nextTurn() throws IOException {
    Information info = controller.turnInfo();
    for (int i = 0; i < players; i++) {
      player[i].send(Message.SERVER_DRAW, info.drawCards());
      player[i].send(Message.SERVER_WISH, info.wish());
      if (info.stack().cards() == 1) {
        player[i].send(Message.SERVER_STACK, info.stack().peek());
      }
      player[i].send(Message.SERVER_CARDS, info.numberOfCards());
      player[i].send(Message.SERVER_PLAYER, info.playerNumber());
      player[i].send(Message.SERVER_TIME, 0L);
    }
    Action action = player[info.playerNumber()].receiveClientAction();
    boolean success = controller.apply(action);
    if (!success) {
      for (int i = 0; i < players; i++) {
        player[i].send(Message.SERVER_YEET, info.playerNumber());
      }
      // TODO: Yeet the player of the game internally
    } else {
      if (action instanceof Action.Play play) {
        for (int i = 0; i < players; i++) {
          player[i].send(Message.SERVER_PLAY, play.card());
        }
      } else if (action instanceof Action.Draw draw) {
        for (int i = 0; i < players; i++) {
          player[i].send(Message.SERVER_DRAW, draw.number());
        }
        player[info.playerNumber()].send(Message.SERVER_DEAL, info.hand());
      } else if (action instanceof Action.PlayRemaining) {
        for (int i = 0; i < players; i++) {
          player[i].send(Message.SERVER_FINISH);
        }
      } else if (action instanceof Action.Wish wish) {
        for (int i = 0; i < players; i++) {
          player[i].send(Message.SERVER_WISH, wish.color());
        }
      }
    }
  }

  @Override
  public void close() throws IOException {
    listener.close();
  }
}
