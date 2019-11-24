package server8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server8.databse.actu;

public class Server extends Application {

    public static boolean setsr = false;
    public static ObservableList<String> actuser = FXCollections.observableArrayList();
    public static ObservableList<Double> actbnd = FXCollections.observableArrayList();
    public static ObservableList<Double> allbnd = FXCollections.observableArrayList();
    public static ObservableList<String> alltym = FXCollections.observableArrayList();
    public static ObservableList<String> alluser = FXCollections.observableArrayList();
    public static ObservableList<Integer> privil = FXCollections.observableArrayList();
    public static ObservableList<String> allemail = FXCollections.observableArrayList();
    public static ObservableList<String> allmobno = FXCollections.observableArrayList();
    public static ObservableList<String> allname = FXCollections.observableArrayList();
    public static ObservableList<String> emm = FXCollections.observableArrayList();
    public static ObservableList<String> blocku = FXCollections.observableArrayList();
    public static ObservableList<String> unblocku = FXCollections.observableArrayList();
    public static ObservableList<String> weblis = FXCollections.observableArrayList();
    public static ObservableList<String> accus = FXCollections.observableArrayList();
    public static ArrayList<String> unlcd = new ArrayList<>();
    public static ObservableList<Logs> log = FXCollections.observableArrayList();

