package com.github.severinnitsche.cards.network.cli;

import com.github.severinnitsche.cards.cli.Adoptable;
import com.github.severinnitsche.cards.cli.Session;

import java.io.IOException;

/**
 * CLI-interface for the client
 */
@Adoptable
public class Client {
  public static void adopt(String id, String host, int port) throws IOException {
    try (var client = new com.github.severinnitsche.cards.network.Client(id, host, port)) {
      System.out.println("""
          Mau-Mau Client
          (C) 2023 Severin Leonard Christian Nitsche
          
          Connecting...
          """);
      client.id();
      System.out.println("Game starting...");
      Session.session(client);
      System.out.println("Finished!");
    }
  }
}
