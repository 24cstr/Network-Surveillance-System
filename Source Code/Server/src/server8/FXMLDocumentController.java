package server8;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.Glyph;
import server8.Pcaper.packt;
import static server8.Server.actuser;
import server8.Server.busage;
import server8.Server.client;
import server8.Server.servoblist;
import static server8.bandwidth.createbwm;
import static server8.bandwidth.moveNeedle;
import server8.databse.actu;

public class FXMLDocumentController implements Initializable {
    public static Thread read;
    boolean pause=false,stopped=false;
    ObservableList <String> webtypeslist = FXCollections.observableArrayList("Blog","Community","Crowdfunding","Explicit","Gaming Website","Government","Media-sharing ","News site","Photo sharing","Religious site","Review site","Search engine","Shopping","Social Media","Torrent","Webmail","Others");      
    @FXML
    private Tab home;
    @FXML
    private GridPane bwmpane;
    @FXML
    private Tab packetmonitor;
    @FXML
    private Button pstart;
    @FXML
    private Button pstop;
    @FXML
    private TableView<packt> ptable;
    @FXML
    private TableColumn<packt, Long> pno;
    @FXML
    private TableColumn<packt, Date> time;
    @FXML
    private TableColumn<packt, Integer> plen;
    @FXML
    private TableColumn<packt, String> proto;
    @FXML
    private TableColumn<packt, String> smac; 
    @FXML
    private TableColumn<packt, String> sip;
    
    @FXML
    private TableColumn<packt, Integer> sp;
    
    @FXML
    private TableColumn<packt, String> dmac;
   
    @FXML
    private TableColumn<packt, String> dip;
   
    @FXML
    private TableColumn<packt, Integer> dp;
    @FXML
    private Button showolddata;
    @FXML
    private Button b1;
    
    @FXML
    private Tab filetransfer;
    @FXML
    private ChoiceBox<String> webtypes;
    
    @FXML
    private TextField priv;
    @FXML
    private Label un;
    @FXML
    private Label eid;
    @FXML
    private Label mno;
    @FXML
    private Label fna;
    
                                               
    
    @FXML
    private TableView<actu> activeusers;
    @FXML
    private TableColumn<actu, String> acuid;
    @FXML
    private TableColumn<actu, String> acip;       
    @FXML
    private AnchorPane notificationpane;
    @FXML
    private ToggleButton enadisabutton;
    @FXML
    private AnchorPane glyph;
    @FXML
    private ChoiceBox<String> webtypes111;//for delete website block

    private Button refreshbu1;
    private Button refreshbucommu;
    @FXML
    private AnchorPane statistics;
    @FXML
    private Button refreshws;
    @FXML
    private ListView<String> weblv;
    @FXML
    private ComboBox<String> tpall;
   
    @FXML
    private ComboBox<String> umsg;
    @FXML
    private ChoiceBox<String> bwuser;
    @FXML
    private ChoiceBox<String> ubwuser;
    @FXML
    private ComboBox<String> bubuser;
    @FXML
    private ChoiceBox<String> unccdgen;
    @FXML
    private TableView<Server.etab> ememtab;
    @FXML
    private TableColumn<Server.etab, String> ename;
    @FXML
    private TableColumn<Server.etab, String> emailid;
    @FXML
    private TableColumn<Server.etab, String> ealert;
    @FXML
    private TableView<Server.busage> butable;
    @FXML
    private TableColumn<Server.busage, String> bun;
    @FXML
    private TableColumn<Server.busage, Double> bus;//from
    @FXML
    private TableColumn<Server.busage, Double> bmax;
    @FXML
    private TableColumn<Server.busage, Time> curses;
    @FXML
    private TableColumn<Server.busage, Time> tses;
    @FXML
    private TableView<Server.op> emailoptab;
    @FXML
    private TableColumn<Server.op, String> op;
    @FXML
    private CheckBox bu;
    @FXML
    private CheckBox st;
    @FXML
    private CheckBox maw;
    @FXML
    private CheckBox at;
    
    @FXML
    private TableView<client> usedatat;
    @FXML
    private TableColumn<client, String> tuseid;
    @FXML
    private TableColumn<client, String> tname;
    @FXML
    private TableColumn<client, String> tmail;
    @FXML
    private TableColumn<client, String> tmob;
    @FXML
    private TableColumn<client, Integer> tpriv;
    @FXML
    private TableColumn<client, String> tcur;
    @FXML
    private TableColumn<client, Button> tebut;
    @FXML
    private TableColumn<client, Button> tdbut;
    
    
    @FXML
    private TextArea webname;
    @FXML
    private CheckBox ser1;
    @FXML
    private CheckBox ser2;
    @FXML
    private CheckBox ser3;
    @FXML
    private CheckBox ser5;
    @FXML
    private CheckBox ser4;
    @FXML
    private CheckBox ser7;
    @FXML
    private CheckBox ser6;
    @FXML
    private CheckBox ser8;
    @FXML
    private CheckBox ser10;
    @FXML
    private CheckBox ser9;
    @FXML
    private TableView<Server.servoblist> ser_table;
    @FXML
    private TableColumn<Server.servoblist, String> ser_table_1;
    @FXML
    private TableColumn<Server.servoblist, String> ser_table_2;
    Thread block;
    @FXML
    private TextArea umg;
    @FXML
    private TextArea bmg;
    @FXML
    private TextArea mmg;
    @FXML
    private TextArea amg;
    @FXML
    private AnchorPane communication;
    private Button mmbut;
    private Button umbut;
    private Button bmbut;
    final CheckComboBox<String> multicastbox= new CheckComboBox<>(Server.actuser);
    @FXML
    private AnchorPane plevel;
    private Button plevel_change1;
    private Button plevel_change2;
    private Button plevel_change3;
    private Button plevel_change4;
    @FXML
    private TextField pl1;
    @FXML
    private TextField pl4;
    @FXML
    private TextField pl2;
    @FXML
    private TextField pl3;
    @FXML
    private TableView<Server.privoblist> pl1_table;
    @FXML
    private TableColumn<Server.privoblist,String > pl1_web;
    @FXML
    private Label p1mb;
    @FXML
    private Label p4mb;
    @FXML
    private Label p3mb;
    @FXML
    private Label p2mb;
    @FXML
    private TableView<Server.privoblist> pl3_table;
    @FXML
    private TableColumn<Server.privoblist, String> pl3_web;
    @FXML
    private TableView<Server.privoblist> pl2_table;
    @FXML
    private TableColumn<Server.privoblist, String> pl2_web;
    @FXML
    private TableView<Server.privoblist> pl4_table;
    @FXML
    private TableColumn<Server.privoblist, String> pl4_web;
    @FXML
    private ComboBox<String> ememn;
    @FXML
    private TextField nm;
    @FXML
    private TextField emid;
    @FXML
    private RadioButton rbblcu;
    @FXML
    private ToggleGroup blunuser;
    @FXML
    private TableView<Server.accesstab> acctab;
    @FXML
    private TableColumn<Server.accesstab, String> acctabu;
    @FXML
    private TableColumn<Server.accesstab, String> acctabl;
    @FXML
    private TableColumn<Server.accesstab, String> acctabb;
    @FXML
    private TextField uncd;
    @FXML
    private TextField crule;
    @FXML
    private TextArea cdesc;
    @FXML
    private Label coutput;
    @FXML
    private PasswordField nps1;
    @FXML
    private PasswordField nps2;
    @FXML
    private PasswordField currp;
    @FXML
    private Button adpcb;
    @FXML
    private Button ademb;
    @FXML
    private TableView<Server.Logs> Logstab;
    @FXML
    private TableColumn<Server.Logs, String> logc;
    @FXML
    private Label cpn;
    @FXML
    private Label cps;
    @FXML
    private TextField custtyp;
    
   
    
