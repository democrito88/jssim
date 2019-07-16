package mse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.openimaj.image.MBFImage;
import org.openimaj.video.xuggle.XuggleVideo;

public class MeanSquareError {
    public MeanSquareError(){
        
    }
    
    // calcula o EQM do valor de uma faixa de cor (vermelho, verde ou azul)
    // de um frame
    public double erroMedioQuadradoUmaFaixa(double[][] original, double[][] processado) {
        if (original.length != processado.length) {
            throw new IllegalArgumentException(String.format("As dimensões das matrize diferem: %d != %d.", original.length, processado.length));
        }

        int qtdeLinhas = original.length;
        int qtdeColunas = original[0].length;
        double rss = 0.0f;
        
        for (int i = 0; i < qtdeLinhas; i++) {
            for (int j = 0; j < qtdeColunas; j++) {
                rss += Math.pow(original[i][j] - processado[i][j], 2.0f);
            }
        }
        return rss / ((qtdeColunas)*(qtdeLinhas));
    }
    
    
    // atua apenas na região afetada pelo algoritmo de
    // aumento de vídeo
    public double[][][] mbfimageParaMatrizDouble(MBFImage imagem){
        int faixas = 3;
        
        short primeiraLinha = 383 - 32;
        short ultimaLinha = 511 - 32;
        short primeiraColuna = 767;
        short ultimaColuna = 1023;
        
        double[][][] resultado = new double[faixas][(ultimaLinha - primeiraLinha)][(ultimaColuna - primeiraColuna)];
        for(int k=0; k<faixas; k++){
            for(int i=primeiraLinha; i<ultimaLinha; i++){
                for(int j=primeiraColuna; j<ultimaColuna; j++){
                    resultado[k][i - primeiraLinha][j - primeiraColuna] = imagem.getBand(k).getPixel(j, i)*256;
                }
            }
        }
        return resultado;
    }
    
    // calcula o EQM de um frame colorido
    public double[] erroMedioQuadradoMultiFaixa(MBFImage original, MBFImage processado){
        
        double[][][] originalEmDouble = mbfimageParaMatrizDouble(original);
        double[][][] processadoEmDouble = mbfimageParaMatrizDouble(processado);
        double[] mse = new double[3];
        
        for(int k=0; k<3; k++){
            mse[k] = erroMedioQuadradoUmaFaixa(originalEmDouble[k], processadoEmDouble[k]);
        }
        
        return mse;
    }
    
    // separa o vídeo em uma coleção de frames
    public MBFImage[] videoParaFrames(XuggleVideo video){
        MBFImage[] frames = new MBFImage[(int)video.countFrames()];
        int i = 0;
        for(MBFImage frame : video){
            frames[i] = frame;
            i++;
        }
        
        return frames;
    }
    
    // transforma os frames dos vídeos em matrizes de double
    public double[][] framesParaEMQ(MBFImage[] framesOriginal, MBFImage[] framesProcessado){
        System.out.println("Frames originais: "+framesOriginal.length+", Frames processados: "+framesProcessado.length);
        int tamanho = framesProcessado.length - 3;
        double[][] emqs = new double[tamanho][3];
        
        for(int j=0; j<tamanho; j++){
                System.out.println("frame: "+j);
                emqs[j] = erroMedioQuadradoMultiFaixa(framesOriginal[j], framesProcessado[j]);
            }
        
        return emqs;
    }
    
    // exibe o EQM calculado
    public void imprimeEMQ(double[][] emq){
        int linhas = emq.length;
        
        System.out.println("mse.MeanSquareError.imprimeEMQ()");
        System.out.println("Frame\tR\tG\tB");
        for(int i=0; i<linhas; i++){
            System.out.println(""+i+"\t"+emq[i][0]+"\t"+emq[i][1]+"\t"+emq[i][2]);
        }
    }
    
    // escreve o EQM em um arquivo de texto (.txt)
    public void escreveArquivoEMQ(String processado, double[][] emq) throws IOException{
        processado = processado.replaceAll(".mov", "_MSE.txt");
        FileWriter fr = new FileWriter(processado);
        BufferedWriter br = new BufferedWriter(fr);
        PrintWriter out = new PrintWriter(br);
        int tamanho = emq.length;
        
        out.write("Frame\tR\tG\tB\n");
        for(int i=0; i<tamanho; i++){
            out.write(i+"\t"+emq[i][0]+"\t"+emq[i][1]+"\t"+emq[i][2]+"\n");
        }
        out.close();
    }
    
    // calcula o EQM do video
    public void calculaEMQVideo(XuggleVideo original, XuggleVideo processado, String nomeProcessado) throws IOException{
        MBFImage[] framesImagemOriginal= videoParaFrames(original);
        System.out.println("Tamanho do original: "+framesImagemOriginal.length);
        MBFImage[] framesImagemProcessado = videoParaFrames(processado);
        System.out.println("Tamanho do processado: "+framesImagemProcessado.length);
        
        double[][] doubleFrames = framesParaEMQ(framesImagemOriginal, framesImagemProcessado);
        
        imprimeEMQ(doubleFrames);
        escreveArquivoEMQ(nomeProcessado, doubleFrames);
    }
}
