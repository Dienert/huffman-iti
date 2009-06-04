package br.ufpb.iti.huffman;

import java.util.Enumeration;
import java.util.Hashtable;



public class Huffman {

	private static Hashtable<Character, Integer> hash = new Hashtable<Character, Integer>();
	private static ListaOrdenada lista = new ListaOrdenada();

	public static void main(String[] args) {
		String message = "abracadabra";
		
		char[] charArray = message.toCharArray();
		
		for (int i=0; i<charArray.length ; i++) {
			updateHashTableFreq(new Character(charArray[i]));
		}
		
		Enumeration<Character> enumeration = hash.keys();
		
		
		//Constroi a lista ordenada da HashTable
		while(enumeration.hasMoreElements()) {
			Character charac = enumeration.nextElement();
			
			
			lista.insere(new No(charac, hash.get(charac)));


			
			
			No primeiro = lista.first;
			No segundo, terceiro, raiz;
			
			//devo ter um no especial para que a raiz da arvore nao se perca
			
			segundo = primeiro.getDir();
			segundo = primeiro.getDir();
			terceiro = new No(' ', primeiro.freq+ segundo.freq); 
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
							
				terceiro = new No(' ', primeiro.freq+ segundo.freq); 
				
				/*não tinha certeza se fazia diferença ou não inicializar esse No 
				*com um caractere ou com outro
				* (ele não deixou eu  colocar apenas '' comoargumento do construtor
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
	}
	
	public static void updateHashTableFreq(Character caracter) {
		if (hash.containsKey(caracter)) {
			hash.put(caracter, (Integer)hash.get(caracter)+1);
		} else {
			hash.put(caracter, 1);
		}
	}
	

	
	
	/*public LinkedList<No> constroiListaOrdenada(Hashtable<Character, Integer> hash){
		
		LinkedList<No> lista = new LinkedList<No>();
		
		
		
		return lista;
		
	}*/
	
	
	 
}
