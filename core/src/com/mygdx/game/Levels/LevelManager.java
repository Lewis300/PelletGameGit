package com.mygdx.game.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Obstacles.Obstacle;
import com.mygdx.game.Obstacles.Pellet_End;
import com.mygdx.game.Obstacles.Pellet_Start;
import com.mygdx.game.PelletGame;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.GameScreenManager;
import com.mygdx.game.Tools.SpawnHandler;
import com.mygdx.game.Tools.Utilities;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.ArrayList;

import box2dLight.RayHandler;


/**
 * Created by Lewis on 8/24/2017.
 */

public class LevelManager
{
    public static int levelnum;
    public static int maxLevel = 14; //PelletGame.prefs.getInteger("maxLevel", 1);
    public static int levelToStartAt = 14;
    private float scale = 1f;

    public static ArrayList<Integer> lvlSequence;

    private static Level currentLevel = null;
    public static boolean isBeingUsedByLevelTester = false;

    private GameScreenManager gsm;
    public LevelManager(GameScreenManager gsm)
    {
        this.gsm = gsm;
        if(!isBeingUsedByLevelTester)levelnum = levelToStartAt-1;
        else{levelnum = lvlSequence.get(0);}
        //scale = Level4.getScale();
    }

    public void create()
    {
        Obstacle.nextId = 0;
        PlayScreen.rayHandler.removeAll();

        if (levelnum == 0){currentLevel = Utilities.createLevel("TiledMaps/level1.tmx", gsm); scale = currentLevel.getScale();}
        else
        {
            currentLevel = Utilities.createLevel("TiledMaps/level"+levelnum+".tmx", gsm);
            scale = currentLevel.getScale();
        }

        currentLevel.createEdgeBox(PlayScreen.gameport, PlayScreen.world);

    }

    public void createScales()
    {
        //if(levelnum == -1 || levelnum == -2){scale = new LevelExample().getScale();}
        if (levelnum == 1 || levelnum == 0){scale = Utilities.getScaleFromMap("TiledMaps/level1.tmx");}
        else{scale = Utilities.getScaleFromMap("TiledMaps/level"+levelnum+".tmx");}
    }

    private int goneToNextLevelCount = 0;
    public void goToNextLevel(World world, Stage gamestage, Table lvlTable, ArrayList<Pellet> pellets, Color[] backGroundColors)
    {

        PlayScreen.pelletAlpha = 0;
        Obstacle.nextId = 0;
        PlayScreen.timePassed = 0;

        lvlTable.clear();
        gamestage.clear();
        PlayScreen.rayHandler.removeAll();
        gamestage.addAction(Actions.show());

        Pellet_End.finalEndSelected = -1123451;

        lvlTable.setFillParent(true);

        for (int i = 0; currentLevel!=null && i < currentLevel.getObstacles().length; i++)
        {
            currentLevel.getObstacles()[i].setInitialized(false);
            currentLevel.getObstacles()[i].destroyBody(world);
        }
        world.clearForces();
        pellets.removeAll(pellets);
        world.setContactListener(new WorldContactListener());

        changeLevel();
        createScales();
        PlayScreen.scale = getScale()*1.2f;

        destroyAllBodies(world);

        this.create();


        setAmbientLight(currentLevel.getAmbientLight());
        gamestage.addAction(Actions.sequence(Actions.alpha(0f), Actions.delay(0.2f), Actions.fadeIn(2f), Actions.show()));

        float padDiv = 40f;
        lvlTable.pad(PlayScreen.gameport.getWorldHeight()/padDiv, PlayScreen.gameport.getWorldWidth()/padDiv, PlayScreen.gameport.getWorldHeight()/padDiv, PlayScreen.gameport.getWorldWidth()/padDiv);

        float btnDiv = 16f;
        lvlTable.add(PlayScreen.menuButton).size(PlayScreen.gameport.getWorldWidth()/btnDiv, PlayScreen.gameport.getWorldWidth()/btnDiv).left();
        lvlTable.row();
        currentLevel.loadToTable(lvlTable);
        //lvlTable.add().size(PlayScreen.gameport.getWorldWidth()/btnDiv, PlayScreen.gameport.getWorldWidth()/btnDiv).right();
        lvlTable.center();

        gamestage.addActor(lvlTable);

        for(int i = 0; i < currentLevel.getObstacles().length; i++)
        {
            currentLevel.getObstacles()[i].resize();
        }

        RayHandler.useDiffuseLight(false);

        updateMaxlevel();

        Color[] colors = Utilities.generateRandomBackgroundColors();
        backGroundColors[0] = colors[0];
        backGroundColors[1] = colors[1];
        backGroundColors[2] = colors[2];
        backGroundColors[3] = colors[3];

        goneToNextLevelCount++;

    }

    public void changeLevel()
    {
        if(isBeingUsedByLevelTester)
        {
            PelletGame.prefs.putInteger("levelnum", lvlSequence.get(goneToNextLevelCount));
            PelletGame.prefs.flush();
            levelnum = PelletGame.prefs.getInteger("levelnum", 1);
        }
        else
        {
            PelletGame.prefs.putInteger("levelnum",levelnum+1);
            PelletGame.prefs.flush();
            levelnum = PelletGame.prefs.getInteger("levelnum", 1);
        }

        if(levelnum > maxLevel && goneToNextLevelCount > 0 && !isBeingUsedByLevelTester)
        {
            maxLevel = levelnum;
        }
    }
    

    public Level getCurrentlvl(){return currentLevel;}

    public float getScale(){return scale;}

    public int getLevelNum(){return  levelnum;}

    public void setLevelnum(int num){levelnum = num;}

    public void setAmbientLight(float light)
    {
        PlayScreen.rayHandler.setAmbientLight(light);
    }

    public void destroyAllBodies(World world)
    {
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for(int i = 0; i< bodies.size; i++)
        {
            world.destroyBody(bodies.get(i));
        }
    }

    private void updateMaxlevel()
    {
        if(levelnum > maxLevel)// && goneToNextLevelCount > 0)
        {
            PelletGame.prefs.putInteger("maxLevel", levelnum);
            PelletGame.prefs.flush();

            maxLevel = levelnum;
        }
    }
}
