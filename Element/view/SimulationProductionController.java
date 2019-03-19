package view;


import java.io.IOException;
import java.util.List;

import element.Achat;
import element.Element;
import element.MatierePremiere;
import element.ProduitManquant;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import production.ChaineProduction;
import production.Couple;
import production.ImportCsv;

public class SimulationProductionController {
	
	@FXML
	private TableView<Achat> achatsTable;
	@FXML
	private TableColumn<Achat, String> aChaineColumn;
	@FXML
	private TableColumn<Achat, String> aCodeColumn;
	@FXML
	private TableColumn<Achat, String> aNomColumn;
	@FXML
	private TableColumn<Achat, String> aMesureColumn;
	@FXML
	private TableColumn<Achat, Double> aPrixAchatColumn;
	@FXML
	private TableColumn<Achat, Double> QuantiteColumn;
	
    @FXML
	private GridPane gridChaine;
    
	// reference l'application principale
	private MainApp mainApp;

	/**
	 * Constructeur
	 */
	public SimulationProductionController() {		
	}
	
	/**
	 * Initialise le controller appelé automatiquement à l'ouverture de la page
	 * .fxml
	 * @throws IOException 
	 */
	@FXML
	private void initialize() throws IOException {	
		this.aChaineColumn.setCellValueFactory(cellData -> cellData.getValue().getChaine().getCodeProperty());
		this.aCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
        this.aNomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
        this.aMesureColumn.setCellValueFactory(cellData -> cellData.getValue().getMesureProperty());
        this.aPrixAchatColumn.setCellValueFactory(cellData -> cellData.getValue().getPrixAchatProperty().asObject());
        this.QuantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQteAProperty().asObject());
	}
	
	/**
	 * appelé par l'application main
	 * 
	 * @param mainApp
	 * @throws IOException 
	 */
	public void setMainApp(MainApp mainApp) throws IOException {
		this.mainApp = mainApp;
		this.tableChaine();
		this.achatsTable.setItems(this.mainApp.getAchatData());
	}
	
	public void tableChaine() {
		Label chaineLabel = new Label("Chaine");
		Label nivLabel = new Label("Niveau");
		
		this.gridChaine.add(chaineLabel, 0,  0);
		this.gridChaine.add(nivLabel, 1,  0);
		
		ObservableList<ChaineProduction> chaines = this.mainApp.getChaineData();
		for (int i = 0; i < chaines.size(); i++) {
				CheckBox ch = new CheckBox();
				ch.setText(chaines.get(i).getCode() + " " + chaines.get(i).getNom());
				this.gridChaine.add(ch, 0, i+1);
		}
		
		for (int i = 0; i < chaines.size(); i++) {
			TextField tf = new TextField();
			tf.setText("0");
			this.gridChaine.add(tf, 1, i+1);
		}
	}
	
	/**
	 * Clicque sur le bouton simuler
	 */
	@FXML
	public void simuler() {
		//verification que au moins une case est cochée
		boolean coche = false;
		for(int i=0; i< this.mainApp.getChaineData().size(); i++) {
			for(Node no : this.gridChaine.getChildren()) {
	    		if(GridPane.getRowIndex(no) == i+1 && GridPane.getColumnIndex(no) == 0) {
	    			CheckBox ch = (CheckBox) no;
	    			if(ch.isSelected()) {
	    				coche = true;
	    			}
	    		}
			}
		}
		if(coche == false) {
			Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Aucune selection");
            alert.setHeaderText("Aucune chaîne selectionée");
            alert.setContentText("Veuillez selectionner une chaîne \n"
            		+ "et rentrer un niveau.");

            alert.showAndWait();
		}else {
			String s ="";
			for(Node n : this.gridChaine.getChildren()) {
				CheckBox ch;
				for( int i = 0; i< this.mainApp.getChaineData().size(); i++) {
					if(GridPane.getRowIndex(n) == i+1 && GridPane.getColumnIndex(n) == 0) {
			            ch = (CheckBox) n;
			            if(ch.isSelected()){
			            	ChaineProduction c = this.mainApp.getChaineData().get(i);
			            	s+= "Chaîne : " + c.getCode() +"\n";
			            	for(Node no : this.gridChaine.getChildren()) {
			            		if(GridPane.getRowIndex(no) == i+1 && GridPane.getColumnIndex(no) == 1) {
			            			TextField tf = (TextField) no;
			            			List<Couple> entrees = c.getEntrees();
			            			boolean reussi = true;
			            			for(Couple couple : entrees) {
			            				for(Element e : this.mainApp.getElementSimulationData()) {
				            				if(couple.getCode().equals(e.getCode())) {
				            					e.soustraire(couple.getQte()*Double.valueOf(tf.getText()));
				            					System.out.println(e.getQuantite());
			            						if(e.examiner()) {
					            					if(e.getClass().getSimpleName().equals("MatierePremiere")){
					            						MatierePremiere ma = (MatierePremiere) e;
					            						if(ma.getPrixAchat() == -1) {
						            						s += "Production impossible, élément " + e.getCode() +" ne possède pas de prix d'achat et la quantité est insuffisante \n";
						            						e.ajouter(couple.getQte()*Double.valueOf(tf.getText()));
						            						reussi = false;
						            					} else {
						            						this.mainApp.getAchatData().add(new Achat(ma.getCode(), ma.getNom(), e.getQuantite(), ma.getMesure(), ma.getPrixVente(), ma.getPrixAchat(), 0-e.getQuantite(), c));
						            						s+="Attention matière(s) première(s) manquante(s) dans le stock \n";
						            					}	
					            					}
					            					if(e.getClass().getSimpleName().equals("Produit")){				            						
					            							s += "Production impossible, élément " + e.getCode() +" ne possède pas de prix d'achat et la quantité est insuffisante \n";
					            							this.mainApp.getProduitManquantData().add(new ProduitManquant(e.getCode(), e.getNom(), e.getQuantite(), e.getMesure(), e.getPrixVente(), 0-e.getQuantite()));
					            							e.ajouter(couple.getQte()*Double.valueOf(tf.getText()));
					            							reussi = false;
					            					}
			            						}
				            				}
				            			}
			            			}
			            			List<Couple> sorties = c.getSorties();
			            			if(reussi) {
			            				s+= "Produit(s) obtenu(s) : ";
			            				for(Couple so: sorties) {
			            					for(Element e : this.mainApp.getElementSimulationData()) {
				            					if(e.getCode().equals(so.getCode())) {
				            						e.setQuantite(so.getQte()*Double.valueOf(tf.getText()));
				            						s+= e.getCode() + " - " + so.getQte() + "x" + tf.getText() + " " +e.getMesure() + "\n";
				            					}
				            				}
			            				}
			            			}	            					            			
			            		}
			            	}
			            }
			        }
				}
			}
			boolean okClicked = this.mainApp.showRecapSimulationDialog(s);
		}
	}
	/**
	 * Retourne un élément grâce à son code
	 * @param c le code de l'élément cherché
	 * @param observableList
	 * @return l'élément recherché
	 */
	public int getElementByCode(String c, ObservableList<Achat> observableList) {
		boolean ok = false;
		int index= -1;
		for( int i =0; i<observableList.size();i++ ) {
			if (observableList.get(i).getCode().equals(c)){
				ok = true;
				index = i;
			}
		}
		return index;
	}
	/**
	 * Appelé lors du clique sur le bouton réinitialiser
	 */
	@FXML
	public void reinitialiser() {
		this.mainApp.getAchatData().removeAll(this.mainApp.getAchatData());
		this.mainApp.getProduitManquantData().removeAll(this.mainApp.getProduitManquantData());
		this.mainApp.getElementSimulationData().removeAll(this.mainApp.getElementSimulationData());
		this.mainApp.getElementSimulationData().addAll(ImportCsv.importElement("elements1.csv", ';'));
		for(int i=0; i< this.mainApp.getChaineData().size(); i++) {
			for(Node no : this.gridChaine.getChildren()) {
	    		if(GridPane.getRowIndex(no) == i+1 && GridPane.getColumnIndex(no) == 0) {
	    			CheckBox ch = (CheckBox) no;
	    			ch.setSelected(false);
	    		}
			}
			for(Node no : this.gridChaine.getChildren()) {
	    		if(GridPane.getRowIndex(no) == i+1 && GridPane.getColumnIndex(no) == 1) {
	    			TextField tf = (TextField) no;
	    			tf.setText("0");
	    		}
			}
		}
		
	}
	
	/**
	 * Appelé lors du clique sur produire
	 */
	@FXML
	public void produire() {
		
	}
	


}
