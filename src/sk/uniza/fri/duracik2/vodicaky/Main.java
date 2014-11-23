/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.uniza.fri.duracik2.blockfile.AZaznam;
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
		try(
			BinarnySubor<Automobil> test = new BinarnySubor<>(strukturaBloku, new File("testSubor.bin"))
		) {
			HashMap<String, Long> testData = new HashMap<>();
			for (int i = 0; i < 10000; i++) {
				/*if (i == 5724) {
					int c = 5;
				}*/
				Automobil record = test.dajVolnyZaznam();
				record.setEvcVozidla(randomString(7).toUpperCase());
				record.nastavValiditu(true);
				test.ulozBlok();
				testData.put(record.getEvcVozidla(), record.dajAdresu());
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
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
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
