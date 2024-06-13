package GUI;

import java.util.LinkedList;
import java.util.List;
/**
 * initializes the class to start the game
 * @author Aarit Parekh
 * @version 5/29
 * */
public class MafiaRunner {
    
    public static List<String> players;

    public MafiaRunner() {

        players = new LinkedList<String>();
        players.add("Mr.Fulk");
        players.add("Mr.Beast");
        players.add("Chandler");
        players.add("Mr.Kwong");
        players.add("kaveyan");
        players.add("nathan");
        players.add("aarit");

        new StartScreen().setVisible(true);
        new MafiaRuleSheet();

        // new chatroom(new Player(new MafiaCharacter("Player", "mafia",
        // true)));
    }

}
