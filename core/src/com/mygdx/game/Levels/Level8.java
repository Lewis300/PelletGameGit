//package com.mygdx.game.Levels;
//
//import com.badlogic.gdx.graphics.Color;
//import com.mygdx.game.Obstacles.Block;
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_End;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Obstacles.Toggle;
//import com.mygdx.game.Obstacles.Track.TrackPoint;
//import com.mygdx.game.Obstacles.WallPortal;
//import com.mygdx.game.Tools.SpawnHandler;
//
///**
// * Created by Lewis on 10/1/2017.
// */
//
//public class Level8 extends Level
//{
//    private static float scale;
//    private float pelletPause;
//    private float ambientLight;
//
//
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_End().setMapPos(2,1),
//                    new Toggle(100, 400, true, true).setMapPos(10,1),
//                    new Mirror(270f).setMapPos(5,2),
//                    new Mirror(180f).setMapPos(7,2),
//                    //new Block(400, 100, "enable").setMapPos(2,3),
//                    new Mirror(270f).setMapPos(1,5),
//                    new Mirror(0).setMapPos(7,5),
//                    new Mirror(90f).setMapPos(9,5),
//                    new Mirror(180f).setMapPos(11,5),
//                    new Block().setMapPos(13, 5),
//                    new Mirror(270f).setMapPos(5, 7),
//                    new Mirror(0).setMapPos(7, 7),
//                    new Mirror(90).setMapPos(9, 7),
//                    new Block().setMapPos(11,7),
//                    new Mirror(180f).setMapPos(13,7),
//                    new Mirror(270f).setMapPos(5,9),
//                    new Mirror(0).setMapPos(7,9),
//                    new Mirror(90).setMapPos(9,9),
//                    new Mirror(180f).setMapPos(11, 9),
//                    new Mirror(270f).setMapPos(13,9),
//                    new Mirror(0).setMapPos(3,11),
//                    new Mirror(90f).setMapPos(5,11),
//                    new Mirror(180f).setMapPos(7,11),
//                    new Mirror(270f).setMapPos(9,11),
//                    new Mirror(0f).setMapPos(11,11),
//                    new Mirror(90f).setMapPos(13,11),
//                    new Mirror(0f).setMapPos(7,14),
//                    new Mirror(180f).setMapPos(12, 14),
//                    new Mirror(0f).setMapPos(1,16),
//                    new Mirror(270f).setMapPos(10,16),
//                    new TrackPoint(30, 0).setMapPos(2,18),
//                    new Mirror(-1, 0f).setMapPos(7,18),
//                    new TrackPoint(32, 0).setMapPos(12,18),
//                    new WallPortal(33, 34, 'r').setMapPos(14,18),
//                    new WallPortal(34, 33, 'l').setMapPos(0,20),
//                    new Mirror(35, 90f).setMapPos(7, 20),
//                    new Pellet_Start("u").setMapPos(7, 22)
//
//            };
//
//
//
//    public Level8()
//    {
//        createMap(15, 24, obstacles);
//        createTrackGroup(Color.BLUE, -1);
//
//        scale = 16/7f;
//        pelletPause = 3.75f;
//        ambientLight = 0.9f;
//    }
//
//    @Override
//    public void handlePelletSpawns(float dt)
//    {
//        SpawnHandler.updateTimePassed(dt);
//        SpawnHandler.fire();
//    }
//
//    @Override
//    public  float getScale()
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
//
//
//}
