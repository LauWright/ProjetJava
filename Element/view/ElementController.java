package view;

import java.io.IOException;

import element.Element;
import element.MatierePremiere;
import element.Produit;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import production.ImportExportCsv;

/**
 * @author laure
 *
 */
public class ElementController {
	
	@FXML
	private Tab chainetab = new Tab();
	@FXML
	private Tab majStocktab = new Tab();
	@FXML
	private Tab productiontab = new Tab();
	@FXML
	private Tab elementtab = new Tab();
	

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

	}

	/**
	 * appelé par l'application main
	 * 
	 * @param mainApp
	 * @throws IOException 
	 */
	/**
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		//this.elementtab.getOnSelectionChanged();
		this.mainApp = mainApp;
		try {
			/*
			 * this.showProductionOverview(); // Load menu overview. FXMLLoader loader = new
			 * FXMLLoader();
			 * loader.setLocation(MainApp.class.getResource("ElementStocks.fxml"));
			 * AnchorPane elements = (AnchorPane) loader.load();
			 * 
			 * this.elementtab.setContent(elements); // connexion de ChaineController à la
			 * mainPage ElementStocksController elemController = loader.getController();
			 * elemController.setMainApp(this.mainApp);
			 */

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
	public void showElementOverview() {
		try {
			// Load menu overview. 
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ElementStocks.fxml"));
			AnchorPane elements = (AnchorPane) loader.load();
			
			this.elementtab.setContent(elements); 
			// connexion de ChaineController à la mainPage 
			ElementStocksController elemController = loader.getController();
			elemController.setMainApp(this.mainApp);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
