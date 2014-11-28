/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.io.File;
import sk.uniza.fri.duracik2.bstrom.BStrom;
import sk.uniza.fri.duracik2.bstrom.BStromZaznam;
import sk.uniza.fri.duracik2.bstrom.IntovyKluc;

/**
 *
 * @author Unlink
 */
public class MainBStrom {
	public static void main(String[] args) {
		//try (BStrom strom = new BStrom(new File("dakyStrom.bin"), new StringovyKluc(((char) 0xFF)+"", 8), 3))
		try (BStrom strom = new BStrom(new File("dakyStrom.bin"), new IntovyKluc(0), 3))
		{			
			/*int numbers = 100000;
			
			List<Integer> ints = new ArrayList<>(numbers);
			for (int i = 0; i < numbers; i++) {
				ints.add(i);
			}
			Collections.shuffle(ints);
			
			
			for(int i:ints) {
				strom.vloz(new BStromZaznam(new StringovyKluc("a"+i+"b", 8), i));
				if (strom.najdi(new StringovyKluc("a"+i+"b", 8)) == -1) {
					System.err.println("err");
				}
			}
			
			for(int i:ints) {
				long x = strom.najdi(new StringovyKluc("a"+i+"b", 8));
				if (x != i) {
					System.err.println("err");
				}
			}*/
			
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
			
			strom.vloz(new BStromZaznam(new IntovyKluc(8), 8));
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
			
			strom.inOrderVypis();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
