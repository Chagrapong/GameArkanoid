package funball.core.Screen;

import playn.core.Image;
import playn.core.ImageLayer;
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

    public HomeScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        Image bgImage = assets().getImage("images/bg.png");
        /*ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);*/

        root = iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(), layer);
        root.addStyles(Style.BACKGROUND.is(Background.image(bgImage)));
        root.setSize(width(), height());
        root.add(new Label("Fun Ball").addStyles(Style.FONT.is(HomeScreen.TITLE_FONT)));

        root.add(new Button("Start Game").onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                ss.push(new GameScreen(ss));
            }
        }));
    }
}
