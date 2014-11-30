/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import java.nio.ByteBuffer;

/**
 *
 * @author Unlink
 */
public class IntovyKluc implements Kluc {

	private int aInt;

	public IntovyKluc(int aInt) {
		this.aInt = aInt;
	}

	@Override
	public void nahraj(byte[] paStream) {
		aInt = ByteBuffer.wrap(paStream).getInt();
	}

	@Override
	public void serializuj(byte[] stream) {
		byte[] array = ByteBuffer.allocate(stream.length).putInt(aInt).array();
		System.arraycopy(array, 0, stream, 0, stream.length);
	}

	@Override
	public Kluc naklonuj() {
		return new IntovyKluc(aInt);
	}

	@Override
	public int dajVelkost() {
		return 4;
	}

	@Override
	public int compareTo(Kluc o) {
		return Integer.compare(aInt, ((IntovyKluc) o).aInt);
	}

	@Override
	public String toString() {
		return aInt + "";
	}

}
