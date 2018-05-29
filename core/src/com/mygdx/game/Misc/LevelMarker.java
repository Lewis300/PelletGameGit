package com.mygdx.game.Misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Levels.LevelManager;
import com.mygdx.game.PelletGame;
import com.mygdx.game.Screens.PlayScreen;

//An actor that displays some text, preferably the number of the current level
public class LevelMarker extends Actor
{
    private float fontHeight;
    private float fontScale;
    public LevelMarker()
    {
        fontHeight = 173*((int) Math.log10(LevelManager.levelnum)+1);
        fontScale = getHeight()/fontHeight;
    }


    @Override
    public void act(float delta)
    {
        try
        {
            fontScale = getHeight()/fontHeight;
            PelletGame.markerFont.getData().setScale(fontScale);
        }
        catch (Exception e){}
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        PelletGame.markerFont.draw(batch, LevelManager.levelnum+"", getX(), getY()+getHeight());
        super.draw(batch, parentAlpha);
    }
}
