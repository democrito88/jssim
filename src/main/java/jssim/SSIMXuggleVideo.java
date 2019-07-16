package jssim;

/**
 *
 * @author rodrigo
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.openimaj.image.MBFImage;
import org.openimaj.video.xuggle.XuggleVideo;

public class SSIMXuggleVideo{
	
	public int qtdeFrames;
	public double[] quadrosSSIM;
	public XuggleVideo videoOriginal;
	public XuggleVideo videoProcessado;
	
	public SSIMXuggleVideo(File videoOriginal, File videoProcessado){
            this.videoOriginal = new XuggleVideo(videoOriginal);
            this.videoProcessado = new XuggleVideo(videoProcessado);
            this.qtdeFrames = 61;
            this.quadrosSSIM = new double[61];
	}
	
	//Separa o video em quadros
	public MBFImage[] separaEmFrames(XuggleVideo video){
            System.out.println("separaEmFrames()");
            MBFImage[] arrayImagens = new MBFImage[this.qtdeFrames];
            int i = 0;
            for(MBFImage frame: video){
                arrayImagens[i] = frame;
                i++;
                System.out.println("frame: "+i);
                if(i == this.qtdeFrames){break;}
            }
            return arrayImagens;
	}
	
	//Criar array com SSIMs de cada quadro
	public double[] framesParaSSIM(MBFImage[] arrayframesOriginal, MBFImage[] arrayframesProcessado)throws SsimException, IOException{
            SsimCalculator ssimc;
            int quantidadeDeFrames = arrayframesProcessado.length;
            System.out.println("Quantidade de frames usada: "+quantidadeDeFrames);
            double[] arraySSIM = new double[quantidadeDeFrames];
            for(int i = 0; i < quantidadeDeFrames - 4; i++){
                System.out.println("i:"+i);
                
                
                ssimc = new SsimCalculator(arrayframesOriginal[i]);
                arraySSIM[i] = ssimc.compareMBFImageTo(arrayframesProcessado[i]);
                
            }
            System.out.println("Tamanho do array SSIM: "+arraySSIM.length);
            return arraySSIM;
	}
        
        // exibe o resultado dos SSIMs
        public void imprimeArraySsim(double[] arraySSIM){
            short comprimento = (short)arraySSIM.length;
            System.out.println("Frame\tSSIM");
            for(int i=0; i<comprimento; i++){
                System.out.println(i+":\t"+arraySSIM[i]);
            }
        }
        
        // escreve o resultado calculado em um arquivo
        public void imprimeArraySsimEmArquivo(String processado, double[] arraySSIM) throws IOException{
            processado = processado.replaceAll(".mov", "_SSIM.txt");
            FileWriter fr = new FileWriter(processado);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter out = new PrintWriter(br);
            for(int i=0; i<arraySSIM.length; i++){
                if(arraySSIM[i] != 0.0){
                    out.write(arraySSIM[i]+"");
                    out.write("\n");
                }   
            }
            out.close();
        }
}
