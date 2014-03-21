package funball.core.Screen;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import playn.core.Font;
import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by Chagrapong on 26/2/2557.
 */
public class HomeScreen extends UIScreen {
    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,24);
    private final ScreenStack ss;
    private Root root;
    private Image start,quit,background;
    public HomeScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        background = assets().getImage("images/HomeScreen/bg.png");
        ImageLayer backgroundLayer = graphics().createImageLayer(background);
        layer.add(backgroundLayer);
        backgroundLayer.setTranslation(0f,0f);

        start = assets().getImage("images/HomeScreen/start.png");
        ImageLayer startLayer = graphics().createImageLayer(start);
        layer.add(startLayer);
        startLayer.setTranslation(100f,150f);
//
//        quit = assets().getImage("images/quit.png");
//        ImageLayer quitLayer = graphics().createImageLayer(quit);
//        layer.add(quitLayer);
//        quitLayer.setTranslation(100f,220f);

        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.push(new GameScreen(ss));
            }
        });

    }
}
