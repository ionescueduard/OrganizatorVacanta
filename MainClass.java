
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

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
			Date data = new Date(Integer.parseInt(line[6].trim().split("-")[2]), 
										  Integer.parseInt(line[6].trim().split("-")[1]), 
										  Integer.parseInt(line[6].trim().split("-")[0]));
			
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

	public static void print() {
		if(!tari.isEmpty()) {
			for (String key : tari.keySet()) {
				System.out.println("\t\t\t" + key + ":");
				System.out.println(tari.get(key));
			}
		} else {
			System.out.println("Nu exista nicio locatie");
		}
		
		if(!locatii.isEmpty()) {
			for (String key : locatii.keySet()) {
				System.out.println(locatii.get(key));
			}
		}
		
		
	}
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		int var;
		String command;
		boolean exit = false;
		
		readFile("test1");
		while(!exit) {
			System.out.println("Introduceti comanda:");
			System.out.println("\t1. Informatii despre o locatie.");
			System.out.println("\t2. Top 5 locatii din tara/judetul/orasul X, incepand cu data zz/mm/yy, timp de Y zile.");
			System.out.println("\t3. Cea mai ieftina locatie pentru 10 zile.");
			System.out.println("\t4. Exit.");
			
			
			var = keyboard.nextInt();
			
			switch(var) {
			case 1:
				System.out.println("Introduceti numele locatiei:");
				keyboard.nextLine();
				command = keyboard.nextLine();//.trim();
				System.out.println(locatii.get(command) + "\n");
				break;
			case 2:
				System.out.println("TODO:");
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
