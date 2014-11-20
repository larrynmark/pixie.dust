package pixie.dust;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import pixie.dust.asm.TraceTransformer;
import pixie.dust.asm.Transformer;


public class AgentTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		// skip package
		if (className.startsWith("pixie/"))
			return null;
		if (className.startsWith("org/objectweb/asm"))
			return null;

		ClassReader cr = new ClassReader(classfileBuffer);
		final ClassDesc classDesc = new ClassDesc();

		cr.accept(new ClassVisitor(Opcodes.ASM5) {
			public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
				classDesc.set(version, access, name, signature, superName, interfaces);
			}
		}, 0);

		ClassWriter cw;
		switch (classDesc.version) {
		case Opcodes.V1_1:
		case Opcodes.V1_2:
		case Opcodes.V1_3:
		case Opcodes.V1_4:
		case Opcodes.V1_5:
		case Opcodes.V1_6:
		case Opcodes.V1_7:
		case Opcodes.V1_8:
			cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			break;
		default:
			cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		}

		Transformer transformer = new TraceTransformer();
		
		ClassVisitor cv = cw;

		cv = transformer.transform(cv, className, classDesc);
		
		if (cv != cw) {
			cr.accept(cv, ClassReader.EXPAND_FRAMES);
			byte[] bytes = cw.toByteArray();
			return bytes;
		}
		
		return null;
	}

}
