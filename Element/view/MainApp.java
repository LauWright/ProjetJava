package view;

import java.io.IOException;

import element.Achat;
import element.Element;
import element.ProduitManquant;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import production.ChaineProduction;
import production.ImportCsv;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private ChaineProduction newChaine;

	private ObservableList<element.Element> stockElements = FXCollections.observableArrayList();
	private ObservableList<element.Element> stockElementsSimulation = FXCollections.observableArrayList();
	private ObservableList<production.ChaineProduction> chaines = FXCollections.observableArrayList();
	private ObservableList<Achat> achats = FXCollections.observableArrayList();
	private ObservableList<ProduitManquant> produitM= FXCollections.observableArrayList();
	/**
	 * Constructeur
	 */
	public MainApp() {
		this.stockElements = ImportCsv.importElement("elements1.csv", ';');
		this.stockElementsSimulation = ImportCsv.importElement("elements1.csv", ';');
		this.chaines = ImportCsv.importChaineProduction("chaines.csv", ';');
	}

	/**
	 * Retourne la liste d'elements à charger à l'ouverture
	 */
	public ObservableList<element.Element> getElementData() {
		return this.stockElements;
	}
	
	/**
	 * Retourne la liste d'elementssimulé
	 */
	public ObservableList<element.Element> getElementSimulationData() {
		return this.stockElementsSimulation;
	}

	/**
	 * Retourne la liste des chaines de production
	 */
	public ObservableList<production.ChaineProduction> getChaineData() {
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
	 * @param element
	 * @return
	 */
	public boolean showElementEditDialog(Element element) {
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

	        // Set the person into the controller.
	        ElementEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setElement(element);

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
	public boolean showRecapSimulationDialog(String s) {
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
	        controller.setText(s);

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