package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ElementController {
	@FXML
    private TableView<element.Element> elementTable;
    @FXML
    private TableColumn<element.Element, String> codeColumn;
    @FXML
    private TableColumn<element.Element, String> nomColumn;
    @FXML
    private TableColumn<element.Element, Double> quantiteColumn;
    @FXML
    private TableColumn<element.Element, String> mesureColumn;
    @FXML
    private TableColumn<element.Element, Double> prixVenteColumn;
    @FXML
    private Tab chainetab = new Tab();
    
    //reference l'application principale
    private MainApp mainApp;

    /**
     * Constructeur
     */
    public ElementController() {	
    }
    
    /**
     * Initialise le controller appelé automatiquement à l'ouverture de la page .fxml
     */
    @FXML
    private void initialize() {
        // Initialize le tableau du stock.
        this.codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
        this.nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
        this.quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantiteProperty().asObject());
        this.mesureColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
        this.prixVenteColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixVenteProperty().asObject());
    }
    
    /**
     * appelé par l'application main
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add liste des element à la table elementTable
        elementTable.setItems(mainApp.getElementData());
    }
    
    /**
     * Appelé lors de l'appuie sur le bouton supprimer
     */
    @FXML
    private void handleDeleteElement() {
        int selectedIndex = elementTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            elementTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Aucun element selectionné");
            alert.setHeaderText("Aucun element selectionné");
            alert.setContentText("Veuillez selectionner un élément dans la table.");

            alert.showAndWait();
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
            
          //connexion de ChaineController à la mainPage
            ChaineController chaineController = loader.getController();
            chaineController.setMainApp(this.mainApp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
