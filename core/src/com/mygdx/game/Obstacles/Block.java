package com.mygdx.game.Obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.GameScreenManager;
import com.mygdx.game.Tools.Reusables;


/**
 * Created by Lewis on 8/12/2017.
 */

public class Block extends Obstacle
{
    private float SCALE = 1;

    private Sprite block;
    private static Texture blockTex = Reusables.blockTexture;


    //private int id;
    private int toggleId;

    private PolygonShape rect;
    private Body B2body;
    private int hit = 0;
    private double hittime = 0;
    private static String acceptedDirs = "udlr";

    private float dt;

    private float posX = 0;
    private float posY = 0;


    public Block(GameScreenManager gsm)
    {
        super(gsm);
        create();
        defineBody();
    }

    public Block(GameScreenManager gsm, int id)
    {
        super(gsm);
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
            block.setColor(block.getColor().r, block.getColor().g, block.getColor().b, Toggle.fadealpha);
        }

    }

    public void create()
    {

        //Set scale, id, and position
        SCALE = PlayScreen.scale;



        //Create the Sprite
        block = createScaledSprite(blockTex);
        block.setOriginCenter();
        block.setPosition(this.posX, this.posY);


        //Set Actor properties
        //setBounds(block.getX(), block.getY(),block.getWidth(),block.getHeight());
        setTouchable(Touchable.enabled);

        setOrigin(block.getOriginX(),block.getOriginY());
        rotateBy(block.getRotation());

        //Set points for Box2D rectangle
        float[] points = new float[8];
        points[0] = block.getX()+10f/PlayScreen.PPM/SCALE; points[1] = block.getY()+118f/PlayScreen.PPM/SCALE;
        points[2] = block.getX()+118f/PlayScreen.PPM/SCALE; points[3] = block.getY()+118f/PlayScreen.PPM/SCALE;
        points[4] = block.getX()+118f/PlayScreen.PPM/SCALE; points[5] = block.getY()+10f/PlayScreen.PPM/SCALE;
        points[6] = block.getX()+10f/PlayScreen.PPM/SCALE; points[7] = block.getY()+10f/PlayScreen.PPM/SCALE;

        //Create Box2D rectangle
        rect = new PolygonShape();
        rect.set(points);

        addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if(!gsm.getScreen().isInLvlMenu())
                {
                    hit++;
                    if(hit>2){hit = 2;}
                }
                return true;
            }
        });
    }

    //Method that creates a Box2D body
    public void defineBody()
    {
        if(B2body != null){PlayScreen.world.destroyBody(B2body);}


        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;
        B2body = PlayScreen.world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = rect;
        B2body.createFixture(fdef).setUserData(getUserData());


    }

    @Override
    public void act(float delta)
    {

        //Update position
        SCALE = PlayScreen.scale;
        //block.setX(centerX);
        //block.setY(centerY);
        dt = delta;
        if(hit>0)
        {
            positionChanged();
            if(hittime>(1.0/5.0))
            {
                hit--;
                hittime = 0;
                if(hit == 0)
                {
                    //Set rotation to the nearest multiple of 90
                    block.setRotation(90 * (Math.round(block.getRotation() / 90)));

                    if (block.getRotation() >= 360)
                    {
                        block.setRotation(((block.getRotation() - 360*(int)(block.getRotation()/360)) / 90) * 90);
                    }

                    //(block.getRotation());

                }

            }
        }
        
        if(fadeIn || fadeOut)
        {
            if(fadeIn)
            {
                if(block.getColor().a <0.95f)
                {
                    block.setColor(block.getColor().r, block.getColor().g, block.getColor().b, block.getColor().a + 1 * delta * 8 / 4.0f);
                }
                else
                {
                    block.setColor(block.getColor().r, block.getColor().g, block.getColor().b, 1f);
                    fadeIn = false;
                    fadeOut = false;
                    ////System.out.println("FAG");
                }
            }
            else
            {
                if(block.getColor().a >= 0.39f)
                {
                    block.setColor(block.getColor().r, block.getColor().g, block.getColor().b, block.getColor().a - 1 * delta * 15 / 4.0f);
                }
                else
                {
                    block.setColor(block.getColor().r, block.getColor().g, block.getColor().b, Toggle.fadealpha);
                    fadeIn = false;
                    fadeOut = false;
                }
            }
        }

        calculateBox();

        super.act(delta);
    }

    @Override
    protected void positionChanged()
    {
        //Rotate
        hittime+=dt;
        block.rotate(450f*dt);
        super.positionChanged();
    }

    private String toggleAction = "";
    @Override
    public void resize()
    {
        hit++;
        block.rotate(-90);
        //positionChanged();
    }

    public void calculateBox()
    {

        //Calculate location of Box2D rectangle and update the body
        float[] points = new float[8];
        points[0] = block.getX()+10f/PlayScreen.PPM/SCALE; points[1] = block.getY()+118f/PlayScreen.PPM/SCALE;
        points[2] = block.getX()+118f/PlayScreen.PPM/SCALE; points[3] = block.getY()+118f/PlayScreen.PPM/SCALE;
        points[4] = block.getX()+118f/PlayScreen.PPM/SCALE; points[5] = block.getY()+10f/PlayScreen.PPM/SCALE;
        points[6] = block.getX()+10f/PlayScreen.PPM/SCALE; points[7] = block.getY()+10f/PlayScreen.PPM/SCALE;

        rect.set(points);
        defineBody();
    }

    @Override
    public void handleCollision(Pellet pellet)
    {
        if(pellet.collided && block.getColor().a > Toggle.fadealpha)
        {
            if(acceptedDirs.equals("udlr"))
            {
                if(pellet.direction == 'u'){pellet.direction = 'd';}
                else if(pellet.direction == 'd'){pellet.direction = 'u';}
                else if(pellet.direction == 'l'){pellet.direction = 'r';}
                else if(pellet.direction == 'r'){pellet.direction = 'l';}
            }
            pellet.collided = false;

        }
    }
    
    private boolean fadeIn = false;
    private boolean fadeOut = false;
    
    @Override
    public void actToggle()
    {
        if (toggleAction.equals("disable") && block.getColor().a < Toggle.fadealpha+0.01f)
        {
            fadeIn = true;
            fadeOut = false;
        }
        else if (toggleAction.equals("disable") && block.getColor().a >=0.99f)
        {
            fadeOut = true;
            fadeIn = false;
        }
        else if(toggleAction.equals("enable") && block.getColor().a >=0.99f)
        {
            fadeOut = true;
            fadeIn = false;
        }
        else if (toggleAction.equals("enable") && block.getColor().a < Toggle.fadealpha+0.01f)
        {
            fadeIn = true;
            fadeOut = false;
        }

    }

    @Override
    public String getUserData()
    {
        return id+""+acceptedDirs;
    }

    //Method that creates a Sprite that is scaled
    @Override
    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize(sprite.getWidth() /PlayScreen.PPM/SCALE,
        sprite.getHeight() /PlayScreen.PPM/SCALE);
        return sprite;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        block.draw(batch);
    }

    @Override
    public float getPosX() {
        return getX();
    }

    @Override
    public float getPosY() {
        return getY();
    }

    @Override
    public float getWidth(){return block.getWidth();}

    @Override
    public float getHeight(){return block.getHeight();}

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
        if(B2body != null)world.destroyBody(B2body);
    }

    @Override
    public void setInitialized(boolean inited)
    {

    }

    @Override
    public String getType()
    {
        return "block";
    }

    @Override
    public void setLocked(boolean isLocked) {

    }

    @Override
    public void dispose() {
       // blockTex.dispose();
    }

    @Override
    public Sprite getSprite() {
        return block;
    }

    @Override
    public void setHit(int hit) {
        this.hit = hit;
    }
}
