package com.mygdx.game.Obstacles;

import com.badlogic.gdx.Gdx;
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

import java.util.ArrayList;


/**
 * Created by Lewis on 9/12/2017.
 */

public class Toggle extends Obstacle
{
    public static float fadealpha = 0.15f;

    private int initializedCount = 0;

    //Toggle properties
    public boolean isOn = false;
    //private int id = -1;
    private int linkedId = -1;
    private float SCALE = 1;
    private Color currentColor = null;
    private boolean isButton = false;

    //Collision vars
    private char hitDir;
    private float hittime = 0f;
    private boolean isChanging = false;
    private int pelletId = -1;

    //texture(s)
    private static Texture buttonTex = Reusables.toggleTexture;
    private Sprite buttonSprite;
    
    //Box2D
    private Body buttonBody;
    private CircleShape shape;



    public Toggle(int id, int linkedId, boolean isOn, boolean isButton)
    {
        makeId(id);
        this.linkedId = linkedId;
        this.isOn = isOn;
        this.SCALE = PlayScreen.scale;
        this.isButton = isButton;
        initializedCount = 0;

        centerObstacleAtAllTimes = false;

        buttonSprite = createScaledSprite(buttonTex);
        if(isOn)currentColor = Color.GREEN;
        else{currentColor = Color.RED;}
        buttonSprite.setColor(currentColor);
        buttonSprite.setOriginCenter();

        shape = new CircleShape();

    }


    public Toggle(int id, boolean isOn, boolean isButton)
    {
        makeId(id);
        this.isOn = isOn;
        this.SCALE = PlayScreen.scale;
        this.isButton = isButton;
        initializedCount = 0;

        centerObstacleAtAllTimes = false;

        buttonSprite = createScaledSprite(buttonTex);
        if(isOn)currentColor = Color.GREEN;
        else{currentColor = Color.RED;}
        buttonSprite.setColor(currentColor);
        buttonSprite.setOriginCenter();

        shape = new CircleShape();
    }

    private BodyDef bdef = new BodyDef();
    private FixtureDef fdef = new FixtureDef();
    @Override
    public void defineBody() 
    {
        buttonSprite.setX(getX() + getWidth()/2 - buttonSprite.getWidth()/2);
        buttonSprite.setY(getY()+ getHeight()/2 - buttonSprite.getHeight()/2);

        if(buttonBody!=null){
            PlayScreen.world.destroyBody(buttonBody);}

        shape.setPosition(new Vector2(buttonSprite.getX()+buttonSprite.getHeight()/2, buttonSprite.getY()+buttonSprite.getHeight()/2));
        shape.setRadius(buttonSprite.getHeight()/2);

        bdef.type = BodyDef.BodyType.KinematicBody;
        buttonBody = PlayScreen.world.createBody(bdef);

        fdef.shape = shape;

        buttonBody.createFixture(fdef).setUserData(getUserData());
    }

    @Override
    public void act(float delta)
    {

        if(linkedObs == null)
        {
            linkedObs = new ArrayList<Obstacle>();

            for(int i = 0; i< PlayScreen.lmg.getCurrentlvl().getObstacles().length; i++)
            {
                if(PlayScreen.lmg.getCurrentlvl().getObstacles()[i].getToggleId() == id)
                {
                    linkedObs.add(PlayScreen.lmg.getCurrentlvl().getObstacles()[i]);
                }
            }
        }

        if(bodyChanged)timeSinceBodyChange+=delta;
        if(isChanging)
        {
            hittime += delta;
            calculateBump(Gdx.graphics.getDeltaTime());

            if (hittime > 0.2f)
            {
                isChanging = false;
                hittime = 0f;
                buttonSprite.setX(getX() + getWidth()/2 - buttonSprite.getWidth() / 2);
                buttonSprite.setY(getY() + getHeight()/2 - buttonSprite.getHeight() / 2);
                if(onTrack)calculateBody();
            }
            buttonSprite.setColor(currentColor);
        }

        if(isMovingToPoint() && onTrack)
        {calculateBody();}

        if(initializedCount<=2)defineBody();
        initializedCount++;
        super.act(delta);

    }

    private int fixtureCount;
    private Vector2 position = new Vector2();
    private void calculateBody()
    {
        buttonSprite.setX(getX() + buttonSprite.getWidth()/4);
        buttonSprite.setY(getY() + buttonSprite.getHeight()/4);
        position.set(buttonSprite.getX(), buttonSprite.getY());
        shape.setPosition(position);

        fdef.shape = shape;

        try
        {

            fixtureCount = buttonBody.getFixtureList().size;
            for (int i = 0; i < fixtureCount; i++) {
                buttonBody.destroyFixture(buttonBody.getFixtureList().get(i));
            }
            buttonBody.createFixture(fdef).setUserData(getUserData());

        }
        catch (Exception e){}
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        buttonSprite.draw(batch);

        if(isButton)
        {
            if (drawBox)
            {
                drawBoudingBox(Gdx.graphics.getDeltaTime(), batch,
                        getX(), getY(),
                        buttonSprite.getWidth(), buttonSprite.getHeight(),
                        true);
            }
            else if (drawBoxOut)
            {

                drawBoudingBox(Gdx.graphics.getDeltaTime(), batch,
                        getX(), getY(),
                        buttonSprite.getWidth(), buttonSprite.getHeight(),
                        false);
            }

        }
        super.draw(batch, parentAlpha);
    }

