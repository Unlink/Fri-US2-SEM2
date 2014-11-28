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
	private int aPocetPlatnychKlucov;
	
	public Uzol(Uzol paUzol) {
		aPocetKlucov = paUzol.aPocetKlucov;
		aVelkostKluca = paUzol.aVelkostKluca;
		aJeList = paUzol.aJeList;
		aSample = paUzol.aSample;
		aPocetPlatnychKlucov = paUzol.aPocetPlatnychKlucov;
		for (int i = 0; i < aPocetKlucov; i++) {
			aKluce[i] = paUzol.aKluce[i].naklonuj();
		}
		aAdresy = Arrays.copyOf(paUzol.aAdresy, aPocetKlucov+1);
	}

	public Uzol(Kluc paSample, int paPocetKlucov, boolean jeList) {
		aSample = paSample;
		aJeList = false;
		aPocetKlucov = paPocetKlucov;
		aPocetPlatnychKlucov = 0;
		aVelkostKluca = paSample.dajVelkost();
		aKluce = new Kluc[paPocetKlucov];
		for (int i = 0; i < aPocetKlucov; i++) {
			aKluce[i] = aSample.naklonuj();
		}
		aAdresy = new long[paPocetKlucov+1];
	}

	@Override
	public int dajVelkost() {
		//jeden boolean pocet klucov + kluc + adresa + 1 adresa navyše
		return 1+4+aVelkostKluca*aPocetKlucov + 8*(aPocetKlucov+1);
	}

	@Override
	public void nahraj(DataInputStream paStream) {
		try {
			aJeList = paStream.readBoolean();
			aPocetPlatnychKlucov = paStream.readInt();
			byte[] buffer = new byte[aVelkostKluca];
			for (Kluc kluc : aKluce) {
				paStream.read(buffer);
				kluc.nahraj(buffer);
			}
			for (int i = 0; i < aPocetKlucov+1; i++) {
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
			stream.writeInt(aPocetPlatnychKlucov);
			byte[] buffer = new byte[aVelkostKluca];
			for (Kluc kluc : aKluce) {
				kluc.serializuj(buffer);
				stream.write(buffer);
			}
			for (int i = 0; i < aPocetKlucov+1; i++) {
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
	
	public boolean jeList() {
		return aJeList;
	}

	public long dalsiaAdresa(Kluc kluc) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (kluc.compareTo(aKluce[i]) <= 0)
				return aAdresy[i];
		}
		return aAdresy[aPocetKlucov];
	}

	public long dajAdresuKluca(Kluc paKluc) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (paKluc.compareTo(aKluce[i]) == 0)
				return aAdresy[i];
		}
		return -1;
	}

	public boolean maMiesto() {
		return aPocetPlatnychKlucov < aPocetKlucov;
	}
	
	public void vloz(BStromZaznam zaznam) {
		int x = jeList() ? 0 : 1;
			for (int i=aPocetPlatnychKlucov; i>0; i--) {
				if (zaznam.getKluc().compareTo(aKluce[i-1]) < 0) {
					aKluce[i] = aKluce[i-1];
					aAdresy[i+x] = aAdresy[i-1+x];
				}
				else {
					aKluce[i] = zaznam.getKluc();
					aAdresy[i+x] = zaznam.getAdresa();
				}
			}
	}

	public void rozdelList(BStromZaznam paZaznam, Uzol novy) {		
		
		
	}
	 
}
