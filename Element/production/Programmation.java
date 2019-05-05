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
	
	
}