/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import java.util.Arrays;

/**
 *
 * @author Unlink
 */
public class StringovyKluc implements Kluc {

	private String aString;
	private final int aMaxVelksot;

	public StringovyKluc(String aString, int aMaxVelksot) {
		this.aString = aString;
		this.aMaxVelksot = aMaxVelksot;
	}
	
	@Override
	public void nahraj(byte[] paStream) {
		aString = new String(paStream).trim();
	}

	@Override
	public void serializuj(byte[] stream) {
		Arrays.fill(stream, (byte)0);
		byte [] temp = aString.getBytes();
		System.arraycopy(temp, 0, stream, 0, Math.min(stream.length, temp.length));
	}

	@Override
	public Kluc naklonuj() {
		return new StringovyKluc(aString, aMaxVelksot);
	}

	@Override
	public int dajVelkost() {
		return aMaxVelksot*2;
	}

	@Override
	public int compareTo(Kluc o) {
		return aString.compareTo(((StringovyKluc)o).aString);
	}

	@Override
	public String toString() {
		return aString;
	}
	
	
	
}
