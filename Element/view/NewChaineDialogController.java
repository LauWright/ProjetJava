package view;

import java.util.ArrayList;
import java.util.List;

import element.Element;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import production.ChaineProduction;
import production.Couple;

public class NewChaineDialogController {

	@FXML
	private TextField codeField;
	@FXML
	private TextField nomField;
	@FXML
	private TextField entreesField;
	@FXML
	private TextField sortiesField;

	@FXML
	private TableView<Element> elementTable;
	@FXML
	private TableColumn<Element, String> codeColumn;
	@FXML
	private TableColumn<Element, String> nomColumn;
	private MainApp mainApp;
	private Stage dialogStage;
	private ChaineProduction chaine;
	private boolean okClicked = false;

	/**
	 * Initialise la vue
	 */
	@FXML
	private void initialize() {
		this.codeColumn.setCellValueFactory(cellData -> cellData.getValue().getCodeProperty());
		this.nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
	}

	/**
	 * Initialise le stage pour cette dialogue
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	/**
	 * reference à l'application pricipale
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        //ajout des elements au tableau
        this.elementTable.setItems(this.mainApp.getElementData());
    }

	/**
	 * Sets the person to be edited in the dialog.
	 *
	 * @param person
	 */
	public void setChaine(ChaineProduction ch) {
		this.chaine = ch;

		this.codeField.setText("");
		this.nomField.setText("");
		this.entreesField.setText("");
		this.sortiesField.setText("");
	}

	/**
	 * Retourne vrai si l'utilisateur clique sur valider, sinon faux
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return this.okClicked;
	}

	/**
	 * Methode appelé lors du clique ur le bouton validé, ajoute une chaine
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			// Liste des entrees
			List<Couple> entrees = new ArrayList<>();
			String e = this.entreesField.getText();
			String[] eSplit = e.split(";");

			for (String en : eSplit) {
				if (!en.equals("")) {
					String[] split = en.split(",");
					Couple entree = new Couple(split[0], Double.parseDouble(split[1]));
					entrees.add(entree);
				}
			}
			// Liste des sorties
			List<Couple> sorties = new ArrayList<>();
			String s = this.sortiesField.getText();
			String[] sSplit = s.split(";");

			for (String so : sSplit) {
				if (!so.equals("")) {
					String[] split = so.split(",");
					Couple sortie = new Couple(split[0], Double.parseDouble(split[1]));
					sorties.add(sortie);
				}
			}

			this.chaine.setCode(this.codeField.getText());
			this.chaine.setNom(this.nomField.getText());
			this.chaine.setEntrees(entrees);
			this.chaine.setSorties(sorties);

			this.okClicked = true;
			this.dialogStage.close();
		}
	}

	private boolean isInputValid() {
		String errorMessage = "";

		if (this.codeField.getText() == null || this.codeField.getText().length() == 0) {
			errorMessage += "Vous devez remplir le champ code \n";
		}
		if (this.nomField.getText() == null || this.nomField.getText().length() == 0) {
			errorMessage += "Vous devez remplir le champ nom \n";
		}
		if (this.entreesField.getText() == null || this.entreesField.getText().length() == 0) {
			errorMessage += "Vous devez remplir le champ des entrées \n";
		}

		if (sortiesField.getText() == null || sortiesField.getText().length() == 0) {
			errorMessage += "Vous devez remplir le champ des sorties \n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Montrer le message d'erreur.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Champ non rempli");
			alert.setHeaderText("Veuillez remplir les champs demandés");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	public ChaineProduction getNewChaine() {
		return this.chaine;
	}

	/**
	 * Methode appelé lors du clique sur annuler, ferme la fenetre de dialogue
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
