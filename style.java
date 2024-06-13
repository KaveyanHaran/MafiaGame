package GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * interface containing some style effects
 * @author Aarit Parekh
 * @version 5/29/24
 */
public interface style {
    public abstract JButton createStyledButton(String text, Font font, String iconPath);

    public abstract void addHoverEffect(JButton button, Color hoverBg, Color hoverFg);

    public abstract void addShadowEffect(JLabel label, Color shadowColor, int shadowOffset);
}
