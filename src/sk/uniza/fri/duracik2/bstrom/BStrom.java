/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import sk.uniza.fri.duracik2.blockfile.BinarnySubor;
import sk.uniza.fri.duracik2.blockfile.Blok;

/**
 *
 * @author Unlink
 */
public class BStrom implements AutoCloseable {

	private BinarnySubor<Uzol> aSubor;
	private BStromInfoBlok aInfoBlok;
	private int aStupen;

	public BStrom(File aCesta, Kluc paSampleKey, int paStupen) throws IOException, InstantiationException, IllegalAccessException {
		aInfoBlok = new BStromInfoBlok();
		aStupen = paStupen;
		Uzol uzol = new Uzol(paSampleKey, aStupen - 1, true);
		aSubor = new BinarnySubor<>(uzol, 1, aCesta, aInfoBlok);
	}

	@Override
	public void close() throws Exception {
		aSubor.close();
	}

	public long najdi(Kluc paKluc) throws IOException {
		Uzol uzol = aSubor.dajZaznam(aInfoBlok.getKoren());
		while (!uzol.jeList()) {
			long addresa = uzol.dalsiaAdresa(paKluc);
			uzol = aSubor.dajZaznam(addresa);
		}
		return uzol.dajAdresuKluca(paKluc);
	}

	public void vloz(BStromZaznam paZaznam) throws IOException {
		Kluc kluc = paZaznam.getKluc();
		if (aInfoBlok.getKoren() == -1) {
			vlozNovyKoren(paZaznam);
			return;
		}
		LinkedList<Blok> bloky = traverzujStrom(kluc);
		Uzol uzol = (Uzol) bloky.getLast().dajZaznam(0);
		if (uzol.dajAdresuKluca(kluc) != Uzol.KLUC_NENAJDENY) {
			throw new DuplikatnyPrvokException();
		}

		//znovu nacitanie zo suboru
		uzol = aSubor.dajZaznam(uzol.dajAdresu());

		if (uzol.maMiesto()) {
			uzol.vloz(paZaznam);
			aSubor.ulozBlok();
		}
		else {
			uzol = (Uzol) bloky.getLast().dajZaznam(0);
			Uzol novy = aSubor.dajVolnyZaznam();
			novy.setJeList(true);
			novy.nastavValiditu(true);
			Kluc k = uzol.rozdelList(paZaznam, novy);
			aSubor.ulozBlok();
			uzol.setAddr(novy.dajAdresu());
			aSubor.nastavBlok(bloky.removeLast());
			aSubor.ulozBlok();
			BStromZaznam bz = new BStromZaznam(k, novy.dajAdresu());
			while (!bloky.isEmpty()) {
				Blok b = bloky.removeLast();
				uzol = (Uzol) b.dajZaznam(0);
				if (uzol.maMiesto()) {
					uzol.vloz(bz);
					aSubor.nastavBlok(b);
					aSubor.ulozBlok();
					bz = null;
					break;
				}
				else {
					novy = aSubor.dajVolnyZaznam();
					novy.setJeList(false);
					novy.nastavValiditu(true);
					k = uzol.rozdelVnutornyUzol(bz, novy);
					aSubor.ulozBlok();
					aSubor.nastavBlok(b);
					aSubor.ulozBlok();
					bz = new BStromZaznam(k, novy.dajAdresu());
				}
			}
			//Pridavame koreň
			if (bz != null) {
				Uzol novyKoren = aSubor.dajVolnyZaznam();
				novyKoren.setJeList(false);
				novyKoren.vloz(bz);
				novyKoren.setAddr(aInfoBlok.getKoren());
				novyKoren.nastavValiditu(true);
				aSubor.ulozBlok();
				aInfoBlok.setKoren(novyKoren.dajAdresu());
			}
		}

	}

