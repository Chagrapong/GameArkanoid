package funball.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import funball.core.FunBall;

public class FunBallActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new FunBall());
  }
}
