package com.mygdx.game.Obstacles.Track;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.Obstacles.Obstacle;
import com.mygdx.game.Obstacles.Toggle;
import com.mygdx.game.PelletGame;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Reusables;

/**
 * Created by Lewis on 9/25/2017.
 */


public class TrackPoint extends Obstacle
{
    public static int trackAmount = 0;

    //Properties
   //private int id = -1;
    private TrackGroup group;
    private boolean touched = false;
    private float SCALE = 1;
    private int trackNum = -1;
    private boolean hasObjectOnTop = false;
    
    //For Toggles
    private String toggleAction = null;
    private boolean allowTouch = true;

    //Textrue vars
    private static Texture point = Reusables.greyBorder;
    private static Texture dot = Reusables.coloredPoint;

    private Sprite pointSprite;
    private Sprite dotSprite;

    public TrackPoint(int id, int trackNum)
    {
        makeId(id);
        this.trackNum = trackNum;
        SCALE = PlayScreen.scale;

        if(trackNum > trackAmount-1)
        {
            trackAmount++;
        }
        pointSprite = createScaledSprite(point);
        dotSprite = createScaledSprite(dot);

        setTouchable(Touchable.enabled);

        addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if(allowTouch)touched = true;
                ////System.out.println("touchied");
                return super.touchDown(event, x, y, pointer, button);
            }
        });


    }

    public TrackPoint(int id, int trackNum, int toggleId, String action)
    {
        makeId(id);
        setToggleId(toggleId);
        this.trackNum = trackNum;
        this.toggleAction = action;
        SCALE = PlayScreen.scale;

        pointSprite = createScaledSprite(point);
        dotSprite = createScaledSprite(dot);

        setTouchable(Touchable.enabled);


        if(action.equals("disable"))
        {
            dotSprite.setColor(dotSprite.getColor().r, dotSprite.getColor().g, dotSprite.getColor().b, Toggle.fadealpha);
            pointSprite.setColor(pointSprite.getColor().r, pointSprite.getColor().g, pointSprite.getColor().b, Toggle.fadealpha);

            allowTouch = false;
        }
        addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if(!PelletGame.gsm.getScreen().isInLvlMenu())
                {
                    if(allowTouch)touched = true;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void setGroup(TrackGroup group){this.group = group;}

    private boolean doCloseAnim = false;
    private float closeTime = 0;
    public float openTime = 0;

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(doCloseAnim)
        {
            animateClose(Gdx.graphics.getDeltaTime(), closeTime, pointSprite, dotSprite, batch);
        }
        else if(!hasObjectOnTop)
        {
            openTime+= Gdx.graphics.getDeltaTime();
            animateOpen(Gdx.graphics.getDeltaTime(), openTime, pointSprite, dotSprite, batch);
        }


        super.draw(batch, parentAlpha);
    }

    private int initialized = 0;

    @Override
    public void act(float delta)
    {
        if(initialized <2)
        {
            pointSprite.setPosition(getX(), getY());
            dotSprite.setPosition(getX() + pointSprite.getWidth() / 2 - dotSprite.getWidth() / 2, getY() + pointSprite.getHeight() / 2 - dotSprite.getHeight() / 2);
            initialized++;
        }
        if(touched && !group.isObstacleMoving())
        {
            touched = false;
            group.setAllPointsHasOnTop(false);
            group.getObstacle().moveToPoint(this);
            hasObjectOnTop = true;
            openTime = 0;
            closeTime = 0;
            doCloseAnim = true;
        }

        if(initialized <=3)
        {
            if(initialized == 2)
            {
                //setBounds(getX()-pointSprite.getWidth() , getY() - pointSprite.getHeight()
                    //    , pointSprite.getWidth()*2, pointSprite.getHeight()*2);

                pointSprite.setPosition(getX() + pointSprite.getWidth()/2, getY() + pointSprite.getHeight()/2);
                dotSprite.setPosition(getX() + pointSprite.getWidth()/2 + dotSprite.getWidth() / 2, getY() + pointSprite.getHeight()/2 + dotSprite.getHeight() / 2);

                initialized = 3;
            }
            //else{setBounds(getX(), getY(), pointSprite.getWidth()*2, pointSprite.getHeight()*2);}
        }

        if(fadeIn || fadeOut)
        {
            if(fadeIn)
            {
                if(dotSprite.getColor().a <0.95f)
                {
                    dotSprite.setColor(dotSprite.getColor().r, dotSprite.getColor().g, dotSprite.getColor().b, dotSprite.getColor().a + 1 * delta * 8 / 4.0f);
                    pointSprite.setColor(pointSprite.getColor().r, pointSprite.getColor().g, pointSprite.getColor().b, pointSprite.getColor().a + 1 * delta * 8 / 4.0f);
                }
                else
                {
                    dotSprite.setColor(dotSprite.getColor().r, dotSprite.getColor().g, dotSprite.getColor().b, 1f);
                    pointSprite.setColor(pointSprite.getColor().r, pointSprite.getColor().g, pointSprite.getColor().b, 1f);
                    fadeIn = false;
                    fadeOut = false;
                }
            }
            else
            {
                if(dotSprite.getColor().a >= 0.39f)
                {
                    dotSprite.setColor(dotSprite.getColor().r, dotSprite.getColor().g, dotSprite.getColor().b, dotSprite.getColor().a - 1 * delta * 15 / 4.0f);
                    pointSprite.setColor(pointSprite.getColor().r, pointSprite.getColor().g, pointSprite.getColor().b, pointSprite.getColor().a - 1 * delta * 15 / 4.0f);
                }
                else
                {
                    dotSprite.setColor(dotSprite.getColor().r, dotSprite.getColor().g, dotSprite.getColor().b, Toggle.fadealpha);
                    pointSprite.setColor(pointSprite.getColor().r, pointSprite.getColor().g, pointSprite.getColor().b, Toggle.fadealpha);
                    fadeIn = false;
                    fadeOut = false;
                }
            }
        }

        if(doCloseAnim){closeTime+=delta;}

        super.act(delta);
    }

    public int getTrackNum(){return trackNum;}

    public void setHasObjectOnTop(boolean b){hasObjectOnTop = b;}

    public void setPointColor(Color c)
    {
        dotSprite.setColor(c);
    }

    @Override
    public void defineBody() {

    }

    @Override
    public void handleCollision(Pellet pellet) {

    }

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
    public String getUserData() {
        return "r";
    } // 'r' for tRack

    @Override
    public float getPosX() {
        return getX();
    }

    @Override
    public float getPosY() {
        return getY();
    }

    @Override
    public void setFull(boolean full) {

    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void destroyBody(World world) {

    }

    @Override
    public void setInitialized(boolean inited) {

    }

    @Override
    public void resize() {

    }

    @Override
    public String getType() {
        return "track";
    }


    private boolean fadeIn = false;
    private boolean fadeOut = false;
    @Override
    public void actToggle() 
    {
        if (toggleAction.equals("disable") && pointSprite.getColor().a < Toggle.fadealpha+0.01f)
        {
            fadeIn = true;
            fadeOut = false;
        }
        else if (toggleAction.equals("disable") && pointSprite.getColor().a >=0.99f)
        {
            fadeOut = true;
            fadeIn = false;
        }
        else if(toggleAction.equals("enable"))
        {
            fadeOut = true;
            fadeIn = false;
        }

        allowTouch = !allowTouch;

        if(!allowTouch && hasObjectOnTop)
        {
            while(true)
            {
                int index = (int)(Math.random()*group.getPoints().length);

                if(group.getPoints()[index].allowTouch)
                {
                    group.getPoints()[index].touched = true;
                    break;
                }
            }
        }
    }

    @Override
    public void setLocked(boolean isLocked) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Sprite getSprite() {
        return pointSprite;
    }

    @Override
    public void setHit(int hit) {

    }
    private Color defaultColor;

    private Vector2 origin = new Vector2();
    private void animateClose(float dt, float timePassed, Sprite pSprite, Sprite dSprite, Batch batch)
    {
        float totalTime = Obstacle.pointMoveTime;

        defaultColor = batch.getColor();
        origin.set(pSprite.getX() + pSprite.getWidth()/2, pSprite.getY() + pSprite.getHeight()/2);

        float widthPNow = pSprite.getWidth() - (pSprite.getWidth()*timePassed/totalTime);
        float heightPNow = pSprite.getHeight() - (pSprite.getHeight()*timePassed/totalTime);

        float widthDNow = dSprite.getWidth() - (dSprite.getWidth()*timePassed/totalTime);
        float heightDNow = dSprite.getHeight() - (dSprite.getHeight()*timePassed/totalTime);

        batch.setColor(batch.getColor().a, batch.getColor().g, batch.getColor().b, pSprite.getColor().a);
        batch.draw(pSprite, origin.x - widthPNow/2, origin.y - heightPNow/2,
                widthPNow, heightPNow);

        batch.setColor(group.color.r, group.color.g, group.color.b, pSprite.getColor().a);
        batch.draw(dSprite, origin.x - widthDNow/2, origin.y - heightDNow/2,
                widthDNow, heightDNow);
        batch.setColor(defaultColor);

        if(timePassed>= totalTime)
        {
            doCloseAnim = false;
            closeTime = 0;

        }
    }

    private void animateOpen(float dt, float timePassed, Sprite pSprite, Sprite dSprite, Batch batch)
    {
        float totalTime = Obstacle.pointMoveTime;

        defaultColor = batch.getColor();
        origin.set(pSprite.getX() + pSprite.getWidth()/2, pSprite.getY() + pSprite.getHeight()/2);

        float widthPNow = (pSprite.getWidth()*timePassed);
        float heightPNow = (pSprite.getHeight()*timePassed);

        float widthDNow = (dSprite.getWidth()*timePassed);
        float heightDNow = (dSprite.getHeight()*timePassed);

        if(timePassed < totalTime)
        {
            batch.setColor(batch.getColor().a, batch.getColor().g, batch.getColor().b, pSprite.getColor().a);
            batch.draw(pSprite, origin.x - widthPNow / 2, origin.y - heightPNow / 2,
                    widthPNow, heightPNow);

            batch.setColor(group.color.r, group.color.g, group.color.b, pSprite.getColor().a);
            batch.draw(dSprite, origin.x - widthDNow / 2, origin.y - heightDNow / 2,
                    widthDNow, heightDNow);
        }
        else
        {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, pSprite.getColor().a);
            batch.draw(pSprite, pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight());
            batch.setColor(group.color.r, group.color.g, group.color.b, pSprite.getColor().a);
            batch.draw(dSprite, origin.x - dSprite.getWidth()/2, origin.y - dSprite.getHeight()/2, dSprite.getWidth(), dSprite.getHeight());

        }
        batch.setColor(defaultColor);
    }

    public void setTouched(boolean touched){this.touched = touched;}

    public boolean allowTouch(){return allowTouch;}
}
