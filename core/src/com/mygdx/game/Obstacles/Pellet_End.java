package com.mygdx.game.Obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.PelletGame;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Reusables;

import box2dLight.PointLight;


/**
 * Created by Lewis on 8/12/2017.
 */

public class Pellet_End extends PelletHole
{
    private Sprite laserEnd;
    private static Texture laserEndTex = Reusables.pelletEndTexture;

    private Sprite fillPellet;
    private static Texture pelletTex = Reusables.pelletAtEndTexture;

    private float SCALE = 1;

    public int pelletNum = 0;
    public boolean needsMorePellets = false;

    private Body hitCirc = null;

    private boolean initialized = false;
    private boolean remove = false;
    private boolean full = false;
    private boolean hitCircNull = false;

    private String toggleAction = "";

    public static int finalEndSelected = -1123451;

    private float pelletIncrease = 0;

    public Pellet_End()
    {
        create();
        defineBody();
        needsMorePellets = false;
    }

    public Pellet_End(int id)
    {
        create();
        makeId(id);
        defineBody();
    }

    public void addToggle(int toggleId, String startingAction)
    {
        setToggleId(toggleId);
        toggleAction = startingAction;

        if(startingAction.equals("disable"))
        {
            laserEnd.setColor(laserEnd.getColor().r, laserEnd.getColor().g, laserEnd.getColor().b, Toggle.fadealpha);
            fillPellet.setColor(fillPellet.getColor().r, fillPellet.getColor().g, fillPellet.getColor().b, Toggle.fadealpha);
        }
    }

    private float thisWidth = 0;
    public void create()
    {
        //Set scale and ID
        SCALE = PlayScreen.scale;

        //Create texture and pellet fill texture
        laserEnd = createScaledSprite(laserEndTex);
        fillPellet = createScaledPellet(pelletTex);

        thisWidth = (128/PlayScreen.PPM/PlayScreen.scale)*1.3f;
    }

    //Method that creates a Box2D Body to be placed over the Sprite
    public void defineBody()
    {
        if(hitCirc != null){
            PlayScreen.world.destroyBody(hitCirc);}

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;
        hitCirc = PlayScreen.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape circ = new CircleShape();

        circ.setRadius(laserEnd.getHeight()/16);
        circ.setPosition(new Vector2(laserEnd.getX()+laserEnd.getWidth()/2, laserEnd.getY()+laserEnd.getHeight()/2));

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


    //Method to create a scaled Sprite
    @Override
    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize(sprite.getWidth()/10 / PlayScreen.PPM/SCALE,
        sprite.getHeight()/10 / PlayScreen.PPM/SCALE);
        return sprite;
    }

