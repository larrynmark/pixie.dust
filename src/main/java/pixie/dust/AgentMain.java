package pixie.dust;

import java.lang.instrument.Instrumentation;

public class AgentMain {
	private static Instrumentation instrumentation;

	public static void premain(String options, Instrumentation i) {
		
		AgentMain.instrumentation = i;

		// Class Transform
		AgentMain.instrumentation.addTransformer(new AgentTransformer());
	}
}
