package jssim;

/**
 *
 * @author rodrigo
 */
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openimaj.image.MBFImage;

public class ExecutaSSIM{
    // calcula o SSIM em cada arquivo de um diretório
    public static void listarArquivosPorPasta(final File folder, String nome1) throws SsimException, IOException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println("Entrando no diretório: "+fileEntry.getName());
                System.out.println("Ele possui "+fileEntry.listFiles().length+" arquivos");
                listarArquivosPorPasta(fileEntry, nome1);
            } else if(Files.getFileExtension(fileEntry.getPath()).equals("mov")){
                System.out.println("Arquivo da vez: "+fileEntry.getPath());
                
                calcularSssimDoArquivo(nome1, fileEntry.getAbsolutePath());
            }
        }
    }
    
    // calcular a SSIM de um arquivo
    public static void calcularSssimDoArquivo(String nomeOriginal, String nomeProcessado) throws SsimException, IOException{
        System.out.println("Calculando a SSIM do vídeo: "+nomeProcessado);
        File video1 = new File(nomeOriginal);
        File video2 = new File(nomeProcessado);
        
        SSIMXuggleVideo sxv = new SSIMXuggleVideo(video1, video2);
        System.out.println("Quantidade de frames original: "+sxv.videoOriginal.countFrames());
        System.out.println("Quantidade de frames processado: "+sxv.videoProcessado.countFrames());
        MBFImage[] framesSeparadosOriginais = sxv.separaEmFrames(sxv.videoOriginal);
        MBFImage[] framesSeparadosProcessados = sxv.separaEmFrames(sxv.videoProcessado);
        
        // calcula o SSIM entre os vídeos
        double[] arraySSIM = sxv.framesParaSSIM(framesSeparadosOriginais, framesSeparadosProcessados);
        sxv.imprimeArraySsim(arraySSIM);
        sxv.imprimeArraySsimEmArquivo(nomeProcessado, arraySSIM);
    }
    
    public static void main(String[] args){
        try {
            // vídeo original
            String nome1 = "Pulso.mov";
            
            // diretório onde estão os vídeos
            final File folder = new File("/home/rodrigo/NetBeansProjects/structural-similarity-master/filtro");
            System.out.println("O diretório possui "+folder.listFiles().length+" arquivos");
            for(File arquivo: folder.listFiles()){
                System.out.println(""+arquivo.getName());
            }
            Scanner sc = new Scanner(System.in);
            sc.nextInt();
            
            // calcula os SSIMs dos vídeos no diretório
            listarArquivosPorPasta(folder, nome1);
        } catch (SsimException ex) {
            Logger.getLogger(ExecutaSSIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExecutaSSIM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}