package client;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextField uid;
    @FXML
    private TextField pass;
    @FXML
    private Label errmsg;
    @FXML
    private Button loginbut;
    
    public ValidationSupport validationSupport = new ValidationSupport();
    
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        validationSupport.registerValidator(uid, Validator.createEmptyValidator("Text is required"));
        validationSupport.registerValidator(pass, Validator.createEmptyValidator("Text is required"));
        
        pass.textProperty().addListener((observable, oldValue, newValue) -> {
           loginbut.setDisable(uid.getText().trim().isEmpty()||pass.getText().trim().isEmpty());
           
        });
        uid.textProperty().addListener((observable, oldValue, newValue) -> {
           loginbut.setDisable(uid.getText().trim().isEmpty()||pass.getText().trim().isEmpty());
        });
    }    

    @FXML
    private void login(MouseEvent event) {
        String userID=uid.getText();
        String passwrd=pass.getText();
        boolean log=false;
        
          log= loginregister.login(userID,passwrd);
          if(log)
        {
            try {
                Stage stage=(Stage)uid.getScene().getWindow();
                Parent root1;
                stage.close();
                Client.user.userID=userID;
                root1 = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Scene scene1 = new Scene(root1);
                stage.setScene(scene1);
                stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
        errmsg.setText("Wrong Username or Password");
        }
    }

    @FXML
    private void register(MouseEvent event) {
        Stage stage=(Stage)uid.getScene().getWindow();
        Parent root1;
        try {
            stage.close();
            root1 = FXMLLoader.load(getClass().getResource("register.fxml"));
            Scene scene1 = new Scene(root1);
            
            stage.setScene(scene1);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }

    @FXML
    private void forgtpass(MouseEvent event) {
      Dialog<Pair<String, String>> dialog = new Dialog<>();
      dialog.setTitle("Fogot Password");
      dialog.setHeaderText("\nContact your Admin for a unlock code. \nIf you already have a code enter it below\n");

// Set the icon (must be included in the project).
//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

// Set the button types.
ButtonType loginButtonType = new ButtonType("Retrive", ButtonData.OK_DONE);
dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
GridPane grid = new GridPane();
grid.setHgap(10);
grid.setVgap(10);
grid.setPadding(new Insets(20, 150, 10, 10));

TextField username = new TextField();
username.setPromptText("Username");
PasswordField password = new PasswordField();
password.setPromptText("Unlock Code");

grid.add(new Label("Username:"), 0, 0);
grid.add(username, 1, 0);
grid.add(new Label("Unlock Code:"), 0, 1);
grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
password.textProperty().addListener((observable, oldValue, newValue) -> {
    loginButton.setDisable(newValue.trim().isEmpty()||username.getText().isEmpty());
});

dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
dialog.setResultConverter(dialogButton -> {
    if (dialogButton == loginButtonType) {
        return new Pair<>(username.getText(), password.getText());
    }
    return null;
});

Optional<Pair<String, String>> result = dialog.showAndWait();
final String un;
        
final String ps;
    un=result.get().getKey();
    ps=result.get().getValue();
    System.out.println("Username=" + un + ", Password=" + ps);
 
    //
    if(loginregister.uncode(un, ps)){
      System.out.println("code correct");
      Dialog<Pair<String, String>> dialog1 = new Dialog<>();
      dialog1.setTitle("Fogot Password");
      dialog1.setHeaderText("\nContact your Admin for a unlock code. \nIf you already have a code enter it below\n");

// Set the icon (must be included in the project).
//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

// Set the button types.
ButtonType loginButtonType1 = new ButtonType("Update", ButtonData.OK_DONE);
dialog1.getDialogPane().getButtonTypes().addAll(loginButtonType1, ButtonType.CANCEL);

// Create the username and password labels and fields.
grid = new GridPane();
grid.setHgap(10);
grid.setVgap(10);
grid.setPadding(new Insets(20, 150, 10, 10));

PasswordField password1 = new PasswordField();
password.setPromptText("Enter a Password");
PasswordField password2 = new PasswordField();
password.setPromptText("Re-enter the Password");

grid.add(new Label("Password :"), 0, 0);
grid.add(password1, 1, 0);
grid.add(new Label("Confirm Password:"), 0, 1);
grid.add(password2, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
Node loginButton1 = dialog1.getDialogPane().lookupButton(loginButtonType1);
loginButton1.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).

        password2.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton1.setDisable(password1.getText().isEmpty());
            loginButton1.setDisable(newValue.trim().isEmpty() || !newValue.equals(password1.getText()));

        });

dialog1.getDialogPane().setContent(grid);

// Request focus on the username field by default.
Platform.runLater(() -> password1.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
dialog1.setResultConverter(dialogButton -> {
    if (dialogButton == loginButtonType1) {
        return new Pair<>(password1.getText(), password2.getText());
    }
    return null;
});

Optional<Pair<String, String>> result2 = dialog1.showAndWait();
final String ps1;
        
final String ps2;
    ps1=result2.get().getKey();
    ps2=result2.get().getValue();
 
 if(loginregister.sndpass(un,ps2)){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setContentText("Your password has been reset");
         alert.showAndWait();
    }
 else{
       Alert alert = new Alert(Alert.AlertType.WARNING);
         alert.setContentText("There was some error while reseting your password....\nPlease try again");
         alert.showAndWait();
 }
    }
    else{
        
         Alert alert = new Alert(Alert.AlertType.WARNING);
         alert.setContentText("You entered a wrong unlock code");
         alert.showAndWait();
        }
    }
    
    
}
