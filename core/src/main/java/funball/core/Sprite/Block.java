package funball.core.Sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import tripleplay.game.UIScreen;
import funball.core.Screen.GameScreen;
import tripleplay.game.UIScreen;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chagrapong on 18/3/2557.
 */
public class Block extends UIScreen{
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private List<Block> bb = new ArrayList<Block>();

    public enum State{
        IDLE,DIE
    };
    private State state = State.IDLE;
    private int e = 0;
    private int offset = 0;
    private Body body;


    public Block(final World world,final float x,final  float y){
        this.sprite = SpriteLoader.getSprite("images/Block/Block.json");

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

//        sprite.layer().addListener(new Pointer.Adapter(){
//            @Override
//            public void onPointerEnd(Pointer.Event event) {
//
//            }
//        });
    }



    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(42 * GameScreen.M_PER_PIXEL/2 ,
                sprite.layer().height()* GameScreen.M_PER_PIXEL/2 );
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }

    public Layer layer(){
       return sprite.layer();
    }

    @Override
    public void update(int delta) {
        if (!hasLoaded) return;
        e += delta;
        if (e > 150){
            switch (state) {
                case IDLE:
                    offset = 0;
                    break;
                case DIE:
                    offset = 5;
                    break;
            }
            spriteIndex = offset + ((spriteIndex +1)%1);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }

    @Override
    public void paint(Clock clock) {
        if (!hasLoaded)return;
        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL),
                body.getPosition().y / GameScreen.M_PER_PIXEL);
    }
}
