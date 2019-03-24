package element;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import production.ChaineProduction;

public class Achat extends MatierePremiere{
	private DoubleProperty qteA;
	private ChaineProduction chaine;

	/**
	 * Constructeur d'un achat
	 * @param code d'une matière première : String
	 * @param nom nom d'une matière première : String
	 * @param quantite quantité d'une matière première : double
	 * @param mesure mesure d'une matière première : String
	 * @param prixVente prix de vente d'une matière première : double
	 * @param prixAchat prix d'achat d'une matière première : double
	 * @param qte quantité de matière première à acheter : double
	 * @param c
	 */
	public Achat(String code, String nom, double quantite, String mesure, double prixVente, double prixAchat, double qte, ChaineProduction c) {
		super(code, nom, quantite, mesure, prixVente, prixAchat);
		this.qteA = new SimpleDoubleProperty(qte);
		this.chaine = c;
	}
	
	/**
	 * Récupère la quantité à acheter pour une matière première
	 * @return qteA d'un achat
	 */
	public double getQteA() {
		return qteA.get();
	}
	
	/**
	 * Récupère la quantité à acheter pour une matière première
	 * @return qteA d'un achat
	 */
	public DoubleProperty getQteAProperty() {
		return qteA;
	}
	
	/**
	 * Récupère la quantité à acheter pour une matière première
	 * @return qteA d'un achat
	 */
	public void setQteA(double qte) {
		this.qteA = new SimpleDoubleProperty(qte);
	}
	
	public ChaineProduction getChaine() {
		return this.chaine;
	}
}
