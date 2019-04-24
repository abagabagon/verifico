/*******************************************************************************
 * @name    Utilities
 * @author  Arnel L. Bagabagon Jr.
 * @version 0.1
 * @since   02/06/2019
 * 
 * This Class contains all common methods that support testing and development
 * of other methods throughout the framework.
 * 
 ******************************************************************************/

package selenium;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utilities {
	
	private static Logger log;
	
	static {
		log = LogManager.getLogger(Utilities.class);
	}
	
	/**
	 * Verifies if E-Mail has a valid format.
	 * 
	 * @param  eMail input E-Mail
	 * @return <b>true</b> if E-Mail has a valid format.<br>
	 *         <b>false</b> if E-Mail has a invalid format.
	 */

	public static boolean verifyValidEMail(String eMail) {
		boolean result = true;
		try {
			InternetAddress ia = new InternetAddress(eMail);
			ia.validate();
		} catch (AddressException ex) {
			log.error("Encountered AddressException while validating E-Mail: " + eMail + ".");
			result = false;
		}
		return result;
	}
	
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
	
	/**
	 * Gets current System Date.
	 * 
	 * @param  dateFormat format of the date. Follows format of
	 *                    SimpleDateFormat.
	 * @return current System Date.
	 */

	public static final String getCurrentDate(String dateFormat) {
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		return sdf.format(date);
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
