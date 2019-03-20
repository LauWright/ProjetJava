package production;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import element.Achat;
import element.Element;
import element.MatierePremiere;
import element.ProduitManquant;

public abstract class ExportCsv {
	
	//Delimiter used in CSV file
    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
     
    //CSV file header
    private static final String FILE_HEADER_ELEMENT = "Code;Nom;Quantite;unite;achat;vente;type";
    private static final String FILE_HEADER_ACHAT= "Chaine;Element;Nom;Mesure;Achat;Quantité";
    private static final String FILE_HEADER_PRODUITM= "Chaine;Element;Nom;Mesure;Quantité";
    
    /**
     * Réécriture du fichier élément avec en parametre le nom du fichier dans lequel on vet réécrire la liste d'éléments
     * @param nomFichier
     * @param elements
     */
    public static void writeCsvElement(String nomFichier, List<Element> elements) {
         FileWriter fileWriter = null;
                 
        try {
            fileWriter = new FileWriter(nomFichier);
 
            //Entête Csv
            fileWriter.append(FILE_HEADER_ELEMENT.toString());
             
            //Nouvelle ligne
            fileWriter.append(NEW_LINE_SEPARATOR);
             
            for (Element e : elements) {
            	if(e.getClass().getSimpleName().equals("Produit")) {
            		 fileWriter.append(e.getCode());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(e.getNom());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(e.getQuantite()));
                     fileWriter.append(DELIMITER);
                     fileWriter.append(e.getMesure());
                     fileWriter.append(DELIMITER);
                     fileWriter.append("NA");
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(e.getPrixVente()));
                     fileWriter.append(DELIMITER);
                     fileWriter.append("P");
                     fileWriter.append(NEW_LINE_SEPARATOR);
            	}
            	if(e.getClass().getSimpleName().equals("MatierePremiere")) {
            		 MatierePremiere ma = (MatierePremiere) e;
            		 fileWriter.append(ma.getCode());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(ma.getNom());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(ma.getQuantite()));
                     fileWriter.append(DELIMITER);
                     fileWriter.append(ma.getMesure());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(ma.getPrixAchat()));
                     fileWriter.append(DELIMITER);
                     if(ma.getPrixVente() == -1.0) { 
                    	 fileWriter.append("NA");
                    	 fileWriter.append(DELIMITER);
                     } else {
                    	 fileWriter.append(String.valueOf(ma.getPrixVente()));
                    	 fileWriter.append(DELIMITER);
                     }
                     fileWriter.append("MA");
                     fileWriter.append(NEW_LINE_SEPARATOR);
            	}
            }
            System.out.println("Fichier créé avec succes");             
        } catch (Exception e) {
            e.printStackTrace();
        } finally {      
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }
    
    /**
     * Ajouter un élément au fichier élément
     * @param element
     */
    public static void ajouterCsvElement(Element element) { 
        // Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("elements1.csv", true));
			String s="";			
			s +=  element.getCode() + ";" + element.getNom() + ";" + element.getQuantite() + ";" + element.getMesure();
			if(element.getClass().getSimpleName().equals("MatierePremiere")) {
				MatierePremiere ma = (MatierePremiere) element;
				s += ";" + ma.getPrixAchat();
				if(ma.getPrixVente() == -1.0) { 
					s += ";" + "NA";
				} else {
					s += ";" + ma.getPrixVente();
				}
				s += ";" + "MA";
			} else {
				s += ";" + "NA" + ";" + element.getPrixVente() + ";" + "P";
			}
	        s += "\n";
	        //ecrit la chaine de charactere
	        writer.write(s);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
    }
    
