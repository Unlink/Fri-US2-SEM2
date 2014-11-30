/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.fullIndex;

import sk.uniza.fri.duracik2.blockfile.IZaznam;
import sk.uniza.fri.duracik2.bstrom.Kluc;

/**
 *
 * @author Unlink
 */
public interface IIndexovatelnyPrvok extends IZaznam {
	public Kluc[] dajKluce();
	public void nakopirujData(IIndexovatelnyPrvok paPrvok);
}
