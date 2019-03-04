package production;

import java.util.ArrayList;
import java.util.List;

import element.Element;
import element.MatierePremiere;
import element.Produit;


public class Stock {
	private List<Element> lesElements;
	
	/**
	 * Constructeur d'un stock
	 * @param nomFichier nom du fichier csv à importer
	 * @param separateur symbole de separation des informations dans le fichier csv
	 */
	public Stock( String nomFichier, char separateur) {
		this.lesElements = ImportCsv.importElement(nomFichier, separateur);
	}
	
	/**
	 * Constructeur du stock à partir d'un autre stock
	 * @param s stock à cloner
	 */
	public Stock(Stock s) {
		this.lesElements =new ArrayList<>();
		for(Element e : s.lesElements) {	
			if(e.getClass().getSimpleName().equals("Produit")) {
				Produit p = new Produit((Produit) e);
				this.lesElements.add(p);
			}
			if(e.getClass().getSimpleName().equals("MatierePremiere")) {
				MatierePremiere ma = new MatierePremiere((MatierePremiere) e);
				this.lesElements.add(ma);
			}
		}
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
	 * Retourne un élément grâce à son code
	 * @param c le code de l'élément cherché
	 * @return l'élément recherché
	 */
	public Element getElementByCode(String c) {
		boolean ok = false;
		Element reponse = null;
		int i = 0;
		while (i<this.lesElements.size() && !ok) {
			if (this.lesElements.get(i).getCode() == c){
				ok = true;
				reponse = this.lesElements.get(i);
			}
		}
		if (!ok) {
			reponse = null;
		}
		return reponse;
	}
	/**
	 * Retourne les éléments du stock
	 * @return
	 */
	public List<Element> getElements(){
		return this.lesElements;
	}
	/**
	 * Soustraction d'une quantité sur un élément
	 * @param quantite 
	 * @param code
	 */
	public void soustraire(int quantite, String code) {
		Element elem = this.getElementByCode(code);
		elem.setQuantite(elem.getQuantite()-quantite);
	}
	/**
	 * Ajout d'une quantité sur un élément
	 * @param quantite 
	 * @param code
	 */
	public void ajouter(int quantite, String code) {
		Element elem = this.getElementByCode(code);
		elem.setQuantite(elem.getQuantite()+quantite);
	}
}
