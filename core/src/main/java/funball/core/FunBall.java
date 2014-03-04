package funball.core;

import funball.core.Screen.HomeScreen;
import playn.core.Game;
import playn.core.util.Clock;
import tripleplay.game.ScreenStack;

public class FunBall extends Game.Default {
    private ScreenStack ss = new ScreenStack();
    private Clock.Source clock  = new Clock.Source(33);

  public FunBall() {
    super(33); // call update every 33ms (30 times per second)
  }

  @Override
  public void init() {
    // create and add background image layer
    ss.push(new HomeScreen(ss));

  }

  @Override
  public void update(int delta) {
      ss.update(delta);
  }

  @Override
  public void paint(float alpha) {
    // the background automatically paints itself, so no need to do anything here!
    clock.paint(alpha);
    ss.paint(clock);
  }
}
