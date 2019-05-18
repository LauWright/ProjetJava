package view;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import element.Achat;
import element.Element;
import element.ElementPrix;
import element.MatierePremiere;
import element.Produit;
import element.ProduitManquant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import production.ChaineProduction;
import production.Couple;
import production.ImportExportCsv;
import production.Programmation;
import production.Semaine;

public class SimulationProductionController {

	@FXML
	private ChoiceBox choiceSemaine;

	@FXML
	private GridPane gridChaine;
	@FXML
	private ScrollPane scrollChaine;
	@FXML
	private Label messageExport;
	@FXML
	private Button btnSimuler;
	
	@FXML
	private Button btnExporter;

	@FXML
	private GridPane buttonGrid;

	private int nbButtonRecap = 0;

	private Programmation programmation;

	private boolean newProg = false;
	
	private int index = -1;

	// reference l'application principale
	private MainApp mainApp;

	/**
	 * Constructeur
	 */
	public SimulationProductionController() {
	}

	/**
	 * Initialise le controller appelé automatiquement à l'ouverture de la page
	 * .fxml
	 * 
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {
	}

	/**
	 * appelé par l'application main
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		this.tableChaine();
		this.buttonRecap();
		this.btnExporter.setDisable(true);
		this.btnSimuler.setDisable(true);
	

		// Initialisation selecteur des semaines
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < 8; i++) {
			int week = calendar.get(calendar.WEEK_OF_YEAR);
			this.choiceSemaine.getItems().add("Semaine " + week);
			calendar.add(Calendar.DATE, 7);
		}
		this.choiceSemaine.getSelectionModel().selectFirst();
	}

	/**
	 * Initialise la liste deroulante des chaines avec niveau à saisir
	 */
	public void tableChaine() {
		Label chaineLabel = new Label("Chaine");
		Label nivLabel = new Label("Niveau");

		this.gridChaine.add(chaineLabel, 0, 0);
		this.gridChaine.add(nivLabel, 1, 0);

		ObservableList<Map.Entry<String, ChaineProduction>> chaines = FXCollections
				.observableArrayList(this.mainApp.getChaineData().entrySet());

		List<ChaineProduction> listeChaines = new ArrayList<>();
		for (Entry<String, ChaineProduction> c : chaines) {
			listeChaines.add(c.getValue());
		}

		Comparator<ChaineProduction> comparator = Comparator.comparing(ChaineProduction::getCode);
		listeChaines.sort(comparator);
		for (int i = 0; i < listeChaines.size(); i++) {
			CheckBox ch = new CheckBox();
			ch.setText(listeChaines.get(i).getCode() + " " + listeChaines.get(i).getNom());
			this.gridChaine.add(ch, 0, i + 1);
		}

		for (int i = 0; i < listeChaines.size(); i++) {
			TextField tf = new TextField();
			tf.setText("0");
			this.gridChaine.add(tf, 1, i + 1);
		}

		this.scrollChaine.setContent(gridChaine);
		this.scrollChaine.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scrollChaine.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		this.scrollChaine.setFitToHeight(true);
		this.scrollChaine.setPannable(true);
	}

