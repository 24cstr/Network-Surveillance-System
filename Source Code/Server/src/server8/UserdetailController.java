package server8;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class UserdetailController implements Initializable {
    @FXML
    private TextField up5;
    @FXML
    private TextField up4;
    @FXML
    private TextField up3;
    @FXML
    private TextField up2;
    @FXML
    private Label up1;
    @FXML
    private Button updb;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        up1.setText(Server.ui);
        up2.setText(Server.un);
        up3.setText(Server.ue);
        up4.setText(Server.um);
        up5.setText(Server.up);
        up5.textProperty().addListener((observable, oldValue, newValue) -> {
        updb.setDisable(up5.getText().trim().isEmpty()||!up5.getText().matches("^[1-4]{1}$")||up2.getText().trim().isEmpty()||up3.getText().trim().isEmpty()||up4.getText().trim().isEmpty()||up4.getText().matches("^[0-9]{10}$"));
});
       up2.textProperty().addListener((observable, oldValue, newValue) -> {
        updb.setDisable(up5.getText().trim().isEmpty()||!up5.getText().matches("^[1-4]{1}$")||up2.getText().trim().isEmpty()||up3.getText().trim().isEmpty()||up4.getText().trim().isEmpty()||up4.getText().matches("^[0-9]{10}$"));
});
        up4.textProperty().addListener((observable, oldValue, newValue) -> {
        updb.setDisable(up5.getText().trim().isEmpty()||!up5.getText().matches("^[1-4]{1}$")||up2.getText().trim().isEmpty()||up3.getText().trim().isEmpty()||up4.getText().trim().isEmpty()||up4.getText().matches("^[0-9]{10}$"));
});
         up3.textProperty().addListener((observable, oldValue, newValue) -> {
        updb.setDisable(up5.getText().trim().isEmpty()||!up5.getText().matches("^[1-4]{1}$")||up2.getText().trim().isEmpty()||up3.getText().trim().isEmpty()||up4.getText().trim().isEmpty()||up4.getText().matches("^[0-9]{10}$"));
});
    }    

    @FXML
    private void update_user(MouseEvent event) {
        String upd1=up1.getText();
        String upd2=up2.getText();
        String upd3=up3.getText();
        String upd4=up4.getText();
        String upd5=up5.getText();
            int u5=Integer.parseInt(upd5);
            databse.user_update(upd1,upd2,upd3,upd4,u5);
            Stage stage=(Stage)up5.getScene().getWindow();
        stage.close();
            
    }

    @FXML
    private void update_cancel(MouseEvent event) {
	Stage stage=(Stage)up5.getScene().getWindow();
        stage.close();
    }
    
}