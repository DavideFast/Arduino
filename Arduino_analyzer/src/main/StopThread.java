package main;
import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

public class StopThread extends Thread{

    private SerialPort interfaccia;

    public StopThread(SerialPort interfaccia){
        this.interfaccia = interfaccia;
    }
    
    public void run(){
        try {
                interfaccia.getOutputStream().write('0');
                interfaccia.getOutputStream().flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


    }
}