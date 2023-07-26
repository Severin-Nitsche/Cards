package com.github.severinnitsche.cards.network.cli;

import com.github.severinnitsche.cards.cli.Adoptable;
import com.github.severinnitsche.cards.network.Server;

import java.io.IOException;

/**
 * CLI-interface for the server
 */
@Adoptable
public class Host {

  public static void adopt(int rounds, int players, long seed, int port) throws IOException {
    try (Server server = new Server(rounds, players, seed, port)) {
      System.out.println("""
          Mau-Mau Server
          (C) 2023 Severin Leonard Christian Nitsche
          
          Waiting for connections...
          """);
      server.start();
      System.out.println("Starting Game!");
      server.play();
      System.out.println("Finished!");
    }
  }

}
