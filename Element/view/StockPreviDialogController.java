package view;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import element.Element;
import element.MatierePremiere;
import element.Produit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import production.Semaine;

public class StockPreviDialogController {
	
	@FXML
	private TableView<Element> elementTable;
	@FXML
	private TableColumn<Element, String> codeColumn;
	@FXML
	private TableColumn<Element, String> nomColumn;
	@FXML
	private TableColumn<Element, Double> quantiteColumn;
	@FXML
	private TableColumn<Element, Double> variationColumn;
	
	private Stage dialogStage;
	
	private ObservableList<Element> elements = FXCollections.observableArrayList();
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
		this.codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		this.nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
		this.quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantiteProperty().asObject());
		this.variationColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixAchatProperty().asObject());

		//this.prixVentePColumn.setCellValueFactory(cellData -> { Produit p = cellData.getValue(); return p.getPrixVente() == -1 ? new SimpleStringProperty("Aucun") : new SimpleStringProperty(String.valueOf(p.getPrixVente()));});
		//this.prixAchatPColumn.setCellValueFactory(cellData -> { Produit p = cellData.getValue(); return p.getPrixAchat() == -1 ? new SimpleStringProperty("Aucun") : new SimpleStringProperty(String.valueOf(p.getPrixAchat()));});
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
		for(Entry<String, Element> e : s.getStockPreviSortie().entrySet()) {
			System.out.println(s.getIdSemaine() + " " + e.getKey() + " " + e.getValue().getQuantite());
		}
		
		
		this.elementTable.getItems().removeAll(this.elementTable.getItems());
		
		// Add liste des element à la table elementTable
		ObservableList<Map.Entry<String, Element>> elems = FXCollections.observableArrayList(s.getStockPreviSortie().entrySet());
		for (Entry<String, Element> e : elems) {
			
				this.elements.add((Element) e.getValue());
			
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
