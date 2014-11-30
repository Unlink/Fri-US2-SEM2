/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.uniza.fri.duracik2.bstrom.IntovyKluc;
import sk.uniza.fri.duracik2.bstrom.StringovyKluc;
import sk.uniza.fri.duracik2.fullIndex.UplnyIndex;
import sk.uniza.fri.duracik2.gui.reflection.Funkcia;
import sk.uniza.fri.duracik2.vodicaky.entity.Automobil;
import sk.uniza.fri.duracik2.vodicaky.entity.Preukaz;
import sun.java2d.loops.DrawGlyphListAA;

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

	@Funkcia(id=1, parametre = {"Evc vozidla"})
	public Automobil vyhladajEvc(String evc) throws IOException {
		return aAutomobily.vyhladaj(0, new StringovyKluc(evc, 7));
	}

	@Funkcia(id=2, parametre = {"Vin cislo"})
	public Automobil vyhladajVin(String vin) throws IOException {
		return aAutomobily.vyhladaj(1, new StringovyKluc(vin, 17));
	}

	@Funkcia(id=3, parametre = {"Evc", "Vin Cislo", "Poc naprav", "Hmotnost", "V patrani", "Koniec STK", "Koniec EK"})
	public boolean pridajAutomobil(String paEvc, String paVin, int pocNaprav, int paHmotnost, boolean vPatrani, Date paKoniecStk, Date paKoniecEk) throws IOException {
		Automobil auto = new Automobil(paEvc, paVin, pocNaprav, paHmotnost, vPatrani, paKoniecStk, paKoniecEk);
		return aAutomobily.vlozPrvok(auto) > 0;
	}
	
	@Funkcia(id=4, parametre = {"Aktualne Evc", "Evc", "Vin Cislo", "Poc naprav", "Hmotnost", "V patrani", "Koniec STK", "Koniec EK"})
	public boolean upravAutomobil(String paEvcAktual, String paEvc, String paVin, int pocNaprav, int paHmotnost, boolean vPatrani, Date paKoniecStk, Date paKoniecEk) throws IOException {
		Automobil auto = aAutomobily.vyhladaj(0, new StringovyKluc(paEvcAktual, 7));
		if (!auto.getEvcVozidla().equals(paEvc)) {
			aAutomobily.upravKluc(0, new StringovyKluc(paEvcAktual, 7), new StringovyKluc(paEvc, 7), auto.dajAdresu());
		}
		if (!auto.getVinCislo().equals(paVin)) {
			aAutomobily.upravKluc(1, new StringovyKluc(auto.getVinCislo(), 17), new StringovyKluc(paVin, 17), auto.dajAdresu());
		}
		auto.nakopirujData(new Automobil(paEvc, paVin, pocNaprav, paHmotnost, vPatrani, paKoniecStk, paKoniecEk));
		aAutomobily.commit();
		return true;
	}
	
	@Funkcia(id=5, parametre = {"Evc vozidla"})
	public boolean vyradAutomobilEvc(String evc) throws IOException {
		return aAutomobily.vymaz(0, new StringovyKluc(evc, 7)) != null;
	}

	@Funkcia(id=6, parametre = {"Vin cislo"})
	public boolean vyradAutomobilVin(String vin) throws IOException {
		return aAutomobily.vymaz(1, new StringovyKluc(vin, 17)) != null;
	}
	
	@Funkcia(id=7, parametre = {"Cislo Preukazu"})
	public Preukaz vykladajPreukaz(int cislo) throws IOException {
		return aPreukazy.vyhladaj(0, new IntovyKluc(cislo));
	}
	
	@Funkcia(id=8, parametre = {"Cislo Preukazu"})
	public boolean vymazPreukaz(int cislo) throws IOException {
		return aPreukazy.vymaz(0, new IntovyKluc(cislo)) != null;
	}
	
	@Funkcia(id=9, parametre = {"Meno", "Priezvisko", "Cislo", "Datum Platnosti", "Zákaz", "Pocet priestupkov"})
	public boolean pridajPreukaz(String paMeno, String paPriezvisko, int paCislo, Date paDatumPlatnosti, boolean paZakaz, int paPocetPriestupkov) throws IOException {
		Preukaz preukaz = new Preukaz(paMeno, paPriezvisko, paCislo, paDatumPlatnosti, paZakaz, paPocetPriestupkov);
		return aPreukazy.vlozPrvok(preukaz) > 0;
	}
	
	@Funkcia(id=10, parametre = {"Aktualne Cislo", "Meno", "Priezvisko", "Cislo", "Datum Platnosti", "Zákaz", "Pocet priestupkov"})
	public boolean upravPreukaz(int paAktual, String paMeno, String paPriezvisko, int paCislo, Date paDatumPlatnosti, boolean paZakaz, int paPocetPriestupkov) throws IOException {
		Preukaz preukaz = aPreukazy.vyhladaj(0, new IntovyKluc(paAktual));
		if (preukaz.getCislo() != paCislo) {
			aPreukazy.upravKluc(0, new IntovyKluc(paAktual), new IntovyKluc(paCislo), preukaz.dajAdresu());
		}
		preukaz.nakopirujData(new Preukaz(paMeno, paPriezvisko, paCislo, paDatumPlatnosti, paZakaz, paPocetPriestupkov));
		aPreukazy.commit();
		return true;
	}
	
	@Funkcia()
	public String SuborAut() {
		return aAutomobily.dajSubor().toString();
	}
	
	@Funkcia()
	public String SuborPreukazov() {
		return aPreukazy.dajSubor().toString();
	}
	
	@Funkcia()
	public String SuborAutIndex1() {
		return aAutomobily.dajSubor(0).toString();
	}
	
	@Funkcia()
	public String SuborAutIndex2() {
		return aAutomobily.dajSubor(1).toString();
	}
	
	@Funkcia()
	public String SuborPreukazovIndex1() {
		return aPreukazy.dajSubor(0).toString();
	}
	
	@Funkcia()
	public String StromAutIndex1() throws IOException {
		StringBuilder sb = new StringBuilder("\n");
		aAutomobily.dajSubor(0).vypisStrom(sb);
		return sb.toString();
	}
	
	@Funkcia()
	public String StromAutIndex2() throws IOException {
		StringBuilder sb = new StringBuilder("\n");
		aAutomobily.dajSubor(1).vypisStrom(sb);
		return sb.toString();
	}
	
	@Funkcia()
	public String StromPreukazovIndex1() throws IOException {
		StringBuilder sb = new StringBuilder("\n");
		aPreukazy.dajSubor(0).vypisStrom(sb);
		return sb.toString();
	}
	
	@Funkcia()
	public String VsetkyAutaPodlaEvc() throws IOException {
		return aAutomobily.dajVestko(0);
	}
	
	@Funkcia()
	public String VsetkyAutaPodlaVin() throws IOException {
		return aAutomobily.dajVestko(1);
	}
	
	@Funkcia()
	public String VsetkyPreukazy() throws IOException {
		return aPreukazy.dajVestko(0);
	}
	
	@Funkcia(parametre = {"Pocet aut", "Pocet preukazov"})
	public void generuj(int paAut, int paPreukazov) throws IOException {
		aAutomobily.vyprazdni();
		aPreukazy.vyprazdni();
		HashSet<String> evcs = new HashSet<>();
		HashSet<String> vins = new HashSet<>();
		HashSet<Integer> ids = new HashSet<>();
		for (int i=0; i<paAut; i++) {
			String e = Generator.randomString(2)+Generator.randomNumericString(3)+Generator.randomString(2);
			while (evcs.contains(e)) {
				e = Generator.randomString(2)+Generator.randomNumericString(3)+Generator.randomString(2);
			}
			evcs.add(e);
			String v = Generator.randomNumericString(17);
			while (vins.contains(v)) {
				v = Generator.randomNumericString(17);
			}
			vins.add(v);
			
			pridajAutomobil(e, v, Generator.randomBetween(2, 5), Generator.randomBetween(500, 2000), false, Generator.randomDate(), Generator.randomDate());
			
		}
		
		for (int i=0; i<paPreukazov; i++) {
			int c = Math.abs(Generator.rg.nextInt());
			while (ids.contains(c)) {
				c = Math.abs(Generator.rg.nextInt());
			}
			ids.add(c);
			
			pridajPreukaz(Generator.randomString(5), Generator.randomString(5), c, Generator.randomDate(), false, Generator.rg.nextInt(5));
		}
	}

	@Override
	public void close() throws Exception {
		aAutomobily.close();
		aPreukazy.close();
	}

}
