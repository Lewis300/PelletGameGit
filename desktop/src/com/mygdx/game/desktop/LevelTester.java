package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.mygdx.game.Levels.LevelManager;
import com.mygdx.game.PelletGame;

import javax.swing.JOptionPane;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.PelletGame;

import java.util.ArrayList;

import static com.mygdx.game.PelletGame.openAgain;

/**
 * Created by Lewis on 11/30/2017.
 */

public class LevelTester
{
    public static void main(String[] args)
    {
        ArrayList<Integer> nums = new ArrayList<Integer>();
        //int num = Integer.parseInt(JOptionPane.showInputDialog("Enter level number"));

        while (true)
        {
            try{nums.add(Integer.parseInt(JOptionPane.showInputDialog("Enter level number")));}
            catch (Exception e)
            {
                break;
            }
        }



        LevelManager.isBeingUsedByLevelTester = true;
        LevelManager.lvlSequence = nums;


        LwjglApplication app;
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        app = new LwjglApplication(new PelletGame(), config);
        config.width = (int) (360 * 1.5);
        config.height = (int) (640 * 1.5);
        openAgain = false;


    }
}
