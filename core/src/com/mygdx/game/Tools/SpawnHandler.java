package com.mygdx.game.Tools;

import com.mygdx.game.Obstacles.Pellet_Start;
import com.mygdx.game.PelletStuff.Pellet;

import java.util.ArrayList;

/**
 * Created by Lewis on 9/29/2017.
 */

public class SpawnHandler
{
    //Dependancies
    private static ArrayList<Pellet> pellets;
    private static float spawnPause = 0;
    private static Pellet_Start[] starts;
    private static int fireCount = 0;
    private static float timePassed = 0;

    //If there are multiple starts to a level
    private static boolean multipleStarts = false;
    private static boolean shootSimultaneous = true;

    //If there are multiple fire directions
    private static boolean hasMultipleFireDirections = false;
    private static boolean waitBetweenFires = false;


    public static void set(Pellet_Start[] starts, ArrayList<Pellet> pellets, float spawnPause)
    {
        SpawnHandler.starts = starts;
        SpawnHandler.pellets = pellets;
        SpawnHandler.spawnPause = spawnPause;

        if(starts.length>1){multipleStarts = true;}
        for(int i = 0; i<starts.length; i++)
        {
            if(starts[i].fireDirections.length()>1){hasMultipleFireDirections = true;}
        }


    }

    private static int currentIndex = 0;
    public static void fire()
    {
        fireCount++;

        if(timePassed >= spawnPause)
        {
            if(multipleStarts)
            {
                if (shootSimultaneous)
                {
                    if (waitBetweenFires)
                    {
                        for (int i = 0; i < starts.length; i++)
                        {
                            starts[i].fire(pellets);
                        }
                    }
                    else
                    {
                        for (int i = 0; i < starts.length; i++)
                        {
                            for (int j = 0; j < starts[i].fireDirections.length(); j++)
                            {
                                starts[i].fire(pellets);
                            }
                        }
                    }
                }
                else
                {
                    currentIndex++;

                    if(currentIndex>=starts.length){currentIndex = 0;}

                    if(waitBetweenFires)
                    {
                        starts[currentIndex].fire(pellets);
                    }
                    else
                    {
                        for(int i = 0; i < starts[currentIndex].fireDirections.length(); i++)
                        {
                            starts[currentIndex].fire(pellets);
                        }
                    }
                }
            }
            else
            {
                if(hasMultipleFireDirections)
                {
                    //System.out.println("fag");
                    if(waitBetweenFires)
                    {
                        starts[0].fire(pellets);
                    }
                    else
                    {
                        for(int i = 0; i < starts[0].fireDirections.length(); i++)
                        {
                            starts[0].fire(pellets);
                        }
                    }
                }
                else
                {
                    starts[0].fire(pellets);
                }
            }

            timePassed = 0;
        }

    }

    public static void updateTimePassed(float delta){timePassed+=delta;}
    public static void setShootAllSimultaneously(boolean b){shootSimultaneous = b;}
    public static void setWaitBetweenFires(boolean b){waitBetweenFires = b;}

}
