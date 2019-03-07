package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ChaineController {
	
	@FXML
    private TableView<production.ChaineProduction> chaineTable;
    @FXML
    private TableColumn<production.ChaineProduction, String> codeColumn;
    @FXML
    private TableColumn<production.ChaineProduction, String> nomColumn;
    
    @FXML
    private Label code;
    
    //application principale
    private MainApp mainApp;

    /**
     * Constructeur
     */
	public ChaineController() {
	}
    
	
	/**
     * Initialise le controller
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        this.codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
        this.nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
    }
    
    /**
     * Appelé par le mainApp
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        // Add observable list data to the table
        this.chaineTable.setItems(mainApp.getChaineData());
    }


}
