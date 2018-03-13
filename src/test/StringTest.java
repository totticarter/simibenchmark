package test;

public class StringTest {

	public static void main(String[] args) {
		
		String s = "userindex";
		String[] strings = s.split(":");
		int i = s.split("/").length;
		System.out.println(i);
	}
}
