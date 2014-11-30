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
public class BlokoveBitovePole {
	private BitovePole aBitPole;
	private int aVelkostBloku;

	public BlokoveBitovePole(int paVelkostPola, int paVelkostBloku) {
		aBitPole = new BitovePole(paVelkostPola * 8);
		aVelkostBloku = paVelkostBloku;
	}

	public int dajPocetBlokov() {
		return aBitPole.dajVelkost() / aVelkostBloku;
	}

	public byte[] dajBlok(int paCislo) {
		return Arrays.copyOfRange(aBitPole.dajData(), paCislo * aVelkostBloku, paCislo * aVelkostBloku + aVelkostBloku);
	}

	public void nahrajBlok(int paCislo, byte[] paData) {
		System.arraycopy(paData, 0, aBitPole.dajData(), paCislo * aVelkostBloku, aVelkostBloku);
	}

	public void rozsirBlok() {
		aBitPole.zmenVelkost(aBitPole.dajVelkost() * 8 + aVelkostBloku * 8);
	}

	public void zmensiBlok(int paMnozstvo) {
		aBitPole.zmenVelkost(aBitPole.dajVelkost() * 8 - aVelkostBloku * 8 * paMnozstvo);
	}

	public boolean dajFlag(int paIndex) {
		return aBitPole.dajFlag(paIndex);
	}

	public void nastavFlag(int paIndex, boolean paFlag) {
		aBitPole.nastavFlag(paIndex, paFlag);
	}

	public int dajVelkost() {
		return aBitPole.dajVelkost();
	}

	public int dajPocetZaznamov() {
		return dajVelkost() * 4;
	}

	public int dajPocetZaznamovVBloku() {
		return aVelkostBloku * 4;
	}
}
