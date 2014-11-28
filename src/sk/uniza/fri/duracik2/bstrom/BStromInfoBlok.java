/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.bstrom;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import sk.uniza.fri.duracik2.blockfile.InfoBlok;

/**
 *
 * @author Unlink
 */
public class BStromInfoBlok extends InfoBlok {

	private long aKoren;

	@Override
	public byte[] dajBajty() {		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(getVelkostBloku());
		DataOutputStream output = new DataOutputStream(byteArrayOutputStream);
		try {
			output.writeLong(aKoren);
			output.write(super.dajBajty());
		}
		catch (IOException ex) {
		}
		return byteArrayOutputStream.toByteArray();
	}

	@Override
	public void inicializuj(int paVelksotBloku) {
		super.inicializuj(paVelksotBloku);
		aKoren = -1;
	}

	@Override
	public void deserializuj(byte[] data) {
		super.deserializuj(Arrays.copyOfRange(data, 8, data.length));
		aKoren = ByteBuffer.wrap(data, 0, 8).getLong();
	}
	
	public long getKoren() {
		return aKoren;
	}

	public void setKoren(long aKoren) {
		this.aKoren = aKoren;
	}
	
}
