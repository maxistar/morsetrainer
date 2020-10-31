package com.maxistar.morsetrainer;

import java.util.Map;
import java.util.TreeMap;
import com.maxistar.morsetrainer.TrainingActivity.MorseCode;

public class Constants {
	public static final Map<Character, MorseCode> latins = new TreeMap<>();
	static {
		latins.put('A', new MorseCode("·-", R.raw.a, R.string.singing_a));
		latins.put('B', new MorseCode("-···", R.raw.b, R.string.singing_b));
		latins.put('C', new MorseCode("-·-·", R.raw.c, R.string.singing_c));
		latins.put('D', new MorseCode("-··", R.raw.d, R.string.singing_d));
		latins.put('E', new MorseCode("·", R.raw.e, R.string.singing_e));
		latins.put('F', new MorseCode("··-·", R.raw.f, R.string.singing_f));
		latins.put('G', new MorseCode("--·", R.raw.g, R.string.singing_g));
		latins.put('H', new MorseCode("····", R.raw.h, R.string.singing_h));
		latins.put('J', new MorseCode("·---", R.raw.j, R.string.singing_j));
		latins.put('I', new MorseCode("··", R.raw.i, R.string.singing_i));
		latins.put('K', new MorseCode("-·-", R.raw.k, R.string.singing_k));
		latins.put('L', new MorseCode("·-··", R.raw.l, R.string.singing_l));
		latins.put('M', new MorseCode("--", R.raw.m, R.string.singing_m));
		latins.put('N', new MorseCode("-·", R.raw.n, R.string.singing_n));
		latins.put('O', new MorseCode("---", R.raw.o, R.string.singing_o));
		latins.put('P', new MorseCode("·--·", R.raw.p, R.string.singing_p));
		latins.put('Q', new MorseCode("--·-", R.raw.q, R.string.singing_q));
		latins.put('R', new MorseCode("·-·", R.raw.r, R.string.singing_r));
		latins.put('S', new MorseCode("···", R.raw.s, R.string.singing_s));
		latins.put('T', new MorseCode("-", R.raw.t, R.string.singing_t));
		latins.put('U', new MorseCode("··-", R.raw.u, R.string.singing_u));
		latins.put('V', new MorseCode("···-", R.raw.v, R.string.singing_v));
		latins.put('W', new MorseCode("·--", R.raw.w, R.string.singing_w));
		latins.put('X', new MorseCode("-··-", R.raw.x, R.string.singing_x));
		latins.put('Y', new MorseCode("-·--", R.raw.y, R.string.singing_y));
		latins.put('Z', new MorseCode("--··", R.raw.z, R.string.singing_z));
	}
	
	public static final Map<Character, MorseCode> numbers = new TreeMap<>();
	static {
		numbers.put('1', new MorseCode("·----", R.raw.r1));
		numbers.put('2', new MorseCode("··---", R.raw.r2));
		numbers.put('3', new MorseCode("···--", R.raw.r3));
		numbers.put('4', new MorseCode("····-", R.raw.r4));
		numbers.put('5', new MorseCode("·····", R.raw.r5));
		numbers.put('6', new MorseCode("-····", R.raw.r6));
		numbers.put('7', new MorseCode("--···", R.raw.r7));
		numbers.put('8', new MorseCode("---··", R.raw.r8));
		numbers.put('9', new MorseCode("----·", R.raw.r9));
		numbers.put('0', new MorseCode("-----", R.raw.r0));		
	}
	
	public static final Map<Character, MorseCode> characters = new TreeMap<>();
	static {
		characters.put('.', new MorseCode("·-·-·-", R.raw.point));
		characters.put(':', new MorseCode("---···", R.raw.column));
		characters.put(',', new MorseCode("--··--", R.raw.coma));
		characters.put(';', new MorseCode("-·-·-·", R.raw.semicolumn));
		characters.put('?', new MorseCode("··--··", R.raw.question));
		characters.put('=', new MorseCode("-···-", R.raw.equals));
		characters.put('\'', new MorseCode("·----·", R.raw.rus_apostrof));
		characters.put('+', new MorseCode("·-·-·", R.raw.plus));
		characters.put('!', new MorseCode("-·-·--", R.raw.exclamation));
		characters.put('-', new MorseCode("-····-", R.raw.minus));
		characters.put('/', new MorseCode("-··-·", R.raw.slash));
		characters.put('_', new MorseCode("··--·-", R.raw.understroke));
		characters.put('(', new MorseCode("-·--·", R.raw.opening_par));
		characters.put('"', new MorseCode("·-··-·", R.raw.quote));
		characters.put(')', new MorseCode("-·--·-", R.raw.closing_par));
		characters.put('$', new MorseCode("···-··-", R.raw.dollar));
		characters.put('&', new MorseCode("·-···", R.raw.and));
		characters.put('@', new MorseCode("·--·-·", R.raw.at));		
	}

