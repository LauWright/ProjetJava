package view;

import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
		
		@FXML
		private Label labelSemaine;
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
		
		/**
		 * 
		 * @param id
		 */
		public void tableSemaine(int id) {
			
			
			this.gridSem.getChildren().clear();
			
			this.labelSemaine.setText("Semaine de la programmation " + id);
			
			Programmation p = this.mainApp.getProgrammations().get(id-1);

			//A voir fonction get ACHAT
			
			int i = 0;
			for (Semaine s : p.getSemaines()) {
				if(s.getResultat() > 0) {
					CheckBox ch = new CheckBox();
					ch.setText("Semaine "+s.getIdSemaine());
					
					Button b = new Button();
					b.setText("Stock prévisionnel");
					b.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							stockPrevi(s);
						}
					});
					this.gridSem.add(ch, 0, i);
					this.gridSem.add(b, 1, i);
					i++;
				}
			} 
			
			Button b = new Button ("Valider"); 
			b.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					creationIndicateurs(id);
				}
			});
			this.gridSem.add(b, 0, i);
			
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
		
		/**
		 * Création des indicateurs
		 * @param id
		 */
		public void creationIndicateurs(int id) {
			
			List<Integer> list = new ArrayList<>();
			int i=0;
			//System.out.println(this.gridSem.getChildren().size()+"");
			for (Node no : this.gridSem.getChildren()) {
				System.out.println(no.getClass().getSimpleName());
				if (GridPane.getRowIndex(no) == i  && GridPane.getColumnIndex(no) == 0 && no.getClass().getSimpleName().equals("CheckBox")) {
					boolean coche = false;
					CheckBox ch = (CheckBox) no;
					if (ch.isSelected()) {
						coche = true;
						list.add(getIdSem(ch.getText()));
					}
					i++;
				}
				
			}
			
			int resTotal=0;
			
			int dep = 0;
			
			int vente = 0;
			
			double test=0;
			
			
			Programmation p = this.mainApp.getProgrammations().get(id-1);

			//A voir fonction get ACHAT
			
			for (Semaine s : p.getSemaines()) {
				boolean itsOk = false;
				int o=0;
				while (o<list.size() && !itsOk){
					itsOk = list.get(o) == s.getIdSemaine();
					o++;
				}
				
				if (itsOk) {
					/**
					 * Prix total des éléments en entrée
					 */
					for(Entry<String, Element> elem : s.getStockPreviEntree().entrySet()) {
						dep+=elem.getValue().getPrixAchat();
					}
					
					/**
					 * Prix total de vente des éléments en entrée
					 */
					for(Entry<String, Element> elem : s.getStockPreviSortie().entrySet()) {
						vente+=elem.getValue().getPrixVente();
					}
					
					/**
					 * Total des résultats sur toutes les semaines
					 */
					resTotal += s.getResultat();
					
					/**
					 * Prix total des achats
					 */
					for(Achat a : s.getAchats()) {
						
						dep+=a.getPrixAchat();
						
					}
					
					
					for(Entry<String, Element> elem : s.getStockPreviEntree().entrySet()) {
						//test+=elem.getValue().getQuantite();
					}
					
					
					
					for (Produit prodM : s.getProduitManquant()) {
						test+=prodM.getPrixAchat();
					}
					
					for(Achat a : s.getAchats()){
						//test+=a.getPrixAchat();
						System.out.println("TEST");
					}
				}
			}
			

			System.out.println(test+" test");
			this.res.setText(resTotal+"");
			this.dep.setText(dep+"");
			this.ven.setText(vente+"");
			
		}
		
		/**
		 * Récupération de l'id de semaine pour une chaine de caractère de ce type "Semaine id"
		 * @param String sem semaine en libelle
		 * @return int id de la semaine
		 */
		public int getIdSem(String sem) {
			boolean ok =false;
			String idSem = "";
			for (int i= 0; i<sem.length() ; i++) {
				if (ok) {
					idSem+=sem.charAt(i);
				}else {
					ok = (' ' == sem.charAt(i));
				}
			}
			
			return Integer.parseInt(idSem);
			
		}
		
		public void stockPrevi(Semaine s) {
			boolean okClicked = this.mainApp.showStockPreviDialog(s);
		}
		
		
}
