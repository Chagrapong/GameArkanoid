package funball.core.Screen;

import funball.core.Sprite.Ball;
import funball.core.Sprite.Block;
import funball.core.Sprite.Board;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import funball.core.Debug.DebugDrawBox2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chagrapong on 26/2/2557.
 */
public class GameScreen extends UIScreen{
    private ScreenStack ss;
    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    public static float M_PER_PIXEL = 1 / 26.666667f;//size of world
    private static int width = 24;//640px in physic unit (meter)
    private static int height = 18;//480px in physic unit (meter)
    private Board board;
    private Ball ball;
    private Block block;
    private List<Block> bb = new ArrayList<Block>(10);
    private float sum = 40f;

    private Body ground,up,left,right;
    private Image bgGame,back,windown,boll;
    private ImageLayer bgGameLayer,backLayer,windownLayer,bollLayer;
    private float x_1 = 320f,y_1 = 428f;

    public GameScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        bgGame = assets().getImage("images/GameScreen/Backgrounds.png");
        bgGameLayer = graphics().createImageLayer(bgGame);
        layer.add(bgGameLayer);
        bgGameLayer.setTranslation(0f,0f);

        windown = assets().getImage("images/GameScreen/Blocksbackgrounds.png");
        windownLayer = graphics().createImageLayer(windown);
        layer.add(windownLayer);
        windownLayer.setTranslation(0f,0f);

        back = assets().getImage("images/GameScreen/back.png");
        backLayer = graphics().createImageLayer(back);
        layer.add(backLayer);
        backLayer.setTranslation(0f,0f);

        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(GameScreen.this);
            }
        });

        Vec2 gravity = new Vec2(0.0f, 0f);
        world = new World(gravity, true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        if (showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int) (width / GameScreen.M_PER_PIXEL),
                    (int) (height / GameScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,1f / GameScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }

        ground = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsEdge(new Vec2(0f, 17.5f),new Vec2(24f, 17.5f));
        ground.createFixture(groundShape, 0.0f);

        up = world.createBody(new BodyDef());
        PolygonShape upShape = new PolygonShape();
        upShape.setAsEdge(new Vec2(0f, 0.5f),new Vec2(24f, 0.5f));
        up.createFixture(upShape, 0.0f);

        left = world.createBody(new BodyDef());
        PolygonShape leftShape = new PolygonShape();
        leftShape.setAsEdge(new Vec2(0.77f, 0f),new Vec2(0.77f, 18f));
        left.createFixture(leftShape, 0.0f);

        right = world.createBody(new BodyDef());
        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsEdge(new Vec2(23.23f, 0f),new Vec2(23.23f, 18f));
        right.createFixture(rightShape, 0.0f);

        board = new Board(world,320f,454f);
        layer.add(board.layer());

        ball = new Ball(world,x_1,y_1);
        layer.add(ball.layer());

        block = new Block(world,200f,200f);
        bb.add(block);
        layer.add(block.layer());


        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getBody() == ground) {
                    System.out.println("ลูกบอล");
                }
                else if (contact.getFixtureB().getBody() == ground){
                    System.out.println("A");
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        board.update(delta);
        ball.update(delta);
        block.update(delta);
        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        board.paint(clock);
        ball.paint(clock);
        block.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
