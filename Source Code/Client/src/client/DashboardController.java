package client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

public class DashboardController implements Initializable {

    @FXML
    private Button lout;
    @FXML
    private RadioButton ftbutton;
    @FXML
    private ToggleGroup fileorchat;
    @FXML
    private RadioButton chatbutton;
    @FXML
    private Pane chatpane;
    private final GridPane ftpane = new GridPane();
    private final Button chsfile = new Button("Choose File");
    private final Button sfile = new Button("Send File");
    private final Label ftl = new Label("FILE : ");
    private final TextField filename = new TextField();
    @FXML
    private GridPane ftchtpane;
    @FXML
    private ProgressIndicator bandused;
    @FXML
    private ChoiceBox<String> touser;
    @FXML
    private ProgressBar p1;
    @FXML
    private Label unamedisp;
    @FXML
    private ProgressBar p2;
    @FXML
    private ProgressBar p3;
    @FXML
    private ProgressBar p4;
    @FXML
    private Label bandusedno;

    @FXML
    private TextArea senmsg;
    @FXML
    private TextArea rcmsg;
    @FXML
    private TextArea blcweb;
    @FXML
    private Label sestime;
    @FXML
    private Label accstatus;
    @FXML
    private MenuItem unblcmenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ftpane.setHgap(10);
        ftpane.setVgap(30);
        ftpane.add(ftl, 1, 1);
        ftpane.add(filename, 2, 1);
        ftpane.add(chsfile, 2, 2);
        ftpane.add(sfile, 3, 2);
        //rcmsg.setText("sec");

        unamedisp.setText(Client.user.userID);
        if (Client.user.priv >= 1) {
            p1.setProgress(1);

        }
        if (Client.user.priv >= 2) {
            p2.setProgress(1);

        }
        if (Client.user.priv >= 3) {
            p3.setProgress(1);

        }
        if (Client.user.priv == 4) {
            p4.setProgress(1);

        }
        bandusedno.setText(Client.user.bandusedn + " / " + Client.user.tband);

