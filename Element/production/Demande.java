package production;

public class Demande {
	private int idSemaine;
	private String codeElement;
	private double quantiteDemande;
	
	
	/**
	 * Constructeur d'une demande
	 * @param id
	 * @param code
	 * @param quantite
	 */
	public Demande(int id, String code, double quantite) {
		this.idSemaine = id;
		this.codeElement = code;
		this.quantiteDemande = quantite;
	}


	public int getIdSemaine() {
		return idSemaine;
	}


	public String getCodeElement() {
		return codeElement;
	}


	public double getQuantiteDemande() {
		return quantiteDemande;
	}
	
	

}
