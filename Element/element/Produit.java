package element;

public class Produit extends Element{
	/**
	 * Constructeur d'un produit
	 * @param c code d'un produit : String
	 * @param n nom d'un produit : String
	 * @param q quantité d'un produit : double
	 * @param m mesure d'un produit : String
	 * @param p prix de vente d'un produit : Int
	 */
	public Produit(String code, String nom, double quantite, String mesure, double prix) {
		super(code, nom, quantite, mesure, prix);
	}
	
	/**
	 * Constructeur d'un produit à partir d'un autre produit
	 * @param p
	 */
	public Produit(Produit p) {
		super(p.getCode(), p.getNom(), p.getQuantite(), p.getMesure(), p.getPrixVente());
	}
	
	public String toString() {
		return "Produit : " + super.toString();
	}
}
