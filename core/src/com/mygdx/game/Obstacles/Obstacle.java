package com.mygdx.game.Obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.Obstacles.Track.TrackPoint;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.GameScreenManager;
import com.mygdx.game.Tools.Reusables;

import static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.point;

/**
 * Created by Lewis on 8/25/2017.
 */

//TODO GET ALL OBSTACLES WORKING IN TILED

public abstract class Obstacle extends Actor
{
    private TextureAtlas lineAtlas = new TextureAtlas(Gdx.files.internal("Animations/BoxBound/lineAnimAtlas.txt"));
    private Animation lineAnim = new Animation(1/30f, lineAtlas.getRegions());
    private float elapsedTime = 0f;

    private boolean justMoved = false;
    private boolean movingToPoint = false;

    //four line sprites for drawing bounding box
    private static Texture boxBound = null;
    private Sprite box = new Sprite(Reusables.boxBound);


    private boolean startedBoxAnim = false;

    private float moveDistance = 0;
    private Vector2 nextPoint = new Vector2();

    float endx;
    float endy;

    private float slope;
    private float rise;
    private float run;

    private float degreesAdd = 0;
    private float initialRotation90;

    public boolean isIdDeletable = false;

    public int id = -1;
    public static int nextId = 0;

    private int toggleId = 1300100323;

    public float centerX = 0, centerY = 0;

    public GameScreenManager gsm;

    public Obstacle(GameScreenManager gsm)
    {
        if(gsm != null){this.gsm = gsm;}
        if(id == -1)
        {
            id = nextId + 1;
            nextId++;
        }


        //setSize(128/PlayScreen.PPM/PlayScreen.scale, 128/PlayScreen.PPM/PlayScreen.scale*1.3f);
    }

    private int givenId = 0;
    private boolean  isId = false;
    public void makeId(int id)
    {
        isId = true;
        this.givenId = id;
        this.id = id;

        if(this.id != givenId){this.id = givenId;}
        ////System.out.println(id);
    }


    public boolean onTrack = false;
    public void moveToPoint(TrackPoint point)
    {
        onTrack = true;
        endx = point.getX() ;
        endy = point.getY() ;

        float X;
        float Y;


        X = getX();
        Y = getY();


        point.setTouchable(Touchable.disabled);
        justMoved = true;
        movingToPoint = true;

        moveDistance = getMoveDistance(endx - X, endy - Y);

        currentVelocity = 0;

        rise = (endy - Y);
        run = (endx - X);

        slope = rise/run;
        yintercept = Y - (slope*X);

        degreesAdd = 0;
        initialRotation90 = Math.round(getSprite().getRotation()/90)*90;
    }



    private float yintercept;
    private float distanceMoved = 0;
    private float currentVelocity = 0;

    private float moveTimePassed = 0;

    public static final float pointMoveTime = 1f;

    public boolean centerObstacleAtAllTimes = true;

    @Override
    public void act(float delta)
    {

       if(centerObstacleAtAllTimes)
       {
            try
            {
                getSprite().setX(getX() + getWidth()/2 - getSprite().getWidth()/3);
                getSprite().setY(getY() + getHeight()/2 - getSprite().getWidth()/3);

                centerX = getSprite().getX();
                centerY = getSprite().getY();
            }
            catch (Exception e)
            {
                //getSprite().setPosition(centerX, centerY);
            }
       }


        if(isId){id = givenId;}

        if(movingToPoint)
        {
            moveTimePassed += delta;

            float rise2 = rise * rise;
            float run2 = run * run;

            currentVelocity = getMoveVelocity(moveDistance, currentVelocity, moveTimePassed, distanceMoved, pointMoveTime, delta);

            //currentVelocity = moveDistance;
            float nowDistance = currentVelocity * delta;

            distanceMoved += nowDistance;

            float a;
            float b;
            if (run != 0) {
                a = (float) Math.sqrt((Math.pow(nowDistance, 2) * run2) / (rise2 + run2));
                b = slope * a;
            } else {
                a = 0;
                b = nowDistance;
            }

            a = Math.abs(a);
            b = Math.abs(b);

            if ((rise > 0 && run > 0)) {
                setX(getX() + a);
                setY(getY() + b);

                if (getX() >= endx || getY() >= endy) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            } else if ((rise < 0 && run < 0)) {
                setX(getX() - a);
                setY(getY() - b);

                if (getX() <= endx || getY() <= endy) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            } else if (rise < 0 && run > 0) {

                setX(getX() + a);
                setY(getY() - b);

                if (getX() >= endx || getY() <= endy) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            } else if (rise > 0 && run < 0) {

                setX(getX() - a);
                setY(getY() + b);

                if (getX() <= endx || getY() >= endy) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            } else if (rise == 0 && run > 0) {

                setX(getX() + a);

                if (getX() >= endx) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            } else if (rise == 0 && run < 0) {
                setX(getX() - a);

                if (getX() <= endx) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            } else if (run == 0 && rise > 0) {
                setY(getY() + b);

                if (getY() >= endy) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            } else if (run == 0 && rise < 0) {
                setY(getY() - b);

                if (getY() <= endy) {
                    movingToPoint = false;
                    setX(endx);
                    setY(endy);
                }
            }
            // //System.out.println(a+", "+b);

            getSprite().setRotation(initialRotation90 + degreesAdd);
            degreesAdd += 360 * (nowDistance / moveDistance);

            if (!movingToPoint)
            {
                getSprite().setRotation(initialRotation90);
                setHit(1);
                distanceMoved = 0;
                moveTimePassed = 0;
                currentVelocity = 0;
            }
            if(getType().equals("toggle"))
            {
                //setX(getX()-getSprite().getWidth()/2);
                //setY(getY()-getSprite().getHeight()/2);
            }
        }
        super.act(delta);
    }

