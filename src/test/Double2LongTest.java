package test;

import java.math.BigDecimal;

import javax.swing.plaf.synth.SynthScrollBarUI;

public class Double2LongTest {

	
public static void main(String[] args) {
	
	
//	Double d = Double.longBitsToDouble(4621819117588971520L);
	Double d = Double.longBitsToDouble(4685207112950701097L);
	BigDecimal bd = new BigDecimal(d);
	System.out.println(bd.toPlainString());
}
}
