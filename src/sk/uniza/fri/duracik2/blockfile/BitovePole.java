/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.blockfile;

import java.util.Arrays;

/**
 *
 * @author Unlink
 */
public class BitovePole {

	private byte[] aData;
	private int aVelkost;

	public BitovePole(int paVelkost) {
		aVelkost = paVelkost;
		aData = new byte[dajVelkost()];
	}

	public int dajVelkost() {
		return ((aVelkost-1) / 8) + 1;
	}

	public boolean dajFlag(int paIndex) {
		int blok = paIndex / 8;
		int offest = paIndex % 8;

		return ((aData[blok] & (0x1 << offest)) != 0);
	}

	public void nastavFlag(int paIndex, boolean flag) {
		int blok = paIndex / 8;
		int offest = paIndex % 8;
		if (flag) {
			aData[blok] |= 0x1 << offest;
		}
		else {
			aData[blok] &= ~(0x1 << offest);
		}
	}

	public void nastavData(byte[] paData) {
		aData = paData;
	}

	public byte[] dajData() {
		return aData;
	}

	public void zmenVelkost(int paVelkost) {
		aVelkost = paVelkost;
		aData = Arrays.copyOf(aData, dajVelkost());
	}
	
}
