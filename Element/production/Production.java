package production;

import java.util.List;

import element.Achat;
import element.ProduitManquant;

public class Production {
	private List<ProduitManquant> lesProduitsManquant;
	private List<Achat> lesAchats;
	private Stock stockTemp;
	private Stock leStock;
	private ChaineProduction uneChaine;
	
	/**
	 * Le constructeur d'une production 
	 * @param lesProduits Manquant à une production
	 * @param lesAchats A faire pour réaliser une production
	 * @param stockTemp stock temporaire pour les simulations
	 * @param leStock stock actuel à modifier
	 * @param uneChaine associé à la production testé
	 */
	public Production(List<ProduitManquant> lesProduitsManquant, List<Achat> lesAchats, Stock stockTemp,
			Stock leStock, ChaineProduction uneChaine) {
		super();
		this.lesProduitsManquant = lesProduitsManquant;
		this.lesAchats = lesAchats;
		this.stockTemp = stockTemp;
		this.leStock = leStock;
		this.uneChaine = uneChaine;
	}
	/**
	 * 
	 * @return la chaine testée
	 */
	public ChaineProduction getUneChaine() {
		return uneChaine;
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
