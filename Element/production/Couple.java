package production;

public class Couple {
	private String code;
	private double qte;
	
	//TEST
	
	/**
	 * Constructeur du couple
	 * @param c code d'un élément : String
	 * @param qte quantité d'éléments nécessaires : double
	 */
	public Couple(String c, double qte) {
		this.code = c;
		this.qte = qte;
	}
	/**
	 * 
	 * @return le code d'un élément
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 
	 * @return la quantité nécessaire de cet élément
	 */
	public double getQte() {
		return qte;
	}
	
	@Override
	public String toString() {
		return "Couple [code=" + this.code + ", qte=" + this.qte + "]";
	}
	
	
}