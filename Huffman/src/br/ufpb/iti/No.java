package br.ufpb.iti;

import java.util.Scanner;


public class No {

	int freq = 0;
	String caracter;
	boolean ehEsq;
	
	No esq = null;
	No dir = null;
	No pai = null;

	
	public No(String caracter, int freq) {
		this.caracter = caracter;
		this.freq = freq;
	}
	
	public No() {}
	
	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public String getCaracter() {
		return caracter;
	}

	public void setCaracter(String caracter) {
		this.caracter = caracter;
	}
	

	public No getEsq() {
		return esq;
	}

	public void setEsq(No esq) {
		this.esq = esq;
		esq.setPai(this);
	}

	public No getDir() {
		
		return dir;
	}

	public void setDir(No dir) {
		this.dir = dir;
		dir.setPai(this);
	}
	
	public No getPai() {
		return pai;
	}

	public void setPai(No pai) {
		this.pai = pai;
	}
	
	public static void mostraArvore(No raiz){
		
		if(raiz == null){
			System.out.println("-");
			return;
		}
		else{
		System.out.println(raiz.freq);
		
		
		/*n�o pode fazer isso pois o direito est� nulo
		* modificar m�todo mostraArvore para esse caso.
		* ou o construtor do No... ao inv�s de n�o criar (deixar nulo)
		* � melhor criar umcom frequ�ncia zero?
		*/
			if(raiz.getDir()!=null){
				mostraArvore(raiz.getDir());
			}
			Scanner scan = new Scanner(System.in);
			scan.nextInt();
			if(raiz.getEsq()!= null){
				mostraArvore(raiz.getEsq());
			}
		}
	}
	
	
	public static No constroiArvore(ListaOrdenada lista){
		No primeiro = lista.first;
		No segundo, terceiro;
		No raiz = null;
		
		
		//devo ter um no especial para que a raiz da arvore nao se perca
		
		segundo = primeiro.getDir();
		terceiro = new No(null, primeiro.freq+ segundo.freq); 
		terceiro.setDir(primeiro);
		primeiro.ehEsq = false;
		terceiro.setEsq(segundo);
		primeiro.ehEsq = true;
		
		primeiro = segundo.getDir();
		segundo = primeiro.getDir();
		//nem sempre verdadeiro o que vem a seguir! mudar isso:
		
		//quarto ser� sempre usado para evitar confus�o com refer�ncias

		//Esses testes ser�o feitos a cada vez
		
		
		//devo ter um no especial para que a raiz da arvore nao se perca
		
		//Constroi a Arvore a partir da Lista
		
		
		//antes de entrar no while, existem 3 n�s (primeiro, 
		while(segundo!=null){//acabou de percorrer a lista
			
			if(((primeiro!= null) && (terceiro.freq <= primeiro.freq)) ||((segundo!= null) && terceiro.freq<segundo.freq)){
				//duas �nicas condi��es em que o terceiro ser� usado.
				//unir� o n� recentemente criado ao pr�ximo da lista
				raiz = new No(null, primeiro.freq+ terceiro.freq);
				raiz.setDir(terceiro);
				terceiro.ehEsq = false;
				raiz.setEsq(primeiro);
				primeiro.ehEsq = true;
				terceiro = raiz;
				primeiro = segundo;
				
			}
			
			else{ //terceiro > primeiro
				//unir� os dois pr�ximos da lista
				if((primeiro!=null) && (segundo!= null)){ //deixa o if????
					raiz = new No(null, primeiro.freq + segundo.freq);
					raiz.setDir(primeiro);
					primeiro.ehEsq = false;
					raiz.setEsq(segundo);
					segundo.ehEsq = true;
					primeiro = raiz;
				}
			}
			
			
			//atualiza��es de refer�ncia devem ser feitas caso a caso
			
			segundo = primeiro.getDir();
			
		}  //acabou de percorrer a lista
		
		return raiz;
	}
	
	

}
