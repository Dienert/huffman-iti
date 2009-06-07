package br.ufpb.iti;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class Huffman {

	private static Hashtable<String, Integer> hashFrequencia = new Hashtable<String, Integer>();
	private static Hashtable<String, No> hashNos = new Hashtable<String, No>();
	private static ListaOrdenada lista = new ListaOrdenada();
	
	private static String absolutePath = "";
	private static String type = "-1b";

	public static void printSyntax() {
		System.out.println("Usage: java Huffman file [options]");
		System.out.println("\nwhere options are:");
		System.out.println("\n-1b		read caracters as 1 byte:");
		System.out.println("\n-2b		read caracters as 2 bytes:");
		System.exit(1);
	}
	
	public static void main(String[] args) {
		
		int size = args.length;
		String fileName = "";
		
		if (size >= 1)
			fileName = args[0];
		if(size == 2)
			type = args[1];
		
		if (fileName.equals("") || !(type.equals("-1b") || type.equals("-2b"))) {
			printSyntax();
		}
		
		try {
			//Pega o caminho absoluto do arquivo
			absolutePath = ClassLoader.getSystemResource(fileName).toString().replace("file:", "");
			FileInputStream fReader = new FileInputStream(absolutePath);
			BufferedInputStream buffReader = new BufferedInputStream(fReader);  
			DataInputStream data = new DataInputStream(buffReader);  
			
			byte[] assinatura = new byte[1024];  
			int nBytes;
			while((nBytes = data.read(assinatura)) != -1) {
				if(type.equals("-1b")) {
					for (int i=0; i<nBytes; i++) {
						//Esta linha converte um byte para char e depois para String
						//e insere o caracter na hash de frenquencias
						updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)));
					}
				} else if (type.equals("-2b")) {
					for (int i=0; i<nBytes; i+=2) {
						//Esta linha converte dois bytes para 2 chars e depois para String
						//e insere o caracter na hashtable de frenquencias
						updateHashTableFreq(new String(""+
								(char)(assinatura[i] & 0xFF)+
								(i+1 == nBytes? "" : (char)(assinatura[i+1] & 0xFF))));
						//caso a linha i+1 seja igual ao numero de bytes lidos, entao
						//o segundo caracter nao existe, e serah inserido apenas 1 caracter na hashtable
					}
				}
			}
			
			Enumeration<String> enumeration = hashFrequencia.keys();
			lista.constroiLista(hashFrequencia); //Constroi a lista ordenada da HashTable
			No raiz = No.constroiArvore(lista);
			raiz.mostraArvore();
			
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo nao encontrado");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static void updateHashTableFreq(String caracter) {
		if (hashFrequencia.containsKey(caracter)) {
			hashFrequencia.put(caracter, (Integer)hashFrequencia.get(caracter)+1);
		} else {
			hashFrequencia.put(caracter, 1);
		}
	}
	
	public static void codification() {
		try {
			FileInputStream fReader = new FileInputStream(absolutePath);
			BufferedInputStream buffReader = new BufferedInputStream(fReader);  
			DataInputStream data = new DataInputStream(buffReader);  
			
			byte[] assinatura = new byte[1024];  
			int nBytes;
			while((nBytes = data.read(assinatura)) != -1) {
				if(type.equals("-1b")) {
					for (int i=0; i<nBytes; i++) {
						//Esta linha converte um byte para char e depois para String
						//e insere o caracter na hash de frenquencias
						updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)));
					}
				} else if (type.equals("-2b")) {
					for (int i=0; i<nBytes; i+=2) {
						//Esta linha converte dois bytes para 2 chars e depois para String
						//e insere o caracter na hashtable de frenquencias
						updateHashTableFreq(new String(""+
								(char)(assinatura[i] & 0xFF)+
								(i+1 == nBytes? "" : (char)(assinatura[i+1] & 0xFF))));
						//caso a linha i+1 seja igual ao numero de bytes lidos, entao
						//o segundo caracter nao existe, e serah inserido apenas 1 caracter na hashtable
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
