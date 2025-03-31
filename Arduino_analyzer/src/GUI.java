import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GUI {

    private JFrame frame;
    private DrawGraph mainPanel;

    public GUI(){

       //Calcolo dimensioni schermo
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       int width = screenSize.width;
       int height = screenSize.height;
       
       
       //Creazione Pannello principale
       frame = new JFrame("My First GUI");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(width,height);
       
       frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
       frame.getContentPane().setBackground(new Color(218,252,249));
       frame.setVisible(true);

    }

    public void addGraph(DrawGraph mainPanel) {
        this.mainPanel = mainPanel;
        frame.getContentPane().add(mainPanel);
        frame.repaint();
    }

    public void updateGraph(String totale) {
        mainPanel.updateGraph(totale);
        frame.repaint();
    }
    
}
