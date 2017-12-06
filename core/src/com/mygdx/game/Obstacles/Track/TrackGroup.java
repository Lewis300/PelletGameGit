package com.mygdx.game.Obstacles.Track;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.Obstacles.Obstacle;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by Lewis on 10/7/2017.
 */


public class TrackGroup
{
    private TrackPoint[] points = null;
    private int linkedObId;
    private Obstacle linkedOb = null;
    public Color color = null;
    public boolean tappedFirst = false;

    public TrackGroup(TrackPoint[] points, int ob, Color color)
    {
        this.points = points;
        linkedObId = ob;
        //System.out.println(linkedObId);
        this.color = color;

        for(int i = 0; i<points.length; i++)
        {
            this.points[i].setPointColor(color);
            this.points[i].setGroup(this);
        }


    }

    public void create()
    {
        linkedOb = findLinkedOb(linkedObId);
        linkedOb.setDrawColor(color);

        if(!linkedOb.equals(findLinkedOb(linkedObId))){linkedOb = null; linkedOb = findLinkedOb(linkedObId);}
    }

    private Obstacle findLinkedOb(int obId)
    {

        if(linkedOb == null)
        {

            for (int i = 0; i < PlayScreen.lmg.getCurrentlvl().getObstacles().length; i++)
            {
                if (PlayScreen.lmg.getCurrentlvl().getObstacles()[i].getId() == obId)
                {
                    linkedOb = PlayScreen.lmg.getCurrentlvl().getObstacles()[i];

                    linkedOb.setX(getRandOffScreenX());
                    linkedOb.setY(getRandOffScreenY());

                    break;

                }
            }
            return linkedOb;
        }
        else{return linkedOb;}
    }

    public Obstacle getObstacle(){return findLinkedOb(linkedObId);}

    public void setAllPointsHasOnTop(boolean b)
    {
        for (int i = 0; i< points.length; i++)
        {
            points[i].setHasObjectOnTop(b);
            if(b)
            {
                points[i].setTouchable(Touchable.disabled);
            }
            else
            {
                points[i].setTouchable(Touchable.enabled);
            }

            //points[i].openTime = 0;
        }

    }

    public boolean isObstacleMoving()
    {
        return getObstacle().isMovingToPoint();
    }

    public TrackPoint[] getPoints(){return points;}

    public void sendObjectToRandomPoint()
    {
        //Sending object to a random point
        int location;
        while(true)
        {
            location = (int)(Math.random()*this.points.length);
            if(this.points[location].allowTouch())
            {
                this.points[location].setTouched(true);
                break;
            }
        }

    }

    private float getRandOffScreenY()
    {
        if((int)(Math.random()*2) == 1)
        {
            return PlayScreen.gameport.getWorldHeight() + (float)((PlayScreen.gameport.getWorldHeight()/2)*Math.random());
        }
        else
        {
            return - (float)((PlayScreen.gameport.getWorldHeight()/2)*Math.random());
        }
    }

    private float getRandOffScreenX()
    {
       if((int)(Math.random()*2) == 1)
        {
            return PlayScreen.gameport.getWorldWidth() + (float)((PlayScreen.gameport.getWorldWidth()/2)*Math.random());
        }
        else
        {
            return - (float)((PlayScreen.gameport.getWorldWidth()/2)*Math.random());
        }
    }

}
