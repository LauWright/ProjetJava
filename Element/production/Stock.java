package production;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import element.Element;
import element.MatierePremiere;
import element.Produit;

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
	 * Création de la liste des éléments grâce aux information contenu dans un csv dont le nom est passé en paramètre
	 * @param nomFichier nom du fichier csv à importer
	 * @param separateur symbole de separation des informations dans le fichier csv
	 */
	public void importCsv(String nomFichier, String separateur) {
		BufferedReader br = null;
        String line = "";
        String fichierCsv = "./" + nomFichier;
        String separateurCsv = separateur;

        try {
            br = new BufferedReader(new FileReader(fichierCsv));
            //On lit la première ligne qui contient les noms de colonnes
            br.readLine();
            while ((line = br.readLine()) != null) {

                // utiliser separateur passé en paramètre
                String[] elem = line.split(separateurCsv);
                
                // -1.0 signifie que l'élément n'a pas de prix de vente
            	double d = -1.0;
            	if(!elem[5].equals("NA")) {
            		d= Double.valueOf(elem[5]);
            	}
                
                //création d'un element à ajouter à la liste des elements grace au CSV
                //création de l'élément en fonction du type contenu dans le csv
                Element e = null;
                if (elem[6].equals("MA")) {
                	e = new MatierePremiere(elem[0], elem[1], Double.valueOf(elem[2]), elem[3], d, Double.valueOf(elem[4]));
                	
                }
                if (elem[6].equals("P")) {
                	e = new Produit(elem[0], elem[1], Double.valueOf(elem[2]), elem[3], d);
                	
                }
                this.lesElements.add(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
	 * Soustraction d'une quantité sur un élément
	 * @param quantite 
	 * @param code
	 */
	public void soustraire(int quantite, String code) {
		
	}
}
