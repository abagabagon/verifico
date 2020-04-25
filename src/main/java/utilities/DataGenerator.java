package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataGenerator {
	
	private static Logger log;
	
	static {
		log = LogManager.getLogger(DataGenerator.class);
		log.debug("Initializing Values Class.");
		log.debug("Successfully initialized Values Class.");
	}
	
	/**
	 * Gets current System Date.
	 * 
	 * @param  dateFormat format of the date. Follows format of
	 *                    SimpleDateFormat.
	 * @return current System Date.
	 */

	public static final String getCurrentDate(String dateFormat) {
		log.trace("Getting Current Date");
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		log.trace("Retrieved Date: " + sdf.format(date).toString());
		return sdf.format(date);
	}

}
