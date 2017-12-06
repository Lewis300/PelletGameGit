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
// * Created by Lewis on 8/31/2017.
// */
//
//public class Level4 extends Level
//{
//    private float pelletPause;
//    private float ambientLight;
//    private static float scale;
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_Start("d").setMapPos(1, 1),
//
//                    new Pellet_Start("d").setMapPos(11, 1),
//
//                    new Mirror(90).setMapPos(3,3),
//
//                    new Mirror(0).setMapPos(5, 3),
//
//                    new Mirror(90).setMapPos(7, 3),
//
//                    new Mirror(0).setMapPos(9, 3),
//
//                    new Mirror(180).setMapPos(1, 10),
//
//                    new Mirror(90).setMapPos(5, 10),
//
//                    new Pellet_End().setMapPos(6, 10),
//
//                    new Mirror(0).setMapPos(7,10),
//
//                    new Mirror(270).setMapPos(11, 10),
//
//                    new Mirror(0).setMapPos(3, 18),
//
//                    new Mirror(270).setMapPos(6, 18),
//
//                    new Mirror(90).setMapPos(9, 18),
//
//                    new Pellet_Start("u").setMapPos(1, 19),
//
//                    new Pellet_Start("u").setMapPos(11, 19)
//            };
//
//    public Level4()
//    {
//        //createMap(12, 20, obstacles);
//        //Utilities.loadMap(this, "TiledMaps/level4.tmx");
//        scale = 12/6f;
//        pelletPause = 1.5f;
//        ambientLight = 0.95f;
//    }
//
//    @Override
//    public void handlePelletSpawns(float dt)
//    {
//        SpawnHandler.updateTimePassed(dt);
//        SpawnHandler.setShootAllSimultaneously(false);
//        SpawnHandler.fire();
//
//    }
//
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
//    @Override
//    public float getAmbientLight() {
//        return ambientLight;
//    }
//
//}
