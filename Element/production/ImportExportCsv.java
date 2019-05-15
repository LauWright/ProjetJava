package production;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

public class ImportExportCsv implements ImportExport {
	// Delimiter used in CSV file
	private static final String DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";

	// CSV file header
	private static final String FILE_HEADER_ELEMENT = "Code;Nom;Quantite;unite;achat;vente;coutFabrication;type";
	private static final String FILE_HEADER_ACHAT = "Chaine;Element;Nom;Mesure;Achat;Quantité";
	private static final String FILE_HEADER_PRODUITM = "Chaine;Element;Nom;Mesure;Quantité";

	// Pour les programmation
	private static final String FILE_HEADER_STOCK_ENTREE_SORTIE = "IdProg;IdSemaine;Code;Nom;Quantite;unite;achat;vente;type";
	private static final String FILE_HEADER_NIVEAU_CHAINE = "IdProg;IdSemaine;Niveau;Code;Nom";
	private static final String FILE_HEADER_STOCK_SEMAINE_ACHAT_PRODUITM = "IdProg;IdSemaine;Chaine;Element;Nom;Quantite;Mesure;Vente;Achat;Quantité";
	private static final String FILE_HEADER_STOCK_RESULTAT = "IdProg;IdSemaine;Resultat";

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////IMPORT/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Importation des éléments
	 * 
	 * @param nomFichier
	 * @param separateur
	 * @return
	 */
	@Override
	public ObservableMap<String, Element> importElement(String nomFichier, char separateur) {
		ObservableMap<String, ChaineProduction> chaines = importChaineProduction("chaines.csv", ';');
		ObservableMap<String, Element> elements = FXCollections.observableHashMap();
		CSVParser parser = new CSVParserBuilder().withSeparator(separateur).withIgnoreQuotations(true).build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(nomFichier)).withSkipLines(1).withCSVParser(parser).build();
			String[] line;
			while ((line = reader.readNext()) != null) {
				// -1.0 signifie que l'élément n'a pas de prix de vente
				double dVente = -1.0;
				if (!line[5].equals("NA")) {
					dVente = Double.valueOf(line[5]);
				}

				// création d'un element à ajouter à la liste des elements grace au CSV
				// création de l'élément en fonction du type contenu dans le csv
				Element e = null;
				if (line[7].equals("MA")) {
					e = new MatierePremiere(line[0], line[1], Double.valueOf(line[2]), line[3], dVente, Double.valueOf(line[4]));
				}
				if (line[7].equals("P")) {
					double dAchat = -1.0;
					boolean achetable = false;
					if (!line[4].equals("NA")) {
						dAchat = Double.valueOf(line[4]);
						achetable = true;
					}					
					e = new Produit(line[0], line[1], Double.valueOf(line[2]), line[3], dVente, dAchat, achetable, Double.valueOf(line[6]));
				}
				elements.put(e.getCode(), e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
	 * 
	 * @param nomFichier
	 * @param separateur
	 * @return
	 */
	@Override
	public ObservableMap<String, ChaineProduction> importChaineProduction(String nomFichier, char separateur) {
		ObservableMap<String, ChaineProduction> chaineProductions = FXCollections.observableHashMap();
		CSVParser parser = new CSVParserBuilder().withSeparator(separateur).withIgnoreQuotations(true).build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(nomFichier)).withSkipLines(1).withCSVParser(parser).build();
			String[] line;
			while ((line = reader.readNext()) != null) {
				// Récupère les entrées de la chaine de production à créer
				List<Couple> entrees = new ArrayList<>();
				entrees = importCouple("entrees.csv", ';', line[0]);
				// Récupère les sorties de la chaine de production à créer
				List<Couple> sorties = new ArrayList<>();
				sorties = importCouple("sorties.csv", ';', line[0]);

				ChaineProduction ch = new ChaineProduction(line[0], line[1], entrees, sorties);
				chaineProductions.put(ch.getCode(), ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
	 * 
	 * @param nomFichier
	 * @param separateur
	 * @return une liste d'entrées ou de sorties pour une chaine de production
	 */
	@Override
	public List<Couple> importCouple(String nomFichier, char separateur, String codeChaine) {
		List<Couple> couples = new ArrayList<>();
		CSVParser parser = new CSVParserBuilder().withSeparator(separateur).withIgnoreQuotations(true).build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(nomFichier)).withSkipLines(1).withCSVParser(parser).build();
			String[] line;
			while ((line = reader.readNext()) != null) {
				if (line[0].equals(codeChaine)) {
					for (int i = 1; i < line.length; i++) {
						String[] couple = line[i].split(",");
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

	public List<Programmation> importProgrammations(ObservableMap<String, ChaineProduction> ListeChaine) {
		List<Programmation> programmations = new ArrayList<>();
		CSVParser parser = new CSVParserBuilder().withSeparator(';').withIgnoreQuotations(true).build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader("SemaineResultat.csv")).withSkipLines(1).withCSVParser(parser)
					.build();
			String[] line;
			int i = 0;
			boolean b = false;
			while ((line = reader.readNext()) != null) {
				i = Integer.parseInt(line[0]);
				for (Programmation p : programmations) {
					if (p.getId() == i) {
						b = true;
						Semaine sem = new Semaine(Integer.parseInt(line[1]));
						sem.setResultat(Double.parseDouble(line[2]));
						p.getSemaines().add(sem);
					}
				}
				if (!b) {
					Programmation p = new Programmation(i, new ArrayList<Semaine>());
					Semaine sem = new Semaine(Integer.parseInt(line[1]));
					sem.setResultat(Double.parseDouble(line[2]));
					p.getSemaines().add(sem);
					programmations.add(p);
				}
				b = false;
			}
			reader.close();

			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					s.setStockPreviSortie(FXCollections.observableHashMap());
				}
			}
			CSVReader reader1 = new CSVReaderBuilder(new FileReader("SemaineStockSortie.csv")).withSkipLines(1).withCSVParser(parser).build();
			String[] line1;
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					while ((line1 = reader1.readNext()) != null) {
						if (Integer.parseInt(line1[0]) == p.getId() && Integer.parseInt(line1[1]) == s.getIdSemaine()) {
							if (line1[8].equals("P")) {
								double d = -1;
								boolean a = false;
								if (!line1[6].equals("NA")) {
									d = Double.valueOf(line1[6]);
									a = true;
								}
								double v = -1;
								if (!line1[7].equals("NA")) {
									v = Double.valueOf(line1[7]);
								}
								System.out.println(line1[2]);
								Produit tmp = new Produit((Produit)importElement("newElements.csv", ';').get(line1[2]));
								Element e = new Produit(line1[2], line1[3], Double.valueOf(line1[4]), line1[5], v, d, a,  tmp.getCoutFabrication());
								s.getStockPreviSortie().put(line1[2], e);
							}
							if (line1[8].equals("MA")) {
								double d = -1;
								if (!line1[6].equals("NA")) {
									d = Double.valueOf(line1[6]);
								}
								double v = -1;
								if (!line1[7].equals("NA")) {
									v = Double.valueOf(line1[7]);
								}
								Element e = new MatierePremiere(line1[2], line1[3], Double.valueOf(line1[4]), line1[5], v, d);
								s.getStockPreviSortie().put(line1[2], e);
							}
						}
					}
				}
			}
			reader1.close();
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					s.setStockPreviEntree(FXCollections.observableHashMap());
				}
			}

			CSVReader reader2 = new CSVReaderBuilder(new FileReader("SemaineStockEntre.csv")).withSkipLines(1)
					.withCSVParser(parser).build();
			String[] line2;
			while ((line2 = reader2.readNext()) != null) {
				for (Programmation p : programmations) {
					for (Semaine s : p.getSemaines()) {
						if (Integer.parseInt(line2[0]) == p.getId() && Integer.parseInt(line2[1]) == s.getIdSemaine()) {
							if (line2[8].equals("P")) {
								double d = -1;
								boolean a = false;
								if (!line2[6].equals("NA")) {
									d = Double.valueOf(line2[6]);
									a = true;
								}
								double v = -1;
								if (!line2[7].equals("NA")) {
									v = Double.valueOf(line2[7]);
								}
								Produit tmp = new Produit((Produit)importElement("newElements.csv", ';').get(line2[2]));
								Element e = new Produit(line2[2], line2[3], Double.valueOf(line2[4]), line2[5], v, d, a, tmp.getCoutFabrication());
								s.getStockPreviEntree().put(line2[2], e);
							}
							if (line2[8].equals("MA")) {
								double d = -1;
								if (!line2[6].equals("NA")) {
									d = Double.valueOf(line2[6]);
								}
								double v = -1;
								if (!line2[7].equals("NA")) {
									v = Double.valueOf(line2[7]);
								}
								Element e = new MatierePremiere(line2[2], line2[3], Double.valueOf(line2[4]), line2[5],
										v, d);
								s.getStockPreviEntree().put(line2[2], e);
							}
						}
					}
				}
			}
			reader2.close();

			CSVReader reader3 = new CSVReaderBuilder(new FileReader("SemaineNiveauChaines.csv")).withSkipLines(1)
					.withCSVParser(parser).build();
			String[] line3;
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					while ((line3 = reader3.readNext()) != null) {
						if (Integer.parseInt(line3[0]) == p.getId() && Integer.parseInt(line3[1]) == s.getIdSemaine()) {
							// Récupère les entrées de la chaine de production à créer
							List<Couple> entrees = new ArrayList<>();
							entrees = importCouple("entrees.csv", ';', line3[3]);
							// Récupère les sorties de la chaine de production à créer
							List<Couple> sorties = new ArrayList<>();
							sorties = importCouple("sorties.csv", ';', line3[3]);

							ChaineProduction c = new ChaineProduction(line3[3], line3[4], entrees, sorties);
							if (s.getChaineProductionNiveau().containsKey(Integer.parseInt(line3[2]))) {
								s.getChaineProductionNiveau().get(Integer.parseInt(line3[2])).add(c);
							} else {
								List<ChaineProduction> chaines = new ArrayList<>();
								chaines.add(c);
								s.getChaineProductionNiveau().put(Integer.parseInt(line3[2]), chaines);
							}
						}
					}
				}
			}
			reader3.close();

			CSVReader reader4 = new CSVReaderBuilder(new FileReader("SemaineAchats.csv")).withSkipLines(1)
					.withCSVParser(parser).build();
			String[] line4;
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					while ((line4 = reader4.readNext()) != null) {
						if (Integer.parseInt(line4[0]) == p.getId() && Integer.parseInt(line4[1]) == s.getIdSemaine()) {
							double d = -1;
							if (!line4[7].equals("NA")) {
								d = Double.valueOf(line4[7]);
							}
							double a = -1;
							if (!line4[8].equals("NA")) {
								a = Double.valueOf(line4[8]);
							}

							ChaineProduction c = ListeChaine.get(line4[2]);

							Achat achat = new Achat(line4[3], line4[4], Double.valueOf(line4[5]), line4[6], d, a,
									Double.valueOf(line4[9]), c);
							s.getAchats().add(achat);
						}
					}
				}
			}
			reader4.close();

			CSVReader reader5 = new CSVReaderBuilder(new FileReader("SemaineAchats.csv")).withSkipLines(1)
					.withCSVParser(parser).build();
			String[] line5;
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					while ((line5 = reader4.readNext()) != null) {
						if (Integer.parseInt(line5[0]) == p.getId() && Integer.parseInt(line5[1]) == s.getIdSemaine()) {
							double d = -1;
							if (!line5[7].equals("NA")) {
								d = Double.valueOf(line5[7]);
							}
							double a = -1;
							boolean aa = false;
							if (!line5[8].equals("NA")) {
								a = Double.valueOf(line5[8]);
								aa = true;
							}

							ChaineProduction c = ListeChaine.get(line5[2]);
							Produit tmp = new Produit((Produit)importElement("newElements.csv", ';').get(line5[3]));
							ProduitManquant pm = new ProduitManquant(line5[3], line5[4], Double.valueOf(line5[5]),
									line5[6], d, a, aa, tmp.getCoutFabrication(), Double.valueOf(line5[9]), c);
							s.getProduitManquant().add(pm);
						}
					}
				}
			}
			reader5.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return programmations;
	}

	/**
	 * Créer une liste de demandes
	 * @param nomFichier
	 * @param separateur
	 * @return une liste de demandes
	 */
	@Override
	public List<Demande> importDemande(String nomFichier, char separateur){
		ObservableList<Demande> demandes = FXCollections.observableArrayList();
		CSVParser parser = new CSVParserBuilder().withSeparator(separateur).withIgnoreQuotations(true).build();
		CSVReader reader = null;
		try {
			reader = new CSVReaderBuilder(new FileReader(nomFichier)).withSkipLines(1).withCSVParser(parser).build();
			String[] line;
			while ((line = reader.readNext()) != null) {
				demandes.add(new Demande(Integer.parseInt(line[0]),line[1],Double.parseDouble(line[2])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return demandes;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////EXPORT/////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Réécriture du fichier élément avec en parametre le nom du fichier dans lequel
	 * on vet réécrire la liste d'éléments
	 * 
	 * @param nomFichier
	 * @param elements
	 */
	@Override
	public void writeCsvElement(String nomFichier, Map<String, Element> elements) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(nomFichier);

			// Entête Csv
			fileWriter.append(FILE_HEADER_ELEMENT.toString());

			// Nouvelle ligne
			fileWriter.append(NEW_LINE_SEPARATOR);
			for (Map.Entry<String, Element> e : elements.entrySet()) {
				if (e.getValue().getClass().getSimpleName().equals("Produit")) {
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
				if (e.getValue().getClass().getSimpleName().equals("MatierePremiere")) {
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
					if (ma.getPrixVente() == -1.0) {
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
	 * 
	 * @param element
	 */
	@Override
	public void ajouterCsvElement(Element element) {
		// Crée un BufferedWriter.
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("elements1.csv", true));
			String s = "";
			s += element.getCode() + ";" + element.getNom() + ";" + element.getQuantite() + ";" + element.getMesure();
			if (element.getClass().getSimpleName().equals("MatierePremiere")) {
				MatierePremiere ma = (MatierePremiere) element;
				s += ";" + ma.getPrixAchat();
				if (ma.getPrixVente() == -1.0) {
					s += ";" + "NA";
				} else {
					s += ";" + ma.getPrixVente();
				}
				s += ";" + "MA";
			} else {
				s += ";" + "NA" + ";" + element.getPrixVente() + ";" + "P";
			}
			s += "\n";
			// ecrit la chaine de charactere
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aouter une chaine de production au fichier
	 * 
	 * @param chaines
	 */
	@Override
	public void ajouterCsvChaineProduction(Map<String, ChaineProduction> chaines) {
		// Crée un BufferedWriter.
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("chaine.csv", true));
			String s = "";

			for (Map.Entry<String, ChaineProduction> c : chaines.entrySet()) {
				s += c.getValue().getCode() + ";" + c.getValue().getNom();
				s += "\n";
				ajouterCsvEntree(c.getValue().getCode(), c.getValue().getEntrees());
				ajouterCsvSortie(c.getValue().getCode(), c.getValue().getSorties());
			}
			// ecrit la chaine de charactere
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Ajouter une entree pour une chaine
	 * 
	 * @param code
	 * @param entrees
	 */
	@Override
	public void ajouterCsvEntree(String code, List<Couple> entrees) {
		// Crée un BufferedWriter.
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("entrees.csv", true));
			String s = code;
			for (Couple c : entrees) {
				s += ";" + c.getCode() + "," + c.getQte();
			}
			s += "\n";
			// ecrit la chaine de charactere
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * réécriture du fichier des entrees
	 * 
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
			// ecrit la chaine de charactere
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ajouter une sortie au fichier pour une chaine
	 * 
	 * @param code
	 * @param sorties
	 */
	@Override
	public void ajouterCsvSortie(String code, List<Couple> sorties) {
		// Crée un BufferedWriter.
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("sorties.csv", true));
			String s = code;
			for (Couple c : sorties) {
				s += ";" + c.getCode() + "," + c.getQte();
			}
			s += "\n";
			// ecrit la chaine de charactere
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * réécriture du fichier des sorties
	 * 
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
			// ecrit la chaine de charactere
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ecrire le fichier de chaines de production
	 * 
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

			for (Map.Entry<String, ChaineProduction> c : chaines.entrySet()) {
				s += c.getValue().getCode() + ";" + c.getValue().getNom();
				s += "\n";
				ajouterCsvEntree(c.getValue().getCode(), c.getValue().getEntrees());
				ajouterCsvSortie(c.getValue().getCode(), c.getValue().getSorties());
			}
			// ecrit la chaine de charactere
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ecriture du fichier des achats avec en parametre le nom du fichier et la
	 * liste des Matieres premieres à acheter
	 * 
	 * @param nomFichier
	 * @param elements
	 */
	@Override
	public void writeCsvAchat(String nomFichier, List<Achat> mas) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(nomFichier);

			// Entête Csv
			fileWriter.append(FILE_HEADER_ACHAT.toString());

			// Nouvelle ligne
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
	 * Ecriture du fichier des produits manquants avec en parametre le nom du
	 * fichier et la liste des produits à produire
	 * 
	 * @param nomFichier
	 * @param elements
	 */
	@Override
	public void writeCsvProduitManquant(String nomFichier, List<ProduitManquant> pms) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(nomFichier);

			// Entête Csv
			fileWriter.append(FILE_HEADER_PRODUITM.toString());

			// Nouvelle ligne
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

	/**
	 * Ecriture du fichier des programmations avec en parametre le nom du fichier et
	 * la liste des programmations
	 * 
	 * @param nomFichier
	 * @param elements
	 */
	public void writeCsvProgrammation(List<Programmation> programmations) {
		// Crée un BufferedWriter.
		try {

			FileWriter fileWriter = new FileWriter("SemaineStockEntre.csv");
			////// Création du csv pour les stocks en entre des semaine pour chaque
			////// programme
			// Entête Csv
			fileWriter.append(FILE_HEADER_STOCK_ENTREE_SORTIE.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.flush();
			fileWriter.close();
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					if (s.getStockPreviEntree() != null && s.getResultat() > 0) {
						this.writeCsvStockEntreeSortie("SemaineStockEntre.csv", s.getIdSemaine(), p.getId(),
								s.getStockPreviEntree());
					}
				}
			}

			FileWriter fileWriter1 = new FileWriter("SemaineStockSortie.csv");
			////// Création du csv pour les stocks en sortie des semaine pour chaque
			////// programme
			// Entête Csv
			fileWriter1.append(FILE_HEADER_STOCK_ENTREE_SORTIE.toString());
			fileWriter1.append(NEW_LINE_SEPARATOR);
			fileWriter1.flush();
			fileWriter1.close();
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					if (s.getStockPreviSortie() != null && s.getResultat() > 0) {
						this.writeCsvStockEntreeSortie("SemaineStockSortie.csv", s.getIdSemaine(), p.getId(),
								s.getStockPreviSortie());
					}
				}
			}

			FileWriter fileWriter2 = new FileWriter("SemaineNiveauChaines.csv");
			////// Création du csv pour les stocks en sortie des semaine pour chaque
			////// programme
			// Entête Csv
			fileWriter2.append(FILE_HEADER_NIVEAU_CHAINE.toString());
			fileWriter2.append(NEW_LINE_SEPARATOR);
			fileWriter2.flush();
			fileWriter2.close();
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					for (Integer cle : s.getChaineProductionNiveau().keySet()) {
						if (s.getResultat() > 0) {
							this.writeCsvChaineNiveau("SemaineNiveauChaines.csv", s.getIdSemaine(), p.getId(), cle,
									s.getChaineProductionNiveau().get(cle));
						}
					}
				}
			}

			FileWriter fileWriter3 = new FileWriter("SemaineAchats.csv");
			////// Création du csv pour les achats des semaine pour chaque
			////// programme
			// Entête Csv
			fileWriter3.append(FILE_HEADER_STOCK_SEMAINE_ACHAT_PRODUITM.toString());
			fileWriter3.append(NEW_LINE_SEPARATOR);
			fileWriter3.flush();
			fileWriter3.close();
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					if (s.getResultat() > 0) {
						this.writeCsvSemaineAchat("SemaineAchats.csv", s.getIdSemaine(), p.getId(), s.getAchats());
					}
				}
			}

			FileWriter fileWriter4 = new FileWriter("SemaineProduitM.csv");

			////// Création du csv pour les produits manquants des semaine pour chaque
			////// programme
			// Entête Csv
			fileWriter4.append(FILE_HEADER_STOCK_SEMAINE_ACHAT_PRODUITM.toString());
			fileWriter4.append(NEW_LINE_SEPARATOR);
			fileWriter4.flush();
			fileWriter4.close();
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					if (s.getResultat() > 0) {
						this.writeCsvSemaineProduitM("SemaineProduitM.csv", s.getIdSemaine(), p.getId(),
								s.getProduitManquant());
					}
				}
			}

			FileWriter fileWriter5 = new FileWriter("SemaineResultat.csv");

			////// Création du csv pour les stocks en sortie des semaine pour chaque
			////// programme
			// Entête Csv
			fileWriter5.append(FILE_HEADER_STOCK_RESULTAT.toString());
			fileWriter5.append(NEW_LINE_SEPARATOR);
			fileWriter5.flush();
			fileWriter5.close();
			for (Programmation p : programmations) {
				for (Semaine s : p.getSemaines()) {
					if (s.getResultat() > 0) {
						this.writeCsvSemaineResultat("SemaineResultat.csv", s.getIdSemaine(), p.getId(),
								s.getResultat());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ecriture des stock en entre ou en sortie pour une semaine
	 * 
	 * @param nomFichier
	 * @param idSemaine
	 * @param idProg
	 * @param stock
	 */
	public void writeCsvStockEntreeSortie(String nomFichier, int idSemaine, int idProg, Map<String, Element> stock) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(nomFichier, true);

			// Nouvelle ligne
			ObservableList<Map.Entry<String, Element>> elements = FXCollections.observableArrayList(stock.entrySet());
			for (Entry<String, Element> e : elements) {
				if (e.getValue().getClass().getSimpleName().equals("Produit")) {
					fileWriter.append(String.valueOf(idProg));
					fileWriter.append(DELIMITER);
					fileWriter.append(String.valueOf(idSemaine));
					fileWriter.append(DELIMITER);
					fileWriter.append(e.getValue().getCode());
					fileWriter.append(DELIMITER);
					fileWriter.append(e.getValue().getNom());
					fileWriter.append(DELIMITER);
					fileWriter.append(String.valueOf(e.getValue().getQuantite()));
					fileWriter.append(DELIMITER);
					fileWriter.append(e.getValue().getMesure());
					fileWriter.append(DELIMITER);
					if (e.getValue().getPrixAchat() == -1.0) {
						fileWriter.append("NA");
						fileWriter.append(DELIMITER);
					} else {
						fileWriter.append(String.valueOf(e.getValue().getPrixAchat()));
						fileWriter.append(DELIMITER);
					}
					fileWriter.append(String.valueOf(e.getValue().getPrixVente()));
					fileWriter.append(DELIMITER);
					fileWriter.append("P");
					fileWriter.append(NEW_LINE_SEPARATOR);
				}
				if (e.getValue().getClass().getSimpleName().equals("MatierePremiere")) {
					MatierePremiere ma = (MatierePremiere) e.getValue();
					fileWriter.append(String.valueOf(idProg));
					fileWriter.append(DELIMITER);
					fileWriter.append(String.valueOf(idSemaine));
					fileWriter.append(DELIMITER);
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
					if (ma.getPrixVente() == -1.0) {
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
	 * Ecriture des chaines et niveau pour une semaine
	 * 
	 * @param nomFichier
	 * @param idSemaine
	 * @param idProg
	 * @param niveau
	 * @param chaines
	 */
	public void writeCsvChaineNiveau(String nomFichier, int idSemaine, int idProg, int niveau,
			List<ChaineProduction> chaines) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(nomFichier, true);

			// Nouvelle ligne
			for (ChaineProduction c : chaines) {
				fileWriter.append(String.valueOf(idProg));
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(idSemaine));
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(niveau));
				fileWriter.append(DELIMITER);
				fileWriter.append(c.getCode());
				fileWriter.append(DELIMITER);
				fileWriter.append(c.getNom());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
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
	 * Ecriture des achats pour une semaine
	 * 
	 * @param nomFichier
	 * @param idSemaine
	 * @param idProg
	 * @param achats
	 */
	public void writeCsvSemaineAchat(String nomFichier, int idSemaine, int idProg, List<Achat> achats) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(nomFichier, true);
			// Nouvelle ligne
			for (Achat ma : achats) {
				fileWriter.append(String.valueOf(idProg));
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(idSemaine));
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getChaine().getCode());
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getCode());
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getNom());
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(ma.getQuantite()));
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getMesure());
				fileWriter.append(DELIMITER);
				if (ma.getPrixVente() == -1.0) {
					fileWriter.append("NA");
					fileWriter.append(DELIMITER);
				} else {
					fileWriter.append(String.valueOf(ma.getPrixVente()));
					fileWriter.append(DELIMITER);
				}
				fileWriter.append(String.valueOf(ma.getPrixAchat()));
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(ma.getQteA()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
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
	 * Ecriture des produits manquants pour une semaine
	 * 
	 * @param nomFichier
	 * @param idSemaine
	 * @param idProg
	 * @param produitsManquants
	 */
	public void writeCsvSemaineProduitM(String nomFichier, int idSemaine, int idProg,
			List<ProduitManquant> produitsManquants) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(nomFichier, true);
			// Nouvelle ligne
			for (ProduitManquant ma : produitsManquants) {
				fileWriter.append(String.valueOf(idProg));
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(idSemaine));
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getChaine().getCode());
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getCode());
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getNom());
				fileWriter.append(DELIMITER);
				fileWriter.append(String.valueOf(ma.getQuantite()));
				fileWriter.append(DELIMITER);
				fileWriter.append(ma.getMesure());
				fileWriter.append(DELIMITER);
				if (ma.getPrixVente() == -1.0) {
					fileWriter.append("NA");
					fileWriter.append(DELIMITER);
				} else {
					fileWriter.append(String.valueOf(ma.getPrixVente()));
					fileWriter.append(DELIMITER);
				}
				if (ma.getPrixAchat() == -1.0) {
					fileWriter.append("NA");
					fileWriter.append(DELIMITER);
				} else {
					fileWriter.append(String.valueOf(ma.getPrixAchat()));
					fileWriter.append(DELIMITER);
				}
				fileWriter.append(String.valueOf(ma.getQuantiteM()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
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
	 * Ecriture des resultats pour une semaine
	 * 
	 * @param nomFichier
	 * @param idSemaine
	 * @param idProg
	 * @param resultat
	 */
	public void writeCsvSemaineResultat(String nomFichier, int idSemaine, int idProg, double resultat) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(nomFichier, true);
			// Nouvelle ligne
			fileWriter.append(String.valueOf(idProg));
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(idSemaine));
			fileWriter.append(DELIMITER);
			fileWriter.append(String.valueOf(resultat));
			fileWriter.append(NEW_LINE_SEPARATOR);
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