	public long vymaz(Kluc kluc) throws IOException {
		if (aInfoBlok.getKoren() == -1) {
			return -1;
		}
		LinkedList<Blok> bloky = traverzujStrom(kluc);
		Blok aktual = bloky.removeLast();
		Uzol uzol = (Uzol) aktual.dajZaznam(0);
		long adresaKluca = uzol.dajAdresuKluca(kluc);
		if (adresaKluca == Uzol.KLUC_NENAJDENY) {
			return -1;
		}

		//Ak je v liste dosť prvkov, tak zmažeme a koniec
		if (uzol.dajAdresu() == aInfoBlok.getKoren() || uzol.maDostatokPrvkov()) {
			BStromZaznam zaznam = uzol.odoberKlucZListu(kluc);
			//Ak sme vyprazdnili koreň
			if (uzol.dajAdresu() == aInfoBlok.getKoren() && uzol.getPocetPlatnychKlucov() == 0) {
				zmazKoren(aktual);
				return adresaKluca;
			}
			else {
				aSubor.nastavBlok(aktual);
				aSubor.ulozBlok();
			}
			if (zaznam != null) {
				zmenKluce(bloky, zaznam.getKluc(), kluc);
			}
			return adresaKluca;
		}
		
		uzol.odoberKlucZListu(kluc);
		while (uzol.dajAdresu() != aInfoBlok.getKoren() && uzol.getPocetPlatnychKlucov() < aStupen/2) {
			BStromZaznam zaznam;
			Uzol otec = (Uzol) bloky.getLast().dajZaznam(0);
			BlokHolder lavyBrat = new BlokHolder(null);
			zaznam = zoberZlaveho(uzol.dajAdresu(), otec, lavyBrat);
			if (zaznam != null) {
				Kluc k = ((Uzol)lavyBrat.getBlok().dajZaznam(0)).dajMaximalnyKluc();
				if (uzol.jeList()) {
					uzol.zaradAkoMinimalny(zaznam);
					otec.nahradKluc(zaznam.getKluc().naklonuj(), k);
				}
				else {
					BStromZaznam novy = new BStromZaznam(najdiMaximalnyKluc(zaznam.getAdresa()), zaznam.getAdresa());
					uzol.zaradAkoMinimalny(novy);
					otec.nahradKluc(novy.getKluc().naklonuj(), zaznam.getKluc());
				}
				//otec.nahradKluc(otec.dajNasledujuciKluc(k).naklonuj(), uzol.dajMaximalnyKluc());
				aSubor.nastavBlok(aktual);
				aSubor.ulozBlok();
				aSubor.nastavBlok(bloky.getLast());
				aSubor.ulozBlok();
				zmenKluce(bloky, uzol.dajMaximalnyKluc().naklonuj(), kluc);
				return adresaKluca;
			}
			BlokHolder pravyBrat = new BlokHolder(null);
			zaznam = zoberZPraveho(uzol.dajAdresu(), otec, pravyBrat);
			if (zaznam != null) {
				//Kluc k = otec.dajPredchadzajuciKluc(zaznam.getKluc());
				if (uzol.jeList())
					uzol.zaradAkoMaximalny(zaznam);
				else {
					BStromZaznam novy = new BStromZaznam(najdiMaximalnyKluc(uzol).naklonuj(), zaznam.getAdresa());
					uzol.zaradAkoMaximalny(novy);
				}
				otec.nahradKluc(uzol.dajAdresu(), zaznam.getKluc().naklonuj());
				aSubor.nastavBlok(aktual);
				aSubor.ulozBlok();
				aSubor.nastavBlok(bloky.getLast());
				aSubor.ulozBlok();
				return adresaKluca;
			}

			//Ak sa nepodarilo zobrať z bratov, tak spojime bloky
			if (lavyBrat.getBlok() != null) {
				Uzol brat = (Uzol) lavyBrat.getBlok().dajZaznam(0);
				Kluc k = najdiMaximalnyKluc(brat);
				brat.spojBloky(uzol);
				if (uzol.jeList()) {
					otec.vymaz(uzol.dajAdresu());
					zmenKluce(bloky, brat.dajMaximalnyKluc(), kluc);
				}
				else {
					
					brat.vloz(new BStromZaznam(k.naklonuj(), uzol.getAddr()));
					otec.vymaz(uzol.dajAdresu());
				}
				//Uloženie brata
				aSubor.nastavBlok(lavyBrat.getBlok());
				aSubor.ulozBlok();
				//Uloženie otca
				aSubor.nastavBlok(bloky.getLast());
				aSubor.ulozBlok();
				//Vymazanie bloku
				aSubor.nastavBlok(aktual);
				uzol.nastavValiditu(false);
				aSubor.ulozBlok();
			}
			else if (pravyBrat.getBlok() != null) {
				Uzol brat = (Uzol) pravyBrat.getBlok().dajZaznam(0);
				Kluc k = (uzol.jeList()) ? kluc : najdiMaximalnyKluc(uzol);
				uzol.spojBloky(brat);
				if (uzol.jeList()) {
					otec.vymaz(brat.dajAdresu());
					//zmenKluce(bloky, k, kluc);
				}
				else {
					uzol.vloz(new BStromZaznam(k.naklonuj(), brat.getAddr()));
					otec.vymaz(brat.dajAdresu());
				}
				//Uloženie uzla
				aSubor.nastavBlok(aktual);
				aSubor.ulozBlok();
				aSubor.nastavBlok(bloky.getLast());
				aSubor.ulozBlok();
				//Vymazanie bloku
				aSubor.nastavBlok(pravyBrat.getBlok());
				brat.nastavValiditu(false);
				aSubor.ulozBlok();
			}
		
			aktual = bloky.removeLast();
			uzol = (Uzol) aktual.dajZaznam(0);
		}
		
		if (uzol.dajAdresu() == aInfoBlok.getKoren() && uzol.jePrazdny()){
			aInfoBlok.setKoren(uzol.getAddr());
			aSubor.nastavBlok(aktual);
			uzol.nastavValiditu(false);
			aSubor.ulozBlok();
		}
		
		return adresaKluca;
	}

