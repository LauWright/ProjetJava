package production;

import java.util.List;

import element.Element;


public class Stock {
	private List<Element> lesElements;
	
	/**
	 * Constructeur du stock
	 * @param nomFichier nom du fichier csv à importer
	 * @param separateur symbole de separation des informations dans le fichier csv
	 */
	public Stock( String nomFichier, char separateur) {
		this.lesElements = ImportCsv.importElement(nomFichier, separateur);
	}
	
	/**
	 * Affiche tous les elements du stock
	 */
	public void afficher() {
		for(Element e : this.lesElements) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * Soustraction d'une quantité sur un élément
	 * @param quantite 
	 * @param code
	 */
	public void soustraire(int quantite, String code) {
		
	}
}
