package com.mygdx.game.Obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.PelletGame;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.GameScreenManager;
import com.mygdx.game.Tools.Reusables;

/**
 * Created by Lewis on 8/5/2017.
 */


public class Mirror extends Obstacle
{
    float[] points = new float[6];
    private float initRotation = 0;

    private static float SCALE = 1;

    //Texture
    private Sprite mirror;
    private static Texture mirrorTex = Reusables.mirrorTexture;

    private double hittime = 0;
    private float dt;

    private int hit = -1;
    public Body B2body;

    public String acceptedDirs;

    private Rectangle touchbox;
    boolean initialized = false;


    int resizecount = 0;
    float rotationMinus = 0;

    //Calculate triangle objects
    private FixtureDef fdef;
    private PolygonShape shape;
    private Polygon poly;

    public Mirror(GameScreenManager gsm, float rotation)
    {
        super(gsm);
        int plusr = 0;
        if(PlayScreen.levelsPassedSinceOpen >= 1)
        {
            plusr = -270;
        }

        create(rotation+plusr);
        defineBody();
        calculateTriangle(mirror.getX(), mirror.getY(), mirror.getRotation());
    }

    public Mirror(GameScreenManager gsm, int id, float rotation)
    {
        super(gsm);
        int plusr = 0;
        if(PlayScreen.levelsPassedSinceOpen >= 1)
        {
            plusr = -270;
        }

        create(rotation+plusr);
        makeId(id);

        defineBody();
        calculateTriangle(mirror.getX(), mirror.getY(), mirror.getRotation());
    }

    public void addToggle(int toggleId, String startingAction)
    {
        setToggleId(toggleId);

        if(startingAction.equals("disable"))
        {
            mirror.setColor(mirror.getColor().r, mirror.getColor().g, mirror.getColor().b, Toggle.fadealpha);
        }
    }
    
