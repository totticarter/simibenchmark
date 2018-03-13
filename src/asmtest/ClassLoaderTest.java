package asmtest;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;  
  
public class ClassLoaderTest {  
  
    public static void main(String[] args) throws IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {  
        //读取本地的class文件内的字节码，转换成字节码数组  
//        File file = new File(".");  
        InputStream  input = new FileInputStream("/Users/waixingren/Programmer2.class");  
        byte[] result = new byte[1024];  
          
        int count = input.read(result);  
        // 使用自定义的类加载器将 byte字节码数组转换为对应的class对象  
        MyClassLoader loader = new MyClassLoader();  
        Class clazz = loader.defineMyClass( result, 0, count);  
        //测试加载是否成功，打印class 对象的名称  
        System.out.println(clazz.getCanonicalName());  
                  
               //实例化一个Programmer对象  
        Object o;
		try {
			o = clazz.newInstance();

			clazz.getMethod("code", null).invoke(o, null);  
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
                   //调用Programmer的code方法  

 }  
} 
