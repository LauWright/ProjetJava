package production;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Demande {
	private IntegerProperty idSemaine;
	private StringProperty codeElement;
	private StringProperty nomElement;
	private DoubleProperty quantiteDemande;
	
	
	/**
	 * Constructeur d'une demande
	 * @param id
	 * @param code
	 * @param quantite
	 */
	public Demande(int id, String code, String nom, double quantite) {
		this.idSemaine = new SimpleIntegerProperty(id);
		this.codeElement = new SimpleStringProperty(code);
		this.nomElement = new SimpleStringProperty(nom);
		this.quantiteDemande = new SimpleDoubleProperty(quantite);
	}


	public int getIdSemaine() {
		return idSemaine.get();
	}
	
	public IntegerProperty getIdPropertySemaine() {
		return idSemaine;
	}


	public String getCodeElement() {
		return codeElement.get();
	}
	
	public StringProperty getCodePropertyElement() {
		return codeElement;
	}

	public String getNomElement() {
		return nomElement.get();
	}
	
	public StringProperty getNomPropertyElement() {
		return nomElement;
	}

	public double getQuantiteDemande() {
		return quantiteDemande.get();
	}
	
	public DoubleProperty getQuantitePropertyDemande() {
		return quantiteDemande;
	}

}
