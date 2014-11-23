/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.blockfile;

/**
 *
 * @author Unlink
 */
public abstract class AZaznam implements IZaznam {

	private long aAdresa;
	private boolean aValidny;

	public AZaznam() {
		aAdresa = -1;
		aValidny = false;
	}
	
	
	@Override
	public void nastavAdresu(long paAdresa) {
		aAdresa = paAdresa;
	}

	@Override
	public long dajAdresu() {
		return aAdresa;
	}

	@Override
	public boolean jeValidny() {
		return aValidny;
	}

	@Override
	public void nastavValiditu(boolean paValidita) {
		aValidny = paValidita;
	}
}
