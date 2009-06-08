package br.ufpb.iti;

import java.util.Enumeration;

import java.util.Hashtable;

/**
 * 
 * @author Dienert<br/>
 * 		   Amanda
 *
 */
public class ListaOrdenada {
	
	private No first = null;
	
	/***
	 * 
	 * Insere um novo No na lista de forma ordenada pela frequencia do caracter
	 * de cada no
	 * @param novo
	 * 		O novo no a ser inserido
	 * 
	 ***/
	public void insere(No novo) {
		//Nao possui nenhum caracter ainda (lista vazia)
		if (first == null) {
			first = novo;
		} else {
			No aux = first;
			while (aux.getDir() != null) {
				if (aux.getFreq() > novo.getFreq())
					break;
				else 
					aux = aux.getDir(); 
			}
			//inserir antes de aux
			if(aux.getFreq() > novo.getFreq()) { 
				//novo vai passar a ser first
				if (aux.getCaracter() == first.getCaracter()) {
					this.first = novo;
				} else {
					aux.getEsq().setDir(novo);
					novo.setEsq(aux.getEsq());
				}
				novo.setDir(aux);
				aux.setEsq(novo);
			} else {
				novo.setEsq(aux);
				aux.setDir(novo);
			}
		}
	}
	
	/**
	 * 
	 * Retorna o primeiro nós da lista encadeada ao mesmo tempo em que este é removido
	 * da lista.
	 * @return Nó removido da lista
	 * 
	 */
	public No removeFirst() {
		No aux = first;
		first = first.getDir();
		if (first != null)
			first.setEsq(null);
		aux.setDir(null);
		return aux; 
	}
	
	/**
	 * 
	 * Constrói uma lista duplamente encadeada ordenada pelo número da frequencia
	 * de cada símbolo existente na hashtable
	 * @param hash Hashtable contendo os símbolos e suas respectivas frequencias
	 * 
	 */
	public void constroiLista(Hashtable<String, Integer> hash){
		Enumeration<String> enumeration = hash.keys();
		//Constroi a lista ordenada da HashTable
		while(enumeration.hasMoreElements()) {
			String charac = enumeration.nextElement();
			this.insere(new No(charac.toString(), hash.get(charac)));
		}
		
	}

	public No getFirst() {
		return first;
	}

	public void setFirst(No first) {
		this.first = first;
	}
	
}
