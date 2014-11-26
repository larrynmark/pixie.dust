package pixie.dust;

import org.apache.commons.lang3.time.FastDateFormat;

public class Logger {
	private static FastDateFormat time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public static void println(String id, String message) {
		System.out.println(time.format(System.currentTimeMillis()) + " [PIXIE] [" + id + "] " + message);
	}
	
	public static void println(String message) {
		System.out.println(time.format(System.currentTimeMillis()) + " [PIXIE] " + message);
	}
}