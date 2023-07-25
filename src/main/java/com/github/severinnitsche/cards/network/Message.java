package com.github.severinnitsche.cards.network;

public enum Message {
  CLIENT_ID(false, 0x42), SERVER_DEAL(false, 0x69),
  SERVER_DRAW(true, 0x6a), SERVER_WISH(true, 0x6b),
  SERVER_STACK(true, 0x6c), SERVER_CARDS(true, 0x6c),
  SERVER_PLAYER(true, 0x6e), SERVER_TIME(true, 0x6f),
  CLIENT_PLAY(false, 0x43), CLIENT_TAKE(false, 0x44),
  CLIENT_FINISH(false, 0x45), CLIENT_WISH(false, 0x46),
  SERVER_PLAY(true, 0x70), SERVER_TAKE(true, 0x71),
  SERVER_ROUND(true, 0x72), SERVER_RESULT(true, 0x73),
  SERVER_YEET(true, 0x74);

  public static final int DELIMITER = 0x1;
  public static final int TERMINATOR = 0x0;

  public final boolean broadcast;
  public final int code;

  Message(boolean broadcast, int code) {
    this.broadcast = broadcast;
    this.code = code;
  }
}
