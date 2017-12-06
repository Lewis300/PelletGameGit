//package com.mygdx.game.Levels;
//
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_End;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Tools.SpawnHandler;
//import com.mygdx.game.Tools.Utilities;
//
///**
// * Created by Lewis on 8/15/2017.
// */
//
///*
//    COMMENTS FOR LEVELS IN LEVEL 1
// */
//
//public class Level2 extends Level
//{
////    private float pelletPause;
////    private float ambientLight;
////    private static float scale;
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_End().setMapPos(2, 1),
//
//                    new Pellet_End().setMapPos(6, 1),
//
//                    new Pellet_End().setMapPos(1, 4),
//
//                    new Mirror(0).setMapPos(4, 4),
//
//                    new Pellet_End().setMapPos(7, 4),
//
//                    new Mirror(90).setMapPos(2, 6),
//
//                    new Pellet_Start("udlr").setMapPos(4, 6),
//
//                    new Mirror(270).setMapPos(6, 6),
//
//                    new Pellet_End().setMapPos(1, 8),
//
//                    new Mirror(180).setMapPos(4, 8),
//
//                    new Pellet_End().setMapPos(7, 8),
//
//                    new Pellet_End().setMapPos(2, 10),
//
//                    new Pellet_End().setMapPos(6, 10)
//            };
//
//
//    public Level2()
//    {
//        //Utilities.loadMap(this, "TiledMaps/level2.tmx");
//        //createMap(9, 13, obstacles);
////
////        scale = 12/6f;
////
////        ambientLight = 0.95f;
////        pelletPause = 3.5f;
//    }
//
//
////    @Override
////    public void handlePelletSpawns(float dt)
////    {
////        SpawnHandler.updateTimePassed(dt);
////        SpawnHandler.setWaitBetweenFires(true);
////        SpawnHandler.fire();
////    }
////
////    @Override
////    public float getScale() {
////        return scale;
////    }
////
////    @Override
////    public float getPelletPause()
////    {
////        return pelletPause;
////    }
////
////    @Override
////    public float getAmbientLight() {
////        return ambientLight;
////    }
////
//
//
//}
