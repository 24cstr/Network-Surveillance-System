package server8;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class chat {
    
static ServerSocket msgsoc;    
public static void sendmsg(String[] args) throws Exception
  {
     
     Socket sock = new Socket(args[0], 5216);       
     OutputStream ostream = sock.getOutputStream();                 
     DataOutputStream dos = new DataOutputStream(ostream);
     dos.writeBytes("Admin : "+args[1]);                                                         
     dos.close();                            
     ostream.close();   
     sock.close();        
    }

public static String recievemsg() throws Exception
   {  
     msgsoc=new ServerSocket(5216);  
     Socket sock = msgsoc.accept();  
     InputStream istream = sock.getInputStream();  
     BufferedReader br=new BufferedReader(new InputStreamReader(istream));
     String message2="";
     while(br.ready())
     message2+= br.readLine()+"\n";
     br.close(); 
     istream.close(); 
     sock.close(); 
     msgsoc.close();
     return(message2+"\n");
  }       
}