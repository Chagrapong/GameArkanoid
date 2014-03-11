package funball.core.Sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
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

   public enum State{
        IDLE,RUNR,RUNL
    };

    private State state = State.IDLE;
    private int e = 0;
    private int offset = 0;
    private Body body;


    public Board(final World world,final float x,final  float y){
        this.sprite = SpriteLoader.getSprite("images/Board.json");

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

        sprite.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                state = State.IDLE;
                spriteIndex = -1;
                e = 0;
            }
        });

        PlayN.keyboard().setListener(new Keyboard.Listener() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                if (event.key() == Key.RIGHT) {
                    state = State.RUNR;
                    spriteIndex = 1;
                    e = 0;

                }
                if (event.key() == Key.LEFT) {
                    state = State.RUNL;
                    spriteIndex = 1;
                    e = 0;

                }
            }

            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {

            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                if (event.key() == Key.LEFT) {
                    state = State.IDLE;
                    spriteIndex = 1;
                    e = 0;
                }
                if (event.key() == Key.RIGHT) {
                    state = State.IDLE;
                    spriteIndex = 1;
                    e = 0;
                }
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
                sprite.layer().height()* GameScreen.M_PER_PIXEL/2 );
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 80f;

        body.createFixture(fixtureDef);
        //body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }

    public Layer layer(){
        return sprite.layer();
    }

    @Override
    public void update(int delta){
        if (!hasLoaded) return;
        e += delta;
        if (e > 150){
            switch (state) {
                case IDLE:offset = 0;
                    break;
                case RUNR:offset = 0;
                    break;
                case RUNL:offset = 0;
                    break;
            }
            spriteIndex = offset + ((spriteIndex +1)%4);
            sprite.setSprite(spriteIndex);
            e = 0;
            if (state == State.RUNL){
                body.applyForce(new Vec2(-800f,0f),body.getPosition());
            }
            if (state == State.RUNR){
                body.applyForce(new Vec2(800f,0f),body.getPosition());
            }
        }
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
