package com.mygdx.game.Tools;

import com.badlogic.gdx.Screen;
import com.mygdx.game.PelletGame;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by Lewis on 8/19/2017.
 */

public class GameScreenManager
{
    private GameScreen currentscreen;
    private PelletGame game;

    public GameScreenManager(PelletGame game)
    {
        this.game = game;
    }

    public void setCurrentscreen(String screen)
    {
        if(screen.equals("playscreen")){currentscreen = new PlayScreen(game);}
        if(screen.equals("menuscreen")){currentscreen = new MenuScreen(game);}
        //if(screen.equals("lighttest")){currentscreen = new LightTest(game);}
    }


    public GameScreen getScreen(){return currentscreen;}
}
