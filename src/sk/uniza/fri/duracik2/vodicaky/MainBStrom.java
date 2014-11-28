/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.io.File;
import sk.uniza.fri.duracik2.bstrom.BStrom;
import sk.uniza.fri.duracik2.bstrom.BStromZaznam;
import sk.uniza.fri.duracik2.bstrom.StringovyKluc;

/**
 *
 * @author Unlink
 */
public class MainBStrom {
	public static void main(String[] args) {
		try (BStrom strom = new BStrom(new File("dakyStrom.bin"), new StringovyKluc(((char) 0xFF)+"", 7), 3))
		{
			for (int i=0; i<100; i++) {
				strom.vloz(new BStromZaznam(new StringovyKluc("_"+i+"_", 3), i));
			}
			
			for (int i=0; i<100; i++) {
				System.out.println(strom.najdi(new StringovyKluc("_"+i+"_", 3)));
			}
			
			/*strom.vloz(new BStromZaznam(new StringovyKluc("8", 7), 4));
			System.out.println(strom.najdi(new StringovyKluc("8", 7)));
			
			strom.vloz(new BStromZaznam(new StringovyKluc("5", 7), 5));
			System.out.println(strom.najdi(new StringovyKluc("5", 7)));
			
			strom.vloz(new BStromZaznam(new StringovyKluc("1", 7), 5));
			System.out.println(strom.najdi(new StringovyKluc("1", 7)));
			
			strom.vloz(new BStromZaznam(new StringovyKluc("7", 7), 5));
			System.out.println(strom.najdi(new StringovyKluc("7", 7)));
			
			strom.vloz(new BStromZaznam(new StringovyKluc("3", 7), 5));
			System.out.println(strom.najdi(new StringovyKluc("3", 7)));*/
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
