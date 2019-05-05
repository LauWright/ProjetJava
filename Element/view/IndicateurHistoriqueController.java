package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class IndicateurHistoriqueController {

	// reference l'application principale
		private MainApp mainApp;
		/**
		 * Constructeur
		 */
		public IndicateurHistoriqueController() {
		}

		/**
		 * Initialise le controller appelé automatiquement à l'ouverture de la page
		 * .fxml
		 */
		@FXML
		private void initialize() {

		}

		/**
		 * appelé par l'application main
		 * 
		 * @param mainApp		 
		 * */
		public void setMainApp(MainApp mainApp) {
			this.mainApp = mainApp;
		}
		
		
}
