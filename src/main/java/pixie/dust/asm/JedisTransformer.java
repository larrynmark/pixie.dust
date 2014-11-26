package pixie.dust.asm;

import java.util.HashSet;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import pixie.dust.ClassDesc;
import pixie.dust.Logger;
import pixie.dust.trace.RedisTracer;

public class JedisTransformer implements Transformer, Opcodes {
	public static final HashSet<String> target = new HashSet<String>();

	static {
		target.add("redis/clients/jedis/Connection");
	}

	public ClassVisitor transform(ClassVisitor cv, String className, ClassDesc classDesc) {
		if (target.contains(className) == false) {
			return cv;
		}
		Logger.println("redis connection found: " + className);
		
		return new JedisConnectionCV(className, cv);
	}
}

class JedisConnectionCV extends ClassVisitor implements Opcodes {

	private String className;

	public JedisConnectionCV(String className, ClassVisitor cv) {
		super(ASM5, cv);
		this.className = className;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		if (name.equals("sendCommand") && desc.equals("(Lredis/clients/jedis/Protocol$Command;[[B)Lredis/clients/jedis/Connection;"))
			mv = new JedisConnectionMethodAdapter(className, mv, access, name, desc);
		return mv;
	}

}

class JedisConnectionMethodAdapter extends AdviceAdapter {

	public JedisConnectionMethodAdapter(String className, MethodVisitor mv, int acc, String name, String desc) {
		super(ASM5, mv, acc, name, desc);
	}

	@Override
	public void onMethodExit(int opcode) {
		mv.visitCode();

		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKESTATIC, RedisTracer.class.getName().replace('.', '/'), "send", "(Ljava/lang/Object;[[B)V", false);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(maxStack + 2, maxLocals + 3);
	}
}