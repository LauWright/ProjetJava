package element;

public class Produit extends Element{
	/**
	 * Constructeur de produit
	 * @param c code d'un produit : String
	 * @param n nom d'un produit : String
	 * @param q quantité d'un produit : double
	 * @param m mesure d'un produit : String
	 * @param p prix de vente d'un produit : Int
	 */
	public Produit(String c, String n, double q, String m, double p) {
		super(c, n, q, m, p);
	}
}
