package pixie.dust.trace;

public class RedisTracer {
	public static void send(Object obj, byte[]... bs) {
		StringBuilder sb = new StringBuilder();
		sb.append("[### PIXIE DUST - REDIS ### ");
		sb.append(obj);
		sb.append(" ");
		
		for(byte[] b : bs) {
			sb.append(new String(b) + " ");
		}
		
		System.out.println(sb);		
	}
}
