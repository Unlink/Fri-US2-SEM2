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
			return -5;
		}
		LinkedList<Blok> bloky = traverzujStrom(kluc);
		Uzol uzol = (Uzol) bloky.getLast().dajZaznam(0);
		long adresaKluca = uzol.dajAdresuKluca(kluc);
		if (adresaKluca == Uzol.KLUC_NENAJDENY) {
			return -1;
		}

		//Ak je v liste dosť prvkov, tak zmažeme a koniec
		if (uzol.dajAdresu() == aInfoBlok.getKoren() || uzol.maDostatokPrvkov()) {
			BStromZaznam zaznam = uzol.odoberKlucZListu(kluc);
			//Ak sme vyprazdnili koreň
			if (uzol.dajAdresu() == aInfoBlok.getKoren() && uzol.getPocetPlatnychKlucov() == 0) {
				zmazKoren(bloky.removeLast());
				return adresaKluca;
			}
			else {
				aSubor.nastavBlok(bloky.removeLast());
				aSubor.ulozBlok();
			}
			if (zaznam != null) {
				zmenKluce(bloky, zaznam, kluc);
			}
			return adresaKluca;
		}
		
		
		
		return 1;
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

	private void zmenKluce(LinkedList<Blok> bloky, BStromZaznam zaznam, Kluc kluc) throws IOException {
		while (bloky.size() > 0) {
			Blok blok = bloky.removeLast();
			Uzol uzol = (Uzol) blok.dajZaznam(0);
			if (uzol.nahradKluc(kluc, zaznam.getKluc())) {
				aSubor.nastavBlok(blok);
				aSubor.ulozBlok();
			}
		}
	}

}