    public static int nohr = 2;
    public static Double pl1 = 0.0, pl2 = 0.0, pl3 = 0.0, pl4 = 0.0;
    public static String ui, un, um, ue, up;
    public static boolean mail = true;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                closeprogram();
            }
        });

        //Starting Pcap
        final Thread Pcapture = new Thread(new Runnable() {
            public void run() {
                try {
                    
                    while(stage.isShowing()){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    while(!stage.isShowing()){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("pcap start");
                    Pcaper.pcap();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Pcapture.start();

        final Thread userup = new Thread(new Runnable() {
            public void run() {
                databse.getalluser();
                for (int i = 0; i < Server.allname.size(); i++) {
                    busage b = new busage(Server.alluser.get(i), "0:0:0", Server.alltym.get(i), Server.allbnd.get(i), Server.privil.get(i));
                    Server.busage.bdat.add(b);
                    //client(String usid,String usname,String emil,String crrst,Integer mbno,Integer prvlev)
                    client c = new client(Server.alluser.get(i), Server.allname.get(i), Server.allemail.get(i), "Inactive", Server.allmobno.get(i), Server.privil.get(i));
                    client.clist.add(c);
                }
            }
        });
        userup.start();

        final Thread ulogin = new Thread(new Runnable() {
            public void run() {
                try {
                    clientlogin();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ulogin.start();

        final Thread usreq = new Thread(() -> {
            try {
                userreq();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        usreq.start();

        final Thread act = new Thread(new Runnable() {
            public void run() {
                FXMLDocumentController.actref();
            }
        });
        act.start();

        userup.join();

        final Thread mail = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                sendmail();
            }
        });

        Calendar calendar = Calendar.getInstance();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(mail, millisToNextHour(calendar), nohr * 60 * 60 * 1000, TimeUnit.MILLISECONDS);

        databse.startuptask();
        setsr = true;

        final Thread sendactu = new Thread(new Runnable() {
            public void run() {
                try {
                    senduinf();
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        sendactu.start();

    }

    static void closeprogram() {
        Pcaper.dumper.flush();
        Pcaper.dumper.close(); // Won't be able to delete without explicit close  
        Pcaper.pcap.close();
        databse.exittasks();
        System.exit(0);
    }

    static void clientlogin() throws IOException {
        ServerSocket soc = new ServerSocket(5217);
        DataInputStream din;
        DataOutputStream dout;
        while (true) {
            Socket sock = soc.accept();
            din = new DataInputStream(sock.getInputStream());
            dout = new DataOutputStream(sock.getOutputStream());
            String uandp = din.readUTF();
            String split[] = uandp.split(";");

            if (split[0].equalsIgnoreCase("clogin")) {
                client.login(split, sock, dout);
            } else if (split[0].equalsIgnoreCase("clogout")) {
                client.logout(split, dout, sock);
            } else if (split[0].equalsIgnoreCase("creg")) {
                client.register(split, dout, sock);
            }

            sock.close();
        }
    }

    static void userreq() throws IOException {
        ServerSocket soc = new ServerSocket(5218);
        DataInputStream din;
        DataOutputStream dout;
        boolean m;
        while (true) {
            Socket sock = soc.accept();
            m = false;
            din = new DataInputStream(sock.getInputStream());
            dout = new DataOutputStream(sock.getOutputStream());
            String uandp = din.readUTF();
            String split[] = uandp.split(":");
            if (split[0].equalsIgnoreCase("unblock")) {
                m = unblockreq(split);
                if (m) {
                    dout.writeUTF("accepted");
                } else {
                    dout.writeUTF("error");
                }
            } else if (split[0].equalsIgnoreCase("privchg")) {
                m = privchangereq(split);
                if (m) {
                    dout.writeUTF("accepted");
                } else {
                    dout.writeUTF("error");
                }
            }
            sock.close();
        }
    }

    static void sendmail() {

        addtolog("Sending mail");

        if (!mail) {
            System.out.println("not Sending mail");
            return;
        }
        for (int z = 0; z < Server.etab.etabl.size(); z++) {
            Gmail.to.add(Server.etab.etabl.get(z).email);
        }

        String m = "";
        for (int j = 0; j < op.opdata.size(); j++) {
            if (op.opdata.get(j).opt.equalsIgnoreCase("Most accessed websites")) {
                Pcaper.webl.clear();
                server8.Pcaper.sholddata = false;
                System.out.println("Adding Most accessed websites");
                String l = new String("\n\n The top 5 Websites were : \n");
                Pcaper.readpacketfromfile(5);
                if (Pcaper.webl.size() == 5) {
                    for (int i = 0; i < 5; i++) {
                        l += ("\t\t\t\t\t\t" + Pcaper.webl.get(i) + "\n");
                    }
                }
                m = m.concat(l);
            } else if (op.opdata.get(j).opt.equalsIgnoreCase("Session Time")) {
                System.out.println("Adding Session Time");
                String l = new String("\n\n The total Session Time of all Users till now are : \n\n");
                for (int i = 0; i < alluser.size(); i++) {
                    l += "\t\t\t\t\t\t" + alluser.get(i) + " : " + alltym.get(i) + "\n";
                }
                m = m.concat(l);
            } else if (op.opdata.get(j).opt.equalsIgnoreCase("Bandwidth usage")) {
                System.out.println("Adding Bandwidth usage");
                String l = new String("\n\n The total Bandwidth Usage of all Users till now are : \n\n");
                for (int i = 0; i < alluser.size(); i++) {
                    l += "\t\t\t\t\t\t" + alluser.get(i) + " : " + Math.round(allbnd.get(i)) + "  MB" + "\n";
                }
                m = m.concat(l);
            } else if (op.opdata.get(j).opt.equalsIgnoreCase("Alerts")) {
                System.out.println("Adding Alerts");
                String l = new String("\n\n The total packet information are : \n");
                Pcaper.totp = (long) 0;
                Pcaper.totsize = (long) 0;
                Pcaper.readpacketfromfile(1000);
                long mb = Pcaper.totsize;
                mb = mb / 1024;
                mb = mb / 1024;
                l += "\t\t\t\t\t\t The total no. of packets are : " + Pcaper.totp + "\n";
                l += "\t\t\t\t\t\t The total Size of all packets are : " + mb + " MB \n";
                l += "\n\n The Alerts till now are : \n\n";
                for (int i = 0; i < log.size(); i++) {
                    l += "->" + log.get(i) + "\n";
                }
                m = m.concat(l);
            }
        }
        System.out.println("\n\nMail Is : \n" + m);
        Gmail.mail(m);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class busage {

        String uname;
        Double bandus, bandmax;
        String curr, tot;
        static ObservableList<busage> bdat = FXCollections.observableArrayList();

        busage(String n, String c, String t, Double bnu, int p) {
            this.uname = n;
            this.bandus = bnu;
            this.curr = c;
            this.tot = t;
            if (p == 1) {
                this.bandmax = 256.0;
            } else if (p == 2) {
                this.bandmax = 512.0;
            } else if (p == 3) {
                this.bandmax = 1024.0;
            } else if (p == 4) {
                this.bandmax = 5120.0;
            }
        }

        public String getUname() {
            return (uname);
        }

        public Double getBandus() {
            return (bandus);
        }

        public Double getBandmax() {
            return (bandmax);
        }

        public String getCurr() {
            return (curr);
        }

        public String getTot() {
            return (tot);
        }

    }

    static void fetchusde() {
        String IPadd = null;
        actbnd.clear();
        for (int j = 0; j < actuser.size(); j++) {
            IPadd = actu.data.get(j).useIP;
            Socket ClientSoc;
            DataOutputStream dout = null;
            DataInputStream din = null;
            try {
                ClientSoc = new Socket(IPadd, 5221);
                din = new DataInputStream(ClientSoc.getInputStream());
                dout = new DataOutputStream(ClientSoc.getOutputStream());
                dout.writeUTF("sendurinfo");
                String msgFromServer = din.readUTF();
                if (!msgFromServer.equalsIgnoreCase("error")) {
                    String msg[] = msgFromServer.split(":");
                    Double bused = Double.parseDouble(msg[1]);

                    for (int i = 0; i < actuser.size(); i++) {
                        if (msg[0].equalsIgnoreCase(actuser.get(i))) {
                            actbnd.add(i, bused);
                            for (int v = 0; v < alluser.size(); v++) {
                                if (alluser.get(v).equalsIgnoreCase(actuser.get(i))) {
                                    allbnd.set(v, bused);
                                }
                            }
                            System.out.println(actbnd.get(i));
                            break;
                        }

                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private static long millisToNextHour(Calendar calendar) {
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int millis = calendar.get(Calendar.MILLISECOND);
        int minutesToNextHour = 60 - minutes;
        int secondsToNextHour = 60 - seconds;
        int millisToNextHour = 1000 - millis;
        long l = minutesToNextHour * 60 * 1000 + secondsToNextHour * 1000 + millisToNextHour;
        System.out.println(l / 60 / 1000);
        return l;
    }

    public static boolean unblockreq(String[] split) {
        String user = split[1];
        boolean log = FXMLDocumentController.getconform("The user " + user + " has requested you to unblock his account.\nDo You want to unblock the account");
        if (log) {
            for (int i = 0; i < blocku.size(); i++) {
                if (blocku.get(i).equalsIgnoreCase(user)) {
                    blocku.remove(i);
                    unblocku.add(user);
                    break;
                }
            }
            databse.blck(user, "unblc");
            accesstab e = new accesstab();
            for (int i = 0; i < accesstab.acstb.size(); i++) {
                if (accesstab.acstb.get(i).unam.equalsIgnoreCase(user)) {
                    e.bw = accesstab.acstb.get(i).bw;
                    e.lck = "UNBLOCKED";
                    e.unam = user;
                    accesstab.acstb.set(i, e);
                    break;
                }
            }
            return (true);
        } else {
            return (false);
        }

    }

    public static boolean privchangereq(String[] split) {
        String user = split[1];
        String pl = split[2];
        String nplv = split[3];
        String reason = split[4];
        boolean log = FXMLDocumentController.getconform("The user " + user + " has requested you to change his Privillage level from " + pl + " to " + nplv + ".\n\nThe Reason as given by him is :\n\t " + reason + ".\nDo You want to accept his request??");

        if (log) {

            for (int i = 0; i < client.clist.size(); i++) {
                if (client.clist.get(i).useid.equalsIgnoreCase(user)) {
                    client c = new client(client.clist.get(i).useid, client.clist.get(i).usename, client.clist.get(i).email, client.clist.get(i).currst, client.clist.get(i).mobno, Integer.parseInt(nplv));
                    client.clist.set(i, c);
                    break;
                }

            }
            for (int j = 0; j < alluser.size(); j++) {

                if (user.equalsIgnoreCase(alluser.get(j))) {
                    Server.privil.set(j, Integer.parseInt(nplv));
                    break;
                }
            }
            return (true);
        } else {
            return (false);
        }
    }

    public static class op {

        public String opt;
        public static ObservableList<op> opdata = FXCollections.observableArrayList();

        public String getOpt() {
            return (opt);
        }

    }

    public static class client {

        public String useid, usename, email, currst, mobno;
        public Integer privlev;
        public Button editup = new Button("Update");
        public Button udel = new Button("Delete");
        public static ObservableList<client> clist = FXCollections.observableArrayList();

        public String getUseid() {
            return (useid);
        }

        public String getUsename() {
            return (usename);
        }

        public String getEmail() {
            return (email);
        }

        public String getCurrst() {
            return (currst);
        }

        public String getMobno() {
            return (mobno);
        }

        public Integer getPrivlev() {
            return (privlev);
        }

        public Button getEditup() {
            return (editup);
        }

        public Button getUdel() {
            return (udel);
        }

        client(String usid, String usname, String emil, String crrst, String mbno, Integer prvlev) {
            this.useid = usid;
            this.usename = usname;
            this.email = emil;
            this.currst = crrst;
            this.mobno = mbno;
            this.privlev = prvlev;
            editup.setOnAction((ActionEvent event) -> {
                shwupalrt(this.useid, this.usename, this.email, this.currst, this.mobno, this.privlev);
            });
            udel.setOnAction((ActionEvent event) -> {
                databse.deluser(this.useid);
            });
        }

        public static void login(String[] split, Socket sock, DataOutputStream dout) {
            String user = split[1];
            String pass = split[2];
            boolean log = databse.chcklogindata(user, pass);
            try {
                if (log) {

                    int j;
                    for (j = 0; j < Server.alluser.size(); j++) {
                        if (user.equalsIgnoreCase(Server.alluser.get(j))) {
                            break;
                        }
                    }
                    int p = Server.privil.get(j);
                    String msg = "";
                    if (p == 1) {
                        msg = "login:" + Server.privil.get(j) + ":" + Server.allbnd.get(j) + ":" + pl1;
                    }
                    if (p == 2) {
                        msg = "login:" + Server.privil.get(j) + ":" + Server.allbnd.get(j) + ":" + pl2;
                    }
                    if (p == 3) {
                        msg = "login:" + Server.privil.get(j) + ":" + Server.allbnd.get(j) + ":" + pl3;
                    }
                    if (p == 4) {
                        msg = "login:" + Server.privil.get(j) + ":" + Server.allbnd.get(j) + ":" + pl4;
                    }

                    String websites = new String("\n");
                    for (int i = 0; i < accesstab.acstb.size(); i++) {
                        if (accesstab.acstb.get(i).unam.equalsIgnoreCase(user)) {
                            websites += accesstab.acstb.get(i).bw;
                        }
                    }
                    msg += ":" + websites;
                    boolean unb = true;
                    for (int i = 0; i < blocku.size(); i++) {
                        if (blocku.get(i).equalsIgnoreCase(user)) {
                            unb = false;
                            break;
                        }
                    }
                    if (unb) {
                        msg += ":UNBLOCKED";
                    } else {
                        msg += ":BLOCKED";
                    }

                    dout.writeUTF(msg);
                    String ip = sock.getRemoteSocketAddress().toString();
                    ip = ip.substring(1);
                    ip = ip.split(":")[0];
                    actu a = new actu(user, ip);
                    actu.data.add(a);
                    actuser.add(user);
                    databse.aftrlogin(user, ip, Server.privil.get(j));
                    for (int i = 0; i < client.clist.size(); i++) {
                        if (user.equalsIgnoreCase(client.clist.get(i).useid)) {
                            client c = client.clist.get(i);
                            c.currst = "active";
                            client.clist.set(i, c);
                        }
                    }
                    addtolog("User " + user + " logged in");
                    // FXMLDocumentController.homelog.appendText(" -> User :  "+user+" logged in ");
                } else {
                    dout.writeUTF("false");
                }

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public static void logout(String[] msgg, DataOutputStream dout, Socket sock) {
            try {
                String user = msgg[1];
                String bnd = msgg[2];
                String ses = msgg[3];
                dout.writeUTF("logout");
                String ip = sock.getRemoteSocketAddress().toString();
                ip = ip.substring(1);
                ip = ip.split(":")[0];
                int pr = 0;
                //System.out.println("logged out"+ip);
                for (int i = 0; i < Server.alluser.size(); i++) {
                    if (Server.alluser.get(i).equalsIgnoreCase(user)) {
                        pr = Server.privil.get(i);
                        break;
                    }
                }
                databse.aftrlogout(user, ip, pr);
                actu a = new actu(user, ip);
                a.bndd = Double.parseDouble(bnd);
                a.ses = ses;
                System.out.println(ses);
                a.setdatabse();

                Platform.runLater(() -> {
                    int i = 0;
                    for (int f = 0; f < actuser.size(); f++) {
                        actu b = actu.data.get(f);
                        if (b.user.equalsIgnoreCase(a.user)) {
                            i = f;
                            break;
                        }

                    }
                    actu.data.remove(i);
                    actuser.remove(i);
                    for (int h = 0; h < client.clist.size(); h++) {
                        if (user.equalsIgnoreCase(client.clist.get(h).useid)) {
                            client c = client.clist.get(h);
                            c.currst = "inactive";
                            client.clist.set(h, c);
                        }
                    }
                    addtolog("User " + user + " logged out");
                });
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public static void register(String[] split, DataOutputStream dout, Socket sock) {
            String fname = split[1];
            String uid = split[2];
            String pass = split[3];
            String email = split[4];
            String mobno = split[5];
            // System.out.println(user+"::"+pass);
            try {
                boolean log = databse.wrtregdata(fname, uid, pass, email, mobno);
                if (log) {
                    dout.writeUTF("reg");
                    addtolog("New User " + uid + " has requested for registration");
                } else {
                    dout.writeUTF("false");
                }

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void shwupalrt(String usid, String usname, String emil, String crrst, String mbno, Integer prvlev) {
        try {
            ui = usid;
            un = usname;
            ue = emil;
            um = mbno;
            up = prvlev + "";
            Stage st = new Stage();
            Parent rot = FXMLLoader.load(Server.class.getResource("userdetail.fxml"));
            Scene scene = new Scene(rot);
            st.setScene(scene);
            st.show();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class servoblist {

        public String tableport;
        public String tablename;
        public static ObservableList<servoblist> servdata = FXCollections.observableArrayList();

        public String getTablename() {

            return (tablename);
        }

        public String getTableport() {
            return (tableport);
        }

    }

    static void senduinf() {
        try {
            ServerSocket soc = new ServerSocket(5220);
            //System.out.println("socket ready");
            DataInputStream din;
            DataOutputStream dout;
            while (true) {
                Socket sock = soc.accept();
                //System.out.println("::");
                din = new DataInputStream(sock.getInputStream());
                dout = new DataOutputStream(sock.getOutputStream());
                String regdata = din.readUTF();
                if (regdata.equalsIgnoreCase("senduserandip")) {
                    String data = "";
                    for (int i = 0; i < actu.data.size(); i++) {
                        data += actu.data.get(i).user + ";";
                    }
                    data += "Admin:";
                    for (int i = 0; i < actu.data.size(); i++) {
                        data += actu.data.get(i).useIP + ";";
                    }
                    String ip = sock.getLocalAddress().toString();
                    ip = ip.substring(1);
                    ip = ip.split(":")[0];
                    data += ip;
                    dout.writeBytes(data);
                } else {
                    //chech unlockcode
                    String[] ms = regdata.split(":");
                    if (ms[0].equalsIgnoreCase("unlockcode")) {
                        String[] un = new String[2];
                        boolean d = true;
                        for (int i = 0; i < unlcd.size(); i++) {
                            un = unlcd.get(i).split(":");
                            if (un[0].equals(ms[1]) && un[1].equals(ms[2])) {
                                dout.writeBytes("reset password");
                                d = false;
                                break;
                            }
                        }
                        if (d) {
                            dout.writeBytes("error");
                        }
                    } else {
                        databse.uppass(ms[1], ms[2]);
                        dout.writeBytes("password reset");
                    }
                }
                sock.close();
            }
        } catch (IOException ex) {
            //Logger.getLogger(loginregister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class privoblist {

        public String privtable;

        public static ObservableList<privoblist> privillage1 = FXCollections.observableArrayList();
        public static ObservableList<privoblist> privillage2 = FXCollections.observableArrayList();
        public static ObservableList<privoblist> privillage3 = FXCollections.observableArrayList();
        public static ObservableList<privoblist> privillage4 = FXCollections.observableArrayList();

        public String getPrivtable() {
            return (privtable);
        }

    }

    public static class etab {

        public String enam, email, eal;
        public static ObservableList<etab> etabl = FXCollections.observableArrayList();

        public String getEnam() {
            return (enam);
        }

        public String getEmail() {
            return (email);
        }

        public String getEal() {
            return (eal);
        }
    }

    public static class accesstab {

        public String unam, lck, bw;
        public static ObservableList<accesstab> acstb = FXCollections.observableArrayList();

        public String getUnam() {
            return (unam);
        }

        public String getLck() {
            return (lck);
        }

        public String getBw() {
            return (bw);
        }
    }

    public static class Logs {

        public String log;

        public String getLog() {
            return (log);
        }
    }

    public static void addtolog(String l) {

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        l += " at " + hours + " : " + minutes + " : " + seconds;
        Logs lg = new Logs();
        lg.log = l;
        log.add(lg);
        if (l.startsWith("Sending")) {
            String lst = "Last Sent mail was at " + hours + " : " + minutes + " : " + seconds;

            for (int i = 0; i < etab.etabl.size(); i++) {
                etab e = new etab();
                e.enam = etab.etabl.get(i).enam;
                e.email = etab.etabl.get(i).email;
                e.eal = lst;
                etab.etabl.set(i, e);
            }
        }
    }

    public static class flt {
        public String[] srcip, dstip, srcp, dstp, prtcl;
    }
}
