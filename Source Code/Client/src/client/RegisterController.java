/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Acer
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField fname;
    @FXML
    private TextField uname;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField repass;
    @FXML
    private TextField email;
    @FXML
    private TextField mobno;
    @FXML
    private Label errmsg;
    @FXML
    private Button regb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        regb.setDisable(true);
        mobno.textProperty().addListener((observable, oldValue, newValue) -> {
           regb.setDisable(!mobno.getText().matches("^[0-9]{10}$")||fname.getText().trim().isEmpty()||uname.getText().trim().isEmpty()||pass.getText().trim().isEmpty()||email.getText().trim().isEmpty()||!repass.getText().equals(pass.getText()));
           
        });
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            regb.setDisable(!mobno.getText().matches("^[0-9]{10}$")||fname.getText().trim().isEmpty()||uname.getText().trim().isEmpty()||pass.getText().trim().isEmpty()||email.getText().trim().isEmpty()||!repass.getText().equals(pass.getText()));

        });
        pass.textProperty().addListener((observable, oldValue, newValue) -> {
            regb.setDisable(!mobno.getText().matches("^[0-9]{10}$")||fname.getText().trim().isEmpty()||uname.getText().trim().isEmpty()||pass.getText().trim().isEmpty()||email.getText().trim().isEmpty()||!repass.getText().equals(pass.getText()));

        });
        repass.textProperty().addListener((observable, oldValue, newValue) -> {
            regb.setDisable(!mobno.getText().matches("^[0-9]{10}$")||fname.getText().trim().isEmpty()||uname.getText().trim().isEmpty()||pass.getText().trim().isEmpty()||email.getText().trim().isEmpty()||!repass.getText().equals(pass.getText()));

        });
        fname.textProperty().addListener((observable, oldValue, newValue) -> {
            regb.setDisable(!mobno.getText().matches("^[0-9]{10}$")||fname.getText().trim().isEmpty()||uname.getText().trim().isEmpty()||pass.getText().trim().isEmpty()||email.getText().trim().isEmpty()||!repass.getText().equals(pass.getText()));

        });
        uname.textProperty().addListener((observable, oldValue, newValue) -> {
            regb.setDisable(!mobno.getText().matches("^[0-9]{10}$")||fname.getText().trim().isEmpty()||uname.getText().trim().isEmpty()||pass.getText().trim().isEmpty()||email.getText().trim().isEmpty()||!repass.getText().equals(pass.getText()));
        });
    }

    @FXML
    private void registeruser(MouseEvent event) {
        errmsg.setText(" ");
        String fn=fname.getText();
        String uid=uname.getText();
        String pss=pass.getText();
        String repss=repass.getText();
        String eid=email.getText();
        String mno=mobno.getText();
        if(!pss.equals(repss))
        {
            errmsg.setText("The Passwords Entered doesn't match");
        }
        else{
              
              boolean reg=false;
            try {
                reg = loginregister.register(fn,uid,pss,eid,mno);
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
              if(reg)
              {
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Information Dialog");
                 alert.setHeaderText("Registration Status");
                 alert.setContentText("Your data has been sent to the Admin. \nYou can login once the Admin accepts your request");
                 alert.showAndWait();
              }
        }
        
    }

    @FXML
    private void backtologin(MouseEvent event) {

        Stage stage=(Stage)mobno.getScene().getWindow();
        Parent root1;
        try {
            stage.close();
            root1 = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene1 = new Scene(root1);
            stage.setScene(scene1);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
