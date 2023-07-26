package com.github.severinnitsche.cards.network.utility;

import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.card.Card;
import com.github.severinnitsche.cards.core.card.Color;
import com.github.severinnitsche.cards.core.card.Type;
import com.github.severinnitsche.cards.network.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * This class eases the network communication by providing convenience Methods to send/receive messages.
 */
public class NetworkUtility {

  private BufferedInputStream in;
  private OutputStream out;

  public NetworkUtility(InputStream in, OutputStream out) {
    this.in = new BufferedInputStream(in);
    this.out = out;
  }

  /**
   * Peeks the stream
   * @return a peek at the next byte
   * @throws IOException
   */
  public int peek() throws IOException {
    in.mark(2);
    int peek = in.read();
    in.reset();
    return peek;
  }

  /**
   * Send a message with the specified type
   * @param type the message type (byte)
   */
  public void send(int type) throws IOException {
    out.write(type);
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, String value) throws IOException {
    out.write(type);
    for (int i = 0; i < value.length(); i++) {
      out.write(value.charAt(i));
      out.write(value.charAt(i) >>> 8);
    }
    out.write(0);
    out.write(0);
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, Color value) throws IOException {
    out.write(type);
    if (value == null) {
      out.write(0x00);
    } else {
      out.write(NetworkCardUtility.code(value));
    }
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, Type value) throws IOException {
    out.write(type);
    out.write(NetworkCardUtility.code(value));
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, Card value) throws IOException {
    out.write(type);
    out.write(NetworkCardUtility.code(value.color));
    out.write(NetworkCardUtility.code(value.type));
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, Iterable<Card> value) throws IOException {
    out.write(type);
    var iterator = value.iterator();

    while (iterator.hasNext()) {
      var next = iterator.next();
      out.write(NetworkCardUtility.code(next.color));
      out.write(NetworkCardUtility.code(next.type));
      if (iterator.hasNext()) {
        out.write(Message.DELIMITER);
      }
    }
    out.write(Message.TERMINATOR);
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, long value) throws IOException {
    out.write(type);
    out.write((int) (value >>> 56));
    out.write((int) (value >>> 48));
    out.write((int) (value >>> 40));
    out.write((int) (value >>> 32));
    out.write((int) (value >>> 24));
    out.write((int) (value >>> 16));
    out.write((int) (value >>> 8));
    out.write((int) (value));
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, int value) throws IOException {
    out.write(type);
    out.write(value);
  }

  /**
   * Send a message with the specified type and values a, b
   * @param type the message type (byte)
   * @param a the first value
   * @param b the second value
   */
  public void send(int type, int a, int b) throws IOException {
    out.write(type);
    out.write(a);
    out.write(b);
  }

  /**
   * Send a message with the specified type and value
   * @param type the message type (byte)
   * @param value the message value
   */
  public void send(int type, int[] value) throws IOException {
    out.write(type);
    out.write(value[0]);
    for (int i = 1; i < value.length; i++) {
      out.write(Message.DELIMITER);
      out.write(value[i]);
    }
    out.write(Message.TERMINATOR);
  }

