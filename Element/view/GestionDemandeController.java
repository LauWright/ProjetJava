package view;

import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import production.Demande;

public class GestionDemandeController {
	

	@FXML
    private TableView<Demande> demandeTable;
	@FXML
    private TableColumn<Demande, Integer> idColumn;
    @FXML
    private TableColumn<Demande, String> codeColumn;
    @FXML
    private TableColumn<Demande, String> nomColumn;
    @FXML
    private TableColumn<Demande, Double> quantiteColumn;
    
    
  //application principale
    private MainApp mainApp;

    /**
     * Constructeur
     */
	public GestionDemandeController() {
	}
    
	
	/**
     * Initialise le controller
     */
    @FXML
    private void initialize() {
        // Initialise la table des demandes
    	
    	this.idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdPropertySemaine().asObject());
		this.codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodePropertyElement());
		this.nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomPropertyElement());
		this.quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantitePropertyDemande().asObject());
    }
    
    /**
     * Appelé par le mainApp
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
    	this.mainApp = mainApp;
    	
        // Add observable list data to the table
        this.demandeTable.setItems(this.mainApp.getDemandes());
        this.demandeTable.getSortOrder().addAll(this.idColumn);
    }

}
