package server8;

import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server8.Server.servoblist;

public class databse {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/abnss";
    static final String USER = "root";
    static final String PASS = "gunjan";
    static int prvl;
    static String fname=null,uid,pass,email;
    static Long mobno;
    
    
public static void getalluser(){
    Connection conn = null;
    Statement stmt = null; 
   try{

      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT uid,privillage FROM ulogin";
      ResultSet rs = stmt.executeQuery(sql);
      int i=0;
      while(rs.next()){
         //Retrieve by column name
         if(!rs.getString("uid").equalsIgnoreCase("Admin")){
         Server.alluser.add(i,rs.getString("uid"));
         Server.privil.add(i,rs.getInt("privillage"));
         System.out.println(rs.getString("uid"));
         PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM bandused WHERE uid = ?");
         stmt1.setString(1, Server.alluser.get(i));
         ResultSet r=stmt1.executeQuery();
         if (!r.isBeforeFirst() ) {
         Server.allbnd.add(i, 0.0);
         Server.alltym.add(i,"0:0:0");
         }
         else
         { 
          r.next();    
         Server.allbnd.add(i, r.getDouble("bandusage"));
         Server.alltym.add(i, r.getString("totalsession"));
         }
         
         r.close();
         stmt1.close();
         stmt1 = conn.prepareStatement("SELECT * FROM register WHERE uid = ?");
         stmt1.setString(1, Server.alluser.get(i));
         r=stmt1.executeQuery();
         r.next();
         //get from register
         Server.allemail.add(i,r.getString("emailid"));
         Server.allname.add(i,r.getString("name"));
         Server.allmobno.add(i,r.getString("mobno"));    
         i++;
     }
   }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
     }catch(Exception e){
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
          se2.printStackTrace();
      }
    
   }
}