/**
 * Aouter une chaine de production au fichier
 * @param chaines
 */
    public static void ajouterCsvChaineProduction(List<ChaineProduction> chaines) { 
    	// Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("chaine.csv", true));
			String s = "";
	        for(ChaineProduction c : chaines) {
	        	s += c.getCode() + ";" + c.getNom();
	        	s += "\n";
	        	ExportCsv.ajouterCsvEntree(c.getCode(), c.getEntrees());
	        	ExportCsv.ajouterCsvSortie(c.getCode(), c.getSorties());
	        }
	        //ecrit la chaine de charactere
	        writer.write(s);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
    	
    }
    
    /**
     * Ajouter une entree pour une chaine
     * @param code
     * @param entrees
     */
    public static void ajouterCsvEntree(String code, List<Couple> entrees) { 
        // Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("entrees.csv",true));
			String s = code ;
	        for(Couple c : entrees) {
	        	s += ";" + c.getCode() + "," + c.getQte();
	        }
	        s += "\n";
	        //ecrit la chaine de charactere
	        writer.write(s);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
    }
    
    /**
     * réécriture du fichier des entrees
     * @param code
     * @param entrees
     */
    public static void writeCsvEntree() { 
        // Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("entrees.csv"));
			String s = "Code;Entrees \n";
	        //ecrit la chaine de charactere
	        writer.write(s);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
    }
    
    /**
     * Ajouter une sortie au fichier pour une chaine
     * @param code
     * @param sorties
     */
    public static void ajouterCsvSortie(String code, List<Couple> sorties) { 
        // Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("sorties.csv", true));
			String s = code ;
	        for(Couple c : sorties) {
	        	s += ";" + c.getCode() + "," + c.getQte();
	        }
	        s += "\n";
	        //ecrit la chaine de charactere
	        writer.write(s);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
    }
    
    /**
     * réécriture du fichier des sorties
     * @param code
     * @param entrees
     */
    public static void writeCsvSortie() { 
        // Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("sorties.csv"));
			String s = "Code;Sorties \n";
	        //ecrit la chaine de charactere
	        writer.write(s);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
    }
    
    /**
     * Ecrire le fichier de chaines de production
     * @param chaines
     */
    public static void writeCsvChaineProduction(List<ChaineProduction> chaines) { 
    	// Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("chaines.csv"));
			String s = "Code;Nom \n";
			ExportCsv.writeCsvEntree();
			ExportCsv.writeCsvSortie();
	        for(ChaineProduction c : chaines) {
	        	s += c.getCode() + ";" + c.getNom();
	        	s += "\n";
	        	ExportCsv.ajouterCsvEntree(c.getCode(), c.getEntrees());
	        	ExportCsv.ajouterCsvSortie(c.getCode(), c.getSorties());
	        }
	        //ecrit la chaine de charactere
	        writer.write(s);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
    }
    
    
    
    /**
     * Ecriture du fichier des achats avec en parametre le nom du fichier et la liste des Matieres premieres à acheter
     * @param nomFichier
     * @param elements
     */
    public static void writeCsvAchat(String nomFichier, List<Achat> mas) {
         FileWriter fileWriter = null;
                 
        try {
            fileWriter = new FileWriter(nomFichier);
 
            //Entête Csv
            fileWriter.append(FILE_HEADER_ACHAT.toString());
             
            //Nouvelle ligne
            fileWriter.append(NEW_LINE_SEPARATOR);
             
            for (Achat ma : mas) {
            		 fileWriter.append(ma.getChaine().getCode());
            		 fileWriter.append(DELIMITER);
            		 fileWriter.append(ma.getCode());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(ma.getNom());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(ma.getMesure());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(ma.getPrixAchat()));
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(ma.getQteA()));
                     fileWriter.append(DELIMITER);
            }
            System.out.println("Fichier créé avec succes");             
        } catch (Exception e) {
            e.printStackTrace();
        } finally {      
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }
        
    /**
     * Ecriture du fichier des produits manquants avec en parametre le nom du fichier et la liste des produits à produire
     * @param nomFichier
     * @param elements
     */
    public static void writeCsvProduitManquant(String nomFichier, List<ProduitManquant> pms) {
         FileWriter fileWriter = null;
                 
        try {
            fileWriter = new FileWriter(nomFichier);
 
            //Entête Csv
            fileWriter.append(FILE_HEADER_PRODUITM.toString());
             
            //Nouvelle ligne
            fileWriter.append(NEW_LINE_SEPARATOR);
             
            for (ProduitManquant ma : pms) {
            		 fileWriter.append(ma.getChaine().getCode());
            		 fileWriter.append(DELIMITER);
            		 fileWriter.append(ma.getCode());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(ma.getNom());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(ma.getMesure());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(ma.getQuantiteM()));
                     fileWriter.append(DELIMITER);
            }
            System.out.println("Fichier créé avec succes");             
        } catch (Exception e) {
            e.printStackTrace();
        } finally {      
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }
}