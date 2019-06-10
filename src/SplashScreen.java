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
import java.io.IOException;
import java.net.URISyntaxException;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;
/*
* Creates Main menu. Main menu contains a Start button, How to Play Button, and Quit Button
*
* @author Ivan Karlov
*
* Some code was not created by me, but taken from StackOverflow, which are listed:
* https://stackoverflow.com/questions/12468654/switching-between-jpanels - answer by NamelyProgrammer
*
* */

public class SplashScreen implements ActionListener {

   private JFrame window = new JFrame ("ColourSee");
    private JPanel howToPane = new JPanelWithBackgroundImage("graphic/background.png");
    JPanel menuPane = new JPanelWithBackgroundImage("graphics/background.png");
    private JPanel levelPane = new JPanelWithBackgroundImage("graphics/background.png");
    JPanel contentPane = new JPanel();

    JButton level1Button = new JButton ("1-1");
    JButton level2Button = new JButton ("1-2");
    JButton level3Button = new JButton ("1-3");
    JButton level4Button = new JButton ("2-1");
    JButton level5Button = new JButton ("2-2");
    JButton level6Button = new JButton ("2-3");
    JButton level7Button = new JButton ("3-1");
    JButton level8Button = new JButton ("3-2");
    JButton level9Button = new JButton ("3-3");


    public SplashScreen() throws IOException, URISyntaxException {
        JButton startButton = new JButton("Start");
        JButton howToPlayButton = new JButton ("How to Play");
        JButton quitButton = new JButton ("Quit");
        JButton toMenuButton = new JButton("Back to Menu");

        contentPane.setLayout(new CardLayout());
        contentPane.add(menuPane);
        contentPane.add(howToPane);
        contentPane.add(levelPane);

        menuPane.setBorder(new EmptyBorder(10,10,10,10));
        menuPane.setLayout(new GridBagLayout());
        levelPane.setBorder(new EmptyBorder(10,10,10,10));
        levelPane.setLayout(new GridBagLayout());

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

        levelPane.add(level1Button, gbc);
        levelPane.add(level2Button, gbc);
        levelPane.add(level3Button, gbc);
        levelPane.add(level4Button, gbc);
        levelPane.add(level5Button, gbc);
        levelPane.add(level6Button, gbc);
        levelPane.add(level7Button, gbc);
        levelPane.add(level8Button, gbc);
        levelPane.add(level9Button, gbc);
        levelPane.add(toMenuButton, gbc);

        level1Button.addActionListener(this);
        level2Button.addActionListener(this);
        level3Button.addActionListener(this);
        level4Button.addActionListener(this);
        level5Button.addActionListener(this);
        level6Button.addActionListener(this);
        level7Button.addActionListener(this);
        level8Button.addActionListener(this);
        level9Button.addActionListener(this);

        gbc.weighty = 1;

        howToPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.anchor = GridBagConstraints.NORTHWEST;
        howToPane.add(toMenuButton, gbc);

        toMenuButton.addActionListener(this);

        window.add(contentPane);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(650, 400);
        window.setLocationByPlatform(true);
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        CardLayout cardLayout = (CardLayout) contentPane.getLayout();

        if (actionEvent.getActionCommand().equals("Quit"))
            window.dispose();

        else if (actionEvent.getActionCommand().equals("How to Play")) {
            cardLayout.next(contentPane);
        }

        else if (actionEvent.getActionCommand().equals("Start")) {
            cardLayout.last(contentPane);
        }

        else if (actionEvent.getActionCommand().equals("Back to Menu")) {
            cardLayout.first(contentPane);
        }

        else if (actionEvent.getActionCommand().equals("1-1") || actionEvent.getActionCommand().equals("1-2") || actionEvent.getActionCommand().equals("1-3")
        || actionEvent.getActionCommand().equals("2-1") || actionEvent.getActionCommand().equals("2-2") || actionEvent.getActionCommand().equals("2-3")
                || actionEvent.getActionCommand().equals("3-1") || actionEvent.getActionCommand().equals("3-2") || actionEvent.getActionCommand().equals("3-3")) {
            {
                Level level = new Level();
                level.runGame(actionEvent.getActionCommand());
                cardLayout.first(contentPane);
            }
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        new SplashScreen();
    }
}
