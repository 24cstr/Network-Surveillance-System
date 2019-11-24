/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


    public class loginregister {
    public static ObservableList<String> userlist;
    public static ObservableList<String> iplist;
    public static Socket connect(int port) throws IOException{
       
        String IPadd="127.0.0.1" ;
        Socket soc = null;
       
        soc=new Socket(IPadd,port);
        return(soc);
}
    
    public static boolean login(String uid,String pass) {
        try {
            Socket ClientSoc;
            DataOutputStream dout = null;
            DataInputStream din = null;
            
            ClientSoc=connect(5217);
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            
            
            dout.writeUTF("clogin"+";"+uid+";"+pass);
            String msgFromServer=din.readUTF();
            String msg[]=msgFromServer.split(":");
            if(msg[0].equalsIgnoreCase("login"))
            {
                Client.user.priv=Integer.parseInt(msg[1]);
                Client.user.bandusedn=Double.parseDouble(msg[2]);
                Client.user.tband=Double.parseDouble(msg[3]);
                Client.user.blweb=msg[4];
                Client.user.status=msg[5];
                final Thread Pcapture = new Thread(new Runnable() {public void run(){
                    bandusagecal.pcaper();
                }});
                Pcapture.start();
                return(true);
            }
            else{
                return(false);
            }
        } catch (IOException ex) {
            DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
        }
        return(false);
    }
    
    public static boolean register(String fname,String uid,String pass,String email,String mobno) throws IOException {
        
        Socket ClientSoc;
        DataOutputStream dout = null;
        DataInputStream din = null;
    try
        {
            ClientSoc=connect(5217);
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            
        }
        catch(Exception ex)
        {
            DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
            return(false);
        }
           dout.writeUTF("creg"+";"+fname+";"+uid+";"+pass+";"+email+";"+mobno);
           String msgFromServer=din.readUTF();
           if(msgFromServer.equalsIgnoreCase("reg"))
           {
           return(true);
           }
           else{
           return(false);
           }
    }

    public static boolean logout() throws IOException{
        Socket ClientSoc;
        DataOutputStream dout = null;
        DataInputStream din = null;
    try
        {
            ClientSoc=connect(5217);
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            
        }
        catch(Exception ex)
        {
            DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
            return(false);
        }
           
           dout.writeUTF("clogout"+";"+Client.user.userID+";"+Client.user.bandusedn+";"+Client.user.sestime);
           String msgFromServer=din.readUTF();
           if(msgFromServer.equalsIgnoreCase("logout"))
           {
               Client.user.userID="NIL";
               return(true);
           }
           else{
           return(false);
           }
     }
    
    public static boolean uncode(String u,String c){
    try
    {
        Socket ClientSoc;
        DataOutputStream dout = null;
        DataInputStream din = null;
        
        ClientSoc=connect(5220);
        din=new DataInputStream(ClientSoc.getInputStream());
        dout=new DataOutputStream(ClientSoc.getOutputStream());
        dout.writeUTF("unlockcode:"+u+":"+c);
        String msgFromServer=din.readLine();
        if(!msgFromServer.equalsIgnoreCase("error"))
        {
            return(true);
        }
        else
            return(false);
        
    }
        catch(IOException ex)
        {
            DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
            
        }
           return(false);
     }
     
    public static boolean sndpass(String u,String p){
    try
    {
        Socket ClientSoc;
        DataOutputStream dout = null;
        DataInputStream din = null;
        try
        {
            ClientSoc=connect(5220);
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        dout.writeUTF("PasswordSet:"+u+":"+p);
        String msgFromServer=din.readLine();
        if(!msgFromServer.equalsIgnoreCase("error"))
        {
            return(true);
        }
        else
            return(false);
        
    }
        catch(IOException ex)
        {
            DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
            return(false);
        }
     } 
    
    public static boolean ftuser() throws IOException{
        Socket ClientSoc;
        DataOutputStream dout = null;
        DataInputStream din = null;
    try
        {
            ClientSoc=connect(5220);
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            
        }
        catch(Exception ex)
        {
            DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
            return(false);
        }
           dout.writeUTF("senduserandip");
           String msgFromServer=din.readLine();
           if(!msgFromServer.equalsIgnoreCase("error"))
           {
               String msg[]=msgFromServer.split(":");
               String ulist[]=msg[0].split(";");
               String iplist1[]=msg[1].split(";");
               userlist = FXCollections.observableArrayList(ulist);
               iplist=FXCollections.observableArrayList(iplist1);
           }
           
           return(false);
           
     }
    
    public static void sendinfo(){
        try {
            ServerSocket soc=new ServerSocket(5221);
            //System.out.println("socket ready");
            DataInputStream din;
            DataOutputStream dout;
            while(true)
            {
                Socket sock=soc.accept();
                //System.out.println("::");
                din=new DataInputStream(sock.getInputStream());
                dout=new DataOutputStream(sock.getOutputStream());
                String regdata=din.readUTF();
                if(regdata.equalsIgnoreCase("sendurinfo")){
                dout.writeUTF(Client.user.userID+":"+Math.round(Client.user.bandusedn));
                }
                sock.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(loginregister.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    public static boolean req(){
    
        try
    {
            Socket ClientSoc;
            DataOutputStream dout = null;
            DataInputStream din = null;

            ClientSoc = connect(5218);
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
 
                dout.writeUTF("unblock:"+Client.user.userID);
                String msgFromServer = din.readUTF();
               
                if (msgFromServer.equalsIgnoreCase("error")) {
                    DashboardController.showwarning("Sorry your request was rejected by the Admin");
                    return(false);
                } 
                else if(msgFromServer.equalsIgnoreCase("accepted")){
                    
                    DashboardController.showinfo("Your account has been unblocked");
                    return(true);
                }

        } catch (IOException ex) {
           DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
            return(false);
        }
        return(false);
    }
    
    public static boolean req(int lvl,String msg){
        
        try
    {
            Socket ClientSoc;
            DataOutputStream dout = null;
            DataInputStream din = null;

            ClientSoc = connect(5218);
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
 
                dout.writeUTF("privchg:"+Client.user.userID+":"+Client.user.priv+":"+lvl+":"+msg);
                String msgFromServer = din.readUTF();
                if (msgFromServer.equalsIgnoreCase("error")) {
                    DashboardController.showwarning("Sorry your request was rejected by the Admin");
                    return(false);
                } 
                else if(msgFromServer.equalsIgnoreCase("accepted")){
                    DashboardController.showinfo("Your Privillage Level has been changed");
                    Client.user.priv=lvl;
                    return(true);
                }

        } catch (IOException ex) {
            DashboardController.showwarning("SORRY!!!!  THE SERVER IS NOT AVAILABLE NOW.......PLEASE TRY AGAIN LATER...");
            return(false);
        }
        return(false);
    }

}
