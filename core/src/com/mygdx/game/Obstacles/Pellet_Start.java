package com.mygdx.game.Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Reusables;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import box2dLight.PointLight;

/**
 * Created by Lewis on 8/12/2017.
 */


public class Pellet_Start extends PelletHole
{
    private Sprite laserStart;

    private float SCALE = 1;

    private Body hitCirc;
    private boolean initialized = false;

    public String fireDirections = "";
    public int fireCount = 0;

    private PointLight startLight;
    private PointLight startLight1;
    private PointLight startLight2;
    private float lightDistance = 0;

    private float initialDelayTime = 0;
    private float pelletPause = 0;
    private boolean fireInAllDirectionsAtOnce = true;

    public Pellet_Start(String fireDirections, float initalDelayTime, float pelletPause, boolean fireInAllDirectionsAtOnce)
    {
        SCALE = PlayScreen.scale;

        this.initialDelayTime = initalDelayTime;
        this.pelletPause = pelletPause;
        this.fireInAllDirectionsAtOnce = fireInAllDirectionsAtOnce;

        lightDistance = 120f/ PlayScreen.PPM/SCALE;
        startLight = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight1 = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight2 = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight.setXray(true);

        //Create the Sprite
        laserStart = createScaledSprite(Reusables.pelletStartTex);

        this.fireDirections = fireDirections;

        defineBody();
    }

    public Pellet_Start(int id, String fireDirections, float initalDelayTime, float pelletPause, boolean fireInAllDirectionsAtOnce)
    {
        makeId(id);
        SCALE = PlayScreen.scale;

        this.initialDelayTime = initalDelayTime;
        this.pelletPause = pelletPause;
        this.fireInAllDirectionsAtOnce = fireInAllDirectionsAtOnce;

        lightDistance = 120f/ PlayScreen.PPM/SCALE;
        startLight = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight1 = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight2 = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight.setXray(true);


        //Create the Sprite
        laserStart = createScaledSprite(Reusables.pelletStartTex);

        this.fireDirections = fireDirections;

        defineBody();
    }

    public Pellet_Start(String fireDirections)
    {
        SCALE = PlayScreen.scale;

        lightDistance = 120f/ PlayScreen.PPM/SCALE;
        startLight = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight1 = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight2 = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight.setXray(true);

        //Create the Sprite
        laserStart = createScaledSprite(Reusables.pelletStartTex);

        this.fireDirections = fireDirections;

        defineBody();
    }

    public Pellet_Start(int id, String fireDirections)
    {
        makeId(id);
        SCALE = PlayScreen.scale;

        lightDistance = 120f/ PlayScreen.PPM/SCALE;
        startLight = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, lightDistance, PlayScreen.PPM/SCALE, PlayScreen.gameport.getWorldHeight()-(PlayScreen.PPM/SCALE));
        startLight.setXray(true);

        //Create the Sprite
        laserStart = createScaledSprite(Reusables.pelletStartTex);

        this.fireDirections = fireDirections;