static boolean chcklogindata(String user, String pass){
    Connection conn = null;
    Statement stmt = null;
    boolean loginb=false;
   try{

      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT * FROM ulogin";
      ResultSet rs = stmt.executeQuery(sql);

      while(rs.next()){
         //Retrieve by column name
         String first = rs.getString("uid");
         String last = rs.getString("pass");
         if(user.equals(first)&&pass.equals(last))
         {
             loginb=true;
             prvl= rs.getInt("privillage");
             break;
            
         }
             
      }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
   return(loginb);
    }

static boolean wrtregdata(String fname,String uid, String pass,String email,String mobno){
    Connection conn = null;
    PreparedStatement stmt = null;
    String vali="no";
    boolean regd=false;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      Long mo=Long.parseLong(mobno);
      stmt = conn.prepareStatement("INSERT INTO register VALUES (?, ?, ?, ?, ?, ?)");
      stmt.setString(1, fname);
      stmt.setString(2, uid);
      stmt.setString(3, pass);
      stmt.setString(4, email);
      stmt.setLong(5, mo);
      stmt.setString(6, vali);
      stmt.executeUpdate();
      regd=true;
      //STEP 6: Clean-up environment
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
   return(regd);

}

static boolean accpt(){
    Connection conn = null;
    Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");
      //STEP 3: Open a connection
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      //STEP 4: Execute a query
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT * FROM register WHERE verified = 'no'";
      ResultSet rs = stmt.executeQuery(sql);
      
      if(rs.next()){
      do{
         //Retrieve by column name
         fname = rs.getString("name");
         uid = rs.getString("uid"); 
         pass = rs.getString("passwd");
         email=rs.getString("emailid");
         mobno=rs.getLong("mobno");
         String verified=rs.getString("verified");
         
         if(verified.equalsIgnoreCase("no"))
         {
            //server8.FXMLDocumentController.validate(fname,uid,pass,email,mobno);
         } 
         break;
      }while(rs.next());}else{
      fname=null;
      uid=null;
      pass=null;
      email=null;
      mobno=null;
      
      }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
   return(true);
    
}

public static boolean wrtdatalogin(String uid, String pass,String priv,String op){
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean regd=false;
   try{
     
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
     if(op.equalsIgnoreCase("wr"))
      {
      Integer pri=Integer.parseInt(priv);
      Server.addtolog("User "+uid+"'s Registartion request accepted");
      stmt = conn.prepareStatement("INSERT INTO ulogin VALUES (?, ?, ?)");    
      stmt.setString(1, uid);
      stmt.setString(2, pass);
      stmt.setInt(3, pri);
      stmt.executeUpdate();     
      stmt = conn.prepareStatement("UPDATE register SET verified=? WHERE uid = ?");
      stmt.setString(1, "YES");
      stmt.setString(2, uid);
      stmt.executeUpdate();
      stmt = conn.prepareStatement("INSERT INTO bandused VALUES (?,?,?)");    
      stmt.setString(1, uid);
      stmt.setDouble(2, 0);
      stmt.setString(3, "0:0:0");
      stmt.executeUpdate();
      stmt = conn.prepareStatement("INSERT INTO accesscontrol VALUES (?,?,?,?)");    
      stmt.setString(1, uid);
      stmt.setString(2, "");
      stmt.setString(3, "");
      stmt.setString(4, "unblocked");
      stmt.executeUpdate();
          
      regd=true;
      }
     else if(op.equalsIgnoreCase("del"))
      {
         stmt = conn.prepareStatement("DELETE FROM register WHERE uid = ?");
         stmt.setString(1, uid);
         stmt.executeUpdate();
      }
      //STEP 6: Clean-up environment
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
   return(regd);
   
}

public static class actu{

String user;
String useIP;
Double bndd;
String ses;
String ttym;
static public final ObservableList<actu> data =FXCollections.observableArrayList();

actu(String u,String ip){
this.user=u;
this.useIP=ip;
}

 public String getUser(){return(user);}
 public String getUseIP(){return(useIP);}
 
 void setdatabse(){
    
     for(int i=0;i<Server.alluser.size();i++){
         if(this.user.equalsIgnoreCase(Server.alluser.get(i))){
         this.ttym=Server.alltym.get(i);
         break;
         }
     }
         int s=Integer.parseInt(this.ttym.split(":")[2]);
         int m=Integer.parseInt(this.ttym.split(":")[1]);
         int h=Integer.parseInt(this.ttym.split(":")[0]);
         int s1=Integer.parseInt(this.ses.split(":")[2]);
         int m1=Integer.parseInt(this.ses.split(":")[1]);
         int h1=Integer.parseInt(this.ses.split(":")[0]);
         s+=s1;
         if(s>60){
         s-=60;
         m+=1;
         }
         m+=m1;
         if(m>60){
         m-=60;
         h+=1;
         }
         h+=h1;
         this.ttym=h+":"+m+":"+s;
         System.out.println(ttym);
    Connection conn = null;
    PreparedStatement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);    
      stmt = conn.prepareStatement("UPDATE bandused SET bandusage=?,totalsession=? WHERE uid = ?");
      stmt.setDouble(1, this.bndd);
      stmt.setString(2, this.ttym);
      stmt.setString(3, this.user);     
      stmt.executeUpdate();
      stmt.close();
      conn.close();
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
   }
 }
}

static void exittasks(){
    Connection conn = null;
    PreparedStatement stmt = null;
    boolean regd=false;
    int i=0;
   try{
     
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      
      //For storing all the selected services 
      stmt = conn.prepareStatement("DELETE FROM blkservice");    
      stmt.execute();
      
      for(i=0;i<servoblist.servdata.size();i++){
      stmt = conn.prepareStatement("INSERT INTO blkservice VALUES (?, ?)");    
      stmt.setString(1, servoblist.servdata.get(i).tablename);
      stmt.setInt(2, Integer.parseInt(servoblist.servdata.get(i).tableport));
      stmt.executeUpdate();     
      }
      
      //Clean-up environment
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
          
      }
      
   }

}

