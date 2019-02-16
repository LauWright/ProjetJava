package production;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import element.Element;
import element.MatierePremiere;

public abstract class ExportCsv {
	
	//Delimiter used in CSV file
    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
     
    //CSV file header
    private static final String FILE_HEADER_ELEMENT = "Code;Nom;Quantite;unite;achat;vente;type";
 
    public static void writeCsvElement(List<Element> elements) {
         FileWriter fileWriter = null;
                 
        try {
            fileWriter = new FileWriter("elements1.csv");
 
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
    

    public static void ajouterCsvChaineProduction(List<Element> elements) { 
    	
    }
    
    public static void ajouterCsvEntree(String code, List<Couple> entrees) { 
        // Crée un BufferedWriter.
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("entrees1.csv", true));
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
}
   

