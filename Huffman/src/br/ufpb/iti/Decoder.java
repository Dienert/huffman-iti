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
	
	
	
	
	private static String absolutePath = "";
	private static String absolutePathResult = "";
	private static int modo = -1;
	
	
	//começando do zero!
	//abre o arquivo
	

	/**
	 * 
	 * Imprime a sintaxe correta de uso do programa
	 * 
	 */
	public static void printSyntax() {
		System.out.println("Usage: java HuffmanDecoder file ");
				
		System.exit(1);
	}
	
	
	//abrir fluxo e ler arquivo
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
		
				
				
				//se funciona, você não questiona =P
				byte[] assinatura = new byte[1024];  
				int[] indices = new int[8*1024];
				int[] teste = new int[8*9];
				
				
				data.read(assinatura);
				for (int i = 0; i < assinatura.length; i++) {
					System.out.print(assinatura[i]+" ");
					
				}
				teste = deByteArrayParaIntArray(assinatura, 9);
				
				modo = teste[0];
/*				System.out.println("modo: "+modo);
				for (int i = 0; i < teste.length; i++) {
					System.out.print(teste[i]);
				}*/
				
				
				
				int[] primeiros = new int[8*8];
				
				for (int i = 0; i < primeiros.length; i++) {
					primeiros[i] = teste[i+1];
				}
				
				long[] cabecalho = numerosDeIndices(primeiros);
				
				System.out.println(cabecalho[0] + " " + cabecalho[1]);
				
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	
	
		
		
		//melhor pegar 4 arrays de inteiros  e convertê-los
		
		
		/*o método seguinte irá converter o byte para um array de inteiros representando os índices para compor o número maior.  
		 * ... eu acho
		 */
	public static int[] deByteArrayParaIntArray(byte[] bytes, int nBytes){
		//passando esse array, se eu fizer uma modificação,
		//quando o método acabar, a modificação continua feita?
		
		int[] indices = new int[1024*8]; 
		int[] temporario = {-1,-1,-1,-1,-1,-1,-1,-1};
		
		for(int i = 0; i<nBytes; i++){ //de byte em byte
			int valor = (int) bytes[i]; 
			System.out.println("valor: " +valor);
			if(valor>=0){
				for(int j = 1; j<=8; j++){
					 //convertendo de byte para int implicitamente
					temporario[8-j] = valor%2;		
					valor = valor/2;
				}
			}
			else{
				temporario[0] = 1;
				valor = Math.abs(valor);
				for (int j = 0; j < 7; j++) {
					temporario[7-j] = valor%2;		
					valor = valor/2;
				}
			}
			System.out.println("temporario:");
			for (int j = 0; j < temporario.length; j++) {
				System.out.print(temporario[j]);
			}
			System.out.println("\n");
			
			for(int j = 0; j< 8; j++){
				indices[j+(8*i)] = temporario[j];
			}
		}
		return indices;
	}
	
//melhor fazer para cada byte individualmente
	
	/*entrada: um array com número múltiplo de 8 contendo índices. no caso
	 * do decodificador, temos que pgar a partir do 2º bit lido até o 33º,
	 * já que o primeiro é usado para identificar o modo de leitura	 * 
	 */
	
	public static long[] numerosDeIndices(int[] indices){
		long[] numeros = new long[indices.length/32];
		
		for (int i = 0; i < numeros.length; i++) {
			
			for(int j =1; j<=32; j++){
				numeros[i] +=  ((int) Math.pow(2.0, 32-j)) * indices[(i*32) + j-1];
			}
			
		}
		
		return numeros;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*	private static Hashtable<String, Integer> hashFrequencia = new Hashtable<String, Integer>();
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
		
		if (fileName.equals("") || !(type.equals(ONE_BYTE) || type.equals(TWO_BYTES))) {
			printSyntax();
		}
		
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

			dataOut.writeBytes(abc);	
			
				
		} catch (FileNotFoundException e) {
			System.exit(0);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String percorreArvore(No raiz, byte b){
		No temp = raiz;
		while(raiz!= null){
			
		}
		
	}*/
}

