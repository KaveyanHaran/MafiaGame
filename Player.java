package entity;

/**
 * Represents a player in the game with various attributes like name, wins,
 * games played, and the character themselves
 * Primary contributor: Kaveyan Kirubaharan
 */

public class Player
{

    private String         name;

    private int            wins;

    private int            games;

    private MafiaCharacter character;
    public static boolean  acted;
    public static boolean  hasVoted;

    /**
     * A player object gets constructed with a given character. All attributes
     * get initialized
     *
     * @param character
     *            MafiaCharacter which is assigned to the player
     */

    public Player(MafiaCharacter character)
    {

        this.name = character.getName();

        this.character = character;

        this.wins = 0;

        this.games = 0;
        hasVoted = false;
        acted = false;
    }


    /**
     * The name of player returned
     *
     * @return the name of the player
     */

    public String getName()
    {

        return name;

    }


    /**
     * Returns the number of wins the character has
     *
     * @return the number of wins
     */

    public int getWins()
    {

        return wins;

    }


    /**
     * Returns the number of games played by the player.
     *
     * @return the number of games played
     */

    public int getGames()
    {

        return games;

    }


    /**
     * Player’s win count get incremented by one
     */

    public void incrementWins()
    {

        this.wins++;

    }


    /**
     * Player’s games played gets incremented by one
     */

    public void incrementGames()
    {

        this.games++;

    }


    /**
     * Returns the character assigned to player
     *
     * @return the character assigned to the player
     */

    public MafiaCharacter getCharacter()
    {

        return character;

    }


    /**
     * Sets character assigned to player
     *
     * @param character
     *            the MafiaCharacter to assign to the player
     */

    public void setCharacter(MafiaCharacter character)
    {

        this.character = character;

    }

}
