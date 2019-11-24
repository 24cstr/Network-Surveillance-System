package client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class communication {
    static ServerSocket FTPsoc,msgsoc;
    static void FTPserverstart()
    {
        try {
            while(true)
        {
            Socket ClientSoc=FTPsoc.accept();
            DataInputStream din=new DataInputStream(ClientSoc.getInputStream());
            DataOutputStream dout=new DataOutputStream(ClientSoc.getOutputStream());
            ReceiveFile(din,dout,ClientSoc);
            ClientSoc.close();
         }
        } catch (IOException ex) {
            Logger.getLogger(communication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void ReceiveFile(DataInputStream din,DataOutputStream dout,Socket soc) throws Exception
    {
        String filename=din.readUTF();
        if(filename.compareTo("File not found")==0)
        {
            return;
        }
        String fn="C:\\Users\\Acer\\Documents\\serverfiles\\";
        String[] part=filename.split("\\\\");
        fn=fn.concat(part[((part.length)-1)]);
        File f=new File(fn);
        String option;
        if(f.exists())
        {
            dout.writeUTF("File Already Exists");
            option=din.readUTF();
        }
        else
        {
            dout.writeUTF("SendFile");
            option="Y";
        }
            
            if(option.compareTo("Y")==0)
            {
                try (FileOutputStream fout = new FileOutputStream(f)) {
               String temp;
               temp=din.readUTF();
               long ch=Long.parseLong(temp),bufsize=0,fsize=0;
                    byte[] b;int s,bufferSize = soc.getReceiveBufferSize();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte buffer[] = new byte[bufferSize];
                    while(fsize<ch)
                    {   
                        bufsize=0;
                        while(bufsize<41943040&&(fsize+bufsize)<ch)//40MB per write
                        {
                          s=din.read(buffer);
                          baos.write(buffer, 0, s);
                          bufsize+=s;
                          DashboardController.showinfo("Recieved ="+(fsize/ch)*100+"%");
                        }
                    fsize+=bufsize;
                    b = baos.toByteArray();
                    fout.write(b);
                    baos.reset();
                   }
            }
                dout.writeUTF("File Upload Successfull");
                DashboardController.showinfo("\tFile Received Successfully.");
                TimeUnit.MILLISECONDS.sleep(700);
                
         }
            
              soc.close(); 
    }
    
    
    static void sendFile(String filename,String to){
        
        Socket soc = null;
        try{
        soc=new Socket(to,5215);
        DataInputStream din=new DataInputStream(soc.getInputStream());
        DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
              
        File f=new File(filename);
        if(!f.exists())
        {
            dout.writeUTF("File not found");
            return;
        }
        
        dout.writeUTF(filename);
        
        String msgFromServer=din.readUTF();
        if(msgFromServer.compareTo("File Already Exists")==0)
        {
            String Option;
            Option="y"; 
        
            if("y".equals(Option))    
            {
                dout.writeUTF("Y");
            }
            else
            {
                dout.writeUTF("N");
                return;
            }
        }
            try (FileInputStream fin = new FileInputStream(f)) {
        int bytesread=0;long bread=0,flen=f.length();
        byte fileContent[] = new byte[134169449];
        dout.writeUTF(String.valueOf(flen));
        DashboardController.showinfo("\tFile Sending started");
        do
        { 
            bytesread=fin.read(fileContent,0,fileContent.length);
            bread+=bytesread;
            dout.write(fileContent,0,bytesread);
            dout.flush();
             DashboardController.showinfo("Sent ="+(bread/flen)*100+"%");
        }while(flen>bread);

                System.out.println(din.readUTF());
                TimeUnit.MILLISECONDS.sleep(700);
                
            } 
        if(din.readUTF().equalsIgnoreCase("File Upload Successfull"))
        {
        DashboardController.showinfo("File Upload Successfull");
        }
        }
        catch(Exception ex)
        {
             DashboardController.showinfo("SORRY!!!!  THE USER IS OFFLINE.......PLEASE TRY AGAIN LATER...");
        }
    }

public static void sendmsg(String[] args) throws Exception
  {
     Socket sock = new Socket(args[0], 5216);       
     OutputStream ostream = sock.getOutputStream();                 
     DataOutputStream dos = new DataOutputStream(ostream);
     dos.writeBytes(args[1]);                                                         
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