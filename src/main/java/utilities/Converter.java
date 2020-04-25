package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Converter {
	
	private static Logger log;
	
	static {
		log = LogManager.getLogger(Converter.class);
		log.debug("Initializing Converter Class.");
		log.debug("Successfully initialized Converter Class.");
	}
	
	/**
	 * Converts a String to Integer by parsing input string.
	 * 
	 * @param  input Input string for conversion to date format.
	 * @return converted Integer
	 */
	
	public static final int convertStringToInteger(String input) {
		int index = 0;
		try {
			log.trace("Converting String to Integer.");
			index = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			log.error("Encountered NumberFormatException while converting String to Integer!");
		} catch (Exception e) {
			log.error("Encountered Exception while converting String to Integer!");
		}
		return index;
	}
	
	/**
	 * Converts a String to Date by parsing input string.
	 * 
	 * @param  stringDate input string for conversion to date format.
	 * @param  dateFormat format of the date. Follows format of
	 *                    SimpleDateFormat.
	 * @return converted Date
	 */

	public static final Date convertStringToDate(String stringDate, String dateFormat) {
		Date date = null;
		try {
			log.trace("Parsing String Date.");
			date = new SimpleDateFormat(dateFormat).parse(stringDate);
			log.trace("Successfully parsed String Date!");
		} catch (ParseException e) {
			log.error("Encountered ParseException while converting Date from String to Date Format!");
		} catch (Exception e) {
			log.error("Encountered Exception while converting Date from String to Date Format!");
		}
		return date;
	}
	
}
