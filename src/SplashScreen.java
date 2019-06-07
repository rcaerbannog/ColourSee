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

public class SplashScreen implements ActionListener {

    JFrame window = new JFrame ("ColourSee");
    JPanel howToPane = new JPanelWithBackgroundImage("graphic/background.png");
    JPanel menuPane = new JPanelWithBackgroundImage("graphics/background.png");

    public SplashScreen() throws IOException, URISyntaxException {
        JButton startButton = new JButton("Start");
        JButton howToPlayButton = new JButton ("How to Play");
        JButton quitButton = new JButton ("Quit");
        JButton toMenuButton = new JButton("Back to Menu");

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

        window.add(menuPane);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(650, 400);
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Quit"))
            window.dispose();

        //TODO look at CardLayout exercise and adapt menu accordingly
        else if (actionEvent.getActionCommand().equals("How to Play"))
            window.add(howToPane);
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        new SplashScreen();
    }
}
