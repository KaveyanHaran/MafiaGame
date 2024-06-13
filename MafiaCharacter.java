package entity;

import java.util.*;

/**
 * Represents a character in the game. Holds various attributes like suspicion
 * level, name, and more
 * 
 * @author Nathan Ye
 * @version 5/29/24
 */

public class MafiaCharacter
    implements Comparable<MafiaCharacter>
{

    private String                   name;

    private String                   role;

    private boolean                  isPlayer;

    private boolean                  isGood;

    private boolean                  isSaved;

    private boolean                  isProtected;

    private boolean                  isAlive;

    private int                      susometer    = 30;

    private TreeMap<String, Integer> accusedroles = new TreeMap<>();

    /**
     * Constructs a MafiaCharacter will the specified attributes.
     *
     * @param n
     *            the name of the character
     * @param r
     *            the role of the character
     * @param iP
     *            true if the character is controlled by a player, false
     *            otherwise
     */

    public MafiaCharacter(String n, String r, boolean iP)
    {

        name = n;

        role = r;

        isPlayer = iP;

        isGood = !r.equals("mafia");

        isSaved = false;

        isProtected = false;

        isAlive = true;

    }


    /**
     * Name of character returned
     *
     * @return name of character
     */

    public String getName()
    {

        return name;

    }


    /**
     * Set’s character status to dead
     */

    public void setDead()
    {

        isAlive = false;

    }


    /**
     * Role of character is returned
     *
     * @return Role of character
     */

    public String getRole()
    {

        return role;

    }


    /**
     * Check if character is controlled player
     *
     * @return if character is controlled return true otherwise false
     */

    public boolean checkPlayer()
    {

        return isPlayer;

    }


    /**
     * Checks if the character is good - not the Mafia
     *
     * @return true if the character is good otherwise false
     */

    public boolean checkGood()
    {

        return isGood;

    }


    /**
     * Checks if the character has been saved
     *
     * @return true if the character has been saved false otherwise
     */

    public boolean checkSaved()
    {

        return isSaved;

    }


    /**
     * Checks if the character is protected.
     *
     * @return true if character is protected and false otherwise
     */

    public boolean checkProtected()
    {

        return isProtected;

    }


    /**
     * Checks if the character is alive.
     *
     * @return true if the character is alive, false otherwise
     */

    public boolean checkAlive()
    {

        return isAlive;

    }


    /**
     * Alive status of the character is set
     *
     * @param b
     *            true to set the character as alive, false to set as dead
     */

    public void setAlive(boolean b)
    {

        isAlive = b;

    }


    /**
     * Sets the protected status of the character.
     *
     * @param b
     *            true to set the character as protected, false otherwise
     */

    public void setProtected(boolean b)
    {

        isProtected = b;

    }


    /**
     * Sets the saved status of the character.
     *
     * @param b
     *            true to set the character as saved, false otherwise
     */

    public void setSaved(boolean b)
    {

        isSaved = b;

    }


    /**
     * Sets the role of the character.
     *
     * @param s
     *            the new role of the character
     */

    public void setRole(String s)
    {

        role = s;

    }


    /**
     * Sets the good status of the character.
     *
     * @param b
     *            true to set the character as good, false otherwise
     */

    public void setGood(boolean b)
    {

        isGood = b;

    }


    /**
     * The sus level of character is returned
     *
     * @return the suspicion level of the character
     */

    public double getSus()
    {

        return susometer;

    }


    /**
     * Suspicion level of character is raised by a certain amount
     *
     * @param s
     *            the amount to increase the suspicion level
     */

    public void addSus(double s)
    {

        susometer += s;

    }


    /**
     * Characters get compared based of suspicion level
     *
     * @param c
     *            the character to compare to
     * @return the difference in suspicion levels between the characters
     */

    @Override

    public int compareTo(MafiaCharacter c)
    {

        return (int)(susometer - c.getSus());

    }


    /**
     * Returns the accused roles map which contains roles and their accusation
     * counts.
     *
     * @return the accused roles map
     */

    public TreeMap<String, Integer> getAccusedRoles()
    {

        return accusedroles;

    }


    /**
     * Updates accused role with suspicion level
     *
     * @param s
     *            the role to accuse
     * @param n
     *            # of accusations
     */

    public void addAccusedRole(String s, int n)
    {

        s = s.toLowerCase();

        accusedroles.put(s, accusedroles.getOrDefault(s, 0) + n);

    }


    /**
     * Returns the most suspicious role based on accusations.
     *
     * @return the most suspicious role, or "civilian" if no accusations exist
     */

    public String mostSusRole()
    {

        if (accusedroles.isEmpty())
        {

            return "civilian";

        }

        int max = 0;

        String s = "";

        for (String role : accusedroles.keySet())
        {

            if (accusedroles.get(role) > max)
            {

                max = accusedroles.get(role);

                s = role;

            }

        }

        return s.isEmpty() ? "civilian" : s;

    }

}
