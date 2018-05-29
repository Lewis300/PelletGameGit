package com.mygdx.game.PelletStuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.Screens.PlayScreen;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import static com.mygdx.game.Screens.PlayScreen.lmg;
import static com.mygdx.game.Screens.PlayScreen.rayHandler;

/**
 * Created by Lewis on 8/5/2017.
 */

public class Pellet implements Pool.Poolable
{
    public static double speed = PlayScreen.gameport.getWorldWidth()/2.4*2;
    public String id = "0";

    private static float SCALE = lmg.getScale();

    public Sprite pellet;
    private static Texture pelletTex = new Texture("pellet.png");

    public float x, y;

    public char direction;

    public boolean remove = false;
    private boolean hitEnd = false;

    private double lifeTime;

    private CircleShape circ;
    private Body circBody;

    public PointLight pelletLight = null;
    public boolean hasLight = false;
    public boolean removeLight = false;

    public boolean collided = false;
    public boolean justInteracted = false;
    private float interactTime = 0;

    public Pellet(float x, float y, char direction)
    {
        create(x, y, direction);
        RayHandler.useDiffuseLight(false);
    }

    public static float lightSize = 140f*3 / PlayScreen.PPM / SCALE;
    public Pellet(float x, float y, char direction, boolean hasLight)
    {
        this.hasLight = hasLight;
        create(x, y, direction);

        if(hasLight)
        {
            RayHandler.useDiffuseLight(true);
            pelletLight = new PointLight(PlayScreen.rayHandler, 20, Color.WHITE, 0, x, y);
            pelletLight.setSoft(false);
            pelletLight.setXray(true);
//            speed = PlayScreen.gameport.getWorldWidth()/4.8;
            removeLight = false;
        }
        else
        {
            RayHandler.useDiffuseLight(false);
        }
    }

    public void create(float x, float y, char direction)
    {
        //Set position
        this.x = x;
        this.y = y;

        SCALE = PlayScreen.scale;

        //Create pellet Sprite
        pellet = createScaledSprite(pelletTex);
        pellet.setX(x-pellet.getHeight()/2);
        pellet.setY(y-pellet.getHeight()/2);

        //Create Box2D body
        this.direction = direction;
        circ = new CircleShape();

        speed = PlayScreen.gameport.getWorldWidth()/2.4;

        defineBody();
    }

    public void update(float dt)
    {
        //Check if the pellet is allowed to interact with other Obstacles
        lifeTime+=dt;
        if(justInteracted){interactTime+=dt;}
        //42.5/speed/ PlayScreen.PPM
        if(interactTime>=0){justInteracted = false; interactTime = 0;}

        if(y - pellet.getHeight()*5 > PlayScreen.gameport.getWorldHeight()*1.5 || y<-PlayScreen.gameport.getWorldHeight()*1.5-pellet.getHeight()*5 || x - pellet.getWidth()*20 > PlayScreen.gameport.getWorldWidth()*1.5 || x<-PlayScreen.gameport.getWorldWidth()*1.5-pellet.getWidth()*5){remove = true; removeLight = true;}

        //Update position
        if(!remove && !hitEnd)
        {
            this.x = pellet.getX();
            this.y = pellet.getY();

            if (direction == 'u'){this.y+= speed * dt;}
            else if (direction == 'd'){this.y-= speed * dt;}
            else if (direction == 'l'){this.x-=speed * dt;}
            else if (direction == 'r') {this.x+= speed * dt;}

            pellet.setX(x);
            pellet.setY(y);

            if(hasLight)
            {
                pelletLight.setPosition(x + (pellet.getWidth()/2),y + (pellet.getHeight()/2));
            }

            defineBody();
        }

        if(pelletLight != null && pelletLight.getDistance() < lightSize)
        {
            pelletLight.setDistance(pelletLight.getDistance() + lightSize*dt*2);

            if(pelletLight.getDistance() >= lightSize){pelletLight.setDistance(lightSize);}
        }
    }
    //Method that creates and defines a Box2D body
    public void defineBody()
    {

        if(circBody!=null){
            PlayScreen.world.destroyBody(circBody);}

        circ.setPosition(new Vector2(pellet.getX()+pellet.getHeight()/2, pellet.getY()+pellet.getHeight()/2));
        circ.setRadius(((pellet.getHeight()*SCALE)/2)/15);

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        circBody = PlayScreen.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = circ;

        circBody.createFixture(fdef).setUserData("p"+this.id+""+direction);

    }

    //Method that creates a scaled Sprite
    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize((sprite.getWidth() / PlayScreen.PPM/SCALE)/2f,
        (sprite.getHeight() / PlayScreen.PPM/SCALE)/2f);
        return sprite;
    }

    public void render(SpriteBatch batch, float parentAlpha)
    {
        pellet.draw(batch);
    }

    public Body getBody()
    {
        return circBody;
    }

    //This method would be used in the case the pellet object are recycled
    //This version of the project instead destroys the pellet object and makes a new one
    @Override
    public void reset()
    {
        id = "0";

        SCALE = lmg.getScale();

        remove = false;
        hitEnd = false;

        collided = false;
        justInteracted = false;
        interactTime = 0;
    }

    public void setPosition(float xe, float ye)
    {
        pellet.setX(xe);
        pellet.setY(ye);
    }

    public static float getWidth(){return (pelletTex.getWidth()/ PlayScreen.PPM/SCALE)/2f;}

    public char getOppositeDirection()
    {
        if(direction == 'u'){return 'd';}
        else if(direction == 'd'){return 'u';}
        else if(direction == 'l'){return 'r';}
        else if(direction == 'r'){return 'l';}

        return 'z';
    }
}
