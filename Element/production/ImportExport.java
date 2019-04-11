package production;

import java.util.List;

import element.Achat;
import element.Element;
import element.ProduitManquant;
import javafx.collections.ObservableList;

public interface ImportExport {
	
	//IMPORT
	/**
	 * Importation des éléments
	 * @param nomFichier
	 * @param separateur
	 * @return
	 */
	public abstract ObservableList<Element> importElement(String nomFichier, char separateur);
	
	/**
	 * Importation des chaines de production
	 * @param nomFichier
	 * @param separateur
	 * @return
	 */
	public abstract ObservableList<ChaineProduction> importChaineProduction(String nomFichier, char separateur);
	
	/**
	 * Créer une liste de d'entrées ou de sorties pour une chaine de production
	 * @param nomFichier
	 * @param separateur
	 * @return une liste d'entrées ou de sorties pour une chaine de production
	 */
	public abstract List<Couple> importCouple(String nomFichier, char separateur, String codeChaine);
	
	//EXPORT
	/**
     * Réécriture du fichier élément avec en parametre le nom du fichier dans lequel on vet réécrire la liste d'éléments
     * @param nomFichier
     * @param elements
     */
	 public void writeCsvElement(String nomFichier, List<Element> elements);
	 
	 /**
	     * Ajouter un élément au fichier élément
	     * @param element
	     */
	 public void ajouterCsvElement(Element element);
	 
	 /**
	  * Aouter une chaine de production au fichier
	  * @param chaines
	  */
	 public void ajouterCsvChaineProduction(List<ChaineProduction> chaines);
	 
	 /**
	     * Ajouter une entree pour une chaine
	     * @param code
	     * @param entrees
	     */
	 public void ajouterCsvEntree(String code, List<Couple> entrees);
	 
	 /**
	     * réécriture du fichier des entrees
	     * @param code
	     * @param entrees
	     */
	 public void writeCsvEntree();
	 
	  /**
	     * Ajouter une sortie au fichier pour une chaine
	     * @param code
	     * @param sorties
	     */
	 public void ajouterCsvSortie(String code, List<Couple> sorties);
	 
	 /**
	     * réécriture du fichier des sorties
	     * @param code
	     * @param entrees
	     */
	 public void writeCsvSortie();
	 
	 /**
	     * Ecrire le fichier de chaines de production
	     * @param chaines
	     */
	 public void writeCsvChaineProduction(List<ChaineProduction> chaines);
	 
	 /**
	     * Ecriture du fichier des achats avec en parametre le nom du fichier et la liste des Matieres premieres à acheter
	     * @param nomFichier
	     * @param elements
	     */
	 public void writeCsvAchat(String nomFichier, List<Achat> mas);
	
	 /**
	     * Ecriture du fichier des produits manquants avec en parametre le nom du fichier et la liste des produits à produire
	     * @param nomFichier
	     * @param elements
	     */
	 public void writeCsvProduitManquant(String nomFichier, List<ProduitManquant> pms);
	
	
}
