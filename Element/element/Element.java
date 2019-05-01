package element;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Element {
	private StringProperty code;
	private StringProperty nom;
	private DoubleProperty quantite;
	private StringProperty mesure;
	private DoubleProperty prixVente;
	private DoubleProperty prixAchat;
	
	/**
	 * Constructeur d'un Element
	 * @param code d'un element : String
	 * @param nom nom d'un element : String
	 * @param quantite quantité d'un element : double
	 * @param mesure mesure d'un element : String
	 * @param prixVente prix de vente d'un element : double
	 */
	public Element(String code, String nom, double quantite, String mesure, double prixVente, double prixAchat) {
		this.code = new SimpleStringProperty(code);
		this.nom = new SimpleStringProperty(nom);
		this.quantite = new SimpleDoubleProperty(quantite);
		this.mesure = new SimpleStringProperty(mesure);
		this.prixVente = new SimpleDoubleProperty(prixVente);
		this.prixAchat = new SimpleDoubleProperty(prixAchat);
	}
	
	
	/**
	 * Examine la quantite d'un element
	 * @return true si la quantite est negatif sinon false
	 */
	public boolean examiner() {
		boolean quantiteNegatif = false;
		if(this.quantite.get() < 0) {
			quantiteNegatif = true;
		}
		return quantiteNegatif;
	}
	
	
	/**
	 * Soustraction d'une quantité sur un élément
	 * @param quantite
	 */
	public void soustraire(double quantite) {
		this.setQuantite(this.getQuantite()-quantite);
	}
	
	/**
	 * Ajout d'une quantité sur un élément
	 * @param quantite 
	 * @param code
	 */
	public void ajouter(double quantite) {
		this.setQuantite(this.getQuantite()+quantite);
	}
	
	////////////////////////////////GETTERS/SETTERS////////////////////////////////////////////////////////

	//GETTEURS
	
	/**
	 * Recuperer code d'un element
	 * @return code d'un element : String
	 */
	public String getCode() {
		return this.code.get();
	}
	
	/**
	 * Recuperer la propriété code d'un element
	 * @return code d'un element : String
	 */
	public StringProperty getCodeProperty() {
		return this.code;
	}

	/**
	 * Recuperer nom d'un element
	 * @return nom d'un element : String
	 */
	public String getNom() {
		return this.nom.get();
	}
	
	/**
	 * Recuperer la propriété code d'un element
	 * @return code d'un element : String
	 */
	public StringProperty getNomProperty() {
		return this.nom;
	}

	/**
	 * Recuperer quantité d'un element
	 * @return quantite d'un element : double
	 */
	public double getQuantite() {
		return this.quantite.get();
	}
	
	/**
	 * Recuperer la propriété quantité d'un element
	 * @return quantite d'un element : double
	 */
	public DoubleProperty getQuantiteProperty() {
		return this.quantite;
	}

	/**
	 * Recuperer mesure d'un element
	 * @return mesure d'un element : String
	 */
	public String getMesure() {
		return this.mesure.get();
	}
	
	/**
	 * Recuperer la propriete mesure d'un element
	 * @return mesure d'un element : String
	 */
	public StringProperty getMesureProperty() {
		return this.mesure;
	}
	
	/**
	 * Recuperer la propriété prix de vente d'un element
	 * @return prixVente d'un element : double
	 */
	public DoubleProperty getPrixVenteProperty() {
		return this.prixVente;
	}

	/**
	 * Recuperer prix de vente d'un element
	 * @return prixVente d'un element : double
	 */
	public double getPrixVente() {
		return this.prixVente.get();
	}
	
	/**
	 * Recuperer la propriété prix d'achat d'un element
	 * @return prixVente d'un element : double
	 */
	public DoubleProperty getPrixAchatProperty() {
		return this.prixAchat;
	}
	
	/**
	 * Recuperer prix d'achat d'un element
	 * @return prixVente d'un element : double
	 */
	public double getPrixAchat() {
		return this.prixAchat.get();
	}
	

	public DoubleProperty getNoPrixAchatProperty() {
		return new SimpleDoubleProperty(0.0);
	}
	
	//SETTERS
	
	/**
	 * Modifie le nom de l'élément
	 * @param n nouveau nom de l'élément
	 */
	public void setNom(String n) {
		this.nom.set(n);
	}
	/**
	 * Modifie la quantité de l'élément
	 * @param q nouvelle quantité de l'élément
	 */
	public void setQuantite(double q) {
		this.quantite.set(q);
	}
	
	
	public void setCode(String code) {
		this.code.set(code);
	}


	public void setMesure(String mesure) {
		this.mesure.set(mesure);
	}


	public void setPrixVente(Double prixVente) {
		this.prixVente.set(prixVente);
	}
	
	public void setPrixAchat(Double prixAchat) {
		this.prixAchat.set(prixAchat);
	}

	@Override
	public String toString() {
		return "Code=" + this.code + ", nom=" + this.nom + ", quantité=" + this.quantite + ", mesure=" + this.mesure
				+ ", prix de vente=" + this.prixVente;
	}
}
