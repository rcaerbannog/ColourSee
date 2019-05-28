import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class Controller {

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
        System.out.println("START");
    }
}
