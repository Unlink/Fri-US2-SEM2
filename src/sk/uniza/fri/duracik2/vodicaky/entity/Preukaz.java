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
import sk.uniza.fri.duracik2.bstrom.IntovyKluc;
import sk.uniza.fri.duracik2.bstrom.Kluc;
import sk.uniza.fri.duracik2.fullIndex.IIndexovatelnyPrvok;

/**
 *
 * @author Unlink
 */
public class Preukaz extends AZaznam implements IIndexovatelnyPrvok {

	//40 znakov
	private String aMeno;
	//40 znakov
	private String aPriezvisko;
	//unikatne
	private int aCislo;
	//datum
	private Date aDatumPlatnosti;
	//zakaz viest
	private boolean aZakaz;
	//počet dopravných priestupkov
	private int aPocetPriestupkov;

	public Preukaz(String paMeno, String paPriezvisko, int paCislo, Date paDatumPlatnosti, boolean paZakaz, int paPocetPriestupkov) {
		this.aMeno = paMeno;
		this.aPriezvisko = paPriezvisko;
		this.aCislo = paCislo;
		this.aDatumPlatnosti = paDatumPlatnosti;
		this.aZakaz = paZakaz;
		this.aPocetPriestupkov = paPocetPriestupkov;
	}

	public Preukaz() {
		this("", "", 0, new Date(), false, 0);
	}
	
	@Override
	public int dajVelkost() {
		return 40*2 //Meno
			+ 40*2	//Prezv
			+ 4		//Cislo
			+ 8		//Datum
			+ 1		//Zakaz
			+ 4;	//Pocet
	}

	@Override
	public void nahraj(DataInputStream paStream) {
		try {
			byte[] buff = new byte[40 * 2];
			paStream.read(buff);
			aMeno = new String(buff).trim();
			buff = new byte[40 * 2];
			paStream.read(buff);
			aPriezvisko = new String(buff).trim();
			aCislo = paStream.readInt();
			aDatumPlatnosti = new Date(paStream.readLong());
			aZakaz = paStream.readBoolean();
			aPocetPriestupkov = paStream.readInt();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void serializuj(DataOutputStream paStream) {
		try {
			byte[] buff = new byte[40 * 2];
			byte[] temp = aMeno.getBytes();
			System.arraycopy(temp, 0, buff, 0, Math.min(buff.length, temp.length));
			paStream.write(buff);
			buff = new byte[40 * 2];
			temp = aPriezvisko.getBytes();
			System.arraycopy(temp, 0, buff, 0, Math.min(buff.length, temp.length));
			paStream.write(buff);
			paStream.writeInt(aCislo);
			paStream.writeLong(aDatumPlatnosti.getTime());
			paStream.writeBoolean(aZakaz);
			paStream.writeInt(aPocetPriestupkov);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public IZaznam naklonuj() {
		Preukaz preukaz = new Preukaz();
		preukaz.aCislo = aCislo;
		preukaz.aDatumPlatnosti = new Date(aDatumPlatnosti.getTime());
		preukaz.aMeno = aMeno;
		preukaz.aPriezvisko = aPriezvisko;
		preukaz.aZakaz = aZakaz;
		preukaz.aPocetPriestupkov = aPocetPriestupkov;
		return preukaz;
	}

	@Override
	public Kluc[] dajKluce() {
		return new Kluc[] {
			new IntovyKluc(aCislo)
		};
	}

	@Override
	public void nakopirujData(IIndexovatelnyPrvok paPrvok) {
		Preukaz preukaz = (Preukaz) paPrvok;
		aCislo = preukaz.aCislo;
		aDatumPlatnosti = new Date(preukaz.aDatumPlatnosti.getTime());
		aMeno = preukaz.aMeno;
		aPriezvisko = preukaz.aPriezvisko;
		aZakaz = preukaz.aZakaz;
		aPocetPriestupkov = preukaz.aPocetPriestupkov;
	}
	
}
