package main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawGraph extends JPanel {
    private static final int MAX_SCORE = 20;
    private static final int BORDER_GAP = 30;
    private static final Color GRAPH_COLOR = Color.blue;
    private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 150, 180);
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    private static final int GRAPH_POINT_WIDTH = 12;
    private static final int Y_HATCH_CNT = 10;
    private List<Float> scores;
    private List<Integer> times;
 
    public DrawGraph() {
       this.scores = new ArrayList<>();
       this.times = new ArrayList<>();
    }
 
    @Override
    protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       Graphics2D g2 = (Graphics2D)g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
       double xScale = ((double) getWidth() - 2 * BORDER_GAP) / 100;//(scores.size() - 1);
       double yScale = ((double) getHeight() - 2 * BORDER_GAP) / 200;//(MAX_SCORE - 1);
 
       List<Point> graphPoints = new ArrayList<Point>();
       
         if(scores.size()<=100){
            for (int i = 0; i < scores.size(); i++) {
               int x1 = (int) (i * xScale + BORDER_GAP);
               int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
               graphPoints.add(new Point(x1, y1));
            }
         }
         else{
            for (int i = 0; i < 100; i++) {
               int x1 = (int) (i * xScale + BORDER_GAP);
               int y1 = (int) ((MAX_SCORE - scores.get(scores.size()-100+i)) * yScale + BORDER_GAP);
               graphPoints.add(new Point(x1, y1));
            }
         }

      /*for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
            graphPoints.add(new Point(x1, y1));
       }*/
 
       // create x and y axes 
       g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
       g2.drawLine(BORDER_GAP, getHeight()/2 - BORDER_GAP, getWidth() - BORDER_GAP, getHeight()/2 - BORDER_GAP);
 
       // create hatch marks for y axis. 
       for (int i = 0; i < Y_HATCH_CNT; i++) {
          int x0 = BORDER_GAP;
          int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
          int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
          int y1 = y0;
          g2.drawLine(x0, y0, x1, y1);
       }
 
       // and for x axis
       for (int i = 0; i < scores.size() - 1; i++) {
          int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
          int x1 = x0;
          int y0 = getHeight() - BORDER_GAP;
          int y1 = y0 - GRAPH_POINT_WIDTH;
          g2.drawLine(x0, y0, x1, y1);
       }
 
       Stroke oldStroke = g2.getStroke();
       g2.setColor(GRAPH_COLOR);
       g2.setStroke(GRAPH_STROKE);
       for (int i = 0; i < graphPoints.size() - 1; i++) {
          int x1 = graphPoints.get(i).x;
          int y1 = graphPoints.get(i).y;
          int x2 = graphPoints.get(i + 1).x;
          int y2 = graphPoints.get(i + 1).y;
          g2.drawLine(x1, y1, x2, y2);         
       }
 
       g2.setStroke(oldStroke);      
       g2.setColor(GRAPH_POINT_COLOR);
       for (int i = 0; i < graphPoints.size(); i++) {
          int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
          int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
          int ovalW = GRAPH_POINT_WIDTH;
          int ovalH = GRAPH_POINT_WIDTH;
          g2.fillOval(x, y, ovalW, ovalH);
       }
    }

    public void updateGraph(String totale){
      scores.removeAll(scores);
      times.removeAll(times);
        String data = totale.replace("//s","").replace("AVVIO","").replace("STOP","").replace(" ","");
        String[] data_diviso = data.split("\n");
        String[] valori;
        if(data.length()>0)
        for(String row : data_diviso){
            if(!row.isEmpty() && row.length()>5){
               System.out.println(row);
               valori = row.split(",");
               if(valori[0].matches("-?\\d+(\\.\\d+)?") && valori[1].matches("-?\\d+(\\.\\d+)?")){
               if(valori[0].contains("-"))
                  scores.add(-76-Float.parseFloat(valori[0].replace("-","")));
               else
                  scores.add(-76+Float.parseFloat(valori[0]));
               times.add(Integer.parseInt(valori[1]));

               }
               else
                  scores.add(Float.parseFloat("10"));
            }
        }
        paintImmediately(0,0,getWidth(),getHeight());
      }
   }