package view;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import element.Achat;
import element.Element;
import element.MatierePremiere;
import element.ProduitManquant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import production.ChaineProduction;
import production.Couple;
import production.ImportExportCsv;

public class SimulationProductionController {

	@FXML
	private TableView<Achat> achatsTable;
	@FXML
	private TableColumn<Achat, String> aChaineColumn;
	@FXML
	private TableColumn<Achat, String> aCodeColumn;
	@FXML
	private TableColumn<Achat, String> aNomColumn;
	@FXML
	private TableColumn<Achat, String> aMesureColumn;
	@FXML
	private TableColumn<Achat, Double> aPrixAchatColumn;
	@FXML
	private TableColumn<Achat, Double> QuantiteColumn;

	@FXML
	private TableView<ProduitManquant> produitsTable;
	@FXML
	private TableColumn<ProduitManquant, String> pChaineColumn;
	@FXML
	private TableColumn<ProduitManquant, String> pCodeColumn;
	@FXML
	private TableColumn<ProduitManquant, String> pNomColumn;
	@FXML
	private TableColumn<ProduitManquant, String> pMesureColumn;
	@FXML
	private TableColumn<ProduitManquant, Double> pQuantiteColumn;

	@FXML
	private GridPane gridChaine;
	@FXML
	private ScrollPane scrollChaine;
	@FXML
	private Label messageExport;

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
		this.aChaineColumn.setCellValueFactory(cellData -> cellData.getValue().getChaine().getCodeProperty());
		this.aCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		this.aNomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
		this.aMesureColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
		this.aPrixAchatColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixAchatProperty().asObject());
		this.QuantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQteAProperty().asObject());

		this.pChaineColumn.setCellValueFactory(cellData -> cellData.getValue().getChaine().getCodeProperty());
		this.pCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		this.pNomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
		this.pMesureColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
		this.pQuantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantiteMProperty().asObject());
	}

	/**
	 * appelé par l'application main
	 * 
	 * @param mainApp
	 * @throws IOException
	 */
	public void setMainApp(MainApp mainApp) throws IOException {
		this.mainApp = mainApp;
		this.tableChaine();
		this.achatsTable.setItems(this.mainApp.getAchatData());
		this.produitsTable.setItems(this.mainApp.getProduitManquantData());
	}

	public void tableChaine() {
		Label chaineLabel = new Label("Chaine");
		Label nivLabel = new Label("Niveau");

		this.gridChaine.add(chaineLabel, 0, 0);
		this.gridChaine.add(nivLabel, 1, 0);

		ObservableList<Map.Entry<String, ChaineProduction>> chaines = FXCollections
				.observableArrayList(this.mainApp.getChaineData().entrySet());
		for (int i = 0; i < chaines.size(); i++) {
			CheckBox ch = new CheckBox();
			ch.setText(chaines.get(i).getValue().getCode() + " " + chaines.get(i).getValue().getNom());
			this.gridChaine.add(ch, 0, i + 1);
		}

		for (int i = 0; i < chaines.size(); i++) {
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
	 * Clicque sur le bouton simuler
	 */
	@FXML
	public void simuler() {
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
			alert.setHeaderText("Aucune chaîne selectionée");
			alert.setContentText("Veuillez selectionner une chaîne \n" + "et rentrer un niveau.");

			alert.showAndWait();
		} else {
			this.mainApp.setSimulation(true);
			String s = "";
			for (Node n : this.gridChaine.getChildren()) {
				CheckBox ch;
				for (int i = 0; i < this.mainApp.getChaineData().size(); i++) {
					if (GridPane.getRowIndex(n) == i + 1 && GridPane.getColumnIndex(n) == 0) {
						ch = (CheckBox) n;
						if (ch.isSelected()) {
							ChaineProduction c = this.mainApp.getChaineData().get(ch.getText().split(" ")[0]);
							s += "\n";
							s += "Chaîne : " + c.getCode();
							for (Node no : this.gridChaine.getChildren()) {
								if (GridPane.getRowIndex(no) == i + 1 && GridPane.getColumnIndex(no) == 1) {
									TextField tf = (TextField) no;
									s += " , niveau " + tf.getText() + "\n";
									List<Couple> entrees = c.getEntrees();
									boolean reussi = true;
									for (Couple couple : entrees) {
										Element e = this.mainApp.getElementData().get(couple.getCode());
										if (couple.getCode().equals(e.getCode())) {
											e.soustraire(couple.getQte() * Double.valueOf(tf.getText()));
											System.out.println(e.getQuantite());
											if (e.examiner()) {
												if (e.getClass().getSimpleName().equals("MatierePremiere")) {
													MatierePremiere ma = (MatierePremiere) e;
													if (ma.getPrixAchat() == -1) {
														s += "Production impossible, élément " + e.getCode()
																+ " ne possède pas de prix d'achat et la quantité est insuffisante \n";
														e.ajouter(couple.getQte() * Double.valueOf(tf.getText()));
														reussi = false;
													} else {
														this.mainApp.getAchatData()
																.add(new Achat(ma.getCode(), ma.getNom(),
																		e.getQuantite(), ma.getMesure(),
																		ma.getPrixVente(), ma.getPrixAchat(),
																		0 - e.getQuantite(), c));
														s += "Attention matière première " + ma.getCode()
																+ " manquante dans le stock \n";
													}
												}
												if (e.getClass().getSimpleName().equals("Produit")) {
													s += "Production impossible, élément " + e.getCode()
															+ " ne possède pas de prix d'achat et la quantité est insuffisante \n";
													this.mainApp.getProduitManquantData()
															.add(new ProduitManquant(e.getCode(), e.getNom(),
																	e.getQuantite(), e.getMesure(), e.getPrixVente(),
																	0 - e.getQuantite(), c));
													e.ajouter(couple.getQte() * Double.valueOf(tf.getText()));
													reussi = false;
												}
											}
										}
									}
									List<Couple> sorties = c.getSorties();
									if (reussi) {
										s += "Produit(s) obtenu(s) : ";
										for (Couple so : sorties) {
											Element e = this.mainApp.getElementData().get(so.getCode());
											if (e.getCode().equals(so.getCode())) {
												e.setQuantite(so.getQte() * Double.valueOf(tf.getText()));
												if (e.getQuantite() >= 0) {
													ProduitManquant prm = null;
													for (ProduitManquant pm : this.mainApp.getProduitManquantData()) {
														if (pm.getCode().equals(e.getCode())) {
															prm = pm;
														}
													}
													this.mainApp.getProduitManquantData().remove(prm);
												}
												double total = so.getQte() * Double.valueOf(tf.getText());
												s += e.getCode() + " - " + so.getQte() + " x " + tf.getText() + " = "
														+ total + " " + e.getMesure() + "\n";
											}
										}
									}
								}
							}
						}
					}
				}
			}
			double efficacite = 0;
			ObservableList<Map.Entry<String, Element>> elements = FXCollections
					.observableArrayList(this.mainApp.getElementData().entrySet());
			for (Entry<String, Element> e : elements) {
				if (e.getValue().getPrixVente() != -1 && e.getValue().getQuantite() >= 0) {
					efficacite += e.getValue().getPrixVente() * e.getValue().getQuantite();
				}
			}
			for (Achat e : this.mainApp.getAchatData()) {
				if (e.getPrixVente() != -1 && e.getQuantite() >= 0) {
					efficacite -= e.getPrixAchat() * e.getQteA();
				}
			}

			String effic = "Efficacité " + efficacite;
			boolean okClicked = this.mainApp.showRecapSimulationDialog(s, effic);
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
				// (new ImportExportCsv()).writeCsvElement("oldElements.csv",
				// this.mainApp.getElementData());
				this.mainApp.getElementData().clear();
				this.mainApp.getElementData().putAll(this.mainApp.getElementSimulationData());
				// (new ImportExportCsv()).writeCsvElement("newElements.csv",
				// this.mainApp.getElementData());
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
