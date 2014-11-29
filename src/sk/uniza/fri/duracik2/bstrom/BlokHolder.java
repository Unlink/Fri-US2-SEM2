/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import sk.uniza.fri.duracik2.blockfile.Blok;

/**
 *
 * @author Unlink
 */
public class BlokHolder {
	private Blok aBlok;

	public BlokHolder(Blok paBlok) {
		this.aBlok = paBlok;
	}

	public Blok getBlok() {
		return aBlok;
	}

	public void setBlok(Blok paBlok) {
		this.aBlok = paBlok;
	}
	
	
}
