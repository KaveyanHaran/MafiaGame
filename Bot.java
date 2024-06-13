
package entity;

import game_engine.Game;

import java.util.*;

import GUI.chatroom;

/**
 * Represents an automated player in the mafia game. Interacts with other
 * classes based on the information it holds.
 * 
 * @author Nathan Ye
 * @version 5/29/24
 */

public class Bot
{

    private List<MafiaCharacter> information;

    private List<MafiaCharacter> characters;

    private MafiaCharacter       self;

    private int                  risk = 50;

    /**
     * Constructs instance of bot with the given MafiaCharacter features
     *
     * @param s
     *            the MafiaCharacter representing the bot
     */

    public Bot(MafiaCharacter s)
    {

        characters = Game.Characters;
        information = new ArrayList<MafiaCharacter>();

        self = s;

        if (s.getRole().toLowerCase().equals("mafia"))
        {

            for (int i = 0; i < characters.size(); i++)
            {

                if (!characters.get(i).getRole().toLowerCase().equals("mafia"))
                {

                    information
                        .add(new MafiaCharacter(characters.get(i).getName(), "unknowngood", false));

                }
                else
                {

                    information
                        .add(new MafiaCharacter(characters.get(i).getName(), "mafia", false));

                }

            }

        }
        else
        {

            for (int i = 0; i < characters.size(); i++)
            {

                information.add(new MafiaCharacter(characters.get(i).getName(), "unknown", false));

            }

        }

    }


    /**
     * The character representing bot gets returned
     *
     * @return the character representing the bot
     */

    public MafiaCharacter getCharacter()
    {

        return self;

    }


    /**
     * The action which is played based of Bot’s role
     */

    public void action()
    {

        if (self.getRole().toLowerCase().equals("mafia"))
        {

            Random r = new Random();

            while (true)
            {

                int num = r.nextInt(information.size() - 1);

                if (!information.get(num).getRole().toLowerCase().equals("mafia"))
                {

                    Game.mafiaKill(information.get(num).getName());

                    update();

                    break;

                }

            }

        }
        else if (self.getRole().toLowerCase().equals("detective"))
        {

            for (int i = 0; i < information.size(); i++)
            {

                if (information.get(i).getRole().equals("unknown"))
                {

                    information.get(i)
                        .setRole(Game.detectiveInvestigate(information.get(i).getName()));

                    if (information.get(i).getRole().toLowerCase().equals("mafia"))
                    {

                        information.get(i).addSus(999);

                    }
                    else
                    {

                        information.get(i).addSus(-999);

                    }

                    update();

                    break;

                }

            }

        }
        else if (self.getRole().toLowerCase().equals("doctor"))
        {

            Random r = new Random();
            for (int i = information.size() - 1; i >= 0; i--)
            {

                if (i > 0)
                {
                    if (r.nextDouble() > 0.8)
                    {
                        continue;
                    }
                }
                if (information.get(i).getRole().equals("unknown"))
                {
                    Game.doctorSave(information.get(i).getName());

                    update();

                    break;

                }

            }

        }

    }


    /**
     * Votes for a character.
     *
     * @return character name who the bot will vote for
     */

    public String vote()
    {
        update();
        Random random = new Random();
        double randomizer = random.nextDouble();
        if (risk * randomizer <= 20)
        {
            chatroom.gameLogArea.append(self.getName() + ": " + "I skipped here\n");
            return "SKIP";
        }
        if (self.checkGood())
        {

            for (MafiaCharacter c : information)
            {

                if (!c.getName().equals(self.getName()))
                {
                    chatroom.gameLogArea
                        .append(self.getName() + ": " + "I voted for " + c.getName() + "\n");
                    // System.out.println(self.getName() + " risk: " + risk);
                    return c.getName();

                }

            }

        }
        else
        {

            for (MafiaCharacter c : information)
            {

                if (!c.getName().equals(self.getName()))
                {

                    if (!c.checkGood() && c.getSus() > 100)
                    {

                        chatroom.gameLogArea
                            .append(self.getName() + ": " + "I voted for " + c.getName());
                        return c.getName();

                    }

                }

            }

        }

        return "";

    }


    /**
     * Bot’s info gets updated with current game state
     */

