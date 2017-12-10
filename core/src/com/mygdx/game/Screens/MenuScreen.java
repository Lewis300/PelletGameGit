package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Buttons.PlayButton;
import com.mygdx.game.PelletGame;
import com.mygdx.game.PelletStuff.Pellet;

/**
 * Created by Lewis on 8/20/2017.
 */

public class MenuScreen extends GameScreen
{

    private PelletGame game;

    //Scene2D
    public static Stage menustage;
    private Table toptable;
    private Table bottomtable;
    private Table centertable;

    private Viewport menuport;

    public static boolean fadingOut = false;
    private static float fadeOutTime = 0;

    public MenuScreen(PelletGame game)
    {
        this.game = game;


        menuport = new ExtendViewport(PelletGame.WIDTH/PelletGame.PPM, PelletGame.HEIGHT/PelletGame.PPM);

        menustage = new Stage(menuport, game.batch);
        Gdx.input.setInputProcessor(menustage);

        toptable = new Table();
        toptable.top();

        bottomtable = new Table();
        bottomtable.bottom();

        centertable = new Table();
        centertable.center();
        centertable.setFillParent(true);

        centertable.add(new PlayButton()).center().fill();

        menustage.addActor(centertable);

        menustage.setDebugAll(true);

    }

    private void update(float dt)
    {
        if(fadeOutTime >=1f){
            PelletGame.gsm.setCurrentscreen("playscreen"); game.setTheScreen(PelletGame.gsm.getScreen());}

        if(fadingOut){fadeOutTime+=dt;}
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        //Clear screen
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(PelletGame.backgroundTex , 0-menuport.getWorldWidth()/2,0- menuport.getWorldHeight() , 4000,4000);
        game.batch.end();

        menustage.act(delta);
        menustage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose()
    {

        menustage.dispose();
    }
}
