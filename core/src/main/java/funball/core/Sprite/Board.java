package funball.core.Sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import tripleplay.game.UIScreen;
import funball.core.Screen.GameScreen;

/**
 * Created by Chagrapong on 4/3/2557.
 */
public class Board extends UIScreen {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;

   /* public enum State{
        IDLE,
    };

    private State state = State.IDLE;
    private int e = 0;
    private int offset = 0;*/
    private Body body;


    public Board(final World world,final float x,final  float y){
        this.sprite = SpriteLoader.getSprite("images/board.json");

        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x, y);

                body = initPhysicsBody(world,
                        GameScreen.M_PER_PIXEL* x,
                        GameScreen.M_PER_PIXEL * y);
                hasLoaded = true;

            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });

    }

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(56 * GameScreen.M_PER_PIXEL ,
                sprite.layer().height()* GameScreen.M_PER_PIXEL /2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;

        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }

    public Layer layer(){
        return sprite.layer();
    }

    @Override
    public void update(int delta){
        PlayN.keyboard().setListener(new Keyboard.Adapter(){
            @Override
            public void onKeyDown(Keyboard.Event event) {
                if (event.key()== playn.core.Key.RIGHT){
                    body.applyForce(new Vec2(50f,0f),body.getPosition());
                }
                else if (event.key()== playn.core.Key.LEFT){
                    body.applyForce(new Vec2(-50f,0f),body.getPosition());
                }
            }
        });

    }

    @Override
    public void paint(Clock clock) {
        if (!hasLoaded)return;
        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL),
                body.getPosition().y / GameScreen.M_PER_PIXEL);
        //sprite.layer().setRotation(body.getAngle());
    }
}
