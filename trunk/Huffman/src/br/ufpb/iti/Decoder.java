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
 * se for a vers�o de 1 byte
l� 1 byte
pra o n�mero
de simbolos usados
o outro l� 2
l� mais 1 byte para o n�mero de bits livres do �ltimo byte
a� faz um for
para o n�mero de s�mbolos
e vai colocando recriando o hash de frequencias
do mesmo jeito
q est� no codificador
(l� s�mbolo e frequencia do arquivo)
constr�i a arvore
e o hash de c�digos
hashCodes
e vai decodificando a mensagem
quer dizer
n�o precisa de hash de c�digos
quer dizer
qual � o mais eficiente?
vc  v� ai
se � ir pegando os bits
e ir procurando na �rvore
ou no hash
 */
public class Decoder {
	
	
	
	
	private static String absolutePath = "";
	private static String absolutePathResult = "";
	private static int modo = -1;
	private static Hashtable<String, Integer> hashFrequencia = new Hashtable<String, Integer>();
	
	
	//come�ando do zero!
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
		
				int[] assinatura = new int[1024];  
				int[] teste = new int[8*9];
				
				for (int i = 0; i <assinatura.length; i++) {
					assinatura[i] = data.read();
				}
				
				/*for (int i = 0; i < assinatura.length; i++) {
					System.out.print(assinatura[i]+" ");
					
				}*/
				teste = deByteArrayParaIntArray(assinatura, 9);
				
				modo = teste[0];
/*				System.out.println("modo: "+modo);
				for (int i = 0; i < teste.length; i++) {
					System.out.print(teste[i]);
				}*/
				
				
				
				int[] primeiros = new int[8*8];
				
				for (int i = 0; i < primeiros.length; i++) {
					primeiros[i] = teste[i+1];
					System.out.print(primeiros[i]);
					if((i+1)%32 == 0)
						System.out.println();
				}
				
				int[] cabecalho = numerosDeIndices(primeiros, 32);
				
				System.out.println(cabecalho[0] + " " + cabecalho[1]);
				
				int buffer[] = new int[8];
				
				for (int i = 0; i < buffer.length-1; i++) {
					buffer[i] = primeiros[65+i];
				}
				
				
				int contador = 9; // bytes lidos 
				int nSimbolosDiferentes = cabecalho[1];
				int nLidos = 0;
				int[] numero = new int[8];
				int[] temp = new int[1];
				int[] number = new int[1];
				char carac = '@';
				ListaOrdenada lista = new ListaOrdenada();
				
				
				int contadorDeNo = 0;
				
				while(nLidos != nSimbolosDiferentes){
					if(contador == 1024){
						for (int i = 0; i <assinatura.length; i++) {
							assinatura[i] = data.read();
						}	
					}
					temp[0] = assinatura[contador];
					contador++;
					numero = deByteArrayParaIntArray(temp,1);
					buffer[7] = numero[0];
					number = numerosDeIndices(buffer,8);
					
					
					//atualizando buffer
					for (int i = 0; i < buffer.length-1; i++) {
						buffer[i] = number[i+1];
					}
					
					//agora, converto esse int para caractere
					
					
					carac = (char) number[0];
					
					int[] freq = new int[4];
					
					for (int i = 0; i < 4; i++) {
						freq[i] = assinatura[contador];
						contador++;
					}
					//freq � um array com 4 posi��es
					int[] freq2 = new int[4*8];
					freq2 = deByteArrayParaIntArray(freq, 4);
					
					int[] var = new int[32];
					int i = 0;
					for (; i < 7; i++) {
						var[i] = buffer[i];
					}
					
					//os is que sobrarem ser�o preenchidos pelo que ficou em freq
					
					for (int j = 0; j < 25; j++) {
						var[i] = freq2[j];
					}
					
					//atualizando buffer
					for (i = 25; i < 32; i++) {
						buffer[i] = freq[i+1];
					}
					
					int[] numeroDaFrequencia = numerosDeIndices(freq2,32);
					//No noh = new No(carac,numeroDaFrequencia[0]);
					lista.insere(new No(carac+"",numeroDaFrequencia[0]));
					nLidos++;
				}
					
					//lista criada :P
					
				
				No raiz = No.constroiArvore(lista);
				
				//l� o arquivo do come�o, pulando os primeiros 8 bytes
				//no nono byte, despreza o 1� bit e faz um buffer
					
					
					
