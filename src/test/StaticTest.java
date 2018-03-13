package test;

public class StaticTest {

	public int b = 10;
	public static int add(int a){
		
		return a+1;
	}
	
	public static void main(String[] args) {
		
		StaticTest st = new StaticTest();
		int c = StaticTest.add(st.b);
		
	}
	
}
