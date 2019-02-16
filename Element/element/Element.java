package element;

public abstract class Element {
	private String code;
	private String nom;
	private double quantite;
	private String mesure;
	private double prixVente;
	
	
	/**
	 * Constructeur d'un Element
	 * @param code d'un element : String
	 * @param nom nom d'un element : String
	 * @param quantite quantité d'un element : double
	 * @param mesure mesure d'un element : String
	 * @param prixVente prix de vente d'un element : double
	 */
	public Element(String code, String nom, double quantite, String mesure, double prixVente) {
		this.code = code;
		this.nom = nom;
		this.quantite = quantite;
		this.mesure = mesure;
		this.prixVente = prixVente;
	}
	
	
	/**
	 * Examine la quantite d'un element
	 * @return true si la quantite est negatif sinon false
	 */
	public boolean examiner() {
		boolean quantiteNegatif = false;
		if(this.quantite < 0) {
			quantiteNegatif = true;
		}
		return quantiteNegatif;
	}
	
	////////////////////////////////GETTERS/SETTERS////////////////////////////////////////////////////////

	/**
	 * Recuperer code d'un element
	 * @return code d'un element : String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Recuperer nom d'un element
	 * @return nom d'un element : String
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Recuperer quantité d'un element
	 * @return quantite d'un element : double
	 */
	public double getQuantite() {
		return quantite;
	}

	/**
	 * Recuperer mesure d'un element
	 * @return mesure d'un element : String
	 */
	public String getMesure() {
		return mesure;
	}

	/**
	 * Recuperer prix de vente d'un element
	 * @return prixVente d'un element : double
	 */
	public double getPrixVente() {
		return prixVente;
	}
	
	public void setNom(String n) {
		this.nom= n;
	}

	@Override
	public String toString() {
		return "Code=" + code + ", nom=" + nom + ", quantité=" + quantite + ", mesure=" + mesure
				+ ", prix de vente=" + prixVente;
	}
}
