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
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import production.ChaineProduction;
import production.Couple;
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
		private GridPane gridRes;
		@FXML
		private ChoiceBox choiceProg;

		
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
			
			this.gridRes.getChildren().clear();
			
			int j=0;
			
			List<Integer> list = new ArrayList<>();
			
			int i=0;
			
			
			/**
			 * On récupère les id de semaine sélectionnés
			 */
			
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
			
			/**
			 * Initialisation des indicateurs
			 */
			
			double resTotal=0;
			
			double dep = 0;
			
			double vente = 0;
			
			/**
			 * On récupère la programmation choisie
			 */
			Programmation p = this.mainApp.getProgrammations().get(id-1);

			
			/**
			 * On parcours les semaines de la programmation			
			 */
			for (Semaine s : p.getSemaines()) {
				
				if(j!=0)
					j++;
				
				
				boolean itsOk = false;
				/**
				 * On regarde si la semaine a été sélectionné
				 */
				int o=0;
				while (o<list.size() && !itsOk){
					itsOk = list.get(o) == s.getIdSemaine();
					o++;
				}
				
				/**
				 * Si la semaine a été sélectionné
				 */
				if (itsOk) {
					
					/**
					 * On affiche le numéro de semaine
					 */
					Label semLab = new Label("Semaine : "+s.getIdSemaine());
					this.gridRes.add(semLab, 0, j);
					
					
					/**
					 * On instancie la somme des achats de la semaine et la somme des ventes
					 */
					double achatSemaine = 0;
					double venteSemaine=0;
					double resSemaine = 0;
					double nbProd = 0;
					double benefSemaine = 0;
					double rentaSemaine = 0;
					
					/**
					 * Total des résultats sur toutes les semaines
					 */
					resTotal += s.getResultat();
					
					/**
					 * Résultat de la semaine et affichage
					 */
					resSemaine += s.getResultat();
					
					j++;
					Label resSemLab = new Label("Résultat");
					this.gridRes.add(resSemLab, 0, j);
					
					resSemaine = (double)Math.round(resSemaine * 1000) / 1000;
					
					Label resSem = new Label();
					resSem.setText(resSemaine+"");
					this.gridRes.add(resSem, 1, j);

					Element unElement = null;
					
					Set cles = s.getChaineProductionNiveau().keySet();
					
					Iterator it = cles.iterator();
					
					/**
					 * On parcours les chaines de production de la semaine
					 */
					while (it.hasNext()){
					   Object cle = it.next(); // tu peux typer plus finement ici
					   
					   List<ChaineProduction> valeur =(List<ChaineProduction>) s.getChaineProductionNiveau().get(cle); // tu peux typer plus finement ici
					   
					   /**
					    * On récupère le nombre de fois que la production a été faite
					    */
					   nbProd = (int) cle;
					   
					   /**
					    * Pour récupérer les achats de cette production
					    */
					   double achatProd = 0;
					   
					   /**
					    * Pour chaque chaine on récupère les entrées et on calcul les dépenses pour la fabriquer
					    */
					   for(ChaineProduction cp : valeur) {
						   
						   for(Couple cpl : cp.getEntrees()) {

							   unElement = this.recupElem(cpl.getCode());
							   
							   if (unElement.getClass().getSimpleName().equals("Produit")) {
									
								   	Produit prod = (Produit) unElement;
									
								   	achatProd += prod.getCoutFabrication();
									
								}else {
									
									achatProd += unElement.getPrixAchat();
									
								}
							   
						   }
						   
					   }
					   
					   /**
						 * On multiplie le coût d'achat de la production par le nombre de productions faites
						 */
					   	achatProd = achatProd*nbProd;
					   
						achatSemaine += achatProd;
					   
					}
					
					
					
					/**
					 * On ajoute les achats de la semaine à l'achat total
					 */
					dep+=achatSemaine;
					
					/**
					 * Affichage de la somme d'achat de la semaine
					 */
					j++;
					Label achLab = new Label("Achats");
					this.gridRes.add(achLab, 0, j);
					
					achatSemaine = (double)Math.round(achatSemaine * 1000) / 1000;
					
					Label achCou = new Label();
					achCou.setText(achatSemaine+"");
					this.gridRes.add(achCou, 1, j);
					
					
					
					Set clesVente = s.getChaineProductionNiveau().keySet();
					
					Iterator itVente = clesVente.iterator();
					
					/**
					 * On parcours les sorties pour avoir le prix de vente
					 */
					while (itVente.hasNext()){
						
					   Object cle = itVente.next(); // tu peux typer plus finement ici
					   
					   List<ChaineProduction> valeur =(List<ChaineProduction>) s.getChaineProductionNiveau().get(cle); // tu peux typer plus finement ici
					   
					   /**
					    * On récupère le nombre de fois que la production a été faite
					    */
					   nbProd = (int) cle;
					   
					   /**
					    * Pour récupérer les achats de cette production
					    */
					   double venteProd = 0;
					   
					   /**
					    * Pour chaque chaine de production on additionne le prix de vente
					    */
					   for(ChaineProduction cp : valeur) {
						   
						   for(Couple cpl : cp.getSorties()) {

							   	unElement = this.recupElem(cpl.getCode());
							   	
								Produit prod = (Produit) unElement;
								
								venteProd += prod.getPrixVente();
							   
						   }
					   
						   venteProd = venteProd*nbProd;
						   
					   }
					   
					   venteSemaine += venteProd;
					   
					}
					
					/**
					 * S'il n'y a pas de prix de vente on considère que c'est au moins le coût de production
					 */
					if (venteSemaine<=0) {
					
						
						venteSemaine = achatSemaine;
					
					}
					
					/**
					 * On ajoute au prix total
					 */
					vente+=venteSemaine;
					
					/**
					 * On affiche le prix de vente de la semaine
					 */
					j++;
					Label vntLab = new Label("Vente");
					this.gridRes.add(vntLab, 0, j);
					
					venteSemaine = (double)Math.round(venteSemaine * 1000) / 1000;
					
					Label vntCou = new Label();
					vntCou.setText(venteSemaine+"");
					this.gridRes.add(vntCou, 1, j);
					
					/**
					 * Bénéfice de la semaine
					 */
					benefSemaine = venteSemaine - achatSemaine;
					
					/**
					 * On affiche le bénéfice de la semaine
					 */
					j++;
					Label benefLab = new Label("Bénéfice ");
					this.gridRes.add(benefLab, 0, j);
					
					benefSemaine = (double)Math.round(benefSemaine * 1000) / 1000;
					
					Label benefCou = new Label();
					benefCou.setText(benefSemaine+"");
					this.gridRes.add(benefCou, 1, j);
					
					/**
					 * Rentabilité semaine
					 */
					rentaSemaine = 100*venteSemaine/achatSemaine;
					/**
					 * On affiche la rentabilité de la semaine
					 */
					j++;
					Label rentaLab = new Label("Rentabilité ");
					this.gridRes.add(rentaLab, 0, j);
					
					rentaSemaine = (double)Math.round(rentaSemaine * 1000) / 1000;
					
					Label rentaCou = new Label();
					rentaCou.setText(rentaSemaine+"%");
					this.gridRes.add(rentaCou, 1, j);
					
					Separator separator1 = new Separator();
					j++;
					this.gridRes.add(separator1, 0, j);
					
				} 
				 
			}
			
			
			/**
			 * Affichage du total des semaines
			 */
			j++;
			Label totalRecap = new Label("Total des semaines");
			this.gridRes.add(totalRecap, 0, j);
			
			/**
			 * Affichage du résultat total
			 */
			j++;
			Label resLab = new Label("Resultat");
			this.gridRes.add(resLab, 0, j);
			
			resTotal = (double)Math.round(resTotal * 1000) / 1000;
			
			Label leresTotal = new Label();
			leresTotal.setText(resTotal+"");
			this.gridRes.add(leresTotal, 1, j);
			
			/**
			 * Affichage des dépenses totales
			 */
			j++;
			Label depLab = new Label("Achats");
			this.gridRes.add(depLab, 0, j);
			
			dep = (double)Math.round(dep * 1000) / 1000;
			
			Label ledep = new Label();
			ledep.setText(dep+"");
			this.gridRes.add(ledep, 1, j);
			
			
			j++;
			Label venteTotal = new Label("Revient");
			this.gridRes.add(venteTotal, 0, j);
			
			vente = (double)Math.round(vente * 1000) / 1000;
			
			Label leven = new Label();
			leven.setText(vente+"");
			
			this.gridRes.add(leven, 1, j);
					
			/**
			 * Bénéfice de la programmation
			 */
			double benefProgra = vente - dep;
			
			/**
			 * On affiche le bénéfice de la semaine
			 */
			j++;
			Label benefPLab = new Label("Bénéfice ");
			this.gridRes.add(benefPLab, 0, j);
			
			benefProgra = (double)Math.round(benefProgra * 1000) / 1000;
			
			Label benefPCou = new Label();
			benefPCou.setText(benefProgra+"");
			this.gridRes.add(benefPCou, 1, j);
			
			/**
			 * Rentabilité semaine
			 */
			double rentaProga = 100*vente/dep;
			/**
			 * On affiche la rentabilité de la semaine
			 */
			j++;
			Label rentaLab = new Label("Rentabilité ");
			this.gridRes.add(rentaLab, 0, j);
			
			rentaProga = (double)Math.round(rentaProga * 1000) / 1000;
			
			Label rentaPCou = new Label();
			rentaPCou.setText(rentaProga+"%");
			this.gridRes.add(rentaPCou, 1, j);
			

			
		}
		
		/**
		 * Récupérer un élément à partir d'un code
		 * @param code
		 * @return Element
		 */
		public Element recupElem(String code) {
			
			boolean ok = false;
			
			Element unElement = null;
			
			Set clesElements = this.mainApp.getElementData().keySet();
			
			Iterator itElements = clesElements.iterator();
			
			while (itElements.hasNext() && !ok){
				
				String clefsElements = (String) itElements.next();
				
				unElement = (Element) this.mainApp.getElementData().get(clefsElements);
				
				if(unElement.getCode().equals(code)) {
					
					ok = true;
					
				}
				
			}
			
			return unElement;
			
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
