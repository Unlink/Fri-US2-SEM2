/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.testy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sk.uniza.fri.duracik2.blockfile.BitovePole;

/**
 *
 * @author Unlink
 */
public class BitovePoleTest {
	
	private BitovePole bitovePole1;
	private BitovePole bitovePole2;
	private BitovePole bitovePole3;
	
	public BitovePoleTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		bitovePole1 = new BitovePole(5);
		bitovePole2 = new BitovePole(8);
		bitovePole3 = new BitovePole(12);
	}
	
	@After
	public void tearDown() {
	}

	@Test
    public void test1() {
		bitovePole1.nastavFlag(2, true);
		bitovePole2.nastavFlag(2, true);
		bitovePole3.nastavFlag(2, true);
		
		assertTrue(bitovePole1.dajFlag(2));
		assertTrue(bitovePole2.dajFlag(2));
		assertTrue(bitovePole3.dajFlag(2));
	}
	
	@Test
    public void test2() {
		assertFalse(bitovePole1.dajFlag(1));
		assertFalse(bitovePole2.dajFlag(1));
		assertFalse(bitovePole3.dajFlag(1));
	}
	
	@Test
    public void test3() {
		bitovePole2.nastavFlag(7, true);
		bitovePole3.nastavFlag(7, true);
		
		assertTrue(bitovePole2.dajFlag(7));
		assertTrue(bitovePole3.dajFlag(7));
	}
	
	
	@Test
    public void test() {
		bitovePole3.nastavFlag(9, true);
		assertTrue(bitovePole3.dajFlag(9));
		assertFalse(bitovePole3.dajFlag(10));
	}
	
	@Test
    public void test4() {
		assertFalse(bitovePole1.dajFlag(0));
		bitovePole1.nastavFlag(0, true);
		assertTrue(bitovePole1.dajFlag(0));
		bitovePole1.nastavFlag(0, false);
		assertFalse(bitovePole1.dajFlag(0));
	}
	
	@Test
	public void test5() {
		assertEquals(1, bitovePole1.dajVelkost());
		assertEquals(1, bitovePole2.dajVelkost());
		assertEquals(2, bitovePole3.dajVelkost());
	}
	
}
