package production;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import element.Achat;
import element.Element;
import element.MatierePremiere;
import element.Produit;
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
	public Production(Stock leStock, String nomFichier, String separateur) {
		this.lesProduitsManquant = new ArrayList<>();
		this.lesAchats = new ArrayList<>();

		this.leStock = leStock;
		this.stockTemp = stockTemp;
		
		//création de la liste des chaines de production grace à un fichier csv
		this.lesChaines = new ArrayList<>();
		this.importCsv(nomFichier, separateur);
	}
	
	/**
	 * Création de la liste des chaines de production grâce aux information contenu dans un csv dont le nom est passé en paramètre
	 * @param nomFichier nom du fichier csv à importer
	 * @param separateur symbole de separation des informations dans le fichier csv
	 */
	public void importCsv(String nomFichier, String separateur) {		
		List<Couple> entrees = new ArrayList<Couple>();
		List<Couple> sorties = new ArrayList<Couple>();

		BufferedReader br = null;
        String line = "";
        String fichierCsv = "./" + nomFichier;
        String separateurCsv = separateur;

        try {
            br = new BufferedReader(new FileReader(fichierCsv));
            //On lit la première ligne qui contient les noms de colonnes
            br.readLine();

            while ((line = br.readLine()) != null) {
                entrees = new ArrayList<Couple>();
        		sorties = new ArrayList<Couple>();

                // utiliser separateur passé en paramètre
                String[] chaines = line.split(separateurCsv);
                
                //création liste des couples en entree
                String[] coupleE = chaines[2].split(",");
                String codeE = "";
                double qteE = 0.0;
                for(int i = 0; i<coupleE.length; i++) {
                	if (i%2 == 0) {
                		codeE = coupleE[i].substring(1, coupleE[i].length());
                	}
                	if (i%2 == 1) {
                		qteE = Double.valueOf(coupleE[i].substring(0, coupleE[i].length()-1));
                		entrees.add(new Couple(codeE, qteE));
                	}
                }
                //création liste des couples en sortie
                String[] coupleS = chaines[3].split(",");
                String codeS = "";
                double qteS = 0.0;
                for(int i = 0; i<coupleS.length; i++) {
                	if (i%2 == 0) {
                		codeS = coupleS[i].substring(1, coupleS[i].length());
                	}
                	if (i%2 == 1) {
                		qteS = Double.valueOf(coupleS[i].substring(0, coupleS[i].length()-1));
                		sorties.add(new Couple(codeS, qteS));
                	}
                }
                
                //création d'une chaine à ajouter à la liste des chaines grace au CSV
                ChaineProduction c = new ChaineProduction(chaines[0], chaines[1], entrees, sorties);
                this.lesChaines.add(c);
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
