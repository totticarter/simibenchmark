package asmtest2;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;



public class AddTimeClassVistor extends ClassVisitor {
    private String owner;
    private boolean isInterface;
    public AddTimeClassVistor(ClassVisitor cv) {
        super(327680, cv);
    }
    @Override
    public void visit(int version, int access, String name, String signature,String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if(!name.equals("<init>") && !isInterface && mv!=null){
            //为方法添加计时功能
            mv = new AddTimeMethodVistor(mv);
        }
        return mv;
    }
    @Override
    public FieldVisitor visitField(int arg0, String arg1, String arg2, String arg3, Object arg4) {
    	return super.visitField(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public void visitEnd() {
        //添加字段
        if(!isInterface){
            FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "timer", "J", null, null);
            if(fv!=null){
                fv.visitEnd();
            }
        }
        cv.visitEnd();
    }

    class AddTimeMethodVistor extends MethodVisitor{
        public AddTimeMethodVistor(MethodVisitor mv) {
            super(327680,mv);
        }
        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
            mv.visitInsn(Opcodes.LSUB);
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J");
        }
        @Override
        public void visitInsn(int opcode) {
            if((opcode>=Opcodes.IRETURN && opcode<=Opcodes.RETURN) || opcode==Opcodes.ATHROW){
                mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J");
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
                mv.visitInsn(Opcodes.LADD);
                mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J");
            }
            mv.visitInsn(opcode);
        }
        @Override
        public void visitMaxs(int maxStack, int maxLocal) {
            mv.visitMaxs(maxStack+4, maxLocal);
        }
    }

}
