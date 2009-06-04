package br.ufpb.iti.huffman;


public class No {

	int freq = 0;
	char caracter;
	boolean ehEsq;
	
	No esq = null;
	No dir = null;

	
	public No(char caracter, int freq) {
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

	public char getCaracter() {
		return caracter;
	}

	public void setCaracter(char caracter) {
		this.caracter = caracter;
	}

	public No getEsq() {
		return esq;
	}

	public void setEsq(No esq) {
		this.esq = esq;
	}

	public No getDir() {
		return dir;
	}

	public void setDir(No dir) {
		this.dir = dir;
	}
	
	public void mostraArvore(){
		
		if(this.equals(null)){
			return;
		}
		
		System.out.println(this.freq);
		this.getDir().mostraArvore();
		this.getEsq().mostraArvore();
		
	}

}
