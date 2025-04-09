package main;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.fazecast.jSerialComm.SerialPort;

public class StartThread extends Thread{

    private SerialPort interfaccia;
    private CSV_creator file;
    private String atleta;
    private String esercizio;
    private DrawGraph istance;

    public StartThread(SerialPort interfaccia, CSV_creator file, String atleta, String esercizio, DrawGraph istance){
        this.interfaccia = interfaccia;
        this.file = file;
        this.atleta = atleta;
        this.esercizio = esercizio;
        this.istance = istance;
    }

    public void run() {
        System.out.println(interfaccia);
        if(!interfaccia.isOpen())
                interfaccia.openPort();
            try {
                interfaccia.getOutputStream().write('1');
                interfaccia.getOutputStream().flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            String[] diviso;
            String[] data;
            String totale="";
            

            try (InputStream in = interfaccia.getInputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                boolean lock = true; 
                while ((len = in.read(buffer)) > -1 && lock) {
                    totale = totale + (new String(buffer,0,len));
                    totale = totale.replace("HX711 not found.","0.4");
                    istance.updateGraph(totale);
                    if(totale.contains("STOP")){
                        lock=false;
                        totale = totale.replace("//s","");
                        totale = totale.replace("AVVIO","");
                        totale = totale.replace("STOP","");
                        diviso = totale.split("\n");
                        file = new CSV_creator((atleta+"____"+esercizio+"____"+new Date().toString()).replace(":","-").replace(" ","_"));
                        for(int i=0; i<diviso.length;i++){
                            if(diviso[i].length()!=0){
                                data = diviso[i].split(",");
                                if(!data[0].equals("HX711 not found."))
                                    file.addValore(data[0]);
                                else
                                    file.addValore("0");
                                file.addTempo(data[1]);
                            }
                        }
                        file.salvaFile();
                        interfaccia.closePort();
                    }
                }
            } catch (Exception ee) {
                System.out.println("CAZOOOOO");
                ee.printStackTrace();
            } finally {
                //interfaccia.closePort();
                System.out.println("Port closed.");
            }
            System.out.println("RAGGIUNTO");
    }


}
