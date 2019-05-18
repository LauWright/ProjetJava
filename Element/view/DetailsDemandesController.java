package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DetailsDemandesController {
	@FXML
	private Label programmationlabel;
	
	@FXML
	private Label recapLabel;
	
	@FXML
	private ScrollPane scrollDemande;
	
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

    	this.scrollDemande.setContent(this.ancre);
		this.scrollDemande.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.scrollDemande.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		this.scrollDemande.setFitToHeight(true);
		this.scrollDemande.setPannable(true);
    }
	
	/**
	 * Set le texte du label recap
	 * @param s
	 */
	public void setText(String s, String sp) {
		this.programmationlabel.setText(sp);
		this.recapLabel.setText(s);
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
