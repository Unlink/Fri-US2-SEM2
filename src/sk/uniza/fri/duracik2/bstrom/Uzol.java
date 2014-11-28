/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import sk.uniza.fri.duracik2.blockfile.AZaznam;
import sk.uniza.fri.duracik2.blockfile.IZaznam;

/**
 *
 * @author Unlink
 */
public class Uzol extends AZaznam {
	private int aPocetKlucov;
	private int aVelkostKluca;
	private Kluc[] aKluce;
	private long[] aAdresy;
	private boolean aJeList;
	private Kluc aSample;
	
	public Uzol(Uzol paUzol) {
		aPocetKlucov = paUzol.aPocetKlucov;
		aVelkostKluca = paUzol.aVelkostKluca;
		aJeList = paUzol.aJeList;
		aSample = paUzol.aSample;
		for (int i = 0; i < aPocetKlucov; i++) {
			aKluce[i] = paUzol.aKluce[i].naklonuj();
		}
		aAdresy = Arrays.copyOf(paUzol.aAdresy, aPocetKlucov+2);
	}

	public Uzol(Kluc paSample, int paPocetKlucov, boolean jeList) {
		aSample = paSample;
		aJeList = false;
		aPocetKlucov = paPocetKlucov;
		aVelkostKluca = paSample.dajVelkost();
		aKluce = new Kluc[paPocetKlucov];
		for (int i = 0; i < aPocetKlucov; i++) {
			aKluce[i] = aSample.naklonuj();
		}
		aAdresy = new long[paPocetKlucov+2];
	}

	@Override
	public int dajVelkost() {
		//jeden boolean + kluc + adresa + 2 adresy navyše
		return 1+aVelkostKluca*aPocetKlucov + 8*(aPocetKlucov+2);
	}

	@Override
	public void nahraj(DataInputStream paStream) {
		try {
			aJeList = paStream.readBoolean();
			byte[] buffer = new byte[aVelkostKluca];
			for (Kluc kluc : aKluce) {
				paStream.read(buffer);
				kluc.nahraj(buffer);
			}
			for (int i = 0; i < aPocetKlucov+2; i++) {
				aAdresy[i] = paStream.readLong();
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void serializuj(DataOutputStream stream) {
		try {
			stream.writeBoolean(aJeList);
			byte[] buffer = new byte[aVelkostKluca];
			for (Kluc kluc : aKluce) {
				kluc.serializuj(buffer);
				stream.write(buffer);
			}
			for (int i = 0; i < aPocetKlucov+2; i++) {
				stream.writeLong(aAdresy[i]);
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public IZaznam naklonuj() {
		return new Uzol(this);
	}
	
	
}
