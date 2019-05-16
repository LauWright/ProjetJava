package element;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ElementVariation {
	private StringProperty codeElement;
	private StringProperty nom;
	private DoubleProperty quantite;
	private DoubleProperty variation;
	
	
	public ElementVariation(String codeElement, String nom, double quantite, double variation) {
		this.codeElement = new SimpleStringProperty(codeElement);
		this.nom = new SimpleStringProperty(nom);
		this.quantite = new SimpleDoubleProperty(quantite);
		this.variation = new SimpleDoubleProperty(variation);
	}
	public StringProperty getCodeElement() {
		return this.codeElement;
	}
	public StringProperty getNom() {
		return this.nom;
	}
	public DoubleProperty getQuantite() {
		return this.quantite;
	}
	public DoubleProperty getVariation() {
		return this.variation;
	}
	
	

}
