package mse;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssim.SsimException;
import org.openimaj.video.xuggle.XuggleVideo;

public class ExecutaMSE {
    
    // calcula o EQM de todos os arquivos em um diretório que possuem a extensão ".mov"
    public static void listarArquivosPorPasta(final File folder, String nome1, MeanSquareError mse) throws SsimException, IOException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println("Entrando no diretório: "+fileEntry.getName());
                System.out.println("Ele possui "+fileEntry.listFiles().length+" arquivos");
                listarArquivosPorPasta(fileEntry, nome1, mse);
            } else if(Files.getFileExtension(fileEntry.getPath()).equals("mov")){
                System.out.println("Original: "+nome1);
                System.out.println("Arquivo da vez: "+fileEntry.getPath());
                mse.calculaEMQVideo(new XuggleVideo(new File(nome1)), new XuggleVideo(new File(fileEntry.getAbsolutePath())), fileEntry.getName());
            }
        }
    }
    
    public static void main(String[] args){
        String arquivo1 = "Pulso.mov";
        
        // configura o diretório dos vídeos
        final File folder = new File("/home/rodrigo/NetBeansProjects/structural-similarity-master/filtro");
        System.out.println("O diretório possui "+folder.listFiles().length+" arquivos");
        for(File arquivo: folder.listFiles()){
            System.out.println(""+arquivo.getName());
        }
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
        
        try {
            MeanSquareError mse = new MeanSquareError();
            listarArquivosPorPasta(folder, arquivo1, mse);
        } catch (IOException ex) {
            Logger.getLogger(ExecutaMSE.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SsimException ex) {
            Logger.getLogger(ExecutaMSE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