        defineBody();
    }

    private boolean isOn = true;

    public void addToggle(int toggleId, String startingAction)
    {
        setToggleId(toggleId);

        if(startingAction.equals("disable"))
        {
            startLight.setDistance(0);
            isOn = false;
        }

    }

    public void defineBody()
    {
        if(hitCirc!= null){
            PlayScreen.world.destroyBody(hitCirc);}
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;
        hitCirc = PlayScreen.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circ = new CircleShape();

        circ.setRadius(5f/ PlayScreen.PPM);
        circ.setPosition(new Vector2(centerX + getWidth()/2, centerY + getHeight()/2));

        fdef.shape = circ;

        int fixtureCount = 0;
        if(hitCirc.getFixtureList().size !=0){fixtureCount = hitCirc.getFixtureList().size;}
        else{fixtureCount = 0;}
        for(int i = 0; i<fixtureCount; i++)
        {
           hitCirc.destroyFixture(hitCirc.getFixtureList().get(i));
        }
        hitCirc.createFixture(fdef).setUserData(getUserData());
    }

    @Override
    public void handleCollision(Pellet pellet)
    {
        if(isOn)
        {
            setFull(true);
            pellet.remove = true;
            pellet.removeLight = true;
            pellet.justInteracted = true;
            pellet.collided = true;
        }
    }

    private boolean animateClose = false;

    @Override
    public void actToggle()
    {
        if(isOn)
        {
            animateClose = true;
            isOn = false;
        }
        else
        {
            animateClose = false;
            isOn = true;
            startLight.setActive(true);
        }
    }

    //Method to create a scaled Sprite
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
        return "s"+id;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        PlayScreen.pelletAlpha = parentAlpha;
    }

    private float timeSinceLastFire = 0;
    private double timeCheck = 0;
    @Override
    public void act(float delta)
    {

        centerX = getX() + getWidth()/2 - laserStart.getWidth()/3;
        centerY = getY() + getHeight()/2 - laserStart.getWidth()/3;

        startLight.setPosition(centerX+getWidth()/2, centerY+getHeight()/2);
        startLight1.setPosition(centerX+getWidth()/2, centerY+getHeight()/2);
        startLight2.setPosition(centerX+getWidth()/2, centerY+getHeight()/2);

        if(!initialized)
        {
            laserStart.setX(getX());
            laserStart.setY(getY());
            defineBody();
        }

        if(!animateClose)
        {
            if(startLight.getDistance() >= lightDistance){startLight.setDistance(lightDistance);}
            else
            {
                startLight.setDistance(startLight.getDistance() + (lightDistance*delta*3));
            }
        }
        else
        {
            if(startLight.getDistance() <= 0){startLight.setDistance(0); startLight.setActive(false);}
            else
            {
                startLight.setDistance(startLight.getDistance() - lightDistance*delta*3);
            }
        }

        timeCheck = Math.round(100.0*(PlayScreen.timePassed-initialDelayTime))/100.0;
        if(timeCheck % pelletPause >= -delta &&
            timeCheck % pelletPause <= delta && PlayScreen.timePassed-initialDelayTime > 0)
        {
            if(timeSinceLastFire >= pelletPause-delta)
            {
                fire(PlayScreen.pellets);
                timeSinceLastFire = 0;
            }
        }

        timeSinceLastFire+=delta;

        super.act(delta);
    }

    private char currentDir = 'd';
    public void fire(ArrayList<Pellet> pellets)
    {

        if(isOn)
        {
            fireCount++;
            currentDir = fireDirections.charAt(fireCount % fireDirections.length());
            if(PlayScreen.lmg.getCurrentlvl().getAmbientLight() == 0.95f)
            {
                if(fireInAllDirectionsAtOnce)
                {
                    for(int i = 0; i<fireDirections.length(); i++)
                    {
                        pellets.add(new Pellet(getSpawnX(fireDirections.charAt(i)), getSpawnY(fireDirections.charAt(i)), fireDirections.charAt(i)));
                    }
                }
                else {pellets.add(new Pellet(getSpawnX(currentDir), getSpawnY(currentDir), currentDir));}
            }
            else
            {
                if(fireInAllDirectionsAtOnce)
                {
                    for(int i = 0; i<fireDirections.length(); i++)
                    {
                        pellets.add(new Pellet(getSpawnX(fireDirections.charAt(i)), getSpawnY(fireDirections.charAt(i)), fireDirections.charAt(i), true));
                    }
                }
                else {pellets.add(new Pellet(getSpawnX(currentDir), getSpawnY(currentDir), currentDir, true));}
            }
        }
    }

    @Override
    public float getSpawnX(char direction)
    {
        float x = 0;

        if(direction == 'u'){x = getPosX() + getWidth() / 2;}
        else if(direction == 'd'){x = getPosX() + getWidth() / 2;}
        else if(direction == 'l'){x = getPosX() + getWidth() * 1 / 3;}
        else if(direction == 'r'){x = getPosX() + getWidth() * 2 / 3;}

        return x;
    }

    @Override
    public float getSpawnY(char direction)
    {
        float y = 0;

        if(direction == 'u'){y = getPosY() + getHeight() * 2/3;}
        else if(direction == 'd'){y = getPosY() + getHeight() * 1/3;}
        else if(direction == 'l'){y = getPosY() + getHeight() / 2;}
        else if(direction == 'r'){y = getPosY() + getHeight() / 2;}

        return y;
    }

    @Override
    public void resize() {

    }

    @Override
    public float getPosX(){return centerX;}

    @Override
    public float getPosY(){return centerY;}

    @Override
    public float getWidth(){return laserStart.getWidth();}

    @Override
    public float getHeight(){return laserStart.getHeight();}

    @Override
    public void setFull(boolean full) {

    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public float getFillPelletRadius() {
        return 0;
    }

    @Override
    public void destroyBody(World world)
    {
        world.destroyBody(hitCirc);
    }


    @Override
    public void setInitialized(boolean inited) {

    }

    @Override
    public String getType()
    {
        return "pelletStart";
    }

    public String getFireDirections()
    {
        return fireDirections;
    }

    @Override
    public void setLocked(boolean isLocked) {

    }

    @Override
    public void dispose() {
    }

    @Override
    public Sprite getSprite() {
        return laserStart;
    }

    @Override
    public void setHit(int hit) {

    }
}
