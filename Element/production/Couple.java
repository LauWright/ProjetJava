package production;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Couple {
	private StringProperty code;
	private DoubleProperty qte;
	
	/**
	 * Constructeur du couple
	 * @param c code d'un élément : String
	 * @param qte quantité d'éléments nécessaires : double
	 */
	public Couple(String c, double qte) {
		this.code = new SimpleStringProperty(c);
		this.qte = new SimpleDoubleProperty(qte);
	}
	
	/**
	 * 
	 * @return la propriété code d'un élément
	 */
	public StringProperty getCodePtoperty() {
		return code;
	}
	
	/**
	 * 
	 * @return le code d'un élément
	 */
	public String getCode() {
		return code.get();
	}
	
	/**
	 * 
	 * @return la propriété quantité nécessaire de cet élément
	 */
	public DoubleProperty getQteProperty() {
		return qte;
	}
	
	/**
	 * 
	 * @return la quantité nécessaire de cet élément
	 */
	public double getQte() {
		return qte.get();
	}
	
	@Override
	public String toString() {
		return "Couple [code=" + this.code + ", qte=" + this.qte + "]";
	}
	
	
}