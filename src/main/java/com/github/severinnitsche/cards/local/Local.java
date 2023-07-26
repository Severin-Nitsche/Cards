package com.github.severinnitsche.cards.local;

import com.github.severinnitsche.cards.cli.Adoptable;
import com.github.severinnitsche.cards.cli.Session;
import com.github.severinnitsche.cards.core.controller.Controller;

import java.util.Random;

@Adoptable
public class Local {

  public static void adopt(int players, int rounds, long seed) {
    Random random;
    if (seed == 0) {
      random = new Random();
    } else {
      random = new Random(seed);
    }
    Controller controller = new LocalController(rounds, players, random);
    Session.session(controller);
  }

}
