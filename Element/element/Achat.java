package element;

public class Achat extends MatierePremiere{
	private double qteA;

	/**
	 * Constructeur d'un achat
	 * @param code d'une matière première : String
	 * @param nom nom d'une matière première : String
	 * @param quantite quantité d'une matière première : double
	 * @param mesure mesure d'une matière première : String
	 * @param prixVente prix de vente d'une matière première : double
	 * @param prixAchat prix d'achat d'une matière première : double
	 * @param qte quantité de matière première à acheter : double
	 */
	public Achat(String code, String nom, double quantite, String mesure, double prixVente, double prixAchat, double qte) {
		super(code, nom, quantite, mesure, prixVente, prixAchat);
		this.qteA = qte;
	}
	
	/**
	 * Récupère la quantité à acheter pour une matière première
	 * @return qteA d'un achat
	 */
	public double getQteA() {
		return qteA;
	}
	
	
	
	
}
