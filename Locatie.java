import java.util.ArrayList;
import java.util.Date;

public class Locatie {
	public String numeLocatie;
	public String oras;
	public float pretPerZi;
	public ArrayList<String> activitati;
	public Date deCand;
	public int perioada;
	
	public Locatie(String numeLocatie, String oras, float pretPerZi, ArrayList<String> activitati, Date deCand, int perioada){
		this.numeLocatie = numeLocatie;
		this.oras = oras;
		this.pretPerZi = pretPerZi;
		this.activitati = activitati;
		this.deCand = deCand;
		this.perioada = perioada;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return numeLocatie + 
			   ", Pret: " + pretPerZi + " euro/zi, Activitati: " + activitati + 
			   ", Incepand din data de: " + deCand.getDay() + "/"
										  + deCand.getMonth() + "/"
										  + deCand.getYear() +
		       ", Perioada: " + perioada + "zile.";
	}
}
