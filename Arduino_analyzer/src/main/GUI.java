package main;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    private JFrame frame;
    private DrawGraph mainPanel;
    private SerialPort interfaccia;
    private SerialPort[] porte;
    private String esercizio;
    private String atleta;
    private CSV_creator file;

    public void setPorte(SerialPort[] p){
        porte = p;
    }

    @SuppressWarnings("unchecked")
    public GUI(){

       //Calcolo dimensioni schermo
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       int width = screenSize.width;
       int height = screenSize.height;
       Interfaccia_Arduino aggancio = new Interfaccia_Arduino();
       interfaccia = aggancio.getInterfaccia();

       //Creazione Pannello principale
       frame = new JFrame("Cella di carico");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(width,height);
       
       frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
       frame.getContentPane().setBackground(new Color(218,252,249));

       JButton start = new JButton("START");
       start.setBounds(10, 10, 100, 100);
       JButton stop = new JButton("STOP");
       stop.setBounds(110,10,100,100);

       porte = aggancio.getPorte();

       String[] appoggio = new String[porte.length];
       for(int i=0;i<porte.length;i++)
            appoggio[i] = porte[i].getDescriptivePortName();


       JComboBox<String> porta = new JComboBox<String>(appoggio);
       porta.setBounds(210, 10, 100, 100);

       porta.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent e) { 
            interfaccia = porte[((JComboBox<String>) e.getSource()).getSelectedIndex()];
            aggancio.changePort(interfaccia.getSystemPortName());
            interfaccia = aggancio.getInterfaccia();
        } 
      } );

       String[] data = {"","Pull over","Pull over sx","Pull over dx","Squat","Squat mono sx","Squat mono dx", "Flessore sx", "Flessore dx", "Leg extension sx", "Leg estension dx", "Gastroacnemio sx", "Gastroacnemio dx", "Soleo sx", "Soleo dx"};

       JComboBox<String> esercizi = new JComboBox<String>(data);
       esercizi.setBounds(310,10,100,100);

       esercizi.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent e) { 
            esercizio = (String) ((JComboBox) e.getSource()).getSelectedItem();
            System.out.println(esercizio);
        } 
      } );

       String[] nomi = {"","Baldaccini Giulia","Bonifazi Francesca","Borasso Francesca", "Candellori Davide","Candellori Vanessa","Capri Andrea","Cecconelli Emma","Di Mario Matilde","Felici Sofia","Gallinella Leo","Mazzoli Aurora","Negroni Giulia","Patrone Andrea","Proietti Linda"};

       JComboBox<String> atleti = new JComboBox<String>(nomi);
       atleti.setBounds(310,10,100,100);

       atleti.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent e) { 
            atleta = (String) ((JComboBox) e.getSource()).getSelectedItem();
            System.out.println(atleta);
        } 
      } );

      

       JPanel menu = new JPanel();

       frame.getContentPane().add(menu,BorderLayout.NORTH);
       menu.add(start,BorderLayout.NORTH);
       menu.add(stop,BorderLayout.SOUTH);
       menu.add(porta,BorderLayout.NORTH);
       menu.add(esercizi,BorderLayout.SOUTH);
       menu.add(atleti, BorderLayout.SOUTH);

       

       start.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent e) {
            if(atleta!=null && esercizio!=null){
            StartThread myStart = new StartThread(interfaccia, file, atleta, esercizio, mainPanel);
            myStart.start();
            }
            else
                System.out.println("Seleziona atleta ed esercizio");
        } 
       } );

      stop.addActionListener(new ActionListener() { 
        public void actionPerformed(ActionEvent e) { 
            StopThread myStop = new StopThread(interfaccia);
            myStop.start();
        } 
      } );




       frame.setVisible(true);

    }

    public void addGraph(DrawGraph mainPanel) {
        this.mainPanel = mainPanel;
        frame.getContentPane().add(mainPanel,BorderLayout.CENTER);
        frame.repaint();
    }

    public void updateGraph(String totale) {
        System.out.println("OKK");
        mainPanel.updateGraph(totale);
        frame.repaint();
    }
    
}
