package asmtest2;

public class MyClassLoader extends ClassLoader {

	public Class<?> defineMyClass( byte[] b, int off, int len)
    {
        return super.defineClass("asmtest2.C", b, off, len);
    }

}
