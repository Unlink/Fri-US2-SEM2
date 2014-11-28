/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.blockfile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 *
 * @author Unlink
 * @param <T>
 */
public class BinarnySubor<T extends IZaznam> implements AutoCloseable {
	
	private RandomAccessFile aSubor;
	private File aCesta;
	private Blok aBuffer;
	private InfoBlok aInfoBlok;
	private BlokoveBitovePole aBitPole;
	private int aPoslednyBlok;
	
	public BinarnySubor(List<IZaznam> paStruktura, File paSubor) throws IOException {
		this(paStruktura, paSubor, -1);
	}
	
	public BinarnySubor(List<IZaznam> paStruktura, File paSubor, int paVelkostBloku) throws IOException {
		aBuffer = new Blok(paStruktura.toArray(new IZaznam[paStruktura.size()]));
		aCesta = paSubor;
		paVelkostBloku = (paVelkostBloku < 0) ? aBuffer.getVelkost() : paVelkostBloku;
		if (!paSubor.exists()) {
			vyvorNovySubor(paVelkostBloku);
		}
		else {
			nacitajZMetadat(paVelkostBloku);
		}
		aPoslednyBlok = 0;
	}

	private void vyvorNovySubor(int paVelkostBloku) throws IOException {
		aCesta.createNewFile();
		aSubor = new RandomAccessFile(aCesta, "rw");
		aInfoBlok = new InfoBlok(paVelkostBloku);
		aSubor.write(aInfoBlok.dajBajty());
		aBitPole = new BlokoveBitovePole(paVelkostBloku, paVelkostBloku);
		aSubor.seek(paVelkostBloku);
		aSubor.write(aBitPole.dajBlok(0));
	}
	
	private void nacitajZMetadat(int paVelkostBloku) throws IOException {
		aSubor = new RandomAccessFile(aCesta, "rw");
		byte[] buffer = new byte[paVelkostBloku];
		aSubor.seek(0L);
		aSubor.read(buffer);
		aInfoBlok = new InfoBlok(buffer);
		aBitPole = new BlokoveBitovePole(aInfoBlok.getPocetInfoBlokov()*paVelkostBloku, paVelkostBloku);
		//Nacitanie bitoveho pola
		for (int i = 0; i < aBitPole.dajPocetBlokov(); i++) {
			aSubor.seek(spocitajAdresuBitPola(i));
			aSubor.read(buffer);
			aBitPole.nahrajBlok(i, buffer);
		}
	}
	
	private long spocitajAdresuBitPola(int paIndex) {
		//Infoblok + x * velkost priestoru, ktorý blok pokrýva
		return aInfoBlok.getVelkostBloku() + paIndex*(aBitPole.dajPocetZaznamovVBloku()+1)*aInfoBlok.getVelkostBloku();
	}

	@Override
	public void close() throws Exception {
		aSubor.seek(0);
		aSubor.write(aInfoBlok.dajBajty());
		for (int i = 0; i < aBitPole.dajPocetBlokov(); i++) {
			aSubor.seek(spocitajAdresuBitPola(i));
			aSubor.write(aBitPole.dajBlok(i));
		}
		if (aBuffer.getAdresaBloku() > 0) {
			aSubor.seek(aBuffer.getAdresaBloku()*aInfoBlok.getVelkostBloku());
			aSubor.write(aBuffer.dajBajty());
		}
		aSubor.close();
		System.out.println("Pocet Blokov: "+aInfoBlok.getPocetBlokov());
		System.out.println("Pocet info Blokov: "+aInfoBlok.getPocetInfoBlokov());
	}
	
	public T dajVolnyZaznam() throws IOException {
		int indexBloku = najdiMiesto();
		return (T) aBuffer.dajZaznam(indexBloku);
	}
	
	public T dajZaznam(long paAdresa) throws IOException {
		long indexBloku = Blok.dajIndexBloku(paAdresa);
		int indexZaznamu = Blok.dajIndexZaznamu(paAdresa);
		if (aBuffer.getAdresaBloku() != indexBloku) {
			nacitajBlok(indexBloku);
		}
		if (aBuffer.jeValidny(indexZaznamu)) {
			return (T) aBuffer.dajZaznam(indexZaznamu);
		}
		return null;
	}
	
	/**
	 * Nájde miesto na vloženie záznamu, ak daný blok má voľné miesto, tak ho použijeme
	 * Inak načítame z disku iný, alebo vytvoríme nový
	 * @return
	 * @throws IOException 
	 */
	private int najdiMiesto() throws IOException {
		if (aBuffer.getAdresaBloku() > 0 && aBuffer.dajVolnyZaznam() >= 0) {
			return aBuffer.dajVolnyZaznam();
		}
		else {
			najdiVolnyBlok();
			return aBuffer.dajVolnyZaznam();
		}
	}
	
