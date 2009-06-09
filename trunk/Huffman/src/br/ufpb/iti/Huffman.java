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
	private static Hashtable<String, String> hashCodes = new Hashtable<String, String>();
	private static ListaOrdenada lista = new ListaOrdenada();
	
	private static String absolutePath = "";
	private static String absolutePathResult = "";
	private static String type = "-1b";
	
	private static final String ONE_BYTE = "-1b";
	private static final String TWO_BYTES = "-2b";
	
	private static String buffer = "";

	/**
	 * 
	 * Imprime a sintaxe correta de uso do programa
	 * 
	 */
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
						updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)+
								(i+1 == nBytes? "" : (char)(assinatura[i+1] & 0xFF))));
						//caso a linha i+1 seja igual ao numero de bytes lidos, entao
						//o segundo caracter nao existe, e serah inserido apenas 1 caracter na hashtable
					}
				}
			}
			
			lista.constroiLista(hashFrequencia); //Constroi a lista ordenada da HashTable
		
			No raiz = No.constroiArvore(lista);
			
			if(raiz != null)
				No.mostraArvore(raiz);
			else System.out.println("raiz não existe");
			
			//Gera um hash de simbolos e seus codigos
			generateHashSimbolsAndCodes(raiz, "");
			
			//Inicia codificacao da mensagem
			codification();
			
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo nao encontrado");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * Insere caracter numa hashTable incrementando de 1 o seu valor, caso este já
	 * exista.
	 * @param caracter Símbolo a ser inserido na hashtable
	 */
	public static void updateHashTableFreq(String caracter) {
		if (hashFrequencia.containsKey(caracter)) {
			hashFrequencia.put(caracter, (Integer)hashFrequencia.get(caracter)+1);
		} else {
			hashFrequencia.put(caracter, 1);
		}
	}
	
	/**
	 * Gera um hash contendo os símbolos e o seu código binário, onde o símbolo é
	 * a chave do hash para seu respectivo código
	 * @param raiz Raiz da árvore de Huffman
	 * @param code Código inicial da raiz que será usado recursivamente
	 */
	public static void generateHashSimbolsAndCodes(No raiz, String code) {
		if (raiz != null) {
			generateHashSimbolsAndCodes(raiz.getFilhoEsq(), code+"0");
			generateHashSimbolsAndCodes(raiz.getFilhoDir(), code+"1");
			if (raiz.getFilhoEsq() == null && raiz.getFilhoDir() == null) {
				hashCodes.put(raiz.getCaracter(), code);
			}
		}
	}
	
	/**
	 * 
	 * Codifica a mensagem com base na hashtable que contem os códigos de cada 
	 * símbolo. Automaticamente o método faz a diferenciação entre os dois tipos
	 * de codificação: usando 1 byte ou 2 bytes.
	 * 
	 */
	public static void codification() {
		try {
			FileInputStream fReader = new FileInputStream(absolutePath);
			BufferedInputStream buffReader = new BufferedInputStream(fReader);  
			DataInputStream dataIn = new DataInputStream(buffReader);  
			
			if (!absolutePath.equals("")) {
				int lastIndx = absolutePath.lastIndexOf(".");
				absolutePathResult = absolutePath.substring(0, lastIndx+1)+"adh";
			} else {
				absolutePathResult = "/tmp/teste.adh";
			}
			
			FileOutputStream fWriter = new FileOutputStream(absolutePathResult);
			BufferedOutputStream buffWriter = new BufferedOutputStream(fWriter);  
			DataOutputStream dataOut = new DataOutputStream(buffWriter);
			
			byte[] assinatura = new byte[1024];  
			int nBytes;
			while((nBytes = dataIn.read(assinatura)) != -1) {
				if(type.equals(ONE_BYTE)) {
					for (int i=0; i<nBytes; i++) {
						String code = hashCodes.get(new String(""+(char)(assinatura[i] & 0xFF)));
						save(code, buffer, dataOut, (i+1) == nBytes);
						
					}
				} else if (type.equals(TWO_BYTES)) {
					for (int i=0; i<nBytes; i+=2) {
						String code = hashCodes.get(new String(""+(char)(assinatura[i] & 0xFF)+
								(i+1 == nBytes? "" : (char)(assinatura[i+1] & 0xFF))));
						save(code, buffer, dataOut, (i+1) == nBytes);
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
	
//	public static String getCode(No no) {
//		No aux = no;
//		String codigo = "";
//		while(aux.getPai() != null) {
//			if (aux.getEhEsq())
//				codigo = codigo+"0";
//			else
//				codigo = codigo+"1";
//			aux = aux.getPai();
//		}
//		return invertCode(codigo);
//	}
//	
//	public static String invertCode(String code) {
//		char[] codeChars = code.toCharArray();
//		char[] newCode = code.toCharArray();
//		for (int i = 0, j = codeChars.length-1; i < codeChars.length; i++, j--)
//			newCode[j] = codeChars[i];
//		return new String(newCode);
//	}
	
	/**
	 * Salva um código binário em arquivo. Utiliza-se um buffer para que os dados
	 * sejam salvos de byte em byte.
	 * @param code
	 * @param isLastByte
	 * @return O numero de bits que não são utilizados no ultimo byte
	 */
	public static int save(String code, String buffer, DataOutputStream data, boolean isLastByte) {
		
		int bufferSize = buffer.length();
		int codeSize = code.length();
		int freeSpace = 8-bufferSize;
		
		try {
			if (freeSpace < codeSize) {
				//Se o espaço que falta no buffer é menor que o tamanho do codigo 
				//passado, entao o codigo eh quebrado e o restante eh colocado no buffer
				String[] result = divideCode(code, freeSpace);
				buffer = buffer+result[0];
				//Este save garante que o code apresenta 8 bits
				//para ser salvo, o buffer eh zerado
				save(buffer, "", data, isLastByte);
				buffer = result[1]; //coloca o restante no buffer
				if (buffer.length() >= 8) {
					save(buffer, "", data, isLastByte);
				}
			} else if (freeSpace >= codeSize) {
				buffer = buffer+code;
				if (buffer.length() == 8) {
					System.out.println("codigo salvo: "+buffer);
					data.writeByte(getByte(buffer));
					buffer = "";
				} else if (buffer.length() < 8 && isLastByte) {
					System.out.println("ultimo codigo salvo: "+buffer);
					data.writeByte(getByte(buffer));
					buffer = "";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
				}
		return 0;
	}
	
	/**
	 * 
	 * Retorna o código decimal da String que representa esse número em binário
	 * @param code Código em binário
	 * @return Número decimal que representa o código binário
	 * 
	 */
	public static int getByte(String code) {
		int result =  0;
		for (int i=0, j=code.length()-1; i<code.length();i++, j--) {
			if (code.charAt(j) == '1')
				result += Math.pow(2, i);
		}
		return result;
	}
	
	/**
	 * 
	 * Divide uma String em duas partes, a primeira contendo o número de caracteres
	 * especificado por size e a segunda contendo o restante da String inicial
	 * @param code String que será dividida
	 * @param size Tamanho que a primeira parte da String vai ter
	 * @return Um array de String com duas Strings, no índice 0 a primeira parte
	 * e no índice 1, a segunda parte
	 * 
	 */
	public static String[] divideCode(String code, int size) {
		char[] part = new char[size];
		code.getChars(0, size, part, 0);
		char[] restChars = new char[code.length()-size];
		code.getChars(size, code.length(), restChars, 0);
		String[] result = {new String(part), new String(restChars)};
		return result;
	}
	
}