  /**
   * Send an action
   */
  public void sendClientAction(Action action) throws IOException {
    if (action instanceof Action.Wish wish) {
      send(Message.CLIENT_WISH, wish.color());
    } else if (action instanceof Action.Play play) {
      send(Message.CLIENT_PLAY, play.card());
    } else if (action instanceof Action.Draw draw) {
      send(Message.CLIENT_TAKE, draw.number());
    } else if (action instanceof Action.PlayRemaining) {
      send(Message.CLIENT_FINISH);
    }
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public String receiveString(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    return receiveString();
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public Color receiveColor(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    return NetworkCardUtility.color(in.read());
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public Type receiveType(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    return NetworkCardUtility.type(in.read());
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public Card receiveCard(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    return NetworkCardUtility.card(in.read(), in.read());
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public Iterable<Card> receiveCards(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    ArrayList<Card> cards = new ArrayList<>();
    do {
      cards.add(NetworkCardUtility.card(in.read(), in.read()));
    } while(in.read() == Message.DELIMITER);
    return cards;
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public long receiveLong(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    return ((long)in.read() << 56) | ((long)in.read() << 48) | ((long)in.read() << 40)
        | ((long)in.read() << 32) | ((long)in.read() << 24) | ((long)in.read() << 16)
        | ((long)in.read() << 8) | ((long)in.read());
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public int receiveInt(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    return in.read();
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public int[] receiveIntX2(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    return new int[] {in.read(), in.read()};
  }

  /**
   * Receive a message with the specified type
   * @param type the message type (byte)
   */
  public int[] receiveInts(int type) throws IOException {
    int rt = in.read();
    if (rt != type) {
      throw new IllegalStateException("Expected "+type+" but got "+rt);
    }
    ArrayList<Integer> ints = new ArrayList<>();
    do {
      ints.add(in.read());
    } while(in.read() == Message.DELIMITER);
    return ints.stream().mapToInt(Integer::intValue).toArray();
  }

  /**
   * Receive a message
   */
  public String receiveString() throws IOException {
    StringBuilder stringer = new StringBuilder();
    int c1 = in.read();
    int c2 = in.read();
    while (((c1 | c2) != 0) && c1 != -1) {
      stringer.append((char) (c1 << 8 | c2));
      c1 = in.read();
      c2 = in.read();
    }
    return stringer.toString();
  }

  /**
   * Receive a message
   */
  public Color receiveColor() throws IOException {
    return NetworkCardUtility.color(in.read());
  }

  /**
   * Receive a message
   */
  public Type receiveType() throws IOException {
    return NetworkCardUtility.type(in.read());
  }

  /**
   * Receive a message
   */
  public Card receiveCard() throws IOException {
    return NetworkCardUtility.card(in.read(), in.read());
  }

  /**
   * Receive a message
   */
  public Iterable<Card> receiveCards() throws IOException {
    ArrayList<Card> cards = new ArrayList<>();
    do {
      cards.add(NetworkCardUtility.card(in.read(), in.read()));
    } while(in.read() == Message.DELIMITER);
    return cards;
  }

  /**
   * Receive a message
   */
  public long receiveLong() throws IOException {
    return ((long)in.read() << 56) | ((long)in.read() << 48) | ((long)in.read() << 40)
        | ((long)in.read() << 32) | ((long)in.read() << 24) | ((long)in.read() << 16)
        | ((long)in.read() << 8) | ((long)in.read());
  }

  /**
   * Receive a message
   */
  public int receiveInt() throws IOException {
    return in.read();
  }

  /**
   * Receive a message
   */
  public int[] receiveIntX2() throws IOException {
    return new int[] {in.read(), in.read()};
  }

  /**
   * Receive a message
   */
  public int[] receiveInts() throws IOException {
    ArrayList<Integer> ints = new ArrayList<>();
    do {
      ints.add(in.read());
    } while(in.read() == Message.DELIMITER);
    return ints.stream().mapToInt(Integer::intValue).toArray();
  }

  /**
   * Receive an action
   */
  public Action receiveClientAction() throws IOException {
    var read = in.read();
    return switch (read) {
      case Message.CLIENT_PLAY -> new Action.Play(receiveCard());
      case Message.CLIENT_TAKE -> new Action.Draw(receiveInt());
      case Message.CLIENT_WISH -> new Action.Wish(receiveColor());
      case Message.CLIENT_FINISH -> new Action.PlayRemaining();
      default -> throw new IllegalStateException("Expected action but got: "+read);
    };
  }

  /**
   * Receive an action
   */
  public Action receiveServerAction() throws IOException {
    var read = in.read();
    return switch (read) {
      case Message.SERVER_PLAY -> new Action.Play(receiveCard());
      case Message.SERVER_DRAW -> new Action.Draw(receiveInt());
      case Message.SERVER_WISH -> new Action.Wish(receiveColor());
      case Message.SERVER_FINISH -> new Action.PlayRemaining();
      default -> throw new IllegalStateException("Expected action but got: "+read);
    };
  }
}
