package production;

import java.util.ArrayList;
import java.util.List;

import element.Achat;
import element.ProduitManquant;

public class Production {
	private List<ProduitManquant> lesProduitsManquant;
	private List<Achat> lesAchats;
	private Stock stockTemp;
	private Stock leStock;
	private List<ChaineProduction> lesChaines;
	
	/**
	 * Le constructeur d'une production 
	 * @param lesProduits Manquant à une production
	 * @param lesAchats A faire pour réaliser une production
	 * @param stockTemp stock temporaire pour les simulations
	 * @param leStock stock actuel à modifier
	 * @param nomFichier nom du fichier csv à importer
	 * @param separateur symbole de separation des informations dans le fichier csv
	 */
	public Production(Stock leStock, String nomFichier, char separateur) {
		this.lesProduitsManquant = new ArrayList<>();
		this.lesAchats = new ArrayList<>();

		this.leStock = leStock;
		this.stockTemp = leStock;
		
		//création de la liste des chaines de production grace à un fichier csv
		this.lesChaines = ImportCsv.importChaineProduction(nomFichier, separateur);
		
	}
	
	/**
	 * 
	 * @return la chaine testée
	 */
	public List<ChaineProduction> getUneChaine() {
		return this.lesChaines;
	}
	
	/**
	 * 
	 * @return les produits manquant pour réaliser une production
	 */
	public List<ProduitManquant> getLesProduitsManquant() {
		return lesProduitsManquant;
	}
	/**
	 * 
	 * @return les achats de matière première à effectuer
	 */
	public List<Achat> getLesAchats() {
		return lesAchats;
	}
	/**
	 * 
	 * @return le stock cloné pour les tests
	 */
	public Stock getStockTemp() {
		return stockTemp;
	}
	/**
	 * 
	 * @return le stock
	 */
	public Stock getLeStock() {
		return leStock;
	}

}