					//number[0] � o n�mero
					
					
					
					
					
				
				
				
				
				
				
				
				//ser� que t� dando errado porque eu estou lendo coisas demais???
				
				int frequenciasLidas = 0;
				int bytesLidos = 9;
				int[] temporario = new int[8]; 
				
				
				
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* ler s�mbolo e sua frequencia e guardar na hash.
				 * 
				 * como?
				 * 
				 * sobraram 7 bits depois que o cabe�alho foi lido.
				 * leio mais 5 bytes (8 bits para o caracter e 32 para sua frequencia).
				 * sobrar�o 7 novamente. vou lendo at� que numeroDeFrequenciasLidas == 
				 * n�meroDeSimbolosDiferentes.
				 * 
				 * mas como vou saber que tal s�mbolo � tal s�mbolo? quer dizer, estou lendo em bin�rio...
				 * s� converter direto em caracter funciona?
				 */
				/*
				 * depois, armazenar� na hash os 7 bits restantes do �ltimo byte 
				 *	depois, tem que ler os 1024-9 restantes e continuar lendo at� que
				 *lidos seja igual ao n�mero de s�mbolos existentes
				 *
				 * em seguida, cria uma lista a partir da hash e depois a �rvore
				 * 
				 * para decodificar, teremos que ler novamente o arquivo. abre, 
				 * l� primeiros 9 bytes, s� considera os 7 �ltimos bits do �ltimo.
				 * em seguida, faz um for percorrendo os 1024 - 9 restantes e
				 * continuar codificando at� que seja igual ao n�mero de s�mbolos
				 * existentes. 
				 */
				
				
/*				
				
			
	*/	}
		
	
	
		
		
		//melhor pegar 4 arrays de inteiros  e convert�-los
		
		
		/*o m�todo seguinte ir� converter o byte para um array de inteiros representando os �ndices para compor o n�mero maior.  
		 * ... eu acho
		 */
	public static int[] deByteArrayParaIntArray(int[] bytes, int nBytes){
		//passando esse array, se eu fizer uma modifica��o,
		//quando o m�todo acabar, a modifica��o continua feita?
		
		int[] indices = new int[1024*8]; 
		int[] temporario = {-1,-1,-1,-1,-1,-1,-1,-1};
			
		for(int i = 0; i<nBytes; i++){ //de byte em byte
			
			int valor = (int) bytes[i];
			System.out.println("valor: " +valor);
			
			boolean ehNegativo = valor<0? true: false; 
			valor = Math.abs(valor);
				for(int j = 1; j<=8; j++){
					temporario[8-j] = valor%2;		
					valor = valor/2;
				}
			
				if(!ehNegativo){
					for(int j = 0; j< 8; j++){
						indices[j+(8*i)] = temporario[j];
					}
				}
				else{
					
					System.out.println("temporario1:");
					for (int j = 0; j < temporario.length; j++) {
						System.out.print(temporario[j]);
					}
					
					for (int j = 0; j < temporario.length; j++) {
							if(temporario[j]== 1)temporario[j] = 0;
							else temporario[j] = 1;
						}
						
						System.out.println("temporario2:");
						for (int j = 0; j < temporario.length; j++) {
							System.out.print(temporario[j]);
						}
						System.out.println("\n");
						int j = 8;
						while(j>0 && temporario[j-1] == 1 ){
							j--;
							temporario[j]=0;
													
							}
						j--;
						temporario[j] = 1;
						//temporario[j]++;				
			}
				
			System.out.println("temporario3:");
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
	
	/*entrada: um array com n�mero m�ltiplo de 8 contendo �ndices. no caso
	 * do decodificador, temos que pgar a partir do 2� bit lido at� o 33�,
	 * j� que o primeiro � usado para identificar o modo de leitura	 * 
	 */
	
	//esse a � s� para n�o usar 32... vou pensar em um nome melhor para esse argumento.
	public static int[] numerosDeIndices(int[] indices, int a){
		int[] numeros = new int[indices.length/a];
		
		for (int i = 0; i < numeros.length; i++) {
			
			for(int j =31; j>=0; j--){
				numeros[i] +=  ((int) Math.pow(2.0,j)) * indices[(i*a) + 31-j];
			}
			
		}
		
		return numeros;
	}
	

	
	public static Hashtable<String, Integer> controiHash(long numero, String nome){
		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		
		
		return hash;
		
	}
	

}

