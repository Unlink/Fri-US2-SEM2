/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.blockfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Unlink
 */
public class InfoBlok {

	private int aPocetZaznamov;
	private int aVelkostBloku;
	private int aPocetBlokov;
	private int aPocetInfoBlokov;

	public InfoBlok() {
	}

	public void deserializuj(byte[] data) {
		DataInputStream input = new DataInputStream(new ByteArrayInputStream(data));
		try {
			aPocetZaznamov = input.readInt();
			aVelkostBloku = input.readInt();
			aPocetBlokov = input.readInt();
			aPocetInfoBlokov = input.readInt();
		}
		catch (IOException ex) {
		}
	}

	public void inicializuj(int paVelksotBloku) {
		this.aVelkostBloku = paVelksotBloku;
		aPocetZaznamov = 0;
		aPocetBlokov = 0;
		aPocetInfoBlokov = 1;
	}

	public int getPocetZaznamov() {
		return aPocetZaznamov;
	}

	public void setPocetZaznamov(int aPocetZaznamov) {
		this.aPocetZaznamov = aPocetZaznamov;
	}

	public void setVelkostBloku(int aVelkostBloku) {
		this.aVelkostBloku = aVelkostBloku;
	}

	public int getPocetBlokov() {
		return aPocetBlokov;
	}

	public void setPocetBlokov(int aPocetBlokov) {
		this.aPocetBlokov = aPocetBlokov;
	}

	public int getVelkostBloku() {
		return aVelkostBloku;
	}

	public byte[] dajBajty() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(aVelkostBloku);
		DataOutputStream output = new DataOutputStream(byteArrayOutputStream);
		try {
			output.writeInt(aPocetZaznamov);
			output.writeInt(aVelkostBloku);
			output.writeInt(aPocetBlokov);
			output.writeInt(aPocetInfoBlokov);
		}
		catch (IOException ex) {
		}
		return byteArrayOutputStream.toByteArray();
	}

	public int getPocetInfoBlokov() {
		return aPocetInfoBlokov;
	}

	public void setPocetInfoBlokov(int aPocetInfoBlokov) {
		this.aPocetInfoBlokov = aPocetInfoBlokov;
	}

}