static void startuptask(){
    Connection conn = null;
    Statement stmt = null;
    boolean loginb=false;
    try{

      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT * FROM blkservice";
      ResultSet rs = stmt.executeQuery(sql);
      
      while(rs.next()){
         servoblist e=new servoblist();
         e.tablename = rs.getString("sername");
         e.tableport=""+rs.getInt("portno");
         servoblist.servdata.add(e); 
      }
      sql = "SELECT * FROM plevel WHERE level=1";
      rs = stmt.executeQuery(sql);
      while(rs.next()){
         Server.pl1=rs.getDouble("bandalloc");
         if(!(rs.getString("weblist")==null)){
             Server.privoblist n=new Server.privoblist();
             n.privtable=rs.getString("weblist");
                Server.privoblist.privillage1.add(n);
         }
                 break;
      }
      sql = "SELECT * FROM plevel WHERE level=4";
      rs = stmt.executeQuery(sql);
      while(rs.next()){
         Server.pl4=rs.getDouble("bandalloc");
         if(!(rs.getString("weblist")==null)){
             Server.privoblist n=new Server.privoblist();
             n.privtable=rs.getString("weblist");
                Server.privoblist.privillage4.add(n);
         }
         break;
      }
      sql = "SELECT * FROM plevel WHERE level=2";
      rs = stmt.executeQuery(sql);
      while(rs.next()){
         Server.pl2=rs.getDouble("bandalloc");
         if(!(rs.getString("weblist")==null)){
             Server.privoblist n=new Server.privoblist();
             n.privtable=rs.getString("weblist");
                Server.privoblist.privillage2.add(n);
         }
         break;
      }
      sql = "SELECT * FROM plevel WHERE level=3";
      rs = stmt.executeQuery(sql);
      while(rs.next()){
         Server.pl3=rs.getDouble("bandalloc");
         if(!(rs.getString("weblist")==null)){
             Server.privoblist n=new Server.privoblist();
             n.privtable=rs.getString("weblist");
                Server.privoblist.privillage3.add(n);
         }
         break;
      }
      
      sql = "SELECT * FROM notification";
      rs = stmt.executeQuery(sql);
      while(rs.next()){
         Server.etab m=new Server.etab();
         m.enam=rs.getString("name");
         m.eal=rs.getString("data_mail");
         m.email=rs.getString("email");
         Server.etab.etabl.add(m);
         Server.emm.add(m.enam);
      }
      
      sql = "SELECT * FROM accesscontrol";
      rs = stmt.executeQuery(sql);
      boolean ad=true;
      ArrayList<String> user=new ArrayList<>();
      ArrayList<String> lc=new ArrayList<>();
      ArrayList<String> webs=new ArrayList<>();
      while(rs.next()){
         ad=true;
         
         for(int i=0;i<user.size();i++){
         if(user.get(i).equalsIgnoreCase(rs.getString("uid"))){
           String w=webs.get(i);
           w+=rs.getString("webname")+"\n";
           webs.set(i, w);
           ad=false;
           break;
         }
         }
         if(ad){
             user.add(user.size(), rs.getString("uid"));
             lc.add(lc.size(), rs.getString("status"));
             webs.add(webs.size(), rs.getString("webname")+"\n");
         }
         
      }
      for(int i=0;i<user.size();i++){
      Server.accesstab m=new Server.accesstab();
         m.bw=webs.get(i);
         m.lck=lc.get(i);
         m.unam=user.get(i);
         Server.accesstab.acstb.add(m);
         Server.accus.add(m.unam);
          if (!"ALL".equalsIgnoreCase(m.unam)) {
              if (m.lck.equalsIgnoreCase("unblocked")) {
                  Server.unblocku.add(m.unam);
              } else {
                  Server.blocku.add(m.unam);
              }
          }
      }
      rs.close();
      stmt.close();
      conn.close();
      
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
}

static void wbsblck(String web,String user,String typ){
    Connection conn = null;
    PreparedStatement stmt = null;

   try{
     
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      stmt = conn.prepareStatement("INSERT INTO accesscontrol VALUES(?,?,?,?)");    
      stmt.setString(1, user);
      stmt.setString(2, web);
      stmt.setString(3,typ);
      stmt.setString(4, "unblocked");
      stmt.executeUpdate();     
      Server.addtolog("User "+user+"'s access to Website "+web+" has been blocked");
      //Clean-up environment
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
          
      }
      
   }
}