    public int getId(){return id;}

    public void setToggleId(int id){toggleId = id;}

    public int getToggleId(){return toggleId;}

    public boolean hasJustMoved()
    {
        return justMoved;
    }

    public void setJustMoved(boolean b)
    {
        justMoved = b;
    }

    public boolean isMovingToPoint(){return movingToPoint;}

    private float getMoveDistance(float x, float y)
    {
        return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public abstract void defineBody();

    public abstract Sprite createScaledSprite(Texture texture);

    public abstract String getUserData();

    public abstract float getPosX();

    public abstract float getPosY();

    public abstract void setFull(boolean full);

    public abstract boolean isFull();

    public abstract void destroyBody(World world);

    public abstract void setInitialized(boolean inited);

    public abstract void resize();

    public abstract String getType();

    public abstract void handleCollision(Pellet pellet);

    public abstract void actToggle();

    public abstract void setLocked(boolean isLocked);

    public abstract void dispose();


    private float boxTime = 0f;
    private float shrinkDistance = 0f;
    private boolean drawBox = true;
    public void drawBoudingBox(float delta, Batch batch, float x, float y, float width, float height, boolean drawIn)
    {
        boxTime+=delta;

        if(!startedBoxAnim && boxTime == delta)
        {
            startedBoxAnim = true;
            drawBox = true;
            box.setSize(PlayScreen.gameport.getScreenHeight(),
                    PlayScreen.gameport.getScreenHeight());

            shrinkDistance = box.getWidth() - width*1.5f;

        }

        if(drawIn)
        {
            box.setSize(box.getWidth() - (shrinkDistance * delta * 2.5f), box.getHeight() - (shrinkDistance * delta * 2.5f));

            if(box.getWidth() < width*1.5f)
            {
                if(boxTime>0)
                {
                    if(boxBound == null){boxBound = Reusables.boxBound;}

                }
                box.setTexture(boxBound);
                box.setSize(width*1.6f, height*1.6f);
                boxTime = 0f;
            }
        }
        else
        {
            shrinkDistance = PlayScreen.gameport.getScreenHeight() - width*1.5f;

            box.setSize(box.getWidth() + (shrinkDistance * delta * 2.5f), box.getHeight() + (shrinkDistance * delta * 2.5f));

            if(boxTime >= 0.4f)
            {

                boxTime = 0f;
                box.setSize(PlayScreen.gameport.getScreenHeight(),
                    PlayScreen.gameport.getScreenHeight());

                shrinkDistance = box.getWidth() - width*1.5f;
            }
        }


        box.setPosition(getX() + getWidth()/2 - box.getWidth()/2,getY() + getWidth()/2 - box.getHeight()/2);

        box.draw(batch);
    }

    public abstract Sprite getSprite();

    public abstract void setHit(int hit);

    private float getMoveVelocity(float totalDistance, float cv, float currentTime, float currentDistance, float timeDesired, float dt)
    {
        float currVel = cv;
        float distanceLeft = Math.abs(totalDistance - currentDistance);
        float timeLeft = timeDesired - currentTime;

        float a = ((currVel + totalDistance*dt) - currVel)/timeDesired;

        if(currentDistance <= totalDistance/2.00f + currVel*dt)
        {
            currVel += a;
        }
        else
        {
            currVel -= a;
        }
        return currVel;
    }

    public Toggle getToggle(int id)
    {
        for(int i = 0; i < PlayScreen.lmg.getCurrentlvl().getObstacles().length; i++)
        {
            if(PlayScreen.lmg.getCurrentlvl().getObstacles()[i].getId() == id)
            {
                return (Toggle) PlayScreen.lmg.getCurrentlvl().getObstacles()[i];
            }
        }

        return null;
    }

    private int mapX, mapY;
    public Obstacle setMapPos(int x, int y)
    {
        mapX = x;
        mapY = y;

        return this;
    }

    public Vector2 getMapPos(){return new Vector2(mapX, mapY);}

    public int getMapX(){return mapX;}

    public int getMapY(){return mapY;}

    private Color batchColor = null;
    public void setDrawColor(Color color)
    {
        batchColor = color;

        if(getType().equals("mirror"))
        {
            getSprite().setTexture(Reusables.whiteMirrorTexture);
            getSprite().setColor(color.r, color.g, color.b, getSprite().getColor().a);
        }
        else if(getType().equals("block"))
        {
            getSprite().setTexture(Reusables.blockBlankTex);
            getSprite().setColor(color.r, color.g, color.b, getSprite().getColor().a);
        }
        else if(getType().equals("pelletStart"))
        {
            getSprite().setTexture(Reusables.pelletStartTex);
            getSprite().setColor(color.r, color.g, color.b, getSprite().getColor().a);
        }
        else if(getType().equals("pelletEnd"))
        {
            getSprite().setTexture(Reusables.pelletEndBlankTexture);
            getSprite().setColor(color.r, color.g, color.b, getSprite().getColor().a);
        }
    }

    public Color getDrawColor(){return batchColor;}


}
