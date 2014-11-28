/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import sk.uniza.fri.duracik2.blockfile.AZaznam;
import sk.uniza.fri.duracik2.blockfile.IZaznam;

/**
 *
 * @author Unlink
 */
public class Uzol extends AZaznam {
	private int aPocetKlucov;
	private int aVelkostKluca;
	private ArrayList<BStromZaznam> aKluce;
	private long aAddr;
	private boolean aJeList;
	private Kluc aSample;
	private int aPocetPlatnychKlucov;
	
	public Uzol(Uzol paUzol) {
		nastavValiditu(true);
		aPocetKlucov = paUzol.aPocetKlucov;
		aVelkostKluca = paUzol.aVelkostKluca;
		aJeList = paUzol.aJeList;
		aSample = paUzol.aSample;
		aPocetPlatnychKlucov = paUzol.aPocetPlatnychKlucov;
		aAddr = paUzol.aAddr;
		aKluce = new ArrayList<>();
		for (BStromZaznam zaznam : paUzol.aKluce) {
			aKluce.add(new BStromZaznam(zaznam.getKluc().naklonuj(), zaznam.getAdresa()));
		}
	}

	public Uzol(Kluc paSample, int paPocetKlucov, boolean jeList) {
		nastavValiditu(true);
		aSample = paSample;
		aJeList = false;
		aPocetKlucov = paPocetKlucov;
		aPocetPlatnychKlucov = 0;
		aVelkostKluca = paSample.dajVelkost();
		aKluce = new ArrayList<>(aPocetKlucov);
		for (int i = 0; i < aPocetKlucov; i++) {
			aKluce.add(new BStromZaznam(aSample.naklonuj(), -1));
		}
		aAddr = -1;
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
			for (int i = 0; i < aPocetKlucov; i++) {
				paStream.read(buffer);
				long addr = paStream.readLong();
				if (i<aPocetPlatnychKlucov) {
					aKluce.set(i, new BStromZaznam(aSample.naklonuj(), -1));
					aKluce.get(i).getKluc().nahraj(buffer);
					aKluce.get(i).setAdresa(addr);
				}
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
			for (BStromZaznam k : aKluce) {
				k.getKluc().serializuj(buffer);
				stream.write(buffer);
				stream.writeLong(k.getAdresa());
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
			if (kluc.compareTo(aKluce.get(i).getKluc()) <= 0) {
				if (i == 0) {
					return aAddr;
				}
				else {
					return aKluce.get(i-1).getAdresa();
				}
			}
		}
		return aKluce.get(aPocetPlatnychKlucov-1).getAdresa();
	}

	public long dajAdresuKluca(Kluc paKluc) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (paKluc.compareTo(aKluce.get(i).getKluc()) == 0)
				return aKluce.get(i).getAdresa();
		}
		return -1;
	}

	public boolean maMiesto() {
		return aPocetPlatnychKlucov < aPocetKlucov;
	}
	
	public void vloz(BStromZaznam zaznam) {
		boolean pridany = false;
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (zaznam.getKluc().compareTo(aKluce.get(i).getKluc()) < 0) {
				aKluce.add(i, zaznam);
				aKluce.remove(aPocetKlucov);
				aPocetPlatnychKlucov++;
				pridany = true;
				break;
			}	
		}
		if (!pridany) {
			aKluce.set(aPocetPlatnychKlucov, zaznam);
			aPocetPlatnychKlucov++;
		}
		
	}

	public Kluc rozdelList(BStromZaznam paZaznam, Uzol novy) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (paZaznam.getKluc().compareTo(aKluce.get(i).getKluc()) < 0) {
				aKluce.add(i, paZaznam);
				break;
			}
		}
		if (aKluce.size() == aPocetKlucov) {
			aKluce.add(paZaznam);
		}
		
		aPocetPlatnychKlucov = (aPocetKlucov/2)+1;
		for (int i=aPocetPlatnychKlucov; i<=aPocetKlucov; i++) {
			novy.vloz(aKluce.get(i));
		}
		//Smerník na ďalší uzol v zotriedení
		novy.aAddr = aAddr;
		//Zmazeme ten docasný
		aKluce.remove(aPocetKlucov);
		return aKluce.get(aPocetPlatnychKlucov-1).getKluc();
	}

	public long getAddr() {
		return aAddr;
	}

	public void setAddr(long aAddr) {
		this.aAddr = aAddr;
	}

	public void setJeList(boolean aJeList) {
		this.aJeList = aJeList;
	}
	
	

	public Kluc rozdelVnutornyUzol(BStromZaznam paZaznam, Uzol novy) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (paZaznam.getKluc().compareTo(aKluce.get(i).getKluc()) < 0) {
				aKluce.add(i, paZaznam);
				break;
			}
		}
		
		aPocetPlatnychKlucov = (aPocetKlucov/2);
		for (int i=(aPocetKlucov/2)+1; i<=aPocetKlucov; i++) {
			novy.vloz(aKluce.get(i));
		}
		//Smerník na ďalší uzol v zotriedení
		novy.aAddr = aKluce.get(aPocetPlatnychKlucov).getAdresa();
		//Zmazeme ten docasný
		aKluce.remove(aPocetKlucov);
		return aKluce.get(aPocetPlatnychKlucov).getKluc();
	}

	@Override
	public void nastavValiditu(boolean paValidita) {
		super.nastavValiditu(paValidita);
		if (!paValidita) {
			aPocetPlatnychKlucov = 0;
		}
	}
	
	
	
}
