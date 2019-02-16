package production;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import element.Element;
import element.MatierePremiere;
import element.Produit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public abstract class ImportCsv {
	
	public static List<Element> importElement(String nomFichier, char separateur){
		List<Element> elements = new ArrayList<>();
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
				elements.add(e);
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

	public static List<ChaineProduction> importChaineProduction(String nomFichier, char separateur){
		List<ChaineProduction> chaineProductions = new ArrayList<>();
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
				chaineProductions.add(ch);
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
	public static List<Couple> importCouple(String nomFichier, char separateur, String codeChaine){
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
}


