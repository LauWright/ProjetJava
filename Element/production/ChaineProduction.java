package production;

import java.util.List;

public class ChaineProduction {
	private String code;
	private String nom;
	private List<Couple> entrees;
	private List<Couple> sorties;
	
	public ChaineProduction(String code, String nom, List<Couple> entrees, List<Couple> sorties) {
		this.code = code;
		this.nom = nom;
		this.entrees = entrees;
		this.sorties = sorties;
		
		
	}
		
}


