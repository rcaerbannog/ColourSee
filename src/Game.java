import java.util.Scanner;

/**
 * Test class for Level
 * To be deleted later
 */
public class Game {

    public static void main(String[]Args){
        String level = "1-1";

        Level newLevel = new Level();

        int action = newLevel.runGame(level);
        System.out.println(action);
    }
}
