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
// * Created by Lewis on 8/20/2017.
// */
//
//public class Level3 extends Level
//{
//    private float pelletPause;
//    private float ambientLight;
//    private static float scale;
//
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_End().setMapPos(1, 1),
//
//                    new Mirror(90f).setMapPos(6, 1),
//
//                    new Block().setMapPos(1, 2),
//
//                    new Mirror(0).setMapPos(1, 4),
//
//                    new Mirror(270).setMapPos(6, 4),
//
//                    new Pellet_Start("r").setMapPos(1, 10),
//
//                    new Mirror(0).setMapPos(6, 10),
//
//                    new Mirror(90).setMapPos(1, 16),
//
//                    new Mirror(270).setMapPos(6, 16),
//
//                    new Block().setMapPos(1, 18),
//
//                    new Pellet_End().setMapPos(1, 19),
//
//                    new Mirror(180).setMapPos(6, 19)
//            };
//
//    public Level3()
//    {
//
//        //Utilities.loadMap(this, "TiledMaps/level3.tmx");
//        //createMap(8, 21, obstacles);
//
//        pelletPause = 5f;
////        ambientLight = 0.95f;
//        scale = 11/6f;
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
//    @Override
//    public float getAmbientLight() {
//        return ambientLight;
//    }
//
//}
