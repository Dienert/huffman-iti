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
	private static String buffer = "";
	
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

		int caracteresTotais = data.readInt();
		
		int simbolosDiferentes = data.readInt();
		
		byte simbolosTamanho = data.readByte();
			
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
					String ext = absolutePathResult.substring(lastIndx+1, 
												absolutePathResult.length()-1);
					absolutePathResult = nome+".decodificado"+ext;
				}
			}
		}
		
		FileOutputStream fWriter = new FileOutputStream(absolutePathResult);
		BufferedOutputStream buffWriter = new BufferedOutputStream(fWriter);  
		DataOutputStream out = new DataOutputStream(buffWriter);
		
		byte[] assinatura = new byte[1024];  
		int nBytes;
		
		while((nBytes = data.read(assinatura)) != -1) {
			if(simbolosTamanho == 8) {
				for (int i=0; i<nBytes; i++) {
					String code = hashCodes.get(new String(""+(char)(assinatura[i] & 0xFF)));
					boolean isLastByte = ((i+1) == nBytes) && (nBytes != 1024);
					buffer = save(code, buffer, out, isLastByte); //Atualizando o buffer
				}
			} else {
				for (int i=0; i<nBytes; i+=2) {
					String code = hashCodes.get(new String(""+(char)(assinatura[i] & 0xFF)+
							(i+1 == nBytes? "" : (char)(assinatura[i+1] & 0xFF))));
					boolean isLastByte = ((i+1) == nBytes) && (nBytes != 1024);
					buffer = save(code, buffer, out, isLastByte); //Atualizando o buffer
				}
			}
		}
		out.close();
		
		
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Não foi possível ler o arquivo");
			System.exit(1);
		}
	}
	
	
	
}

