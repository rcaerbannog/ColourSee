import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SplashScreen implements ActionListener {

    private JFrame window = new JFrame ("ColourSee");
    private JPanel panel = new JPanel();

    private JPanel howToPane;
    private JPanel menuPane;
    private ArrayList<World> levelSelectWorlds;
    private int selectedWorld = 0;

    private String playerCBType;


    public SplashScreen() throws IOException, URISyntaxException {
        howToPane = new JPanelWithBackgroundImage("mainMenuBkg.png");
        menuPane = new JPanelWithBackgroundImage("mainMenuBkg.png");

        //Construct menu pane
        JButton startButton = new JButton("Start");
        startButton.setActionCommand("Start");
        JButton howToPlayButton = new JButton ("How to Play");
        howToPlayButton.setActionCommand("How to play");
        JButton quitButton = new JButton ("Quit");
        quitButton.setActionCommand("QUIT");

        menuPane.setBorder(new EmptyBorder(10,10,10,10));
        menuPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        menuPane.add(startButton, gbc);
        menuPane.add(howToPlayButton, gbc);
        menuPane.add(quitButton, gbc);

        startButton.addActionListener(this);
        howToPlayButton.addActionListener(this);
        quitButton.addActionListener(this);

        gbc.weighty = 1;

        changePanel(menuPane);
        window.add(panel);

        generateLevelSelect();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(650, 400);
        window.setVisible(true);
    }

    public void generateLevelSelect(){
        levelSelectWorlds = new ArrayList<World>();
        try{
            BufferedReader in = new BufferedReader(new FileReader(
                    FileChooser.getLevelsFolder() + "LevelsList.txt"));

            String inputLine = in.readLine();
            boolean isFirstWorld = true;
            boolean prevLevelCompleted = true;
            while (!inputLine.equals("<EOF>")){
                World levelSelectPanel = new World();
                levelSelectWorlds.add(levelSelectPanel);

                levelSelectPanel.setLayout(new BoxLayout(levelSelectPanel, BoxLayout.Y_AXIS));

                Box topPanel = Box.createHorizontalBox();
                JLabel worldTitleLabel = new JLabel(inputLine);
                worldTitleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                worldTitleLabel.setFont(new Font("Arial", Font.BOLD|Font.ITALIC, 20));
                JButton prevButton = new JButton("PREVIOUS WORLD");
                prevButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
                JButton nextButton = new JButton("NEXT WORLD");
                nextButton.setAlignmentX(JButton.RIGHT_ALIGNMENT);

                JPanel midPanel = new JPanel();
                midPanel.setLayout(new GridLayout(3, 5, 5, 5));
                Dimension levelButtonD = new Dimension(50, 50);
                inputLine = in.readLine();
                while (!inputLine.equals(null) && !inputLine.equals("")){
                    String[] tokens = inputLine.split(" #SCORE ");
                    //tokens[0] is the level directory
                    //tokens[1] is the score
                    String name = tokens[0];
                    String status = tokens[1];
                    JButton levelButton = new JButton(tokens[0]);   //This will be the level directory and score
                    /* Meant to change status of levels so they are unlocked in sequential order
                    if (tokens[1].equals("locked") && !prevLevelCompleted){
                        levelSelectPanel.addLevel(name, "locked");
                        levelButton.setEnabled(false);
                        prevLevelCompleted = false;
                    }
                    else if (tokens[1].equals("completed")){
                        levelSelectPanel.addLevel(name, "completed");
                        prevLevelCompleted = true;
                    }
                    else{   //either previous level is completed, or this level is unlocked
                        levelSelectPanel.addLevel(name, "unlocked");
                        prevLevelCompleted = false;
                    }
                    */
                    levelButton.setPreferredSize(levelButtonD);
                    levelButton.setMinimumSize(levelButtonD);
                    levelButton.setMaximumSize(levelButtonD);
                    levelButton.setActionCommand("PLAY LEVEL");
                    levelButton.addActionListener(this);
                    midPanel.add(levelButton);
                    inputLine = in.readLine();
                }

                JPanel bottomPanel = new JPanel();
                bottomPanel.setLayout(new FlowLayout());
                JButton mainMenuButton = new JButton("Back to Main Menu");
                mainMenuButton.setActionCommand("MAIN MENU");
                mainMenuButton.addActionListener(this);

                inputLine = in.readLine();  //This should either return the next world name or "<EOF>"
                if (inputLine.equals("<EOF>")) topPanel.remove(nextButton);
                if (isFirstWorld){
                    topPanel.remove(prevButton);
                    isFirstWorld = false;
                }

                //Add panels to level select
                levelSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                levelSelectPanel.add(topPanel);
                levelSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                levelSelectPanel.add(midPanel);
                levelSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                levelSelectPanel.add(bottomPanel);
                levelSelectPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
            in.close();

        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        Object source = actionEvent.getSource();

        if (action.equals("QUIT"))
            window.dispose();
        else if (action.equals("Start")){
            //Implement reading options.txt later, if time allows
            playerCBType = "normal";
            selectedWorld = 0;
            changePanel(levelSelectWorlds.get(selectedWorld));
        }
        else if (action.equals("How to Play"))
            changePanel(howToPane);
        else if (action.equals("MAIN MENU"))
            changePanel(menuPane);
        else if (action.equals("PREVIOUS WORLD")){
            selectedWorld--;
            changePanel(levelSelectWorlds.get(selectedWorld));
        }
        else if (action.equals("NEXT WORLD")){
            selectedWorld++;
            changePanel(levelSelectWorlds.get(selectedWorld));
        }
        else if (source instanceof JButton && action.equals("PLAY LEVEL")){
            JButton sourceButton = (JButton)source;
            Level level = new Level();
            int nextAction;
            do {
                nextAction = level.runGame(sourceButton.getText(), Simulation.normal);
                if (level.getResult() == 1){
                    //levelSelectWorlds.get(selectedWorld).setStatus(sourceButton.getText(), "completed");
                }
            }
            while (nextAction == Level.RESTART);

            if (nextAction == -1){
                JOptionPane.showMessageDialog(window, "There was an error while playing the level.");
                changePanel(menuPane);
            }
            else if (nextAction == 0){
                JOptionPane.showMessageDialog(window, "The level window was closed by the player.");
                changePanel(menuPane);
            }
            else if (nextAction == Level.LEVELS)
                changePanel(levelSelectWorlds.get(selectedWorld));
            else if (nextAction == Level.MAIN_MENU)
                changePanel(menuPane);
            else{
                changePanel(menuPane);
            }
        }
    }

    public void changePanel(JPanel newPanel){
        panel = newPanel;
        window.revalidate();
        window.repaint();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        new SplashScreen();
    }
}
