import java.util.ArrayList;

public class Oras {
	public String oras;
	public ArrayList<Locatie> locatii;
	
	public Oras(String nume) {
		this.oras = nume; 
		locatii = new ArrayList<Locatie>();
	}
	
	@Override
	public String toString() {
		for (int i = 0; i < locatii.size(); i++)
			System.out.println(locatii.get(i).toString());
		return "";
	}
}