    public void update()
    {

        for (MafiaCharacter c : information)
        {
            c.setDead();
        }
        for (int i = 0; i < information.size(); i++)
        {

            for (int j = 0; j < characters.size(); j++)
            {

                if (information.get(i).getName().equals(characters.get(j).getName()))
                {

                    if (characters.get(j).checkAlive())
                    {

                        information.get(i).setAlive(true);

                    }

                }

            }

        }

        for (int i = information.size() - 1; i >= 0; i--)
        {

            if (!information.get(i).checkAlive())
            {

                if (self.checkGood())
                {

                    risk += 10;

                }
                else
                {

                    if (information.get(i).getRole().toLowerCase().equals("mafia"))
                    {

                        risk *= 2;

                    }
                    else
                    {

                        risk -= 10;

                    }

                }

                information.remove(i);

            }

        }

        Collections.sort(information, Collections.reverseOrder());

    }


    /**
     * The bot's suspicion tracker gets updated in a randomized way based on a
     * normal distribution.
     *
     * @param name
     *            , name of the character
     * @param n
     *            the amount added to suspicion
     */

    public void updatesus(String name, double n)
    {
        Random r = new Random();
        for (MafiaCharacter c : information)
        {

            if (c.getName().equals(name))
            {

                c.addSus((r.nextGaussian() + 1) * n);

            }

        }

    }


    /**
     * Process a chat message for a bot.
     *
     * @param msg
     *            the incoming message
     */

