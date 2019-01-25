package element;

public class ProduitManquant extends Produit{
	
	private double qteP;
	/**
	 * Constructeur de produit
	 * @param c code d'un produit : String
	 * @param n nom d'un produit : String
	 * @param q quantité d'un produit : double
	 * @param m mesure d'un produit : String
	 * @param p prix de vente d'un produit : Int
	 * @param qM quantité manquante d'un produit
	 */
	public ProduitManquant(String c, String n, double q, String m, double p, double qM) {
		super(c, n, q, m, p);
		this.qteP =qM;
	}
	
	/**
	 * 
	 * @return la quantité manquante du produit : double
	 */
	public double getQuantiteM() {
		return this.qteP;
	}
	
}
