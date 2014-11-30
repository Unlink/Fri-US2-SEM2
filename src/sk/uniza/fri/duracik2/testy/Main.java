/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.testy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import sk.uniza.fri.duracik2.blockfile.BinarnySubor;
import sk.uniza.fri.duracik2.blockfile.IZaznam;
import sk.uniza.fri.duracik2.vodicaky.entity.Automobil;

/**
 *
 * @author Unlink
 */
public class Main {
	public static void main(String[] args) {
		try(
			BinarnySubor<Automobil> test = new BinarnySubor<>(new Automobil(), 4, new File("testSubor.bin"))
		) {
			HashMap<String, Long> testData = new HashMap<>();
			HashMap<String, Long> testData2 = new HashMap<>();
			HashSet<String> unikatnySet = new HashSet<>();
			for (int i = 0; i < 20000; i++) {
				String kluc = randomString(7).toUpperCase();
				while (unikatnySet.contains(kluc))
					kluc = randomString(7).toUpperCase();
				unikatnySet.add(kluc);
				Automobil record = test.dajVolnyZaznam();
				record.setEvcVozidla(kluc);
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
				unikatnySet.remove(entrySet.getKey());
				if (x++ > 10000) {
					break;
				}
			}
			
			for (int i = 0; i < 20000; i++) {
				String kluc = randomString(7).toUpperCase();
				while (unikatnySet.contains(kluc))
					kluc = randomString(7).toUpperCase();
				unikatnySet.add(kluc);
				Automobil record = test.dajVolnyZaznam();
				record.setEvcVozidla(kluc);
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
			
			System.out.println(test.toString());		
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static int EVCX = 1;
	
	public static String randomString(int len) {
		String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
		Random rg = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(rg.nextInt(AB.length())));
		}
		return sb.toString();
	}
}
