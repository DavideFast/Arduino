import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.fazecast.jSerialComm.*;

public class Prova {
    public static void main(String[] args) {
        // List all available serial ports
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            System.out.println("Available port: " + port.getSystemPortName());
            System.out.println(port.getDescriptivePortName());
        }

        // Open the desired serial port
        SerialPort serialPort = SerialPort.getCommPort("COM4"); // Change "COM3" to your port name
        serialPort.setComPortParameters(921600, 8, 1, 0); // Set baud rate, data bits, stop bits, and parity
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        if (serialPort.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Failed to open port.");
            return;
        }

        CSV_creator file;
        String[] diviso;
        String[] data;
        String totale="";



        GUI m = new GUI();
        List<Integer> scores = new ArrayList<Integer>();
        Random random = new Random();
        int maxDataPoints = 16;
        int maxScore = 20;
        for (int i = 0; i < maxDataPoints ; i++) {
            scores.add(random.nextInt(maxScore));
        }
        DrawGraph mainPanel = new DrawGraph();
        m.addGraph(mainPanel);



        
        // Read data from the serial port
        try (InputStream in = serialPort.getInputStream()) {
            
            
            
            byte[] buffer = new byte[1024];
            int len;
            
            while ((len = in.read(buffer)) > -1) {
                totale = totale + (new String(buffer,0,len));
                totale = totale.replace("HX711 not found.","0.4");
                m.updateGraph(totale);
                if(totale.contains("STOP")){
                    totale = totale.replace("//s","");
                    totale = totale.replace("AVVIO","");
                    totale = totale.replace("STOP","");
                    diviso = totale.split("\n");
                    file = new CSV_creator("File 1");
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
                    Scanner myObj = new Scanner(System.in);
                    System.out.println("Dai un nome al file");
                    String userName = myObj.nextLine();
                    
                    file.rename(userName);
                    while(!file.salvaFile()){
                        System.out.println("File non creato forse ne esiste uno con lo stesso nome");
                        myObj = new Scanner(System.in);
                        System.out.println("Rinomina il file");
                        userName = myObj.nextLine();
                        file.rename(userName); 
                    }
                    //myObj.close();
                    file.pulisci();
                    totale="";
                    if (serialPort.isOpen() ) {
                        System.out.println("Continuare con i test");
                        String command = myObj.next(); 
                        serialPort.getOutputStream().write(command.charAt(0));
                        serialPort.getOutputStream().flush();
        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            serialPort.closePort();
            System.out.println("Port closed.");
        }
    }
}
