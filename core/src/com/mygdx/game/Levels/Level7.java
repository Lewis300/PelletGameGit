//package com.mygdx.game.Levels;
//
//import com.badlogic.gdx.graphics.Color;
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_End;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Obstacles.Track.TrackPoint;
//import com.mygdx.game.Tools.SpawnHandler;
//
///**
// * Created by Lewis on 9/26/2017.
// */
//
//public class Level7 extends Level
//{
//    private float pelletPause;
//    private float ambientLight;
//    private static float scale;
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_Start("d").setMapPos(1, 1),
//
//                    new Pellet_End().setMapPos(0, 2),
//
//                    new TrackPoint(200, 0).setMapPos(5, 2),
//
//                    new Mirror(300,90).setMapPos(3, 6),
//
//                    new TrackPoint(400, 0).setMapPos(1, 10),
//
//                    new Mirror(270).setMapPos(5, 10)
//            };
//
//
//    public Level7()
//    {
//
//        createMap(7, 11, obstacles);
//        createTrackGroup(Color.BLUE, 300);
//
//
//        scale = 12/6;
//
//        ambientLight = 0.95f;
//        pelletPause = 3.75f;
//    }
//
//    @Override
//    public void handlePelletSpawns(float dt)
//    {
//        SpawnHandler.updateTimePassed(dt);
//        SpawnHandler.fire();
//
//    }
//
//
//    @Override
//    public float getScale()
//    {
//        return scale;
//    }
//
//    @Override
//    public float getPelletPause() {
//        return pelletPause;
//    }
//
//
//    @Override
//    public float getAmbientLight() {
//        return ambientLight;
//    }
//
//}
