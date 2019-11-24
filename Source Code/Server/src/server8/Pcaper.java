package server8;

import java.io.File;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import org.jnetpcap.JBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.voip.Sip;

public class Pcaper {
   
static PcapDumper dumper;
static Pcap pcap;
static Long totp,totsize;
static boolean sholddata=false,filtr=false;
static Server.flt fltr=new Server.flt();
static int in=0;
public static class pckt implements java.io.Serializable
     {
         String sourceIP,destIP;
         Date capTime;
     }    
public static class packt 
     {
         Long frameno;
         String sourceIP="",destIP="",protocol="",sourceMAC="",destMAC="";
         Integer sourcePort=0,destPort=0,packetSize=0;
         Date capTime=new Date(0,0,0);
         
         
         public String getSourceIP(){return(sourceIP);}
         public String getDestIP(){return(destIP);}
         public String getProtocol(){return(protocol);}
         public String getSourceMAC(){return(sourceMAC);}
         public String getDestMAC(){return(destMAC);}
         public Integer getSourcePort(){return(sourcePort);}
         public Integer getDestPort(){return(destPort);}
         public Integer getPacketSize(){return(packetSize);}
         public Date getCapTime(){return(capTime);}
         public Long getFrameno(){return(frameno);}
         
         @Override
         public String toString()
         {
           String s="Time : "+capTime+"     protocol : "+protocol+"     source MAC : "+sourceMAC+"     sourceIP : "+sourceIP+"     sourcePort : "+sourcePort+"     dest MAC : "+destMAC+"     destIP : "+destIP+"     destPort : "+destPort;
           return(s);
         }
         
     }    

static class PData{
        String pdetail;
         PData(String s)
          {
             this.pdetail=s;
          }
        }
        
        static ArrayList<PData> pd=new ArrayList<>();
        public static int pcount=0;
        public static double nobytes=0;
        static packt p=new packt();
        public static boolean livemonitoring=false;
        static public final ObservableList<packt> data =FXCollections.observableArrayList();
        public static ObservableList<String> webl=FXCollections.observableArrayList();
       
        public static void pcap() throws UnknownHostException
        
