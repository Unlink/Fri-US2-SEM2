/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sk.uniza.fri.duracik2.bstrom.BStrom;
import sk.uniza.fri.duracik2.bstrom.BStromZaznam;
import sk.uniza.fri.duracik2.bstrom.IntovyKluc;

/**
 *
 * @author Unlink
 */
public class MainBTreeReferencny {
	public static void vloz(BStrom s, int k) throws IOException {
		s.vloz(new BStromZaznam(new IntovyKluc(k), k));
	}

	public static void zmaz(BStrom s, int k) throws IOException {
		s.vymaz(new IntovyKluc(k));
	}

	public static void main(String[] args) {
		new File("dakyStrom.bin").delete();
		new File("dakyStrom.bin.bitmap").delete();

		try (BStrom strom = new BStrom(new File("dakyStrom.bin"), new IntovyKluc(0), 3)) {
			int numbers = 1000;

			List<Integer> ints = new ArrayList<>(numbers);
			for (int i = 0; i < numbers; i++) {
				ints.add(i);
			}
			Collections.shuffle(ints);

			for (int i : ints) {
				strom.vloz(new BStromZaznam(new IntovyKluc(i), i));
				if (strom.najdi(new IntovyKluc(i)) == -1) {
					System.err.println("err");
				}
				//System.out.println("vloz(strom, " + i + ");");
			}

			 //strom.inOrderVypis();
			for (int i : ints) {
				long x = strom.najdi(new IntovyKluc(i));
				if (x != i) {
					System.err.println("err");
				}
			}

			//strom.inOrderVypis();
			Collections.shuffle(ints);

			for (int i : ints) {
				//System.out.println("zmaz(strom, " + i + ");");
				long x = strom.vymaz(new IntovyKluc(i));
				if (x != i) {
					System.err.println("err");
				}
			}

			/*strom.vloz(new BStromZaznam(new StringovyKluc("08", 7), 8));
			 System.out.println(strom.najdi(new StringovyKluc("08", 7)));
			
			 strom.vloz(new BStromZaznam(new StringovyKluc("05", 7), 5));
			 System.out.println(strom.najdi(new StringovyKluc("05", 7)));
			
			 strom.vloz(new BStromZaznam(new StringovyKluc("01", 7), 1));
			 System.out.println(strom.najdi(new StringovyKluc("01", 7)));
			
			 strom.vloz(new BStromZaznam(new StringovyKluc("07", 7), 7));
			 System.out.println(strom.najdi(new StringovyKluc("07", 7)));
			
			 strom.vloz(new BStromZaznam(new StringovyKluc("03", 7), 3));
			 System.out.println(strom.najdi(new StringovyKluc("03", 7)));
			
			 strom.vloz(new BStromZaznam(new StringovyKluc("12", 7), 12));
			 System.out.println(strom.najdi(new StringovyKluc("12", 7)));
			
			 strom.vloz(new BStromZaznam(new StringovyKluc("09", 7), 9));
			 System.out.println(strom.najdi(new StringovyKluc("09", 7)));
			
			 strom.vloz(new BStromZaznam(new StringovyKluc("06", 7), 6));
			 System.out.println(strom.najdi(new StringovyKluc("06", 7)));*/

			/*strom.vloz(new BStromZaznam(new IntovyKluc(8), 8));
			 System.out.println(strom.najdi(new IntovyKluc(8)));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(5), 5));
			 System.out.println(strom.najdi(new IntovyKluc(5)));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(1), 1));
			 System.out.println(strom.najdi(new IntovyKluc(1)));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(7), 7));
			 System.out.println(strom.najdi(new IntovyKluc(7)));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(3), 3));
			 System.out.println(strom.najdi(new IntovyKluc(3)));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(12), 12));
			 System.out.println(strom.najdi(new IntovyKluc(12)));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(9), 9));
			 System.out.println(strom.najdi(new IntovyKluc(9)));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(6), 6));
			 System.out.println(strom.najdi(new IntovyKluc(6)));
			
			 strom.vymaz(new IntovyKluc(7));
			 strom.vymaz(new IntovyKluc(8));
			 strom.vymaz(new IntovyKluc(12));
			 strom.vymaz(new IntovyKluc(9));
			 strom.vymaz(new IntovyKluc(5));
			 strom.vymaz(new IntovyKluc(6));
			 strom.vymaz(new IntovyKluc(3));
			 strom.vymaz(new IntovyKluc(1));
			
			 strom.vloz(new BStromZaznam(new IntovyKluc(6), 6));
			 System.out.println(strom.najdi(new IntovyKluc(6)));
			 */
			strom.inOrderVypis();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
