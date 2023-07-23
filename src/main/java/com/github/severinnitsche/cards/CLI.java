package com.github.severinnitsche.cards;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CLI {

  public static void main(String[] args) {
    int players = 2;
    int rounds = 1;
    Random random = new Random();
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-h", "--help" -> help();
        case "-s", "--spieler" -> players = Integer.parseInt(args[++i]);
        case "-r", "--runden" -> rounds = Integer.parseInt(args[++i]);
        case "--seed" -> random = new Random(Long.parseLong(args[++i]));
      }
    }
    play(players, rounds, random);
  }

  public static void play(int players, int rounds, Random random) {
    Controller controller = new Controller(rounds, players, random);
    controller.nextRound();
    boolean status = false;
    Scanner input = new Scanner(System.in);
    while (!controller.hasGameTerminated()) {
      var info = controller.turnInfo();
      System.out.printf("""
          Runde % 3d/% 3d                                         Du bist Spieler % 3d
          
          Spielkarten: %s - Spielstand: %s
          
          Stapel: %s %s
          
          [Entwicklerinformationen: %s]
          
          Spielkarten: %s
          Meldung: %b
          Eingabe:\s""",
          controller.round(), controller.rounds(), info.playerNumber() + 1,
          Arrays.toString(info.numberOfCards()), Arrays.toString(controller.scores()),
          info.stack().peek(), info.wish() == null ? "" : "Wunsch: "+info.wish(),
          info,
          info.hand(),
          status);
      String userInput = input.nextLine();
      switch (userInput.charAt(0)) {
        case 's' -> {
          try {
            Card card = Card.fromMnemonic(userInput.split(" ")[1]);
            status = controller.apply(new Action.Play(card));
          } catch (IllegalArgumentException err) {
            System.err.println(err.getLocalizedMessage());
            status = false;
          }
        }
        case 'z' -> {
          if (info.drawCards() == 0) {
            status = controller.apply(new Action.Draw(1));
          } else {
            status = controller.apply(new Action.Draw(info.drawCards()));
          }
        }
        case 'e' -> {
          status = controller.apply(new Action.PlayRemaining());
        }
        case 'w' -> {
          try {
            status = controller.apply(new Action.Wish(switch (userInput.split(" ")[1].toLowerCase()) {
              case "k", "♦️" -> Color.DIAMONDS;
              case "h", "♥️" -> Color.HEART;
              case "p", "♠️" -> Color.SPADES;
              case "x", "♣️" -> Color.CLUBS;
              default -> throw new IllegalStateException(
                  "Unexpected value: " + userInput.split(" ")[1].toLowerCase());
            }));
          } catch (IllegalStateException err) {
            System.err.println(err.getLocalizedMessage());
            status = false;
          }
        }
        default -> {
          status = false;
        }
      }
      if (controller.hasRoundTerminated()) {
        controller.nextRound();
      }
    }
    System.out.println(Arrays.toString(controller.scores()));
  }

  /**
   * Display a help screen and exit 👩🏽‍❤️‍👨🏼
   */
  public static void help() {
    System.out.println("""
        Mau Mau
        Copyright (C) 2023 Severin Leonard Christian Nitsche
        
          -h, --help     Zeigt diesen Hilfetext an
          -s, --spieler  (Standard: 2) Die Anzahl der Spieler
          -r, --runden   (Standard: 1) Die Anzahl der Runden
          --seed         Der seed für den RNG
          
          Das Spiel ist eine lokale Version von Mau Mau.
          Sie können Eingaben wie folgt vornehmen:
           s [Karte] - Spielt die angegebene Karte aus
           z - Zieht die notwendige Anzahl an Karten
           e - Legt alle Buben ab
           w [Farbe] - Wünscht die angegebene Farbe
          Dabei werden Karten wie folgt notiert:
           [Karte] = [Farbe][Typ]
          Mit den Typen:
            7 = Sieben    B = Bube
            8 = Acht      D = Dame
            9 = Neun      K = König
           10 = Zehn      A = Ass
          Und den Farben:
           H = Herz       X = Kreuz
           K = Karo       P = Pik
          Alternativ die Emojis: ♥️ ♠️ ♣️ ♦️
        """);
    System.exit(69);
  }

}
