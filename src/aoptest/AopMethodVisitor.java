package aoptest;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class AopMethodVisitor extends MethodVisitor implements Opcodes {
    public AopMethodVisitor(int api, MethodVisitor mv) {
        super(api, mv);
    }
    public void visitCode() {
        super.visitCode();
        this.visitMethodInsn(INVOKESTATIC, "aoptest/AopInterceptor", "beforeInvoke", "()V");
    }
    public void visitInsn(int opcode) {
        if (opcode == RETURN) {
            mv.visitMethodInsn(INVOKESTATIC, "aoptest/AopInterceptor", "afterInvoke", "()V");
        }
        super.visitInsn(opcode);
    }
}