          {
            
            final Pcap pcap=startpcap();
            //readpacketfromfile();
            Thread cap=new Thread(new Runnable() {public void run(){
            capturepackets(pcap);}});
            cap.start();
        } 
        static Pcap startpcap()
          {
            List<PcapIf> alldevs = new ArrayList<>(); // Will be filled with NICs  
            final StringBuilder errbuf = new StringBuilder(); // For any error msgs 
            int r = Pcap.findAllDevs(alldevs, errbuf);  
            if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Can't read list of devices");  
                return(null);
            } 
            int i = 0,j=0; 
            for (PcapIf device : alldevs) {
                if(LoginController.intfc.equals(device.getName())){j=i;break;}
                i++;
            }
            PcapIf device = alldevs.get(j); // We know we have atleast 1 device  
            final String s=device.getName();
           // JOptionPane.showMessageDialog(null, s );
            final int snaplen = 64 * 1024;           // Capture all packets, no trucation  
            final int flags = Pcap.MODE_NON_PROMISCUOUS; // capture all packets  
            final int timeout = 100;           // 1 seconds in millis  
            final Pcap pcap = Pcap.openLive(s, snaplen, flags, timeout, errbuf);
            if (pcap == null) {  
                System.err.printf("Error while opening device for capture: " + errbuf.toString());  
                return(null);   
            }
           Thread dump = new Thread(new Runnable() {public void run(){
            writepackettofile(s, snaplen, flags, timeout, errbuf);
            }});
           dump.start();
            return(pcap);
     }

        static void  capturepackets(Pcap pcap)
          {
                final Ethernet eth=new Ethernet();
                final Ip4 ip=new Ip4();
                final Ip6 ip6=new Ip6();
                final Arp arp=new Arp();
                JBufferHandler<String> handler = new JBufferHandler<String>() {  
                private final PcapPacket packet = new PcapPacket(JMemory.POINTER); 
                public void nextPacket(PcapHeader header, JBuffer buffer, String user) {     
                nobytes+= buffer.size();
                    if(livemonitoring) 
                 {
                 packet.peer(buffer); // Peer the data to our packet  
                 packet.getCaptureHeader().peerTo(header,0);
                 packet.scan(Ethernet.ID);
                 p.packetSize=packet.getCaptureHeader().wirelen();
                 pcount++;  
                 //pd.add(new PData(packet.toString()));
                 p.destPort=0;
                 p.sourcePort=0;
                 int type=packet.getHeader(eth).type();
                 p.capTime=new Date(packet.getCaptureHeader().timestampInMillis());
                 p.destMAC=org.jnetpcap.packet.format.FormatUtils.mac(packet.getHeader(eth).destination());
                 p.sourceMAC=org.jnetpcap.packet.format.FormatUtils.mac(packet.getHeader(eth).source());      
                 //Info from IP4 header 
                 
                 if(type==2048)
                 {
                   
                   p.sourceIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(ip).source());
                   p.destIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(ip).destination());
                   type=packet.getHeader(ip).type();
                     switch (type) {
                         case 1:
                             p.protocol="Internet Control Message Protocol (ICMP)";
                             break;
                         case 2:
                             p.protocol="Internet Group Management Protocol (IGMP)";
                             break;
                         case 6:
                             final Tcp tcp = new Tcp();
                             p.protocol="Transmission Control Protocol (TCP)";
                             p.destPort=packet.getHeader(tcp).destination();
                             p.sourcePort=packet.getHeader(tcp).source();
                             break;
                         case 17:
                             final Udp udp = new Udp();
                             p.protocol="User Datagram Protocol (UDP)";
                             p.destPort=packet.getHeader(udp).destination();
                             p.sourcePort=packet.getHeader(udp).source();
                             break;
                         case 41:
                             p.protocol="IPv6 encapsulation (ENCAP)";
                             break;
                         case 89:
                             p.protocol="Open Shortest Path First (OSPF)";
                             break;
                         default:
                             p.protocol="Unknown";
                             break;
                     }
                 }
                 if(type==2054)
                 {
                   
                    p.protocol="Address Resolution Protocol (ARP)";
                    p.sourceIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(arp).spa());
                    p.destIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(arp).tpa());
                 }
                 if(type==34525)
                 {
                   
                    p.sourceIP=org.jnetpcap.packet.format.FormatUtils.asStringIp6(packet.getHeader(ip6).source(),true);
                    p.sourceIP=p.sourceIP.replaceAll("(:(0)*)|(^0+)",":");
                    p.destIP=org.jnetpcap.packet.format.FormatUtils.asStringIp6(packet.getHeader(ip6).destination(),true);
                    p.destIP=p.destIP.replaceAll("(:(0)*)|(^0+)",":");
                    type=packet.getHeader(ip6).next();
                   
                     switch (type) {
                         case 0:
                             p.protocol="IPv6 Hop-by-Hop Option (HOPOPT)";
                             break;
                         case 1:
                             p.protocol="Internet Control Message Protocol (ICMP)";
                             break;
                         case 2:
                             p.protocol="Internet Group Management Protocol (IGMP)";
                             break;
                         case 6:
                             final Tcp tcp = new Tcp();
                             p.protocol="Transmission Control Protocol (TCP)";
                             p.destPort=packet.getHeader(tcp).destination();
                             p.sourcePort=packet.getHeader(tcp).source();
                             break;
                         case 17:
                             final Udp udp = new Udp();
                             p.protocol="User Datagram Protocol (UDP)";
                             p.destPort=packet.getHeader(udp).destination();
                             p.sourcePort=packet.getHeader(udp).source();
                             break;
                         case 58:
                             p.protocol="ICMP for IPv6";
                             break;
                         case 89:
                             p.protocol="Open Shortest Path First (OSPF)";
                             break;
                         default:
                             p.protocol="Unknown : "+type;
                             
                             break;
                     }
                 
                 }
                 
                 if(packet.hasHeader(new Http()))
                 {p.protocol="Hyper Text Transfer Protocol (HTTP)";}
                 
                 if(packet.hasHeader(new Sip()))
                 {p.protocol="Session Initiation Protocol (SIP)";}
                 
                     if(!filtr){        
                     packt d=new packt();
                     d.capTime=p.capTime;
                     d.destIP=p.destIP;
                     d.destMAC=p.destMAC;
                     d.destPort=p.destPort;
                     d.frameno=(long)pcount;
                     d.packetSize=p.packetSize;
                     d.protocol=p.protocol;
                     d.sourceIP=p.sourceIP;
                     d.sourceMAC=p.sourceMAC;
                     d.sourcePort=p.sourcePort;
                     data.add(d);//this adds the packet to table
                     }
                     else
                         filter(p);
                 } 
                 
              }  
         };  
  
