package com.mygdx.game.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mygdx.game.PelletGame;
import com.mygdx.game.Tools.AdHandler;

public class AndroidLauncher extends AndroidApplication implements AdHandler
{
    private PelletGame game;

    private static final String TAG = "AndroidLauncher";
    private String appId = "ca-app-pub-4181653067059521~8458411855";

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    protected InterstitialAd adView;

    private AdRequest.Builder builder = new AdRequest.Builder();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case SHOW_ADS:
                    adView.show();
                    break;
            }

        }
    };

	@Override
	protected void onCreate (Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        game = new PelletGame(this);
		initialize(game, config);
        hideVirtualButtons();

         //Create adview
        RelativeLayout layout = new RelativeLayout(this);
        View gameView = initializeForView(new PelletGame(this), config);
        layout.addView(gameView);

        adView = new InterstitialAd(this);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded()
            {
                Log.i("Ads", "Ad Loaded!");
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i)
            {
                adView.loadAd(builder.build());
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdClosed()
            {
                adView.loadAd(builder.build());
                super.onAdClosed();
            }
        });

        adView.setAdUnitId("ca-app-pub-4181653067059521/6714494465");

        builder.addTestDevice("E78A05D3A3D8C10414647F945276BEBA");
        adView.loadAd(builder.build());

        setContentView(layout);
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

    @Override
    public void showAds(boolean show)
    {
        handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }
}
