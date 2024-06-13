package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import entity.Bot;
import entity.MafiaCharacter;
import entity.Player;
import game_engine.Game;

/**
 * contains the implementation for the Mafia Chatroom
 * 
 * @author Aarit Parekh
 * @version 5/29/24
 */
public class chatroom
{
    private JFrame                   frame;
    public static JTextArea          gameLogArea;
    private JTextField               actionInput;
    private JButton                  accuseButton, defendButton, disagreeButton, actionButton,
        voteButton;
    private static JComboBox<String> nameComboBox, roleComboBox, optionsComboBox;
    public static ArrayList<String>  nameList;
    public static String[]           names;   // =
    // { "Mr.Fulk", "Mr.Beast", "Chandler", "aarit", "kaveyan", "nathan",
    // "Mr.Kwong" };
    public static String[]           options; // =
    // { "Mr.Fulk", "Mr.Beast", "Chandler", "aarit", "kaveyan", "nathan",
    // "Mr.Kwong", "SKIP" };
    public static final String[]     roles = { "mafia", "detective", "doctor", "civilian" };
    private Player                   player;

    /**
     * Initializes the Chatroom
     * 
     * @param p
     *            instance of player created
     */
    public chatroom(Player p)
    {
        nameList = new ArrayList<String>();
        for (MafiaCharacter c : Game.Characters)
        {
            if (c.checkAlive())
            {
                nameList.add(c.getName());
            }
        }
        names = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++)
        {
            names[i] = nameList.get(i);
        }
        nameList.add("SKIP");
        options = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++)
        {
            options[i] = nameList.get(i);
        }
        player = p;
        Player.hasVoted = false;
        frame = new JFrame("Chatroom");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Game Log Panel
        gameLogArea = new JTextArea();
        gameLogArea.setEditable(false);
        JScrollPane gameLogScrollPane = new JScrollPane(gameLogArea);
        gameLogScrollPane.setBorder(BorderFactory.createTitledBorder("Game Log"));

        // Action Panel
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionInput = new JTextField();
        accuseButton = new JButton("Accuse");
        defendButton = new JButton("Defend");
        disagreeButton = new JButton("Disagree");
        actionButton = new JButton("Action");
        voteButton = new JButton("Vote");
        nameComboBox = new JComboBox<>(names);
        roleComboBox = new JComboBox<>(roles);
        optionsComboBox = new JComboBox<>(options);

        accuseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showAccuseOptions();
            }
        });

        defendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showDefendOptions();
            }
        });

        disagreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showDisagreeOptions();
            }
        });

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showActionOptions();
            }
        });
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showVoteOptions();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(accuseButton);
        buttonPanel.add(defendButton);
        buttonPanel.add(disagreeButton);
        buttonPanel.add(actionButton);
        buttonPanel.add(voteButton);

        actionPanel.add(actionInput, BorderLayout.CENTER);
        actionPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(gameLogScrollPane, BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }


    /**
     * Updates the nameLists in chatroom by taking out dead characters
     */
    public static void updateNames()
    {
        nameList = new ArrayList<String>();
        for (MafiaCharacter c : Game.Characters)
        {
            if (c.checkAlive())
            {
                nameList.add(c.getName());
            }
        }
        names = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++)
        {
            names[i] = nameList.get(i);
        }
        nameList.add("SKIP");
        options = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++)
        {
            options[i] = nameList.get(i);
        }
        nameComboBox = new JComboBox<>(names);
        optionsComboBox = new JComboBox<>(options);
    }


    /**
     * Closes the chatroom
     */
    public void closeChatroom()
    {
        frame.dispose();
    }


    /**
     * Sends a message to all bots in the game.
     *
     * @param msg
     *            the message to be sent to the bots
     */

    public static void sendBots(String msg)
    {

        for (Bot b : Game.bots)
        {

            b.receiveMsg(msg);

        }

    }


    /**
     * Handles chat logic
     */

    public static void handleChat()
    {

        List<Bot> bots = Game.bots;

        ArrayList<Bot> aliveBots = new ArrayList<Bot>();

        for (Bot b : bots)
        {

            if (b.getCharacter().checkAlive())
            {

                aliveBots.add(b);

            }

        }

        Random r = new Random();

        int index = r.nextInt(aliveBots.size());

        String s = aliveBots.get(index).sendMsg();
        // System.out.println(s);
        sendBots(s);

        chatroom.gameLogArea.append(s + "\n");

    }


    /**
     * handles chatroom actions
     */
    private void handleAction()
    {
        String action = actionInput.getText();
        if (action.isEmpty())
        {
            JOptionPane.showMessageDialog(
                frame,
                "Message cannot be empty!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        gameLogArea.append(player.getName() + ": " + action + "\n");
        actionInput.setText("");
        sendBots(player.getName() + ": " + action);
        if (Player.hasVoted)
        {
            return;
        }
        handleChat();
    }


    /**
     * shows the accuse options in a scrollbar
     */
    private void showAccuseOptions()
    {
        String[] options =
            { "I think %s is %s", "I strongly suspect %s is %s", "I knoweth for sure %s is %s" };
        String selectedOption = (String)JOptionPane.showInputDialog(
            frame,
            "Select an accuse message:",
            "Accuse",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
        if (selectedOption != null)
        {
            showNameRoleDialog(selectedOption);
        }
    }


    /**
     * shows the defend options in a scrollbar
     */
    private void showDefendOptions()
    {
        String[] options = { "I AM %s", "I am not %s" };
        String selectedOption = (String)JOptionPane.showInputDialog(
            frame,
            "Select a defend message:",
            "Defend",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
        if (selectedOption != null)
        {
            showRoleDialog(selectedOption);
        }
    }


    /**
     * shows disagree options in a scrollbar
     */
    private void showDisagreeOptions()
    {
        String[] options = { "I don't think %s is %s", "I really don't think %s is %s",
            "I know that %s is not %s" };
        String selectedOption = (String)JOptionPane.showInputDialog(
            frame,
            "Select a disagree message:",
            "Disagree",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
        if (selectedOption != null)
        {
            showNameRoleDialog(selectedOption);
        }
    }


    /**
     * Shows the options the player has for suggesting actions
     */
    private void showActionOptions()
    {
        String[] options = { "Let's vote out %s", "Let's skip here" };
        String selectedOption = (String)JOptionPane.showInputDialog(
            frame,
            "Select an action message:",
            "Suggest Action",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
        if (selectedOption != null)
        {
            showNameDialog(selectedOption);
        }
    }


    /**
     * Shows the characters which the player can vote for.
     */

    private void showVoteOptions()
    {
        String[] options = { "I voted for %s" };
        String selectedOption = (String)JOptionPane.showInputDialog(
            frame,
            "Select a person to vote",
            "Vote another character",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
        if (selectedOption != null)
        {
            showVoteDialog(selectedOption);
        }
    }


    /**
     * Helper method that displays the name and role of a character
     * 
     * @param messageTemplate
     *            input param based on selection
     */

    private void showNameRoleDialog(String messageTemplate)
    {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameComboBox);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Select Name and Role",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            String name = (String)nameComboBox.getSelectedItem();
            String role = (String)roleComboBox.getSelectedItem();
            String message = String.format(messageTemplate, name, role);
            actionInput.setText(message);
            handleAction();
        }
    }


    /**
     * Helper method that displays the role of a character
     * 
     * @param messageTemplate
     *            input param based on selection
     */

    private void showRoleDialog(String messageTemplate)
    {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Select Role",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            String role = (String)roleComboBox.getSelectedItem();
            String message = String.format(messageTemplate, role);
            actionInput.setText(message);
            handleAction();
        }
    }


    /**
     * Helper method that displays the name of a character
     * 
     * @param messageTemplate
     *            input param based on selection
     */
    private void showNameDialog(String messageTemplate)
    {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameComboBox);

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Select Name",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            String name = (String)nameComboBox.getSelectedItem();
            String message = String.format(messageTemplate, name);
            actionInput.setText(message);
            handleAction();
        }
    }


    /**
     * Helper method that displays the vote options
     * 
     * @param messageTemplate
     *            input param based on selection
     */

    private void showVoteDialog(String messageTemplate)
    {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Name:"));
        panel.add(optionsComboBox);

        int result = JOptionPane.showConfirmDialog(
            frame,
            panel,
            "Select Name",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION)
        {
            if (Player.hasVoted == false)
            {
                String name = (String)optionsComboBox.getSelectedItem();
                String message = String.format(messageTemplate, name);
                if (name.equals("SKIP"))
                {
                    message = "I skipped here";
                }
                actionInput.setText(message);
                Player.hasVoted = true;
                handleAction();
                if (name.equals("SKIP"))
                {
                    Game.handleDayAction("SKIP");
                }
                else
                {
                    Game.handleDayAction(message.split(" ")[3]);
                }
                Game.botsVote();
                // System.out.println(Game.votes);
            }
        }
    }

    // public static void main(String[] args)
    // {
    // SwingUtilities.invokeLater(new Runnable() {
    // @Override
    // public void run()
    // {
    // MafiaCharacter c = new MafiaCharacter("nyeezy", "mafia", true);
    // Player p = new Player(c);
    // new chatroom(p);
    // }
    // });
    // }

}
