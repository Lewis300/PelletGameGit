package com.mygdx.game;

import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.PelletGame;

public class AndroidLauncher extends AndroidApplication
{
    private PelletGame game;

	@Override
	protected void onCreate (Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        game = new PelletGame();
		initialize(game, config);
        hideVirtualButtons();
	}

    @Override
    protected void onResume()
    {
        hideVirtualButtons();
        //PlayScreen.lmg.setLevelnum(PelletGame.prefs.getInteger("levelnum"));
        super.onResume();
    }

    public void hideVirtualButtons()
    {
         View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause()
    {
        //PelletGame.prefs.putInteger("levelnum", PlayScreen.lmg.getLevelNum());
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        game.dispose();
        finish();
        System.exit(0);
    }

}
