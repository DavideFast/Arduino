package main;
import com.fazecast.jSerialComm.SerialPort;

public class Interfaccia_Arduino {

    private String porta_utilizzata = "COM4";
    private SerialPort[] porte;
    private SerialPort interfaccia;


    public void changePort(String newPort){
        interfaccia.closePort();
        // Open the desired serial port
        interfaccia = SerialPort.getCommPort(newPort); // Change "COM3" to your port name
        interfaccia.setComPortParameters(921600, 8, 1, 0); // Set baud rate, data bits, stop bits, and parity
        interfaccia.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        if (interfaccia.openPort()) {
            System.out.println("Port opened successfully.");
        } else {
            System.out.println("Failed to open port.");
            return;
        }

    }

    public Interfaccia_Arduino(){
        porte = SerialPort.getCommPorts();
        
        // Open the desired serial port
        interfaccia = SerialPort.getCommPort(porta_utilizzata); // Change "COM3" to your port name
        interfaccia.setComPortParameters(921600, 8, 1, 0); // Set baud rate, data bits, stop bits, and parity
        interfaccia.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        if (interfaccia.openPort()) {
            //System.out.println("Port opened successfully.");
        } else {
            //System.out.println("Failed to open port.");
            return;
        }
    }

    public String getPorta_utilizzata() {
        return porta_utilizzata;
    }

    public SerialPort[] getPorte() {
        return porte;
    }

    public SerialPort getInterfaccia() {
        return interfaccia;
    }

    public void setPorta_utilizzata(String porta_utilizzata) {
        this.porta_utilizzata = porta_utilizzata;
    }
    
}
