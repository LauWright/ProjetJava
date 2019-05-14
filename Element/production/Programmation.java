package production;

import java.util.ArrayList;
import java.util.List;

public class Programmation {
	private int idProgrammation;
	private List<Semaine> semaines;
	
	public Programmation(int id, List<Semaine> s) {
		this.idProgrammation = id;
		this.semaines = s;
	}

	public List<Semaine> getSemaines() {
		return semaines;
	}

	public int getId() {
		return idProgrammation;
	}
	
	public Semaine getPrixMoinsCher(String codeElement) {

		Semaine res = this.semaines.get(0);;
		for(Semaine s : this.semaines) {
			if(s.getStockPreviEntree().get(codeElement).getPrixAchat() != -1) {
				if(s.getStockPreviEntree().get(codeElement).getPrixAchat() < res.getStockPreviEntree().get(codeElement).getPrixAchat()) {
					res = s;
				}
			}
		}
		return res;
	}
	
}