static void aftrlogin(String user,String ip, int pr){
    
    Connection conn = null;
    PreparedStatement stmt = null;
   try{

      Class.forName("com.mysql.jdbc.Driver");
      
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      
      stmt = conn.prepareStatement("SELECT * FROM accesscontrol where uid = ? ");
      stmt.setString(1, user);
      
      ResultSet rs = stmt.executeQuery();
      String website="";
      
      while(rs.next()){
          
         //Retrieve by column name
         String web = rs.getString("webname");
         String cat = rs.getString("categeory");
         String stat = rs.getString("status");
         if(stat.equalsIgnoreCase("blocked"))
         {
            //block user command
            Accesscontrol.userblock(ip);
         }
         else 
         {    
             //block website of the user
             website+=(web+"\n");
             //System.out.println(website);
            
             Accesscontrol.websiteblock(web, ip);
         }
             
      }
      
      stmt = conn.prepareStatement("SELECT * FROM plevel where level = ? ");
      stmt.setInt(1, pr);
      rs = stmt.executeQuery();
      while(rs.next()){
         //Retrieve by column name
         String web = rs.getString("weblist");
         if(web!=null)
         {
             //block website of the user
             
             Accesscontrol.websiteblock(web, ip);
         }            
      }
      //System.out.println(website);
      rs.close();
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
  
    }

static void aftrlogout(String user,String ip,int pr){
    
    Connection conn = null;
    PreparedStatement stmt = null;
   try{

      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.prepareStatement("SELECT * FROM accesscontrol where uid = ? ");
      stmt.setString(1, user);
      ResultSet rs = stmt.executeQuery();
      while(rs.next()){
         //Retrieve by column name
         String web = rs.getString("webname");
         String cat = rs.getString("categeory");
         String stat = rs.getString("status");
         if(stat.equalsIgnoreCase("blocked"))
         {
            //block user command
            Accesscontrol.userunblock(ip);
         }
         else 
         {    
             //block website of the user
            
             Accesscontrol.websiteunblock(web, ip);
         }
             
      }
            stmt = conn.prepareStatement("SELECT * FROM plevel where level = ? ");
      stmt.setInt(1, pr);
      rs = stmt.executeQuery();
      while(rs.next()){
         //Retrieve by column name
         String web = rs.getString("weblist");
         if(web!=null)
         {
             //block website of the user
             Accesscontrol.websiteunblock(web, ip);
         }            
      }
      rs.close();
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
  
    }

static void updatebusage(int p,Double mb){
 
         Connection conn = null;
         PreparedStatement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);    
      stmt = conn.prepareStatement("UPDATE plevel SET bandalloc=? WHERE level = ?");
      stmt.setDouble(1,mb);
      stmt.setInt(2,p);     
      stmt.executeUpdate();
      stmt.close();
      conn.close();
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
   }
}

static void updatebusage(int p,double mb,String web){
    Connection conn = null;
    PreparedStatement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.prepareStatement("INSERT INTO plevel VALUES (?, ?, ?)");
      stmt.setInt(1, p);
      stmt.setDouble(2, mb);
      stmt.setString(3, web);
      stmt.executeUpdate();
      if(p==1){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage1.add(n);
         }
      else if(p==2){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage2.add(n);
         }
      else if(p==3){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage3.add(n);
         }
      else if(p==4){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage4.add(n);
         }
      
      //STEP 6: Clean-up environment
      stmt.close();
      conn.close();
      updatebusage(p,mb);
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
 }

static void removeprivweb(int p,String web,int in){
    Connection conn = null;
    PreparedStatement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.prepareStatement("delete from plevel where level=? AND weblist=?");
      stmt.setInt(1, p);
      stmt.setString(2, web);
      stmt.executeUpdate();
      if(p==1){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage1.remove(in);
         }
      else if(p==2){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage2.remove(in);
         }
      else if(p==3){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage3.remove(in);
         }
      else if(p==4){
             Server.privoblist n=new Server.privoblist();
             n.privtable=web;
             Server.privoblist.privillage4.remove(in);
         }
      
      //STEP 6: Clean-up environment
      stmt.close();
      conn.close();
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
    
   }
 }

public static void user_update(String uid,String uname,String email,String mobno,int pr){
    Connection conn = null;
         PreparedStatement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);    
      stmt = conn.prepareStatement("UPDATE register SET name=?,emailid=?,mobno=? WHERE uid = ?");
      stmt.setString(1,uname);
      stmt.setString(2,email);
      stmt.setString(3,mobno);
      stmt.setString(4,uid);
      stmt.executeUpdate();
      Server.addtolog("User "+uname+"'s Registration Details were updated");
      stmt = conn.prepareStatement("UPDATE ulogin SET privillage=? WHERE uid = ?");
      stmt.setInt(1,pr);
      stmt.setString(2,uid);
      stmt.executeUpdate();
      for(int h=0;h<Server.client.clist.size();h++){
          if(uid.equalsIgnoreCase(Server.client.clist.get(h).useid)){
          Server.client c=Server.client.clist.get(h);
          c.usename=uname;
          c.email=email;
          c.mobno=mobno;
          c.privlev=pr;
          Server.client.clist.set(h, c);
          }
          }
      stmt.close();
      conn.close();
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
   }
    
}

