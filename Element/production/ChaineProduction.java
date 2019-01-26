package production;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author pouget
 *
 */
public class ChaineProduction {
	private String code;
	private String nom;
	private List<Couple> entrees;
	private List<Couple> sorties;
	
	/**
	 * Constructeur d'une chaine de production
	 * @param code d'une chaine de production : String
	 * @param nom d'une chaine de production : String
	 * @param entrees éléments nécessaires pour effectuer chaine de production List<Couple>
	 * @param sorties produits sortants de la chaine : List<Couple>
	 */
	public ChaineProduction(String code, String nom, List<Couple> entrees, List<Couple> sorties) {
		this.code = code;
		this.nom = nom;
		this.entrees = entrees;
		this.sorties = sorties;
	}

	/**
	 * @return le code de la chaine de production
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @return le nom de la chaine de production
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * @return les éléments nécessaires pour effectuer chaine de production
	 */
	public List<Couple> getEntrees() {
		return entrees;
	}
	
	/**
	 * @return les produits sortant de la chaine de production
	 */
	public List<Couple> getSorties() {
		return sorties;
	}

	public void afficher() {
		System.out.println("ChaineProduction [code=" + this.code + ", nom=" + this.nom); 
		System.out.println("Liste des entrees : ");
		for(Couple c : this.entrees) {
			System.out.println(c.toString());
		}
		System.out.println("Liste des sorties : ");
		for(Couple c : this.sorties) {
			System.out.println(c.toString());
		}

	}
}


