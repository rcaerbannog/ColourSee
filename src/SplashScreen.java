import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class SplashScreen extends Application {

    private static Stage primaryStage;
    private static Scene mainMenu;
    private static Scene level;
    private static String variable = "TEST";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        createScenes();
        variable = "1";
        mainMenu();
        variable = "2";
    }

    public void createScenes() throws Exception{
        //Main menu
        Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("main menu.fxml"));
        mainMenu = new Scene(mainMenuRoot, 1000, 750);

        //Level
        Parent levelRoot = FXMLLoader.load(getClass().getResource("level.fxml"));
        level = new Scene(levelRoot, 1000, 750);

    }

    public void createLevel() throws Exception{
        primaryStage.setTitle("Level");
        primaryStage.setScene(level);
        primaryStage.show();
    }

    public void mainMenu() throws Exception{
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }

    //Controller stuff
    public void initialize(){

    }

    @FXML
    protected void handleQuitButtonAction(ActionEvent event){
        System.out.println("Thanks for playing!");
        System.exit(1);
    }

    @FXML
    protected void handleOptionsButtonAction(ActionEvent event){
        System.out.println("OPTIONS");
    }

    @FXML
    protected void handleStartButtonAction(){
        try{createLevel();}catch(Exception e){}
    }
}
