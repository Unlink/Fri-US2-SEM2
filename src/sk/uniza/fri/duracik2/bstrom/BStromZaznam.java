/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

/**
 *
 * @author Unlink
 */
public class BStromZaznam {
	private Kluc aKluc;
	private long aAdresa;

	public BStromZaznam(Kluc aKluc, long aAdresa) {
		this.aKluc = aKluc;
		this.aAdresa = aAdresa;
	}

	public Kluc getKluc() {
		return aKluc;
	}

	public void setKluc(Kluc aKluc) {
		this.aKluc = aKluc;
	}

	public long getAdresa() {
		return aAdresa;
	}

	public void setAdresa(long aAdresa) {
		this.aAdresa = aAdresa;
	}
	
	
}
