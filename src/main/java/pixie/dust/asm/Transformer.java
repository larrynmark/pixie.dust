package pixie.dust.asm;

import org.objectweb.asm.ClassVisitor;

import pixie.dust.ClassDesc;

public interface Transformer {
	public ClassVisitor transform(ClassVisitor cv, String className,
			ClassDesc classDesc);
}
