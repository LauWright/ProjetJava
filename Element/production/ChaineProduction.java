package production;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author pouget
 *
 */
public class ChaineProduction {
	private StringProperty code;
	private StringProperty nom;
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
		this.code = new SimpleStringProperty(code);
		this.nom = new SimpleStringProperty(nom);
		this.entrees = entrees;
		this.sorties = sorties;
	}

	/**
	 * @return le code de la chaine de production
	 */
	public String getCode() {
		return this.code.get();
	}
	
	/**
	 * @return la propriété code de la chaine de production
	 */
	public StringProperty getCodeProperty() {
		return this.code;
	}
	
	/**
	 * Modifie le code de la chaine
	 * @param code
	 */
	public void setCode(String code) {
		this.code = new SimpleStringProperty(code);
	}

	/**
	 * @return le nom de la chaine de production
	 */
	public String getNom() {
		return nom.get();
	}
	
	/**
	 * @return la propriété nom de la chaine de production
	 */
	public StringProperty getNomProperty() {
		return nom;
	}
	
	/**
	 * Modifie le nom de la chaine
	 * @param nom
	 */
	public void setNom(String nom) {
		this.nom = new SimpleStringProperty(nom);
	}

	/**
	 * @return les éléments nécessaires pour effectuer chaine de production
	 */
	public List<Couple> getEntrees() {
		return entrees;
	}
	
	/**
	 * Modifie les entrees
	 * @param entrees
	 */
	public void setEntrees(List<Couple> entrees) {
		this.entrees = entrees;
	}

	/**
	 * @return les produits sortant de la chaine de production
	 */
	public List<Couple> getSorties() {
		return sorties;
	}
	
	/**
	 * Modifie les sorties
	 * @param sorties
	 */
	public void setSorties(List<Couple> sorties) {
		this.sorties = sorties;
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


