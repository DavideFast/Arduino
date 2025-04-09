package main;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSV_creator {
    
    private String nome;
    private List<Float> valori;
    private List<Integer> tempi;

    public void addValore(String valore){
        String exactly = valore.replace("-", "");
        if(valore.contains("-"))
            valori.add(-Float.parseFloat(exactly));
        else
            valori.add(Float.parseFloat(exactly));
    }

    public void addTempo(String tempo){
        tempi.add(Integer.parseInt(tempo));
    }

    public void pulisci(){
        valori.removeAll(valori);
        tempi.removeAll(tempi);
    }

    public void rename(String nome){
        this.nome = nome;
    }

    public boolean salvaFile() throws IOException{
        String userHome = System.getProperty("user.home");
        String desktopPath = Paths.get(userHome, "Desktop").toString();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String giorno = formatter.format(date);
        
        Path path = Paths.get(desktopPath+"/Test "+giorno);
        if(!new File(path.toString()).isDirectory())
            Files.createDirectory(path);
        String filePath = path.toString()+"/"+nome+".csv";
        XYLineChartExample img = new XYLineChartExample(valori,tempi, nome);
        File file = new File(filePath);
        if (file.exists())
            return false; 

        String[] header = {"Valori", "Tempi"};

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append(header[0]+" ");
            for (int i=0;i<valori.size();i++) {
                writer.append(valori.get(i).toString().replace(".",",")).append(" ");
            }
            writer.append("\n").append(header[1]+" ");
            for (int i=0;i<valori.size();i++) {
                writer.append(tempi.get(i).toString()).append(" ");
            }
            System.out.println("File CSV creato con successo!");
            return true;
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file CSV: " + e.getMessage());
            return false;
        }
    }

    public CSV_creator(String nome){
        this.nome = nome;
        valori = new ArrayList<>();
        tempi = new ArrayList<>();
    }


}