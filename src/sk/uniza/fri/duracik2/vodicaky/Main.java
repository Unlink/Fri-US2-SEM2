/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sk.uniza.fri.duracik2.blockfile.BinarnySubor;
import sk.uniza.fri.duracik2.blockfile.IZaznam;
import sk.uniza.fri.duracik2.vodicaky.entity.Automobil;

/**
 *
 * @author Unlink
 */
public class Main {
	public static void main(String[] args) {
		List<IZaznam> strukturaBloku = new ArrayList<>();
		strukturaBloku.add(new Automobil());
		strukturaBloku.add(new Automobil());
		strukturaBloku.add(new Automobil());
		strukturaBloku.add(new Automobil());
		try(
			BinarnySubor<Automobil> test = new BinarnySubor<>(strukturaBloku, new File("testSubor.bin"))
		) {
			/*HashMap<String, Long> testData = new HashMap<>();
			HashMap<String, Long> testData2 = new HashMap<>();
			for (int i = 0; i < 100000; i++) {
				Automobil record = test.dajVolnyZaznam();
				record.setEvcVozidla(randomString(7).toUpperCase());
				record.nastavValiditu(true);
				test.ulozBlok();
				testData.put(record.getEvcVozidla(), record.dajAdresu());
				testData2.put(record.getEvcVozidla(), record.dajAdresu());
				//System.out.println(i+": "+record.dajAdresu()+" -> "+record.getEvcVozidla());
			}
			
			for (Map.Entry<String, Long> entrySet : testData.entrySet()) {
				//System.out.println("Citam "+entrySet.getValue());
				Automobil record = test.dajZaznam(entrySet.getValue());
				//System.out.println(record.getEvcVozidla()+" = "+entrySet.getKey());
				if (!record.getEvcVozidla().equals(entrySet.getKey())) {
					System.err.println("ERRORRRRR");
					break;
				}
			}
			
			int x = 0;
			for (Map.Entry<String, Long> entrySet : testData.entrySet()) {
				//System.out.println("Mazem "+entrySet.getValue());
				Automobil record = test.dajZaznam(entrySet.getValue());
				record.nastavValiditu(false);
				test.ulozBlok();
				testData2.remove(entrySet.getKey());
				if (x++ > 60000) {
					break;
				}
			}
			
			for (int i = 0; i < 60000; i++) {
				Automobil record = test.dajVolnyZaznam();
				record.setEvcVozidla(randomString(7).toUpperCase());
				record.nastavValiditu(true);
				test.ulozBlok();
				testData2.put(record.getEvcVozidla(), record.dajAdresu());
				//System.out.println(i+": "+record.dajAdresu()+" -> "+record.getEvcVozidla());
			}
			
			for (Map.Entry<String, Long> entrySet : testData2.entrySet()) {
				//System.out.println("Citam "+entrySet.getValue());
				Automobil record = test.dajZaznam(entrySet.getValue());
				//System.out.println(record.getEvcVozidla()+" = "+entrySet.getKey());
				if (!record.getEvcVozidla().equals(entrySet.getKey())) {
					System.err.println("ERRORRRRR");
					break;
				}
			}
			
			for (Map.Entry<String, Long> entrySet : testData2.entrySet()) {
				//System.out.println("Mazem "+entrySet.getValue());
				Automobil record = test.dajZaznam(entrySet.getValue());
				record.nastavValiditu(false);
				test.ulozBlok();
			}
			
			
 			Automobil record = test.dajVolnyZaznam();
			record.setEvcVozidla(randomString(7).toUpperCase());
			record.nastavValiditu(true);
			test.ulozBlok();
			/*long auto = record.dajAdresu();
			record.nastavValiditu(false);
			test.ulozBlok();*/
			//System.out.println("Adresa noveho recordu "+record.dajAdresu());
			
			//System.out.println(test.toString());
			
			/*
			record = test.dajVolnyZaznam();
			record.setEvcVozidla(randomString(7).toUpperCase());
			record.nastavValiditu(true);
			test.ulozBlok();
			long auto2 = record.dajAdresu();
			
			record = test.dajVolnyZaznam();
			record.setEvcVozidla(randomString(7).toUpperCase());
			record.nastavValiditu(true);
			test.ulozBlok();
			long auto3 = record.dajAdresu();
			
			record = test.dajVolnyZaznam();
			record.setEvcVozidla(randomString(7).toUpperCase());
			record.nastavValiditu(true);
			test.ulozBlok();
			long auto4 = record.dajAdresu();
			
			record = test.dajZaznam(auto3);
			record.nastavValiditu(false);
			test.ulozBlok();
			
			record = test.dajZaznam(auto4);
			record.nastavValiditu(false);
			test.ulozBlok();*/
			
			
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static int EVCX = 1;
	
	public static String randomString(int len) {
		/*String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
		Random rg = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(rg.nextInt(AB.length())));
		}
		return sb.toString();*/
		return ""+(EVCX++);
	}
}
