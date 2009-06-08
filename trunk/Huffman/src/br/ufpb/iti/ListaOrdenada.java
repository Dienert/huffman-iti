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
	
	public No first = new No();
	
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
		if (first.getFreq() == 0) {
			first.setCaracter(novo.getCaracter());
			first.setFreq(novo.getFreq());
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
	
	public void constroiLista(Hashtable<String, Integer> hash){
		Enumeration<String> enumeration = hash.keys();
		//Constroi a lista ordenada da HashTable
		while(enumeration.hasMoreElements()) {
			String charac = enumeration.nextElement();
			this.insere(new No(charac.toString(), hash.get(charac)));
		}
		
	}
}
