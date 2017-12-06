//package com.mygdx.game.Levels;
//
//import com.mygdx.game.Obstacles.Mirror;
//import com.mygdx.game.Obstacles.Obstacle;
//import com.mygdx.game.Obstacles.Pellet_End;
//import com.mygdx.game.Obstacles.Pellet_Start;
//import com.mygdx.game.Tools.SpawnHandler;
//
///*
//--------------------------------------------------------INSTRUCTIONS / GUIDE FOR JETT--------------------------------------------------------
//
//
//    *Before doing anything you must set up the constructor method for this Level*
//
//        - the method header looks like this (If this were the 6th level):
//
//                public Level6()
//                {
//                    (Things go in here later)
//                }
//
//
//                                        -------------------------Obstacles-------------------------
//
//    - An Obstacle is the term and class type of all objects than can be placed in a level other than the pellets
//
//    - Different Obstacles need different information given to them in order to function
//    - This information is put in the brackets at the end of the Obstacle name (i.e new Mirror() <-- these brackets)
//
//    - ALL Obstacles require a position on the map given by the .setMapPos(x, y) method
//        * the .setMapPos(x, y) method goes at the end of obstacle initialization and the co-ordinates are found by hovering the mouse over the specified obstacle in the TileD map
//
//        **OBSTACLE INITIALIZATION GUIDE**
//        *
//        *   IMPORTANT: You only need to initialize an obstacle with an id if it is being acted upon/linked with other objects on the map (i.e. TrackPoints and Toggles)
//        *   IMPORTANT: ALL ID'S YOU PERSONALLY ENTER MUST BE NEGATIVE AND NOT EQUAL TO THE ID'S OF ANY OTHER OBSTACLES
//        *
//        *   MIRROR:
//        *      - the only information that mirrors need is the angle that they initially
//        *        start off of when the level begins. This number must be either 0, 90, 180, or 270
//        *
//        *       Sample initialization: new Mirror(180).setMapPos(4, 0)
//        *
//        *       Initialization with id: new Mirror(-12, 180).setMapPos(4, 0)
//        *
//        *   BLOCK:
//        *       - blocks need no information to function (except when linked to a Toggle or TrackGroup)
//        *
//        *       Sample initialization: new Block().setMapPos(12, 14)
//        *
//        *       Initialization with id: new Block(-4).setMapPos(12, 14)
//        *
//        *    PELLET_END:
//        *       - Pellet_End's need no information to function (except when linked to a Toggle or TrackGroup)
//        *
//        *       Sample initialization: new Pellet_End().setMapPos(3,12)
//        *
//        *       Initialization with id: new Pellet_End(-7).setMapPos(3,12)
//        *
//        *    PELLET_START:
//        *       - The only information Pellet_Start's need is the directions in which it can fire
//        *           *This is represented by the characters 'u' - up, 'd' - down, 'l' - left, and 'r' - right. MAKE SURE YOU USE DOUBLE QUOTES IN THE BRACKETS
//        *
//        *       Sample initialization: new Pellet_Start("u").setMapPos(6, 7) --- (this will only fire pellets upwards, you can use multiple directions if you like(1.e. "uldr" or "dl"))
//        *
//        *       Initialization with id: new Pellet_Start(-2, "u").setMapPos(6, 7)
//        *
//        *
//        *    TRACKPOINT:
//        *       - A TrackPoint needs two important pieces of information
//        *           #1 - An id to represent that specific TrackPoint object
//        *           #2 - A number that is the id of the group of points that this point is a part of (THIS MUST START AT 0 AND INCREASE BY 1 WITH EVERY NEW GROUP)
//        *
//        *           Sample initialization: new TrackPoint(-15, 0).setMapPos(5, 9)
//        *
//        *           Initialization with id: (all TrackPoints must have an id)
//        *
//        *           -(More work will need to be done later on to configure the TrackPoint groups, visit the "Setting up TrackGroups" section for that)
//        *
//        *    WALLPORTAL:
//        *       - The WallPortal needs three important pieces of information
//        *           #1 - An id to represent that specific WallPortal object.
//        *           #2 - The id of the other WallPortal to which the one you are creating is linked to
//        *           #3 - A character representing which side of the screen it is on, top = 't', bottom = 'b', right = 'r', left = 'l'. MAKE SURE YOU USE SINGLE QUOTES IN THE BRACKETS
//        *
//        *       NOTE: Only place WallPortals on the edges of the map or the level will look all fucked up and be weird. Just keep them on the wall.
//        *
//        *       Sample initialization: new WallPortal(-10, -20, 'l').setMapPos(9,3)
//        *
//        *       Initialization with id: (all WallPortals must have an id)
//        *
//        *    TOGGLE:
//        *       - The Toggle needs four important pieces of information
//        *           #1 - An id
//        *           #2 - The id of the obstacle the button will be acting upon
//        *           #3 - A boolean representing whether the toggle starts on or off (true = on/it starts green, false = off/it starts red).
//        *                The color of the toggle has no effect on which state the obstacle starts in
//        *           #4 - A boolean stating whether this Toggle is a button, which means it can only be clicked once (true = its a button, false = it is just a regular Toggle)
//        *
//        *       *INITIALIZATION SAMPLES FOR OBSTACLES LINKED WITH TOGGLES*
//        *        - These obstacels need an additional 2 pieces of information. The id of the toggle, and the action (either "enable" or "disable". This is the state the obstacle starts in)
//        *        - (In these examples -1 is the id of the toggle)
//        *
//        *           - MIRROR: new Mirror(-12, 180f, -1, "enable").setMapPos(4, 0)
//        *
//        *           - BLOCK: new Block(-4, -1, "disable").setMapPos(12, 14)
//        *
//        *           - PELLET_END: new Pellet_End(-7, -1, "disable").setMapPos(6, 7)
//        *
//        *           - PELLET_START: Pellet_Start's do not work with Toggle's
//        *
//        *           - TRACKPOINT: new TrackPoint(-15, 0, -1, "disable").setMapPos(5, 9)
//        *
//        *           - WALLPORTAL: new WallPortal(-10, -20, 'l', -1, "disable").setMapPos(9, 3)
//        *                         (YOU ONLY NEED TO LINK ONE OF THE TWO WALLPORTALS FOR BOTH OF THEM TO ACT IN UNISON ACCORDING TO THE TOGGLE)
//        *
//        *
//
//
//
//        How to set up the map:
//
//            - You need to create an Obstacle array. The initialization of that looks like this:
//
//                NOTE: "obstacles" is the name given to this array
//                private Obstacle[] obstacles =
//                        {
//                                new Pellet_Start("d").setMapPos(5, 2),
//                                new Mirror(270f).setMapPos(5, 6),
//                                ...
//                        }
//
//
//            - In your constructor THE FIRST LINE OF CODE MUST BE THE CREATION OF THE MAP. Do this by using the createMap() method.
//              It looks like this:
//
//                createMap(int mapWidth, int mapHeight, Obstacle[] obstacles)
//
//                Ex: createMap(14, 24, obstacles)
//
//
//            - After the map is created you need to set a few variables
//
//                #1 - The scale of the map
//                    - You will have to test out the level to find a scale that makes the map look best
//                    - i.e for a map with dimensions 14 by 24, a good looking scale is 14/7
//
//                    example: scale = 14/7f; (f must be used to indicate that it is a 'float' variable)
//
//                #2 - The ambient light of a map
//                    - This is preferably set to 0.95
//
//                    example: ambientLight = 0.95f (f must be used to indicate that it is a 'float' variable)
//
//                #3 - The time in seconds between fires of a pellet
//                    - This can be whatever you want. But you should have an idea of what it should be for the specific level you're making
//
//                    example: pelletPause = 1f (f must be used to indicate that it is a 'float' variable)
//
//
//            ##EXAMPLE CONSTRUCTOR##
//
//            public Level6()
//            {
//                createMap(14, 24, obstacles);
//
//                scale = 14/7f;
//                ambientLight = 0.95f
//                pelletPause = 1f;
//            }
//
//
//
//                                        -------------------------Setting Up TrackGroups-------------------------
//
//
//            To bound all the points in a certain group together you must call the createTrackGroup() method in your constructor
//                - This method requires two pieces of information, the Color of the points and obstacle, and the id of the obstacle the group controls
//
//                Sample: createTrackGroup(Color.PINK, -12);
//
//
//            REMEMBER: This must go after the createMap() method is called.
//
//            Sample constructor:
//
//            public Level6()
//            {
//                createMap(14, 24, obstacles);
//
//                createTrackGroup(Color.PINK, -12);
//
//                scale = 14/7f;
//                ambientLight = 0.95f
//                pelletPause = 1f;
//            }
//
//            For every group of points you have you must call a createTrackGroup() method in your constructor
//                - Make sure they control different obstacles and have different colors
//
//            Another sample constructor:
//
//            public Level6()
//            {
//                createMap(14, 24, obstacles);
//
//                createTrackGroup(Color.PINK, -12);
//                createTrackGroup(Color.BLUE, -19);
//                createTrackGroup(Color.YELLOW, -7);
//                ...
//
//                scale = 14/7f;
//                ambientLight = 0.95f
//                pelletPause = 1f;
//            }
//
// */
//
//public class LevelExample extends Level
//{
//    private static float pelletPause;
//    private static float ambientLight;
//    private float scale;
//
//
//    private Obstacle[] obstacles =
//            {
//                    new Pellet_Start("d").setMapPos(2, 1),
//
//                    new Mirror(270).setMapPos(2, 3),
//
//                    new Pellet_End().setMapPos(4, 3)
//
//            };
//
//    public LevelExample()
//    {
//        createMap(6, 6, obstacles);
//
//        scale = 5/6f;
//
//        ambientLight = 0.95f;
//        pelletPause = 1.5f;
//    }
//
//
//
//    @Override
//    public void handlePelletSpawns(float dt)
//    {
//        SpawnHandler.updateTimePassed(dt);
//        SpawnHandler.fire();
//    }
//
//    @Override
//    public float getScale() {
//        return scale;
//    }
//
//    @Override
//    public float getPelletPause()
//    {
//        return pelletPause;
//    }
//
//    @Override
//    public float getAmbientLight()
//    {
//        return ambientLight;
//    }
//}
