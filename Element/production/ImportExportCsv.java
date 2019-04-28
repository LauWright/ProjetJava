package production;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import element.Achat;
import element.Element;
import element.MatierePremiere;
import element.Produit;
import element.ProduitManquant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ImportExportCsv implements ImportExport{
	//Delimiter used in CSV file
    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
     
    //CSV file header
    private static final String FILE_HEADER_ELEMENT = "Code;Nom;Quantite;unite;achat;vente;type";
    private static final String FILE_HEADER_ACHAT= "Chaine;Element;Nom;Mesure;Achat;Quantité";
    private static final String FILE_HEADER_PRODUITM= "Chaine;Element;Nom;Mesure;Quantité";
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////IMPORT/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Importation des éléments
	 * @param nomFichier
	 * @param separateur
	 * @return
	 */
	@Override
	public ObservableMap<String, Element> importElement(String nomFichier, char separateur){
		ObservableMap<String, Element> elements = FXCollections.observableHashMap();
		CSVParser parser = new CSVParserBuilder().withSeparator(separateur)
				                                 .withIgnoreQuotations(true)
                                                 .build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(nomFichier)).withSkipLines(1)
                                                                     .withCSVParser(parser)
                                                                     .build();
			String [] line;
			while ((line = reader.readNext()) != null)
			{
				// -1.0 signifie que l'élément n'a pas de prix de vente
            	double d = -1.0;
            	if(!line[5].equals("NA")) {
            		d= Double.valueOf(line[5]);
            	}
                
                //création d'un element à ajouter à la liste des elements grace au CSV
                //création de l'élément en fonction du type contenu dans le csv
                Element e = null;
                if (line[6].equals("MA")) {
                	e = new MatierePremiere(line[0], line[1], Double.valueOf(line[2]), line[3], d, Double.valueOf(line[4]));
                	
                }
                if (line[6].equals("P")) {
                	e = new Produit(line[0], line[1], Double.valueOf(line[2]), line[3], d);
                	
                }
				elements.put(e.getCode(), e);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return elements;
	}

	/**
	 * Importation des chaines de production
	 * @param nomFichier
	 * @param separateur
	 * @return
	 */
	@Override
	public ObservableMap<String, ChaineProduction> importChaineProduction(String nomFichier, char separateur){
		ObservableMap<String, ChaineProduction> chaineProductions = FXCollections.observableHashMap();
		CSVParser parser = new CSVParserBuilder().withSeparator(separateur)
				                                 .withIgnoreQuotations(true)
                                                 .build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(nomFichier)).withSkipLines(1)
                                                                     .withCSVParser(parser)
                                                                     .build();
			String [] line;
			while ((line = reader.readNext()) != null)
			{
				//Récupère les entrées de la chaine de production à créer
				List<Couple> entrees = new ArrayList<>();
				entrees = importCouple("entrees.csv", ';', line[0]);
				//Récupère les sorties de la chaine de production à créer
				List<Couple> sorties = new ArrayList<>();
				sorties = importCouple("sorties.csv", ';', line[0]);
				
				ChaineProduction ch = new ChaineProduction(line[0], line[1], entrees, sorties);
				chaineProductions.put(ch.getCode(), ch);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return chaineProductions;
	}

	
	/**
	 * Créer une liste de d'entrées ou de sorties pour une chaine de production
	 * @param nomFichier
	 * @param separateur
	 * @return une liste d'entrées ou de sorties pour une chaine de production
	 */
	@Override
	public List<Couple> importCouple(String nomFichier, char separateur, String codeChaine){
		List<Couple> couples = new ArrayList<>();
		CSVParser parser = new CSVParserBuilder().withSeparator(separateur)
												 .withIgnoreQuotations(true)
				                                 .build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(nomFichier)).withSkipLines(1)
					                                                 .withCSVParser(parser)
					                                                 .build();
			String [] line;
			while ((line = reader.readNext()) != null)
			{
				if(line[0].equals(codeChaine)) {
					for(int i=1; i < line.length; i++ )
					{
						String [] couple = line[i].split(",");
						Couple c = new Couple(couple[0], Double.valueOf(couple[1]));
						couples.add(c);
					}
				}
			}	
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return couples;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////EXPORT/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * Réécriture du fichier élément avec en parametre le nom du fichier dans lequel on vet réécrire la liste d'éléments
     * @param nomFichier
     * @param elements
     */
	@Override
	public void writeCsvElement(String nomFichier, Map<String, Element> elements) {
         FileWriter fileWriter = null;
                 
        try {
            fileWriter = new FileWriter(nomFichier);
 
            //Entête Csv
            fileWriter.append(FILE_HEADER_ELEMENT.toString());
             
            //Nouvelle ligne
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (Map.Entry<String, Element> e : elements.entrySet()) {
            	if(e.getValue().getClass().getSimpleName().equals("Produit")) {
            		 fileWriter.append(e.getValue().getCode());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(e.getValue().getNom());
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(e.getValue().getQuantite()));
                     fileWriter.append(DELIMITER);
                     fileWriter.append(e.getValue().getMesure());
                     fileWriter.append(DELIMITER);
                     fileWriter.append("NA");
                     fileWriter.append(DELIMITER);
                     fileWriter.append(String.valueOf(e.getValue().getPrixVente()));
                     fileWriter.append(DELIMITER);
                     fileWriter.append("P");
                     fileWriter.append(NEW_LINE_SEPARATOR);
            	}
            	if(e.getValue().getClass().getSimpleName().equals("MatierePremiere")) {
            		 MatierePremiere ma = (MatierePremiere) e.getValue();
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
	@Override
    public void ajouterCsvElement(Element element) { 
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
	@Override
    public void ajouterCsvChaineProduction(Map<String, ChaineProduction> chaines) { 
    	// Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("chaine.csv", true));
			String s = "";
			
			 for (Map.Entry<String, ChaineProduction> c:  chaines.entrySet()) {
	        	s += c.getValue().getCode() + ";" + c.getValue().getNom();
	        	s += "\n";
	        	ajouterCsvEntree(c.getValue().getCode(), c.getValue().getEntrees());
	        	ajouterCsvSortie(c.getValue().getCode(), c.getValue().getSorties());
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
	@Override
    public void ajouterCsvEntree(String code, List<Couple> entrees) { 
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
	@Override
    public void writeCsvEntree() { 
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
	@Override
    public void ajouterCsvSortie(String code, List<Couple> sorties) { 
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
	@Override
    public void writeCsvSortie() { 
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
	@Override
    public void writeCsvChaineProduction(Map<String, ChaineProduction> chaines) { 
    	// Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("chaines.csv"));
			String s = "Code;Nom \n";
			writeCsvEntree();
			writeCsvSortie();
			
			for (Map.Entry<String, ChaineProduction> c:  chaines.entrySet()) {
	        	s += c.getValue().getCode() + ";" + c.getValue().getNom();
	        	s += "\n";
	        	ajouterCsvEntree(c.getValue().getCode(), c.getValue().getEntrees());
	        	ajouterCsvSortie(c.getValue().getCode(), c.getValue().getSorties());
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
	@Override
    public void writeCsvAchat(String nomFichier, List<Achat> mas) {
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
                     fileWriter.append("\n");
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
	@Override
    public void writeCsvProduitManquant(String nomFichier, List<ProduitManquant> pms) {
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
                     fileWriter.append("\n");
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


