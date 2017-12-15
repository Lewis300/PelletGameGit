package com.mygdx.game.Buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.Screens.MenuScreen;

/**
 * Created by Lewis on 8/20/2017.
 */

public class PlayButton extends Actor
{

    private Sprite button;
    private Texture buttonPressed;
    private Texture buttonUnpressed;

    private Vector2 touchedDownCoords;
    private Vector2 touchedUpCoords;

    private boolean touchedDown = false;
    private boolean touchedUp = false;

    private Rectangle buttonRect;

    public PlayButton()
    {
        setTouchable(Touchable.enabled);

        buttonUnpressed = new Texture("Buttons/playButton.png");
        buttonPressed = new Texture("Buttons/playButtonPressed.png");

        button = createScaledSprite(buttonUnpressed);
        button.setOriginCenter();

        setOrigin(button.getOriginX(), button.getOriginY());
        setBounds(button.getX(), button.getY(), button.getWidth(), button.getHeight());

        touchedDownCoords = new Vector2();
        touchedUpCoords = new Vector2();

        buttonRect = new Rectangle();
        buttonRect.set(button.getX(), button.getY(), button.getWidth(), button.getHeight());



        addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                touchedDownCoords.set(x,y);
                touchedDown = true;
                touchedUp = false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                touchedUpCoords.set(x,y);
                touchedUp = true;
                touchedDown = false;
                super.touchUp(event, x, y, pointer, button);


            }
        });
    }

    @Override
    public void act(float delta)
    {
        if(touchedDown){button.set(createScaledSprite(buttonPressed));}
        if(touchedUp && buttonRect.contains(touchedDownCoords) && buttonRect.contains(touchedUpCoords))
        {
            //button.set(createScaledSprite(buttonUnpressed));

            touchedUp = false;
            getStage().addAction(Actions.sequence(Actions.alpha(1f), Actions.fadeOut(1f)));

        }
        else if(touchedUp)
        {
            button.set(createScaledSprite(buttonUnpressed));
            touchedUp = false;
        }
//        button.setX(getX()-button.getWidth()/2);
//        button.setY(getY()-button.getHeight()/2);
        button.setX(getX());
        button.setY(getY());

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a*parentAlpha);
        button.setColor(batch.getColor());

        button.draw(batch);
        batch.setColor(1,1,1,1);
    }

    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize(sprite.getWidth() / 1.5f,
        sprite.getHeight() /1.5f);
        return sprite;
    }

}
