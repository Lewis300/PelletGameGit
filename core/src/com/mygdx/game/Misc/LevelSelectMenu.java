package com.mygdx.game.Misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.Levels.Level;
import com.mygdx.game.Levels.LevelManager;
import com.mygdx.game.PelletGame;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.mygdx.game.Tools.GameScreenManager;
import com.mygdx.game.Tools.Reusables;

import java.util.ArrayList;

import static com.badlogic.gdx.Input.Keys.Y;

/**
 * Created by Lewis on 11/16/2017.
 */

//TODO WORK ON MENU
public class LevelSelectMenu extends Table
{
    public boolean isUp = false;

    private Sprite border;
    private Sprite filler;

    public Button backButton;
    private Skin backButtonSkin;

    private Skin backGround;

    private float stageNegativeY;

    private ScrollPane pane;
    public static Skin paneSkin;
    private List list;
    public Table btnTable;

    private TextButton.TextButtonStyle btnStyle;

    private ArrayList<LevelButton> buttons;

    private GameScreenManager gsm;

    public LevelSelectMenu(GameScreenManager gsm)
    {
        this.gsm = gsm;
        setFillParent(true);
        center();

        backGround = new Skin(new TextureAtlas("Misc/LevelSelectMenu/levelSelectMenuBackGround.txt"));
        setBackground(backGround.getDrawable("levelSelectMenuBackground"));

        backButtonSkin = new Skin(new TextureAtlas("Buttons/BackButton/backButton.txt"));
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = backButtonSkin.getDrawable("back");
        style.down = backButtonSkin.getDrawable("backDown");
        backButton = new Button(style);

        backButton.setWidth(PelletGame.WIDTH/PlayScreen.PPM/16);
        backButton.setHeight(PelletGame.WIDTH/PlayScreen.PPM/16);
        backButton.left();

        add(backButton).size(PelletGame.WIDTH/PlayScreen.PPM/12, PelletGame.WIDTH/PlayScreen.PPM/12).left();
        top();
        left();
        row();


        //Pad sides
        padTop(PelletGame.HEIGHT/PlayScreen.PPM/15);
        padBottom(PelletGame.HEIGHT/PlayScreen.PPM/15);
        padLeft(PelletGame.WIDTH/PlayScreen.PPM/15);
        padRight(PelletGame.WIDTH/PlayScreen.PPM/15);

        paneSkin = new Skin(Gdx.files.internal("Misc/LevelSelectMenu/levelSelectMenu.json"), new TextureAtlas("Misc/LevelSelectMenu/levelSelectText.txt"));
        paneSkin.getFont("white").getData().setScale(0.1f);


        setSkin(paneSkin);
        list = new List(paneSkin);
        btnTable = new Table(paneSkin);


        //btnTable.setFillParent(true);

        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = paneSkin.getFont("white");
        btnStyle.fontColor = Color.BLUE;
        btnStyle.downFontColor = Color.BLACK;



        updateLvlMenu();
    }

    public void bringUp()
    {
        isUp = true;
        setY(-getStage().getHeight());
    }

    public void bringDown()
    {
        isUp = false;
    }

    @Override
    public void act(float delta)
    {
        delta = Gdx.graphics.getDeltaTime();
        if(isUp){setY(Math.min(getY() - getY()*delta/0.3f, 0));}
        else
        {
            setY(Math.max(getY() - getStage().getHeight()*delta/0.3f, -getStage().getHeight()));
        }
        super.act(delta);
    }

    public void updateLvlMenu()
    {
        buttons = new ArrayList<LevelButton>();

        for(int i = 0; i<LevelManager.maxLevel-1; i++)
        {
            buttons.add(new LevelButton(i+1+"", paneSkin, gsm));
        }

        btnTable.clear();
        btnTable.clearChildren();

        if(buttons.size()<LevelManager.maxLevel){buttons.add(new LevelButton(LevelManager.maxLevel+"", paneSkin, gsm));}
        for(int i = 1; i <= LevelManager.maxLevel; i++)
        {
            btnTable.add(buttons.get(i-1)).fill().expand().size(30, 30).top().center();
            if(i == LevelManager.maxLevel)
            {
                while(i%5 !=0)
                {
                    btnTable.add().fill().expand().size(30, 30).top().left();
                    i++;
                }
            }

            if(i%5 == 0 && i < LevelManager.maxLevel){btnTable.row();}

            if(i >= LevelManager.maxLevel)
            {
                btnTable.row();

                for(int j = 0; j<50*Math.ceil(LevelManager.maxLevel/50.0) - i; j++)
                {
                    //btnTable.add().fill().expand().size(30, 30).top().left();

                    if(j%5 == 0 && LevelManager.maxLevel <=40)
                    {
                        btnTable.add().fill().expand().size(30, 30).top().left();
                        btnTable.row();
                    }
                }

            }
        }
        //btnTable.debugAll();

        if(pane == null)
        {
            pane = new ScrollPane(btnTable, paneSkin);
        }
        else
        {
            pane.clearChildren();
            pane.setWidget(btnTable);
        }
        pane.setSize(getWidth(), getHeight());

        clear();
        clearChildren();

        add(backButton).size(PelletGame.WIDTH/PlayScreen.PPM/12, PelletGame.WIDTH/PlayScreen.PPM/12).left();
        top();
        left();
        row();

        //pane.setForceScroll(false, false);
        pane.setScrollingDisabled(true, false);
        add(pane).fill().expand().top().right();
    }


    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize(PlayScreen.gameport.getWorldWidth() - PlayScreen.gameport.getWorldWidth()/10,
        PlayScreen.gameport.getWorldHeight() - PlayScreen.gameport.getWorldHeight()/10);
        return sprite;
    }

    public void dispose()
    {
        backGround.getAtlas().dispose();
        backGround.dispose();
        backButtonSkin.getAtlas().dispose();
        backButtonSkin.dispose();
        paneSkin.getAtlas().dispose();
        paneSkin.dispose();
    }
}
