/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import sk.uniza.fri.duracik2.blockfile.AZaznam;
import sk.uniza.fri.duracik2.blockfile.IZaznam;

/**
 *
 * @author Unlink
 */
public class Automobil extends AZaznam {
	
	/**
	 * dlzka 7 znakov
	 */
	private String aEvcVozidla;
	/**
	 * dlzka 17 znakov
	 */
	private String aVinCislo;
	private short aPocetNaprav;
	private int aHmotnost;
	private boolean aVPatrani;
	private Date aKoniecStk;
	private Date aKonciecEk;

	public Automobil() {
		aEvcVozidla = "";
		aVinCislo = "";
		aKoniecStk = new Date();
		aKonciecEk = new Date();
	}
	
	

	@Override
	public int dajVelkost() {
		return 7*2
			+ 17*2
			+ 2
			+ 4
			+ 1
			+ 8
			+ 8;
	}

	@Override
	public void nahraj(DataInputStream paStream) {
		try {
			byte[] buff = new byte[7*2];
			paStream.read(buff);
			aEvcVozidla = new String(buff).trim();
			buff = new byte[17*2];
			paStream.read(buff);
			aVinCislo = new String(buff).trim();
			aPocetNaprav = paStream.readShort();
			aHmotnost = paStream.readInt();
			aVPatrani = paStream.readBoolean();
			aKoniecStk = new Date(paStream.readLong());
			aKonciecEk = new Date(paStream.readLong());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

	@Override
	public void serializuj(DataOutputStream stream) {
		try {
			byte [] buff = new byte[7*2];
			byte [] temp = aEvcVozidla.getBytes();
			System.arraycopy(temp, 0, buff, 0, Math.min(buff.length, temp.length));
			stream.write(buff);
			buff = new byte[17*2];
			temp = aVinCislo.getBytes();
			System.arraycopy(temp, 0, buff, 0, Math.min(buff.length, temp.length));
			stream.write(buff);
			
			stream.writeShort(aPocetNaprav);
			stream.writeInt(aHmotnost);
			stream.writeBoolean(aVPatrani);
			stream.writeLong(aKoniecStk.getTime());
			stream.writeLong(aKonciecEk.getTime());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	

	@Override
	public IZaznam naklonuj() {
		Automobil auto = new Automobil();
		auto.setEvcVozidla(aEvcVozidla);
		auto.setHmotnost(aHmotnost);
		auto.setKonciecEk(new Date(aKonciecEk.getTime()));
		auto.setKoniecStk(new Date(aKoniecStk.getTime()));
		auto.setPocetNaprav(aPocetNaprav);
		auto.setVPatrani(aVPatrani);
		auto.setVinCislo(aVinCislo);
		return auto;
	}

	public String getEvcVozidla() {
		return aEvcVozidla;
	}

	public void setEvcVozidla(String aEvcVozidla) {
		this.aEvcVozidla = aEvcVozidla;
	}

	public String getVinCislo() {
		return aVinCislo;
	}

	public void setVinCislo(String aVinCislo) {
		this.aVinCislo = aVinCislo;
	}

	public short getPocetNaprav() {
		return aPocetNaprav;
	}

	public void setPocetNaprav(short aPocetNaprav) {
		this.aPocetNaprav = aPocetNaprav;
	}

	public int getHmotnost() {
		return aHmotnost;
	}

	public void setHmotnost(int aHmotnost) {
		this.aHmotnost = aHmotnost;
	}

	public boolean isVPatrani() {
		return aVPatrani;
	}

	public void setVPatrani(boolean aVPatrani) {
		this.aVPatrani = aVPatrani;
	}

	public Date getKoniecStk() {
		return aKoniecStk;
	}

	public void setKoniecStk(Date aKoniecStk) {
		this.aKoniecStk = aKoniecStk;
	}

	public Date getKonciecEk() {
		return aKonciecEk;
	}

	public void setKonciecEk(Date aKonciecEk) {
		this.aKonciecEk = aKonciecEk;
	}

	@Override
	public String toString() {
		return "Automobil{" + "aEvcVozidla=" + aEvcVozidla + ", aVinCislo=" + aVinCislo + '}';
	}
	
}