        setmsg();
        bandindi();
        chsfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(null);
                filename.setText(file.getPath());
            }
        });
        ftchtpane.add(ftpane, 0, 0);
        ftpane.setVisible(false);
        sfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String fname = filename.getText();
                final Thread FT = new Thread(new Runnable() {
                    public void run() {
                        String to = touser.getValue();
                        if (to.equalsIgnoreCase("Admin")) {
                            showinfo("can't send message to the ADMIN");
                            return;
                        }
                        for (int i = 0; i < loginregister.userlist.size(); i++) {
                            if (to.equalsIgnoreCase(loginregister.userlist.get(i))) {
                                to = loginregister.iplist.get(i);
                                break;
                            }
                        }
                        communication.sendFile(fname, to);
                    }
                });
                FT.start();

            }
        });
        final Thread bandup = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        bandindi();
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        bandup.start();
        final Thread timer = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    updsest();
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        timer.start();
        blcweb.setText(Client.user.blweb);
        accstatus.setText(Client.user.status);
    }

    @FXML
    private void logout(MouseEvent event) {

        boolean log = false;
        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setContentText("Do you really want to logout now??");
        ButtonType yes = new ButtonType("YES");
        ButtonType no = new ButtonType("NO");
        al.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = al.showAndWait();
        if (result.get() == yes) {
            try {

                log = loginregister.logout();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (log) {
                try {
                    communication.msgsoc.close();
                    Stage stage = (Stage) lout.getScene().getWindow();
                    Parent root1;
                    stage.close();
                    root1 = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                    Scene scene1 = new Scene(root1);
                    stage.setScene(scene1);
                    stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
        }
    }

    @FXML
    private void fcpanechange(ActionEvent event) {
        if (ftbutton.isSelected()) {
            chatpane.setVisible(false);
            ftpane.setVisible(true);
        } else if (chatbutton.isSelected()) {
            chatpane.setVisible(true);
            ftpane.setVisible(false);
        }

    }

    @FXML
    private void userlistget(MouseEvent event) throws IOException {
        //fetch user list from server
        loginregister.ftuser();
        touser.setItems(loginregister.userlist);
    }

    public void bandindi() {
        Client.user.updatebandusedn();
        Platform.runLater(() -> {
            double d = Client.user.bandusedn / Client.user.tband;
            bandused.setProgress(d);
            bandusedno.setText(String.format(Math.round(Client.user.bandusedn) + " / " + Math.round(Client.user.tband)));
        });
    }

    public void updsest() {
        Client.user.refreshtime();
        Platform.runLater(() -> {
            sestime.setText(Client.user.sestime);
        });
    }

    @FXML
    private void sendmesg(MouseEvent event) throws Exception {
        String[] msg = new String[2];
        String to = touser.getValue();
        if(to==null){
            showwarning("Select a user to send the message");
            return;
        }
        for (int i = 0; i < loginregister.userlist.size(); i++) {
            if (to.equalsIgnoreCase(loginregister.userlist.get(i))) {
                to = loginregister.iplist.get(i);
                break;
            }
        }
        msg[0] = to;
        msg[1] = Client.user.userID + " : " + senmsg.getText();
        communication.sendmsg(msg);
        senmsg.clear();
    }

    public void setmsg() {
        final Thread rmsg = new Thread(new Runnable() {
            public void run() {
                String rmessg = new String();
                try {
                    while (true) {
                        rmessg += communication.recievemsg();
                        rcmsg.setText(rmessg);
                    }
                } catch (Exception ex) {
                    // Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        rmsg.start();

    }

    public static void showinfo(String a) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(a);
            alert.showAndWait();
        });

    }

    public static void showwarning(String a) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(a);
            alert.showAndWait();
        });

    }

    @FXML
    private void reqprvchange(MouseEvent event) {

        String reas = "";
        int pri = 0;

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Change Privillage Level");
        dialog.setHeaderText("Request For Changing Your Privillage Level");
        ButtonType loginButtonType = new ButtonType("Request", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField privl = new TextField();
        privl.setPromptText("Enter The New Privillage Level You Want.");
        TextArea rsn = new TextArea();
        rsn.setPromptText("Enter The Reason Why You Want To Change Your Privillage Level.");

        grid.add(new Label("New Privillage Level :"), 0, 0);
        grid.add(privl, 1, 0);
        grid.add(new Label("Reason :"), 0, 1);
        grid.add(rsn, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        privl.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty() || !newValue.matches("^[1-4]{1}$") || rsn.getText().trim().isEmpty());
        });
        rsn.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(privl.getText().trim().isEmpty() || !privl.getText().matches("^[1-4]{1}$") || rsn.getText().trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> privl.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(privl.getText(), rsn.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        boolean st = false;
        if (result.isPresent()) {
            pri = Integer.parseInt(result.get().getKey());
            reas = result.get().getValue();
            st = loginregister.req(pri, reas);
        }

        if (st) {
            if (Client.user.priv >= 1) {
                p1.setProgress(1);
            } else {
                p1.setProgress(0);
            }
            if (Client.user.priv >= 2) {
                p2.setProgress(1);
            } else {
                p2.setProgress(0);
            }
            if (Client.user.priv >= 3) {
                p3.setProgress(1);
            } else {
                p3.setProgress(0);
            }
            if (Client.user.priv == 4) {
                p4.setProgress(1);
            } else {
                p4.setProgress(0);
            }
        }
    }

    @FXML
    private void requnblocacc(ActionEvent event) {

        boolean rq = loginregister.req();
        if (rq) {
            accstatus.setText("UNBLOCKED");
        }
    }

    @FXML
    private void chechblocked(WindowEvent event) {
        if (accstatus.getText().equalsIgnoreCase("UNBLOCKED")) {
            unblcmenu.setVisible(false);
            System.out.println("unblocked");
        } else {
            unblcmenu.setVisible(true);
            System.out.println("blocked");
        }
    }
}
