package com.mygdx.game.Obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Reusables;

import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * Created by Lewis on 12/1/2017.
 */

public class Blank extends Obstacle
{
    private float SCALE;

    public Blank()
    {
        SCALE = PlayScreen.scale;
        //setBounds(getX(), getY(), 128/PlayScreen.PPM/SCALE, 128/PlayScreen.PPM/SCALE);
    }

    @Override
    public void defineBody() {

    }

    @Override
    public Sprite createScaledSprite(Texture texture)
    {
        Sprite sprite = new Sprite(texture);
        sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        sprite.setSize(sprite.getWidth() / PlayScreen.PPM/SCALE,
        sprite.getHeight() / PlayScreen.PPM/SCALE);
        return sprite;
    }

    @Override
    public String getUserData() {
        return null;
    }

    @Override
    public float getPosX() {
        return 0;
    }

    @Override
    public float getPosY() {
        return 0;
    }

    @Override
    public void setFull(boolean full) {

    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void destroyBody(World world) {

    }

    @Override
    public void setInitialized(boolean inited) {

    }

    @Override
    public void resize() {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void handleCollision(Pellet pellet) {

    }

    @Override
    public void actToggle() {

    }

    @Override
    public void setLocked(boolean isLocked) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public void setHit(int hit) {

    }
}

