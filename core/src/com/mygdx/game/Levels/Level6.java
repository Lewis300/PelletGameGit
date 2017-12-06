//package com.mygdx.game.Levels;
//
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_End;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Obstacles.Toggle;
//import com.mygdx.game.Obstacles.WallPortal;
//import com.mygdx.game.Tools.SpawnHandler;
//import com.mygdx.game.Tools.Utilities;
//
///**
// * Created by Lewis on 9/20/2017.
// */
//
//public class Level6 extends Level
//{
//    private static float scale;
//    private float pelletPause;
//    private float ambientLight;
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_Start(0, "d").setMapPos(5, 1),
//
//                    //new Pellet_End(1, 4, "disable").setMapPos(12, 1),
//
//                    new Mirror(2, 270).setMapPos(1, 4),
//
//                    new Mirror(3, 0).setMapPos(5, 4),
//
//                    new Toggle(4, 1, false, true).setMapPos(12, 4),
//
//                    new Toggle(5, 8, false, true).setMapPos(1, 19),
//
//                    new Mirror(6, 90).setMapPos(8, 19),
//
//                    new Mirror(7, 180).setMapPos(12, 19),
//
//                    //new Pellet_End(8, 5, "disable").setMapPos(1, 22),
//
//                    new Pellet_Start(9, "u").setMapPos(8, 22),
//
//                    new WallPortal(10, 11, 'l').setMapPos(0, 4),
//
//                    new WallPortal(11, 10, 'b').setMapPos(1, 21),
//
//                    new WallPortal(12, 13, 't').setMapPos(12, 0),
//
//                    new WallPortal(13, 12, 'r').setMapPos(13, 19)
//            };
//
//    public Level6()
//    {
//        //createMap(14, 24, obstacles);
//
//       // Utilities.loadMap(this, "TiledMaps/level6.tmx");
//        scale = 12/6f;
//        pelletPause = 3.75f;
//        ambientLight = 0.95f;
//    }
//
//
//    @Override
//    public void handlePelletSpawns(float dt)
//    {
//        SpawnHandler.updateTimePassed(dt);
//        SpawnHandler.setShootAllSimultaneously(true);
//        SpawnHandler.fire();
//
//        super.handlePelletSpawns(dt);
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
//    @Override
//    public float getAmbientLight() {
//        return ambientLight;
//    }
//}
