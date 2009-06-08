package br.ufpb.iti;



public class No {

	private int freq = 0;
	private String caracter;
	private boolean ehEsq;
	
	private No esq = null;
	private No dir = null;
	private No pai = null;
	private No filhoEsq = null;
	private No filhoDir = null;
	
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
	
	
	public boolean getEhEsq() {
		return ehEsq;
	}

	public void setEhEsq(boolean v) {
		this.ehEsq = v;
	}
	
	public No getEsq() {
		return esq;
	}

	public void setEsq(No esq) {
		this.esq = esq;
		if (esq != null)
			esq.setPai(this);
	}

	public No getDir() {
		
		return dir;
	}

	public void setDir(No dir) {
		this.dir = dir;
		if(dir != null)
			dir.setPai(this);
	}
	
	public No getPai() {
		return pai;
	}

	public void setPai(No pai) {
		this.pai = pai;
	}
	
	public No getFilhoEsq() {
		return filhoEsq;
	}

	public void setFilhoEsq(No filhoEsq) {
		this.filhoEsq = filhoEsq;
	}

	public No getFilhoDir() {
		return filhoDir;
	}

	public void setFilhoDir(No filhoDir) {
		this.filhoDir = filhoDir;
	}

	public static void mostraArvore(No raiz){
		
		if(raiz == null){
			System.out.println("-");
			return;
		}
		else{
			System.out.println(raiz.freq);
			
			
			/*não pode fazer isso pois o direito está nulo
			* modificar método mostraArvore para esse caso.
			* ou o construtor do No... ao invés de não criar (deixar nulo)
			* é melhor criar umcom frequência zero?
			*/
			if(raiz.getFilhoDir()!=null){
				mostraArvore(raiz.getFilhoDir());
			}
			/*Scanner scan = new Scanner(System.in);
			scan.nextInt();*/
			if(raiz.getFilhoEsq()!= null){
				mostraArvore(raiz.getFilhoEsq());
			}
		}
	}
	
	/**
	 * 
	 * Constroi a arvore de Huffman, somando os dois primeiros elementos da lista
	 * ordenada e removendo-os da lista, até que sobre apenas o nó raiz
	 * @param lista Lista ordenada contendo os nós dos símbolos e suas frenquencias
	 * @return Retorna o nó raiz da árvore
	 * 
	 */
	public static No constroiArvore(ListaOrdenada lista){
		while (lista.getFirst().getDir() != null) {
			No primeiro = lista.removeFirst();
			No segundo = lista.removeFirst();
			No novo = new No(null, primeiro.getFreq()+ segundo.getFreq());
			novo.setFilhoEsq(primeiro);
			novo.setFilhoDir(segundo);
			lista.insere(novo);
		}
		return lista.getFirst();
	}

}
