package br.ufpb.iti;


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
	
	public void mostraArvore(){
		
		if(this.equals(null)){
			return;
		}
		
		System.out.println(this.freq);
		this.getDir().mostraArvore();
		this.getEsq().mostraArvore();
		
	}
	
	
	public static No constroiArvore(ListaOrdenada lista){
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
			
			
			
			//AQUI, ELE DEVE FAZER UMA ESCOLHA ENTRE O PRÓXIMO NÓ DA LISTA OU O TERCEIRO QUE ACABA DE SER CRIADO
			//o novo no criado tera como filhos os dois de menor frequencia disponiveis.
			//um da lista e um da arvore (no primeiro caso, os dois da lista
			terceiro.setDir(primeiro);
			terceiro.setEsq(segundo);
			
			
			//atualizando as referencias
			primeiro = terceiro;
			segundo = segundo.getDir();
			
		}  //acabou de percorrer a lista
		return raiz;
	}

}
