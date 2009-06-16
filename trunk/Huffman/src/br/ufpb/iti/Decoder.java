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
		
				byte[] assinatura = new byte[1024];  
				int[] teste = new int[8*9];
				
				for (int i = 0; i <assinatura.length; i++) {
					assinatura[i] = data.readByte();
				}
				
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
					System.out.print(primeiros[i]);
					if((i+1)%32 == 0)
						System.out.println();
				}
				
				long[] cabecalho = numerosDeIndices(primeiros, 32);
				
				System.out.println(cabecalho[0] + " " + cabecalho[1]);
				
				
				
				//ser� que t� dando errado porque eu estou lendo coisas demais???
				
				int frequenciasLidas = 0;
				int bytesLidos = 9;
				int[] temporario = new int[8]; 
				
				
				//later
				/*while(frequenciasLidas != cabecalho[1]){
					if(bytesLidos == 1024){
						data.read(assinatura);
						bytesLidos = 0;
					}
					*/
					//assinatura[bytesLidos+1]
					           //TODO continuar a partir daqui
					
					
					
					
				//}
				
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
	public static int[] deByteArrayParaIntArray(byte[] bytes, int nBytes){
		//passando esse array, se eu fizer uma modifica��o,
		//quando o m�todo acabar, a modifica��o continua feita?
		
		int[] indices = new int[1024*8]; 
		int[] temporario = {-1,-1,-1,-1,-1,-1,-1,-1};
		boolean ehNegativo = false;
		
		for(int i = 0; i<nBytes; i++){ //de byte em byte
			
			int valor = (int) bytes[i];
			System.out.println("valor: " +valor);
			if(valor<0){
				ehNegativo = true;				
				valor = Math.abs(valor);
				System.out.println("valor absoluto: " +valor);
			}
				for(int j = 1; j<=8; j++){
					 //convertendo de byte para int implicitamente
					temporario[8-j] = valor%2;		
					valor = valor/2;
				}
			
				if(!ehNegativo || (valor==0)){
					for(int j = 0; j< 8; j++){
						indices[j+(8*i)] = temporario[j];
					}
				}
				else{
						for (int j = 0; j < temporario.length; j++) {
							if(temporario[j]== 1)temporario[j] = 0;
							else temporario[j] = 1;
						}
						
						System.out.println("temporario1:");
						for (int j = 0; j < temporario.length; j++) {
							System.out.print(temporario[j]);
						}
						System.out.println("\n");
						int j = 7;
						while(temporario[j] == 1 && j>0){
							temporario[j]=0;
							j--;						
					}
						temporario[j]++;				
			}
				
			System.out.println("temporario2:");
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
	public static long[] numerosDeIndices(int[] indices, int a){
		long[] numeros = new long[indices.length/a];
		
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