    private boolean drawEnd = true;
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(drawEnd)
        {
            if (hitCircNull && fillPellet.getX() != 0)
            {
                fillPellet.draw(batch);
            }
            //if the actor has not been initialized
            if (!initialized)
            {
                laserEnd.setX(getX());
                laserEnd.setY(getY());
                defineBody();
            }

            //if this end portal has already been filled
            laserEnd.draw(batch);
            batch.setColor(1,1,1,1);


            initialized = true;

            if(needsMorePellets && pelletNum>0)
            {

                PelletGame.font44.draw(batch, pelletNum+"",
                        laserEnd.getX() + (laserEnd.getWidth()/2.09f) - textWidth/2,
                        laserEnd.getY() + (laserEnd.getHeight()/1.85f) + textHeight/2);

            }
        }

    }

    private boolean fillPelletIn = true;
    private int actCount = 0;
    @Override
    public void act(float delta)
    {
        if(actCount<5)actCount++;
        //Calculate the size of the fill pellet when this portal has been filled
        pelletIncrease = thisWidth*delta*1.1f;

        if(hitCircNull)
        {
            if(fillPellet.getWidth()+laserEnd.getWidth()/(128/25) < laserEnd.getWidth()){fillPelletIn = true;}
            else{fillPelletIn = false;}

            if(fillPelletIn)
            {
                laserEnd.setSize(laserEnd.getWidth() - thisWidth*delta*0.9f, laserEnd.getWidth() - thisWidth*delta*0.9f);
                fillPellet.setSize(fillPellet.getWidth()+pelletIncrease, fillPellet.getHeight()+pelletIncrease);

                laserEnd.setX(getX() + thisWidth/2 - laserEnd.getWidth()/2);
                laserEnd.setY(getY() + thisWidth/2 - laserEnd.getWidth()/2);
                fillPellet.setX(getX() + thisWidth/2 - fillPellet.getWidth()/2);
                fillPellet.setY(getY() + thisWidth/2 - fillPellet.getWidth()/2);

            }
            else if(!fillPelletIn && laserEnd.getWidth() > 0)
            {

                laserEnd.setX(getX() + thisWidth/2 -laserEnd.getWidth()/2);
                laserEnd.setY(getY() + thisWidth/2 - laserEnd.getWidth()/2);
                fillPellet.setX(getX() + thisWidth/2 - fillPellet.getWidth()/2);
                fillPellet.setY(getY() + thisWidth/2 - fillPellet.getWidth()/2);

                laserEnd.setSize(laserEnd.getWidth() - thisWidth*delta*1.3f, laserEnd.getWidth() - thisWidth*delta*1.3f);
                fillPellet.setSize(laserEnd.getWidth()-(laserEnd.getWidth()/(128/25)), laserEnd.getHeight()-(laserEnd.getHeight()/(128/25)));
            }
            else{drawEnd = false;}

        }


        if(fadeIn || fadeOut)
        {
            if(fadeIn)
            {
                if(laserEnd.getColor().a <0.95f)
                {
                    laserEnd.setColor(laserEnd.getColor().r, laserEnd.getColor().g, laserEnd.getColor().b, laserEnd.getColor().a + 1 * delta * 8 / 4.0f);
                    fillPellet.setColor(fillPellet.getColor().r, fillPellet.getColor().g, fillPellet.getColor().b, fillPellet.getColor().a + 1 * delta * 8 / 4.0f);
                }
                else
                {
                    laserEnd.setColor(laserEnd.getColor().r, laserEnd.getColor().g, laserEnd.getColor().b, 1f);
                    fillPellet.setColor(fillPellet.getColor().r, fillPellet.getColor().g, fillPellet.getColor().b, 1f);
                    fadeIn = false;
                    fadeOut = false;
                }
            }
            else
            {
                if(laserEnd.getColor().a >= 0.39f)
                {
                    laserEnd.setColor(laserEnd.getColor().r, laserEnd.getColor().g, laserEnd.getColor().b, laserEnd.getColor().a - 1 * delta * 15 / 4.0f);
                    fillPellet.setColor(fillPellet.getColor().r, fillPellet.getColor().g, fillPellet.getColor().b, fillPellet.getColor().a - 1 * delta * 15 / 4.0f);
                }
                else
                {
                    laserEnd.setColor(laserEnd.getColor().r, laserEnd.getColor().g, laserEnd.getColor().b, Toggle.fadealpha);
                    fillPellet.setColor(fillPellet.getColor().r, fillPellet.getColor().g, fillPellet.getColor().b, Toggle.fadealpha);
                    fadeIn = false;
                    fadeOut = false;
                }
            }
        }

        if(pelletlight !=null && pelletlight.getDistance() > 0)
        {
            pelletlight.setDistance(pelletlight.getDistance() - Pellet.lightSize*delta*2);
        }

        if(remove){hitCircNull = true; remove = false;}

        if(actCount<5){defineBody();}

        calculateTextDims();

        super.act(delta);
    }

    private PointLight pelletlight = null;

    @Override
    public void handleCollision(Pellet pellet)
    {
        if(laserEnd.getColor().a > Toggle.fadealpha && fillPelletIn)
        {
            if (needsMorePellets)
            {
                pelletNum--;
            }
            else
            {
                setFull(true);
            }

            if (pelletNum == 0 && needsMorePellets)
            {
                setFull(true);
            }

            pellet.removeLight = false;
            pellet.remove = true;
            pellet.justInteracted = true;
            pellet.collided = true;

            this.pelletlight = pellet.pelletLight;
        }
    }

    private boolean fadeIn = false;
    private boolean fadeOut = false;

    @Override
    public void actToggle()
    {
        if (toggleAction.equals("disable") && laserEnd.getColor().a < Toggle.fadealpha+0.01f)
        {
            fadeIn = true;
            fadeOut = false;
        }
        else if (toggleAction.equals("disable") && laserEnd.getColor().a >=0.99f)
        {
            fadeOut = true;
            fadeIn = false;
        }
    }

    @Override
    public String getUserData()
    {
        return "e"+id;
    }

    @Override
    public void setFull(boolean full)
    {
        if(full)
        {
            remove = true;
            this.full = true;
            if(areAllEndPortalsFull() && finalEndSelected == -1123451){finalEndSelected = id;}
        }
    }


    @Override
    public void resize() {

    }

    @Override
    public float getPosX(){return getX();}

    @Override
    public float getPosY(){return getY();}


    public float getWidth(){return laserEnd.getWidth();}


    public float getHeight(){return laserEnd.getHeight();}

    public boolean areAllEndPortalsFull()
    {
        for(int i = 0; i< PlayScreen.lmg.getCurrentlvl().getPelletEnd().length; i++)
        {
            if(!PlayScreen.lmg.getCurrentlvl().getPelletEnd()[i].isFull()){return false;}
        }
        return true;
    }

    //Method that creates a scaled Sprite (specifically a pellet)
    public Sprite createScaledPellet(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize((sprite.getWidth() / PlayScreen.PPM/SCALE)/2f,
        (sprite.getHeight() / PlayScreen.PPM/SCALE)/2f);
        return sprite;
    }

    float textWidth = 0;
    float textHeight = 0;
    public void calculateTextDims()
    {
        GlyphLayout layout = new GlyphLayout(PelletGame.font44, pelletNum+"");
        textWidth = layout.width;
        textHeight = layout.height;
    }


    @Override
    public boolean isFull() {
        return this.full;
    }

    @Override
    public float getFillPelletRadius() {
        return fillPellet.getHeight()/2;
    }

    @Override
    public void destroyBody(World world)
    {
        world.destroyBody(hitCirc);
    }

    @Override
    public void setInitialized(boolean inited) {}

    @Override
    public String getType()
    {
        return "pelletEnd";
    }

    @Override
    public float getSpawnX(char dir) {
        return 0;
    }

    @Override
    public float getSpawnY(char dir) {
        return 0;
    }

    @Override
    public void setLocked(boolean isLocked) {

    }

    @Override
    public void dispose() {}

    @Override
    public Sprite getSprite()
    {
        if(isFull())
        {return null;}
        return laserEnd;
    }

    @Override
    public void setHit(int hit) {}

}
