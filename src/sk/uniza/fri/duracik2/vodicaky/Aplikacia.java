/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.util.Date;
import sk.uniza.fri.duracik2.gui.reflection.Funkcia;

/**
 *
 * @author Unlink
 */
public class Aplikacia {
	
	
	
	@Funkcia(parametre = {"Meno"})
	public String povecAhoj(String meno) {
		return "Ahoj "+meno;
	}
	
	@Funkcia(parametre = {"Datum"})
	public String bla(Date meno) {
		return "Ahoj "+meno;
	}
	
}