	public static final Map<Character, MorseCode> cyrilics = new TreeMap<>();
	static {
		cyrilics.put('А', new MorseCode("·-", R.raw.rus_a, R.string.cyr_singing_a));
		cyrilics.put('Б', new MorseCode("-···", R.raw.rus_b, R.string.cyr_singing_b));
		cyrilics.put('В', new MorseCode("·--", R.raw.rus_v, R.string.cyr_singing_w));
		cyrilics.put('Г', new MorseCode("--·", R.raw.rus_g, R.string.cyr_singing_g));
		cyrilics.put('Д', new MorseCode("-··", R.raw.rus_d, R.string.cyr_singing_d));
		cyrilics.put('Е', new MorseCode("·", R.raw.rus_e, R.string.cyr_singing_e));
		cyrilics.put('Ё', new MorseCode("·", R.raw.rus_oe, R.string.cyr_singing_e));
		cyrilics.put('Ж', new MorseCode("···-", R.raw.rus_j, R.string.cyr_singing_v));
		cyrilics.put('З', new MorseCode("--··", R.raw.rus_z, R.string.cyr_singing_z));
		cyrilics.put('И', new MorseCode("··", R.raw.rus_i, R.string.cyr_singing_i));
		cyrilics.put('Й', new MorseCode("·---", R.raw.rus_ii, R.string.cyr_singing_j));
		cyrilics.put('К', new MorseCode("-·-", R.raw.rus_k, R.string.cyr_singing_k));
		cyrilics.put('Л', new MorseCode("·-··", R.raw.rus_l, R.string.cyr_singing_l));
		cyrilics.put('М', new MorseCode("--", R.raw.rus_m, R.string.cyr_singing_m));
		cyrilics.put('Н', new MorseCode("-·", R.raw.rus_n, R.string.cyr_singing_n));
		cyrilics.put('О', new MorseCode("---", R.raw.rus_o, R.string.cyr_singing_o));
		cyrilics.put('П', new MorseCode("·--·", R.raw.rus_p, R.string.cyr_singing_p));
		cyrilics.put('Р', new MorseCode("·-·", R.raw.rus_r, R.string.cyr_singing_r));
		cyrilics.put('С', new MorseCode("···", R.raw.rus_s, R.string.cyr_singing_s));
		cyrilics.put('Т', new MorseCode("-", R.raw.rus_t, R.string.cyr_singing_t));
		cyrilics.put('У', new MorseCode("··-", R.raw.rus_u, R.string.cyr_singing_u));
		cyrilics.put('Ф', new MorseCode("··-·", R.raw.rus_f, R.string.cyr_singing_f));
		cyrilics.put('Х', new MorseCode("····", R.raw.rus_h, R.string.cyr_singing_h));
		cyrilics.put('Ц', new MorseCode("-·-·", R.raw.rus_c, R.string.cyr_singing_c));
		cyrilics.put('Ч', new MorseCode("---·", R.raw.rus_ch, R.string.cyr_singing_ch));
		cyrilics.put('Ш', new MorseCode("----", R.raw.rus_sh, R.string.cyr_singing_sh));
		cyrilics.put('Щ', new MorseCode("--·-", R.raw.rus_sch, R.string.cyr_singing_q));
		cyrilics.put('Ъ', new MorseCode("--·--", R.raw.rus_tz, R.string.cyr_singing_tvznk));
		cyrilics.put('Ы', new MorseCode("-·--", R.raw.rus_y, R.string.cyr_singing_y));
		cyrilics.put('Ь', new MorseCode("-··-", R.raw.rus_mz, R.string.cyr_singing_x));
		cyrilics.put('Э', new MorseCode("··-··", R.raw.rus_ee, R.string.cyr_singing_ee));
		cyrilics.put('Ю', new MorseCode("··--", R.raw.rus_yu, R.string.cyr_singing_yu));
		cyrilics.put('Я', new MorseCode("·-·-", R.raw.rus_ya, R.string.cyr_singing_ya));
	}
}
