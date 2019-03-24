package element;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import production.ChaineProduction;

public class ProduitManquant extends Produit{
	
	private DoubleProperty qteP;
	private ChaineProduction chaine;
	
	/**
	 * Constructeur de produit
	 * @param c code d'un produit : String
	 * @param n nom d'un produit : String
	 * @param q quantité d'un produit : double
	 * @param m mesure d'un produit : String
	 * @param p prix de vente d'un produit : Int
	 * @param qM quantité manquante d'un produit
	 */
	public ProduitManquant(String c, String n, double q, String m, double p, double qM, ChaineProduction chaine) {
		super(c, n, q, m, p);
		this.qteP = new SimpleDoubleProperty(qM);
		this.chaine = chaine;
	}
	
	/**
	 * 
	 * @return la quantité manquante du produit : double
	 */
	public double getQuantiteM() {
		return this.qteP.get();
	}
	
	/**
	 * 
	 * @return la property quantité manquante du produit : double
	 */
	public DoubleProperty getQuantiteMProperty() {
		return this.qteP;
	}

	/**
	 * 
	 * @return la chaine de production du produit manquant
	 */
	public ChaineProduction getChaine() {
		return this.chaine;
	}
	
}
