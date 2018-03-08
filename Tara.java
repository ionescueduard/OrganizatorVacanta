
import java.util.HashMap;

public class Tara {
	public String tara;
	public HashMap<String, Judet> judete;
	
	public Tara(String nume) {
		this.tara = nume;
		judete = new HashMap<String, Judet>();
	}
	
	@Override
	public String toString() {
		for (String key : judete.keySet()) {
			System.out.println("\t\t" + key + ":");
			System.out.println(judete.get(key));
		}

		return "";
	}
}
