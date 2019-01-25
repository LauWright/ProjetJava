package production;

public class Couple {
	private String code;
	private double qte;
	
	public Couple(String c, double qte) {
		this.code = c;
		this.qte = qte;
	}

	public String getCode() {
		return code;
	}

	public double getQte() {
		return qte;
	}
}