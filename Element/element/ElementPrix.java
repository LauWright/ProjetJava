package element;

public class ElementPrix {
	private String codeChaine;
	private String codeElement;
	private String nom;
	private int idSemaine;
	
	public ElementPrix(String codeChaine, String codeElement, String nom, int idSemaine) {
		this.codeChaine = codeChaine;
		this.codeElement = codeElement;
		this.nom = nom;
		this.idSemaine = idSemaine;
	}
	
	public String getCodeElement() {
		return this.codeElement;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public int getIdSemaine() {
		return this.idSemaine;
	}
	
	public String getcodeChaine() {
		return this.codeChaine;
	}
}
