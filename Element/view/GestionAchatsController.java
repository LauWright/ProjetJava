package view;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import element.Element;
import element.ElementPrix;
import element.ElementVariation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import production.ChaineProduction;
import production.Semaine;

public class GestionAchatsController {
	
	@FXML
	private GridPane achatGrid;
	
	private Stage dialogStage;

	// reference l'application principale
	private MainApp mainApp;
	
	private Semaine semaine;
	
	/**
	 * Constructeur
	 */
	public GestionAchatsController() {
	}

	/**
	 * Initialise le controller appelé automatiquement à l'ouverture de la page
	 * .fxml
	 */
	@FXML
	private void initialize() {
		
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
	public void setMainApp(MainApp mainApp, Semaine s, List<ElementPrix> elemP) {
		this.mainApp = mainApp;
		this.semaine = s;
		this.setGridPane(this.semaine, elemP);
	}
	
	/**
	 * Initialisation de l'affichage de la gestion des achats
	 * @param s
	 * @param elemP
	 */
	public void setGridPane(Semaine s, List<ElementPrix> elemP) {
		String codeChaine = "";
		int i = 0;
		for(ElementPrix e : elemP) {
			if(codeChaine.equals(e.getcodeChaine())) {
				
				Label se = new Label( e.getCodeElement() + " " + e.getNom() + " moins cher en semaine " + e.getIdSemaine());
				
				this.achatGrid.add(se, 0, i);
				i++;
				codeChaine = e.getcodeChaine();
			} else {
				ChaineProduction chainetmp = null;
				for(Integer j : s.getChaineProductionNiveau().keySet()) {
					for(ChaineProduction cha : s.getChaineProductionNiveau().get(j)) {
						if(cha.getCode().equals(e.getcodeChaine())) {
							chainetmp = cha;
							break;
						}
					}
				}
				
				Label blancc = new Label("");
				this.achatGrid.add(blancc, 0, i);
				i++;
				
				codeChaine = chainetmp.getCode();
				CheckBox ch = new CheckBox();
				ch.setText(chainetmp.getCode() + " " + chainetmp.getNom());
				this.achatGrid.add(ch, 0, i);
				i++;
				
				Label blanc = new Label("");
				this.achatGrid.add(blanc, 0, i);
				i++;
				
				Label se = new Label(e.getCodeElement() + " " + e.getNom() + " moins cher en semaine " + e.getIdSemaine());
				
				this.achatGrid.add(se, 0, i);
				i++;
			}
		}
	}
	
	/**
	 * Clique sur le bouton valider, pour enlever les chaines coché
	 */
	@FXML
	public void gestionAchat() {
		for (Node n : this.achatGrid.getChildren()) {
			if(n.getClass().getSimpleName().equals("CheckBox")) {
				CheckBox cb = (CheckBox) n;
				String codeC = cb.getText().split(" ")[0];
				ChaineProduction chaine = null;
				int key = 0;
				if(cb.isSelected()) {
					for(Integer j : this.semaine.getChaineProductionNiveau().keySet()) {
						for(ChaineProduction cha : this.semaine.getChaineProductionNiveau().get(j)) {
							if(cha.getCode().equals(codeC)) {
								chaine = cha;
								key = j;
							}
						}
					}
					this.semaine.getChaineProductionNiveau().get(key).remove(chaine);
				}
			}
		}
		dialogStage.close();
	}	
}
