package com.mygdx.game.Buttons;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.PelletGame;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Reusables;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * Created by Lewis on 11/16/2017.
 */

public class SelectLevelMenuButton extends Actor
{
    private Sprite btn;

    public SelectLevelMenuButton()
    {
        btn = createScaledSprite(Reusables.selectLevelMenuButtonTexture);
        setWidth(btn.getWidth());
        setHeight(btn.getHeight());
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
               // openMenu(PelletGame.gsm.getScreen());
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        btn.draw(batch);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta)
    {
        btn.setPosition(PlayScreen.gameport.getWorldWidth() - btn.getWidth(), PlayScreen.gameport.getWorldHeight() - btn.getHeight());
        super.act(delta);
    }

    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize(PlayScreen.gameport.getWorldWidth()/10,
        PlayScreen.gameport.getWorldWidth()/10);
        return sprite;
    }

    //private void openMenu(GameScreen screen)
    {
       // screen.openMenu();
    }
}
