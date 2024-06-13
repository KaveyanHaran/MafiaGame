package game_engine;

import entity.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import GUI.*;

/**
 * Represents the logic of the Mafia Game / manages the character the roles, day
 * / night cycle, and game state
 * Primary Contributor: Kaveyan Kirubaharan
 */

public class Game
{

    public static List<MafiaCharacter>     Characters;

    private static int                     dayCount;

    public static boolean                  isDay;

    private static MafiaCharacter          mafiaTarget;

    private static MafiaCharacter          doctorSave;

    private static MafiaCharacter          detectiveTarget;
    private static chatroom                c;

    public static Player                   player;

    public static List<Bot>                bots;

    public static HashMap<String, Integer> votes;
    public static String                   detective;
    public static String                   mafia;
    public static String                   doctor;

    /**
     * Constructs a Game instance with the character names which are given,
     * assigns roles, and the game state gets initalized.
     * 
     * @param CharacterNames
     *            list of names for the game characters
     */

    public Game(List<String> CharacterNames, MainGui g)
    {
        Characters = new ArrayList<>();
        votes = new HashMap<String, Integer>();

        for (String name : CharacterNames)
        {

            Characters.add(new MafiaCharacter(name, "civilian", false));

        }

        Characters.add(new MafiaCharacter("Player", "civilian", true));

        assignRoles();

        dayCount = 1;

        isDay = false;

    }


    /**
     * Characters roles are assigned, character list gets shuffled, and player
     * and bot instances get initalized
     */

    public static void assignRoles()
    {
        playSound("game_engine\\start.wav");
        bots = new ArrayList<Bot>();

        Collections.shuffle(Characters);

        Characters.get(0).setRole("mafia");
        mafia = Characters.get(0).getName();

        Characters.get(1).setRole("detective");
        detective = Characters.get(1).getName();

        Characters.get(2).setRole("doctor");
        doctor = Characters.get(2).getName();

        for (int i = 3; i < Characters.size(); i++)
        {

            Characters.get(i).setRole("civilian");

        }
        Collections.shuffle(Characters);
        for (int i = 0; i < Characters.size(); i++)
        {

            if (Characters.get(i).checkPlayer())
            {

                player = new Player(Characters.get(i));

            }
            else
            {

                bots.add(new Bot(Characters.get(i)));

            }

        }
    }


    /**
     * Player instance gets returned.
     *
     * @return the player instance
     */

    public Player getPlayer()
    {

        return player;

    }


    /**
     * Plays a sound from the specified file.
     *
     * @param soundFileName
     *            the name of the sound file to play
     */
    public static void playSound(String soundFileName)
    {
        try
        {
            File soundFile = new File(soundFileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Next turn gets proceeded to and day / night gets “switched”
     */

    public static void nextTurn()
    {

        if (isDay)
        {
            votes = new HashMap<String, Integer>();
            executeNightActions();
            Player.hasVoted = false;
            Player.acted = false;
            dayCount++;
            c.closeChatroom();
        }
        else
        {
            c = new chatroom(new Player(new MafiaCharacter("Player", "mafia", true)));
        }

        isDay = !isDay;
        MainGui.updateGameStatus();
    }


    /**
     * Actions that occur during the night gets executed Including mafia kills
     * and doctor saves.
     */

    private static void executeNightActions()
    {

        if (mafiaTarget != null && doctorSave != mafiaTarget)
        {

            mafiaTarget.setDead();

        }

        detectiveTarget = null;

        mafiaTarget = null;

        doctorSave = null;

    }


    /**
     * Returns if it is currently day.
     *
     * @return true if it is day and false if it is night
     */

    public boolean isDay()
    {

        return isDay;

    }


    /**
     * Returns the current day count
     *
     * @return the current day count
     */

    public static int getDayCount()
    {

        return dayCount;

    }


    /**
     * Returns the list of all characters in game
     *
     * @return the list of all characters
     */

    public List<MafiaCharacter> getCharacters()
    {

        return Characters;

    }


    /**
     * Sets the target for the mafia to kill during night
     *
     * @param target,
     *            target character for mafia
     */

    public void setMafiaTarget(MafiaCharacter target)
    {

        mafiaTarget = target;

    }


    /**
     * Sets the target for the doctor to save during the night.
     *
     * @param target
     *            the target character for the doctor
     */

    public void setDoctorSave(MafiaCharacter target)
    {

        doctorSave = target;

    }


    /**
     * target set for the detective to investigate during the night.
     *
     * @param target,
     *            target character for the detective
     */

    public void setDetectiveTarget(MafiaCharacter target)
    {

        detectiveTarget = target;

    }


    /**
     * If there are any detective target returns it.
     *
     * @return an Optional containing the detective's target, empty if none
     */

    public Optional<MafiaCharacter> getDetectiveTarget()
    {

        return Optional.ofNullable(detectiveTarget);

    }


    /**
     * Checks if win condition is met by either parties
     *
     * @return 0 if the win condition is not met, 1 if mafia won, and 2 if
     *         civilians won
     */

    public int checkWinCondition()
    {

        long mafiaCount =
            Characters.stream().filter(p -> p.getRole().equals("mafia") && p.checkAlive()).count();

        long villagerCount =
            Characters.stream().filter(p -> !p.getRole().equals("mafia") && p.checkAlive()).count();

        if (mafiaCount == 0)
        {
            return 2;
        }
        else if (mafiaCount >= villagerCount)
        {
            return 1;
        }
        return 0;

    }


    /**
     * Returns the list of all alive characters in the game.
     *
     * @return the list of all alive characters
     */

    public List<MafiaCharacter> getAliveCharacters()
    {

        ArrayList<MafiaCharacter> alivechar = new ArrayList<MafiaCharacter>();
        for (MafiaCharacter c : Game.Characters)
        {
            if (c.checkAlive())
            {
                alivechar.add(c);
            }
        }
        return alivechar;

    }


    /**
     * Returns the list of all bot instances in the game.
     *
     * @return the list of all bot instances
     */

    public List<Bot> getBots()
    {

        return bots;

    }


    /**
     * Character found by name
     *
     * @param name
     *            the name of the character
     * @return the character with the given name, or null if not found
     */

    public MafiaCharacter findCharacterByName(String name)
    {

        return Characters.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);

    }


    /**
     * Processes day action - vote
     *
     * @param action
     *            , action to be processed, vote for player to be eliminated
     */

    public static void handleDayAction(String action)
    {

        // Process voting logic

        votes.put(action, votes.getOrDefault(action, 0) + 1);

        // Assume day ends when all players have voted
        int votecount = 0;
        for (String s : votes.keySet())
        {
            votecount += votes.get(s);
        }

        if (votecount >= Characters.size())
        {
            playSound("game_engine\\vote.wav");
            String eliminatedPlayer = getMaxVotedPlayer();
            if (eliminatedPlayer.equals("SKIP"))
            {
                chatroom.gameLogArea.append("No one was voted out! (skip or tie)\n");
                chatroom.gameLogArea.append("Night falls.\n");
                chatroom.gameLogArea.append("Chat will close in 3 seconds...\n");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run()
                    {
                        Game.nextTurn();
                        MainGui.updateGameStatus();
                    }
                }, 3000);
                votes.clear();
                return;
            }
            chatroom.gameLogArea.append(eliminatedPlayer + " was voted out!\n");

            chatroom.gameLogArea.append("Night falls.\n");
            chatroom.gameLogArea.append("Chat will close in 3 seconds...\n");

            new Timer().schedule(new TimerTask() {
                @Override
                public void run()
                {
                    Game.nextTurn();
                }
            }, 3000);

            eliminatePlayer(eliminatedPlayer);

            votes.clear();
            // System.out.println(Characters.size());

        }

    }