    public void receiveMsg(String msg)
    {

        String msgname = msg.split(": ")[0];

        String msgpart2 = msg.split(": ")[1];

        if (msgpart2.contains("I think "))
        {

            String accused = msg.split(" ")[2];

            String role = msg.split(" ")[4];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(accused, 10);

                updatesus(msgname, 5);

            }
            else
            {

                updatesus(accused, -10);

                updatesus(msgname, -5);

            }

        }
        else if (msgpart2.contains("I strongly suspect "))
        {

            String accused = msg.split(" ")[3];

            String role = msg.split(" ")[5];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(accused, 20);

                updatesus(msgname, 10);

            }
            else
            {

                updatesus(accused, -20);

                updatesus(msgname, -5);

            }

        }
        else if (msgpart2.contains("I knoweth for sure "))
        {

            String accused = msg.split(" ")[3];

            String role = msg.split(" ")[5];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(accused, 50);

                updatesus(msgname, 30);

            }
            else
            {

                updatesus(accused, -30);

                updatesus(msgname, -10);

            }

        }
        else if (msgpart2.contains("I don't think"))
        {

            String accused = msg.split(" ")[3];

            String role = msg.split(" ")[5];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(accused, -10);

                updatesus(msgname, 5);

            }
            else
            {

                updatesus(accused, 7);

                updatesus(msgname, 5);

            }

        }
        else if (msgpart2.contains("I really don't think"))
        {

            String accused = msg.split(" ")[4];

            String role = msg.split(" ")[6];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(accused, -20);

                updatesus(msgname, 7);

            }
            else
            {

                updatesus(accused, 10);

                updatesus(msgname, 5);

            }

        }
        else if (msgpart2.contains("I know that"))
        {

            String accused = msg.split(" ")[3];

            String role = msg.split(" ")[6];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(accused, -30);

                updatesus(msgname, 15);

            }
            else
            {

                updatesus(accused, 15);

                updatesus(msgname, -5);

            }

        }
        else if (msgpart2.contains("I AM"))
        {

            String role = msg.split(" ")[2];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(msgname, 200);

            }
            else if (role.toLowerCase().equals("civilian"))
            {

                updatesus(msgname, -15);

            }
            else
            {

                updatesus(msgname, 10);

            }

        }
        else if (msgpart2.contains("I am not"))
        {

            String role = msg.split(" ")[3];

            if (role.toLowerCase().equals("mafia"))
            {

                updatesus(msgname, -3);

            }
            else if (role.toLowerCase().equals("civilian"))
            {

                updatesus(msgname, 15);

            }
            else
            {

                updatesus(msgname, -5);

            }

        }
        else if (msgpart2.contains("Let's vote out "))
        {

            String accused = msg.split(" ")[3];

            if (information.size() > 6)
            {

                updatesus(msgname, 5);

            }

            updatesus(accused, 10);

        }
        else if (msgpart2.contains("I voted for "))
        {
            String accused = msg.split(" ")[3];

            if (information.size() > 6)
            {

                updatesus(msgname, 5);

            }

            updatesus(accused, 10);
        }
        else
        {

            if (information.size() <= 6)
            {

                updatesus(msgname, 10);

            }
            else
            {

                updatesus(msgname, -5);

            }

        }

    }


    /**
     * A message gets chosen to get sent to the chatroom
     *
     * @return the message to send
     */

    public String sendMsg()
    {

        Random r = new Random();

        double randomizer = r.nextDouble();
        randomizer = r.nextDouble();

        // Accuse

        if (randomizer < 0.4)
        {

            int randomindex = r.nextInt(information.size());

            if (information.get(randomindex).getName().equals(self.getName()))
            {

                if (randomindex != 0)
                {

                    randomindex--;

                }
                else
                {

                    randomindex++;

                }

            }

            randomizer = r.nextDouble();

            if (information.get(randomindex).getSus() * randomizer >= 80)
            {

                return this.getCharacter().getName() + ": " + "I knoweth for sure "
                    + information.get(randomindex).getName() + " is "
                    + information.get(randomindex).mostSusRole();

            }
            else if (information.get(randomindex).getSus() * randomizer >= 60)
            {

                return this.getCharacter().getName() + ": " + "I strongly suspect "
                    + information.get(randomindex).getName() + " is "
                    + information.get(randomindex).mostSusRole();

            }
            else
            {

                return this.getCharacter().getName() + ": " + "I think "
                    + information.get(randomindex).getName() + " is "
                    + information.get(randomindex).mostSusRole();

            }

        }

        // Disagree

        else if (randomizer < 0.8)
        {

            int randomindex = r.nextInt(information.size());

            if (information.get(randomindex).getName().equals(self.getName()))
            {

                if (randomindex != 0)
                {

                    randomindex--;

                }
                else
                {

                    randomindex++;

                }

            }

            randomizer = r.nextDouble();

            if (information.get(randomindex).getSus() * randomizer <= 10)
            {

                return this.getCharacter().getName() + ": " + "I know that "
                    + information.get(randomindex).getName() + " is not "
                    + information.get(randomindex).mostSusRole();

            }
            else if (information.get(randomindex).getSus() * randomizer <= 30)
            {

                return this.getCharacter().getName() + ": " + "I really don't think "
                    + information.get(randomindex).getName() + " is "
                    + information.get(randomindex).mostSusRole();

            }
            else
            {

                return this.getCharacter().getName() + ": " + "I don't think "
                    + information.get(randomindex).getName() + " is "
                    + information.get(randomindex).mostSusRole();

            }

        }

        // Defend

        else if (randomizer < 0.9)
        {

            randomizer = r.nextDouble();

            if (risk * randomizer >= 70)
            {

                ArrayList<String> options = new ArrayList<String>();

                options.add("civilian");

                options.add("detective");

                options.add("doctor");

                for (MafiaCharacter c : information)
                {

                    if (c.getName().equals(self.getName()))
                    {

                        continue;

                    }

                    if (options.size() <= 0)
                    {

                        break;

                    }

                    for (int j = 0; j < options.size();)
                    {

                        if (c.getAccusedRoles().containsKey(options.get(j)))
                        {

                            options.remove(options.get(j));

                        }
                        else
                        {

                            j++;

                        }

                    }

                }

                if (options.size() <= 0)
                {

                    return this.getCharacter().getName() + ": " + "I AM civilian";

                }
                else
                {

                    return this.getCharacter().getName() + ": " + "I AM "
                        + options.get(r.nextInt(options.size()));

                }

            }
            else
            {

                return this.getCharacter().getName() + ": " + "I am not " + self.mostSusRole();

            }

        }

        // Actions

        else
        {

            if (risk * randomizer <= 60)
            {

                return this.getCharacter().getName() + ": " + "Let's skip here";

            }
            else
            {

                return this.getCharacter().getName() + ": " + "Let's vote out "
                    + information.get(0).getName();

            }

        }

    }

}
