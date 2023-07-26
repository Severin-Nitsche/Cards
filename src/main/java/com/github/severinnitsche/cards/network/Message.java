package com.github.severinnitsche.cards.network;

public enum Message {
  ;
  public static final int CLIENT_ID = 0x42;
  public static final int CLIENT_PLAY = 0x43;
  public static final int CLIENT_TAKE = 0x44;
  public static final int CLIENT_FINISH = 0x45;
  public static final int CLIENT_WISH = 0x46;

  public static final int SERVER_DEAL = 0x69;
  public static final int SERVER_DRAW = 0x6a;
  public static final int SERVER_WISH = 0x6b;
  public static final int SERVER_STACK = 0x6c;
  public static final int SERVER_CARDS = 0x6d;
  public static final int SERVER_PLAYER = 0x6e;
  public static final int SERVER_TIME = 0x6f;
  public static final int SERVER_PLAY = 0x70;
  // public static final int SERVER_TAKE = 0x71;
  public static final int SERVER_ROUND = 0x72;
  public static final int SERVER_RESULT = 0x73;
  public static final int SERVER_YEET = 0x74;

  public static final int DELIMITER = 0x01;
  public static final int TERMINATOR = 0x00;
}
