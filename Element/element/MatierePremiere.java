package element;

public class MatierePremiere extends Element{
	private double prixAchat;

	public MatierePremiere(String code, String nom, double quantite, String mesure, double prixVente, double prixAchat) {
		super(code, nom, quantite, mesure, prixVente);
		this.prixAchat = prixAchat;
	}


}