pcap.loop(Pcap.LOOP_INFINITE, handler, "jNetPcap rocks!");  
  
pcap.close();
          }

     public static void writepackettofile(String s, int i, int i1, int i2, StringBuilder sb){
        try
      {
          
          StringBuilder errbuf = new StringBuilder();
            int fno=new File("/root/packetcapture/").listFiles().length+1;
            pcap = Pcap.openLive(s, i, i1, i2, sb);
            String ofile = "/root/packetcapture/temp"+fno+".cap"; 
            dumper = pcap.dumpOpen(ofile);
            pcap.loop(Pcap.LOOP_INFINITE, dumper); // Special native dumper call to loop  
            
      }catch(Exception ex)
      {
         ex.printStackTrace();
      }
     
     }
     
     public static void readpacketfromfile(final int h){ 
         boolean wastrue=false;
         int pc = 0;
     try
      {
          if(livemonitoring){
          livemonitoring=false;
          wastrue=true;
          pc=pcount;
          }
          data.clear();
          final StringBuilder errbuf = new StringBuilder(); // For any error msgs
          int fno=new File("/root/packetcapture/").listFiles().length;
          final List<String> ip = new ArrayList<>();
          final List<Integer> cip = new ArrayList<>();
          final List<byte[]> ipb = new ArrayList<>();
     in=0;
     pcount=0;
     totp=(long)0;
     totsize=(long)0;
     final Ip4 ip4=new Ip4();
     final Tcp tcp = new Tcp();
     final Udp udp = new Udp();
     final Ip6 ip6=new Ip6();
     final Arp arp=new Arp();
     final Ethernet eth=new Ethernet();
     packt k=new packt();
     //loop for different pcap files
          for(int i=1;i<=fno;i++){
          
          String FileAddress="/root/packetcapture/temp"+i+".cap";
          Pcap pcap2 = Pcap.openOffline(FileAddress, errbuf);
    
   //Throw exception if it cannot open the file
    if (pcap2 == null) {  
             System.out.println("file problem");
             return;
          }
     
JBufferHandler<String> jpacketHandler1 = new JBufferHandler<String>() {
    
    int type=0;
    
                private final PcapPacket packet = new PcapPacket(JMemory.POINTER); 
                public void nextPacket(PcapHeader header, JBuffer buffer, String user) {  
                    
                 packet.peer(buffer); 
                 packet.getCaptureHeader().peerTo(header,0);
                 packet.scan(Ethernet.ID);
                 totsize+= buffer.size();
                 totp++;
                 
             if(h!=1000){//not just no of packets and size  
                 pcount++;
                 k.packetSize=packet.getCaptureHeader().wirelen();
                 k.destPort=0;
                 k.sourcePort=0;
                 if(packet.hasHeader(ip4))
                 {
                   k.sourceIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(ip4).source());
                   k.destIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(ip4).destination());
                 }
                 if(packet.hasHeader(tcp))
                 { 
                   k.destPort=packet.getHeader(tcp).destination();
                   k.sourcePort=packet.getHeader(tcp).source();
                 }
                 else if(packet.hasHeader(udp))
                 {   k.destPort=packet.getHeader(udp).destination();
                     k.sourcePort=packet.getHeader(udp).source();
                 }
   
                 if(sholddata)//this adds the packet to table
                 {
                 if(packet.hasHeader(eth))
                 {
                     type=packet.getHeader(eth).type();
                 }
                 k.capTime=new Date(packet.getCaptureHeader().timestampInMillis()); 
                 k.destMAC=org.jnetpcap.packet.format.FormatUtils.mac(packet.getHeader(eth).destination());
                 k.sourceMAC=org.jnetpcap.packet.format.FormatUtils.mac(packet.getHeader(eth).source());      
                 //Info from IP4 header 
                 
                 if(type==2048)
                 {
                   type=packet.getHeader(ip4).type();
                     switch (type) {
                         case 1:
                             k.protocol="Internet Control Message Protocol (ICMP)";
                             break;
                         case 2:
                             k.protocol="Internet Group Management Protocol (IGMP)";
                             break;
                         case 6:
                             k.protocol="Transmission Control Protocol (TCP)";
                             break;
                         case 17:   
                             k.protocol="User Datagram Protocol (UDP)";       
                             break;
                         case 41:
                             k.protocol="IPv6 encapsulation (ENCAP)";
                             break;
                         case 89:
                             k.protocol="Open Shortest Path First (OSPF)";
                             break;
                         default:
                             k.protocol="Unknown";
                             break;
                     }
                 }
                 if(type==2054)
                 {
                    
                    k.protocol="Address Resolution Protocol (ARP)";
                    k.sourceIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(arp).spa());
                    k.destIP=org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(arp).tpa());
                 }
                 if(type==34525)
                 {
                    
                    k.sourceIP=org.jnetpcap.packet.format.FormatUtils.asStringIp6(packet.getHeader(ip6).source(),true);
                    k.sourceIP=k.sourceIP.replaceAll("(:(0)*)|(^0+)",":");
                    k.destIP=org.jnetpcap.packet.format.FormatUtils.asStringIp6(packet.getHeader(ip6).destination(),true);
                    k.destIP=k.destIP.replaceAll("(:(0)*)|(^0+)",":");
                    type=packet.getHeader(ip6).next();
                   
                     switch (type) {
                         case 0:
                             k.protocol="IPv6 Hop-by-Hop Option (HOPOPT)";
                             break;
                         case 1:
                             k.protocol="Internet Control Message Protocol (ICMP)";
                             break;
                         case 2:
                             k.protocol="Internet Group Management Protocol (IGMP)";
                             break;
                         case 6:
                             k.protocol="Transmission Control Protocol (TCP)";
                             break;
                         case 17:
                             k.protocol="User Datagram Protocol (UDP)";
                             break;
                         case 58:
                             k.protocol="ICMP for IPv6";
                             break;
                         case 89:
                             k.protocol="Open Shortest Path First (OSPF)";
                             break;
                         default:
                             k.protocol="Unknown : "+type;
                             
                             break;
                     }
                 
                 }
                 
                 if(packet.hasHeader(new Http()))
                 {k.protocol="Hyper Text Transfer Protocol (HTTP)";}
                 
                 if(packet.hasHeader(new Sip()))
                 {k.protocol="Session Initiation Protocol (SIP)";}
                   
                 k.frameno=(long)pcount;
                     packt d=new packt();
                     d.capTime=k.capTime;
                     d.destIP=k.destIP;
                     d.destMAC=k.destMAC;
                     d.destPort=k.destPort;
                     d.frameno=(long)pcount;
                     d.packetSize=k.packetSize;
                     d.protocol=k.protocol;
                     d.sourceIP=k.sourceIP;
                     d.sourceMAC=k.sourceMAC;
                     d.sourcePort=k.sourcePort;
                     data.add(d);
                 }
                 
            else{
                 //for finding stats
                 int t=0;
                 boolean found=false;
                 if(k.destIP.startsWith("192.168")||k.destIP.startsWith("172.16")||k.destIP.startsWith("10.")||!packet.hasHeader(ip4)) //ignores private addresses
                   {
                        //System.out.println("internal");
                        return;
                   }
                if(!(k.sourcePort==80||k.destPort==80||k.sourcePort==443||k.destPort==443)) //ignores private addresses
                   {
                        //System.out.println("not 80 or 443");
                        return;
                   }
                final String f=k.destIP.substring(0, 8);
                
                 for(in=0;in<ip.size();in++){
                 if(ip.get(in).startsWith(f))
                 {
                     t=cip.get(in);
                     t++;
                     cip.set(in,t);
                     found=true;
                     //System.out.println("webiste ++ ");
                 }
                }
                 if(found==false){
                 ip.add(in,k.destIP);
                 //System.out.println("added website");
                 ipb.add(packet.getHeader(ip4).destination());
                 cip.add(in, 1);
                 }
                 
                 }        
}  
                }
          };
          pcap2.loop(-1,jpacketHandler1,null);
          pcap2.close();
          
          }
          if(wastrue){
          livemonitoring=true;
          pcount=pc;
          }
          
        if(h==0||h==5){  
         int l=0;
         
         if(h==0)
             l=ip.size();
         else if(h==5)
             l=5;
          for(int i=0;i<l;i++){
              
            int index = i;
            for (int j = i + 1; j < cip.size(); j++)
                if (cip.get(j) > cip.get(index))
                    index = j;
            int LargerNumber = cip.get(index);
            String iph=ip.get(index);
            byte[] ib=ipb.get(index);
            cip.set(index, cip.get(i));
            ip.set(index, ip.get(i));
            ipb.set(index, ipb.get(i));
            cip.set(i,LargerNumber);
            ip.set(i,iph);
            ipb.set(i,ib);
            ip.set(i, Accesscontrol.iptohost(ipb.get(i)));
            final String nm=ip.get(i);
            webl.add(nm);
            System.out.println("was inside top website");
          }
      }
   }
      