	private void najdiVolnyBlok() throws IOException {
		int i;
		int max = aBitPole.dajPocetZaznamov()*2;
		for (i = aPoslednyBlok; i < max; i+=2 ) {
			//Ak je blok prázdny, alebo obsahuje miesto na zaznam
			if (aBitPole.dajFlag(i) ^ aBitPole.dajFlag(i+1)) {
				nacitajBlok(realnyIndexBloku(i/2));
				aPoslednyBlok = i;
				return;
			}
			//Blok nieje alokovaný ale ešte spadá pod index blok
			else if (!aBitPole.dajFlag(i) && !aBitPole.dajFlag(i+1)) {
				long addr = realnyIndexBloku(i/2);
				alokujBlok(addr);
				aBitPole.nastavFlag(i+1, true);
				aBuffer.nastavAdresuBloku(addr);
				aPoslednyBlok = i;
				return;
			}
		}
		aPoslednyBlok = i;
		rozsirSubor();
	}

	private void alokujBlok(long addr) throws IOException {
		aSubor.setLength(aInfoBlok.getVelkostBloku()*(addr+1));
		aInfoBlok.setPocetBlokov(aInfoBlok.getPocetBlokov()+1);
	}

	private void rozsirSubor() throws IOException {
		//Nič sme nesašli v bitovom poli => rozšír pole o jeden bitový blok
		aBitPole.rozsirBlok();
		aSubor.setLength(spocitajAdresuBitPola(aBitPole.dajPocetBlokov()-1) + 2*aInfoBlok.getVelkostBloku());		
		aInfoBlok.setPocetBlokov(aInfoBlok.getPocetBlokov()+1);
		aInfoBlok.setPocetInfoBlokov(aInfoBlok.getPocetInfoBlokov()+1);
		//Zapís na disk bitový blok
		aSubor.seek(spocitajAdresuBitPola(aBitPole.dajPocetBlokov()-1));
		aSubor.write(aBitPole.dajBlok(aBitPole.dajPocetBlokov()-1));
		aBuffer.nastavAdresuBloku((spocitajAdresuBitPola(aBitPole.dajPocetBlokov()-1) + aInfoBlok.getVelkostBloku())/aInfoBlok.getVelkostBloku());
	}
	
	private long realnyIndexBloku(long paIndex) {
		return 2+paIndex+(paIndex/(aBitPole.dajPocetZaznamovVBloku()));
	}
	
	private int relativnyIndexBloku(long paIndex) {
		int index = (int) Math.ceil((aBitPole.dajPocetZaznamovVBloku()*(paIndex-2))/(double)(aBitPole.dajPocetZaznamovVBloku()+1));
		return index;
	}

	private void nacitajBlok(long indexBloku) throws IOException {
		byte[] data = new byte[aInfoBlok.getVelkostBloku()];
		aSubor.seek(indexBloku*aInfoBlok.getVelkostBloku());
		aSubor.read(data);
		aBuffer.nahraj(data, indexBloku);
	}
	
	public void ulozBlok() throws IOException {
		int index = relativnyIndexBloku(aBuffer.getAdresaBloku())*2;
		if (aBuffer.jePlny()) {
			aBitPole.nastavFlag(index, true);
			aBitPole.nastavFlag(index+1, true);
		}
		else if (aBuffer.jePrazndy()) {
			aBitPole.nastavFlag(index, false);
			aBitPole.nastavFlag(index+1, true);
			aPoslednyBlok = (aPoslednyBlok > index) ? index : aPoslednyBlok;
		}
		else {
			aBitPole.nastavFlag(index, true);
			aBitPole.nastavFlag(index+1, false);
			aPoslednyBlok = (aPoslednyBlok > index) ? index : aPoslednyBlok;
		}
		
		aSubor.seek(aBuffer.getAdresaBloku()*aInfoBlok.getVelkostBloku());
		aSubor.write(aBuffer.dajBajty());
		
		if (aBuffer.jePrazndy() && ((index/2) + 1) == aInfoBlok.getPocetBlokov()) {
			zmensiSubor(index);
		}
			
	}

	private void zmensiSubor(int paIndex) throws IOException {
		int poslednyZaznam = paIndex;
		int zmazanych = 0;
		
		for (; poslednyZaznam >= 0 && !aBitPole.dajFlag(poslednyZaznam); poslednyZaznam-= 2) {
			zmazanych++;
			aBitPole.nastavFlag(poslednyZaznam, false);
			aBitPole.nastavFlag(poslednyZaznam+1, false);
		}
		
		aInfoBlok.setPocetBlokov(aInfoBlok.getPocetBlokov()-zmazanych);
		
		int pocIndexBlokov = Math.max(1, ((aInfoBlok.getPocetBlokov()-1)/aBitPole.dajPocetZaznamovVBloku()) + 1);
		if (aInfoBlok.getPocetInfoBlokov() != pocIndexBlokov) {
			aBitPole.zmensiBlok(aInfoBlok.getPocetInfoBlokov() - pocIndexBlokov);
			aInfoBlok.setPocetInfoBlokov(pocIndexBlokov);
		}
		
		aSubor.setLength(realnyIndexBloku((poslednyZaznam)/2)*aInfoBlok.getVelkostBloku() + aInfoBlok.getVelkostBloku());
		aBuffer.nastavAdresuBloku(-1);
	}
	
}
