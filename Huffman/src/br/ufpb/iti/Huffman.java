package br.ufpb.iti;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
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
		
		int sizeParams = args.length;
		String fileName = "";
		
		currentTime();
		if (sizeParams >= 1)
			fileName = args[0];
		if(sizeParams == 2)
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
			int lidos = 0;
			while((nBytes = data.read(assinatura)) != -1) {
				if(type.equals(ONE_BYTE)) {
					for (int i=0; i<nBytes; i++) {
						//Esta linha converte um byte para char e depois para String
						//e insere o caracter na hash de frenquencias
						updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)));
						lidos++;
					}
				} else if (type.equals(TWO_BYTES)) {
					for (int i=0; i<nBytes; i+=2) {
						//Esta linha converte dois bytes para 2 chars e depois para String
						//e insere o caracter na hashtable de frenquencias
						updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)+
								(i+1 == nBytes? "" : (char)(assinatura[i+1] & 0xFF)))); 
						//para que danado serve esse 0xFF ... ele significa true... então é par
						//caso a linha i+1 seja igual ao numero de bytes lidos, entao
						//o segundo caracter nao existe, e serah inserido apenas 1 caracter na hashtable
						lidos++;
					}
				}
			}
			
			//array que armazena o numero de simbolos da mensagem original no
			//indice 0 e o numero de simbolos diferentes no indice 1
			int[] counters;
			
			//Constroi a lista ordenada da HashTable
			counters = lista.constroiLista(hashFrequencia); 
			
			System.out.println("Numero de símbolos da mensagem: "+counters[0]);
			
			System.out.println("Numero de símbolos diferentes da mensagem: "+counters[1]);
			
			System.out.println("Entropia: "+calculaEntropia(lista, counters[0]));
			
			No raiz = No.constroiArvore(lista);
			
			System.out.println("\n");
			
			No.percorreInOrdem(raiz);
			
			System.out.println("\n");
			
			//Gera um hash de simbolos e seus codigos
			updateHashSimbolsAndCodes(raiz, "");
			
