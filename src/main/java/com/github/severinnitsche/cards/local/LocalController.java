package com.github.severinnitsche.cards.local;

import com.github.severinnitsche.cards.core.action.Action;
import com.github.severinnitsche.cards.core.card.Hand;
import com.github.severinnitsche.cards.core.controller.Controller;
import com.github.severinnitsche.cards.core.controller.Game;
import com.github.severinnitsche.cards.core.controller.Information;
import com.github.severinnitsche.cards.core.controller.Round;

import java.util.Random;

public class LocalController implements Controller {

  private Game game;
  private Round current;
  private Random source;
  private boolean applied;

  public LocalController(int rounds, int players, Random source) {
    this.game = new Game(rounds, players);
    this.source = source;
  }

  @Override
  public int preferred() {
    return -1;
  }

  @Override
  public void nextRound() {
    if (current == null) {
      current = new Round(source, game.players, game.round() % game.players);
      return;
    }
    if (hasRoundTerminated() && game.round() <= game.rounds) {
      applied = false;
      current = new Round(source, game.players, game.round() % game.players);
    }
  }

  @Override
  public boolean hasRoundTerminated() {
    if (current == null) {
      return true;
    }
    if (current.hasTerminated()) {
      if (!applied) {
        game.setScores(current.scores());
        applied = true;
      }
      return true;
    }
    return false;
  }

  @Override
  public int round() {
    return game.round();
  }

  @Override
  public int rounds() {
    return game.rounds;
  }

  @Override
  public boolean hasGameTerminated() {
    return game.round() > game.rounds;
  }

  @Override
  public Hand handOf(int player) {
    return current.handOf(player);
  }

  @Override
  public int scoreOf(int player) {
    hasRoundTerminated(); // update scores
    return game.scoreOf(player);
  }

  @Override
  public int[] scores() {
    int[] scores = new int[game.players];
    for (int i = 0; i < game.players; i++) {
      scores[i] = game.scoreOf(i);
    }
    return scores;
  }

  @Override
  public boolean apply(Action action) {
    if (current == null || current.hasTerminated() || hasGameTerminated()) {
      throw new IllegalStateException("Cannot apply an action without a running round!");
    }
    return current.act(action);
  }

  @Override
  public Action fetchAndApply() {
    throw new UnsupportedOperationException("We control everything");
  }

  @Override
  public Information turnInfo() {
    if (current == null || current.hasTerminated() || hasGameTerminated()) {
      throw new IllegalStateException("Cannot fetch turn information without a running round!");
    }
    return current.info();
  }

}