	/**
	 * Affiche le recapitulatif d'une simulation pour une semaine donnée
	 */
	public void buttonRecap() {
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < 8; i++) {
			this.buttonGrid.getRowConstraints().get(0).setMinHeight(50);
			int week = calendar.get(calendar.WEEK_OF_YEAR);
			calendar.add(Calendar.DATE, 7);
			

			Label sem = new Label("Semaine " + week);

			Button recap = new Button("Récapitulatif");
			Button delete = new Button("Supprimer");

			sem.setVisible(false);
			recap.setVisible(false);
			recap.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					getRecap(sem.getText());
				}
			});
			delete.setVisible(false);
			this.buttonGrid.add(sem, 0, i);
			this.buttonGrid.add(recap, 1, i);
			this.buttonGrid.add(delete, 2, i);
			
		}
	}

	/**
	 * Clicque sur le bouton nouvelle programmation, crée une nouvelle programmation sur 8 semaines
	 */
	@FXML
	public void newProgrammation() {
		this.newProg = true;
		
		this.index = -1;
		this.choiceSemaine.getSelectionModel().selectFirst();
		if (this.programmation != null) {
			this.mainApp.getProgrammations().add(this.programmation);
		}
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
		alert.setContentText("Etes-vous sûr de vouloir démarrer \n" + "une nouvelle programmation?");
		alert.setHeaderText("");
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			this.btnExporter.setDisable(false);
			this.btnSimuler.setDisable(false);
			/// Réinitialiser les stocks prévisionnels
			List<Semaine> semaines = new ArrayList<>();
			Calendar calendar = Calendar.getInstance();
			for (int i = 0; i < 8; i++) {
				int week = calendar.get(calendar.WEEK_OF_YEAR);
				semaines.add(new Semaine(week));
				calendar.add(Calendar.DATE, 7);
			}
			this.programmation = new Programmation(this.mainApp.getnbProgrammation() + 1, semaines);
			this.programmation.getSemaines().get(0).setStockPreviEntree(new ImportExportCsv().importElement("newElements.csv", ';'));
			this.programmation.getSemaines().get(0).setStockPreviSortie(new ImportExportCsv().importElement("newElements.csv", ';'));
			for(int i=1; i<this.programmation.getSemaines().size(); i++) {
				this.programmation.getSemaines().get(i).setStockPreviEntreeNewPrix(new ImportExportCsv().importElement("newElements.csv", ';'));
				this.programmation.getSemaines().get(i).setStockPreviSortie(new ImportExportCsv().importElement("newElements.csv", ';'));
				this.programmation.getSemaines().get(i).setPrixStock(this.programmation.getSemaines().get(i).getStockPreviEntree());
			}
			
			
			this.mainApp.setnbProgrammation(this.mainApp.getnbProgrammation() + 1);
			System.out.println("ok");

			for (Node n : this.buttonGrid.getChildren()) {
				n.setVisible(false);
			}
		}

	}

	/**
	 * Clicque sur le bouton simuler
	 */
	@FXML
	public void simuler() {
		List<ElementPrix> listeElemPrix = new ArrayList<>();
		if (this.newProg == false) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Aucune programmation");
			alert.setHeaderText("Attention aucune programmation créée");
			alert.setContentText("Veuillez créer une nouvelle programmation\n");

			alert.showAndWait();
		} else {
			// verification que au moins une case est cochée
			boolean coche = false;
			for (int i = 0; i < this.mainApp.getChaineData().size(); i++) {
				for (Node no : this.gridChaine.getChildren()) {
					if (GridPane.getRowIndex(no) == i + 1 && GridPane.getColumnIndex(no) == 0) {
						CheckBox ch = (CheckBox) no;
						if (ch.isSelected()) {
							coche = true;
						}
					}
				}
			}
			if (coche == false) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("Aucune selection");
				alert.setHeaderText("Attention aucune chaîne selectionée");
				alert.setContentText("Veuillez selectionner une chaîne \n" + "et rentrer un niveau.");

				alert.showAndWait();
			} else {
				Semaine semaine = null;
				if (this.choiceSemaine.getSelectionModel().getSelectedIndex() == 0 && this.index == -1) {
					semaine = this.programmation.getSemaines()
							.get(this.choiceSemaine.getSelectionModel().getSelectedIndex());
				} else {
					if(this.index + 1 == this.choiceSemaine.getSelectionModel().getSelectedIndex()) {		
						semaine = this.programmation.getSemaines()
								.get(this.choiceSemaine.getSelectionModel().getSelectedIndex());
						if (this.programmation.getSemaines().get(this.choiceSemaine.getSelectionModel().getSelectedIndex() - 1).getStockPreviSortie() != null) {
							ObservableMap<String, Element> stockse = this.programmation.getSemaines()
									.get(this.choiceSemaine.getSelectionModel().getSelectedIndex() - 1).getStockPreviSortie();
							semaine.setQuantiteStock(stockse);
							semaine.setQuantiteStockSortie(stockse);
						} else {
							ObservableMap<String, Element> stockse = this.mainApp.getElementData();
							semaine.setStockPreviEntree(stockse);
							semaine.setQuantiteStockSortie(stockse);
						}
					} else if (this.index == this.choiceSemaine.getSelectionModel().getSelectedIndex()){
						semaine = this.programmation.getSemaines().get(this.choiceSemaine.getSelectionModel().getSelectedIndex());
					} else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.initOwner(mainApp.getPrimaryStage());
						alert.setTitle("Semaine incorrect");
						alert.setHeaderText("Attention semaine incorrecte");
						alert.setContentText("Veuillez choisir la semaine consécutif \n" + "à la précédente selectionné \n");

						alert.showAndWait();
						return;
					}
				}
				this.index = this.choiceSemaine.getSelectionModel().getSelectedIndex();
				this.mainApp.setSimulation(true);
				for (Node n : this.gridChaine.getChildren()) {
					boolean ok = false;
					CheckBox ch;
					for (int i = 0; i < this.mainApp.getChaineData().size(); i++) {
						if (GridPane.getRowIndex(n) == i + 1 && GridPane.getColumnIndex(n) == 0) {
							ch = (CheckBox) n;
							if (ch.isSelected()) {
								TextField tf = null;
								for (Node no : this.gridChaine.getChildren()) {
									if (GridPane.getRowIndex(no) == i + 1 && GridPane.getColumnIndex(no) == 1) {
										tf = (TextField) no;
									}
								}
								ChaineProduction c = this.mainApp.getChaineData().get(ch.getText().split(" ")[0]);
								int y = 0;
								for (Integer key : semaine.getChaineProductionNiveau().keySet()) {
									boolean place = false;
									List<ChaineProduction> chainesN = semaine.getChaineProductionNiveau().get(key);

									for (ChaineProduction cn : chainesN) {
										if (cn.getCode().equals(c.getCode())) {
											chainesN.remove(cn);
											place = true;
											y = key;
											List<ChaineProduction> newChaines = semaine.getChaineProductionNiveau()
													.get(key + Integer.parseInt(tf.getText()));
											if (newChaines == null) {
												ok = true;
												newChaines = new ArrayList<ChaineProduction>();
												newChaines.add(c);
												semaine.getChaineProductionNiveau()
														.put((Integer.parseInt(tf.getText()) + key), newChaines);

											} else {
												ok = true;
												semaine.getChaineProductionNiveau()
														.get(Integer.parseInt(tf.getText()) + key).add(c);

											}
										}
										if (chainesN.isEmpty()) {
											break;
										}
										i++;
									}
									if (place) {
										if (semaine.getChaineProductionNiveau().get(y).isEmpty()) {
											semaine.getChaineProductionNiveau().remove(y);
										}
										break;
									}
									if (semaine.getChaineProductionNiveau().keySet().isEmpty()) {
										break;
									}
								}

								if (!ok) {
									List<ChaineProduction> chaines = semaine.getChaineProductionNiveau()
											.get(Integer.parseInt(tf.getText()));
									if (chaines == null) {
										chaines = new ArrayList<ChaineProduction>();
										chaines.add(c);
										semaine.getChaineProductionNiveau().put(Integer.parseInt(tf.getText()),
												chaines);
									} else {
										semaine.getChaineProductionNiveau().get(Integer.parseInt(tf.getText())).add(c);
									}
								}
								List<Couple> entrees = c.getEntrees();
								boolean reussi = true;
								for (Couple couple : entrees) {
									Element e = semaine.getStockPreviSortie().get(couple.getCode());
									e.soustraire(couple.getQte() * Double.valueOf(tf.getText()));
									if (e.examiner()) {
										if (e.getClass().getSimpleName().equals("MatierePremiere")) {
											MatierePremiere ma = (MatierePremiere) e;
											if (ma.getPrixAchat() == -1) {
												e.ajouter(couple.getQte() * Double.valueOf(tf.getText()));
												reussi = false;
											} else {
												Semaine sem = this.programmation.getPrixMoinsCher(ma.getCode());
												if(sem.getIdSemaine() != semaine.getIdSemaine()) {
													listeElemPrix.add(new ElementPrix(c.getCode(), e.getCode(), e.getNom(), sem.getIdSemaine()));
												}else {
													semaine.getAchats().add(new Achat(ma.getCode(), ma.getNom(), e.getQuantite(),
															ma.getMesure(), ma.getPrixVente(), ma.getPrixAchat(), 0 - e.getQuantite(), c));
													List<Couple> sorties = c.getSorties();
													for (Couple couples : sorties) {
														semaine.getStockPreviSortie().get(couples.getCode()).ajouter(couples.getQte());
													}
												}
												if (e.getQuantite() < 0) {
													e.setQuantite(0);
												}
											}
										}
										if (e.getClass().getSimpleName().equals("Produit")) {
											Produit p = (Produit) e;
											if (e.getPrixAchat() == -1) {
												semaine.getProduitManquant().add(new ProduitManquant(p.getCode(),
														p.getNom(), p.getQuantite(), p.getMesure(), p.getPrixVente(),
														p.getPrixAchat(), p.isAchetable(), p.getCoutFabrication(), 0 - e.getQuantite(), c));
												e.ajouter(couple.getQte() * Double.valueOf(tf.getText()));
												reussi = false;
											} else {
												semaine.getAchats()
														.add(new Achat(p.getCode(), p.getNom(), e.getQuantite(),
																p.getMesure(), p.getPrixVente(), p.getPrixAchat(),
																0 - e.getQuantite(), c));
												List<Couple> sorties = c.getSorties();
												for (Couple couples : sorties) {
													semaine.getStockPreviSortie().get(couples.getCode()).ajouter(couples.getQte());
												}
												if (e.getQuantite() < 0) {
													e.setQuantite(0);
												}
												reussi = false;
											}
										}
									} else {
										List<Couple> sorties = c.getSorties();
										for (Couple couples : sorties) {
											semaine.getStockPreviSortie().get(couples.getCode()).ajouter(couples.getQte());
										}
									}
									
								}
							}
						}
					}
				}
				double efficacite = 0;
				ObservableList<Map.Entry<String, Element>> elements = FXCollections
						.observableArrayList(semaine.getStockPreviSortie().entrySet());
				for (Entry<String, Element> e : elements) {
					if (e.getValue().getPrixVente() != -1 && e.getValue().getQuantite() >= 0) {
						efficacite += e.getValue().getPrixVente() * e.getValue().getQuantite();
					}
				}
				for (Achat e : semaine.getAchats()) {
					if (e.getPrixVente() != -1 && e.getQuantite() >= 0) {
						efficacite -= e.getPrixAchat() * e.getQteA();
					}
				}
				semaine.setResultat(efficacite);
				String effic = "Efficacité " + efficacite;
				boolean okAchats = false;
				if(!listeElemPrix.isEmpty()) {
					okAchats = this.mainApp.showGestionAchatsDialog(semaine, listeElemPrix);
				}else {
					okAchats = true;
				}
				if(okAchats) {
					boolean okClicked = this.mainApp.showRecapSimulationDialog(this.getRecapSimulation(semaine), effic,
							semaine.getIdSemaine());
					this.addButtonRecap(semaine.getIdSemaine());
				}
			}
		}
	}
	
	public void getRecap(String b) {
		int i = Integer.parseInt(b.split(" ")[1]);
		Semaine se = null;
		for (Semaine s : this.programmation.getSemaines()) {
			if (s.getIdSemaine() == i) {
				se = s;
			}
		}
		boolean okClicked = this.mainApp.showRecapSimulationDialog(this.getRecapSimulation(se),
				"Efficacité " + se.getResultat(), se.getIdSemaine());
	}

	/**
	 * Retourne le recapitulatif d'une simulation pour 1 semaine
	 * 
	 * @param semaine
	 * @return une chaine de caractères
	 */
	public String getRecapSimulation(Semaine semaine) {
		String result = "";

		for (Integer key : semaine.getChaineProductionNiveau().keySet()) {
			List<ChaineProduction> chaines = semaine.getChaineProductionNiveau().get(key);
			int i = 0;
			for (ChaineProduction c : chaines) {
				result += c.getCode() + " " + c.getNom() + " x " + key + ": ";
				result += this.estProductible(semaine, c.getCode());
				for (Couple entree : c.getEntrees()) {
					result += "- " + entree.getQte() * key + " x " + entree.getCode() + " "
							+ this.mainApp.getElementData().get(entree.getCode()).getNom();
					result += "\n";
				}
				result += "\n";
				for (Couple sortie : c.getSorties()) {
					result += "+ " + sortie.getQte() * key + " x " + sortie.getCode() + " "
							+ this.mainApp.getElementData().get(sortie.getCode()).getNom();
					result += "\n";
				}
				result += "\n";

				if (this.achatPourLaChaine(semaine, c.getCode())) {
					result += "Achats :\n";
					for (Achat a : semaine.getAchats()) {
						if (a.getChaine().getCode().equals(c.getCode())) {
							result += a.getCode() + " " + this.mainApp.getElementData().get(a.getCode()).getNom()
									+ " x " + a.getQteA() + "\n";
						}
					}
					result += "\n";
				}
				if (this.produitManquantPourLaChaine(semaine, c.getCode())) {
					result += "Produits manquants :\n";
					for (ProduitManquant a : semaine.getProduitManquant()) {
						if (a.getChaine().getCode().equals(c.getCode())) {
							result += a.getCode() + " " + this.mainApp.getElementData().get(a.getCode()).getNom()
									+ " x " + a.getQuantiteM() + " --> produit créé à partir de la ou les chaines : ";
							List<String> ss = this.getChaineAProduire(a.getCode());
							for (String s : ss) {
								result += s + " ";
							}
							result += "\n";
						}
					}
					result += "\n";
				}
			}
		}
		return result;
	}

	/**
	 * retourne vrai si la chaine a des achats a effectuer
	 * 
	 * @param sem
	 * @param codeChaine
	 * @return
	 */
	public boolean achatPourLaChaine(Semaine sem, String codeChaine) {
		boolean s = false;
		for (Achat a : sem.getAchats()) {
			if (a.getChaine().getCode().equals(codeChaine)) {
				s = true;
			}
		}
		return s;
	}

	/**
	 * Retourne vrai si la chaine a besoin de produit manquant
	 * 
	 * @param sem
	 * @param codeChaine
	 * @return
	 */
	public boolean produitManquantPourLaChaine(Semaine sem, String codeChaine) {
		boolean s = false;
		for (ProduitManquant a : sem.getProduitManquant()) {
			if (a.getChaine().getCode().equals(codeChaine)) {
				s = true;
			}
		}
		return s;
	}

	/**
	 * Indique à un utilisateur qu'un achat est moins cher une semaine si ce n'est pas la meme semaine
	 * @param codeE
	 * @param s
	 */
	public void gestionDesAchats(String codeE, String nomE, Semaine s, String codeC, String nomC, int idSemaineCourante) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
		alert.setContentText("L'élément " + codeE + " " + nomE + " \n est moins cher en semaine " + s.getIdSemaine() + "\n Voulez-vous quand meme réaliser ces achats \n et cette chaine la semaine du " + idSemaineCourante);
		alert.setHeaderText(codeC + " " + nomC);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			this.mainApp.getProgrammations().clear();
			this.mainApp.setnbProgrammation(0); 
			new ImportExportCsv().writeCsvProgrammation(this.mainApp.getProgrammations());
			this.newProg = false;			
		}
	}
	
	/**
	 * Indique si une chaine est productible ou non
	 * 
	 * @param sem
	 * @param codeChaine
	 * @return
	 */
	public String estProductible(Semaine sem, String codeChaine) {
		String s = " Production possible \n";
		for (Achat a : sem.getAchats()) {
			if (a.getChaine().getCode().equals(codeChaine)) {
				s = " Production possible mais achat à effectuer \n";
			}
		}

		for (ProduitManquant p : sem.getProduitManquant()) {
			if (p.getChaine().getCode().equals(codeChaine)) {
				s = " Production impossible, élément à produire préalablement \n";
			}
		}
		return s;
	}

	/**
	 * Récupère la ou les chaine à produire pour un produit manquant
	 * 
	 * @param s
	 * @return
	 */
	public List<String> getChaineAProduire(String s) {
		List<String> codesChaines = new ArrayList<>();

		for (String se : this.mainApp.getChaineData().keySet()) {
			ChaineProduction c = this.mainApp.getChaineData().get(se);
			for (Couple co : c.getSorties()) {
				if (co.getCode().equals(s)) {
					codesChaines.add(c.getCode());
				}
			}
		}
		return codesChaines;
	}

	public void addButtonRecap(int semaineCode) {
		Semaine s = null;
		int i = 0;
		for (Semaine se : this.programmation.getSemaines()) {
			if (se.getIdSemaine() == semaineCode) {
				for (Node n : this.buttonGrid.getChildren()) {
					if (GridPane.getRowIndex(n) == i && GridPane.getColumnIndex(n) == 0) {
						n.setVisible(true);
					}
					if (GridPane.getRowIndex(n) == i && GridPane.getColumnIndex(n) == 1) {
						n.setVisible(true);
					}
					if (GridPane.getRowIndex(n) == i && GridPane.getColumnIndex(n) == 2) {
						n.setVisible(true);
						n.setDisable(true);
					}
				}
			}
			i++;
		}
	}

	/**
	 * Retourne un élément grâce à son code
	 * 
	 * @param c              le code de l'élément cherché
	 * @param observableList
	 * @return l'élément recherché
	 */
	public int getElementByCode(String c, ObservableList<Achat> observableList) {
		boolean ok = false;
		int index = -1;
		for (int i = 0; i < observableList.size(); i++) {
			if (observableList.get(i).getCode().equals(c)) {
				ok = true;
				index = i;
			}
		}
		return index;
	}

	/**
	 * Appelé lors du clique sur le bouton réinitialiser
	 */
	@FXML
	public void reinitialiser() {
		this.mainApp.setSimulation(false);
		this.mainApp.getAchatData().removeAll(this.mainApp.getAchatData());
		this.mainApp.getProduitManquantData().removeAll(this.mainApp.getProduitManquantData());
		this.mainApp.getElementSimulationData().clear();
		this.mainApp.getElementSimulationData().putAll(new ImportExportCsv().importElement("newElements.csv", ';'));
		for (int i = 0; i < this.mainApp.getChaineData().size(); i++) {
			for (Node no : this.gridChaine.getChildren()) {
				if (GridPane.getRowIndex(no) == i + 1 && GridPane.getColumnIndex(no) == 0) {
					CheckBox ch = (CheckBox) no;
					ch.setSelected(false);
				}
			}
			for (Node no : this.gridChaine.getChildren()) {
				if (GridPane.getRowIndex(no) == i + 1 && GridPane.getColumnIndex(no) == 1) {
					TextField tf = (TextField) no;
					tf.setText("0");
				}
			}
		}
	}
	
	/**
	 * Appelé lors du clique sur le bouton réinitialiser programmations
	 */
	@FXML
	public void reinitialiseProgramations() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
		alert.setContentText("Etes-vous sûr de vouloir tout réinitialiser? \n" + "Cela supprimera les programmations enregistrées \n" + "jusqu'a maintenant");
		alert.setHeaderText("");
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			this.mainApp.getProgrammations().clear();
			this.mainApp.setnbProgrammation(0); 
			new ImportExportCsv().writeCsvProgrammation(this.mainApp.getProgrammations());
			this.newProg = false;			
		}
	}
	
	/**
	 * Export des programmations
	 * @param programmations
	 */
	public void exporterProgrammations() {
		if(newProg) {
			this.btnExporter.setDisable(true);
			this.btnSimuler.setDisable(true);
			this.mainApp.getProgrammations().add(this.programmation);
			new ImportExportCsv().writeCsvProgrammation(this.mainApp.getProgrammations());
			this.newProgrammation();
			Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
			alert.setContentText("Export réussi!");
			alert.setHeaderText("");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Aucune programmation");
			alert.setHeaderText("Attention aucune programmation créée");
			alert.setContentText("Veuillez créer une nouvelle programmation\n");

			alert.showAndWait();
		}
	}

	/**
	 * Appelé lors du clique sur produire
	 */
	@FXML
	public void produire() {
		if (this.mainApp.getSimulation()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Confirmation");
			alert.setHeaderText("Etes-vous sûr de vouloir effectuer cette production?");

			// option != null.
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get() == null) {
				this.messageExport.setText("No selection!");
			} else if (option.get() == ButtonType.OK) {
				this.messageExport.setTextFill(Color.web("#52BE80"));
				this.messageExport.setText("Production effectuée");
				(new ImportExportCsv()).writeCsvElement("oldElements.csv", this.mainApp.getElementData());
				this.mainApp.getElementData().clear();
				this.mainApp.getElementData().putAll(this.mainApp.getElementSimulationData());
				(new ImportExportCsv()).writeCsvElement("newElements.csv", this.mainApp.getElementData());
				Date d = new Date();
				DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
				if (this.mainApp.getAchatData().size() != 0) {
					String nomA = "achats " + mediumDateFormat.format(d).toString() + ".csv";
					(new ImportExportCsv()).writeCsvAchat(nomA, this.mainApp.getAchatData());
				}
				if (this.mainApp.getProduitManquantData().size() != 0) {
					String nomP = "produitsManquants " + mediumDateFormat.format(d).toString() + ".csv";
					(new ImportExportCsv()).writeCsvProduitManquant(nomP, this.mainApp.getProduitManquantData());
				}

				this.mainApp.getAchatData().removeAll(this.mainApp.getAchatData());
				this.mainApp.getProduitManquantData().removeAll(this.mainApp.getProduitManquantData());

				// reinitialise les champs
				for (int i = 0; i < this.mainApp.getChaineData().size(); i++) {
					for (Node no : this.gridChaine.getChildren()) {
						if (GridPane.getRowIndex(no) == i + 1 && GridPane.getColumnIndex(no) == 0) {
							CheckBox ch = (CheckBox) no;
							ch.setSelected(false);
						}
					}
					for (Node no : this.gridChaine.getChildren()) {
						if (GridPane.getRowIndex(no) == i + 1 && GridPane.getColumnIndex(no) == 1) {
							TextField tf = (TextField) no;
							tf.setText("0");
						}
					}
				}
			} else if (option.get() == ButtonType.CANCEL) {
				this.messageExport.setTextFill(Color.web("#FF5733"));
				this.messageExport.setText("Production annulée");
			} else {
				this.messageExport.setText("");
			}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Aucune simulation");
			alert.setHeaderText("Aucune simulation effectuée");
			alert.setContentText("Veuillez réaliser une simulation avant de produire");

			alert.showAndWait();
		}
	}
}