//			System.out.println(hashFrequencia);
//			System.out.println(hashCodes);
			
			//Inicia codificacao da mensagem
			codification();
			
			putHeader(counters, hashFrequencia, absolutePathResult);
			
			currentTime();
			
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
	 * 
	 * Atualiza um hash global contendo símbolos e códigos binários, onde o símbolo é
	 * a chave do hash para seu respectivo código
	 * @param raiz Raiz da árvore de Huffman
	 * @param code Código inicial da raiz que será usado recursivamente
	 * 
	 */
	public static void updateHashSimbolsAndCodes(No raiz, String code) {
		if (raiz != null) {
			updateHashSimbolsAndCodes(raiz.getFilhoEsq(), code+"0");
			updateHashSimbolsAndCodes(raiz.getFilhoDir(), code+"1");
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
				if (lastIndx == -1)
					absolutePathResult = absolutePath+".adh";
				else absolutePathResult = absolutePath+".adh";
			} else {
				absolutePathResult = "/tmp/teste.adh";
			}

			/*FileWriter fWriter = new FileWriter(absolutePathResult);
			BufferedWriter out = new BufferedWriter(fWriter);
*/
			
			FileOutputStream fWriter = new FileOutputStream(absolutePathResult);
			BufferedOutputStream buffWriter = new BufferedOutputStream(fWriter);  
			DataOutputStream out = new DataOutputStream(buffWriter);
			
			byte[] assinatura = new byte[1024];  
			int nBytes;

			while((nBytes = dataIn.read(assinatura)) != -1) {
				if(type.equals(ONE_BYTE)) {
					for (int i=0; i<nBytes; i++) {
						String code = hashCodes.get(new String(""+(char)(assinatura[i] & 0xFF)));
						boolean isLastByte = ((i+1) == nBytes) && (nBytes != 1024);
						buffer = save(code, buffer, out, isLastByte); //Atualizando o buffer
					}
				} else if (type.equals(TWO_BYTES)) {
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
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void putHeader(int[] counters, Hashtable<String, Integer>hashFreq, String absolutePath) {
		try {
			FileInputStream fReader = new FileInputStream(absolutePath);
			BufferedInputStream buffReader = new BufferedInputStream(fReader);  
			DataInputStream dataIn = new DataInputStream(buffReader);  
			
			/*FileWriter fWriter = new FileWriter(absolutePath+".tmp");
			BufferedWriter out = new BufferedWriter(fWriter);*/
			
			FileOutputStream fWriter = new FileOutputStream(absolutePath+".tmp");
			BufferedOutputStream buffWriter = new BufferedOutputStream(fWriter);  
			DataOutputStream out = new DataOutputStream(buffWriter);
			
			buffer = "";
			
			int messageSize = counters[0];
			int usedSimbolsNumber = counters[1];
			
			//Montagem da parte inicial do cabeçalho
			//O primeiro bit eh relativo ao número de bytes usados para cada símbolo
			//Se forem 8 bits => 1º bit = 0
			//Se forem 16 bits => 1º bit = 1
			String parteFixa = 
				(getFormatedCode(messageSize, 32)+ // Nº de símbolos da msg
				getFormatedCode(usedSimbolsNumber, 32)+ // Nº de símb diferentes da msg
				(type.equals(ONE_BYTE)?"00001000":"00010000"));
			
			buffer = save(parteFixa, buffer, out, false);
			
			System.out.println("parte fixa do header: "+parteFixa);
			
			Enumeration<String> enumeration = hashFreq.keys();
			
			int count = 1;
			if (type.equals(ONE_BYTE)) {
				while(enumeration.hasMoreElements()) {
					String simbol = enumeration.nextElement();
					int freq = hashFreq.get(simbol);
					char charac = simbol.charAt(0);
					buffer = save(getFormatedCode(charac, 8)+
								  getFormatedCode(freq, 32), 
								  buffer, out, count==usedSimbolsNumber);
				}
			} else {
				while(enumeration.hasMoreElements()) {
					String simbol = enumeration.nextElement();
					int freq = hashFreq.get(simbol);
					char char1 = simbol.charAt(0);
					int lenght = simbol.length();
					boolean has2Digits = false;
					char char2 = ' ';
					if (lenght == 2) {
						char2 = simbol.charAt(1);
						has2Digits = true;
					}
					buffer = save(getFormatedCode(char1, 8)+
								  (has2Digits ? getFormatedCode(char2, 8) : "")+
								  getFormatedCode(freq, 32),
								  buffer, out, count==usedSimbolsNumber);
				}
				
			}
			
			//Adiciona a mensagem codificada
			byte[] assinatura = new byte[1024];  
			int nBytes;
			while((nBytes = dataIn.read(assinatura)) != -1) {
				for (int i=0; i<nBytes; i++) {
					buffer = save(getFormatedCode(assinatura[i], 8),
							      buffer, out, false);
				}
			}			
			if (buffer.length() < 8)
				buffer = save(buffer, "", out, true);
			
			out.close();
			
			File fileIn = new File(absolutePath);
			fileIn.delete();
			File fileOut = new File(absolutePath+".tmp");
			fileOut.renameTo(fileIn);
			fileOut.delete();
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado");
			System.exit(0);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Salva um código binário em arquivo. Utiliza-se um buffer para que os dados
	 * sejam salvos de byte em byte.
	 * @param code
	 * @param isLastByte
	 * @return Array de Strings que contém o novo buffer a ser atualizado
	 */
	public static String save(String code, String buffer, DataOutputStream out, boolean isLastByte) {

		int bufferSize = buffer.length();
		int codeSize = code.length();
		int freeSpace = 8-bufferSize;
		String result = buffer;

		try {
			if (freeSpace < codeSize) {
				//Se o espaço que falta no buffer é menor que o tamanho do codigo 
				//passado, entao o codigo eh quebrado e o restante eh colocado no buffer
				String[] divided = divideCode(code, freeSpace);
				buffer = buffer+divided[0];
				//Neste save o codigo a ser salvo deve ter exatamente 8 bits
				//para ser salvo, o buffer eh zerado
				save(buffer, "", out, isLastByte);
				buffer = divided[1]; //coloca o restante no buffer
				if (buffer.length() >= 8) {
					result = save(buffer, "", out, isLastByte);
					return result;
				}
			} else if (freeSpace >= codeSize) {
				buffer = buffer+code;
				if (buffer.length() == 8) {
//					System.out.print(buffer+"|");
					out.writeByte(getByte(buffer));
					buffer = "";
				} else if (buffer.length() < 8 && isLastByte) {
//					System.out.println("\nUltimo codigo salvo: "+buffer);
					out.writeByte(getByte(buffer));
					buffer = "";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = buffer;
		return result;
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
	 * Retorna um String relativa ao código binário de um inteiro
	 * @param inteiro Número decimal a ser convertido em binário representado 
	 * como String
	 * @return A String que representa o binário
	 * 
	 */
	public static String getCode(int inteiro) {
		int resto = inteiro%2;
		int quosc = inteiro/2;
		String result = Integer.toString(resto);
		return inteiro==0?"":getCode(quosc)+result;
	}
	
	/**
	 * 
	 * Converte um inteiro para binário em forma de String com um número exato 
	 * de bits, completando com zero os bits que não forem usados.
	 * @param inteiro Número a ser convertido para binário
	 * @param nBits Número de bits que a String deve ter
	 * @return String formatada de acordo com o número de bits especificados
	 * @see #getCode(int)
	 * 
	 */
	public static String getFormatedCode(int inteiro, int nBits) {
		String result = "";
		result = Integer.toBinaryString(inteiro);
		if (inteiro <0) {
			result = inverteBits(subtraiUm(result));
			result = result.substring(24, 31);
		} else {
			int actualSize = result.length();
			if (nBits != actualSize) {
				for (int i=0; i<(nBits-actualSize);i++)
					result = "0"+result;
			}			
		}
		return result;
	}
	
	/**
	 * 
	 * Divide uma String em duas partes, a primeira contendo o número de 
	 * caracteres especificado por size e a segunda contendo o restante 
	 * da String inicial
	 * @param code String que será dividida
	 * @param size Tamanho que a primeira parte da String vai ter
	 * @return Um array de String com duas Strings, no índice 0 a primeira 
	 * parte e no índice 1, a segunda parte
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
	
	/**
	 * Cálculo da entropia dos dados
	 * @param lista
	 * @param lidos
	 * @return Valo da entropia
	 */
	public static float calculaEntropia(ListaOrdenada lista, int lidos){
		No aux = lista.getFirst();
		float entropia = 0.0f;
		for(; aux!=null; aux = aux.getDir()){
			entropia += ((aux.getFreq()/(float)lidos) * 
						(Math.log(1/(float)aux.getFreq())));
		}
		return -entropia;
	}
	
	/**
	 * 
	 * Imprime o tempo corrente e o retorna como String
	 * @return Tempo corrente em String
	 * 
	 */
	public static String currentTime() {
		long time = System.currentTimeMillis();
		Calendar.getInstance().setTimeInMillis(time);
		int hour = Calendar.getInstance().get(Calendar.HOUR);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		int second = Calendar.getInstance().get(Calendar.SECOND);
		int mili = Calendar.getInstance().get(Calendar.MILLISECOND);
		String out = hour+":"+minute+":"+second+"."+mili;
		System.out.println("Time: "+out);
		return out;
	}
	
	public static String inverteBits(String code) {
		String result = "";
		for (int i=0; i<code.length(); i++) {
			result = result+(code.charAt(i)=='0'? "1" : "0");
		}
		return result;
	}
	
	public static String subtraiUm(String code) {
		int j = 32;
		char[] result = code.toCharArray();
		while(j>0 && result[j-1] == '0' ){
			j--;
			result[j]=1;
		}
		j--;
		result[j] = '0';
		return String.valueOf(result);
	}
}
