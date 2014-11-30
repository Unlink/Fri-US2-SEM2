/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.uniza.fri.duracik2.bstrom.IntovyKluc;
import sk.uniza.fri.duracik2.bstrom.StringovyKluc;
import sk.uniza.fri.duracik2.fullIndex.UplnyIndex;
import sk.uniza.fri.duracik2.gui.reflection.Funkcia;
import sk.uniza.fri.duracik2.vodicaky.entity.Automobil;
import sk.uniza.fri.duracik2.vodicaky.entity.Preukaz;

/**
 *
 * @author Unlink
 */
public class Aplikacia implements AutoCloseable {

	private UplnyIndex<Automobil> aAutomobily;
	private UplnyIndex<Preukaz> aPreukazy;

	public Aplikacia() throws IOException {
		try {
			File zlozka = new File("data");
			aAutomobily = new UplnyIndex<>(new Automobil(), 3, new int[]{5,5}, zlozka, true);
			aPreukazy = new UplnyIndex<>(new Preukaz(), 3, new int[]{5,5}, zlozka, true);
		}
		catch (InstantiationException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	@Funkcia(parametre = {"Evc vozidla"})
	public Automobil vyhladajEvc(String evc) throws IOException {
		return aAutomobily.vyhladaj(0, new StringovyKluc(evc, 7));
	}

	@Funkcia(parametre = {"Vin cislo"})
	public Automobil vyhladajVin(String vin) throws IOException {
		return aAutomobily.vyhladaj(1, new StringovyKluc(vin, 17));
	}

	@Funkcia(parametre = {"Evc", "Vin Cislo", "Poc naprav", "Hmotnost", "V patrani", "Koniec STK", "Koniec EK"})
	public boolean pridajAutomobil(String paEvc, String paVin, int pocNaprav, int paHmotnost, boolean vPatrani, Date paKoniecStk, Date paKoniecEk) throws IOException {
		Automobil auto = new Automobil(paEvc, paVin, pocNaprav, paHmotnost, vPatrani, paKoniecStk, paKoniecEk);
		return aAutomobily.vlozPrvok(auto) > 0;
	}
	
	@Funkcia(parametre = {"Evc vozidla"})
	public boolean vyradEvc(String evc) throws IOException {
		return aAutomobily.vymaz(0, new StringovyKluc(evc, 7)) != null;
	}

	@Funkcia(parametre = {"Vin cislo"})
	public boolean vyradVin(String vin) throws IOException {
		return aAutomobily.vymaz(1, new StringovyKluc(vin, 17)) != null;
	}
	
	@Funkcia(parametre = {"Cislo Preukazu"})
	public Preukaz vykladajPreukaz(int cislo) throws IOException {
		return aPreukazy.vyhladaj(0, new IntovyKluc(cislo));
	}
	
	@Funkcia(parametre = {"Cislo Preukazu"})
	public boolean vymazPreukaz(int cislo) throws IOException {
		return aPreukazy.vymaz(0, new IntovyKluc(cislo)) != null;
	}
	
	@Funkcia(parametre = {"Meno", "Priezvisko", "Cislo", "Datum Platnosti", "Zákaz", "Pocet priestupkov"})
	public boolean pridajPreukaz(String paMeno, String paPriezvisko, int paCislo, Date paDatumPlatnosti, boolean paZakaz, int paPocetPriestupkov) throws IOException {
		Preukaz preukaz = new Preukaz(paMeno, paPriezvisko, paCislo, paDatumPlatnosti, paZakaz, paPocetPriestupkov);
		return aPreukazy.vlozPrvok(preukaz) > 0;
	}

	@Override
	public void close() throws Exception {
		aAutomobily.close();
		aPreukazy.close();
	}

}
