package jssim;

/**
 *
 * @author rodrigo
 */
import java.io.File;
import java.io.IOException;
import org.openimaj.image.MBFImage;
import org.openimaj.video.xuggle.XuggleVideo;

public class SSIMVideo{
	
	public short qtdeFrames;
	public double[] quadrosSSIM;
	public File videoOriginal;
	public File videoProcessado;
	
	public SSIMVideo(File videoOriginal, File videoProcessado){
		this.videoOriginal = videoOriginal;
		this.videoProcessado = videoProcessado;
		this.qtdeFrames = 64;
		this.quadrosSSIM = new double[this.qtdeFrames];
	}
	
	//Separar o vídeo em quadros
	public MBFImage[] separaEmFrames(File video){
            XuggleVideo xv = new XuggleVideo(video);
            MBFImage[] arrayImagens = new MBFImage[this.qtdeFrames];
            int i = 0;
            for(MBFImage frame: xv){
                arrayImagens[i] = frame;
                i++;
            }
            return null;
	}
	
	//Cria array com SSIMs de cada quadro
	public double[] framesParaSSIM(MBFImage[] arrayframesOriginal, MBFImage[] arrayframesProcessado)throws SsimException, IOException{
		SsimCalculator ssimc;
		double[] arraySSIM = new double[this.qtdeFrames];
		for(int i = 0; i < this.qtdeFrames; i++){
			ssimc = new SsimCalculator(arrayframesOriginal[i]);
			arraySSIM[i] = ssimc.compareMBFImageTo(arrayframesProcessado[i]);
		}
		
		return arraySSIM;
	}
}
