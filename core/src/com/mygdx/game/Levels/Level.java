package com.mygdx.game.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Obstacles.Blank;
import com.mygdx.game.Obstacles.Mirror;
import com.mygdx.game.Obstacles.Obstacle;
import com.mygdx.game.Obstacles.Pellet_End;
import com.mygdx.game.Obstacles.Pellet_Start;
import com.mygdx.game.Obstacles.Track.TrackGroup;
import com.mygdx.game.Obstacles.Track.TrackPoint;
import com.mygdx.game.Obstacles.WallPortal;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.SpawnHandler;

import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.PointLight;

/**
 * Created by Lewis on 8/24/2017.
 */

public class Level
{
    private boolean hasSentObstaclesToPoints = false;
    public float getScale(){return scale;}

    public float getAmbientLight(){return ambientLight;}

    private Body[] box;

    private Obstacle[][] map = null;
    private Obstacle[] obstacles = null;

    private float scale = 1;
    private float ambientLight = 0.95f;

    private boolean waitBetweenFires = false;
    private boolean shootSimultaneously = false;

    public Level(){}

    public Level(float scale, float ambientLight, boolean shootSimultaneously, boolean waitBetweenFires)
    {
        this.scale = scale;
        this.ambientLight = ambientLight;
        this.shootSimultaneously = shootSimultaneously;
        this.waitBetweenFires = waitBetweenFires;
    }

    public void createMap(int width, int height, Obstacle[] obstacles)
    {
        this.obstacles = obstacles;
        if(map == null) {
            map = new Obstacle[height][width];

            for (int i = 0; i < obstacles.length; i++)
            {
                //System.out.println(obstacles[i].getMapX()+", "+obstacles[i].getMapY()+"      "+obstacles[i].getType());
                map[obstacles[i].getMapY()][obstacles[i].getMapX()] = obstacles[i];
            }
        }
    }

    public void createMap(int width, int height, ArrayList<Obstacle> obs)
    {
        obstacles = new Obstacle[obs.size()];
        for(int i = 0; i<obs.size(); i++)
        {
            obstacles[i] = obs.get(i);
        }

        if(map == null) {
            map = new Obstacle[height][width];

            for (int i = 0; i < obs.size(); i++)
            {
                map[obs.get(i).getMapY()][obs.get(i).getMapX()] = obs.get(i);
            }
        }

    }

    public void setShootSimultaneously(boolean b){shootSimultaneously = b;}
    public void setWaitBetweenFires(boolean b){waitBetweenFires = b;}

    public Obstacle[][] getMap(){return map;}

    public Obstacle[] getObstacles(){return obstacles;}

    private Pellet_Start[] starts = null;
    public Pellet_Start[] getPelletStart()
    {
        if(starts == null)
        {
            ArrayList<Pellet_Start> startArr = new ArrayList<Pellet_Start>();

            for (int i = 0; i < map.length; i++)
            {
                for (int j = 0; j < map[0].length; j++)
                {
                    if(map[i][j] !=null)
                    {
                        if (map[i][j].getType().equals("pelletStart"))
                        {
                            startArr.add((Pellet_Start)map[i][j]);
                        }
                    }
                }
            }

            starts = new Pellet_Start[startArr.size()];

            for (int i = 0; i < starts.length; i++)
            {
                starts[i] = startArr.get(i);
            }
            startArr.removeAll(startArr);

        }

        return starts;
    }

    private Pellet_End[] ends = null;
    public Pellet_End[] getPelletEnd()
    {
        if(ends == null)
        {
            ArrayList<Pellet_End> endArr = new ArrayList<Pellet_End>();

            for (int i = 0; i < map.length; i++)
            {
                for (int j = 0; j < map[0].length; j++)
                {
                    if(map[i][j] !=null)
                    {
                        if (map[i][j].getType().equals("pelletEnd"))
                        {
                            endArr.add((Pellet_End) map[i][j]);
                        }
                    }
                }
            }

            ends = new Pellet_End[endArr.size()];

            for (int i = 0; i < ends.length; i++)
            {
                ends[i] = endArr.get(i);
            }
            endArr.removeAll(endArr);

        }

        return ends;
    }

    private Obstacle currentActor;
    private Cell currentCell;
    private float sideLength;

    public void loadToTable(Table table)
    {

        sideLength = 128/PlayScreen.PPM/PlayScreen.scale;
//        for(int i = 1; i<map[0].length+1; i++)
//        {
//            table.add();
//        }
//        table.add();
//        table.row();

        //table.setScaleX(2.0f);
        for(int i = 0; i < map.length; i++)
        {
            for(int j = 0; j < map[0].length; j++)
            {
                if(map[i][j] == null)
                {
                    table.add().expand().size(sideLength);
                }
                else if(i == map.length-1 && j == map[0].length-1)
                {
                    table.add(PlayScreen.menuButton).size(PlayScreen.gameport.getScreenWidth()/40, PlayScreen.gameport.getScreenWidth()/40);
                }
                else
                {
                    currentActor = map[i][j];

                    table.add(currentActor).center();
                    currentCell = table.getCell(currentActor);

//                    float x = table.getCell(currentActor).getActorX() - (sidelength - table.getCell(currentActor).getActorWidth());
//                    float y = table.getCell(currentActor).getActorY() + (sidelength - table.getCell(currentActor).getActorWidth());

                    table.getCell(currentActor).size(sideLength*1.3f).center();
                }
            }
            if(i <map.length-1)table.row();
            table.center();
            table.setFillParent(true);
        }


    }

