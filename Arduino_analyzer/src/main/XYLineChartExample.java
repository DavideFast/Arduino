package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYLineChartExample extends JFrame {

    private XYDataset createDataset(List<Float> scores, List<Integer> tempo) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.setAutoWidth(rootPaneCheckingEnabled);
        XYSeries series1 = new XYSeries("Forza [Kg]");
        double median = 0;
        double max = -120;
        int contatore=0;
    
        for(int i=0;i<scores.size();i++){
            series1.add(tempo.get(i), scores.get(i));
            if(scores.get(i)>2){
                median = median + scores.get(i);
                contatore = contatore + 1;
            }
            if(max<scores.get(i))
                max = scores.get(i);
        }

        median = median / contatore;

        XYSeries series2 = new XYSeries("Max [Kg]");
        XYSeries series3 = new XYSeries("Media [Kg]");

        for(int i=0;i<scores.size();i++){
            series2.add((int) tempo.get(i),max);
            series3.add((int) tempo.get(i),median);
        }
        
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
    
        return dataset;
    }
 
    public XYLineChartExample(List<Float> scores, List<Integer> tempo, String titolo) throws IOException {
        super(titolo);
 
        JPanel chartPanel = createChartPanel(scores,tempo,titolo);
        add(chartPanel);
 
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
 
    private JPanel createChartPanel(List<Float> scores, List<Integer> tempo, String titolo) throws IOException {
        String chartTitle = titolo;
        String xAxisLabel = "Tempo [ms]";
        String yAxisLabel = "Forza [Kg]";
        String userHome = System.getProperty("user.home");
        String desktopPath = Paths.get(userHome, "Desktop").toString();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String giorno = formatter.format(date);
        
        Path path = Paths.get(desktopPath+"/Test "+giorno);
        if(!new File(path.toString()).isDirectory())
            Files.createDirectory(path);
        XYDataset dataset = createDataset(scores, tempo);
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
        ChartUtilities.saveChartAsPNG(new File(desktopPath+"/Test "+giorno+"/"+titolo+".png"), chart, 1920, 1080);
        return new ChartPanel(chart);
    }

}