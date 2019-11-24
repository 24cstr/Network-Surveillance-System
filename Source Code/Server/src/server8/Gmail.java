/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server8;

import java.util.ArrayList;
import java.util.Properties;  
import javax.mail.*;  
import javax.mail.internet.*;

import javax.mail.PasswordAuthentication;

  
public class Gmail {  

static ArrayList<String> to=new ArrayList<>();
public static void mail(String args) {  
  
  //Get the session object  
  Properties props = new Properties();  
  props.put("mail.smtp.host", "smtp.gmail.com");  
  props.put("mail.smtp.socketFactory.port", "465");  
  props.put("mail.smtp.socketFactory.class",  
            "javax.net.ssl.SSLSocketFactory");  
  props.put("mail.smtp.auth", "true");  
  props.put("mail.smtp.port", "465");  
   
  Session session = Session.getDefaultInstance(props,  
   new javax.mail.Authenticator() {  
   protected PasswordAuthentication getPasswordAuthentication() {  
   return new PasswordAuthentication("rpiproject0@gmail.com","rpiproject0@gtu.com");//change accordingly  
   }  
  });  
   for(int i=0;i<to.size();i++){
  //compose message  
  try {  
   MimeMessage message = new MimeMessage(session);  
   message.setFrom(new InternetAddress("rpiproject0@gmail.com"));//change accordingly  
   message.addRecipient(Message.RecipientType.TO,new InternetAddress(to.get(i)));  
   message.setSubject("Updates From Your Network");  
   message.setText("\n\nThe Recent updates from your network are : \n\n"+args);  
   
   //send message  
   Transport.send(message);  
   System.out.println("message sent successfully");  
   
  } catch (MessagingException e) {throw new RuntimeException(e);}  
   }
 }  
}  
