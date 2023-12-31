package com.github.severinnitsche.cards.cli;

import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.card.Card;
import com.github.severinnitsche.cards.core.card.Color;
import com.github.severinnitsche.cards.core.controller.Controller;

import java.util.Arrays;
import java.util.Scanner;

/**
 * This class is a cli for a game session
 */
public class Session {
  /**
   * Plays a session with the specified controller
   * @param controller the game controller
   */
  public static void session(Controller controller) {
    controller.nextRound();
    boolean status = false;
    String msg = "";
    Scanner input = new Scanner(System.in);
    int lastPlayer = -1;
    while (!controller.hasGameTerminated()) {
      var info = controller.turnInfo();
      if (controller.preferred() == -1 && lastPlayer != info.playerNumber()) {
        System.out.printf("""
                /==============================================================================\\
                ||Runde % 3d/% 3d                                            Du bist Spieler % 3d||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                               Drücke Enter ↲                               ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                ||                                                                            ||
                \\==============================================================================/""",
            controller.round(), controller.rounds(), info.playerNumber() + 1);
        input.nextLine();
      }
      lastPlayer = info.playerNumber();
      if (info.playerNumber() == controller.preferred() || controller.preferred() == -1) {
        System.out.printf("""
                Runde % 3d/% 3d                                                Du bist Spieler % 3d
                          
                Spielkarten: %s - Spielstand: %s
                          
                Stapel: %s %s
                          
                Ziehe % 3d Karten
                          
                Spielkarten: %s
                Meldung: %b
                          
                %s
                Farben: ♦️K, ♥️H, ♠️P, ♣️X            Werte: 7, 8, 9, 10,
                                                          [B]ube, [D]ame, [K]önig, [A]ss
                                                          
                Karte Spielen:                         Wünschen:
                  Syntax: s [Farbe][Typ]                 Syntax: w [Farbe]
                  Beispiel: s h10 - spielt die ♥️10      Beispiel: w k - wünscht sich ♦️
                          
                Karten Ziehen:                         Buben ablegen:
                  Syntax: z                              Syntax: e
                          
                          
                Eingabe:\s""",
            controller.round(), controller.rounds(), info.playerNumber() + 1,
            Arrays.toString(info.numberOfCards()), Arrays.toString(controller.scores()),
            info.stack().peek(), info.wish() == null ? "" : "Wunsch: " + info.wish(),
            info.drawCards(),
            info.hand(),
            status,
            msg);
        msg = "";
        String userInput = input.nextLine();
        try {
          switch (userInput.charAt(0)) {
            case 's' -> {
              try {
                Card card = Card.fromMnemonic(userInput.split(" ")[1]);
                status = controller.apply(new Action.Play(card));
              } catch (IllegalArgumentException err) {
                msg = err.getLocalizedMessage();
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
                msg = err.getLocalizedMessage();
                status = false;
              }
            }
            default -> {
              status = false;
            }
          }
        } catch (Exception err) {
          msg = err.getLocalizedMessage();
        }
      } else {
        var act = controller.fetchAndApply();
        System.out.println(act);
      }
      if (controller.hasRoundTerminated()) {
        if (!controller.hasGameTerminated()) {
          controller.nextRound();
        }
      }
    }
    System.out.println(Arrays.toString(controller.scores()));
  }
}