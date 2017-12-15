package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Levels.LevelManager;
import com.mygdx.game.Obstacles.Obstacle;
import com.mygdx.game.Obstacles.Pellet_End;
import com.mygdx.game.Obstacles.Toggle;
import com.mygdx.game.PelletGame;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Tools.GameScreenManager;
import com.mygdx.game.Tools.Reusables;
import com.mygdx.game.Tools.Utilities;
import com.mygdx.game.Tools.WorldContactListener;
import java.util.ArrayList;
import box2dLight.RayHandler;

/**
 * Created by Lewis on 8/23/2017.
 */

//TODO MAKE A LEVEL MARKER

public class PlayScreen extends GameScreen
{
    //Game
    public static PelletGame game;
    public static final float PPM = 3.5f;
    public static float scale = 1f;
    public static float timePassed = 0;
    public static int levelsPassedSinceOpen = 0;

    //Initialize
    private int doneInitialResize = 0;

    //Camera and viewport
    private static OrthographicCamera gamecam;
    public static Viewport gameport;

    //Stage and table
    private static Table lvlTable;
    private static Table gameTable;

    //For fading animation
    public static float pelletAlpha = 0f;

    //Level change
    private static boolean enteringFromMenu = true;
    public static boolean levelIsChanging = false;
    public static boolean drawLevelText = true;
    private static boolean goneToNextLevel = false;
    private static Color backgroundSpriteColor;

    private static GlyphLayout lvlChangeLayout;
    public static String lvlChangeText = "";
    private static float lvlTextWidth;
    private static float lvlTextHeight;


    public static LevelManager lmg;

    //Box2D
    public static World world;
    private static Box2DDebugRenderer b2dr;
    public static RayHandler rayHandler;
    public static ArrayList<Obstacle> bodiesNeedToChange;


    //Pellets
    public static ArrayList<Pellet> pellets;
    public static float pelletPause = 0;

    //Buttons
    public static Button menuButton;
    private static Skin menuButtonSkin;

    private static int pelletSpeedMult = 1;

    private ShapeRenderer sr;
    private Color[] backGroundColors;

    public PlayScreen(GameScreenManager gsm, PelletGame game)
    {
        super(gsm);
        this.game = game;
    }

    public void create()
    {
        Gdx.input.setCatchBackKey(true);

        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        backGroundColors = new Color[4];
        backGroundColors[0] = Color.BLUE;
        backGroundColors[1] = Color.RED;
        backGroundColors[2] = Color.OLIVE;
        backGroundColors[3] = Color.PINK;

        gamecam = new OrthographicCamera(game.WIDTH/PPM, game.HEIGHT/PPM);
        gamecam.position.set(game.WIDTH/2/PPM, game.HEIGHT/2/PPM, 0);
        gamecam.update();

        gameport = new ExtendViewport(game.WIDTH/PPM, game.HEIGHT/PPM, gamecam);
        gameport.apply(true);
        game.batch.setProjectionMatrix(gamecam.combined);

        //Level table and stage
        gameTable = new Table();
        gameTable.center();
        gameTable.setFillParent(true);
        lvlTable = new Table();
        lvlTable.center();
        lvlTable.setFillParent(true);
        gamestage = new Stage(gameport, game.batch);

        //Buttons
        menuButtonSkin = new Skin(new TextureAtlas("Buttons/MenuButton/menuButton.txt"));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = menuButtonSkin.getDrawable("levelSelectMenuBtn");
        style.down = menuButtonSkin.getDrawable("levelSelectMenuBtnDown");
        menuButton = new Button(style);
        menuButton.setWidth(gameport.getScreenWidth()/10);
        menuButton.setHeight(gameport.getScreenWidth()/10);


        Gdx.input.setInputProcessor(gamestage);

        //Box2D
        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();
        rayHandler = new RayHandler(world);
        rayHandler.setCombinedMatrix(gamecam);
        rayHandler.setShadows(true);
        rayHandler.setCombinedMatrix(gamecam);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(1);

        pellets = new ArrayList<Pellet>();

        world.setContactListener(new WorldContactListener());

        //Level change text layout
        lvlChangeLayout = new GlyphLayout();

        bodiesNeedToChange = new ArrayList<Obstacle>();
    }

