package production;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import element.Achat;
import element.ProduitManquant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Semaine {
	private int idSemaine;
	
	private ObservableMap<String, element.Element> stockPreviEntree;
	private ObservableMap<String, element.Element> stockPreviSortie;
	
	private Map<Integer, List<ChaineProduction>> chaineProductionNiveau;
	
	private ObservableList<Achat> achats;
	private ObservableList<ProduitManquant> produitManquant;
	private double resultat;
	
	
	/**
	 * Constructeur de la class Semaine
	 * @param id
	 */
	public Semaine( int id) {
		this.idSemaine = id;
		this.chaineProductionNiveau = new HashMap<>();
		this.achats = FXCollections.observableArrayList();
		this.produitManquant = FXCollections.observableArrayList();	
		this.stockPreviEntree = FXCollections.emptyObservableMap();
		this.stockPreviSortie = FXCollections.emptyObservableMap();
	}

	
	public int getIdSemaine() {
		return idSemaine;
	}

	public void setIdSemaine(int idSemaine) {
		this.idSemaine = idSemaine;
	}

	public ObservableMap<String, element.Element> getStockPreviEntree() {
		return stockPreviEntree;
	}

	public void setStockPreviEntree(ObservableMap<String, element.Element> stockPreviEntree) {
		this.stockPreviEntree = stockPreviEntree;
	}
	
	/**
	 * Modifier le prix du stock pour simuler le changement de prix entre les semaines
	 * @param stockPreviEntree
	 */
	public void setStockPreviEntreeNewPrix(ObservableMap<String, element.Element> stockPreviEntree) {
		for(Map.Entry<String, element.Element> e: stockPreviEntree.entrySet()) {
			if(e.getValue().getPrixAchat() != -1) {
				Random r = new Random();
				double randomValue = 0;
				randomValue = e.getValue().getPrixAchat() + r.nextInt(6 + 1);
				randomValue = e.getValue().getPrixAchat() - r.nextInt(6 + 1);
				if(randomValue <= 0) {
					randomValue = 1;
				}
				e.getValue().setPrixAchat(randomValue);
			}
		}
		this.stockPreviEntree = stockPreviEntree;
	}
	
	/**
	 * Modifier la quantite du stock en entre
	 * @param stockPreviEntree
	 */
	public void setQuantiteStock(ObservableMap<String, element.Element> stockPreviEntree) {
		for (Map.Entry<String, element.Element> e: stockPreviEntree.entrySet()) {
			this.stockPreviEntree.get(e.getKey()).setQuantite(e.getValue().getQuantite());
		}
	}
	
	/**
	 * Modifier la quantite du stock en sortie
	 * @param stockPreviEntree
	 */
	public void setQuantiteStockSortie(ObservableMap<String, element.Element> stockPreviEntree) {
		for (Map.Entry<String, element.Element> e: stockPreviEntree.entrySet()) {
			this.stockPreviSortie.get(e.getKey()).setQuantite(e.getValue().getQuantite());
		}
	}

	public void setPrixStock(ObservableMap<String, element.Element> stockPreviEntree) {
		for(Map.Entry<String, element.Element> e: stockPreviEntree.entrySet()) {
			if(e.getValue().getPrixAchat() != -1) {
				this.stockPreviEntree.get(e.getKey()).setPrixAchat(e.getValue().getPrixAchat());
			}			
		}	
	}

	public ObservableMap<String, element.Element> getStockPreviSortie() {
		return stockPreviSortie;
	}

	public void setStockPreviSortie(ObservableMap<String, element.Element> stockPreviSortie) {
		this.stockPreviSortie = stockPreviSortie;
	}

	public ObservableList<Achat> getAchats() {
		return achats;
	}

	public void setAchats(ObservableList<Achat> achats) {
		this.achats = achats;
	}

	public ObservableList<ProduitManquant> getProduitManquant() {
		return produitManquant;
	}

	public void setProduitManquant(ObservableList<ProduitManquant> produitManquant) {
		this.produitManquant = produitManquant;
	}

	public double getResultat() {
		return resultat;
	}

	public void setResultat(double resultat) {
		this.resultat = resultat;
	}


	public Map<Integer, List<ChaineProduction>> getChaineProductionNiveau() {
		return chaineProductionNiveau;
	}


	public void setChaineProductionNiveau(Map<Integer, List<ChaineProduction>> chaineProductionNiveau) {
		this.chaineProductionNiveau = chaineProductionNiveau;
	}
	
	
}
