package server8;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    @FXML
    private PasswordField adminps;
    static String intfc=new String();
    Stage stage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
    }    

    @FXML
    private void adminlogin(MouseEvent event) {
        
        if(databse.chcklogindata("Admin", adminps.getText())){
            try {
            stage=(Stage)adminps.getScene().getWindow();
            Parent root1;
            stage.close();
            getintf();
            root1 = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene1 = new Scene(root1);
            stage.setScene(scene1);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
            stage.show();
            if(adminps.getText().equalsIgnoreCase("Password")){
            FXMLDocumentController.showwarning("You are still using the default password.....\nPlease change it as soon as possible for better security....\nFor Changing your Password : Go to Advance Option -> Admin pasword.");
            }
            Server.addtolog("Server Side Program Started");  
         }else{
           FXMLDocumentController.showwarning("Sorry you entered a wrong password....Try Again");
           adminps.clear();
        }
                           
    }  
    
    void getintf(){
    List<String> choices = new ArrayList<>();
    choices.add("eth0");
    choices.add("wlan0");

    ChoiceDialog<String> dialog = new ChoiceDialog<>("eth0", choices);
    dialog.setTitle("Choice Dialog");
    dialog.setHeaderText("Network Interface Selection");
    dialog.setContentText("Choose the interface though which your pi is connected to the network :");

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()){
    System.out.println("Your choice: " + result.get());
    intfc = result.get();
    }

    }
}
