package element;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MatierePremiere extends Element{
	/**
	 * Constructeur MatierePremiere
	 * @param code d'une matière première : String
	 * @param nom nom d'une matière première : String
	 * @param quantite quantité d'une matière première : double
	 * @param mesure mesure d'une matière première : String
	 * @param prixVente prix de vente d'une matière première : double
	 * @param prixAchat prix de vente d'une matière première : double
	 */
	public MatierePremiere(String code, String nom, double quantite, String mesure, double prixVente, double prixAchat) {
		super(code, nom, quantite, mesure, prixVente, prixAchat);
	}
	
	/**
	 * Constructeur MatierePremiere à partir d'une autre matiere premiere
	 * @param ma
	 */
	public MatierePremiere(MatierePremiere ma) {
		super(ma.getCode(), ma.getNom(), ma.getQuantite(), ma.getMesure(), ma.getPrixVente(), ma.getPrixAchat());
	}	

	public String toString() {
		return "Matière première : " + super.toString() + " prix d'achat=" + this.getPrixAchat();
	}
	

}