     @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        adpcb.setDisable(true);
        ademb.setDisable(true);
        nps2.textProperty().addListener((observable, oldValue, newValue) -> {
        adpcb.setDisable(newValue.trim().isEmpty()||!newValue.equals(nps1.getText())||currp.getText().isEmpty());
    });
         nps1.textProperty().addListener((observable, oldValue, newValue) -> {
         adpcb.setDisable(newValue.trim().isEmpty()||!newValue.equals(nps2.getText())||currp.getText().isEmpty());
    });
        emid.textProperty().addListener((observable, oldValue, newValue) -> {
        ademb.setDisable(newValue.trim().isEmpty()||nm.getText().isEmpty());
    });
        umsg.setItems(Server.actuser);
        bwuser.setItems(Server.accus);
        ubwuser.setItems(Server.accus);
        unccdgen.setItems(Server.alluser);
        ememn.setItems(Server.emm);
        //logarea.setText(logs);
        webtypes.setItems(webtypeslist);
        tpall.getItems().addAll("Top 5 Websites","All Websites");
        tpall.setValue("Top 5 Websites");
       
        //refreshbu1=new Button("Refresh",new Glyph("FontAwesome","REFRESH")) ;
       // refreshbu1.setLayoutX(1075);
        //refreshbu1.setLayoutY(122);
        //statistics.getChildren().add(refreshbu1);
        //refreshbu1.setOnAction((ActionEvent event) -> {refrbanu();});
        //showolddata.setGraphic(new ImageView(imageDecline));
        //add glyph in button                   
        refreshbu1=new Button("Refresh",new Glyph("FontAwesome","REFRESH")) ;
        refreshbu1.setLayoutX(1075);
        refreshbu1.setLayoutY(122);
        statistics.getChildren().add(refreshbu1);
        refreshbu1.setOnAction((ActionEvent event) -> {refrbanu();});
        
        ////-----LEVEL 1 PRIVILLAGE LEVEL
        plevel_change1=new Button("Change",new Glyph("FontAwesome","EDIT")) ;
        plevel_change1.setLayoutX(320);
        plevel_change1.setLayoutY(170);
        plevel.getChildren().add(plevel_change1);
        plevel_change1.setOnAction((ActionEvent event) -> {changeprev1();});
        
        ////-----LEVEL 2 PRIVILLAGE LEVEL
         plevel_change2=new Button("Change",new Glyph("FontAwesome","EDIT")) ;
        plevel_change2.setLayoutX(1310);
        plevel_change2.setLayoutY(177);
        plevel.getChildren().add(plevel_change2);
        plevel_change2.setOnAction((ActionEvent event) -> {changeprev2();});
        
        //////------LEVEL 3 PRIVILLAGE LEVEL
         plevel_change3=new Button("Change",new Glyph("FontAwesome","EDIT")) ;
        plevel_change3.setLayoutX(314);
        plevel_change3.setLayoutY(677);
        plevel.getChildren().add(plevel_change3);
        plevel_change3.setOnAction((ActionEvent event) -> {changeprev3();});
        
        //////------LEVEL 4 PRIVILLAGE LEVEL
        plevel_change4=new Button("Change",new Glyph("FontAwesome","EDIT")) ;
        plevel_change4.setLayoutX(1310);
        plevel_change4.setLayoutY(640);
        plevel.getChildren().add(plevel_change4);
        plevel_change4.setOnAction((ActionEvent event) -> {changeprev4();});
        
        mmbut=new Button("Send",new Glyph("FontAwesome","SEND")) ;
        mmbut.setLayoutX(691);
        mmbut.setLayoutY(473);
       communication.getChildren().add(mmbut);
       mmbut.setOnAction((ActionEvent event) -> {senmmsg();});
       //---------------
        umbut=new Button("Send",new Glyph("FontAwesome","SEND")) ;
        umbut.setLayoutX(691);
        umbut.setLayoutY(788);
       communication.getChildren().add(umbut);
       
       umbut.setOnAction((ActionEvent event) -> {senumsg();});
       ///////----------------
        bmbut=new Button("Send",new Glyph("FontAwesome","SEND")) ;
        bmbut.setLayoutX(691);
        bmbut.setLayoutY(138);
        communication.getChildren().add(bmbut);
        bmbut.setOnAction((ActionEvent event) -> {senbmsg();});
                
        //checkcombobox
      multicastbox.setLayoutX(138);
      multicastbox.setLayoutY(394);
      communication.getChildren().add(multicastbox);
      //-------------------
        createbwm(bwmpane);


        Thread st = new Thread(setsrt);
        st.start();
        
        Thread settab = new Thread(tabset);
        settab.start();
        
