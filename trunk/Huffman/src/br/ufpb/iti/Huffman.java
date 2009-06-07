package br.ufpb.iti;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;



public class Huffman {

	private static Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
	private static ListaOrdenada lista = new ListaOrdenada();

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
		String type = "-1b";
		
		if (size >= 1)
			fileName = args[0];
		if(size == 2)
			type = args[1];
		
		if (fileName.equals("") || !(type.equals("-1b") || type.equals("-2b"))) {
			printSyntax();
		}
		
		try {
			//Pega o caminho absoluto do arquivo
			String absolutePath = ClassLoader.getSystemResource(fileName).toString().replace("file:", "");
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
					for (int i=0; i<nBytes; i=+2) {
						//Esta linha converte dois byte para 2 chars e depois para String
						//e insere o caracter na hash de frenquencias
						updateHashTableFreq(new String(""+(char)(assinatura[i] & 0xFF)+
														  (char)(assinatura[i+1] & 0xFF)));
					}
				}
			}
			
			Enumeration<String> enumeration = hash.keys();
			
			//Constroi a lista ordenada da HashTable
			while(enumeration.hasMoreElements()) {
				String charac = enumeration.nextElement();
				lista.insere(new No(charac.toString(), hash.get(charac)));
				
				No primeiro = lista.first;
				No segundo, terceiro, raiz;
				
				//devo ter um no especial para que a raiz da arvore nao se perca
				
				segundo = primeiro.getDir();
				segundo = primeiro.getDir();
				terceiro = new No(null, primeiro.freq+ segundo.freq); 
				terceiro.setDir(primeiro);
				primeiro.ehEsq = false;
				terceiro.setEsq(segundo);
				primeiro.ehEsq = true;
				raiz = terceiro;
				
				primeiro = terceiro;
				segundo = segundo.getDir();
				
				//devo ter um no especial para que a raiz da arvore nao se perca
				
				//Constroi a Arvore a partir da Lista
				while(segundo!=null){//acabou de percorrer a lista
					
					terceiro = new No(null, primeiro.freq+ segundo.freq); 
					
					/*nao tinha certeza se fazia diferenca ou nao inicializar esse No 
					 *com um caractere ou com outro
					 * (ele nao deixou eu  colocar apenas '' comoargumento do construtor
					 */
					
					//o novo no criado tera como filhos os dois de menor frequencia disponiveis.
					//um da lista e um da arvore (no primeiro caso, os dois da lista
					terceiro.setDir(primeiro);
					terceiro.setEsq(segundo);
					
					
					//atualizando as referencias
					primeiro = terceiro;
					segundo = segundo.getDir();
					
				}  //acabou de percorrer a lista
				
				raiz.mostraArvore();
				
			}
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo nao encontrado");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static void updateHashTableFreq(String caracter) {
		if (hash.containsKey(caracter)) {
			hash.put(caracter, (Integer)hash.get(caracter)+1);
		} else {
			hash.put(caracter, 1);
		}
	}
	
}
