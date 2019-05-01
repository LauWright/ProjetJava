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
import production.ImportExportCsv;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private ChaineProduction newChaine;

	private ObservableMap<String, element.Element> stockElements = FXCollections.observableHashMap();
	private ObservableMap<String, element.Element> stockElementsSimulation = FXCollections.observableHashMap();
	private ObservableMap<String, ChaineProduction> chaines = FXCollections.observableHashMap();
	
	
	private ObservableList<Achat> achats = FXCollections.observableArrayList();
	private ObservableList<ProduitManquant> produitM= FXCollections.observableArrayList();
	
	private List<String> semaines = new ArrayList<>();
	
	
	private boolean simulation = false;
	/**
	 * Constructeur
	 */
	public MainApp() {
		this.stockElements = new ImportExportCsv().importElement("newElements.csv", ';');
		this.stockElementsSimulation = new ImportExportCsv().importElement("newElements.csv", ';');
		this.chaines = new ImportExportCsv().importChaineProduction("chaines.csv", ';');
		
		Calendar calendar = Calendar.getInstance();
		for(int i = 0; i<8; i++) {
			int week = calendar.get(calendar.WEEK_OF_YEAR);
			this.semaines.add("Semaine " + week);
			calendar.add(Calendar.DATE, 7);	
		}
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
	 * Retourne la liste des semaines
	 */
	public List<String> getSemaines() {
		return this.semaines;
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
	public boolean showRecapSimulationDialog(String s, String effic) {
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
	        controller.setText(s, effic);

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