package element;

public class Achat extends MatierePremiere{
	private double qteA;

	public Achat(String code, String nom, double quantite, String mesure, double prixVente, double prixAchat, double qte) {
		super(code, nom, quantite, mesure, prixVente, prixAchat);
		this.qteA = qte;
	}
	
	
}
