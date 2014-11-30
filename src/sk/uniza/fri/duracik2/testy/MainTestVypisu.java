/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.testy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import sk.uniza.fri.duracik2.bstrom.BStrom;
import sk.uniza.fri.duracik2.bstrom.BStromZaznam;
import sk.uniza.fri.duracik2.bstrom.IntovyKluc;
import sk.uniza.fri.duracik2.bstrom.Kluc;

/**
 *
 * @author Unlink
 */
public class MainTestVypisu {
	public static Kluc dajKluc(int k) {
		return new IntovyKluc(k);
	}
	
	public static void vloz(BStrom s, int k) throws IOException {
		s.vloz(new BStromZaznam(dajKluc(k), k));
	}

	public static void zmaz(BStrom s, int k) throws IOException {
		if (s.vymaz(dajKluc(k)) < 0) {
			System.err.println("Kluc sa nepodarilo zmazat");
		}
	}
	
	public static void najdi(BStrom s, int k) throws IOException {
		if (s.najdi(dajKluc(k)) == -1) {
			System.err.println("Kluc nenajdeny");
		}
	}
	
	public static int generujCislo(Random rnd, HashSet<Integer> set) {
		int cislo = Math.abs(rnd.nextInt());
		while (set.contains(cislo))
			cislo = Math.abs(rnd.nextInt());
		set.add(cislo);
		return cislo;
	}

	public static void main(String[] args) {
		new File("dakyStrom2.bin").delete();
		new File("dakyStrom2.bin.bitmap").delete();

		try (BStrom strom = new BStrom(new File("dakyStrom2.bin"), dajKluc(0), 3)) {

			for (int i = 0; i < 11; i++) {
				vloz(strom, i);
			}
			
			strom.vypisStrom();

			strom.inOrderVypis();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
