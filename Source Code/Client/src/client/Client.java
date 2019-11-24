package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Client extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
           @Override
        public void handle(WindowEvent event) {
            
       if(!user.userID.equalsIgnoreCase("NIL")){try {
          loginregister.logout();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }}
         System.exit(0);
        }
         });
        final Thread FTPs = new Thread(new Runnable() {public void run(){
        communication.FTPserverstart();
        }});
        FTPs.start();
        final Thread sendinf = new Thread(new Runnable() {public void run(){
        loginregister.sendinfo();
        }});
        sendinf.start();
    }
    public static void main(String[] args) throws IOException {
        communication.FTPsoc=new ServerSocket(5215);
        
        launch(args);
    }
    
    public static class user{
    public static String userID="NIL",blweb;
    public static int priv=0;
    public static Double bandusedn,tband;
    public static String sestime="0:0:0",status;
    static void updatebandusedn(){
    bandusagecal.bndused=(double)bandusagecal.bndused/(1024*1024);
    bandusedn+=bandusagecal.bndused;
    bandusagecal.bndused=0;
    }
    
    static void refreshtime(){      
    String [] ti=new String[3];
    ti=sestime.split(":");
    int s=Integer.parseInt(ti[2]);
    int m=Integer.parseInt(ti[1]);
    int h=Integer.parseInt(ti[0]);
    s += 1;
        if (s == 60)
        {
          m += 1;
          s = 00;
        }
        if (m == 60)
        {
          h += 1;
          m = 00;
        } 
    sestime=h+":"+m+":"+s;
        }
    }
    
}