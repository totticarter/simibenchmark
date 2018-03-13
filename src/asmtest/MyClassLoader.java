package asmtest;

public class MyClassLoader extends ClassLoader {

	public Class<?> defineMyClass( byte[] b, int off, int len)
    {
        return super.defineClass("TestBean_Tmp", b, off, len);
    }

}