    private boolean drawBox = false;
    private boolean drawBoxOut = false;

    private ArrayList<Obstacle> linkedObs;
    @Override
    public void handleCollision(Pellet pellet)
    {
        if(pellet.id.equals(pelletId+"") && bodyChanged && timeSinceBodyChange<0.3){}
        else if(hittime == 0f && !drawBox)
        {

            for(int i = 0; i<linkedObs.size(); i++)
            {
                linkedObs.get(i).actToggle();
            }

            isChanging = true;

            hitDir = pellet.direction;
            pellet.direction = pellet.getOppositeDirection();

            if(isOn)
            {
                isOn = false;
                currentColor = Color.RED;
            }
            else
            {
                isOn = true;
                currentColor = Color.GREEN;
            }
            if(isButton)
            {
                if (drawBox) {setLocked(false);}
                else{setLocked(true);}
            }
        }
        else
        {
            if(pellet.direction == 'u'){pellet.direction = 'd';}
            else if(pellet.direction == 'd'){pellet.direction = 'u';}
            else if(pellet.direction == 'l'){pellet.direction = 'r';}
            else if(pellet.direction == 'r'){pellet.direction = 'l';}
        }


        pelletId = Integer.parseInt(pellet.id);
        pellet.collided = true;
        pellet.justInteracted = true;

    }

    private void calculateBump(float delta)
    {
        float extra = 1;

        if (hittime < 0.10f)
        {
            if (hitDir == 'u')
            {
                buttonSprite.setY((int) (buttonSprite.getY() + Pellet.speed * delta*extra));
                //pellet.direction = 'd';
            }
            else if (hitDir == 'd')
            {
                buttonSprite.setY((int) (buttonSprite.getY() - (Pellet.speed * delta*extra)/2));
                //pellet.direction = 'u';
            }
            else if (hitDir == 'l')
            {
                buttonSprite.setX((int) (buttonSprite.getX() - (Pellet.speed * delta*extra)/2));
               // pellet.direction = 'r';
            }
            else if (hitDir == 'r')
            {
                buttonSprite.setX((int) (buttonSprite.getX() + Pellet.speed * delta*extra));
               // pellet.direction = 'l';
            }

        }
        else if(hittime<0.20)
        {
            float origX;
            float origY;

           if(onTrack)
           {
                origX = position.x - buttonSprite.getWidth() / 2;
                origY = position.y - buttonSprite.getHeight() / 2;
           }
           else
           {
               origX = getX() + getWidth()/2 - buttonSprite.getWidth() / 2;
               origY = getY() + getWidth()/2 - buttonSprite.getHeight() / 2;
           }

            if (buttonSprite.getY() > origY)
            {
                buttonSprite.setY((int) (buttonSprite.getY() - Pellet.speed * delta*extra));
                //pellet.direction = 'u';
            }
            if (buttonSprite.getY() < origY)
            {
                buttonSprite.setY((int) (buttonSprite.getY() + Pellet.speed * delta*extra));
                //pellet.direction = 'd';
            }
            if (buttonSprite.getX() > origX)
            {
                buttonSprite.setX((int) (buttonSprite.getX() - Pellet.speed * delta*extra));
                //pellet.direction = 'r';
            }
            if (buttonSprite.getX() < origX)
            {
                buttonSprite.setX((int) (buttonSprite.getX() + Pellet.speed * delta*extra));
                //pellet.direction = 'l';
            }
        }
    }

    @Override
    public String getUserData() {
        return "b"+this.id;
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
    public void setFull(boolean full) {

    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void destroyBody(World world)
    {
        if(buttonBody != null)world.destroyBody(buttonBody);
        if(bodyChanged){world.destroyBody(bigCirc);}
    }

    @Override
    public void setInitialized(boolean inited) {

    }

    @Override
    public void resize() {

    }

    @Override
    public String getType()
    {
        return "toggle";
    }

    @Override
    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize((sprite.getWidth() / PlayScreen.PPM/SCALE),
        (sprite.getHeight() / PlayScreen.PPM/SCALE));
        return sprite;
    }

    @Override
    public void actToggle()
    {

    }

    @Override
    public void setLocked(boolean isLocked)
    {
        if(isLocked)
        {
            drawBox = true;
            drawBoxOut = false;

            PlayScreen.bodiesNeedToChange.add(this);
        }
        else{drawBox = false; drawBoxOut = true;}
    }


    private boolean bodyChanged = false;
    private float timeSinceBodyChange = 0f;
    private Body bigCirc;
    public void changeBody()
    {
        if(isButton)
        {

            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bigCirc = PlayScreen.world.createBody(bdef);

            //CircleShape shape = new CircleShape();
            shape.setRadius((buttonSprite.getWidth() * 1.5f) / 2 + (PlayScreen.pellets.get(0).pellet.getWidth() / 2f));

            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;

            bigCirc.createFixture(fdef).setUserData(getUserData());
            bodyChanged = true;
        }
    }

    public boolean on()
    {
        return isOn;
    }

    @Override
    public void dispose() {
        //buttonTex.dispose();
    }

    @Override
    public Sprite getSprite() {
        return buttonSprite;
    }

    @Override
    public void setHit(int hit) {

    }
}
