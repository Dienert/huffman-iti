package br.ufpb.iti;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Decoder {
	
	private static String absolutePath = "";
	private static String absolutePathResult = "";
	private static byte[] assinatura = new byte[1024];	
	
	private static int caracteresTotais = 0;
	
	private static int simbolosDiferentes = 0;
	
	private static byte simbolosTamanho;
	
	/**
	 * 
	 * Imprime a sintaxe correta de uso do programa
	 * 
	 */
	public static void printSyntax() {
		System.out.println("Usage: java HuffmanDecoder file.adh ");
				
		System.exit(1);
	}
	
	
	public static void main(String[] args) {
		
		System.out.println("aqui");
		int size = args.length;
		String fileName = "";
				
		if (size >= 1){
			fileName = args[0];
		}
		
		if (fileName.equals("")) {
			printSyntax();
		}
		
		//Pega o caminho absoluto do arquivo
		absolutePath = ClassLoader.getSystemResource(fileName).toString().replace("file:", "");
		FileInputStream fReader;
		try {
			fReader = new FileInputStream(absolutePath);
		
		BufferedInputStream buffReader = new BufferedInputStream(fReader);  
		DataInputStream data = new DataInputStream(buffReader);  

		caracteresTotais = data.readInt();
		
		simbolosDiferentes = data.readInt();
		
		simbolosTamanho = data.readByte();
			
		System.out.println("Total de símbolos: "+caracteresTotais);
		System.out.println("Símbolos diferentes: "+simbolosDiferentes);
		System.out.println("Tamanho dos Símbolos: "+simbolosTamanho);
		
		ListaOrdenada lista = new ListaOrdenada();
		
		if (simbolosTamanho == 8) {
			for (int i=0; i<simbolosDiferentes; i++) {
				No novo = new No();
				char simbol = (char)data.readByte();
				int freq = data.readInt();
				novo.setCaracter(simbol+"");
				novo.setFreq(freq);
				lista.insere(novo);
			}
		} else {
			for (int i=0; i<simbolosDiferentes; i++) {
				No novo = new No();
				char simbol1 = (char)data.readByte();
				char simbol2 = (char)data.readByte();
				int freq = data.readInt();
				novo.setCaracter(simbol1+simbol2+"");
				novo.setFreq(freq);
				lista.insere(novo);
			}
		}
		
		No raiz = No.constroiArvore(lista);
		
		System.out.println("\n");
		
		No.percorreInOrdem(raiz);
		
		System.out.println("\n");
		
		if (!absolutePath.equals("")) {
			int lastIndx = absolutePath.lastIndexOf(".");
			if (lastIndx == -1) {
				System.out.println("O arquivo não foi comprimido com este programa!");
				System.exit(1);
			} else {
				absolutePathResult = absolutePath.substring(0, lastIndx);
				lastIndx = absolutePathResult.lastIndexOf(".");
				if (lastIndx == -1) {
					absolutePathResult = absolutePathResult+".decodificado";
				} else {
					String nome = absolutePathResult.substring(0, lastIndx);
					String ext = absolutePathResult.substring(lastIndx, 
												absolutePathResult.length());
					absolutePathResult = nome+".decodificado"+ext;
				}
			}
		}
		
		FileOutputStream fWriter = new FileOutputStream(absolutePathResult);
		BufferedOutputStream buffWriter = new BufferedOutputStream(fWriter);  
		DataOutputStream out = new DataOutputStream(buffWriter);
		
		decode(data, out, raiz);
		
		out.close();
		
		
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Não foi possível ler o arquivo");
			System.exit(1);
		}
	}
	
	public static void decode(DataInputStream dataIn, DataOutputStream dataOut, No raiz) {
		
		int nBytes;
		
		try {
			No aux = raiz;
			int nSimbolosDecodificados = 0;
			while((nBytes = dataIn.read(assinatura)) != -1) {
				int i = 0;
				int bitIndex = 0;

				while (nSimbolosDecodificados < caracteresTotais && i < nBytes) {
					
					String simbolo = "";
					while (!aux.ehFolha() && bitIndex < 8) {
						char bit = Huffman.getFormatedCode(assinatura[i], 8).
										charAt(bitIndex);
						if (bit == '0')
							aux = aux.getFilhoEsq();
						else
							aux = aux.getFilhoDir();
						bitIndex++;
						
					}
					if (aux.ehFolha()) {
						simbolo = aux.getCaracter();
						dataOut.writeByte(simbolo.charAt(0));
						if (simbolosDiferentes == 8 && simbolo.length() == 2) {
							dataOut.writeByte(simbolo.charAt(1));
						}
						aux = raiz;
						nSimbolosDecodificados ++;
					} else {
						i++;
						bitIndex = 0;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Falha de leitura do arquivo");
			System.exit(1);
		}
	}
	
}

