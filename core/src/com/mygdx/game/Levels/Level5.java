//package com.mygdx.game.Levels;
//
//import com.mygdx.game.Obstacles.Block;
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_End;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Obstacles.Toggle;
//import com.mygdx.game.Tools.SpawnHandler;
//import com.mygdx.game.Tools.Utilities;
//
///**
// * Created by Lewis on 9/12/2017.
// */
//
//public class Level5 extends Level
//{
//    private static float scale;
//    private float pelletPause;
//    private float ambientLight;
//
//    private Obstacle[] obstacles =
//            {
//                    new Mirror(180f).setMapPos(1, 1),
//
//                    new Mirror(0).setMapPos(12,1),
//
//                    new Pellet_Start("r").setMapPos(2,2),
//
//                    new Mirror(180).setMapPos(10, 2),
//
//                    new Mirror(90).setMapPos(1, 8),
//
//                    new Toggle(100, 120, false, true).setMapPos(8,8),
//
//                    new Mirror(270).setMapPos(2, 12),
//
//                    new Mirror(90).setMapPos(10, 12),
//
//                    new Mirror(90).setMapPos(12, 12),
//
//                    new Block().setMapPos(12,14),
//
//                    new Mirror(0).setMapPos(2, 17),
//
//                    new Block().setMapPos(10, 17),
//
//                   // new Pellet_End(120, 100, "disable").setMapPos(2, 20),
//
//                    new Mirror(180).setMapPos(9, 21)
//            };
//
//    public Level5()
//    {
//        //createMap(14, 24, obstacles);
//        //Utilities.loadMap(this, "TiledMaps/level5.tmx");
//
//        scale = 12/6f;
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
//}
