package element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import production.ChaineProduction;

public class Achat extends MatierePremiere{
	private DoubleProperty qteA;
	private ChaineProduction chaine;

	/**
	 * Constructeur d'un achat
	 * @param code d'une matière première ou d'un produit achetable : String
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