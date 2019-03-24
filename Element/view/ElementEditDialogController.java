package view;

import element.Element;
import element.MatierePremiere;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ElementEditDialogController {
	@FXML
    private TextField codeField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField quantiteField;
    @FXML
    private TextField mesureField;
    @FXML
    private TextField prixAchatField;
    @FXML
    private TextField prixVenteField;
    
    private Stage dialogStage;
    private Element element;
    private boolean okClicked = false;
    
    /**
     * Initialise le controller
     */
    @FXML
    private void initialize() {
    }
    
    /**
     * Set le stage de la fenetre de dialogue
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
    * initialise l'eeemnt qui va être edité
    *
    * @param person
    */
   public void setElement(Element element) {
       this.element = element;
       if(element.getClass().getSimpleName().equals("MatierePremiere")) {
    	   MatierePremiere ma = (MatierePremiere) element;
    	   this.prixAchatField.setText(String.valueOf(ma.getPrixAchat()));
       } else {
    	   this.prixAchatField.setText("Aucun");
    	   this.prixAchatField.setDisable(true);
       }
       this.codeField.setText(this.element.getCode());
       this.codeField.setDisable(true);
       this.nomField.setText(this.element.getNom());
       if (!this.element.getCode().equals("") && !this.element.getNom().equals("")) {
    	   this.nomField.setDisable(true);
       }
       this.quantiteField.setText(String.valueOf(this.element.getQuantite()));
       this.mesureField.setText(this.element.getMesure());
       if(this.element.getPrixVente() == -1) {
    	   this.prixVenteField.setText("Aucun"); 
       } else {
    	   this.prixVenteField.setText(String.valueOf(this.element.getPrixVente()));
       }
   }
   
   
   /**
    * Retourne vrai si l'utilisateur clique sur valider sinon aux
    *
    * @return
    */
   public boolean isOkClicked() {
       return okClicked;
   }
   
   /**
    * Appelé quand l'utilisateur clique sur valider
    */
   @FXML
   private void handleOk() {
       if (isInputValid()) {
    	   this.element.setCode(this.codeField.getText());
    	   this.element.setNom(this.nomField.getText());
           this.element.setQuantite(Double.parseDouble(this.quantiteField.getText()));
           this.element.setMesure(this.mesureField.getText());
           if(this.prixVenteField.getText().equals("Aucun")) {
        	   this.element.setPrixVente(Double .valueOf(-1));
           } else {
        	   this.element.setPrixVente(Double .valueOf(this.prixVenteField.getText()));
           }
           
           if(this.element.getClass().getSimpleName().equals("MatierePremiere")) {
        	   MatierePremiere ma = (MatierePremiere) element;
        	   ma.setPrixAchat(Double.parseDouble(this.prixAchatField.getText()));;
           }
           okClicked = true;
           dialogStage.close();
       }
   }
   
   /**
    * CAppelé lors du clique sur annuler
    */
   @FXML
   private void handleCancel() {
       dialogStage.close();
   }

   /**
    * Valide les entrées saisies dans les textfield
    *
    * @return true if the input is valid
    */
   private boolean isInputValid() {
       String errorMessage = "";

       if (this.quantiteField.getText() == null || this.quantiteField.getText().length() == 0) {
           errorMessage += "Aucune quantite valide\n";
       }

       if (this.mesureField.getText() == null || this.mesureField.getText().length() == 0) {
           errorMessage += "Aucun mesure valide\n";
       }

       if (this.prixAchatField.getText() == null || this.prixAchatField.getText().length() == 0) {
           errorMessage += "Aucun prix d'achat valide \n";
       } 

       if (errorMessage.length() == 0) {
           return true;
       } else {
           // Show the error message.
           Alert alert = new Alert(AlertType.ERROR);
           alert.initOwner(dialogStage);
           alert.setTitle("Champs incorrect");
           alert.setHeaderText("Veuillez corriger les champs incorrects");
           alert.setContentText(errorMessage);

           alert.showAndWait();

           return false;
       }
   }

}