    public void sendObstaclesToTrackPoints()
    {
        for(int i = 0; i<groups.size(); i++)
        {
            groups.get(i).sendObjectToRandomPoint();
        }
        hasSentObstaclesToPoints = true;
    }

    public void createEdgeBox(Viewport port, World world)
    {
        int wdiv = 115;
        int hdiv = 204;

        box = new Body[4];

        BodyDef bdef[] = new BodyDef[4];
        FixtureDef fdef[] = new FixtureDef[4];
        EdgeShape[] poly = new EdgeShape[4];

        float[] points = new float[8];
        points[0] = port.getWorldWidth()/wdiv; points[1] = port.getWorldHeight()/hdiv;
        points[2] = port.getWorldWidth()/wdiv; points[3] = port.getWorldHeight() - port.getWorldHeight()/hdiv;
        points[4] = port.getWorldWidth() - port.getWorldWidth()/wdiv; points[5] = port.getWorldHeight() - port.getWorldHeight()/hdiv;
        points[6] = port.getWorldWidth() - port.getWorldWidth()/wdiv; points[7] = port.getWorldHeight()/hdiv;

        for(int i = 0; i< poly.length*2; i+=2)
        {
            poly[i/2] = new EdgeShape();
            if(i<6)poly[i/2].set(new Vector2(points[i], points[i+1]), new Vector2(points[i+2], points[i+3]));
            else{poly[i/2].set(new Vector2(points[6], points[7]), new Vector2(points[0], points[2]));}
        }

        for(int i = 0; i< bdef.length; i++)
        {
            bdef[i] = new BodyDef();
            bdef[i].type = BodyDef.BodyType.StaticBody;
        }
        for(int i = 0; i< box.length; i++)
        {
            box[i] = world.createBody(bdef[i]);
        }
        for(int i = 0; i< fdef.length; i++)
        {
            fdef[i] = new FixtureDef();
            fdef[i].shape = poly[i];
        }



        for(int i = 0; i< bdef.length; i++){box[i].createFixture(fdef[i]).setUserData("edgebox");}

    }

    public void destroyEdgeBox(World world)
    {
        for(int i = 0; i< box.length; i++){world.destroyBody(box[i]);}
    }

    private WallPortal[] portals = null;
    public WallPortal[] getWallPortals()
    {
        if(portals == null)
        {
            ArrayList<WallPortal> arr = new ArrayList<WallPortal>();
            for (int i = 0; i < getObstacles().length; i++)
            {
                if (getObstacles()[i].getType().equals("portal"))
                {
                    arr.add((WallPortal) getObstacles()[i]);
                }
            }

            portals = new WallPortal[arr.size()];
            for(int i = 0; i<arr.size(); i++)
            {
                portals[i] = arr.get(i);
            }

        }
        return portals;
    }

    private ArrayList<TrackGroup> groups = new ArrayList<TrackGroup>();
    public void createTrackGroup(Color color, int obstacleId)
    {
        groups.add(new TrackGroup(getTrackPoints()[groups.size()], obstacleId, color));
    }

    public TrackPoint[][] getTrackPoints()
    {
        ArrayList<ArrayList<TrackPoint>> arr = new ArrayList<ArrayList<TrackPoint>>();
        for(int a = 0; a < TrackPoint.trackAmount; a++)
        {
            arr.add(new ArrayList<TrackPoint>());
            for (int i = 0; i < map.length; i++)
            {
                for (int j = 0; j < map[i].length; j++)
                {
                    if (map[i][j] != null)
                    {
                        if (map[i][j].getType().equals("track"))
                        {
                            if(((TrackPoint)map[i][j]).getTrackNum() == a)
                            {
                                arr.get(a).add(((TrackPoint)map[i][j]));
                            }
                        }
                    }
                }
            }
        }

        TrackPoint[][] points = new TrackPoint[arr.size()][];

        for(int i = 0; i<arr.size(); i++)
        {
            points[i] = new TrackPoint[arr.get(i).size()];
            for(int j = 0; j<arr.get(i).size(); j++)
            {
                points[i][j] = arr.get(i).get(j);
            }
        }

        return points;
    }

    public void updateTracks()
    {
        for(int i = 0; i<groups.size(); i++)
        {
            groups.get(i).create();
        }
    }

}
