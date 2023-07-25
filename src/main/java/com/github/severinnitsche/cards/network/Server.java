package com.github.severinnitsche.cards.network;

import com.github.severinnitsche.cards.core.card.Card;
import com.github.severinnitsche.cards.core.controller.Controller;

import javax.net.ServerSocketFactory;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * This class is used to host games.
 */
public class Server implements Closeable {

  private Controller controller;
  private ServerSocket listener;

  private final int players;

  private String[] identifiers;
  private Socket[] player;

  public Server(int rounds, int players, long seed, int port) throws IOException {
    if (seed == 0) {
      this.controller = new Controller(rounds, players, new Random());
    } else {
      this.controller = new Controller(rounds, players, new Random(seed));
    }
    this.listener = ServerSocketFactory.getDefault().createServerSocket(port);
    this.identifiers = new String[players];
    this.player = new Socket[players];
    this.players = players;
  }

  /**
   * This method waits for all players to connect.
   * This is a blocking operation.
   * @return whether the connections were successful
   */
  public boolean start() throws IOException {
    for (int i = 0; i < players; i++) {
      player[i] = listener.accept();
      StringBuilder idBuilder = new StringBuilder();
      int c1 = player[i].getInputStream().read();
      int c2 = player[i].getInputStream().read();
      while (c1 != 0 && c2 != 0 && c1 != -1) {
        idBuilder.append((char) (c1 << 8 | c2));
        c1 = player[i].getInputStream().read();
        c2 = player[i].getInputStream().read();
      }
      identifiers[i] = idBuilder.toString();
      if (c1 == -1) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method manages the full game
   */
  public void play() throws IOException {
    while (!controller.hasGameTerminated()) {
      nextRound();
      for (int i = 0; i < players; i++) {
        var out = player[i].getOutputStream();
        out.write(Message.SERVER_RESULT.code);
        out.write(controller.scoreOf(0));
        for (int j = 1; j < players; j++) {
          out.write(Message.DELIMITER);
          out.write(controller.scoreOf(j));
        }
        out.write(Message.TERMINATOR);
      }
    }
  }

  /**
   * This method manages the next round
   */
  private void nextRound() throws IOException {
    controller.nextRound();
    for (int i = 0; i < players; i++) {
      var out = player[i].getOutputStream();
      out.write(Message.SERVER_ROUND.code);
      out.write(controller.round());
      out.write(controller.rounds());
    }
    for (int i = 0; i < players; i++) {
      var out = player[i].getOutputStream();
      for (Card card : controller.handOf(i)) {
        out.write(Message.SERVER_DEAL.code);
        out.write(NetworkCardUtility.code(card.color));
        out.write(NetworkCardUtility.code(card.type));
      }
    }
    nextTurn();
  }

  /**
   * This method manages the next turn
   */
  private void nextTurn() {

  }

  @Override
  public void close() throws IOException {
    listener.close();
  }
}
