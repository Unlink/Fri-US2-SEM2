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
public interface Kluc extends Comparable<Kluc> {
	public void nahraj(byte[] paStream);

	public void serializuj(byte[] stream);

	public Kluc naklonuj();

	public int dajVelkost();
}
