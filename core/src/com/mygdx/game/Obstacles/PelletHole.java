package com.mygdx.game.Obstacles;

/**
 * Created by Lewis on 8/25/2017.
 */

public abstract class PelletHole extends Obstacle
{
    public PelletHole()
    {
        super(null);
    }

    public abstract float getFillPelletRadius();

    public abstract float getSpawnX(char dir);

    public abstract float getSpawnY(char dir);
}
