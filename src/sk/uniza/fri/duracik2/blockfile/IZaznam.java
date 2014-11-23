/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.blockfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author Unlink
 */
public interface IZaznam {
	public void nastavAdresu(long paAdresa);
	public long dajAdresu();
	
	public boolean jeValidny();
	public void nastavValiditu(boolean paValidita);
	
	public int dajVelkost();
	public void nahraj(DataInputStream paStream);
	public void serializuj(DataOutputStream stream);
	
	public IZaznam naklonuj();
}
