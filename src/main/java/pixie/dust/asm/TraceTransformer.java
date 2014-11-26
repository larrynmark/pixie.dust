package pixie.dust.asm;

import java.util.HashSet;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import pixie.dust.ClassDesc;


public class TraceTransformer implements Transformer {

	public static HashSet<String> target = new HashSet<String>();
	
	static {
		// TODO Enter the desired package name for analysis
		target.add("redis/clients/jedis");
	}

	public ClassVisitor transform(ClassVisitor cv, String className, ClassDesc classDesc) {

		for(String targetName : target) {
			if (className.startsWith(targetName)) {
				return new TraceVisitor(className, cv);
			}
		}

/*		
		if (target.contains(className)) {
			return new TraceVisitor(className, cv);
		}
*/		
		return cv;
	}

}

class TraceVisitor extends ClassVisitor {
	private String className;

	public TraceVisitor(String className, ClassVisitor cv) {
		super(Opcodes.ASM4, cv);
		this.className = className;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

		if (mv != null) {
			mv = new TraceMethodAdapter(className, mv, access, name, desc);
		}

		return mv;
	}
}

class TraceMethodAdapter extends AdviceAdapter {
	private String mname;
	private String cname;

	public TraceMethodAdapter(String className, MethodVisitor mv, int acc, String name, String desc) {
		super(ASM4, mv, acc, name, desc);
		this.cname = className;
		this.mname = name;
	}

	@Override
	public void onMethodExit(int opcode) {
		mv.visitCode();

		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("[### PIXIE DUST ### " + cname + " " + mname + " " + this.methodDesc + " " + opcode);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(maxStack + 2, maxLocals + 6);
	}
}