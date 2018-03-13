package aoptest;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {

		AopClassLoader loader = new AopClassLoader(null);
		Class myClass = loader.loadClass("TestBean_Tmp");
		System.out.println(myClass.getCanonicalName());
	}
}
