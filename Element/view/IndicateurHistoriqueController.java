package view;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import element.*;

import element.Achat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import production.ChaineProduction;
import production.Programmation;
import production.Semaine;

public class IndicateurHistoriqueController {

	// reference l'application principale
		private MainApp mainApp;
		@FXML
		private ScrollPane scrollChaine;
		@FXML
		private GridPane gridSem; 
		@FXML
		private GridPane gridProg;
		@FXML
		private ChoiceBox choiceProg;
		@FXML
		private Label res;
		@FXML
		private Label dep;
		@FXML
		private Label ven;
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
			
			
			

			
			for(Programmation p : this.mainApp.getProgrammations()) {
				Button b = new Button ("Programmation "+p.getId()); 
				b.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						tableSemaine(p.getId());
					}
				});
				this.gridProg.add(b, 0, p.getId());
			}
			
			this.res.setText("0");
			this.dep.setText("0");
			this.ven.setText("0");
			
			
		}
		
		public void tableSemaine(int id) {
			Label semaineLabel = new Label("Semaine");
				
			int resTotal=0;
			
			int dep = 0;
			
			int vente = 0;
			
			this.gridSem.getChildren().clear();
			
			this.gridSem.add(semaineLabel, 0, 0);
			
			
			Programmation p = this.mainApp.getProgrammations().get(id-1);
			
			
			int i = 1;
			for (Semaine s : p.getSemaines()) {
				
				for(Entry<String, Element> elem : s.getStockPreviEntree().entrySet()) {
					dep+=elem.getValue().getPrixAchat();
				}
				
				for(Entry<String, Element> elem : s.getStockPreviEntree().entrySet()) {
					vente+=elem.getValue().getPrixVente();
				}
				
				resTotal += s.getResultat();
				
				
				for(Achat a : s.getAchats()) {
					
					dep+=a.getPrixAchat();
					
				}
				
				
				Label semLab = new Label("Semaine "+s.getIdSemaine());
				this.gridSem.add(semLab, 0, i);
				i++;
			}
			

			
			this.res.setText(resTotal+"");
			this.dep.setText(dep+"");
			this.ven.setText(vente+"");
		}
		
		@FXML
		public void test() {
			
			System.out.println("Methode test");
			//List<Programmation> p = 
			
			
			/*for(Programmation p : this.mainApp.getProgrammations()) {
				for (Semaine s : p.getSemaines()) {
					System.out.println(s.getResultat()+"");
					
					Set cles = s.getStockPreviEntree().keySet();
					Iterator it = cles.iterator();
		
					while (it.hasNext()){
					   Object cle = it.next(); // tu peux typer plus finement ici
					   Element valeur = s.getStockPreviEntree().get(cle); // tu peux typer plus finement ici
					   System.out.println(valeur.getNom());
					}
				}
			}*/
			//methode tab chaine créer des checkbox dynamiquement
			
		}
		
		
}