	private void zmazKoren(Blok blok) throws IOException {
		Uzol uzol = (Uzol) blok.dajZaznam(0);
		aInfoBlok.setKoren(-1);
		aInfoBlok.setZacUtriedeneho(-1);
		uzol.nastavValiditu(false);
		aSubor.nastavBlok(blok);
		aSubor.ulozBlok();
	}

	private LinkedList<Blok> traverzujStrom(Kluc kluc) throws IOException {
		Uzol uzol = aSubor.dajZaznam(aInfoBlok.getKoren());
		LinkedList<Blok> bloky = new LinkedList<>();
		while (!uzol.jeList()) {
			bloky.add(aSubor.dajBlok().naklonuj());
			long addresa = uzol.dalsiaAdresa(kluc);
			uzol = aSubor.dajZaznam(addresa);
		}
		bloky.add(aSubor.dajBlok().naklonuj());
		return bloky;
	}

	private void vlozNovyKoren(BStromZaznam paZaznam) throws IOException {
		Uzol koren = aSubor.dajVolnyZaznam();
		koren.setJeList(true);
		koren.vloz(paZaznam);
		koren.nastavValiditu(true);
		aSubor.ulozBlok();
		aInfoBlok.setKoren(koren.dajAdresu());
		aInfoBlok.setZacUtriedeneho(koren.dajAdresu());
	}

	public void inOrderVypis() throws IOException {
		if (aInfoBlok.getKoren() == -1) {
			return;
		}
		Uzol uzol = aSubor.dajZaznam(aInfoBlok.getZacUtriedeneho());
		System.out.println(uzol);
		while (uzol.getAddr() > 0) {
			uzol = aSubor.dajZaznam(uzol.getAddr());
			System.out.println(uzol);
		}
	}

	private void zmenKluce(LinkedList<Blok> bloky, Kluc k1, Kluc kluc) throws IOException {
		for (int i=bloky.size()-1; i>=0; i--) {
			Blok blok = bloky.get(i);
			Uzol uzol = (Uzol) blok.dajZaznam(0);
			if (uzol.nahradKluc(kluc, k1)) {
				aSubor.nastavBlok(blok);
				aSubor.ulozBlok();
			}
		}
	}

	private BStromZaznam zoberZlaveho(long adresa, Uzol paOtec, BlokHolder paHolder) throws IOException {
		long addr = paOtec.dajLavehoBrata(adresa);
		if (addr < 0) {
			return null;
		}
		Uzol brat = aSubor.dajZaznam(addr);
		BStromZaznam zaznam = brat.zoberMaximalnyKluc();
		paHolder.setBlok(aSubor.dajBlok().naklonuj());
		if (zaznam != null) {
			aSubor.ulozBlok();
		}
		return zaznam;
	}

	private BStromZaznam zoberZPraveho(long adresa, Uzol paOtec, BlokHolder paHolder) throws IOException {
		long addr = paOtec.dajPravehoBrata(adresa);
		if (addr < 0) {
			return null;
		}
		Uzol brat = aSubor.dajZaznam(addr);
		BStromZaznam zaznam = brat.zoberMinimalnyKluc();
		paHolder.setBlok(aSubor.dajBlok().naklonuj());
		if (zaznam != null) {
			aSubor.ulozBlok();
		}
		return zaznam;
	}

	private Kluc najdiMaximalnyKluc(Uzol paUzol) throws IOException {
		while (!paUzol.jeList()) {
			long adresa = paUzol.dajPoslednuAdresu();
			paUzol = aSubor.dajZaznam(adresa);
		}
		return paUzol.dajMaximalnyKluc();
	}

	private Kluc najdiMaximalnyKluc(long addr) throws IOException {
		Uzol paUzol = aSubor.dajZaznam(addr);
		return najdiMaximalnyKluc(paUzol);
	}
}
