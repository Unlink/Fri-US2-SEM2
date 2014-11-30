/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.testy;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.uniza.fri.duracik2.bstrom.StringovyKluc;
import sk.uniza.fri.duracik2.fullIndex.UplnyIndex;
import sk.uniza.fri.duracik2.vodicaky.entity.Automobil;

/**
 *
 * @author Unlink
 */
public class MainUplnyIndexTest {
	public static void main(String[] args) {
		long teraz = System.currentTimeMillis();
		try (UplnyIndex<Automobil> index = new UplnyIndex<>(new Automobil(), 5, new int[]{5, 5}, new File("data"))) {
			Automobil auto1 = new Automobil("ZA123AA", "123456789", 2, 1240, false, new Date(teraz), new Date(teraz));
			Automobil auto2 = new Automobil("ZA321AA", "987654321", 3, 2500, false, new Date(teraz), new Date(teraz));
			Automobil auto3 = new Automobil("BA222CC", "111111111", 2, 980, false, new Date(teraz), new Date(teraz));
			
			index.vlozPrvok(auto1);
			index.vlozPrvok(auto2);
			index.vlozPrvok(auto3);
			
			Automobil auto01 = index.vyhladaj(0, new StringovyKluc("ZA123AA", 7));
			if (!auto01.equals(auto1)) {
				System.out.println("err auta nesuhlasia 1");
			}
			
			Automobil auto02 = index.vyhladaj(1, new StringovyKluc("987654321", 17));
			if (!auto02.equals(auto2)) {
				System.out.println("err auta nesuhlasia 2");
			}
			
			Automobil auto03 = index.vymaz(0, new StringovyKluc("BA222CC", 7));
			if (!auto03.equals(auto3)) {
				System.out.println("err auta nesuhlasia 3");
			}
			index.vymaz(0, new StringovyKluc("ZA321AA", 7));
			index.vymaz(0, new StringovyKluc("ZA123AA", 7));
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
