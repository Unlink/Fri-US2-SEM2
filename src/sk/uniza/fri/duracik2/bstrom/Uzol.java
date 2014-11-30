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
import sk.uniza.fri.duracik2.blockfile.BinarnySubor;
import sk.uniza.fri.duracik2.blockfile.IZaznam;

/**
 *
 * @author Unlink
 */
public class Uzol extends AZaznam {
	public static final int KLUC_NENAJDENY = -465;
	
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
			aAddr = paStream.readLong();
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
			stream.writeLong(aAddr);
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
		return KLUC_NENAJDENY;
	}

	public boolean maMiesto() {
		return aPocetPlatnychKlucov < aPocetKlucov;
	}
	
	public boolean maDostatokPrvkov() {
		return aPocetPlatnychKlucov > (aPocetKlucov/2);
	}
	
	/**
	 * 
	 * @param paKluc
	 * @return predchádzajúci záznam, ak sme zmazali posledný
	 */
	public BStromZaznam odoberKlucZListu(Kluc paKluc) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (paKluc.compareTo(aKluce.get(i).getKluc()) == 0) {
				aKluce.remove(i);
				aKluce.add(new BStromZaznam(paKluc.naklonuj(), -1));
				//Zmazal som posledný
				if ((aPocetPlatnychKlucov-1) == i && i > 0) {
					aPocetPlatnychKlucov--;
					return aKluce.get(i-1);
				}
				else {
					aPocetPlatnychKlucov--;
					return null;
				}
			}
		}
		return null;
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
		zaradZotriedene(paZaznam);
		
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

	public int getPocetPlatnychKlucov() {
		return aPocetPlatnychKlucov;
	}
	

	public Kluc rozdelVnutornyUzol(BStromZaznam paZaznam, Uzol novy) {
		zaradZotriedene(paZaznam);
		
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

	private void zaradZotriedene(BStromZaznam paZaznam) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (paZaznam.getKluc().compareTo(aKluce.get(i).getKluc()) < 0) {
				aKluce.add(i, paZaznam);
				break;
			}
		}
		if (aKluce.size() == aPocetKlucov) {
			aKluce.add(paZaznam);
		}
	}

	@Override
	public void nastavValiditu(boolean paValidita) {
		super.nastavValiditu(paValidita);
		if (!paValidita) {
			aPocetPlatnychKlucov = 0;
			aAddr = -1;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("List: ").append(aJeList).append("\n");
		sb.append("Speciala adresa: ").append(aAddr).append("\n");;
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			sb.append(aKluce.get(i)).append("\n");
		}
		return sb.toString();
	}

	public boolean nahradKluc(Kluc kluc, Kluc nahrada) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (aKluce.get(i).getKluc().compareTo(kluc) == 0) {
				aKluce.get(i).setKluc(nahrada.naklonuj());
				return true;
			}
		}
		return false;
	}
	
	public boolean nahradKluc(long addr, Kluc nahrada) {
		if (addr == aAddr) {
			aKluce.get(0).setKluc(nahrada);
			return true;
		} 
		for (int i = 0; i < aPocetPlatnychKlucov-1; i++) {
			if (aKluce.get(i).getAdresa() == addr) {
				aKluce.get(i+1).setKluc(nahrada.naklonuj());
				return true;
			}
		}
		return false;
	}
	
	public Kluc dajMaximalnyKluc() {
		return aKluce.get(aPocetPlatnychKlucov-1).getKluc().naklonuj();
	}

	public BStromZaznam zoberMaximalnyKluc() {
		if (maDostatokPrvkov()) {
			BStromZaznam zaznam = aKluce.get(aPocetPlatnychKlucov-1);
			aPocetPlatnychKlucov--;
			return zaznam;
		}
		return null;
	}
	
	public void zaradAkoMinimalny(BStromZaznam zaznam) {
		if (!jeList()) {
			long prvaAddr = aAddr;
			aAddr = zaznam.getAdresa();
			zaznam.setAdresa(prvaAddr);
		}
		aKluce.add(0, zaznam);
		aKluce.remove(aPocetKlucov);
		aPocetPlatnychKlucov++;
	}

	public long dajLavehoBrata(long adresa) {
		if (adresa == aAddr) {
			return -1;
		}
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (aKluce.get(i).getAdresa() == adresa) {
				if (i == 0)
					return aAddr;
				else {
					return aKluce.get(i-1).getAdresa();
				}
			}
		}
		return -1;
	}
	
	public long dajPravehoBrata(long adresa) {
		if (adresa == aAddr && aPocetPlatnychKlucov > 0) {
			return aKluce.get(0).getAdresa();
		}
		for (int i = 0; i < aPocetPlatnychKlucov-1; i++) {
			if (aKluce.get(i).getAdresa() == adresa) {
				return aKluce.get(i+1).getAdresa();
			}
		}
		return -1;
	}

	public BStromZaznam zoberMinimalnyKluc() {
		if (maDostatokPrvkov()) {
			aPocetPlatnychKlucov--;
			BStromZaznam zaznam;
			zaznam = aKluce.get(0);
			aKluce.remove(0);
			aKluce.add(new BStromZaznam(zaznam.getKluc().naklonuj(), -1));
			if (!jeList()) {
				long adresa = aAddr;
				aAddr = zaznam.getAdresa();
				zaznam.setAdresa(adresa);
			}
			return zaznam;
		}
		return null;
	}

	public void zaradAkoMaximalny(BStromZaznam paZaznam) {
		aKluce.set(aPocetPlatnychKlucov, paZaznam);
		aPocetPlatnychKlucov++;
	}

	public void spojBloky(Uzol paUzol) {
		for (int i=0; i<paUzol.aPocetPlatnychKlucov; i++) {
			aKluce.set(aPocetPlatnychKlucov, paUzol.aKluce.get(i));
			aPocetPlatnychKlucov++;
		}
		if (jeList()) {
			aAddr = paUzol.aAddr;
		}
	}

	public Kluc dajPredchadzajuciKluc(Kluc paKluc) {
		for (int i = 1; i < aPocetPlatnychKlucov; i++) {
			if (aKluce.get(i).getKluc().compareTo(paKluc) > 0) {
				return aKluce.get(i-1).getKluc().naklonuj();
			}
		}
		return aKluce.get(aPocetPlatnychKlucov-1).getKluc().naklonuj();
	}
	
	public Kluc dajNasledujuciKluc(Kluc paKluc) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (aKluce.get(i).getKluc().compareTo(paKluc) > 0) {
				return aKluce.get(i).getKluc().naklonuj();
			}
		}
		return null;
	}

	public void vymaz(Kluc paKluc) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (aKluce.get(i).getKluc().compareTo(paKluc) == 0) {
				aKluce.add(new BStromZaznam(paKluc.naklonuj(), -1));
				aKluce.remove(i);
				aPocetPlatnychKlucov--;
				return;
			}
		}
	}
	
	public void vymaz(long addr) {
		for (int i = 0; i < aPocetPlatnychKlucov; i++) {
			if (aKluce.get(i).getAdresa() == addr) {
				aKluce.add(new BStromZaznam(aKluce.get(i).getKluc().naklonuj(), -1));
				aKluce.remove(i);
				aPocetPlatnychKlucov--;
				return;
			}
		}
	}

	public boolean jePrazdny() {
		return aPocetPlatnychKlucov==0;
	}

	public long dajPoslednuAdresu() {
		if (aPocetPlatnychKlucov == 0 && !jeList())
			return aAddr;
		return aKluce.get(aPocetPlatnychKlucov-1).getAdresa();
	}
	
	public void print(BinarnySubor<Uzol> paSubor) throws IOException {
		print(paSubor, "", true);
	}

	private void print(BinarnySubor<Uzol> paSubor, String prefix, boolean isTail) throws IOException {
		if (jeList()) {
			for (int i = aPocetPlatnychKlucov-1; i >=0; i--) {
				System.out.println(prefix + (isTail ? "└─ " : "├─ ") + aKluce.get(i).getKluc());
			}
		}
		else {
			Uzol uzol;
			for (int i = aPocetPlatnychKlucov-1; i >=0; i--) {
				uzol = (Uzol) paSubor.dajZaznam(aKluce.get(i).getAdresa()).naklonuj();
				uzol.print(paSubor, prefix + (isTail ? "   " : "│  "), false);
				System.out.println(prefix + (isTail ? "└─ " : "├─ ") + aKluce.get(i).getKluc());
			}
			uzol = (Uzol) paSubor.dajZaznam(aAddr).naklonuj();
			uzol.print(paSubor, prefix + (isTail ? "   " : "│  "), false);
		}
	}
}
