package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import element.Element;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import production.ChaineProduction;
import production.Couple;
import production.ImportExportCsv;

public class ChaineController {
	
	@FXML
    private TableView<Map.Entry<String, ChaineProduction>> chaineTable;
    @FXML
    private TableColumn<Map.Entry<String, ChaineProduction>, String> codeColumn;
    @FXML
    private TableColumn<Map.Entry<String, ChaineProduction>, String> nomColumn;
    
    @FXML
    private Label code;
    @FXML
    private Label entrees;
    @FXML
    private Label sorties;
    @FXML
    private Label messageExport;
    
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
    	this.messageExport.setText("");
        // Initialise le table des chaines
    	
        this.codeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, ChaineProduction>, String>, ObservableValue<String>>() {            
			@Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, ChaineProduction>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue().getCode());
            }
        });
        
        this.nomColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, ChaineProduction>, String>, ObservableValue<String>>() {            
			@Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, ChaineProduction>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue().getNom());
            }
        });
        
        showChaineDetails(null);
        
        //Ajoute un listener pour ecouter les changement de selection dans le tableau
        this.chaineTable.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> showChaineDetails(newValue));
        
    }
    
    /**
     * Appelé par le mainApp
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
    	this.mainApp = mainApp;
        // Add observable list data to the table
    	ObservableList<Map.Entry<String, ChaineProduction>> items = FXCollections.observableArrayList(this.mainApp.getChaineData().entrySet());
        this.chaineTable.setItems(items);
        this.chaineTable.getSortOrder().addAll(this.codeColumn);
    }
    
    private void showChaineDetails(Entry<String, ChaineProduction> newValue){
    	if(newValue != null) {
    		//remplissage des labels avec les détails de la chaine selectionné dans le tableau
    		this.code.setText(newValue.getValue().getCode());
    		//gestion des entrees
    		List<Couple> entreesChaine = newValue.getValue().getEntrees();
    		String e = "";
    		for(Couple c : entreesChaine) {
    			e += c.toString()+"\n";
    		}
    		this.entrees.setText(e);
    		//gestion des sorties
    		List<Couple> sortiesChaine = newValue.getValue().getSorties();
    		String s = "";
    		for(Couple c : sortiesChaine) {
    			s += c.toString() +"\n";
    		}
    		this.sorties.setText(s);
    	} else {
    		//si la chaine est null on efface ce qu'i y a dans le detail
    		this.code.setText("");
    		this.entrees.setText("");
    		this.sorties.setText("");		
    	}
    }
    
    /**
     * Appelé lorsque l'uilisateur clique sur ajouter, ouvre la fenetre de dialogue d'ajout d'une chaine
     */
    @FXML
    private void handleNewChaine() {
    	this.messageExport.setText("");
    	List<Couple> evide = new ArrayList<>();
    	List<Couple> svide = new ArrayList<>();
    	ChaineProduction tempChaine = new ChaineProduction("", "", evide, svide);
    	int id = this.mainApp.getChaineData().size() + 1;
    	if(id < 10) {
    		tempChaine.setCode("C00" + id);
		}
		if(id >= 10 && id < 100) {
			tempChaine.setCode("C0" + id);
		}
		if(id >= 100) {
			tempChaine.setCode("C" + id);
		}
        boolean okClicked = this.mainApp.showNewChaineDialog(tempChaine);
        if (okClicked) {
            this.mainApp.getChaineData().put(tempChaine.getCode(), tempChaine);
            ObservableList<Map.Entry<String, ChaineProduction>> items = FXCollections.observableArrayList(this.mainApp.getChaineData().entrySet());
            this.chaineTable.setItems(items);

            this.chaineTable.getSortOrder().addAll(this.codeColumn);
            
        }
       
     }
    
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteChaine() {
    	this.messageExport.setText("");
        int selectedIndex = this.chaineTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
        	 Alert alert = new Alert(AlertType.CONFIRMATION);
             alert.initOwner(mainApp.getPrimaryStage());
             alert.setTitle("Confirmation");
             alert.setContentText("Êtes-vous sûr de vouloir supprimer ces chaînes ?");
             alert.setHeaderText("");
             alert.getDialogPane().setPrefSize(480, 100);

             // option != null.
             Optional<ButtonType> option = alert.showAndWait();
        
             if (option.get() == null) {
                this.messageExport.setText("No selection!");
             } else if (option.get() == ButtonType.OK) {
                this.messageExport.setText("Chaine(s) supprimée(s)");
                this.mainApp.getChaineData().remove(this.chaineTable.getItems().get(selectedIndex).getKey());
                this.chaineTable.refresh();
             } else if (option.get() == ButtonType.CANCEL) {
                this.messageExport.setText("Suppression annulé");
             } else {
                this.messageExport.setText("");
             }
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Aucune selection");
            alert.setHeaderText("Aucune chaîne selectionée");
            alert.setContentText("Veuillez selectionner une chaîne dans le tableau");
            alert.getDialogPane().setPrefSize(480, 100);

            alert.showAndWait();
        }
    }
    
    
    /**
     * Appelé lors du clique sur le bouton enregistrer.
     */
    @FXML
    private void handleExporter() {
    	 Alert alert = new Alert(AlertType.CONFIRMATION);
         alert.initOwner(mainApp.getPrimaryStage());
         alert.setTitle("Confirmation");
         alert.setContentText("Êtes-vous sûr de vouloir enregistrer vos modifications ?");
         alert.setHeaderText("");
         
         alert.getDialogPane().setPrefSize(480, 100);

         // option != null.
         Optional<ButtonType> option = alert.showAndWait();
    
         if (option.get() == null) {
            this.messageExport.setText("Aucune selection");
         } else if (option.get() == ButtonType.OK) {
            this.messageExport.setText("Modifications enregistrés");
            (new ImportExportCsv()).writeCsvChaineProduction(this.mainApp.getChaineData());
         } else if (option.get() == ButtonType.CANCEL) {
            this.messageExport.setText("Modifications non enregistrés");
         } else {
            this.messageExport.setText("");
         }
    	
    }
}
