//package com.mygdx.game.Levels;
//
//import com.mygdx.game.Obstacles.Block;
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_End;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Tools.SpawnHandler;
//import com.mygdx.game.Tools.Utilities;
//
///**
// * Created by Lewis on 8/25/2017.
// */
//
//public class Level1 extends Level
//{
//    private float pelletPause;
//    private float ambientLight;
//    private static float scale;
//
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_Start("d").setMapPos(1, 1),
//
//                    new Mirror(0).setMapPos(1, 3),
//
//                    new Mirror(180).setMapPos(5, 3),
//
//                    new Mirror(180).setMapPos(1, 5),
//
//                    new Pellet_End().setMapPos(9, 5),
//
//                    new Mirror(90).setMapPos(1, 7),
//
//                    new Mirror(90).setMapPos(7, 7),
//
//                    new Block().setMapPos(3, 9),
//
//                    new Mirror(90).setMapPos(5, 9),
//
//                    new Mirror(270).setMapPos(7, 9)
//
//            };
//
//   // public Level1()
//    {
//        //Utilities.loadMap(this, "TiledMaps/level1.tmx");
//        //createMap(11, 11, obstacles);
//
//        scale = 14/6f;
//
//        ambientLight = 0.95f;
//        pelletPause = 1.5f;
//
//    }
//
//
////
////    @Override
////    public void handlePelletSpawns(float dt)
////    {
////        SpawnHandler.updateTimePassed(dt);
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
////    public float getAmbientLight()
////    {
////        return ambientLight;
////    }
//}
