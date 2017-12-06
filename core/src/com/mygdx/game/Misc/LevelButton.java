package com.mygdx.game.Misc;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.Levels.LevelManager;
import com.mygdx.game.PelletGame;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by Lewis on 11/17/2017.
 */
public class LevelButton extends TextButton
{
    public LevelButton(String text, Skin skin)
    {
        super(text, skin);
    }

    @Override
    public void act(float delta)
    {
        if(isChecked())
        {
            setChecked(false);
            PlayScreen.lmg.setLevelnum(Integer.parseInt(getText().toString()) - 1);
            PlayScreen.levelIsChanging = true;
            PlayScreen.drawLevelText = false;
            PlayScreen.transitionTime = 2.75f;

            PelletGame.gsm.getScreen().closeLvlSelectMenu();
        }
        super.act(delta);
    }
}
