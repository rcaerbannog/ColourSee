import java.util.Scanner;

public class Game {
    Scanner sc = new Scanner(System.in);
    String level = "FillTest2";

    Level newLevel = new Level();

    int action = newLevel.runGame(level);
}
