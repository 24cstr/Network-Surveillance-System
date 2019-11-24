package server8;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class Accesscontrol {   
/////---------to block the website
    public static void websiteblock (String ip1,String usip) {
   try {
       
       String[] command = { "sudo","iptables","-A" ,"FORWARD","-s",usip,"-d",ip1,"-j","DROP" };
       command_run(command);

   } catch (Exception ex) {
   ex.printStackTrace();
   }
   }
    /////--------------------
    
    /////-------to block a website for all users
    public static void websiteblock (String ip1) {
   try {
       
       String[] command = { "sudo","iptables","-A" ,"FORWARD","-d",ip1,"-j","DROP" };
       command_run(command);
       System.out.println(ip1);
       for(int i=0;i>command.length;i++){
       System.out.println(command[i]);}
   } catch (Exception ex) {
   ex.printStackTrace();
   }
   

   }
    /////////-------------------/////-------------------------
    /////////////////-------------to unblock the website
    
    public static void websiteunblock(String unblock ) throws IOException{
        String[] command = { "sudo","iptables","-D" ,"FORWARD","-d",unblock,"-j","DROP" };
       
        command_run(command);
    }
    //////////---------------------------
      public static void websiteunblock(String ip1,String usip) throws IOException{
        String[] command = { "sudo","iptables","-D" ,"FORWARD","-s",usip,"-d",ip1,"-j","DROP" };
        
        command_run(command);
    }
    ///////////////////////------------------To block user 
    public static void userblock(String usrblock ) throws IOException{
        
     String[] command = { "sudo","iptables","-A" ,"FORWARD","-s",usrblock,"-j","DROP" };
     command_run(command);
     
    }
/////////////////////---------------------------------    
    /*
    if(username=usr||password=psr){
    String[] command = { "sudo","iptables","-D" ,"FORWARD","-s",usrunblock,"-j","DROP" };
     command_run(command);   
    
    else{
    System.out.print("sorry wrong username or password");
    check the login data form database (if ok then unblock else print the message

}*/
    
    
    
    
    public static void userunblock(String usrunblock ) throws IOException{
        
     String[] command = { "sudo","iptables","-D" ,"FORWARD","-s",usrunblock,"-j","DROP" };
     command_run(command);
     
     
    }
    /* public static void macip(String ipb,String mac ) throws IOException{
        
      String[] command = {"iptables"," -A", "INPUT"," -s",ipb," -m ","mac", "!","--mac-source",mac," -j" ,"DROP"};
     command_run(command);
     
            }*/
    public static void checkweb(String ipaddr) throws UnknownHostException
    {
    InetAddress ip=InetAddress.getByName(ipaddr); 
    String hostaddr=ip.getHostAddress();
    //System.out.println(ipaddr+" : "+hostaddr);
    }
    
       public static String iptohost(byte[] addr) throws UnknownHostException
    {
    InetAddress ip=InetAddress.getByAddress(addr);
    String hostaddr=ip.getCanonicalHostName();
    //System.out.println(ipaddr+" : "+hostaddr);
    return(hostaddr);
    }
           
    public static String command_run(String[] args) throws IOException{
         Process p = Runtime.getRuntime().exec(args);
         BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
         //System.out.println(br.readLine());
         String line="",l;
         while((l=br.readLine())!=null)
             line+=l+"\n";
         
         return(line);
    }
    public static String command_run(String args) throws IOException{
        
         Process p = Runtime.getRuntime().exec(args);
         BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
         String line="",l;
         while((l=br.readLine())!=null)
            
             line+=l+"\n";
         return(line);
    }
    
}