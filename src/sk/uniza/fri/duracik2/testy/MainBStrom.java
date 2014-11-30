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
import sk.uniza.fri.duracik2.bstrom.StringovyKluc;

/**
 *
 * @author Unlink
 */
public class MainBStrom {

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
		new File("dakyStrom.bin").delete();
		new File("dakyStrom.bin.bitmap").delete();

		try (BStrom strom = new BStrom(new File("dakyStrom.bin"), dajKluc(0), 5)) {
		//try (BStrom strom = new BStrom(new File("dakyStrom.bin"), new IntovyKluc(0), 0)) {

			int numbers = 10000;
			HashSet<Integer> cisla = new HashSet<>();
			Random rnd = new Random();

			System.out.println("Vkladám "+numbers+" cisel");
			for (int i=0; i<numbers; i++) {
				int x = generujCislo(rnd, cisla);
				vloz(strom, x);
			}

			 //strom.inOrderVypis();
			System.out.println("Vyhladavam vsetko ");
			for (int i : cisla) {
				najdi(strom, i);
			}

			//strom.inOrderVypis();
			List<Integer> cisla2 = new ArrayList<>(cisla);
			Collections.shuffle(cisla2);
			System.out.println("Mazem "+(numbers/2)+" cisel");
			for (int i=0; i<numbers/2; i++) {
				zmaz(strom, cisla2.get(i));
				cisla.remove(cisla2.get(i));
			}
			
			System.out.println("Hladam vsetko");
			for (int i : cisla) {
				najdi(strom, i);
			}
			
			System.out.println("Vkladám "+numbers+" cisel");
			for (int i=0; i<numbers; i++) {
				int x = generujCislo(rnd, cisla);
				vloz(strom, x);
			}
			
			System.out.println("Hladam vsetko");
			for (int i : cisla) {
				najdi(strom, i);
			}
			
			System.out.println("Mazem vsetko");
			for (int i : cisla) {
				zmaz(strom, i);
			}

			strom.inOrderVypis();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
