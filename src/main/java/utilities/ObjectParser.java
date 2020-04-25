package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObjectParser {
	
	private static Logger log;
	
	static {
		log = LogManager.getLogger(ObjectParser.class);
		log.debug("Initializing Object Parser Class.");
		log.debug("Successfully initialized Object Parser Class.");
	}
	
	public static final String[] parseObjectArrayToStringArray(Object[] object) {
		int objectCount = object.length;
		String[] parsed = new String[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = object[i].toString();
			log.trace("Successfully parsed Object to String: " + parsed[i]);
		}
		return parsed;
	}
	
	public static final int[] parseObjectArrayToIntArray(Object[] object) {
		int objectCount = object.length;
		int[] parsed = new int[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = Integer.parseInt(object[i].toString());
			log.trace("Successfully parsed Object to Integer: " + parsed[i]);
		}
		return parsed;
	}
	
	public static final double[] parseObjectArrayToDoubleArray(Object[] object) {
		int objectCount = object.length;
		double[] parsed = new double[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = Double.parseDouble(object[i].toString());
			log.trace("Successfully parsed Object to Double: " + parsed[i]);
		}
		return parsed;
	}
	
	public static final boolean[] parseObjectArrayToBooleanArray(Object[] object) {
		int objectCount = object.length;
		boolean[] parsed = new boolean[objectCount];
		for (int i = 0; i < objectCount; i++) {
			parsed[i] = Boolean.parseBoolean(object[i].toString());
			log.trace("Successfully parsed Object to Boolean: " + parsed[i]);
		}
		return parsed;
	}
}
