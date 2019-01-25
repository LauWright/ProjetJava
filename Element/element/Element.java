package element;

public class Element {
	private String code;
	private String nom;
	private double quantite;
	private String mesure;
	private double prixVente;
	
	public Element(String code, String nom, double quantite, String mesure, double prixVente) {
		this.code = code;
		this.nom = nom;
		this.quantite = quantite;
		this.mesure = mesure;
		this.prixVente = prixVente;
	}

	public String getCode() {
		return code;
	}

	public String getNom() {
		return nom;
	}

	public double getQuantite() {
		return quantite;
	}

	public String getMesure() {
		return mesure;
	}

	public double getPrixVente() {
		return prixVente;
	}
	
}
