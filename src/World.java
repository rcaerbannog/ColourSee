import javax.swing.*;
import java.util.ArrayList;

public class World extends JPanel {
    //public ArrayList<String> levels;
    //public ArrayList<String> levelStatuses;

    public World(){
        super();
        //levels = new ArrayList<String>();
        //levelStatuses = new ArrayList<String>();
    }

    /*
    public ArrayList<String> getLevels(){
        return levels;
    }

    public ArrayList<String> getLevelStatuses(){
        return levels;
    }

    public void addLevel(String levelName, String levelStatus){
        levels.add(levelName);
        levelStatuses.add(levelStatus);
    }

    public int getNumLevels(){
        return levels.size();
    }

    public int setStatus(String name, String status){
        int i;
        for (i = 0; i < levels.size(); i++){
            if (levels.get(i).equals(name)){
                levelStatuses.set(i, status);
                break;
            }
        }
        return i;
    }

    public void setStatus(int index, String status){
        levelStatuses.set(index, status);
    }
    */
}
