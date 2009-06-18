package br.ufpb.iti.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import br.ufpb.iti.Huffman;

public class Testing {

	public static String currentTime() {
		long time = System.currentTimeMillis();
		Calendar.getInstance().setTimeInMillis(time);
		int hour = Calendar.getInstance().get(Calendar.HOUR);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		int second = Calendar.getInstance().get(Calendar.SECOND);
		int mili = Calendar.getInstance().get(Calendar.MILLISECOND);
		String out = hour+":"+minute+":"+second+"."+mili;
		System.out.println("Time: "+out);
		return out;
	}
	
	@Test
	public void divideCode() {
		String code = "00010101";
		String[] result = Huffman.divideCode(code, 3);
		assertTrue(result[0].equals("000"));
		assertTrue(result[1].equals("10101"));
	}
	
	@Test
	public void getByteFromString() {
		int decimal = Huffman.getByte("11111111");
		assertEquals(255, decimal);
	}
	
	@Test
	public void getBinaryFromDeciaml() {
		assertEquals("1001100111011000101101101111", Huffman.getCode(161319791));
	}
	
	@Test
	public void getFormatedBinary1() {
		assertEquals("00000000000000001010", Huffman.getFormatedCode(10, 20));
	}
	
	@Test
	public void getFormatedBinary2() {
		assertEquals("01111111111111111111111111111111", Huffman.getFormatedCode(2147483647, 32));
	}
	
	@Test
	public void getFormatedBinary3() {
		assertEquals("00000000000000100100010000000001", Huffman.getFormatedCode(148481, 32));
	}
	
	public void alteraString(String teste) {
		teste = "nada";
		
	}
	
	@Test
	public void testeGravaELe() {
		try {
			String absolutePath = "/media/Documentos/workspace/linux/mopa/Huffman/bin/files/alice29.adh";
			
			int number = 8;
			
			FileInputStream fReader = new FileInputStream(absolutePath);
			BufferedInputStream buffReader = new BufferedInputStream(fReader);  
			DataInputStream dataIn = new DataInputStream(buffReader);  
			
			int result = dataIn.read();
			
			assertEquals(number, result);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void getByte() {
		Huffman.getByte("01010010");
	}
	
	@Test
	public void getFormatedCode(){
		Huffman.getFormatedCode(-140, 8);
	}
	
	@Test
	public void inverteBits(){
		String result = Huffman.inverteBits("01010101");
		assertEquals("10101010", result);
	}
	
	@Test
	public void removeUm(){
		String result = Huffman.subtraiUm("01010101");
		assertEquals("01010100", result);
	}
	
	
	
	
}
