
import java.util.HashMap;

public class Judet {
	public String judet;
	public HashMap<String, Oras> orase;
	
	public Judet(String nume) {
		this.judet = nume;
		orase = new HashMap<String, Oras>();
	}
	
	@Override
	public String toString() {
		for (String key : orase.keySet()) {
			System.out.println("\t" + key + ":");
			System.out.println(orase.get(key));
		}

		return "";
	}
}
