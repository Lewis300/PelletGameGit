package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.PelletStuff.Pellet;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by Lewis on 8/13/2017.
 */


public class WorldContactListener implements ContactListener
{
    //Identification variables and location variables
    public static String mirrorId;
    public static String pelletId;
    public static String collisionId;
    public static String acceptedDirs;
    public static char pelletDir;

    public static int mirrorX;
    public static int mirrorY;

    public static Fixture fixA;
    public static Fixture fixB;

    //Raw user data variables
    private String udataA;
    private String udataB;

    @Override
    public void beginContact(Contact contact)
    {

        fixA = contact.getFixtureA();
        fixB = contact.getFixtureB();

        //Save user data for each fixture
        udataA = fixA.getUserData().toString();
        udataB = fixB.getUserData().toString();

        String nums = "1234567890";


        if(udataA.charAt(0) == 'p')
        {
            udataB = udataA;
            udataA = fixB.getUserData().toString();
        }

        //Make sure that two of the same object are colliding
        if (udataA.charAt(0) != udataB.charAt(0))
        {
            //Save identification and location variables
            mirrorId = getObjectId(udataA);
            pelletId = getPelletId(udataB);

            collisionId = mirrorId + pelletId;

            acceptedDirs = getAcceptedDirs(udataA);
            pelletDir = getPelletDir(udataB);

            if (nums.contains(udataA.charAt(0)+"") && udataA.length()>5  && udataA.contains("x") && udataA.contains("y"))
            {
                mirrorX = getMirrorX(udataA);
                mirrorY = getMirrorY(udataA);
            }


            //If fixtureA is an end portal or start portal
            if (udataA.charAt(0) == 'e' || udataA.charAt(0) == 's')
            {
                //find the pellet with matching ID and tell it to handle collision
                for (Pellet pellet : PlayScreen.pellets)
                {

                    if (pelletId.equals(pellet.id + ""))
                    {

                        for(int i = 0; i<PlayScreen.lmg.getCurrentlvl().getPelletEnd().length; i++)
                        {
                            if((PlayScreen.lmg.getCurrentlvl().getPelletEnd()[i].getId()+"").equals(mirrorId))
                            {
                                PlayScreen.lmg.getCurrentlvl().getPelletEnd()[i].handleCollision(pellet);
                                break;
                            }
                        }
                        for(int i = 0; i<PlayScreen.lmg.getCurrentlvl().getPelletStart().length; i++)
                        {
                            if((PlayScreen.lmg.getCurrentlvl().getPelletStart()[i].getId()+"").equals(mirrorId))
                            {
                                PlayScreen.lmg.getCurrentlvl().getPelletStart()[i].handleCollision(pellet);
                                break;
                            }
                        }
                        break;
                    }

                }
            }

            //if fixtureA is a button
            else if(udataA.charAt(0) == 'b')
            {
                for(Pellet pellet : PlayScreen.pellets)
                {
                    if(pelletId.equals(pellet.id+""))
                    {
                        for(int i = 0; i<PlayScreen.lmg.getCurrentlvl().getObstacles().length; i++)
                        {
                            ////System.out.println(mirrorId);
                            if(mirrorId.equals(PlayScreen.lmg.getCurrentlvl().getObstacles()[i].getId()+""))
                            {

                                PlayScreen.lmg.getCurrentlvl().getObstacles()[i].handleCollision(pellet);
                                break;
                            }
                        }
                    }
                }
            }
            else if (udataA.charAt(0) == 't')
            {
                for(Pellet pellet : PlayScreen.pellets)
                {
                    if(pelletId.equals(pellet.id+""))
                    {
                        for(int i = 0; i<PlayScreen.lmg.getCurrentlvl().getWallPortals().length; i++)
                        {
                            if(mirrorId.equals(PlayScreen.lmg.getCurrentlvl().getWallPortals()[i].getId()+""))
                            {
                                PlayScreen.lmg.getCurrentlvl().getWallPortals()[i].handleCollision(pellet);

                                pellet.justInteracted = true;
                                pellet.collided = true;

                                break;
                            }
                        }
                        break;
                    }
                }
            }

            //Otherwise the pellet is colliding with a block or mirror
            else
            {
                //Find the pellet with the mathcing ID and tell it to handle collision
                for (Pellet pellet : PlayScreen.pellets)
                {

                    if (pelletId.equals(pellet.id + "") && !pellet.justInteracted)
                    {
                        ////System.out.print("L");
                        pellet.justInteracted = true;
                        pellet.collided = true;


                        for(int i = 0; i<PlayScreen.lmg.getCurrentlvl().getObstacles().length; i++)
                        {
                            if((PlayScreen.lmg.getCurrentlvl().getObstacles()[i].getId()+"").equals(mirrorId))
                            {
                                PlayScreen.lmg.getCurrentlvl().getObstacles()[i].handleCollision(pellet);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact)
    {
      //  ("CONTACT ENDED");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    //Method to extract the pellet directon from the given user data
    private static char  getPelletDir(String userdata)
    {
        String abc = "qwertyuiopasdfghjklzxcvbnm";
        char dir = 'u';

        for(int i = 0; i<userdata.length(); i++)
        {
            if(abc.contains(userdata.charAt(i)+"") && userdata.charAt(i)!='p'){dir = userdata.charAt(i);}
        }
        return dir;
    }

    //Method to extract a mirror or block's accepter directions from the given user data
    private static String getAcceptedDirs(String userdata)
    {
        String nums = "1234567890";
        String dirs = "";

        for(int i = 0; i<userdata.length(); i++)
        {
            if(!nums.contains(userdata.charAt(i)+"") && userdata.charAt(i) != 'x'){dirs = dirs + userdata.charAt(i);}
            if(userdata.charAt(i) == 'x'){break;}
        }
        return dirs;
    }

    //Method to extract the pellet ID from the given user data
    private static String getPelletId(String userdata)
    {
        String abc = "qwertyuiopasdfghjklzxcvbnm";
        String id = "";

        for(int i = 0; i<userdata.length(); i++)
        {
            if(!abc.contains(userdata.charAt(i)+"")){id = id+userdata.charAt(i)+"";}
        }
        return id;
    }

    //Method to extract the object (Actor) ID from the given user data
    private static String getObjectId(String userdata)
    {
        String abc = "qwertyuiopasdfghjklzxcvbnm";
        String nums = "";

        for(int i = 0; i<userdata.length(); i++)
        {
            if(!abc.contains(userdata.charAt(i)+"") && userdata.charAt(i) !='x'){nums = nums+userdata.charAt(i)+"";}
            if(userdata.charAt(i) =='x'){break;}
        }

        return nums;
    }

    //Method to extract the mirror or block's X coordinate from the given user data
    private static int getMirrorX(String userdata)
    {

        return Integer.parseInt(userdata.substring(userdata.indexOf("x")+1, userdata.indexOf("y")));
    }

    //Method to extract the mirror or block's Y coordinate from the given user data
    private static int getMirrorY(String userdata)
    {
        return Integer.parseInt(userdata.substring(userdata.indexOf("y")+1));
    }



}
