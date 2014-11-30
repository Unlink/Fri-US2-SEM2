/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.uniza.fri.duracik2.vodicaky;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author Unlink
 */
public class Generator {

	static final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
	/**
	 * Zdroj:
	 * http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
	 *
	 * @param len
	 * @return
	 */
	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(rg.nextInt(AB.length())));
		}
		return sb.toString();
	}
	
	public static String randomNumericString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(rg.nextInt(10));
		}
		return sb.toString();
	}

	public static long randomBetween(long start, long end) {
		return start + (long) Math.round(rg.nextDouble() * (end - start));
	}

	public static int randomBetween(int start, int end) {
		return start + (int) Math.round(rg.nextDouble() * (end - start));
	}

	public static int randomBetweenAndNot(int start, int end, int not) {
		int res = not;
		while (res == not) {
			res = (int) randomBetween(start, end);
		}
		return res;
	}

	public static long days(int days) {
		return days * 24 * 60 * 60 * 1000L;
	}

	/**
	 * + pol roka od zaciatku
	 *
	 * @param date
	 * @return
	 */
	public static Date randomDate(long date) {
		if (date == 0) {
			date = System.currentTimeMillis();
		}
		return randomDate(date, date + days(150));
	}

	public static Date randomDate(long date, long date2) {
		return new Date(randomBetween(date, date2));
	}
	
	public static Date randomDate() {
		return randomDate(0);
	}

	public static Random rg = new Random();
}