catch (Exception ex) {
         ex.printStackTrace();
    }
         
 }
     public static void filter(packt p){
         {
             int i;boolean j=false;
                     
             if(Pcaper.fltr.srcp!=null){
                       for(i=0;i<Pcaper.fltr.srcp.length;i++){
                       if(Integer.parseInt(Pcaper.fltr.srcp[i].trim())==p.sourcePort){
                       j=true;
                       }
                       }
                       if(!j)
                           return;
                     }
                     
                     j=false;
                     
             if(Pcaper.fltr.dstp!=null){
                       for(i=0;i<Pcaper.fltr.dstp.length;i++){
                       if(Integer.parseInt(Pcaper.fltr.dstp[i].trim())==p.destPort){
                       j=true;
                       }
                       }
                       if(!j)
                           return;
                     }
             
                     j=false;
                     
             if(Pcaper.fltr.dstip!=null){
                       for(i=0;i<Pcaper.fltr.dstip.length;i++){
                       if(Pcaper.fltr.dstip[i].trim().equalsIgnoreCase(p.destIP)){
                       j=true;
                       }
                       }
                       if(!j)
                           return;
                     }
             
                     j=false;
                     
             if(Pcaper.fltr.srcip!=null){
                       for(i=0;i<Pcaper.fltr.srcip.length;i++){
                       if(Pcaper.fltr.srcip[i].trim().equalsIgnoreCase(p.sourceIP)){
                       j=true;
                       }
                       }
                       if(!j)
                           return;
                     }
             
                     j=false;
                     
             if(Pcaper.fltr.prtcl!=null){
                       for(i=0;i<Pcaper.fltr.prtcl.length;i++){
                       if(Pcaper.fltr.prtcl[i].trim().equalsIgnoreCase(p.protocol)){
                       j=true;
                       }
                       }
                       if(!j)
                           return;
                     }
                     packt d=new packt();
                     d.capTime=p.capTime;
                     d.destIP=p.destIP;
                     d.destMAC=p.destMAC;
                     d.destPort=p.destPort;
                     d.frameno=(long)pcount;
                     d.packetSize=p.packetSize;
                     d.protocol=p.protocol;
                     d.sourceIP=p.sourceIP;
                     d.sourceMAC=p.sourceMAC;
                     d.sourcePort=p.sourcePort;
                     data.add(d);
                     
         }
     }
    
}

