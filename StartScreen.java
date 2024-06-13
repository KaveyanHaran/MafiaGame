package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Handles the implementation of the start game screen
 * 
 * @author Aarit Parekh
 * @version 5/29/24
 */
public class StartScreen
    extends JFrame
    implements style
{
    /**
     * Initializes the GUI
     */
    public StartScreen()
    {
        setTitle("Mafia Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Custom Font
        Font titleFont = new Font("Serif", Font.PLAIN, (int)48);
        Font buttonFont = new Font("Serif", Font.PLAIN, (int)24);

        // Create main panel with background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                // Draw a background image
                Image bgImage = new ImageIcon("background.jpg").getImage();
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Mafia Game");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold Color
        addShadowEffect(titleLabel, new Color(72, 61, 139), 2);

        // Start Game Button
        JButton startButton =
            createStyledButton("Start Game", buttonFont, "assets/images/start.png");
        startButton.addActionListener(e -> new MainGui(MafiaRunner.players));

        // Settings Button

        // Help button

        JButton helpButton = createStyledButton("Help", buttonFont, "assets/images/background.jpg");
        helpButton.addActionListener(e -> new MafiaRuleSheet().setVisible(true));

        // Exit Button
        JButton exitButton = createStyledButton("Exit", buttonFont, "assets/images/exit.png");
        exitButton.addActionListener(e -> System.exit(0));

        // Layout setup
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);
        gbc.gridy++;
        mainPanel.add(startButton, gbc);
        gbc.gridy++;

        mainPanel.add(helpButton, gbc);
        gbc.gridy++;
        mainPanel.add(exitButton, gbc);

        add(mainPanel);

    }


    /**
     * Creates a styled button
     * 
     * @param text
     *            button text
     * @param font
     *            button font
     * @param iconPath
     *            path to find the button icon
     * @return the styled button
     */

    public JButton createStyledButton(String text, Font font, String iconPath)
    {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 70));

        // Add icon to button
        ImageIcon icon = new ImageIcon(iconPath);
        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Set background color with an image
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBackground(Color.BLACK);
        //addBackgroundImage(button, iconPath);
        // originally going to add hover effect but kept glitching.
        // addHoverEffect(button, new Color(60, 110, 160, 150), new Color(90,
        // 150, 200, 150));
        return button;
    }


    /**
     * Adds a hover effect
     * 
     * @param button
     *            button to add effect
     * @param hoverBg
     *            background color
     * @param hoverFg
     *            foreground color
     */

    public void addHoverEffect(JButton button, Color hoverBg, Color hoverFg)
    {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt)
            {
                button.setBackground(hoverBg);
                button.setForeground(hoverFg);
            }


            public void mouseExited(MouseEvent evt)
            {
                button.setBackground(new Color(0, 0, 0, 0));
                button.setForeground(Color.WHITE);
            }
        });
    }


    /**
     * adds shadoow effect to a label
     * 
     * @param label
     *            label to add effect
     * @param shadowColor
     *            color to add
     * @param shadowOffset
     *            offset
     */

    public void addShadowEffect(JLabel label, Color shadowColor, int shadowOffset)
    {
        label.setForeground(shadowColor);
        label.setBorder(BorderFactory.createEmptyBorder(shadowOffset, shadowOffset, 0, 0));
    }


    /**
     * Adds a background image
     * 
     * @param button
     *            button to add
     * @param imagePath
     *            path of image
     */
    public void addBackgroundImage(JButton button, String imagePath)
    {
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c)
            {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawImage(
                    new ImageIcon(imagePath).getImage(),
                    0,
                    0,
                    c.getWidth(),
                    c.getHeight(),
                    c);
                super.paint(g, c);
            }
        });
    }
}
