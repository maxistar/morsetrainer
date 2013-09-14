package com.maxistar.morsetrainer;

import java.util.Map;
import java.util.TreeMap;

import com.maxistar.morsetrainer.TrainingActivity.MorseCode;


public class Constants {
	public static final Map<Character, MorseCode> latins = new TreeMap<Character, MorseCode>();
	static {
		latins.put('A', new MorseCode("·-", R.raw.a));
		latins.put('B', new MorseCode("-···", R.raw.b));
		latins.put('C', new MorseCode("-·-·", R.raw.c));
		latins.put('D', new MorseCode("-··", R.raw.d));
		latins.put('E', new MorseCode("·", R.raw.e));
		latins.put('F', new MorseCode("··-·", R.raw.f));
		latins.put('G', new MorseCode("--·", R.raw.g));
		latins.put('H', new MorseCode("····", R.raw.h));
		latins.put('J', new MorseCode("·---", R.raw.j));
		latins.put('I', new MorseCode("··", R.raw.i));
		latins.put('K', new MorseCode("-·-", R.raw.k));
		latins.put('L', new MorseCode("·-··", R.raw.l));
		latins.put('M', new MorseCode("--", R.raw.m));
		latins.put('N', new MorseCode("-·", R.raw.n));
		latins.put('O', new MorseCode("---", R.raw.o));
		latins.put('P', new MorseCode("·--·", R.raw.p));
		latins.put('Q', new MorseCode("--·-", R.raw.q));
		latins.put('R', new MorseCode("·-·", R.raw.r));
		latins.put('S', new MorseCode("···", R.raw.s));
		latins.put('T', new MorseCode("-", R.raw.t));
		latins.put('U', new MorseCode("··-", R.raw.u));
		latins.put('V', new MorseCode("···-", R.raw.v));
		latins.put('W', new MorseCode("·--", R.raw.w));
		latins.put('X', new MorseCode("-··-", R.raw.x));
		latins.put('Y', new MorseCode("-·--", R.raw.y));
		latins.put('Z', new MorseCode("--··", R.raw.z));
	}
	
	public static final Map<Character, MorseCode> numbers = new TreeMap<Character, MorseCode>();
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
	
	public static final Map<Character, MorseCode> characters = new TreeMap<Character, MorseCode>();
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

	public static final Map<Character, MorseCode> cyrilics = new TreeMap<Character, MorseCode>();
	static {
		cyrilics.put('А', new MorseCode("·-", R.raw.rus_a));
		cyrilics.put('Б', new MorseCode("-···", R.raw.rus_b));
		cyrilics.put('В', new MorseCode("·--", R.raw.rus_v));
		cyrilics.put('Г', new MorseCode("--·", R.raw.rus_g));
		cyrilics.put('Д', new MorseCode("-··", R.raw.rus_d));
		cyrilics.put('Е', new MorseCode("·", R.raw.rus_e));
		cyrilics.put('Ё', new MorseCode("·", R.raw.rus_oe));
		cyrilics.put('Ж', new MorseCode("···-", R.raw.rus_j));
		cyrilics.put('З', new MorseCode("--··", R.raw.rus_z));
		cyrilics.put('И', new MorseCode("··", R.raw.rus_i));
		cyrilics.put('Й', new MorseCode("·---", R.raw.rus_ii));
		cyrilics.put('К', new MorseCode("-·-", R.raw.rus_k));
		cyrilics.put('Л', new MorseCode("·-··", R.raw.rus_l));
		cyrilics.put('М', new MorseCode("--", R.raw.rus_m));
		cyrilics.put('Н', new MorseCode("-·", R.raw.rus_n));
		cyrilics.put('О', new MorseCode("---", R.raw.rus_o));
		cyrilics.put('П', new MorseCode("·--·", R.raw.rus_p));
		cyrilics.put('Р', new MorseCode("·-·", R.raw.rus_r));
		cyrilics.put('С', new MorseCode("···", R.raw.rus_s));
		cyrilics.put('Т', new MorseCode("-", R.raw.rus_t));
		cyrilics.put('У', new MorseCode("··-", R.raw.rus_u));
		cyrilics.put('Ф', new MorseCode("··-·", R.raw.rus_f));
		cyrilics.put('Х', new MorseCode("····", R.raw.rus_h));
		cyrilics.put('Ц', new MorseCode("-·-·", R.raw.rus_c));
		cyrilics.put('Ч', new MorseCode("---·", R.raw.rus_ch));
		cyrilics.put('Ш', new MorseCode("----", R.raw.rus_sh));
		cyrilics.put('Щ', new MorseCode("--·-", R.raw.rus_sch));
		cyrilics.put('Ъ', new MorseCode("--·--", R.raw.rus_tz));
		cyrilics.put('Ы', new MorseCode("-·--", R.raw.rus_y));
		cyrilics.put('Ь', new MorseCode("-··-", R.raw.rus_mz));
		cyrilics.put('Э', new MorseCode("··-··", R.raw.rus_ee));
		cyrilics.put('Ю', new MorseCode("··--", R.raw.rus_yu));
		cyrilics.put('Я', new MorseCode("·-·-", R.raw.rus_ya));
		
	}
}
