package view;

import java.io.IOException;

import element.Element;
import element.MatierePremiere;
import element.Produit;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * @author laure
 *
 */
public class ElementController {
	// tableau des matières premières
	@FXML
	private TableView<element.MatierePremiere> matierePremiereTable;
	@FXML
	private TableColumn<element.MatierePremiere, String> codeMAColumn;
	@FXML
	private TableColumn<element.MatierePremiere, String> nomMAColumn;
	@FXML
	private TableColumn<element.MatierePremiere, Double> quantiteMAColumn;
	@FXML
	private TableColumn<element.MatierePremiere, String> mesureMAColumn;
	@FXML
	private TableColumn<element.MatierePremiere, Double> prixAchatMAColumn;
	@FXML
	private TableColumn<element.MatierePremiere, Double> prixVenteMAColumn;

	@FXML
	private TableView<element.Produit> produitTable;
	@FXML
	private TableColumn<element.Produit, String> codePColumn;
	@FXML
	private TableColumn<element.Produit, String> nomPColumn;
	@FXML
	private TableColumn<element.Produit, Double> quantitePColumn;
	@FXML
	private TableColumn<element.Produit, String> mesurePColumn;
	@FXML
	private TableColumn<element.Produit, Double> prixVentePColumn;

	@FXML
	private Tab chainetab = new Tab();
	@FXML
	private Tab majStocktab = new Tab();
	@FXML
	private Tab productiontab = new Tab();
	
	private ObservableList<element.MatierePremiere> matieresPremieres = FXCollections.observableArrayList();
	private ObservableList<element.Produit> produits = FXCollections.observableArrayList();

	// reference l'application principale
	private MainApp mainApp;

	/**
	 * Constructeur
	 */
	public ElementController() {
	}

	/**
	 * Initialise le controller appelé automatiquement à l'ouverture de la page
	 * .fxml
	 */
	@FXML
	private void initialize() {
		// Initialize le tableau des matieres premieres.
		this.codeMAColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		this.nomMAColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
		this.quantiteMAColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantiteProperty().asObject());
		this.mesureMAColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
		this.prixAchatMAColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixAchatProperty().asObject());
		this.prixVenteMAColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixVenteProperty().asObject());

		// Initialize le tableau des produits.
		this.codePColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		this.nomPColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
		this.quantitePColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantiteProperty().asObject());
		this.mesurePColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
		this.prixVentePColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixVenteProperty().asObject());
	}

	/**
	 * appelé par l'application main
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add liste des element à la table elementTable
		ObservableList<Element> elements = this.mainApp.getElementData();
		for (Element e : elements) {
			if (e.getClass().getSimpleName().equals("Produit")) {
				this.produits.add((Produit) e);
			} else {
				this.matieresPremieres.add((MatierePremiere) e);
			}
		}

		this.produitTable.setItems(this.produits);
		this.matierePremiereTable.setItems(this.matieresPremieres);

	}

	/**
	 * Insert la vue chaine dans l'onget gerer les chaines
	 */
	public void showChaineOverview() {
		try {
			// Load menu overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("GestionChaine.fxml"));
			AnchorPane chaines = (AnchorPane) loader.load();

			this.chainetab.setContent(chaines);

			// connexion de ChaineController à la mainPage
			ChaineController chaineController = loader.getController();
			chaineController.setMainApp(this.mainApp);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Insert la vue mise à jour du stock dans l'onget mise à jour des stocks
	 */
	public void showMajStockOverview() {
		try {
			// Load menu overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("MiseAJourStock.fxml"));
			AnchorPane majStock = (AnchorPane) loader.load();

			this.majStocktab.setContent(majStock);
			
			// connexion de MajStockController à la mainPage
			MajStockController majStockController = loader.getController();
			majStockController.setMainApp(this.mainApp);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Insert la vue simulationProduction dans l'onget production
	 */
	public void showProductionOverview() {
		try {
			// Load menu overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("SimulationProduction.fxml"));
			AnchorPane production = (AnchorPane) loader.load();

			this.productiontab.setContent(production);

			// connexion de SimulationProductionController à la mainPage
			SimulationProductionController productionController = loader.getController();
			productionController.setMainApp(this.mainApp);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
