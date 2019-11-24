package client;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

public class bandusagecal {
    static double bndused=(long)0; 
    static void pcaper(){
        List<PcapIf> alldevs = new ArrayList<>(); // Will be filled with NICs  
            final StringBuilder errbuf = new StringBuilder(); // For any error msgs 
            int r = Pcap.findAllDevs(alldevs, errbuf);  
            if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Can't read list of devices");  
                return;
            } 

            PcapIf device = alldevs.get(0); // We know we have atleast 1 device  
            final String s=device.getName();
           // JOptionPane.showMessageDialog(null, s );
            final int snaplen = 64 * 1024;           // Capture all packets, no trucation  
            final int flags = Pcap.MODE_NON_PROMISCUOUS; // capture all packets  
            final int timeout = 100;           // 1 seconds in millis  
            final Pcap pcap = Pcap.openLive(s, snaplen, flags, timeout, errbuf);
            if (pcap == null) {  
                System.err.printf("Error while opening device for capture: " + errbuf.toString());  
                return;
            }
            Tcp tcp =new Tcp();
            Udp udp =new Udp();
            JPacketHandler<String> jpacketHandler = new JPacketHandler<String>() {  
                @Override
                public void nextPacket(JPacket packet, String user) {
                    
                    if(packet.hasHeader(tcp)){
                    if(packet.getHeader(tcp).source()==5215||packet.getHeader(tcp).source()==5216||packet.getHeader(tcp).source()==5217||packet.getHeader(tcp).source()==5218||packet.getHeader(tcp).source()==5219)
                    {
                      return;
                    }
                    }
                    else if(packet.hasHeader(udp)){
                    if(packet.getHeader(udp).source()==5215||packet.getHeader(udp).source()==5216||packet.getHeader(udp).source()==5217||packet.getHeader(udp).source()==5218||packet.getHeader(udp).source()==5219)
                    {
                      return;
                    }
                    }
                 bndused+=(long)packet.getCaptureHeader().wirelen();
             }
               };  
            
          while(true){
            pcap.dispatch(10, jpacketHandler, null); 
           }
    
    }
    
}