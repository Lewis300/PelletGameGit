package com.mygdx.game.Tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.Obstacles.Block;
import com.mygdx.game.Obstacles.Toggle;
import com.mygdx.game.Obstacles.Mirror;
import com.mygdx.game.Obstacles.Obstacle;
import com.mygdx.game.Obstacles.Pellet_End;
import com.mygdx.game.Obstacles.Pellet_Start;
import com.mygdx.game.Obstacles.Track.TrackPoint;
import com.mygdx.game.Obstacles.WallPortal;
import com.mygdx.game.PelletGame;
import com.mygdx.game.Screens.PlayScreen;

import java.util.ArrayList;

/**
 * Created by Lewis on 10/19/2017.
 */

public class Utilities
{
    private static TmxMapLoader mapLoader = new TmxMapLoader();
    private static TiledMap map = new TiledMap();

    private static int getObstaclesFromTiledMapUseCount = 0;

    private static float scale = 1;
    private static float pelletPause = 1;
    private static float ambientLight = 0.95f;
    private static boolean shootSimultaneously = false;
    private static boolean waitBetweenFires = false;

    public static Level createLevel(String filename)
    {
        map = mapLoader.load(filename);


        ArrayList<Obstacle> obs = new ArrayList<Obstacle>();

        ArrayList<Color> groupColors = new ArrayList<Color>();
        ArrayList<Integer> groupNums = new ArrayList<Integer>();
        ArrayList<Integer> trackPointObjectIds = new ArrayList<Integer>();

        int width = 1;
        int height = 1;

        TiledMapTileLayer tlayer = (TiledMapTileLayer)map.getLayers().get("tilelayer");

        TiledMapTileLayer slayer = (TiledMapTileLayer)map.getLayers().get("settingslayer");
        MapProperties mapprops = slayer.getProperties();

        scale = Float.parseFloat(mapprops.get("scale", String.class));
        ambientLight = Float.parseFloat(mapprops.get("ambientlight", String.class));

        if(scale == 0){System.out.println("SCALE NOT SET: USING DEFAULT SCALE 2.00 "); scale = 2.0f;}
        if(ambientLight == 0){System.out.println("AMBIENT LIGHT NOT SET USING DEFAULT VAULE 0.95"); ambientLight = 0.95f;}

        PlayScreen.rayHandler.removeAll();
        for(int i = 0; i<map.getLayers().getCount(); i++)
        {
            MapLayer layer = map.getLayers().get(i);
            MapObjects objects = layer.getObjects();

            int mapWidth = tlayer.getWidth();
            int mapHeight = tlayer.getHeight();

            String layerName = layer.getName();
            width = mapWidth;
            height = mapHeight;

            TiledMapTileLayer.Cell cell;

            for(int j = 0; j<objects.getCount() && objects.getCount() > 0; j++)
            {
                MapObject obj = objects.get(j);
                MapProperties props = obj.getProperties();


                int rotation;
                if(props.get("rotation") == null)
                {
                    rotation = 0;
                }
                else
                {
                    rotation = (int)Float.parseFloat(props.get("rotation").toString());
                }


                int objX = (int)(obj.getProperties().get("x", float.class)/128);
                int objY = (int)(obj.getProperties().get("y", float.class)/128)+1;


                cell = tlayer.getCell(objX, objY);
                if(layer.getName().toLowerCase().equals("mirrors"))
                {
                    //This block figures out the rotation of the mirror
                    float rot = 0;

                    if(rotation == 0 || rotation == 360 || rotation == -360){rot = 0;}
                    else if(rotation == 90 || rotation == -270){rot = 270; objY--;}
                    else if(rotation == 180 || rotation == -180){rot = 180; objY--; objX--;}
                    else if(rotation == 270 || rotation == -90){rot = 90; objX--;}

                    //Check if it just has an id
                    if(props.containsKey("id"))
                    {
                        obs.add(new Mirror(Integer.parseInt(props.get("id").toString()), rot)
                        .setMapPos(objX, mapHeight-objY-1));
                    }
                    //Otherwise just make a regular mirror
                    else{obs.add(new Mirror(rot).setMapPos(objX, mapHeight-objY-1));}

                    //Check if mirror is linked to a toggle
                    if(props.containsKey("toggleid"))
                    {
                        ((Mirror)obs.get(obs.size()-1)).addToggle(
                                Integer.parseInt(props.get("toggleid").toString()),
                                props.get("startingaction", String.class)
                        );
                    }
                }
                else if(layerName.toLowerCase().contains("pelletend"))
                {
                    //Check if the Pellet_End has an id
                    if(props.containsKey("id"))
                    {
                        obs.add(new Pellet_End(Integer.parseInt(props.get("id").toString()))
                                .setMapPos(objX, mapHeight-objY-1));
                    }
                    //Otherwise create a regular Pellet_End
                    else{obs.add(new Pellet_End().setMapPos(objX, mapHeight-objY-1));}

                    //Check if the Pellet_End is linked to anything
                    if(props.containsKey("toggleid"))
                    {
                        ((Pellet_End)obs.get(obs.size()-1)).addToggle(
                                Integer.parseInt(props.get("toggleid").toString()),
                                props.get("startingaction", String.class)
                        );
                    }
                }
                else if(layerName.toLowerCase().contains("pelletstart"))
                {
                    //Check if the Pellet_Start has just an id
                    if(props.containsKey("id"))
                    {
                        if(!props.containsKey("fireallatonce"))
                        {
                            obs.add(new Pellet_Start(Integer.parseInt(props.get("id").toString()),
                                    props.get("firedirections", String.class),
                                    Float.parseFloat(props.get("initialdelay").toString()),
                                    Float.parseFloat(props.get("pelletpause").toString()),
                                    false)
                                    .setMapPos(objX, mapHeight-objY-1));
                        }
                        else
                        {
                            obs.add(new Pellet_Start(Integer.parseInt(props.get("id").toString()),
                                    props.get("firedirections", String.class),
                                    Float.parseFloat(props.get("initialdelay").toString()),
                                    Float.parseFloat(props.get("pelletpause").toString()),
                                    Boolean.parseBoolean(props.get("fireallatonce", String.class)))
                                    .setMapPos(objX, mapHeight-objY-1));
                        }
                    }
                    //Otherwise create a regular Pellet_Start
                    else
                    {
                        if(!props.containsKey("fireallatonce"))
                        {
                            obs.add(new Pellet_Start(
                                    props.get("firedirections", String.class),
                                    Float.parseFloat(props.get("initialdelay").toString()),
                                    Float.parseFloat(props.get("pelletpause").toString()),
                                    false)
                                    .setMapPos(objX, mapHeight-objY-1));
                        }
                        else
                        {
                            obs.add(new Pellet_Start(
                                    props.get("firedirections", String.class),
                                    Float.parseFloat(props.get("initialdelay").toString()),
                                    Float.parseFloat(props.get("pelletpause").toString()),
                                    Boolean.parseBoolean(props.get("fireallatonce", String.class)))
                                    .setMapPos(objX, mapHeight-objY-1));
                        }
                    }

                    //Check if the Pellet_Start is linked to a Toggle
                    if(props.containsKey("toggleid"))
                    {
                        ((Pellet_Start)obs.get(obs.size()-1)).addToggle(
                                Integer.parseInt(props.get("toggleid").toString()),
                                props.get("startingaction", String.class)
                        );
                    }
                }
                else if(layerName.toLowerCase().contains("block"))
                {
                    //Check if the block has an id
                    if(props.containsKey("id"))
                    {
                        obs.add(new Block(Integer.parseInt(props.get("id").toString())).setMapPos(objX, mapHeight-objY-1));
                    }
                    //Otherwise create a regular block
                    else{obs.add(new Block().setMapPos(objX, mapHeight-objY-1));}

                    //Check if the block is linked to a toggle
                    if(props.containsKey("toggleid"))
                    {
                        ((Block)obs.get(obs.size()-1)).addToggle(
                                Integer.parseInt(props.get("toggleid").toString()),
                                props.get("startingaction", String.class)
                        );
                    }
                }
                else if(layerName.toLowerCase().contains("toggle"))
                {
                    if(props.containsKey("linkedid"))
                    {
                        obs.add(new Toggle(
                                Integer.parseInt(props.get("id", String.class)),
                                Integer.parseInt(props.get("linkedid", String.class)),
                                Boolean.parseBoolean(props.get("startson", String.class)),
                                Boolean.parseBoolean(props.get("isbutton", String.class)))
                                .setMapPos(objX, mapHeight-objY-1));
                    }
                    else
                    {
                        obs.add(new Toggle(
                                Integer.parseInt(props.get("id").toString()),
                                Boolean.parseBoolean(props.get("startson", String.class)),
                                Boolean.parseBoolean(props.get("isbutton", String.class)))
                                .setMapPos(objX, mapHeight-objY-1));
                    }
                }
                else if(layerName.toLowerCase().contains("wallportal"))
                {
                    char side = props.get("side", String.class).charAt(0);
                    if(props.containsKey("toggleid"))
                    {
                        obs.add(new WallPortal(Integer.parseInt(props.get("id", String.class)),
                                Integer.parseInt(props.get("linkedid", String.class)),
                                side,
                                Integer.parseInt(props.get("toggleid", String.class)),
                                props.get("startingaction", String.class))
                        .setMapPos(objX, mapHeight-objY-1));
                    }
                    else
                    {
                        obs.add(new WallPortal(Integer.parseInt(props.get("id", String.class)),
                                Integer.parseInt(props.get("linkedid", String.class)),
                                side)
                                .setMapPos(objX, mapHeight-objY-1));
                    }

                    //Makes sure that if the WallPortal object is on the top, its y index is not -1
                    //if(side == 't'){obs.get(obs.size()-1).setMapPos(objX, 2);}

                    //System.out.println(obs.get(obs.size()-1).getMapY()+", "+obs.get(obs.size()-1).getMapX());
                }
                else if(layerName.toLowerCase().contains("trackpoint"))
                {
                    if(props.containsKey("groupcolor"))
                    {
                        if(props.get("groupcolor") != null)groupColors.add(Color.valueOf(props.get("groupcolor", String.class).substring(3)));
                    }
                    if(props.containsKey("linkedid"))
                    {
                        trackPointObjectIds.add(Integer.parseInt(props.get("linkedid", String.class)));
                    }
                    if(!groupNums.contains(Integer.parseInt(props.get("groupnum", String.class))))
                    {
                        groupNums.add(Integer.parseInt(props.get("groupnum", String.class)));
                    }

                    if(props.containsKey("toggleid"))
                    {
                        obs.add(new TrackPoint(Integer.parseInt(props.get("id", String.class)),
                                Integer.parseInt(props.get("groupnum", String.class)),
                                Integer.parseInt(props.get("toggleid", String.class)),
                                props.get("startingaction", String.class))
                                .setMapPos(objX, mapHeight - objY - 1));
                    }
                    else
                    {
                        obs.add(new TrackPoint(Integer.parseInt(props.get("id", String.class)),
                                Integer.parseInt(props.get("groupnum", String.class)))
                                .setMapPos(objX, mapHeight - objY - 1));
                    }
                }
            }
        }

        Obstacle[] obstacles = new Obstacle[obs.size()];

        for(int i = 0; i<obstacles.length; i++)
        {
            obstacles[i] = obs.get(i);
        }

        Level level = new Level(scale, ambientLight, shootSimultaneously, waitBetweenFires);
        level.createMap(width, height, obstacles);

        for(int i = 0; i<groupColors.size(); i++)
        {
            level.createTrackGroup(groupColors.get(i), trackPointObjectIds.get(i));
        }

        getObstaclesFromTiledMapUseCount++;

        return level;
    }

    public static float getScaleFromMap(String file)
    {
        map = mapLoader.load(file);

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("settingslayer");
        for(int i = 0; i<map.getLayers().getCount(); i++)
        {
            if(layer.getName().toLowerCase().contains("settings") &&  layer.getProperties().containsKey("scale"))
            {
                MapProperties props = layer.getProperties();
                return Float.parseFloat(props.get("scale", String.class));
            }
        }
        return 1;
    }
}
