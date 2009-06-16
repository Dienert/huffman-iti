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
	private static Hashtable<String, Integer> hashFrequencia = new Hashtable<String, Integer>();
	
	
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
					//freq é um array com 4 posições
					int[] freq2 = new int[4*8];
					freq2 = deByteArrayParaIntArray(freq, 4);
					
					int[] var = new int[32];
					int i = 0;
					for (; i < 7; i++) {
						var[i] = buffer[i];
					}
					
					//os is que sobrarem serão preenchidos pelo que ficou em freq
					
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
				
				//lê o arquivo do começo, pulando os primeiros 8 bytes
				//no nono byte, despreza o 1º bit e faz um buffer
					
					
					
					//number[0] é o número
					
					
					
					
					
				
				
				
				
				
				
				
				//será que tá dando errado porque eu estou lendo coisas demais???
				
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
				
				/* ler símbolo e sua frequencia e guardar na hash.
				 * 
				 * como?
				 * 
				 * sobraram 7 bits depois que o cabeçalho foi lido.
				 * leio mais 5 bytes (8 bits para o caracter e 32 para sua frequencia).
				 * sobrarão 7 novamente. vou lendo até que numeroDeFrequenciasLidas == 
				 * númeroDeSimbolosDiferentes.
				 * 
				 * mas como vou saber que tal símbolo é tal símbolo? quer dizer, estou lendo em binário...
				 * só converter direto em caracter funciona?
				 */
				/*
				 * depois, armazenará na hash os 7 bits restantes do último byte 
				 *	depois, tem que ler os 1024-9 restantes e continuar lendo até que
				 *lidos seja igual ao número de símbolos existentes
				 *
				 * em seguida, cria uma lista a partir da hash e depois a árvore
				 * 
				 * para decodificar, teremos que ler novamente o arquivo. abre, 
				 * lê primeiros 9 bytes, só considera os 7 últimos bits do último.
				 * em seguida, faz um for percorrendo os 1024 - 9 restantes e
				 * continuar codificando até que seja igual ao número de símbolos
				 * existentes. 
				 */
				
				
/*				
				
			
	*/	}
		
	
	
		
		
		//melhor pegar 4 arrays de inteiros  e convertê-los
		
		
		/*o método seguinte irá converter o byte para um array de inteiros representando os índices para compor o número maior.  
		 * ... eu acho
		 */
	public static int[] deByteArrayParaIntArray(int[] bytes, int nBytes){
		//passando esse array, se eu fizer uma modificação,
		//quando o método acabar, a modificação continua feita?
		
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
	
	/*entrada: um array com número múltiplo de 8 contendo índices. no caso
	 * do decodificador, temos que pgar a partir do 2º bit lido até o 33º,
	 * já que o primeiro é usado para identificar o modo de leitura	 * 
	 */
	
	//esse a é só para não usar 32... vou pensar em um nome melhor para esse argumento.
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

