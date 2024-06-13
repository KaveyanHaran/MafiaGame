package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import entity.MafiaCharacter;
import entity.Player;
import game_engine.Game;

/**
 * Creates the Main GUI for the game
 * @author Aarit Parekh
 * @version 5/29/24
 */
public class MainGui
{
    private static JFrame            frame;
    private Game                     game;
    private static JTextArea         gameStatus;
    private JButton                  nextTurnButton;
    private static JComboBox<String> playerSelection;
    private JButton                  actionButton;
    private JLabel                   actionLabel;
    public static ArrayList<String>  nameList;
    public static String[]           names;

    /**
     * Initializes the main GUI
     * 
     * @param playerNames
     *            list of players
     */
    public MainGui(List<String> playerNames)
    {
        game = new Game(playerNames, this);
        playerSelection = new JComboBox<>(playerNames.toArray(new String[0]));
        initialize();
    }


    /**
     * Updates the nameList in MainGui by taking out dead characters
     */
    public static void updateNames()
    {
        nameList = new ArrayList<>();
        for (MafiaCharacter c : Game.Characters)
        {
            if (c.checkAlive())
            {
                nameList.add(c.getName());
            }
        }
        names = nameList.toArray(new String[0]);
        playerSelection.setModel(new DefaultComboBoxModel<>(names));
    }


    /**
     * Initializes the game
     */
    private void initialize()
    {
        frame = new JFrame("Mafia Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        gameStatus = new JTextArea();
        gameStatus.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameStatus);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.SOUTH);

        controlPanel.add(playerSelection);
        Game.assignRoles();
        showPlayer("You are: " + Game.player.getCharacter().getRole());

        actionButton = new JButton("Perform Action");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                performAction();
            }
        });
        controlPanel.add(actionButton);

        nextTurnButton = new JButton("Next Turn");
        nextTurnButton.addActionListener(new NextTurnListener());
        controlPanel.add(nextTurnButton);

        actionLabel = new JLabel("Select role to perform action");
        controlPanel.add(actionLabel);

        updateGameStatus();
        frame.setVisible(true);
    }


    public static void showPlayer(String s)
    {
        JOptionPane.showMessageDialog(frame, s);
    }


    /**
     * Adds the current status of the game
     */
    public static void updateGameStatus()
    {
        StringBuilder status = new StringBuilder();
        status.append("Day ").append(Game.getDayCount()).append(" - ")
            .append(Game.isDay ? "Day" : "Night").append("\n");

        status.append(Game.player).append("\n");

        gameStatus.setText(status.toString());
    }


    /**
     * Adds which character the player selected
     */
    private void updatePlayerSelection()
    {
        playerSelection.removeAllItems();
        for (String name : names)
        {
            playerSelection.addItem(name);
        }
        playerSelection.setSelectedIndex(-1); // Optionally, you can set to no
                                              // selection
    }


    /**
     * Adds implementation for performing actions
     */
    private void performAction()
    {
        if (game.isDay())
        {
            JOptionPane.showMessageDialog(frame, "Actions can only be performed at night.");
            return;
        }

        String selectedPlayerName = (String)playerSelection.getSelectedItem();
        MafiaCharacter selectedPlayer = game.findCharacterByName(selectedPlayerName);
        if (selectedPlayer == null)
        {
            JOptionPane.showMessageDialog(frame, "No player selected.");
            return;
        }

        String selectedRole = Game.player.getCharacter().getRole();
        switch (selectedRole)
        {
            case "mafia":
                game.setMafiaTarget(selectedPlayer);
                JOptionPane.showMessageDialog(
                    frame,
                    selectedRole + " targets " + selectedPlayerName + ".");
                Player.acted = true;
                Game.nextTurn();
                game.botActions();
                Game.mafiaKill(selectedPlayerName);
                break;
            case "doctor":
                game.setDoctorSave(selectedPlayer);
                JOptionPane.showMessageDialog(
                    frame,
                    selectedRole + " protects " + selectedPlayerName + ".");
                Player.acted = true;
                Game.nextTurn();
                game.botActions();
                Game.doctorSave(selectedPlayerName);
                break;
            case "detective":
                game.setDetectiveTarget(selectedPlayer);
                JOptionPane.showMessageDialog(
                    frame,
                    selectedRole + " investigates " + selectedPlayerName + ".");
                Player.acted = true;
                Game.nextTurn();
                game.botActions();
                JOptionPane.showMessageDialog(
                    frame,
                    selectedPlayerName + " is " + Game.detectiveInvestigate(selectedPlayerName)
                        + "\n");
                break;
            default:
                JOptionPane.showMessageDialog(frame, "You cannot take actions as this role.");
        }
    }

    /**
     * Adds backend code for the next turn button
     */
    private class NextTurnListener
        implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (Game.isDay)
            {
                JOptionPane.showMessageDialog(
                    frame,
                    "Chatroom is open. Close it before proceeding to the next turn.");
                return;
            }
            Game.nextTurn();
            if (!Player.acted)
            {
                game.botActions();
            }
            updateNames();
            updatePlayerSelection();
            if (game.checkWinCondition() != 0)
            {
                if (game.checkWinCondition() == 2)
                {
                    Game.playSound("game_engine\\villagerwin.wav");
                }
                else
                {
                    Game.playSound("game_engine\\mafiawin.wav");
                }
                String message = game.checkWinCondition() == 2 ? "Civilians win!" : "Mafia wins!";
                JOptionPane.showMessageDialog(
                    frame,
                    "Game Over! " + message + "\n" + "Mafia was: " + Game.mafia
                        + "\nDetective was: " + Game.detective + "\nDoctor was: " + Game.doctor);
                System.exit(1);
                nextTurnButton.setEnabled(false);
                actionButton.setEnabled(false);
            }
        }
    }

    // public static void main(String[] args)
    // {
    // SwingUtilities.invokeLater(() -> {
    // List<String> playerNames = List.of("Alice", "Bob", "Charlie", "David",
    // "Eve", "Frank");
    // new MainGui(playerNames);
    // });
    // }
}
