/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import sk.uniza.fri.duracik2.blockfile.BinarnySubor;
import sk.uniza.fri.duracik2.blockfile.Blok;

/**
 *
 * @author Unlink
 * @param <T>
 */
public class BStrom<T extends Kluc> implements AutoCloseable {
	
	private BinarnySubor<Uzol> aSubor;
	private BStromInfoBlok aInfoBlok;
	private int aStupen;

	public BStrom(File aCesta, Kluc paSampleKey, int paStupen) throws IOException, InstantiationException, IllegalAccessException {
		aInfoBlok = new BStromInfoBlok();
		aStupen = paStupen;
		Uzol uzol = new Uzol(paSampleKey, aStupen-1, true);
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
		Uzol uzol = aSubor.dajZaznam(aInfoBlok.getKoren());
		Stack<Blok> bloky = new Stack<>();
		while (!uzol.jeList()) {
			bloky.add(aSubor.dajBlok().naklonuj());
			long addresa = uzol.dalsiaAdresa(kluc);
			uzol = aSubor.dajZaznam(addresa);
		}
		if (uzol.dajAdresuKluca(kluc) != -1) {
			throw new DuplikatnyPrvokException();
		}
		
		if (uzol.maMiesto()) {
			uzol.vloz(paZaznam);
		}
		else {
			Uzol novy = aSubor.dajVolnyZaznam();
			uzol.rozdelList(paZaznam, novy);
		}
		
	}
	
}
