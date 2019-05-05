package element;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Produit extends Element{
	private BooleanProperty estAchetable;
	
	/**
	 * Constructeur d'un produit
	 * @param c code d'un produit : String
	 * @param n nom d'un produit : String
	 * @param q quantité d'un produit : double
	 * @param m mesure d'un produit : String
	 * @param p prix de vente d'un produit : Int
	 */
	public Produit(String code, String nom, double quantite, String mesure, double prixV, double prixA, boolean estA) {
		super(code, nom, quantite, mesure, prixV, prixA);
		this.estAchetable = new SimpleBooleanProperty(estA);
	}
	
	/**
	 * Constructeur d'un produit à partir d'un autre produit
	 * @param p
	 */
	public Produit(Produit p) {
		super(p.getCode(), p.getNom(), p.getQuantite(), p.getMesure(), p.getPrixVente(), p.getPrixAchat());
		this.estAchetable = p.estAchetable;
	}
	
	/**
	 * Savoir si un produit est achetable ou non
	 * @return prixVente d'un element : double
	 */
	public boolean isAchetable() {
		return estAchetable.get();
	}

	/**
	 * Recuperer la propriété achetable d'un produit
	 * @return prixVente d'un element : double
	 */
	public BooleanProperty isAchetableProperty() {
		return estAchetable;
	}
	
	public void setEstAchetable(boolean estAchetable) {
		this.estAchetable = new SimpleBooleanProperty(estAchetable);
	}

	public String toString() {
		return "Produit : " + super.toString();
	}
}
