package main;
import javax.swing.JOptionPane;

public class Avvia {
    public static void main(String[] args) {
        try {
            GUI m = new GUI();
            DrawGraph mainPanel = new DrawGraph();
            m.addGraph(mainPanel);
            String classpath = System.getProperty("java.class.path");
            System.out.println("Classpath: " + classpath);
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(
            null, t.getClass().getSimpleName() + ": " + t.getMessage());
            throw t; // don't suppress Throwable
            } 
    }
        
    }

