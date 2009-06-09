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



/*
 * se for a versão de 1 byte
lê 1 byte
pra o número
de simbolos usados
o outro lê 2
lê mais 1 byte para o número de bits livres do último byte
aí faz um for
para o número de símbolos
e vai colocando recriando o hash de frequencias
do mesmo jeito
q está no codificador
(lê símbolo e frequencia do arquivo)
constrói a arvore
e o hash de códigos
hashCodes
e vai decodificando a mensagem
quer dizer
não precisa de hash de códigos
quer dizer
qual é o mais eficiente?
vc  vê ai
se é ir pegando os bits
e ir procurando na árvore
ou no hash
 */
public class Decoder {
	
	private static Hashtable<String, Integer> hashFrequencia = new Hashtable<String, Integer>();
	private static Hashtable<String, String> hashCodes = new Hashtable<String, String>();
	private static ListaOrdenada lista = new ListaOrdenada();
	
	private static String absolutePath = "";
	private static String absolutePathResult = "";
	private static String type = "-1b";
	
	private static final String ONE_BYTE = "-1b";
	private static final String TWO_BYTES = "-2b";
	
	private static String buffer = "";
	
	public static void main(String[] args) {
		int size = args.length;
		String fileName = "";
		
		
		
		if (size >= 1)
			fileName = args[0];
		if(size == 2)
			type = args[1];
		
	/*	if (fileName.equals("") || !(type.equals(ONE_BYTE) || type.equals(TWO_BYTES))) {
			printSyntax();
		}*/
		
		try {
			//Pega o caminho absoluto do arquivo
			absolutePath = ClassLoader.getSystemResource(fileName).toString().replace("file:", "");
			FileInputStream fReader = new FileInputStream(absolutePath);
			BufferedInputStream buffReader = new BufferedInputStream(fReader);  
			DataInputStream data = new DataInputStream(buffReader);  
			
			
			byte[] numeroUm = new byte[1];
			byte[] numeroDois = new byte[2];
			byte[] bitsLivres = new byte[1];
			int numeroDeSimbolos = 0;
			Hashtable<String, String> hashCodes = new Hashtable<String, String>();
			
			
			byte[] assinatura = new byte[1024];  
			int nBytes = 0;
			int lidos = 0;
			
				if(type.equals(ONE_BYTE)) {
					data.read(numeroUm);
					numeroDeSimbolos = fromBinaryToInt(numeroUm);
					
					
					
					for (int i=0; i<numeroDeSimbolos; i++) {
						//Esta linha converte um byte para char e depois para String
						//e insere o caracter na hash de frenquencias
						Huffman.updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)));
						lidos++;
					}
				} else if (type.equals(TWO_BYTES)) {
					data.read(numeroDois);
					numeroDeSimbolos = fromBinaryToInt(numeroDois);
					
					
					
					for (int i=0; i<numeroDeSimbolos; i+=2) {
						//Esta linha converte dois bytes para 2 chars e depois para String
						//e insere o caracter na hashtable de frenquencias
						Huffman.updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)+
								(i+1 == nBytes? "" : (char)(assinatura[i+1] & 0xFF))));//what is going on here???
						//caso a linha i+1 seja igual ao numero de bytes lidos, entao
						//o segundo caracter nao existe, e serah inserido apenas 1 caracter na hashtable
						lidos++;
					}
				}
				
			
			
			lista.constroiLista(hashFrequencia); //Constroi a lista ordenada da HashTable
			
			No raiz = No.constroiArvore(lista);
			
			//opção 1: gerar Hash e ir pegando.
			hashCodes = Huffman.generateHashSimbolsAndCodes(raiz, "");
			
			//opção 2: percorrer árvore buscando o nó folha
			
	
			
			while((nBytes = data.read(assinatura)) != -1) {
				if(type.equals(ONE_BYTE)) {
					for (int i=numeroDeSimbolos; i<nBytes; i++) {
						//buscará a palavra código no hash e devolverá a palavra bonitinha
						String temp = Byte.toString(assinatura[i]);
						String decodificada = hashCodes.get(temp);
						
						//no caso de percorrer a árvore:
						
						
						
						
						putWord(decodificada);
						
						//colocar em um arquivo?
						
						nBytes++;
					}
				} else if (type.equals(TWO_BYTES)) {
					for (int i=numeroDeSimbolos; i<nBytes; i+=2) {
						//Esta linha converte dois bytes para 2 chars e depois para String
						//e insere o caracter na hashtable de frenquencias
						
						
						String temp = Byte.toString(assinatura[i]).concat(Byte.toString(assinatura[i+1]));
						String decodificada = hashCodes.get(temp);
						putWord(decodificada);
						//caso a linha i+1 seja igual ao numero de bytes lidos, entao
						//o segundo caracter nao existe, e serah inserido apenas 1 caracter na hashtable
						nBytes++;
					}
				}
				
			}
			
			
	
		}catch (FileNotFoundException e) {
			System.err.println("Arquivo nao encontrado");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	public static int fromBinaryToInt(byte[] bytes){
		int numero = 0;
		for (int i = 0; i < bytes.length; i++) {
			numero += ((bytes.length - i) * bytes[i]); 
		}
		return numero;
	}
	
	
	public static void putWord(String abc) {
		try {
						
			
			FileOutputStream fWriter = new FileOutputStream(absolutePath+".tmp");
			BufferedOutputStream buffWriter = new BufferedOutputStream(fWriter);  
			DataOutputStream dataOut = new DataOutputStream(buffWriter);

				
			
				
		} catch (FileNotFoundException e) {
			System.exit(0);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
/*	public static String percorreArvore(No raiz, byte b){
		No temp = raiz;
		while(raiz!= null){
			
		}
		
	}*/
}

