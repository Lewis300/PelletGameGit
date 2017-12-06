//package com.mygdx.game.Levels;
//
//import com.badlogic.gdx.graphics.Color;
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Obstacles.Toggle;
//import com.mygdx.game.Obstacles.Track.TrackPoint;
//import com.mygdx.game.Tools.SpawnHandler;
//
///**
// * Created by Lewis on 10/8/2017.
// */
//
//public class Level9 extends Level
//{
//    private static float scale;
//    private float pelletPause;
//    private float ambientLight;
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_Start("d").setMapPos(4, 1),
//                    new TrackPoint(101, 0).setMapPos(11, 3),
//                    new TrackPoint(102, 1).setMapPos(4, 5),
//                    new TrackPoint(103, 0, 601, "disable").setMapPos(2,7),
//                    new TrackPoint(104, 0).setMapPos(12, 10),
//                    new TrackPoint(105, 1).setMapPos(10,12),
//                    new TrackPoint(106, 0).setMapPos(6,14),
//                    new TrackPoint(107, 1).setMapPos(4,17),
//                    new Mirror(9, 90).setMapPos(13, 18),
//                    new Mirror(10, 90).setMapPos(4,20),
//                    new TrackPoint(108, 0).setMapPos(1, 22),
//                    new TrackPoint(109, 0).setMapPos(10,22),
//
//                    new Toggle(601, 103, true, false).setMapPos(10, 5)
//            };
//
//    public Level9()
//    {
//        createMap(14, 24, obstacles);
//
//        createTrackGroup(Color.BLUE, 9);
//        createTrackGroup(Color.PINK, 10);
//
//        scale = 14/6f;
//        pelletPause = 3.75f;
//        ambientLight = 0.95f;
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
