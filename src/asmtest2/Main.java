package asmtest2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

//import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import asmtest2.MyClassLoader;



public class Main {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        try {
            ClassReader cr = new ClassReader("asmtest2/C");
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classVisitor = new AddTimeClassVistor(cw);
            cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC, "count", "I", null, new Integer(1000));
            cw.visitMethod(Opcodes.ACC_PUBLIC, "printCount", "(Ljava/lang/String;)S", null, null);
            cr.accept(classVisitor, ClassReader.SKIP_DEBUG);
            byte[] data = cw.toByteArray();
            File file = new File("Ccc.class");
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();
            System.out.println("success!");


            MyClassLoader loader = new MyClassLoader();
            Class clazz = loader.defineMyClass( data, 0, data.length);
            System.out.println(clazz.getCanonicalName());

            Object o;
			o = clazz.newInstance();
			clazz.getMethod("m", null).invoke(o, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
