package view;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import element.Element;
import element.ElementVariation;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import production.Semaine;

public class StockPreviDialogController {
	
	@FXML
	private TableView<ElementVariation> elementTable;
	@FXML
	private TableColumn<ElementVariation, String> codeColumn;
	@FXML
	private TableColumn<ElementVariation, String> nomColumn;
	@FXML
	private TableColumn<ElementVariation, Double> quantiteColumn;
	@FXML
	private TableColumn<ElementVariation, Double> variationColumn;
	
	private Stage dialogStage;
	
	private ObservableList<ElementVariation> elements = FXCollections.observableArrayList();
	private Semaine semaine;
	// reference l'application principale
	private MainApp mainApp;
	/**
	 * Constructeur
	 */
	public StockPreviDialogController() {
	}

	/**
	 * Initialise le controller appelé automatiquement à l'ouverture de la page
	 * .fxml
	 */
	@FXML
	private void initialize() {
		// Initialise le tableau des matieres premieres.
		this.codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeElement());
		this.nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNom());
		this.quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantite().asObject());

		this.variationColumn.setCellValueFactory(cellData -> cellData.getValue().getVariation().asObject());
	}
	

	/**
	 * Initialise la popup
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	/**
	 * appelé par l'application main
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp, Semaine s) {
		this.mainApp = mainApp;
		this.semaine = s;
		System.out.println(s.getIdSemaine());
		
		
		this.elementTable.getItems().removeAll(this.elementTable.getItems());
		
		// Add liste des element à la table elementTable
		ObservableList<Map.Entry<String, Element>> elems = FXCollections.observableArrayList(s.getStockPreviSortie().entrySet());
		for (Entry<String, Element> e : elems) {
			System.out.println(s.getStockPreviSortie().get(e.getKey()).getQuantite() + " " + s.getStockPreviEntree().get(e.getKey()).getQuantite());
			double d = s.getStockPreviSortie().get(e.getKey()).getQuantite() - s.getStockPreviEntree().get(e.getKey()).getQuantite();
			ElementVariation elemv = new ElementVariation(e.getKey(), e.getValue().getNom(), e.getValue().getQuantite(), d);
			this.elements.add(elemv);
			
		}
		this.elementTable.setItems(elements);
		this.elementTable.getSortOrder().addAll(this.codeColumn);	
		
	}
	
	 /**
     * Appelé lors du clique sur ok
	 * @throws IOException 
     */
    @FXML
    private void handleFermer() throws IOException {
    	
    	dialogStage.close();
            
    }

}
