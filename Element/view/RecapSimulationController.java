package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RecapSimulationController {
	@FXML
	private Label recapLabel;
	@FXML
	private Label efficLabel;
	
	private Stage dialogStage;
	
	
	/**
	 * Initialisation du controller
	 */
	@FXML
    private void initialize() {
		this.recapLabel.setText("");
    }
	
	/**
	 * Initialise la popup
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	/**
	 * Set le texte du label recap
	 * @param s
	 */
	public void setText(String s, String effic) {
		this.recapLabel.setText(s);
		this.efficLabel.setText(effic);
	}
	
	 /**
     * Appelé lors du clique sur ok
     */
    @FXML
    private void handleOk() {
            dialogStage.close();
    }

}
