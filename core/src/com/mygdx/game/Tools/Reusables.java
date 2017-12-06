package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Lewis on 10/14/2017.
 */

public class Reusables
{
    //Blank
    public static Texture blankTex = new Texture("Obstacles/blank.png");

    //Buttons
    public static Texture selectLevelMenuButtonTexture = new Texture("Buttons/MenuButton/levelSelectMenuBtn.png");

    //Level Select menu
    public static Texture levelMenuBorderTex = new Texture("Misc/levelSelectMenuBorder.png");
    public static Texture levelMenuFillerTex = new Texture("Misc/levelSelectMenuFiller.png");

    //BoxBound
    public static Texture boxBound = new Texture("Obstacles/boxbound.png");

    //Mirror
    public static Texture mirrorTexture = new Texture("Obstacles/mirror.png");
    public static Texture whiteMirrorTexture = new Texture("Obstacles/mirrorWhite.png");

    //Block
    public static Texture blockTexture = new Texture("Obstacles/blockade.png");
    public static Texture blockBlankTex = new Texture("Obstacles/blockadeBlank.png");

    //Pellet_End
    public static Texture pelletEndTexture = new Texture("Obstacles/end.png");
    public static Texture pelletEndBlankTexture = new Texture("Obstacles/endBlank.png");
    public static Texture pelletAtEndTexture = new Texture("pellet.png");

    //Pellet_Start
    public static Texture pelletStartTex = new Texture("Obstacles/startWithLaser.png");

    //Toggle
    public static Texture toggleTexture = new Texture("Obstacles/toggleBlank.png");

    //TrackPoint
    public static Texture greyBorder = new Texture("Obstacles/Track/trackPoint.png");
    public static Texture coloredPoint = new Texture("Obstacles/Track/trackDot.png");


    public static void disposeAll()
    {
        mirrorTexture.dispose();
        whiteMirrorTexture.dispose();
        blockTexture.dispose();
        pelletEndTexture.dispose();
        pelletAtEndTexture.dispose();
        toggleTexture.dispose();
        greyBorder.dispose();
        coloredPoint.dispose();
        boxBound.dispose();
        pelletStartTex.dispose();
        blockBlankTex.dispose();
        pelletEndBlankTexture.dispose();
        selectLevelMenuButtonTexture.dispose();
        levelMenuBorderTex.dispose();
        levelMenuFillerTex.dispose();
    }
}
