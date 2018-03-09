
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainClass {
	public static HashMap<String, Tara> tari;
	public static HashMap<String, Locatie> locatii; //un hash map cu toate locatiile, cu Key-ul "Oras", pentru a intoarce in O(1) o locatie
													// cu toate ca ocupa spatiu, avem timp O(1) la un task;
	public static Locatie ceaMaiIeftinaLocatie;  //stochez aici cea mai ieftina locatie pe 10 zile in momentul in care citesc locatiile din fisier;
	public static float celMaiIeftinPret = Float.MAX_VALUE;
	
	public static LinkedList<Locatie> topFive;
	
 	public static void readFile(String name) {
		tari = new HashMap<String, Tara>();
		locatii = new HashMap<String, Locatie>();
		
		File file = new File(name);
		Scanner scn = null;
		try {
			scn = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Fisierul nu a fost gasit!");
			e.printStackTrace();
		}
		
		String [] line;
		while(scn.hasNextLine()) {
		
			line = scn.nextLine().trim().split(", ");
					
			String [] activitatiTemp = line[5].trim().split("; ");
			ArrayList<String> activitati = new ArrayList<String>();
			for(int i = 0; i < activitatiTemp.length; i++)
				activitati.add(activitatiTemp[i]);
			@SuppressWarnings("deprecation")
			Date data = new Date(Integer.parseInt(line[6].trim().split("/")[2]), 
								 Integer.parseInt(line[6].trim().split("/")[1]), 
								 Integer.parseInt(line[6].trim().split("/")[0]));
			
			
			Locatie locatie = new Locatie(line[0], line[3], Float.parseFloat(line[4]), activitati, data, Integer.parseInt(line[7]) );
			if(locatie.pretPerZi < celMaiIeftinPret) {
				celMaiIeftinPret = locatie.pretPerZi;
				ceaMaiIeftinaLocatie = locatie;
			}
							// mi-am creat locatie, din linia de input
			locatii.put(line[0], locatie); 
							// adaug locatia in structura mea tara -> judet -> oras -> locatie
			
			if(!tari.containsKey(line[1])){ 					//daca nu contine tara, creed tara cu tot cu judet, oras si locatie
				Tara tara = new Tara(line[1]);
				Judet judet = new Judet(line[2]);
				Oras oras = new Oras(line[3]);
				oras.locatii.add(locatie);
				judet.orase.put(line[3], oras);
				tara.judete.put(line[2], judet);
				tari.put(line[1], tara);
			} else {
				Tara tara = tari.get(line[1]);					// daca contine tara, intorc tara si ii mai adaug inca un judet cu oras si locatie
				if(!tara.judete.containsKey(line[2])) {			// daca nu contine nici judetul, creez judetul cu tot cu oras si locatie
					Judet judet = new Judet(line[2]);
					Oras oras = new Oras(line[3]);
					oras.locatii.add(locatie);
					judet.orase.put(line[3], oras);
					tara.judete.put(line[2], judet);
				} else {
					Judet judet = tara.judete.get(line[2]);		//daca contine judetul, intorc judetul si mai adaug inca un oras cu locatie
					if(!judet.orase.containsKey(line[3])) {		//daca nu contine nici orasul, creez un nou oras cu tot cu locatie
						Oras oras = new Oras(line[3]);
						oras.locatii.add(locatie);
						judet.orase.put(line[3], oras);
					} else {
						Oras oras = judet.orase.get(line[3]); 	//ajunge aici cand exista tara, exista judetul dar nu exita orasul
						oras.locatii.add(locatie);
					}
				}
			}
		}
		scn.close();		
	}

 	public static long daysBetween(Date one, Date two) { 
 		long difference = (one.getTime()-two.getTime())/86400000; 
 		return difference; 
 	}
 	
 	public static void findTopFive(int var, String locatie, Date data, int perioada) {
 		topFive = new LinkedList<Locatie>();
 		String [] line = locatie.split("/");
 		switch(var) {
 		case 1: // Tara
 			Tara tara = tari.get(line[0]);
 			for (String key1 : tara.judete.keySet()) {
 				Judet judet = tara.judete.get(key1);
 	 			for (String key : judet.orase.keySet()) {
 					Oras oras = judet.orase.get(key);
 					for(Locatie x : oras.locatii){
 		 				if( daysBetween(x.deCand, data) > 0 ) { // data de inceput dorita e mai devreme decat data de inceput a locatiei
 		 					continue;
 		 				}
 		 				if( x.perioada + daysBetween(x.deCand, data) < perioada) {  //perioada dorita este mai mare decat perioada posibila
 		 					continue;
 		 				}
 		 				
 		 				topFive.add(x);
 		 				if(topFive.size() == 6) {
 		 					Collections.sort(topFive, new ComparatorPret());
 		 					topFive.removeLast();
 		 				}
 		 			}
 				}
 			}
 			break;
 			
 		case 2: //Judet
 			Judet judet = tari.get(line[0]).judete.get(line[1]);
 			for (String key : judet.orase.keySet()) {
				Oras oras = judet.orase.get(key);
				for(Locatie x : oras.locatii){
	 				if( daysBetween(x.deCand, data) > 0 ) { // data de inceput dorita e mai devreme decat data de inceput a locatiei
	 					continue;
	 				}
	 				if( x.perioada + daysBetween(x.deCand, data) < perioada) {  //perioada dorita este mai mare decat perioada posibila
	 					continue;
	 				}
	 				
	 				topFive.add(x);
	 				if(topFive.size() == 6) {
	 					Collections.sort(topFive, new ComparatorPret());
	 					topFive.removeLast();
	 				}
	 			}
			}
 			break;
 		
 		case 3: //Oras		
 			Oras oras = tari.get(line[0]).judete.get(line[1]).orase.get(line[2]);
 			for(Locatie x : oras.locatii){
 				if( daysBetween(x.deCand, data) > 0 ) { // data de inceput dorita e mai devreme decat data de inceput a locatiei
 					continue;
 				}
 				if( x.perioada + daysBetween(x.deCand, data) < perioada) {  //perioada dorita este mai mare decat perioada posibila
 					continue;
 				}
 				
 				topFive.add(x);
 				if(topFive.size() == 6) {
 					Collections.sort(topFive, new ComparatorPret());
 					topFive.removeLast();
 				}
 			}
 			break;
 		}
 	}
 	
	public static void print() {
		if(!tari.isEmpty()) {  // printare structura Tara - > Judet - > Oras
			for (String key : tari.keySet()) {
				System.out.println("\t\t\t" + key + ":");
				System.out.println(tari.get(key));
			}
		} else {
			System.out.println("Nu exista nicio locatie");
		}
		
		if(!locatii.isEmpty()) {  // printare toate locatiile
			for (String key : locatii.keySet()) {
				System.out.println(locatii.get(key));
			}
		}
		
		
	}

	public static void printComenzi() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			System.out.println("Probleme cu Sleep(1)-ul");
			e.printStackTrace();
		}
		System.out.println("Introduceti comanda:");
		System.out.println("\t1. Informatii despre o locatie.");
		System.out.println("\t2. Top 5 locatii din tara/judetul/orasul X, incepand cu data zz/mm/yyyy, timp de Y zile.");
		System.out.println("\t3. Cea mai ieftina locatie pentru 10 zile.");
		System.out.println("\t4. Exit.");
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
		
		readFile("test1");
		print();
		File fileIn = new File("date.in");
		Scanner keyboard = null;
		try {
			keyboard = new Scanner(fileIn);
		} catch (FileNotFoundException e) {
			System.out.println("Nu s-a gasit fisierul date.in");
			e.printStackTrace();
		}
		int var;
		String command;
		String oras;
		String dataTemp;
		Date data;
		int perioada;
		boolean exit = false;
		
		while(!exit) {
			
			printComenzi();
			
			var = keyboard.nextInt();
			
			switch(var) {
			case 1:  // Afisarea unei singure locatii gasite dupa "NUME"
				System.out.println("Introduceti numele locatiei:");
				keyboard.nextLine(); // eliberez "\n"-ul blocat in buffer
				command = keyboard.nextLine();//.trim();
				System.out.println(locatii.get(command) + "\n");
				break;
			case 2:  // Afisare a top 5 locatii din Tara/Judet/Oras incepand din data X, pe o perioada de Y zile
				System.out.println("Alegeti Varianta: 1-Tara, 2-Judet, 3-Oras:");
				var = keyboard.nextInt();
				System.out.println("Introduceti in formatul: " + (var == 1 ? "Tara:" : (var == 2 ? "Tara/Judetul:" : "Tara/Judetul/Orasul:")));
				keyboard.nextLine();  // eliberez "\n"-ul blocat in buffer
				oras = keyboard.nextLine();
				System.out.println("Introduceti data de inceput in formatul dd/mm/yyyy:");
				dataTemp = keyboard.nextLine();
				data = new Date(Integer.parseInt(dataTemp.trim().split("/")[2]), 
						  Integer.parseInt(dataTemp.trim().split("/")[1]), 
						  Integer.parseInt(dataTemp.trim().split("/")[0]));
				System.out.println("Introduceti perioada:");
				perioada = keyboard.nextInt();
				
				findTopFive(var, oras, data, perioada);
				Collections.sort(topFive, new ComparatorPret());
				System.out.println("Cele " + topFive.size() + " locatii sunt:");
				for(Locatie x : topFive)
					System.out.println(x);
				
				break;
			case 3:
				System.out.println("Cea mai ieftina locatie este:");
				System.out.println(ceaMaiIeftinaLocatie);
				System.out.println("Pretul pe 10 zile este de " + ceaMaiIeftinaLocatie.pretPerZi*10 + " euro.\n");
				break;
			case 4:
				exit = true;
				break;
			}
		}
		keyboard.close();
	}

}

class ComparatorPret implements Comparator<Locatie>{

	@Override
	public int compare(Locatie o1, Locatie o2) {
		if(o1.pretPerZi < o2.pretPerZi)
			return -1;
		else
			return 1;
	}
	 
    
}
