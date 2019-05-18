package view;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import element.Achat;
import element.Element;
import element.ElementPrix;
import element.MatierePremiere;
import element.ProduitManquant;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import production.ChaineProduction;
import production.Demande;
import production.ImportExportCsv;
import production.Programmation;
import production.Semaine;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private ChaineProduction newChaine;

	private ObservableMap<String, element.Element> stockElements = FXCollections.observableHashMap();
	private ObservableMap<String, element.Element> stockElementsSimulation = FXCollections.observableHashMap();
	private ObservableMap<String, ChaineProduction> chaines = FXCollections.observableHashMap();
	
	private ObservableList<Demande> demandes = FXCollections.observableArrayList();
	private ObservableList<Achat> achats = FXCollections.observableArrayList();
	private ObservableList<ProduitManquant> produitM= FXCollections.observableArrayList();
	private List<Programmation> programmations = new ArrayList<>();
	private int nbProgrammation;
	
	private boolean simulation = false;
	/**
	 * Constructeur
	 */
	public MainApp() {
		this.stockElements = new ImportExportCsv().importElement("newElements.csv", ';');
		this.stockElementsSimulation = new ImportExportCsv().importElement("newElements.csv", ';');
		this.chaines = new ImportExportCsv().importChaineProduction("chaines.csv", ';');
		
		//import des demandes
		this.demandes = new ImportExportCsv().importDemande("demandeurs.csv", ';');
		
		//Programmations
		this.programmations = new ImportExportCsv().importProgrammations(this.chaines);
		this.nbProgrammation = this.programmations.size();
	}

	/**
	 * Retourne la liste d'elements à charger à l'ouverture
	 */
	public ObservableMap<String, element.Element> getElementData() {
		return this.stockElements;
	}
	
	/**
	 * Retourne la liste d'elementssimulé
	 */
	public ObservableMap<String, element.Element> getElementSimulationData() {
		return this.stockElementsSimulation;
	}

	/**
	 * Retourne la liste des chaines de production
	 */
	public ObservableMap<String, ChaineProduction> getChaineData() {
		return this.chaines;
	}
	
	/**
	 * Retourne la liste des achats
	 */
	public ObservableList<Achat> getAchatData() {
		return this.achats;
	}
	
	/**
	 * Retourne la liste des produits mnquants
	 */
	public ObservableList<ProduitManquant> getProduitManquantData() {
		return this.produitM;
	}
	
	/**
	 * Retourne la liste des produits mnquants
	 */
	public ObservableList<Demande> getDemandes() {
		return this.demandes;
	}
	
	/**
	 * Retourne la liste des programmations
	 */
	public List<Programmation> getProgrammations() {
		return this.programmations;
	}
	
	/**
	 * Retourne le nombre de programmation
	 */
	public int getnbProgrammation() {
		return this.nbProgrammation;
	}
	
	/**
	 * Modifie le nombre de programmation
	 */
	public void setnbProgrammation(int nb) {
		this.nbProgrammation = nb;
	}
	
	/**
	 * Retourne le simulation
	 */
	public boolean getSimulation() {
		return this.simulation;
	}
	
	/**
	 * Retourne le simulation
	 */
	public void setSimulation(boolean b) {
		this.simulation = b;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Gestion de production");

		initRootLayout();

		showMenuOverview();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load menu layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("RootLayer.fxml"));
			this.rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(this.rootLayout);
			this.primaryStage.setScene(scene);
			this.primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche le menu.fxml dans le rootlayer.fxml.
	 */
	public void showMenuOverview() {
		try {
			// Load menu overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Menu.fxml"));
			AnchorPane menu = (AnchorPane) loader.load();

			// ajouter menu au centre de la fenetre principale.
			rootLayout.setCenter(menu);

			// connexion de ElementController à la mainPage
			ElementController elemController = loader.getController();
			elemController.setMainApp(this);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public boolean showNewChaineDialog(ChaineProduction chaine) {
		try {
			// Load fxml fichier + création de la fenetre de dialogue
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("NewChaineDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// création du stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Ajouter une chaîne de production");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			NewChaineDialogController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			controller.setChaine(chaine);
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Ouvre la boite de dialogue pour editer un element
	 * @param selectedElement
	 * @return
	 */
	public boolean showElementEditDialog(Entry<String, Element> selectedElement) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("ElementEditDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Editer un élément");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the element into the controller.
	        ElementEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setElement(selectedElement);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Ouvre la boite de dialogue pour editer un element
	 * @param element
	 * @return
	 */
	public boolean showRecapSimulationDialog(String s, String effic, int sem) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("RecapSimulationDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Récapitulation simulation");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        RecapSimulationController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setMainApp(this);
	        controller.setText(s, effic, sem);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Ouvre la boite de dialogue pour consulter le stock previsionnel
	 * @param element
	 * @return
	 */
	public boolean showStockPreviDialog(Semaine s) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("StockPreviDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Stock previsionnel");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        StockPreviDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setMainApp(this, s);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	

	/**
	 * Ouvre la boite de dialogue pour gerer les achats sur les differentes semaines
	 * @param element
	 * @return
	 */
	public boolean showGestionAchatsDialog(Semaine s, List<ElementPrix> elemP) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("GestionAchatsDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Gestion des achats");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        GestionAchatsController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setMainApp(this, s, elemP);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Ouvre la boite de dialogue pour voir les détails des demandes
	 * @param element
	 * @return
	 */
	public boolean showDetailsDemandesDialog(String recap, String sp) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("DetailsDemandesDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Détails des demandes");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        DetailsDemandesController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setMainApp(this);
	        controller.setText(recap, sp);
	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}