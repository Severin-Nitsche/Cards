package com.github.severinnitsche.cards.network;

import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.card.*;
import com.github.severinnitsche.cards.core.controller.Controller;
import com.github.severinnitsche.cards.core.controller.Information;
import com.github.severinnitsche.cards.network.utility.NetworkCardUtility;
import com.github.severinnitsche.cards.network.utility.NetworkUtility;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import static com.github.severinnitsche.cards.network.Message.*;

/**
 * This class is used to connect to a server
 */
public class Client implements Closeable, Controller {

  private Socket socket;

  private String identifier;
  private NetworkUtility connection;

  private int round;
  private int rounds;
  private Hand hand;
  private Stack stack;
  private int[] scores;
  private int player;
  private boolean wishInProgress;
  private boolean canWish;

  private boolean roundTerminus;

  public Client(String identifier, String host, int port) throws IOException {
    this.identifier = identifier;
    this.socket = new Socket(host, port);
    this.connection = new NetworkUtility(socket.getInputStream(), socket.getOutputStream());
  }

  public void id() throws IOException {
    System.out.println("Identifying as "+identifier);
    connection.send(CLIENT_ID, identifier);
    player = connection.receiveInt();
  }

  @Override
  public int preferred() {
    return player;
  }

  @Override
  public void nextRound() {
    try {
      if (scores != null && !hasRoundTerminated()) {
        return;
      }
      roundTerminus = false;
      int[] rnd = connection.receiveIntX2(SERVER_ROUND);
      this.round = rnd[0];
      this.rounds = rnd[1];
      this.hand = new Hand();
      this.stack = new Stack(32);
      wishInProgress = false;
      canWish = false;
      var cards = connection.receiveCards(SERVER_DEAL);
      for (Card card : cards) {
        hand.draw(card);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean hasRoundTerminated() {
    try {
      if (roundTerminus) {
        return true;
      }
      if (connection.peek() != SERVER_RESULT) {
        return false;
      }
      roundTerminus = true;
      scores = connection.receiveInts(SERVER_RESULT);
      return true;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int round() {
    return round;
  }

  @Override
  public int rounds() {
    return rounds;
  }

  @Override
  public boolean hasGameTerminated() {
    return roundTerminus && round == rounds;
  }

  @Override
  public Hand handOf(int player) {
    throw new UnsupportedOperationException("Cannot access generic hand as client");
  }

  @Override
  public int scoreOf(int player) {
    if (scores == null) {
      return 0;
    }
    return scores[player];
  }

  @Override
  public int[] scores() {
    return scores;
  }

  @Override
  public boolean apply(Action action) {
    try {
      connection.sendClientAction(action);
      if (connection.peek() == SERVER_YEET) {
        connection.receiveInt();
        return false;
      }
      var act = connection.receiveServerAction();
      if (act.equals(action)) {
        applyLocal(act);
        return true;
      }
      return false;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Action fetchAndApply() {
    try {
      var act = connection.receiveServerAction();
      applyLocal(act);
      return act;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void applyLocal(Action act) {
    canWish = false;
    if (act instanceof Action.Wish wish) {
      wishInProgress = true;
    } else if (act instanceof Action.Play play) {
      wishInProgress = wishInProgress && play.card().type == Type.JACK;
      canWish = !wishInProgress && play.card().type == Type.JACK;
      if (hand.holds(play.card())) {
        hand.play(play.card());
      }
      stack.add(play.card());
    } else if (act instanceof Action.PlayRemaining) {
      // Ignore, round ends
    } else if (act instanceof Action.Draw draw) {
      try {
        if (connection.peek() == SERVER_DEAL) {
          var cards = connection.receiveCards(SERVER_DEAL);
          for (var card : cards) {
            if (!hand.holds(card)) {
              hand.draw(card);
            }
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      // Ignore card numbers are updated next turn
    }
  }

  @Override
  public Information turnInfo() {
    try {
      int draw = connection.receiveInt(SERVER_DRAW);
      Color wish = connection.receiveColor(SERVER_WISH);
      if (connection.peek() == SERVER_STACK) {
        while (stack.cards() > 0) {
          stack.draw();
        }
        stack.add(connection.receiveCard(SERVER_STACK));
      }
      int[] cards = connection.receiveInts(SERVER_CARDS);
      int player = connection.receiveInt(SERVER_PLAYER);
      long time = connection.receiveLong(SERVER_TIME);
      if (player == this.player) {
        return new Information(draw, wish, stack, hand, cards, player, canWish);
      } else {
        return new Information(draw, wish, stack, null, cards, player, canWish);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws IOException {
    this.socket.close();
  }
}
