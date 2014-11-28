/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.blockfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Unlink
 */
class Blok implements Cloneable {

	private final IZaznam[] aZaznamy;
	private final BitovePole aValidne;
	private long aAdresaBloku;
	private int aVelkost;
	
	public Blok(IZaznam[] paStruktura) {
		this(paStruktura, -1);
	}
		
	public Blok(IZaznam[] paStruktura, long paAdresaBloku) {
		aZaznamy = paStruktura;
		aValidne = new BitovePole(aZaznamy.length);
		aAdresaBloku = paAdresaBloku;
		aVelkost = aValidne.dajVelkost();
		for (IZaznam zaznam : aZaznamy) {
			aVelkost += zaznam.dajVelkost();
			zaznam.nastavValiditu(false);
		}
	}

	public void nahraj(byte[] data, long paAdresaBloku) {
		aAdresaBloku = paAdresaBloku;
		aValidne.nastavData(Arrays.copyOfRange(data, 0, aValidne.dajVelkost()));
		int position = aValidne.dajVelkost();
		int i = 0;
		for (IZaznam zaznam : aZaznamy) {
			//Len ak je zaznm validny tak ho citame
			if (aValidne.dajFlag(i)) {
				zaznam.nahraj(new DataInputStream(new ByteArrayInputStream(data, position, zaznam.dajVelkost())));
				zaznam.nastavAdresu(spocitajAdresu(aAdresaBloku, i));
				zaznam.nastavValiditu(true);
			}
			else {
				zaznam.nastavValiditu(false);
			}
			position += zaznam.dajVelkost();
			i++;
		}
	}

	public byte[] dajBajty() {
		ByteArrayOutputStream poleBajtov = new ByteArrayOutputStream(aVelkost);
		DataOutputStream stream = new DataOutputStream(poleBajtov);
		try {
			spocitajBitovePoleValidity();
			stream.write(aValidne.dajData());
			for (IZaznam zaznam : aZaznamy) {
				zaznam.serializuj(stream);
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return poleBajtov.toByteArray();
	}
	

	public Blok naklonuj() {
		IZaznam[] zaznamy = new IZaznam[aZaznamy.length];
		for (int i = 0; i < aZaznamy.length; i++) {
			zaznamy[i] = (IZaznam) aZaznamy[i].naklonuj();
		}
		return new Blok(zaznamy, aAdresaBloku);
	}

	public long getAdresaBloku() {
		return aAdresaBloku;
	}
	
	public int dajPocetZaznamov() {
		return aZaznamy.length;
	}

	public void nastavAdresuBloku(long aAdresaBloku) {
		this.aAdresaBloku = aAdresaBloku;
		this.invalidujVsetko();
	}
	
	public IZaznam dajZaznam(int index) {
		aZaznamy[index].nastavAdresu(spocitajAdresu(aAdresaBloku, index));
		return aZaznamy[index];
	}
	
	public boolean jeValidny(int index) {
		return aZaznamy[index].jeValidny();
	}

	public int getVelkost() {
		return aVelkost;
	}
	
	private void spocitajBitovePoleValidity() {
		for (int i = 0; i < aZaznamy.length; i++) {
			aValidne.nastavFlag(i, aZaznamy[i].jeValidny());
		}
	}
	
	public int dajVolnyZaznam() {
		for (int i = 0; i < aZaznamy.length; i++) {
			if (!aZaznamy[i].jeValidny()) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean jePlny() {
		for (int i = 0; i < aZaznamy.length; i++) {
			if (!aZaznamy[i].jeValidny()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean jePrazndy() {
		for (int i = 0; i < aZaznamy.length; i++) {
			if (aZaznamy[i].jeValidny()) {
				return false;
			}
		}
		return true;
	}

	public void invalidujVsetko() {
		for (int i = 0; i < aZaznamy.length; i++) {
			aZaznamy[i].nastavValiditu(false);
		}
	}
	
	public static long spocitajAdresu(long blok, int indexZaznamu) {
		return (blok << 8) + indexZaznamu;
	}
	
	public static long dajIndexBloku(long paAdresa) {
		return paAdresa >> 8;
	}
	
	public static int dajIndexZaznamu(long paAdresa) {
		return (int)(paAdresa & 0xFF);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Blok - " + aAdresaBloku+ "\n");
		for (int i = 0; i < aZaznamy.length; i++) {
			if (aZaznamy[i].jeValidny()) {
				sb.append(i).append(": ").append(aZaznamy[i].toString()).append("\n");
			}
			else {
				sb.append(i).append(": ").append("Neplatny").append("\n");
			}
		}
		return sb.toString();
	}
	
	
}
