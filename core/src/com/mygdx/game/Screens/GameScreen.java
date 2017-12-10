package com.mygdx.game.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.Misc.LevelSelectMenu;
import com.mygdx.game.PelletStuff.Pellet;

/**
 * Created by Lewis on 11/16/2017.
 */

public abstract class GameScreen implements Screen
{

    public Stage gamestage;

    private boolean inLvlMenu = false;
    public boolean closingMenu = false;
    private float deltaMinus = 0;
    private float minusAcceleration;
    private float stopTime = 0.3f;
    private float percentDt = 1;

    private LevelSelectMenu menu = new LevelSelectMenu();

    public float dt;

    public GameScreen()
    {
        minusAcceleration = (0 - percentDt)/stopTime;
    }



    @Override
    public void render(float delta)
    {
        if(!inLvlMenu){this.dt = delta;}
        update(delta);
    }

    public void openLvlSelectMenu()
    {
        inLvlMenu = true;
        closingMenu = false;
        addMenuToStage();
        menu.bringUp();
    }

    public void closeLvlSelectMenu()
    {
        inLvlMenu = false;
        closingMenu = true;
        menu.bringDown();
    }

    private void update(float delta)
    {
        if(inLvlMenu)
        {
            percentDt = Math.max(percentDt + minusAcceleration*delta, 0);
        }
        else
        {
            percentDt = Math.min(percentDt - minusAcceleration*delta, 1);
        }
        this.dt = delta*percentDt;

        if(menu.backButton.isChecked())
        {
            menu.backButton.setChecked(false);
            closeLvlSelectMenu();
        }
    }

    public boolean isInLvlMenu(){return inLvlMenu;}

    public boolean addedMenu = false;
    public void addMenuToStage()
    {
        if(!addedMenu)
        {
            gamestage.addActor(menu);
            addedMenu = true;
        }

    }

    @Override
    public void dispose()
    {
        menu.dispose();
    }

    public LevelSelectMenu getLevelSelectMenu(){return menu;}
}
