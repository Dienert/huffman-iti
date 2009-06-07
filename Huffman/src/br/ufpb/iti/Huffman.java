package br.ufpb.iti;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class Huffman {

	private static Hashtable<String, Integer> hashFrequencia = new Hashtable<String, Integer>();
	private static Hashtable<String, No> hashNos = new Hashtable<String, No>();
	private static ListaOrdenada lista = new ListaOrdenada();
	
	private static String absolutePath = "";
	private static String type = "-1b";
	
	private static final String ONE_BYTE = "-1b";
	private static final String TWO_BYTES = "-2b";
	
	private static String buffer = "";

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
		
		if (fileName.equals("") || !(type.equals(ONE_BYTE) || type.equals(TWO_BYTES))) {
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
				if(type.equals(ONE_BYTE)) {
					for (int i=0; i<nBytes; i++) {
						//Esta linha converte um byte para char e depois para String
						//e insere o caracter na hash de frenquencias
						updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)));
					}
				} else if (type.equals(TWO_BYTES)) {
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
				if(type.equals(ONE_BYTE)) {
					for (int i=0; i<nBytes; i++) {
						No no = hashNos.get(new String(""+(char)(assinatura[i] & 0xFF)));
						String code = getCode(no);
						save(code);
					}
				} else if (type.equals(TWO_BYTES)) {
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
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getCode(No no) {
		No aux = no;
		String codigo = "";
		while(aux.getPai() != null) {
			if (aux.ehEsq)
				codigo = codigo+"0";
			else
				codigo = codigo+"1";
			aux = aux.getPai();
		}
		return invertCode(codigo);
	}
	
	public static String invertCode(String code) {
		char[] codeChars = code.toCharArray();
		char[] newCode = code.toCharArray();
		for (int i = 0, j = codeChars.length-1; i < codeChars.length; i++, j--)
			newCode[j] = codeChars[i];
		return new String(newCode);
	}
	
	public static void save(String code) {
		try {
			FileOutputStream fWriter = new FileOutputStream(absolutePath);
			BufferedOutputStream buffWriter = new BufferedOutputStream(fWriter);  
			DataOutputStream data = new DataOutputStream(buffWriter);
			
			if (type.equals(ONE_BYTE)) {
				if (buffer.length() < 8) {
					int size = buffer.length();
					int codeSize = code.length();
					//Se o espaço que falta no buffer é menor que o codigo passado
					//a codigo eh quebrado e o restante eh colocado no buffer
					if (8-size < codeSize) {
						
					}else {
						
					}
				} 
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
			System.exit(0);
		}
		
	}
	
	public static String divideCode(String code, int size, String rest) {
		char[] part = new char[size];
		code.getChars(0, size, part, 0);
		char[] restChars = new char[8-size];
		code.getChars(size, code.length(), restChars, 0);
		rest = new String(restChars);
		return new String(part);
	}
	
}
