/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.fullIndex;

import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import sk.uniza.fri.duracik2.blockfile.BinarnySubor;
import sk.uniza.fri.duracik2.blockfile.IZaznam;
import sk.uniza.fri.duracik2.bstrom.BStrom;
import sk.uniza.fri.duracik2.bstrom.BStromZaznam;
import sk.uniza.fri.duracik2.bstrom.DuplikatnyPrvokException;
import sk.uniza.fri.duracik2.bstrom.Kluc;

/**
 *
 * @author Unlink
 * @param <T>
 */
public class UplnyIndex<T extends IIndexovatelnyPrvok> implements AutoCloseable {
	
	private BStrom[] aIndexy;
	private BinarnySubor<T> aNeutriedeny;	
	
	public UplnyIndex(T paSample, int paPocZaznamov, int[] paPocetZaznamovVIndexe, boolean paForce) throws IOException, InstantiationException, IllegalAccessException {
		String filename = paSample.getClass().getName();
		if (paForce) {
			new File(filename+".bin").delete();
			for (int i=0; i<paSample.dajKluce().length; i++) {
				new File(filename+".index"+i).delete();
			}
		}
		aNeutriedeny = new BinarnySubor<>(paSample, paPocZaznamov, new File(filename+".bin"));
		aIndexy = new BStrom[paSample.dajKluce().length];
		for (int i=0; i<paSample.dajKluce().length; i++) {
			aIndexy[i] = new BStrom(new File(filename+".index"+i), paSample.dajKluce()[i], paPocetZaznamovVIndexe[i]);
		}
	}

	public UplnyIndex(T paSample, int paPocZaznamov, int[] paPocetZaznamovVIndexe) throws IOException, InstantiationException, IllegalAccessException {
		this(paSample, paPocZaznamov, paPocetZaznamovVIndexe, true);
	}
	
	
	public long vlozPrvok(T paPrvok) throws IOException {
		//Overenie ci tu už taky nieje
		for (int i=0; i<aIndexy.length; i++) {
			if (aIndexy[i].najdi(paPrvok.dajKluce()[i]) > 0){
				throw new DuplikatnyPrvokException("Kluc "+paPrvok.dajKluce()[i]);
			}
		}
		T zaznam = aNeutriedeny.dajVolnyZaznam();
		zaznam.nakopirujData(paPrvok);
		zaznam.nastavValiditu(true);
		aNeutriedeny.ulozBlok();
		long adresa = zaznam.dajAdresu();
		for (int i=0; i<aIndexy.length; i++) {
			aIndexy[i].vloz(new BStromZaznam(paPrvok.dajKluce()[i], adresa));
		}
		return adresa;
	}
	
	public T vyhladaj(int paIndex, Kluc paKluc) throws IOException {
		long adresa = aIndexy[paIndex].najdi(paKluc);
		if (adresa < 0) {
			return null;
		}
		return aNeutriedeny.dajZaznam(adresa);
	}
	
	public T vymaz(int paIndex, Kluc paKluc) throws IOException {
		long adresa = aIndexy[paIndex].vymaz(paKluc);
		if (adresa < 0) {
			return null;
		}
		T prvok = aNeutriedeny.dajZaznam(adresa);
		for (int i=0; i<aIndexy.length; i++) {
			if (i != paIndex) {
				aIndexy[i].vymaz(prvok.dajKluce()[i]);
			}
		}
		prvok.nastavValiditu(false);
		aNeutriedeny.ulozBlok();
		try {
			T kopia = (T) prvok.getClass().newInstance();
			kopia.nakopirujData(prvok);
			return kopia;
		}
		catch (IllegalAccessException | InstantiationException ex) {
			return prvok;
		}
	}

	@Override
	public void close() throws Exception {
		for (BStrom bstrom : aIndexy) {
			bstrom.close();
		}
		aNeutriedeny.close();
	}
}
