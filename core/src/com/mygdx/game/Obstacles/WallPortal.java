package com.mygdx.game.Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;

import box2dLight.ConeLight;

/**
 * Created by Lewis on 9/18/2017.
 */


public class WallPortal extends Obstacle
{
    //Obstacle properties
    //private int id= -1;
    private int linkedId = -1;
    private char side = 'x';
    private char fireDirection = 'x';
    private float coneDegree = 40;

    private boolean close = false;
    private boolean open = true;

    //Box2D
    private Body hitbody;
    private ConeLight light;

    public WallPortal(int id, int linkedId, char side)
    {
        makeId(id);
        this.linkedId = linkedId;
        this.side = side;

        int angle = 0;
        if(side == 't'){angle = 270; fireDirection = 'd';}
        else if(side == 'r'){angle = 180; fireDirection = 'l';}
        else if(side == 'b'){angle = 90; fireDirection = 'u';}
        else if(side == 'l'){angle = 0; fireDirection = 'r';}

        light = new ConeLight(PlayScreen.rayHandler, 15, Color.WHITE, 200, 0, 0, angle, coneDegree);

    }


    private boolean isPrimaryPortal = false;
    public WallPortal(int id, int linkedId, char side, int toggleId, String action)
    {
        this.id = id;
        this.linkedId = linkedId;
        this.side = side;

        setToggleId(toggleId);

        isPrimaryPortal = true;

        int angle = 0;
        if(side == 't'){angle = 270; fireDirection = 'd';}
        else if(side == 'r'){angle = 180; fireDirection = 'l';}
        else if(side == 'b'){angle = 90; fireDirection = 'u';}
        else if(side == 'l'){angle = 0; fireDirection = 'r';}


        if(action.equals("disable"))
        {
            open = false;
            close = true;
        }
        light = new ConeLight(PlayScreen.rayHandler, 5, Color.WHITE, 200, 0, 0, angle, coneDegree);
    }

    private int defineCount = 0;
    @Override
    public void defineBody()
    {


        if(hitbody != null){
            PlayScreen.world.destroyBody(hitbody);}

        defineCount++;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        CircleShape poly = new CircleShape();

        poly.setRadius(3);
        if(side == 't')
        {
            poly.setPosition(new Vector2(getX(), PlayScreen.gameport.getWorldHeight() + Pellet.getWidth()*3.4f));
        }
        else if(side == 'r')
        {
            poly.setPosition(new Vector2(PlayScreen.gameport.getWorldWidth()+ Pellet.getWidth()*3.4f, getY()));
        }
        else if(side == 'b')
        {
            poly.setPosition(new Vector2(getX(), - Pellet.getWidth()*3.4f));
        }
        else if(side == 'l')
        {
            poly.setPosition(new Vector2(0 - Pellet.getWidth()*3.4f, getY()));
        }

        poly.setPosition(light.getPosition());

        //poly.setPosition(new Vector2(poly.getPosition().x+40, poly.getPosition().y));
        bdef.type = BodyDef.BodyType.StaticBody;
        fdef.shape = poly;

        hitbody = PlayScreen.world.createBody(bdef);
        hitbody.createFixture(fdef).setUserData(getUserData());

        ////System.out.println(getUserData());

    }

    private int initialized;
    @Override
    public void act(float delta)
    {

        if(initialized < 5)
        {
            if(side == 't')light.setPosition(getX() + getWidth()/2, PlayScreen.gameport.getWorldHeight() + PlayScreen.gameport.getWorldWidth()/(PlayScreen.scale*6));
            else if(side == 'r')light.setPosition(PlayScreen.gameport.getWorldWidth() + PlayScreen.gameport.getWorldWidth()/(PlayScreen.scale*6), getY() + getHeight()/2);
            else if(side == 'b')light.setPosition(getX() + getWidth()/2, -PlayScreen.gameport.getWorldWidth()/(PlayScreen.scale*6));
            else if(side == 'l')light.setPosition(-PlayScreen.gameport.getWorldWidth()/(PlayScreen.scale*6), getY() + getHeight()/2);

            defineBody();

            initialized++;
        }

        if(isPrimaryPortal)
        {
            if(close)
            {
                open = false;
                getOtherPortal().close = true;
                getOtherPortal().open = false;
            }
            if(open)
            {
                close = false;
                getOtherPortal().close = false;
                getOtherPortal().open = true;
            }
        }

        if(close){closePortal(delta);}
        else if(open){openPortal(delta);}


        if(defineCount<5)defineBody();


        super.act(delta);
    }