public static void deluser(String uid){
    boolean t=false;
    
    t=FXMLDocumentController.getconform("Do You Really Want to Delete : "+uid+"\n\t Once deleted user can't be restored");
    
    if(t){
    Connection conn = null;
    PreparedStatement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);  
      
      stmt = conn.prepareStatement("DELETE from register WHERE uid = ?");
      stmt.setString(1,uid);
      stmt.executeUpdate();
      
      stmt = conn.prepareStatement("DELETE from ulogin WHERE uid = ?");
      stmt.setString(1,uid);
      stmt.executeUpdate();
      
      stmt = conn.prepareStatement("DELETE from accesscontrol WHERE uid = ?");
      stmt.setString(1,uid);
      stmt.executeUpdate();
      
      stmt = conn.prepareStatement("DELETE from bandused WHERE uid = ?");
      stmt.setString(1,uid);
      stmt.executeUpdate();
      
      for(int h=0;h<Server.client.clist.size();h++){
          if(uid.equalsIgnoreCase(Server.client.clist.get(h).useid)){
          Server.client.clist.remove(h);
          }
      }
      Server.addtolog("User "+uid+"'s account was been deleted");
      stmt.close();
      conn.close();
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
   }
}
}

public static void addememtd(Server.etab m){
    Connection conn = null;
    PreparedStatement stmt = null;

   try{
     
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.prepareStatement("INSERT INTO notification VALUES(?,?,?)");    
      stmt.setString(1, m.eal);
      stmt.setString(2, m.enam);
      stmt.setString(3, m.email);
      stmt.executeUpdate(); 
      Server.addtolog("New Member "+m.enam+" has been added to email list");
      //Clean-up environment
      stmt.close();
      conn.close();  
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
          
      }
      
   }
}

public static void rmememtd(String m){
Connection conn = null;
    PreparedStatement stmt = null;

   try{
     
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.prepareStatement("delete from notification where name = ?");    
      stmt.setString(1, m);
      stmt.executeUpdate();   
      Server.addtolog("Member "+m+" was removed from email list");
      //Clean-up environment
      stmt.close();
      conn.close();  
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
          
      }
   }
}

public static void blck(String u,String w){
Connection conn = null;
         PreparedStatement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);    
      if(w.equalsIgnoreCase("blocked")){
      stmt = conn.prepareStatement("UPDATE accesscontrol SET status=? WHERE uid = ?");
      stmt.setString(1,"blocked");
      stmt.setString(2,u);
      }
      else{
      stmt = conn.prepareStatement("UPDATE accesscontrol SET status=? WHERE uid = ?");
      stmt.setString(1,"unblocked");
      stmt.setString(2,u);
      }
      
      stmt.executeUpdate();
      
      stmt.close();
      conn.close();
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
   }
}

public static void rmwbsblck(String web,String user){
    Connection conn = null;
    PreparedStatement stmt = null;

   try{
     
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.prepareStatement("DELETE from accesscontrol WHERE uid=? AND webname=?");    
      stmt.setString(1, user);
      stmt.setString(2, web);
      stmt.executeUpdate();     
      Server.addtolog("User "+user+"'s access to website "+web+"has been reinstigated");
      //Clean-up environment
      stmt.close();
      conn.close();
      
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
          
          
      }
      
   }
}

public static void uppass(String u,String psw){
    
Connection conn = null;
         PreparedStatement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);    
     
      stmt = conn.prepareStatement("UPDATE ulogin SET pass=? WHERE uid = ?");
      stmt.setString(1,psw);
      stmt.setString(2,u);
      stmt.executeUpdate();
      stmt.close();
      conn.close();
      Server.addtolog(u+"'s password was updated");
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
   }
}

public static void incrule(String r,String d){
Connection conn = null;
         PreparedStatement stmt = null;
   try{
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);    
     
      stmt = conn.prepareStatement("insert into iptables values(?,?)");
      stmt.setString(1,r);
      stmt.setString(2,d);
      stmt.executeUpdate();
      Server.addtolog("New Iptables Rule : "+r+" was added");
      stmt.close();
      conn.close();
     }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
         
      }catch(SQLException se2){
          
      }
   }
}
}