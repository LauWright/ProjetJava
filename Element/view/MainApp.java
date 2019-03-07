package view;
import java.io.IOException;

import element.Element;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import production.ImportCsv;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    private ObservableList<element.Element> stockElements = FXCollections.observableArrayList();
    private ObservableList<production.ChaineProduction> chaines = FXCollections.observableArrayList();
    /**
     * Constructeur
     */
    public MainApp(){
    	this.stockElements= ImportCsv.importElement("elements1.csv", ';');
    	this.chaines = ImportCsv.importChaineProduction("chaines.csv", ';');
    }
    
    /**
     * Retourne la liste d'elements à charger à l'ouverture
     */
    public ObservableList<element.Element> getElementData() {
        return this.stockElements;
    }
    
    /**
     * Retourne la liste des chaines de production
     */
    public ObservableList<production.ChaineProduction> getChaineData() {
        return this.chaines;
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
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Menu.fxml"));
            AnchorPane menu = (AnchorPane) loader.load();
            
            // ajouter menu au centre de la fenetre principale.
            rootLayout.setCenter(menu);
            
            //connexion de ElementController à la mainPage
            ElementController elemController = loader.getController();
            elemController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}