    @Override
    public void handleCollision(Pellet pellet)
    {
        if(open && !pellet.justInteracted)
        {
            if(getOtherPortal().open)
            {
                pellet.setPosition(getOtherPortal().getSpawnX(), getOtherPortal().getSpawnY());
                pellet.direction = getOtherPortal().fireDirection;
            }

            pellet.justInteracted = true;
        }
    }

    private void closePortal(float delta)
    {
        if(light.getConeDegree()>15)light.setConeDegree(light.getConeDegree() - coneDegree*delta*5.5f);
        else{light.setConeDegree(15);}
    }

    private void openPortal(float delta)
    {
        if(light.getConeDegree()<coneDegree)light.setConeDegree(light.getConeDegree() + coneDegree*delta*5.5f);
        else{light.setConeDegree(coneDegree);}
    }

    @Override
    public Sprite createScaledSprite(Texture texture) {
        return null;
    }

    @Override
    public String getUserData() {
        return "t"+id;
    }

    @Override
    public float getPosX() {return getX();}

    @Override
    public float getPosY() {
        return getY();
    }

    float spawnX = -1;
    float spawnY = -1;
    public float getSpawnX()
    {
        if(spawnX == -1) {
            if (side == 'r') {
                spawnX = PlayScreen.gameport.getWorldWidth() + Pellet.getWidth();
            } else if (side == 'l') {
                spawnX = -Pellet.getWidth();
            } else if (side == 'b') {
                spawnX = getX() + getWidth()/2 - Pellet.getWidth() / 2;
            } else if (side == 't') {
                spawnX = getX()  + getWidth()/2 - Pellet.getWidth() / 2;
            }
        }
        return spawnX;
    }

    public float getSpawnY()
    {
        if(spawnY == -1) {
            if (side == 'r') {
                spawnY = getY() + getHeight()/2 - Pellet.getWidth() / 2;
            } else if (side == 'l') {
                spawnY = getY() + getHeight()/2 - Pellet.getWidth() / 2;
            } else if (side == 'b') {
                spawnY = 0 - Pellet.getWidth() / 2;
            } else if (side == 't') {
                spawnY = PlayScreen.gameport.getWorldHeight();
            }
        }
        return spawnY;
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

    }

    @Override
    public void setInitialized(boolean inited) {

    }

    @Override
    public void resize() {

    }

    @Override
    public String getType() {
        return "portal";
    }

    @Override
    public void actToggle()
    {

        if (close)
        {
            close = false;
            open = true;

            getOtherPortal().close = false;
            getOtherPortal().open = true;
        }
        else if (open)
        {
            close = true;
            open = false;

            getOtherPortal().close = true;
            getOtherPortal().open = false;
        }

    }

    @Override
    public void setLocked(boolean isLocked) {

    }

    private WallPortal otherPortal = null;
    public WallPortal getOtherPortal()
    {
    //System.out.println(id);System.out.println();
       if(otherPortal == null)
        {
            for(int i = 0; i< PlayScreen.lmg.getCurrentlvl().getObstacles().length; i++)
            {

                //System.out.println(PlayScreen.lmg.getCurrentlvl().getObstacles()[i].getId());
                if (PlayScreen.lmg.getCurrentlvl().getObstacles()[i].getId() == linkedId)
                {
                    otherPortal = (WallPortal) PlayScreen.lmg.getCurrentlvl().getObstacles()[i];
                    break;
                }
            }
        }
        return otherPortal;
    }

    @Override
    public void dispose() {

    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public void setHit(int hit) {

    }
}
