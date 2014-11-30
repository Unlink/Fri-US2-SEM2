/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import sk.uniza.fri.duracik2.blockfile.AZaznam;
import sk.uniza.fri.duracik2.blockfile.IZaznam;
import sk.uniza.fri.duracik2.bstrom.Kluc;
import sk.uniza.fri.duracik2.bstrom.StringovyKluc;
import sk.uniza.fri.duracik2.fullIndex.IIndexovatelnyPrvok;
import sk.uniza.fri.duracik2.gui.IGuiPrint;
import sk.uniza.fri.duracik2.gui.JColorTextPane;

/**
 *
 * @author Unlink
 */
public class Automobil extends AZaznam implements IIndexovatelnyPrvok, IGuiPrint {

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

	public Automobil(String paEvcVozidla, String paVinCislo, int paPocetNaprav, int paHmotnost, boolean paVPatrani, Date paKoniecStk, Date paKonciecEk) {
		this.aEvcVozidla = paEvcVozidla;
		this.aVinCislo = paVinCislo;
		this.aPocetNaprav = (short) paPocetNaprav;
		this.aHmotnost = paHmotnost;
		this.aVPatrani = paVPatrani;
		this.aKoniecStk = paKoniecStk;
		this.aKonciecEk = paKonciecEk;
	}

	@Override
	public int dajVelkost() {
		return 7 * 2 //EVC
			+ 17 * 2 //Vin
			+ 2 //Pocet naprav
			+ 4 //hmotnost
			+ 1 //V patrani
			+ 8 //Datum1
			+ 8;   //Datum 2
	}

	@Override
	public void nahraj(DataInputStream paStream) {
		try {
			byte[] buff = new byte[7 * 2];
			paStream.read(buff);
			aEvcVozidla = new String(buff).trim();
			buff = new byte[17 * 2];
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
			byte[] buff = new byte[7 * 2];
			byte[] temp = aEvcVozidla.getBytes();
			System.arraycopy(temp, 0, buff, 0, Math.min(buff.length, temp.length));
			stream.write(buff);
			buff = new byte[17 * 2];
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

	public Kluc[] dajKluce() {
		return new Kluc[]{
			new StringovyKluc(aEvcVozidla, 7),
			new StringovyKluc(aVinCislo, 17),};
	}

	@Override
	public void nakopirujData(IIndexovatelnyPrvok paPrvok) {
		Automobil auto = (Automobil) paPrvok;
		aEvcVozidla = auto.aEvcVozidla;
		aHmotnost = auto.aHmotnost;
		aKonciecEk = auto.aKonciecEk;
		aKoniecStk = auto.aKoniecStk;
		aPocetNaprav = auto.aPocetNaprav;
		aVPatrani = auto.aVPatrani;
		aVinCislo = auto.aVinCislo;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + Objects.hashCode(this.aEvcVozidla);
		hash = 59 * hash + Objects.hashCode(this.aVinCislo);
		hash = 59 * hash + this.aPocetNaprav;
		hash = 59 * hash + this.aHmotnost;
		hash = 59 * hash + (this.aVPatrani ? 1 : 0);
		hash = 59 * hash + Objects.hashCode(this.aKoniecStk);
		hash = 59 * hash + Objects.hashCode(this.aKonciecEk);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Automobil other = (Automobil) obj;
		if (!Objects.equals(this.aEvcVozidla, other.aEvcVozidla)) {
			return false;
		}
		if (!Objects.equals(this.aVinCislo, other.aVinCislo)) {
			return false;
		}
		if (this.aPocetNaprav != other.aPocetNaprav) {
			return false;
		}
		if (this.aHmotnost != other.aHmotnost) {
			return false;
		}
		if (this.aVPatrani != other.aVPatrani) {
			return false;
		}
		if (!Objects.equals(this.aKoniecStk, other.aKoniecStk)) {
			return false;
		}
		if (!Objects.equals(this.aKonciecEk, other.aKonciecEk)) {
			return false;
		}
		return true;
	}

	@Override
	public void print(JColorTextPane paNe) {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		paNe.append("EVC: ");
		paNe.append(aEvcVozidla);
		paNe.append("\n");
		paNe.append("Vin: ");
		paNe.append(aVinCislo);
		paNe.append("\n");
		paNe.append("Naprav: ");
		paNe.append(aPocetNaprav+"");
		paNe.append("\n");
		paNe.append("Hmotnost: ");
		paNe.append(aHmotnost+"");
		paNe.append("\n");
		paNe.append("Dat STK: ");
		paNe.append(format.format(aKoniecStk));
		paNe.append("\n");
		paNe.append("Dat EK: ");
		paNe.append(format.format(aKonciecEk));
		paNe.append("\n");
		paNe.append("V patrani: ");
		paNe.append(aVPatrani+"");
		paNe.append("\n");
	}

}
