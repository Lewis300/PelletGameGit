package com.mygdx.game.Misc;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.GameScreenManager;

/**
 * Created by Lewis on 11/17/2017.
 */
public class LevelButton extends TextButton
{
    private GameScreenManager gsm;
    public LevelButton(String text, Skin skin, GameScreenManager gsm)
    {
        super(text, skin);
        this.gsm = gsm;
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

            gsm.getScreen().closeLvlSelectMenu();
        }
        super.act(delta);
    }
}