    private void create(float rotation)
    {

        if(rotation == 360 || rotation == 0 || rotation == -360)rotation = 180;
        else if(rotation == 90 || rotation == -270)rotation = 270;
        else if(rotation == 180 || rotation == -180)rotation = 0;
        else if(rotation == 270 || rotation == -90)rotation = 90;




        SCALE = PlayScreen.scale;
        this.initRotation = rotation;
        initialized = false;


        hittime = 0;
        hit = 0;
        resizecount = 0;
        rotationMinus = 0;

        //Create mirror Sprite
        mirror = createScaledSprite(mirrorTex);
        mirror.setOriginCenter();

        //Set Actor properties
        setTouchable(Touchable.enabled);

        touchbox = new Rectangle();


        fdef = new FixtureDef();
        shape = new PolygonShape();
        poly = new Polygon();

        //Create box area possible to touch object
        addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if(!gsm.getScreen().isInLvlMenu())
                {
                    if (hit == -1) {
                        hit = 1;
                    } else {
                        hit++;
                    }
                    if (hit > 3) {
                        hit = 3;
                    }
                }
                return true;
            }
        });

        initRotation = rotation;
        //setRotation(initRotation);
        mirror.setRotation(initRotation);


    }

    //Method that creates a Box2D body over the mirror
    public void defineBody()
    {
        if(B2body != null){
            PlayScreen.world.destroyBody(B2body);}

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;
        B2body = PlayScreen.world.createBody(bdef);


        calculateTriangle(mirror.getX(), mirror.getY(), mirror.getRotation());
    }


    @Override
    protected void positionChanged()
    {

        //Rotate
        hittime += dt;
        mirror.rotate(90f * dt*5f);
        //setRotation(mirror.getRotation());


        super.positionChanged();
    }

    public void rotateAroundPoint(float pointX, float pointY)
    {
        mirror.setCenter(pointX, pointY);
        mirror.rotate(90f * dt*5f);
        setPosition(mirror.getX(), mirror.getY());
        //setRotation(mirror.getRotation());
        mirror.setOriginCenter();

    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(initialized)mirror.draw(batch);
    }

    private int actCount = 0;

    @Override
    public void act(float delta)
    {
        SCALE = PlayScreen.scale;

        if(actCount < 4)actCount++;

        dt=delta;
        if(!initialized)
        {
            mirror.setRotation(initRotation);

            dt = delta;
            positionChanged();

            if (hittime > (1.0 / 5.0))
            {
                if (hit >= 0) hit--;
                hittime = 0;

                //Set rotation to the nearest multiple of 90
                mirror.setRotation(90 * (Math.round(mirror.getRotation() / 90)));

                if (mirror.getRotation() >= 360)
                {
                    mirror.setRotation(((mirror.getRotation() - 360 * (int) (mirror.getRotation() / 360)) / 90) * 90);
                }

                //setRotation(mirror.getRotation());
            }
            //setOrigin(mirror.getOriginX(), mirror.getOriginY());
        }

        if(initialized && hit>0)
        {
            positionChanged();

            if(hittime>=(1.0/5.0))
            {
                if(hit>0)hit--;
                hittime = 0;
                if (hit == 0)
                {

                    //Set rotation to the nearest multiple of 90
                    mirror.setRotation(90 * (Math.round(mirror.getRotation() / 90)));

                    if (mirror.getRotation() >= 360)
                    {
                        mirror.setRotation(((mirror.getRotation() - 360 * (int) (mirror.getRotation() / 360)) / 90) * 90);
                    }

                    //setRotation(mirror.getRotation());
                }
            }

        }
        
        if(fadeIn || fadeOut)
        {
            if(fadeIn)
            {
                if(mirror.getColor().a <0.95f)
                {
                    mirror.setColor(mirror.getColor().r, mirror.getColor().g, mirror.getColor().b, mirror.getColor().a + 1 * delta * 8 / 4.0f);
                }
                else
                {
                    mirror.setColor(mirror.getColor().r, mirror.getColor().g, mirror.getColor().b, 1f);
                    fadeIn = false;
                    fadeOut = false;
                }
            }
            else
            {
                if(mirror.getColor().a >= 0.39f)
                {
                    mirror.setColor(mirror.getColor().r, mirror.getColor().g, mirror.getColor().b, mirror.getColor().a - 1 * delta * 15 / 4.0f);
                }
                else
                {
                    mirror.setColor(mirror.getColor().r, mirror.getColor().g, mirror.getColor().b, Toggle.fadealpha);
                    fadeIn = false;
                    fadeOut = false;
                }
            }
        }




        if(!initialized)
        {
            initialized = true;
        }

        if(actCount<4){defineBody(); calculateTriangle(mirror.getX(), mirror.getY(), mirror.getRotation());}

        if(poly.getRotation() != mirror.getRotation()){calculateTriangle(mirror.getX(), mirror.getY(), mirror.getRotation());}

        super.act(delta);
    }


    //Method that calculates the position of the Box2D body points
    public void calculateTriangle(float posX, float posY, float rotation)
    {

        rotation = (int)rotation;

        float nearRotation = rotation;
        nearRotation = (float)(90 * (Math.round(nearRotation / 90)));

        points[0]=posX+10/ PlayScreen.PPM/SCALE; points[1]=posY+10/ PlayScreen.PPM/SCALE;
        points[2]=posX+118/ PlayScreen.PPM/SCALE; points[3]=posY+10/ PlayScreen.PPM/SCALE;
        points[4]=posX+10/ PlayScreen.PPM/SCALE; points[5]=posY+118/ PlayScreen.PPM/SCALE;

        if(nearRotation > 270f || nearRotation == 0f)
        {
            acceptedDirs = "dl";
        }
        else if(nearRotation > 0f && nearRotation <= 90f)
        {
            acceptedDirs = "dr";

        }
        else if(nearRotation > 90f && nearRotation <=180f)
        {
            acceptedDirs = "ur";
        }
        else if(nearRotation > 180f && nearRotation <= 270f)
        {
            acceptedDirs = "ul";
        }


        poly.setVertices(points);
        poly.setOrigin(mirror.getX() + mirror.getWidth()/2, mirror.getY() + mirror.getHeight()/2);
        poly.setRotation(mirror.getRotation());
        shape.set(poly.getTransformedVertices());
        fdef.shape = shape;
        fdef.density =1f;

        int fixtureCount = B2body.getFixtureList().size;
        for(int i=0;i<fixtureCount;i++)
        {
            B2body.destroyFixture(B2body.getFixtureList().get(i));
        }
        B2body.createFixture(fdef).setUserData(getUserData());
    }

    @Override
    public void handleCollision(Pellet pellet)
    {
        if(!isMovingToPoint())
        {
            if (mirror.getColor().a > Toggle.fadealpha)
            {

                float mirOX = getPosX() + getWidth() / 2;
                float mirOY = getPosY() + getHeight() / 2;

                if (acceptedDirs.equals("dl"))
                {

                    if (pellet.direction == 'u')
                    {
                        pellet.direction = 'd';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                    }
                    else if (pellet.direction == 'd')
                    {
                        pellet.direction = 'r';
                    }
                    else if (pellet.direction == 'l')
                    {
                        pellet.direction = 'u';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                    else if (pellet.direction == 'r')
                    {
                        pellet.direction = 'l';
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                }
                else if (acceptedDirs.equals("dr"))
                {
                    if (pellet.direction == 'u')
                    {
                        pellet.direction = 'd';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                    }
                    else if (pellet.direction == 'd')
                    {
                        pellet.direction = 'l';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2) + 1 / PlayScreen.PPM);
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2) - 1 / PlayScreen.PPM);
                    }
                    else if (pellet.direction == 'l')
                    {
                        pellet.direction = 'r';
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                    else if (pellet.direction == 'r')
                    {
                        pellet.direction = 'u';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                }
                else if (acceptedDirs.equals("ur"))
                {
                    if (pellet.direction == 'u')
                    {
                        pellet.direction = 'l';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                    else if (pellet.direction == 'd')
                    {
                        pellet.direction = 'u';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                    }
                    else if (pellet.direction == 'l')
                    {
                        pellet.direction = 'r';
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));

                    }
                    else if (pellet.direction == 'r')
                    {
                        pellet.direction = 'd';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                }
                else if (acceptedDirs.equals("ul"))
                {
                    if (pellet.direction == 'u')
                    {
                        pellet.direction = 'r';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                    else if (pellet.direction == 'd')
                    {
                        pellet.direction = 'u';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                    }
                    else if (pellet.direction == 'l')
                    {
                        pellet.direction = 'd';
                        pellet.pellet.setX(mirOX - (pellet.pellet.getWidth() / 2));
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                    else if (pellet.direction == 'r')
                    {
                        pellet.direction = 'l';
                        pellet.pellet.setY(mirOY - (pellet.pellet.getHeight() / 2));
                    }
                }
                //Draw the pellet at the origin of the mirror
                pellet.collided = false;
            }
        }
    }

    private String toggleAction = "";
    private boolean fadeIn = false;
    private boolean fadeOut = false;
    
    @Override
    public void actToggle()
    {
        if (toggleAction.equals("disable") && mirror.getColor().a < Toggle.fadealpha+0.01f)
        {
            fadeIn = true;
            fadeOut = false;
            //defineBody();
        }
        else if (toggleAction.equals("disable") && mirror.getColor().a >=0.99f)
        {
            fadeOut = true;
            fadeIn = false;
        }
    }

    @Override
    public String getUserData()
    {
        return id+""+acceptedDirs+"x"+(int)mirror.getX()+"y"+(int)mirror.getY();
    }


    public void resize()
    {
        hit++; rotationMinus+=90f;
        resizecount++;
    }

    @Override
    public int getId()
    {
        return id;
    }

    //Method that creates a scaled Sprite
    @Override
    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize(sprite.getWidth() / PlayScreen.PPM/SCALE,
        sprite.getHeight() / PlayScreen.PPM/SCALE);
        return sprite;
    }

    @Override
    public float getPosX() {
        return mirror.getX();
    }

    @Override
    public float getPosY() {
        return mirror.getY();
    }

    @Override
    public float getWidth(){return mirror.getWidth();}

    @Override
    public float getHeight() {return mirror.getHeight();}

    @Override
    public void setFull(boolean full) {

    }

    @Override
    public boolean isFull() {
        return false;
    }


    @Override
    public void destroyBody(World world)
    {
        world.destroyBody(B2body);
    }


    @Override
    public void setInitialized(boolean inited)
    {
        initialized = inited;
    }

    @Override
    public String getType()
    {
        return "mirror";
    }

    @Override
    public void setLocked(boolean isLocked) {

    }

    @Override
    public void dispose() {
    }

    @Override
    public Sprite getSprite() {
        return mirror;
    }

    @Override
    public void setHit(int hit) {
        this.hit = hit;
    }
}
