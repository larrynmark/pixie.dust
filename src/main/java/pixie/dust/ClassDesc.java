package pixie.dust;

import java.util.Arrays;

public class ClassDesc {
	public int version;
	public int access;
	public String name;
	public String signature;
	public String superName;
	public String[] interfaces;

	public void set(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.version = version;
		this.access = access;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
	}

	@Override
	public String toString() {
		return "ClassDesc [version=" + version + ", access=" + access + ", name=" + name + ", signature=" + signature + ", superName=" + superName + ", interfaces=" + Arrays.toString(interfaces)
				+ "]";
	}

}