    private void handleInput(float dt)
    {
        if(LevelManager.isBeingUsedByLevelTester)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            {
                levelIsChanging = true;
            }
        }
    }

    private boolean hasSentObstaclesToPoints = false;
    public void update(float dt)
    {
        handleInput(dt);

        if(menuButton.isPressed() && !isInLvlMenu() && !levelIsChanging && timePassed>=0.5f)
        {
            openLvlSelectMenu();
            lvlTable.setTouchable(Touchable.disabled);
            menuButton.addAction(Actions.fadeOut(0.5f));
        }
        else
        {
            lvlTable.setTouchable(Touchable.enabled);
        }

        if(closingMenu)
        {
            menuButton.addAction(Actions.fadeIn(0.5f));
        }
        timePassed+=dt;
        pelletPause+=dt;

        gamestage.act(dt);
        if(lmg.getCurrentlvl()!=null)lmg.getCurrentlvl().updateTracks();

        rayHandler.update();

        //Set pellet id's chronologically
        for(int i = 0; i< PlayScreen.pellets.size(); i++)
        {
            pellets.get(i).id = i + "";
        }

        //Check for and remove pellets that ought to be removed
        ArrayList<Pellet> pelletsToRemove= new ArrayList<Pellet>();
        for(Pellet pellet : pellets)
        {
            for(int i = 0; i<pelletSpeedMult; i++){pellet.update(dt);}

            if(pellet.remove)
            {
                pelletsToRemove.add(pellet);
                world.destroyBody(pellet.getBody());
                if(pellet.hasLight && pellet.removeLight)
                {
                    pellet.pelletLight.setActive(false);
                }
            }

        }
        pellets.removeAll(pelletsToRemove);

        if(!levelIsChanging){PelletGame.font440.getData().setScale(0.2857142857142857f);}
        if(levelIsChanging || enteringFromMenu){calculateFadeAlpha(dt);}
        if(Pellet_End.finalEndSelected != -1123451){levelIsChanging = true; drawLevelText = true;}

        for(int i = 0; i<pelletSpeedMult; i++)world.step(dt, 6, 2);
        if(bodiesNeedToChange.size() > 0)
        {
            for (int i = 0; i < bodiesNeedToChange.size(); i++)
            {
                ((Toggle) bodiesNeedToChange.get(i)).changeBody();
            }
            bodiesNeedToChange.removeAll(bodiesNeedToChange);
        }

        if(!hasSentObstaclesToPoints&&timePassed>=0.20)
        {
            lmg.getCurrentlvl().sendObstaclesToTrackPoints();
            hasSentObstaclesToPoints = true;
        }
        rayHandler.update();

    }

    private boolean renderedOnce = false;
    @Override
    public void render(float delta)
    {
        super.render(delta);

        update(dt);
        menuButton.act(delta);

        //Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(0, 0, gameport.getScreenWidth(), gameport.getScreenHeight(), backGroundColors[0], backGroundColors[1], backGroundColors[2], backGroundColors[3]);
        sr.end();

        game.batch.setColor(1, 1, 1, 1);
        game.batch.begin();
        //game.batch.draw(PelletGame.backgroundTex, 0,0, gameport.getScreenWidth(), gameport.getScreenHeight());

        //System.out.println(dt);

        //Draw pellets
        for (Pellet pellet : pellets) {
            pellet.render(game.batch, pelletAlpha);
        }

        game.batch.end();

        rayHandler.render();
        gamestage.draw();



        if (levelIsChanging && fadeTime != 0.0f && fadeTime < 5.45f)
        {
            game.batch.begin();
            PelletGame.backgroundSprite.draw(game.batch);
            if(drawLevelText)
            {
                PelletGame.font440.draw(game.batch, lvlChangeText,
                    gameport.getWorldWidth() / 2 - lvlTextWidth / 2, gameport.getWorldHeight() / 2 + lvlTextHeight / 2);
            }
            game.batch.end();
        }

        if (fadeTime >= 5.5f)
        {
            fadeTime = 0.0f;
        }

        if (enteringFromMenu)
        {
            calculateEnterToPlayStateFade(dt);

            game.batch.begin();
            PelletGame.backgroundSprite.draw(game.batch);
            game.batch.end();
        }

        //gameTable.debugAll();
        //lvlTable.debugAll();
        //gamestage.setDebugAll(true);
        //b2dr.render(world, gamecam.combined);
    }

    @Override
    public void resize(int width, int height)
    {
        if(doneInitialResize<2)
        {
            if (doneInitialResize < 1)
            {
                create();

                //Initialze Level Manager
                lmg = new LevelManager(gsm);
                lmg.createScales();
                scale = lmg.getScale();
                lmg.create();

                if(lmg.getCurrentlvl() != null)
                {
                    lmg.getCurrentlvl().loadToTable(lvlTable);
                    gameTable.addActor(lvlTable);
                    gameTable.add(menuButton);
                    gamestage.addActor(gameTable);
                    rayHandler.setAmbientLight(lmg.getCurrentlvl().getAmbientLight());


                }

                fadeTime = 3.6f;
                enteringFromMenu = true;
                //levelIsChanging = true;
            }

            //Update the Viewport
            gameport.update(width, height);
            doneInitialResize++;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose()
    {
        gamestage.dispose();
        world.dispose();
        b2dr.dispose();
        rayHandler.dispose();
        sr.dispose();
        super.dispose();
    }

    private float fadeAlpha = 1;
    private float fadeTime = 0;
    private void calculateEnterToPlayStateFade(float dt)
    {
        fadeTime+=dt;
        backgroundSpriteColor = game.backgroundSprite.getColor();
        game.batch.setColor(backgroundSpriteColor.r, backgroundSpriteColor.g, backgroundSpriteColor.b, fadeAlpha);
        PelletGame.backgroundSprite.setColor(game.batch.getColor());

        fadeAlpha-=dt/1.5f;


        if(fadeTime>=transitionTime/(11f/3f))
        {
            enteringFromMenu = false;
            fadeAlpha = 0;
            fadeTime = 0;
            goneToNextLevel = false;
            levelIsChanging = false;
        }

    }

    public static float transitionTime = 5.5f;
    private void calculateFadeAlpha(float dt)
    {
        fadeTime+=dt;

        backgroundSpriteColor = game.backgroundSprite.getColor();
        game.batch.setColor(backgroundSpriteColor.r, backgroundSpriteColor.g, backgroundSpriteColor.b, fadeAlpha);
        PelletGame.backgroundSprite.setColor(game.batch.getColor());
        PelletGame.font440.setColor(game.batch.getColor());


        if(fadeTime<=transitionTime/2.75f){fadeAlpha+=dt/(transitionTime/2.75f);}

        if(fadeAlpha>=0.98f && fadeTime < transitionTime/2.68f)
        {
            fadeAlpha = 1.0f;
        }

        if(!enteringFromMenu)calculateTextBounce(PelletGame.font440, dt);

        if(fadeTime>=transitionTime/1.57f)
        {
            if(!goneToNextLevel)
            {
                rayHandler.removeAll();
                lmg.goToNextLevel(world, gamestage, lvlTable, pellets, backGroundColors);
                getLevelSelectMenu().updateLvlMenu();
                addedMenu = false;
                levelsPassedSinceOpen++;
                goneToNextLevel = true;

                timePassed = -0.075f;
                hasSentObstaclesToPoints = false;
            }
            fadeAlpha-= dt/(transitionTime/2.75f);
        }

        if(fadeTime>transitionTime)
        {
            goneToNextLevel = false;
            levelIsChanging = false;
            fadeAlpha = 0;
            bouncetime = 0;
            fadeTime = 0;
            transitionTime = 5.5f;
        }
    }

    private float bouncetime = 0;
    private void calculateTextBounce(BitmapFont font, float dt)
    {
        if(bouncetime<=0.1)lvlChangeText = lmg.getLevelNum() + "";

        if (fadeTime > 2.75 && fadeTime <= 3)
        {
            bouncetime += dt;

            if (bouncetime <= 0.1)
            {
                font.getData().setScale(font.getScaleX() + (1.2857142857142857f) * dt / 2);
                lvlChangeText = lmg.getLevelNum() + "";
            }

            else if (bouncetime > 0.1 && bouncetime < 0.2)
            {
                font.getData().setScale(font.getScaleX() - (1.2857142857142857f) * dt / 2);
                lvlChangeText = lmg.getLevelNum() + 1 + "";
            }



        }
        lvlChangeLayout.setText(font, lvlChangeText);

        lvlTextWidth = lvlChangeLayout.width;
        lvlTextHeight = lvlChangeLayout.height;
    }

}
