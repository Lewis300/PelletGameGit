package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.Levels.LevelManager;
import com.mygdx.game.Misc.LevelSelectMenu;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.GameScreenManager;
import com.mygdx.game.Tools.Reusables;
import com.mygdx.game.Tools.SmartFontGenerator;

public class PelletGame extends Game
{
	public static Application.ApplicationType appType;
	public SpriteBatch batch;
    public static Preferences prefs;
    public static final int WIDTH = 720;
    public static final int HEIGHT = 1280;
    public static final float PPM = 3.5f;

    public GameScreenManager gsm;

    public static Texture backgroundTex;
    public static Sprite backgroundSprite;

    public static BitmapFont font440;
    public static BitmapFont font44;


    public static boolean openAgain = true;

	@Override
	public void create ()
    {
        //Load saved data from previous runs of the game
        prefs = Gdx.app.getPreferences("game.prefs");

        sfg = new SmartFontGenerator();
        initFonts();

        backgroundTex = new Texture("backGroundColor.png");
        backgroundSprite = new Sprite(backgroundTex);

        appType = Gdx.app.getType();

        //Create spritebatch (used to draw things to the screen)
		batch = new SpriteBatch();
        gsm = new GameScreenManager(this);
        gsm.setCurrentscreen("playscreen");
        setTheScreen(gsm.getScreen());

        new LevelSelectMenu(gsm);


        Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render ()
	{
		if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            create();
            Gdx.app.exit();
        }
        super.render();
	}

	@Override
	public void dispose ()
    {
        Reusables.disposeAll();
        super.dispose();

        if(LevelManager.isBeingUsedByLevelTester)
        {
            openAgain = true;
        }
    }

    public void setTheScreen(Screen screen)
    {
        setScreen(screen);
    }

    public static BitmapFont markerFont;

    public static SmartFontGenerator sfg;

    //Loads fonts into the game for later drawing
    private static void initFonts()
    {
        try
        {
            font44 = sfg.createFont(Gdx.files.local("Fonts/BPtypewrite.ttf"), "endfont", 44);

            font44.getData().setScale(0.2857142857142857f);

            font440 = new BitmapFont(sfg.getFontFile("levelfont.fnt", 440));
            font440.getData().setScale(0.2857142857142857f);

            markerFont = new BitmapFont(sfg.getFontFile("levelfont.fnt", 440));

            prefs.putBoolean("createdFont", true);
            prefs.flush();
        }
        catch (Exception e)
        {
            initFonts();
        }
    }


}
