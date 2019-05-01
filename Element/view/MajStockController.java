package view;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import element.Element;
import element.MatierePremiere;
import element.Produit;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import production.ImportExportCsv;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;

public class MajStockController {
	@FXML
	private TableView<Map.Entry<String,Element>> elementTable;
	@FXML
	private TableColumn<Map.Entry<String, Element>, String> codeColumn;
	@FXML
	private TableColumn<Map.Entry<String, Element>, String> nomColumn;
	@FXML
	private TableColumn<Map.Entry<String, Element>, Double> quantiteColumn;
	@FXML
	private TableColumn<Map.Entry<String, Element>, String> mesureColumn;

	@FXML
	private Label codeLabel;
	@FXML
	private Label prixAchatLabel;
	@FXML
	private Label prixVenteLabel;
	@FXML
	private Label typeLabel;
	@FXML
	private Label messageExport;

	@FXML
	private RadioButton maRadio;
	@FXML
	private RadioButton produitRadio;
	@FXML
	private ToggleGroup group = new ToggleGroup();

	// reference l'application principale
	private MainApp mainApp;

	/**
	 * Constructeur
	 **/
	public MajStockController() {
	}

	/**
	 * Initialise le controller
	 */
	@FXML
	private void initialize() {
		this.maRadio.setToggleGroup(group);
		this.produitRadio.setToggleGroup(group);
		this.messageExport.setText("");
		// Initialise les colonne du tableau d'elements
		
		this.codeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Element>, String>, ObservableValue<String>>() {            
			@Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Element>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue().getCode());
            }
        });
		
		this.nomColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Element>, String>, ObservableValue<String>>() {            
			@Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Element>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue().getNom());
            }
        });
		
		this.quantiteColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Element>, Double>, ObservableValue<Double>>() {            
			@Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Map.Entry<String, Element>, Double> p) {
                // for second column we use value
                return new SimpleDoubleProperty(p.getValue().getValue().getQuantite()).asObject();
            }
        });
		
		this.mesureColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Element>, String>, ObservableValue<String>>() {            
			@Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Element>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue().getNom());
            }
        });

		// mettre les details à vide
		showElementDetails(null);

		// Ecouter les changements de selection d'un element dans le tableau
		this.elementTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showElementDetails(newValue));

	}

	/**
	 * appelé par l'application main
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		ObservableList<Map.Entry<String, Element>> items = FXCollections.observableArrayList(this.mainApp.getElementData().entrySet());
		
		this.elementTable.setItems(items);
		this.elementTable.getSortOrder().addAll(this.codeColumn);
	}

	/**
	 * Afficher les détails d'un élément
	 * 
	 * @param person
	 */
	private void showElementDetails(Entry<String, Element> newValue) {
		if (newValue != null) {
			// Remplir les labels avec les informations de l'élément passé en parametre
			this.codeLabel.setText(newValue.getValue().getCode());
			if (newValue.getValue().getPrixAchat() == -1) {
				this.prixAchatLabel.setText("Aucun");
			} else {
				this.prixAchatLabel.setText(String.valueOf(newValue.getValue().getPrixAchat()));
			}
			if(newValue.getValue().getClass().getSimpleName().equals("Produit")) {	
				this.typeLabel.setText("Produit");
			}else {
				this.typeLabel.setText("Matière première");	
			}
			if (newValue.getValue().getPrixVente() == -1) {
				this.prixVenteLabel.setText("Aucun");
			} else {
				this.prixVenteLabel.setText(String.valueOf(newValue.getValue().getPrixVente()));
			}
			
		} else {
			// si element est null.
			this.codeLabel.setText("");
			this.prixAchatLabel.setText("");
			this.prixVenteLabel.setText("");
			this.typeLabel.setText("");
		}
	}

	/**
	 * Appelé lors du clique sur le bouton supprimé
	 */
	@FXML
	private void handleDeleteElement() {
		int selectedIndex = this.elementTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Confirmation");
			alert.setHeaderText("Etes-vous sûr de vouloir supprimer ces éléments?");

			// option != null.
			Optional<ButtonType> option = alert.showAndWait();

			if (option.get() == null) {
				this.messageExport.setText("No selection!");
			} else if (option.get() == ButtonType.OK) {
				this.messageExport.setText("Elément(s) supprimé(s)");
				this.elementTable.getItems().remove(selectedIndex);
			} else if (option.get() == ButtonType.CANCEL) {
				this.messageExport.setText("Suppression annulée");
			} else {
				this.messageExport.setText("");
			}
		} else {
			// Aucune selection.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Aucune sélection");
			alert.setHeaderText("Aucun élément séletionné");
			alert.setContentText("Veuillez sélectionner un ou plusieurs éléments dans le tableau.");

			alert.showAndWait();
		}
	}

	/**
	 * Appelé lors du clique sur le bouton ajouter
	 */
	@FXML
	private void handleNew() {
		if (!this.maRadio.isSelected() && !this.produitRadio.isSelected()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Aucune sélection");
			alert.setHeaderText("Aucun type séletionné");
			alert.setContentText("Veuillez sélectionner un type \n (matière première ou produit");
			alert.showAndWait();			
		}
		int id = this.mainApp.getElementData().size() + 1;
		if (this.maRadio.isSelected()) {
			MatierePremiere ma = new MatierePremiere("", "", 0, "", 0, 0);
			if(id < 10) {
				ma.setCode("E00" + id);
			}
			if(id >= 10 && id < 100) {
				ma.setCode("E0" + id);
			}
			if(id >= 100) {
				ma.setCode("E" + id);
			}
			boolean okClicked = mainApp.showElementEditDialog(new AbstractMap.SimpleEntry<String, Element>(ma.getCode(), ma));
			if (okClicked) {
				mainApp.getElementData().put(ma.getCode(), ma);
			}
		}
		if (this.produitRadio.isSelected()) {
			Produit ma = new Produit("", "", 0, "", 0, 0, false);
			if(id < 10) {
				ma.setCode("E00" + id);
			}
			if(id >= 10 && id < 100) {
				ma.setCode("E0" + id);
			}
			if(id >= 100) {
				ma.setCode("E" + id);
			}
			boolean okClicked = mainApp.showElementEditDialog(new AbstractMap.SimpleEntry<String, Element>(ma.getCode(), ma));
			if (okClicked) {
				mainApp.getElementData().put(ma.getCode(), ma);
			}
		}
	}
	
	/**
	 * Appelé lors du clique sur le bouton modifier
	 */
	@FXML
	private void handleEditElement() {
		Entry<String, Element> selectedElement = this.elementTable.getSelectionModel().getSelectedItem();
	    if (selectedElement != null) {
	        boolean okClicked = this.mainApp.showElementEditDialog(selectedElement);
	        if (okClicked) {
	            showElementDetails(selectedElement);
	        }

	    } else {
	        // Nothing selected.
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(mainApp.getPrimaryStage());
	        alert.setTitle("Aucune sélèction");
	        alert.setHeaderText("Aucun élément selectionné");
	        alert.setContentText("Veuillez choisir un élément à modifier.");

	        alert.showAndWait();
	    }
	}
	
	 /**
     * Appelé lors du clique sur le bouton enregistrer
     */
    @FXML
    private void handleExporter() {
    	 Alert alert = new Alert(AlertType.CONFIRMATION);
         alert.initOwner(mainApp.getPrimaryStage());
         alert.setTitle("Confirmation");
         alert.setHeaderText("Etes-vous sûr de vouloir enregistrer vos modifications?");

         // option != null.
         Optional<ButtonType> option = alert.showAndWait();
    
         if (option.get() == null) {
            this.messageExport.setText("No selection!");
         } else if (option.get() == ButtonType.OK) {
            this.messageExport.setText("Modifications enregistrés");
            //(new ImportExportCsv()).writeCsvElement("newElements.csv", this.mainApp.getElementData());
            //(new ImportExportCsv()).writeCsvElement("oldElements.csv", this.mainApp.getElementData());
            
         } else if (option.get() == ButtonType.CANCEL) {
            this.messageExport.setText("Modifications annulées");
         } else {
            this.messageExport.setText("");
         }
    	
    }

}
