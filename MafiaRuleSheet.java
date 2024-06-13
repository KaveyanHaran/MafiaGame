package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * creates the GUI for the Mafia Rulesheet
 * 
 * @author Kaveyan Kirubaharan
 * @version 5/29/24
 */
public class MafiaRuleSheet
    extends JFrame
{
    /**
     * initializes the MafiaRuleSheet GUI
     */

    public MafiaRuleSheet()
    {
        setTitle("Mafia Game Rulesheet");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                // Draw a background image
                Image bgImage = new ImageIcon("assets/images/rules_background.jpg").getImage();
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Create a scrollable text area for the rules
        JTextArea rulesText = new JTextArea();
        rulesText.setForeground(Color.BLACK);
        rulesText.setOpaque(false);
        rulesText.setEditable(false);
        rulesText.setText(loadRulesText());

        JScrollPane scrollPane = new JScrollPane(rulesText);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Title label
        JLabel titleLabel = new JLabel("Mafia Game Rulesheet", JLabel.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold color
        addShadowEffect(titleLabel, new Color(72, 61, 139), 2);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }


    /**
     * holds the rule text
     * 
     * @return rule text
     */

    private String loadRulesText()
    {
        return "Welcome to the Mafia Game!\n\n" + "Game Setup:\n" + "\n"
            + "- Assign roles secretly: Mafia, Detective, Doctor, Civilian.\n"
            + "- A moderator oversees the game.\n\n" + "Roles:\n"
            + "- Mafia: Knows other Mafia members. Eliminates one player each night.\n"
            + "- Detective: Investigates one player each night to learn if they are Mafia.\n"
            + "- Doctor: Protects a player every night, preferably civilians\n"
            + "- Civilian: Has no special abilities. Tries to identify Mafia members.\n\n"
            + "Phases:\n" + "1. Night Phase:\n" + "   - Mafia choose a target to eliminate.\n"
            + "   - Detective investigates a player.\n" + "   - Doctor protects a player.\n"
            + "2. Day Phase:\n" + "   - Players discuss and vote to eliminate a suspect.\n\n"
            + "Winning Conditions:\n"
            + "- Mafia win if they outnumber or equal the remaining players.\n"
            + "- Villagers win if all Mafia are eliminated.\n";
    }


    /**
     * Text styler
     * 
     * @param label
     *            JLabel to style
     * @param shadowColor
     *            color to add
     * @param shadowOffset
     *            offset to create
     */
    private void addShadowEffect(JLabel label, Color shadowColor, int shadowOffset)
    {
        label.setForeground(shadowColor);
        label.setBorder(BorderFactory.createEmptyBorder(shadowOffset, shadowOffset, 0, 0));
    }

}
