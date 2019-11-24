package server8;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class bandwidth  {

public static Gauge download,temp;
public static Thread moveNeedle=new Thread(),caltemp=new Thread();
    public static void createbwm(GridPane bwpane) {
        GaugeBuilder builder = GaugeBuilder.create().skinType(Gauge.SkinType.BULLET_CHART);
        download  = builder.decimals(1).maxValue(500).unit("KB/Sec").title("Bandwidth").build();
        VBox downBox = getTopicBox("Bandwidth Usage", Color.rgb(77,208,225), download);      
        bwpane.setPadding(new Insets(20));
        bwpane.setBackground(new Background(new BackgroundFill(Color.rgb(244,244,244), CornerRadii.EMPTY, Insets.EMPTY)));
        bwpane.add(downBox, 0, 0);
        
        GaugeBuilder builder2 = GaugeBuilder.create().skinType(Gauge.SkinType.BULLET_CHART);
        temp  = builder2.decimals(1).maxValue(100).unit("°C").title("Pi Temperature").build();
        VBox downBox2 = getTopicBox("Temperature", Color.rgb(250,20,25), temp);      
        temp.setValue(10);
        
        bwpane.add(downBox2, 0, 1);
        moveNeedle = new Thread(new Runnable() {public void run(){
                     double down=0;
          while(true){             
            try {
              down=Pcaper.nobytes/1024;
              //temp.setValue();
              download.setValue(down);
              Pcaper.nobytes=0;         
               moveNeedle.sleep(1000);//as speed is per second and 1000 makes 1 sec
               } catch (InterruptedException ex) {
                    
               }
           }    
}});
caltemp= new Thread(new Runnable() {public void run(){
                     double down=0;
          while(true){             
            try {
                String[] command = {"cat", "/sys/class/thermal/thermal_zone0/temp"};
                Process p = Runtime.getRuntime().exec(command);
         BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
         String line=new String(),l;
         
         while((l=br.readLine())!=null)
             line+=l;
         
         Double tm=Double.parseDouble(line);
         tm=tm/1000;
         temp.setValue(tm);
               caltemp.sleep(1000);//as speed is per second and 1000 makes 1 sec
               } catch (InterruptedException ex) {
                    
               }         catch (IOException ex) {
                             
                         }
           }    
}});        
   
        moveNeedle.start();
        caltemp.start();
    } 
     /*public static double BandwidthDToAngle(){
        double bytesd;
        bytesd=Pcaper.nobytes/1024;
        Pcaper.nobytes=0;
        //System.out.println("down:"+bytesd);
        return (bytesd);      
    }*/
     
  static private VBox getTopicBox(final String TEXT, final Color COLOR, final Gauge GAUGE) {
        Rectangle bar = new Rectangle(200, 3);
        bar.setArcWidth(6);
        bar.setArcHeight(6);
        bar.setFill(COLOR);

        Label label = new Label(TEXT);
        label.setTextFill(COLOR);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(0, 0, 10, 0));

        GAUGE.setBarColor(COLOR);
        GAUGE.setBarBackgroundColor(Color.rgb(39,44,50));
        GAUGE.setAnimated(true);

        VBox vBox = new VBox(bar, label, GAUGE);
        vBox.setSpacing(3);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
}