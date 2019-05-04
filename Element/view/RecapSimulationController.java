package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import production.ChaineProduction;

public class RecapSimulationController {
	@FXML
	private Label recapLabel;
	@FXML
	private Label efficLabel;
	@FXML
	private Label labelSemaine;
	@FXML
	private ScrollPane scrollChaine;
	
	@FXML
	private AnchorPane ancre;
	
	private Stage dialogStage;
	
	private MainApp mainApp;
	/**
	 * Initialisation du controller
	 */
	@FXML
    private void initialize() {
		this.recapLabel.setText("");
    }
	
	
	public void setMainApp(MainApp mainApp) throws IOException {
		this.mainApp = mainApp;
	}
	
	/**
	 * Initialise la popup
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

    	this.scrollChaine.setContent(this.ancre);
		this.scrollChaine.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scrollChaine.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		this.scrollChaine.setFitToHeight(true);
		this.scrollChaine.setPannable(true);
    }
	
	/**
	 * Set le texte du label recap
	 * @param s
	 */
	public void setText(String s, String effic, int sem) {
		this.recapLabel.setText(s);
		this.efficLabel.setText(effic);
		this.labelSemaine.setText("Semaine " + sem);
	}
	
	 /**
     * Appelé lors du clique sur ok
	 * @throws IOException 
     */
    @FXML
    private void handleOk() throws IOException {
    	
    	dialogStage.close();
            
    }
}