    /**
     * Determines the player with amount of votes which is the highest in amount
     * 
     * @return the name of the player with the most votes
     */

    private static String getMaxVotedPlayer()
    {

        String maxVotedPlayer = null;

        int maxVotes = 0;
        int secondmaxVotes = -1;

        for (String player : votes.keySet())
        {

            int voteCount = votes.get(player);

            if (voteCount >= maxVotes)
            {

                secondmaxVotes = maxVotes;
                maxVotes = voteCount;
                maxVotedPlayer = player;

            }

        }
        if (maxVotes == secondmaxVotes)
        {
            return "SKIP";
        }
        return maxVotedPlayer;

    }


    /**
     * Eliminates the player with the name which is given
     *
     * @param playerName
     *            name of the player to be eliminated
     */

    public static void eliminatePlayer(String playerName)
    {
        if (playerName.equals("Player"))
        {
            playSound("game_engine\\playerdeath.wav");
            MainGui.showPlayer("You died...");
            MainGui.showPlayer(
                "Mafia was: " + Game.mafia + "\nDetective was: " + Game.detective + "\nDoctor was: "
                    + Game.doctor);
            System.exit(1);
        }
        for (int i = 0; i < Characters.size(); i++)
        {

            if (Characters.get(i).getName().equals(playerName))
            {

                Characters.get(i).setAlive(false);
                Characters.remove(i);

                break;

            }

        }
        chatroom.updateNames();
        MainGui.updateNames();
    }


    /**
     * Bots vote during the day
     */

    public static void botsVote()
    {

        List<Bot> bots = Game.bots;

        for (Bot b : bots)
        {

            if (b.getCharacter().checkAlive())
            {

                handleDayAction(b.vote());

            }

        }

    }


    /**
     * Processes a kill action by the mafia.
     *
     * @param target
     *            , name of the target to be killed by the mafia
     */

    public static void mafiaKill(String target)
    {

        for (MafiaCharacter character : Characters)
        {

            if (character.getName().equals(target) && character.checkAlive())
            {

                if (character.checkSaved())
                {

                    chatroom.gameLogArea.append(target + " was saved by the doctor!\n");

                }
                else
                {

                    eliminatePlayer(target);
                    chatroom.updateNames();
                    chatroom.gameLogArea.append(target + " was killed by the mafia!\n");
                    playSound("game_engine\\kill.wav");
                }

                break;

            }

        }

    }


    /**
     * Investigation by detective gets processed
     *
     * @param target
     *            , name of target to be investigated
     * @return role of target being investigated
     */

    public static String detectiveInvestigate(String target)
    {

        for (MafiaCharacter character : Characters)
        {

            if (character.getName().equals(target) && character.checkAlive())
            {

                return character.getRole().toLowerCase();

            }

        }

        return "";

    }


    /**
     * Save action by the doctor gets processed.
     *
     * @param target
     *            , name of the target to be saved by the doctor
     */

    public static void doctorSave(String target)
    {

        for (MafiaCharacter character : Characters)
        {

            if (character.getName().equals(target) && character.checkAlive())
            {

                character.setSaved(true);

                chatroom.gameLogArea.append(target + " was protected by the doctor!\n");
                for (Bot b : bots)
                {
                    b.updatesus(target, -999);
                }
                break;

            }

        }

    }


    /**
     * Calls the action of all bots
     */
    public void botActions()
    {
        for (Bot b : bots)
        {
            if (b.getCharacter().checkAlive())
            {
                b.action();
                // System.out.println("Called action for " +
                // b.getCharacter().getName());
            }
        }
    }

}
