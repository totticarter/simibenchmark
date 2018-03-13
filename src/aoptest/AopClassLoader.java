package aoptest;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import asmtest.MyClassLoader;

class AopClassLoader extends ClassLoader implements Opcodes {
    public AopClassLoader(ClassLoader parent) {
        super(parent);
    }
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (!name.contains("TestBean_Tmp"))
            return super.loadClass(name);
        try {
            ClassWriter cw = new ClassWriter(0);
            //
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("aoptest/TestBean.class");
            ClassReader reader = new ClassReader(is);
            reader.accept(new AopClassVisitor(ASM5, cw), ClassReader.SKIP_DEBUG);
            //
            byte[] code = cw.toByteArray();
//                        FileOutputStream fos = new FileOutputStream("/Users/waixingren/bigdata-java/test/bin/aoptest/TestBean_Tmp.class");
//                        fos.write(code);
//                        fos.flush();
//                        fos.close();

            ////////////////////////////////////////////////////////////////////
            MyClassLoader loader = new MyClassLoader();
            Class clazz = loader.defineMyClass( code, 0, code.length);
            System.out.println(clazz.getCanonicalName());
            //////////////////////////////////////////////////////////////////////////


            name = "TestBean_Tmp";
            return this.defineClass(name, code, 0, code.length);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }
}