        Thread upp = new Thread(upplv);
        upp.start();             
        setmsg();
        opsel();
	
        
    }

    @FXML
    private void startlivemonitoring(MouseEvent event) {
        
               
        if(!Pcaper.livemonitoring&&!pause)
        { 
            Pcaper.data.clear();
            Pcaper.pcount=0;
            Pcaper.pd.clear();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Filter");
         alert.setContentText("Do you want add filters to packet monitoring?");
         ButtonType buttonTypeOne = new ButtonType("Yes");
         ButtonType buttonTypeTwo = new ButtonType("No");
         alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
         Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == buttonTypeOne){
              Pcaper.filtr=true;
           }  
        }
        if(Pcaper.filtr&&!pause){
            
Dialog<Server.flt> dialog = new Dialog<>();
Server.flt fl=new Server.flt();
dialog.setTitle("Filters");
dialog.setHeaderText("Choose the filters you want to apply. \nUse ; to diffretiate two different entities.");
ButtonType loginButtonType = new ButtonType("Request", ButtonBar.ButtonData.OK_DONE);
dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
GridPane grid = new GridPane();
grid.setHgap(10);
grid.setVgap(10);
grid.setPadding(new Insets(20, 150, 10, 10));

TextField soip = new TextField();
soip.setPromptText("Enter The Source IPs you want to monitor with a ; separting each new one");

TextField destiip = new TextField();
destiip.setPromptText("Enter The Destination IPs you want to monitor with a ; separting each new one");

TextField destinp = new TextField();
destinp.setPromptText("Enter The Destination Ports you want to monitor with a ; separting each new one");

TextField sop = new TextField();
sop.setPromptText("Enter The Source Ports you want to monitor with a ; separting each new one");

TextField prtc = new TextField();
prtc.setPromptText("Enter The Protocol you want to monitor with a ; separting each new one");

grid.add(new Label("Enter Source IPs :"), 0, 0);
grid.add(soip, 1, 0);

grid.add(new Label("Enter Destination IPs  :"), 0, 1);
grid.add(destiip, 1, 1);

grid.add(new Label("Enter Source Ports :"), 0, 2);
grid.add(sop, 1, 2);

grid.add(new Label("Enter Destination Ports :"), 0, 3);
grid.add(destinp, 1, 3);

grid.add(new Label("Enter Protocols :"), 0, 4);
grid.add(prtc, 1, 4);

dialog.getDialogPane().setContent(grid);

dialog.setResultConverter(dialogButton -> {
    if (dialogButton == loginButtonType) {
        if(!soip.getText().trim().isEmpty())
        fl.srcip=soip.getText().trim().split(";");
        
        if(!destiip.getText().trim().isEmpty())
        fl.dstip=destiip.getText().trim().split(";");
        
        if(!destinp.getText().trim().isEmpty())
        fl.dstp=destinp.getText().trim().split(";");
        
        if(!sop.getText().trim().isEmpty())
        fl.srcp=sop.getText().trim().split(";");
        
        if(!prtc.getText().trim().isEmpty())
        fl.prtcl=prtc.getText().trim().split(";");
        
        return fl;
    }
    return null;
});

Optional<Server.flt> result = dialog.showAndWait();

        if (result.isPresent()) {
            Pcaper.fltr=result.get(); 
        }
        
        }
        pause=false;
        Pcaper.livemonitoring=true; 
        Thread th = new Thread(pctsk);
        th.setDaemon(true);
        th.start();
    }

    @FXML
    private void stoplivemonitoring(MouseEvent event) {
        if(Pcaper.livemonitoring)
        { 
          Pcaper.livemonitoring=false;
          stopped=true;
          Pcaper.filtr=false;
        }
        else
        {
           
            Pcaper.sholddata=false;
            Pcaper.data.clear();
            stopped=false;
            
        }
    }
    
    private void pausemonitoring(MouseEvent event) {
        if(Pcaper.livemonitoring)
        {
        pause=true;
        Pcaper.livemonitoring=false;
        }
    }

    @FXML
    private void showtotalcapturedata(MouseEvent event) {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Information Dialog");
         alert.setHeaderText("Opening Captured Data");
         alert.setContentText("It might take some time to load fully depending on the capture size.....");
         alert.showAndWait();
         Pcaper.sholddata=true;
         Pcaper.data.clear();
         read=new Thread(new Runnable() {public void run(){
         Pcaper.readpacketfromfile(10);}});
         read.start(); 
         Thread th = new Thread(pctsk);
         th.setDaemon(true);
         th.start();
    }

    @FXML
    private void close(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Exit");
         alert.setContentText("Do you really want to CLOSE this application now??");
         ButtonType buttonTypeOne = new ButtonType("Yes");
         ButtonType buttonTypeTwo = new ButtonType("No");
         alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

          Optional<ButtonType> result = alert.showAndWait();
          if (result.get() == buttonTypeOne){
              
              Server.closeprogram();
           } 

    }

    @FXML
    private void about(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("About");
         alert.setHeaderText("About : ");
         alert.setContentText("\nThis is the server side application of Agent Based Network Monitoring System.\nThis software is created by TR Chandrashekar and Bhatt Gunjan as a part of final year project during 2015-2016.");
         alert.showAndWait();
    }
    
    public static void showwarning(String a){
         Platform.runLater(() -> {
         Alert alert = new Alert(Alert.AlertType.WARNING);
         alert.setContentText(a);
         alert.showAndWait();
          });
      
    }
    
    public static void showinfo(String a){
        Platform.runLater(() -> {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setContentText(a);
         alert.showAndWait();
          });
         
      
    }
    
    public static boolean getconform(String a){
        
        boolean t=false;
        
    final FutureTask query = new FutureTask(new Callable() {
    @Override
    public Boolean call() throws Exception {
        boolean g=false;
        Alert al=new Alert(Alert.AlertType.CONFIRMATION); 
        al.setContentText(a);
        ButtonType yes = new ButtonType("YES");
        ButtonType no = new ButtonType("NO");
        al.getButtonTypes().setAll(yes,no);
        
        Optional<ButtonType> result = al.showAndWait();
        
        if (result.get() == yes){
            g=true;
        }
        return(g);
    }
});
     Platform.runLater(query);
        try {
            t=Boolean.parseBoolean(query.get().toString());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     return(t);
    }
    
    @FXML
    private void webblockclick(MouseEvent event) {
     
            block=new Thread(new Runnable() {public void run(){
            String website = webname.getText(); 
            final String wuser = bwuser.getValue();
            String typ=webtypes.getValue();
            if(typ.equalsIgnoreCase("Others")&&!custtyp.getText().trim().isEmpty())
                typ=custtyp.getText();
            final String wtyp=typ;
            
            String wb=null;
            final String[] wbs=website.split("[\\r\\n]+");
            for(int i=0;i<wbs.length;i++){
            if(wuser.equalsIgnoreCase("All"))
            {
                try {
                    Accesscontrol.checkweb(wbs[i]);
                    Accesscontrol.websiteblock(wbs[i]);
                    databse.wbsblck(wbs[i],wuser,wtyp);
                    updateacctab("All",wbs[i],"add");
                } catch (UnknownHostException ex) {
                    showwarning("The Website : "+wbs[i]+"doesn't exist");
                }
                      
            }
            else
            {
                try {
                    Accesscontrol.checkweb(wbs[i]);
                    databse.wbsblck(wbs[i],wuser,wtyp);
                    updateacctab(wuser,wbs[i],"add");
                    for(int j=0;j<Server.actuser.size();j++){
                    if(actu.data.get(j).user.equalsIgnoreCase(bwuser.getValue())){
                      Accesscontrol.websiteblock(wbs[i],actu.data.get(j).useIP);
                      break;
                    }
                 }
                    
                } catch (UnknownHostException ex) {
                    showwarning("The Website : "+wbs[i]+"doesn't exist");
                }
            }
      }
           }});
            block.start();
    }

    void updateacctab(String u,String w,String op){
        
    Server.accesstab e=new Server.accesstab();
    if("add".equals(op)){
       boolean c=true;
       for(int i=0;i<Server.accesstab.acstb.size();i++){ 
       if(Server.accesstab.acstb.get(i).unam.equalsIgnoreCase(u)){
       e.bw=Server.accesstab.acstb.get(i).bw+w;
       e.lck=Server.accesstab.acstb.get(i).lck;
       e.unam=u;
       Server.accesstab.acstb.set(i, e);
       c=false;
       break;
       }
       }
       if(c){
       String l="unblocked";    
       e.bw=w+"\n";
       for(int i=0;i<Server.blocku.size();i++){
           if(u.equalsIgnoreCase(Server.blocku.get(i))){
           l="blocked";
           break;
           }
       }
       e.lck=l;
       e.unam=u;
       Server.accesstab.acstb.add(e);
       }
    }
    else{
       
       for(int i=0;i<Server.accesstab.acstb.size();i++){ 
       if(Server.accesstab.acstb.get(i).unam.equalsIgnoreCase(u)){
       e.bw=Server.accesstab.acstb.get(i).bw.replace(w,"");
       e.lck=Server.accesstab.acstb.get(i).lck;
       e.unam=u;
       Server.accesstab.acstb.set(i, e);
       break;
       }
       }  
    }
    }
    
    @FXML
    private void hometabselection(Event event) {
         if (home.isSelected()) {
             Pcaper.nobytes=0;
               if(moveNeedle.isAlive())
                   moveNeedle.resume();
             
                }
         else{
             moveNeedle.suspend();
         }
    }

    @FXML
    private void openreq(MouseEvent event) {
       setreq();
    }
    
    void setreq(){
        databse.accpt();
        if(databse.fname!=null){
        fna.setText(databse.fname);
        un.setText(databse.uid);
        eid.setText(databse.email);
        mno.setText(databse.mobno.toString());
        priv.setText("");
        }else{
        fna.setText("");
        un.setText("");
        eid.setText("");
        mno.setText("");
        priv.setText("");
        FXMLDocumentController.showinfo("No Registration Request is pending now");
        }
}

    @FXML
    private void validater(MouseEvent event) {
       if(!un.getText().isEmpty()){
        String privl=priv.getText();
        privl=privl.trim();
        if(privl.contentEquals("1")||privl.contentEquals("2")||privl.contentEquals("3")||privl.contentEquals("4")){
        databse.wrtdatalogin(databse.uid,databse.pass,privl,"wr");

          Server.client c=new client(un.getText(),fna.getText(),eid.getText(),"Inactive",mno.getText(),Integer.parseInt(privl));
          Server.client.clist.add(c);
          
        showinfo("The Registration Request has been accepted");
        setreq();
        }else{
            showwarning("Enter valid privilege level");
        }
       }else{
       
       }
        
    }

    @FXML
    private void rejectr(MouseEvent event) {
        if(!un.getText().isEmpty()){
        
        if(getconform("Do you want to  Reject "+un.getText()+"'s Registration Request")){
        String privl="1";
        databse.wrtdatalogin(databse.uid,databse.pass,privl,"del");
        setreq();
        }
        }
    }

    @FXML
    private void unlockweb(MouseEvent event) throws IOException {
         try {
             
            String website =  webtypes111.getValue();
            if(ubwuser.getValue().equalsIgnoreCase("All"))
            {
                Accesscontrol.websiteunblock(website);
                databse.rmwbsblck(website,"All");
                updateacctab("All",website,"rem");
            }
            else{
                 databse.rmwbsblck(website,bwuser.getValue());
                 updateacctab(bwuser.getValue(),website,"rem");
                 for(int i=0;i<Server.actuser.size();i++){
                    if(actu.data.get(i).user.equalsIgnoreCase(bwuser.getValue())){
                      Accesscontrol.websiteunblock(website,actu.data.get(i).useIP);
                    }
                 }
            }
            //Accesscontrol.websiteunblock(website);
        } catch (UnknownHostException ex) {
            showwarning("Wrong website name");
        }
    }

    @FXML
    private void notifyenadis(ActionEvent event) {
        if(!enadisabutton.isSelected()){
        enadisabutton.setText("Disabled");
        bu.getParent().setDisable(true);
        Server.mail=false;
        }
        else{
            enadisabutton.setText("Enabled");
            bu.getParent().setDisable(false);
            Server.mail=true;
    }
    }

    private void wsrefresh(MouseEvent event) throws InterruptedException {
         final int f;
         Pcaper.webl.clear();
         weblv.setItems(null);
         weblv.refresh();
         String j=tpall.getValue();
         if(j.equalsIgnoreCase("Top 5 Websites"))
             f=5;
         else
             f=0;
         refreshws.setDisable(true);
         server8.Pcaper.sholddata=false;
         read=new Thread(new Runnable() {public void run(){
         Pcaper.readpacketfromfile(f);
         Platform.runLater(() -> {
         weblv.setItems(Pcaper.webl);
         refreshws.setDisable(false);
          });
         }});
         
         read.start();
         
    }  


    public static void refrbanu() {
        Server.fetchusde();
          for(int j=0;j<Server.actuser.size();j++){
                    for(int k=0;k<Server.alluser.size();k++){
                   if(Server.alluser.get(k).equalsIgnoreCase(Server.actuser.get(j))){
                       busage b=new busage(Server.alluser.get(k),Server.busage.bdat.get(k).curr,Server.busage.bdat.get(k).tot,Server.actbnd.get(j),Server.privil.get(k));
                       Server.busage.bdat.set(k, b);   
                       break;
                   }
                    }
                } 
    }
    
    static String refreshtime(String t){
    String [] ti=new String[3];
    ti=t.split(":");
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
    t=h+":"+m+":"+s;
    return(t);
    }
    
    public static void actref(){
        while(true){
        try {
            if(Server.actuser.size()!=0){
                for(int j=0;j<Server.actuser.size();j++){
                    for(int k=0;k<Server.alluser.size();k++){
                   if(Server.alluser.get(k).equalsIgnoreCase(Server.actuser.get(j))){
                       busage b=new busage(Server.alluser.get(k),refreshtime(Server.busage.bdat.get(k).curr),refreshtime(Server.busage.bdat.get(k).tot),Server.allbnd.get(k),Server.privil.get(k));
                       Server.busage.bdat.set(k, b);   
                       break;
                     }
                    }
                }                
            }
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }

    @FXML
    private void notiac(ActionEvent event) {
        opsel();
    }
    
    void opsel(){
        
    Server.op.opdata.clear();
        if(bu.isSelected()){
        Server.op e=new Server.op();
        e.opt="Bandwidth usage";
        Server.op.opdata.add(e);
        }
        if(st.isSelected()){
        Server.op c=new Server.op();
        c.opt="Session Time";
        Server.op.opdata.add(c);
        }      
        if(maw.isSelected()){
        Server.op l=new Server.op();
        l.opt="Most accessed websites";        
        Server.op.opdata.add(l);
        }
        if(at.isSelected()){
        Server.op k=new Server.op();
            k.opt="Alerts";
        Server.op.opdata.add(k);
        }
    }
    
Task<Void> pctsk = new Task<Void>() {
         @Override protected Void call() throws Exception {
        pno.setCellValueFactory(new PropertyValueFactory<>("frameno"));
        time.setCellValueFactory(new PropertyValueFactory<>("capTime"));
        plen.setCellValueFactory(new PropertyValueFactory<>("packetSize"));
        proto.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        smac.setCellValueFactory(new PropertyValueFactory<>("sourceMAC"));
        sip.setCellValueFactory(new PropertyValueFactory<>("sourceIP"));
        sp.setCellValueFactory(new PropertyValueFactory<>("sourcePort"));
        dmac.setCellValueFactory(new PropertyValueFactory<>("destMAC"));
        dip.setCellValueFactory(new PropertyValueFactory<>("destIP"));
        dp.setCellValueFactory(new PropertyValueFactory<>("destPort"));
        ptable.setItems(Pcaper.data);
        return null;
         }
     };

    void remser(String n,String p) throws IOException{
            for(int i=0;i<servoblist.servdata.size();i++){
            if(servoblist.servdata.get(i).tablename.equalsIgnoreCase(n)){
                servoblist.servdata.remove(i);
                break;
            }
            }
            String[] command = {"iptables" ,"-D", "FORWARD" ,"-p" ,"tcp","--dport",p,"-j","DROP" };
            Accesscontrol.command_run(command);
    }
    
    void addser(String n,String p) throws IOException{
            servoblist s=new servoblist();
            s.tablename=n;
            s.tableport=p;
            servoblist.servdata.add(s);
            String[] command = {"iptables" ,"-A", "FORWARD" ,"-p" ,"tcp","--dport",p,"-j","DROP" };
            Accesscontrol.command_run(command);
    }

    @FXML
    private void serviceblock1(ActionEvent event) throws IOException {
       if(ser1.isSelected()){
           addser("File Transfer Protocol","5215");
           
       } 
       else{
          remser("File Transfer Protocol","5215");
                   
           
       }
        
    }

    @FXML
    private void serviceblock2(ActionEvent event) throws IOException {
        
        if(ser2.isSelected()){
           addser("Secure shell","22");
           
       } 
       else{
          remser("Secure Shell","22");
                   
           
       }
    }

    @FXML
    private void serviceblock3(ActionEvent event) throws IOException {

        if(ser3.isSelected()){
           addser("Simple Mail Tranfer Protocol","465");
           
       } 
       else{
          remser("Simple Mail Tranfer Protocol","465");
                   
           
       }
    }

    @FXML
    private void serviceblock5(ActionEvent event) throws IOException {
        
        if(ser5.isSelected()){
           addser("Domain Name Server","53");
           
       } 
       else{
          remser("Domain Name Server","53");
                   
           
       }
    }

    @FXML
    private void serviceblock4(ActionEvent event) throws IOException {
        if(ser4.isSelected()){
           addser("Telnet","23");
           
       } 
       else{
          remser("Telnet","23");
                   
           
       }
    }

    @FXML
    private void serviceblock7(ActionEvent event) throws IOException {
        if(ser7.isSelected()){
           addser("Post Office Protocol(Version 3)","110");
           
       } 
       else{
          remser("Post Office Protocol(Version 3)","110");
                   
           
       }
    }

    @FXML
    private void serviceblock6(ActionEvent event) throws IOException {
        if(ser6.isSelected()){
           addser("Trivial File Transfer Protocol","69");
           
       } 
       else{
          remser("Trivial File Transfer Protocol","69");        
           
       }
    }

    @FXML
    private void serviceblock8(ActionEvent event) throws IOException {
        
        if(ser8.isSelected()){
           addser("Internet Message Access Protocol","143");
           
       } 
       else{
          remser("Internet Message Access Protocol","143");
                   
           
       }
    }

    @FXML
    private void serviceblock10(ActionEvent event) throws IOException {
        if(ser10.isSelected()){
           addser("HyperText Transfer Protocol Secured","443");
           
       } 
       else{
          remser("HyperText Transfer Protocol Secured","443");
                   
           
       }
    }

    @FXML
    private void serviceblock9(ActionEvent event) throws IOException {
        
        if(ser9.isSelected()){
           addser("HyperText Transfer Protocol","80");
           
       } 
       else{
          remser("HyperText Transfer Protocol","80");
                   
           
       }
    }

void setser(){
while(!Server.setsr){
try {
        Thread.sleep(10);
    } catch (InterruptedException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
    }
}    
for(int i=0;i<servoblist.servdata.size();i++){
if(
        servoblist.servdata.get(i).tablename.equalsIgnoreCase("File Transfer Protocol"))
{
ser1.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("Secure shell")){
ser2.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("Simple Mail Tranfer Protocol")){
ser3.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("Telnet")){
ser4.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("Domain Name Server")){
ser5.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("Trivial File Transfer Protocol")){
ser6.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("Post Office Protocol(Version 3)")){
ser7.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("Internet Message Access Protocol")){
ser8.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("HyperText Transfer Protocol")){
ser9.setSelected(true);
}
else if(servoblist.servdata.get(i).tablename.equalsIgnoreCase("HyperText Transfer Protocol Secured")){
ser10.setSelected(true);
}
}
}

 Task<Void> setsrt = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            Pcaper.totp = (long) 0;
            Pcaper.totsize = (long) 0;
            Pcaper.readpacketfromfile(1000);
            long mb = Pcaper.totsize;
            mb = mb / 1024;
            final long m = mb / 1024;
              Platform.runLater(() -> {
            cpn.setText(Pcaper.totp+"");
            cps.setText(m+" MB");
          });
            
            setser();
            return null;
        }
  };

Task<Void>  tabset= new Task<Void>() {
         @Override protected Void call() throws Exception {
             
        //active user table     
        acuid.setCellValueFactory(new PropertyValueFactory<>("user"));
        acip.setCellValueFactory(new PropertyValueFactory<>("useIP"));
        activeusers.setItems(actu.data);
        
        //events and logs
        logc.setCellValueFactory(new PropertyValueFactory<>("log"));
        Logstab.setItems(Server.log);
        //user details table
        tuseid.setCellValueFactory(new PropertyValueFactory<>("useid"));
        tname.setCellValueFactory(new PropertyValueFactory<>("usename"));
        tmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcur.setCellValueFactory(new PropertyValueFactory<>("currst"));
        tpriv.setCellValueFactory(new PropertyValueFactory<>("privlev"));
        tmob.setCellValueFactory(new PropertyValueFactory<>("mobno"));
        tebut.setCellValueFactory(new PropertyValueFactory<>("editup"));
        tdbut.setCellValueFactory(new PropertyValueFactory<>("udel"));
        usedatat.setItems(client.clist);   
        
        //bandwidth usage statistics table
        bun.setCellValueFactory(new PropertyValueFactory<>("uname"));
        bus.setCellValueFactory(new PropertyValueFactory<>("bandus"));
        bmax.setCellValueFactory(new PropertyValueFactory<>("bandmax"));
        curses.setCellValueFactory(new PropertyValueFactory<>("curr"));
        tses.setCellValueFactory(new PropertyValueFactory<>("tot"));
        butable.setItems(Server.busage.bdat);
        
        //option table in notification
        op.setCellValueFactory(new PropertyValueFactory<>("opt"));
        emailoptab.setItems(Server.op.opdata);
        
        //Privilage tables
        
        pl1_web.setCellValueFactory(new PropertyValueFactory<>("privtable"));
        pl1_table.setItems(Server.privoblist.privillage1);
        
        pl2_web.setCellValueFactory(new PropertyValueFactory<>("privtable"));
        pl2_table.setItems(Server.privoblist.privillage2);
        
        pl3_web.setCellValueFactory(new PropertyValueFactory<>("privtable"));
        pl3_table.setItems(Server.privoblist.privillage3);
        
        pl4_web.setCellValueFactory(new PropertyValueFactory<>("privtable"));
        pl4_table.setItems(Server.privoblist.privillage4);
        
        //
        ser_table_1.setCellValueFactory(new PropertyValueFactory<>("tablename"));
        ser_table_2.setCellValueFactory(new PropertyValueFactory<>("tableport"));
        ser_table.setItems(Server.servoblist.servdata);
        
        //email list table
        ename.setCellValueFactory(new PropertyValueFactory<>("enam"));
        emailid.setCellValueFactory(new PropertyValueFactory<>("email"));
        ealert.setCellValueFactory(new PropertyValueFactory<>("eal"));
        ememtab.setItems(Server.etab.etabl);
        
        //access control table
        acctabb.setCellValueFactory(new PropertyValueFactory<>("bw"));
        acctabl.setCellValueFactory(new PropertyValueFactory<>("lck"));
        acctabu.setCellValueFactory(new PropertyValueFactory<>("unam"));
        acctab.setItems(Server.accesstab.acstb);
             
        return null;
    }
  };

Task<Void> upplv = new Task<Void>() {
      @Override protected Void call() throws Exception {
        boolean upd=true;     
        while(upd){
        if(Server.pl1==0.0){
        Thread.sleep(200);
        }
        else{
        upd=false;
        }
        }  
          
          Platform.runLater(() -> {
          p1mb.setText(""+Server.pl1);
          p2mb.setText(""+Server.pl2);
          p3mb.setText(""+Server.pl3);
          p4mb.setText(""+Server.pl4);
          });

        return null;
         }
     };

    private void senumsg()  {
        try {
            String[] usm=new String[2];
            usm[0]=umsg.getValue();
            for(int f=0;f<actuser.size();f++)
            {
                if(actu.data.get(f).user.equalsIgnoreCase(usm[0]))
                {
                    usm[0]=actu.data.get(f).useIP;
                }
            }
            usm[1]=umg.getText();
            chat.sendmsg(usm);
            umg.clear();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private void senbmsg()  {
         try {
            String[] usm=new String[2];
            for(int f=0;f<actuser.size();f++)
            {
                    usm[0]=actu.data.get(f).useIP;
                    usm[1]=bmg.getText();
                    chat.sendmsg(usm);
            }
            umg.clear(); 
         }catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
         }
     
     private void senmmsg()  {
          try {
            String[] usm=new String[2];
            ObservableList<String> rec=multicastbox.getItems();
            rec.forEach(((usr) -> {   
                 try {
                     for(int f=0;f<actuser.size();f++)
                     {
                         if(actu.data.get(f).user.equalsIgnoreCase(usr))
                         {
                             System.out.println(usr);
                             usm[0]=actu.data.get(f).useIP;
                         }
                     }
                     usm[1]=mmg.getText();
                     chat.sendmsg(usm);
                 } catch (Exception ex) {
                     Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                 }
            
            }));
            
            mmg.clear();
             
         }catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      }
     
     public void setmsg(){
      final Thread rmsg = new Thread(new Runnable() {public void run(){
          String rmessg=new String();
          try {
              while(true){
              rmessg+=chat.recievemsg();
              amg.setText(rmessg);
              }
          } catch (Exception ex) {
            // Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
          }
        }});
        rmsg.start(); 
     }
     
     public void changeprev1(){
    
                                TextInputDialog d1 = new TextInputDialog("");
                                d1.setTitle("Text Input Dialog");

                                d1.setContentText("Please enter Desired bandwidth Usage For Level 1 :");

                              
                                Optional<String> result = d1.showAndWait();
                                
                                if (result.isPresent()){
     
                                                         if(!result.get().isEmpty()){
    
                                                                                    if(result.get().matches("\\d+$")){
        
                                                                                                                      Double binmb=Double.parseDouble(result.get());
                                                                                                                      databse.updatebusage(1,binmb);
                                                                                                                      p1mb.setText(""+binmb);
                                                                                                                      }
                                                                                    else{
                                                                                        showwarning("please enter Bandwith in numbers");
                                                                                         }
                                                                                     }
                                                             else{
                                                                   showwarning("please enter the value");
                                                                  }
                                                        }
                                }
     
    public void changeprev2() {

        TextInputDialog d2 = new TextInputDialog("");
        d2.setTitle("Text Input Dialog");

        d2.setContentText("Please enter Desired bandwidth Usage For Level 1 :");

        Optional<String> result = d2.showAndWait();

        if (result.isPresent()) {

            if (!result.get().isEmpty()) {

                if (result.get().matches("\\d+$")) {

                    Double binmb = Double.parseDouble(result.get());
                    databse.updatebusage(2, binmb);
                    p2mb.setText("" + binmb);
                } else {
                    showwarning("please enter Bandwith in numbers");
                }
            } else {
                showwarning("please enter the value");
            }
        }
    }
      
      public void changeprev3(){
    
                                TextInputDialog d3 = new TextInputDialog("");
                                d3.setTitle("Text Input Dialog");

                                d3.setContentText("Please enter Desired bandwidth Usage For Level 1 :");

                              
                                Optional<String> result = d3.showAndWait();
                                
                                if (result.isPresent()){
     
                                                         if(!result.get().isEmpty()){
    
                                                                                    if(result.get().matches("\\d+$")){
        
                                                                                                                      Double binmb=Double.parseDouble(result.get());
                                                                                                                      databse.updatebusage(3,binmb);
                                                                                                                       p3mb.setText(""+binmb);
                                                                                                                      }
                                                                                    else{
                                                                                        showwarning("please enter Bandwith in numbers");
                                                                                         }
                                                                                     }
                                                             else{
                                                                   showwarning("please enter the value");
                                                                  }
                                                        }
                                }
      
      public void changeprev4(){
    
                                TextInputDialog d4 = new TextInputDialog("");
                                d4.setTitle("Text Input Dialog");

                                d4.setContentText("Please enter Desired bandwidth Usage For Level 1 :");

                              
                                Optional<String> result = d4.showAndWait();
                                
                                if (result.isPresent()){
     
                                                         if(!result.get().isEmpty()){
    
                                                                                    if(result.get().matches("\\d+$")){
        
                                                                                                                      Double binmb=Double.parseDouble(result.get());
                                                                                                                      databse.updatebusage(4,binmb);
                                                                                                                       p4mb.setText(""+binmb);
                                                                                                                      }
                                                                                    else{
                                                                                        showwarning("please enter Bandwith in numbers");
                                                                                         }
                                                                                     }
                                                             else{
                                                                   showwarning("please enter the value");
                                                                  }
                                                        }
                                }

    @FXML
    private void pl1_add(MouseEvent event) {
        String web_plevel1=pl1.getText();
        try{
        Accesscontrol.checkweb(web_plevel1);
        Double binmb=Double.parseDouble(p1mb.getText());
        databse.updatebusage(1,binmb,web_plevel1);
        showinfo("The Website has been added");
        } catch (UnknownHostException ex) {
            showwarning("Please Enter a Valid Website...");
        }
    }

    @FXML
    private void pl2_add(MouseEvent event) {
        String web_plevel2=pl2.getText();
        try{
        Accesscontrol.checkweb(web_plevel2);
        Double binmb=Double.parseDouble(p2mb.getText());
       databse.updatebusage(2,binmb,web_plevel2);
       showinfo("The Website has been added");
        } catch (UnknownHostException ex) {
            showwarning("Please Enter a Valid Website...");
        }
    }

    @FXML
    private void pl4_add(MouseEvent event) {
        String web_plevel4=pl4.getText();
        try{
        Accesscontrol.checkweb(web_plevel4);
        Double binmb=Double.parseDouble(p4mb.getText());
       databse.updatebusage(4,binmb,web_plevel4);
       showinfo("The Website has been added");
        } catch (UnknownHostException ex) {
            showwarning("Please Enter a Valid Website...");
        }
    }

    @FXML
    private void pl3_add(MouseEvent event) {
        String web_plevel3=pl3.getText();
        try{
        Accesscontrol.checkweb(web_plevel3);
        Double binmb=Double.parseDouble(p3mb.getText());
        databse.updatebusage(3,binmb,web_plevel3);
        showinfo("The Website has been added");
        } catch (UnknownHostException ex) {
            showwarning("Please Enter a Valid Website...");
        }
    }

    @FXML
    private void addemem(MouseEvent event) {
       Server.etab m=new Server.etab();
       m.enam=nm.getText();
       m.email=emid.getText();
       m.eal=" ";
       Server.etab.etabl.add(m);
       Server.emm.add(m.enam);
       databse.addememtd(m);
       nm.clear();
       emid.clear();
       ademb.setDisable(true);
    }

    @FXML
    private void rememem(MouseEvent event) {
        String e=ememn.getValue();
        for(int i=0;i<Server.emm.size();i++){
            if(Server.emm.get(i).equalsIgnoreCase(e)){
                Server.emm.remove(i);
                Server.etab.etabl.remove(i);
                databse.rmememtd(e);
                break;
            }
            }
    }

    @FXML
    private void usblckunblck(MouseEvent event) {
        String d=bubuser.getValue();
        String s="";
       if(rbblcu.isSelected()){
           databse.blck(d,"blocked");
           for(int i=0;i<Server.unblocku.size();i++){
            if(Server.unblocku.get(i).equalsIgnoreCase(d))
           {
              Server.unblocku.remove(i);
              Server.blocku.add(d);
              break;
           }
         }
           s="blocked";
        }
        else{
           databse.blck(d,"unblc");
           for(int i=0;i<Server.blocku.size();i++){
            if(Server.blocku.get(i).equalsIgnoreCase(d))
           {
              Server.blocku.remove(i);
              Server.unblocku.add(d);
              break;
           }
         }
           s="unblocked";
        }
       for(int i=0;i<Server.accesstab.acstb.size();i++){
       if(Server.accesstab.acstb.get(i).unam.equalsIgnoreCase(d)){
           Server.accesstab v=new Server.accesstab();
           v.bw=Server.accesstab.acstb.get(i).bw;
           v.unam=d;
           v.lck=s;
       Server.accesstab.acstb.set(i,v);
       break;
       }
       }
       
    }

    @FXML
    private void loadbub(MouseEvent event) {
        if(rbblcu.isSelected()){
        bubuser.setItems(Server.unblocku);
        }
        else{
        bubuser.setItems(Server.blocku);
        }
    }

    @FXML
    private void loadweb(MouseEvent event) {
        Server.weblis.clear();
        if(ubwuser.getValue()!=null){
        for(int i=0;i<Server.accesstab.acstb.size();i++)
        {
            if(ubwuser.getValue().equalsIgnoreCase(Server.accesstab.acstb.get(i).unam))
            {
              final String[] wbs=Server.accesstab.acstb.get(i).bw.split("[\\r\\n]+"); 
              for(int j=0;j<wbs.length;j++){
               Server.weblis.add(wbs[j]);
              } 
            }
        }
         webtypes111.setItems(Server.weblis);
        }
    }

    @FXML
    private void sendnow(MouseEvent event) {
        Server.sendmail();
        
    }

    @FXML
    private void genuncd(MouseEvent event) {
        String us=unccdgen.getValue();
        String code=passGen.gens();
        uncd.setText(code);
        Server.unlcd.add(us+":"+code);
    }
    
    
    public void reqfieldvalidator(TextArea chck){
        if(chck.getText()==null)
            showwarning("\tYou cannot leave "+chck.getId()+" empty.....\nPlease enter a valid value ");
    } 

    @FXML
    private void addcrule(MouseEvent event) {
        
        String r=crule.getText();
        String d=cdesc.getText();
        String output="";
        try {
            System.out.println(r);
            output=Accesscontrol.command_run(r);
            System.out.println(output);
            if(!output.contains("Try"))
            {
              System.out.println("adding to database");
              databse.incrule(r, d);
            }
        } catch (IOException ex) {
            showwarning("Entered wrong rule.......");
        }
        coutput.setText(output);
    }

    @FXML
    private void chngadminpass(MouseEvent event) {
        if(databse.chcklogindata("Admin", currp.getText())){
        databse.uppass("Admin", nps1.getText());
        showinfo("Admin Password updated");
        }
        else{
        showwarning("Wrong Password.....\nTry Again");
        }
        nps1.clear();
        nps2.clear();
        currp.clear();
        adpcb.setDisable(true);
    }

    @FXML
    private void chckfrcustom(MouseEvent event) {
        try{
        if(webtypes.getValue().equalsIgnoreCase("Others")){
        custtyp.setVisible(true);
        }
        else
        custtyp.setVisible(false);
        }catch(Exception ex){}
    }

    @FXML
    private void chngmailtym(MouseEvent event) {
    List<Integer> choices = new ArrayList<>();
    for(int i=1;i<11;i++)
        choices.add(i);

ChoiceDialog<Integer> dialog = new ChoiceDialog<>(2, choices);
dialog.setTitle("Mail Time Period");
dialog.setHeaderText("Choose the no. of hour between the sending of mail notification");
dialog.setContentText("Choose your time Period:");

// Traditional way to get the response value.
Optional<Integer> result = dialog.showAndWait();
if (result.isPresent()){
    System.out.println("Your choice: " + result.get());
    Server.nohr=result.get();
}
}

    @FXML
    private void remvfrmprl1(Event event) {
        
       Server.privoblist  item = Server.privoblist.privillage1.get(pl1_table.getSelectionModel().getSelectedIndex());
       
       
       if (item != null){ 
            databse.removeprivweb(1,item.privtable,pl1_table.getSelectionModel().getSelectedIndex());
        }
 
    }

    @FXML
    private void remvfrmprl3(Event event) {
        Server.privoblist  item = Server.privoblist.privillage3.get(pl3_table.getSelectionModel().getSelectedIndex());
        //boolean t=getconform("Do you really want to remove "+item.privtable); 
       if (item != null){ 
            databse.removeprivweb(3,item.privtable,pl3_table.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void remvfrmprl2(Event event) {
        Server.privoblist  item = Server.privoblist.privillage2.get(pl2_table.getSelectionModel().getSelectedIndex());
       // boolean t=getconform("Do you really want to remove "+item.privtable); 
       if (item != null){ 
            databse.removeprivweb(2,item.privtable,pl2_table.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void remvfrmprl4(Event event) {
        Server.privoblist  item = Server.privoblist.privillage4.get(pl4_table.getSelectionModel().getSelectedIndex());
        //boolean t=getconform("Do you really want to remove "+item.privtable); 
       if (item != null){ 
            databse.removeprivweb(4,item.privtable,pl4_table.getSelectionModel().getSelectedIndex());
        }    
    } 
   }
 