package view;


import java.util.Map;
import java.util.Map.Entry;

import element.Element;
import element.MatierePremiere;
import element.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ElementStocksController {
	// tableau des matières premières
		@FXML
		private TableView<element.MatierePremiere> matierePremiereTable;
		@FXML
		private TableColumn<element.MatierePremiere, String> codeMAColumn;
		@FXML
		private TableColumn<element.MatierePremiere, String> nomMAColumn;
		@FXML
		private TableColumn<element.MatierePremiere, Double> quantiteMAColumn;
		@FXML
		private TableColumn<element.MatierePremiere, String> mesureMAColumn;
		@FXML
		private TableColumn<element.MatierePremiere, Double> prixAchatMAColumn;

		@FXML
		private TableView<element.Produit> produitTable;
		@FXML
		private TableColumn<element.Produit, String> codePColumn;
		@FXML
		private TableColumn<element.Produit, String> nomPColumn;
		@FXML
		private TableColumn<element.Produit, Double> quantitePColumn;
		@FXML
		private TableColumn<element.Produit, String> mesurePColumn;
		@FXML
		private TableColumn<element.Produit, Double> prixVentePColumn;
		
		private ObservableList<element.MatierePremiere> matieresPremieres = FXCollections.observableArrayList();
		private ObservableList<element.Produit> produits = FXCollections.observableArrayList();
		
		// reference l'application principale
		private MainApp mainApp;
		/**
		 * Constructeur
		 */
		public ElementStocksController() {
		}

		/**
		 * Initialise le controller appelé automatiquement à l'ouverture de la page
		 * .fxml
		 */
		@FXML
		private void initialize() {
			// Initialize le tableau des matieres premieres.
			this.codeMAColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
			this.nomMAColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
			this.quantiteMAColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantiteProperty().asObject());
			this.mesureMAColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
			this.prixAchatMAColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixAchatProperty().asObject());
			
			// Initialize le tableau des produits.
			this.codePColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
			this.nomPColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
			this.quantitePColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantiteProperty().asObject());
			this.mesurePColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
			this.prixVentePColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixVenteProperty().asObject());
		}
		
		/**
		 * appelé par l'application main
		 * 
		 * @param mainApp
		 */
		public void setMainApp(MainApp mainApp) {
			this.mainApp = mainApp;
			this.produitTable.getItems().removeAll(this.produitTable.getItems());
			this.matierePremiereTable.getItems().removeAll(this.matierePremiereTable.getItems());
			// Add liste des element à la table elementTable
			ObservableList<Map.Entry<String, Element>> elements = FXCollections.observableArrayList(this.mainApp.getElementData().entrySet());
			for (Entry<String, Element> e : elements) {
				if (e.getValue().getClass().getSimpleName().equals("Produit")) {
					this.produits.add((Produit) e.getValue());
				} else {
					this.matieresPremieres.add((MatierePremiere) e.getValue());
				}
			}

			this.produitTable.setItems(this.produits);
			this.matierePremiereTable.setItems(this.matieresPremieres);

		}
		
		/**
		 * Mise à jours des tables de stocks
		 */
		
		public void majTable() {
			this.produitTable.getItems().removeAll(this.produitTable.getItems());
			this.matierePremiereTable.getItems().removeAll(this.matierePremiereTable.getItems());
			// Add liste des element à la table elementTable
			ObservableList<Map.Entry<String, Element>> elements = FXCollections.observableArrayList(this.mainApp.getElementData().entrySet());
			for (Entry<String, Element> e : elements) {
				if (e.getValue().getClass().getSimpleName().equals("Produit")) {
					this.produits.add((Produit) e.getValue());
				} else {
					this.matieresPremieres.add((MatierePremiere) e.getValue());
				}
			}

			this.produitTable.setItems(this.produits);
			this.matierePremiereTable.setItems(this.matieresPremieres);
		}

}
