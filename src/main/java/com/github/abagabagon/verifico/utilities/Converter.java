package com.github.abagabagon.verifico.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Converts data types
 * 
 * @author albagabagon
 *
 */

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
			log.error("Encountered ParseException while converting String to Date Format!");
		} catch (Exception e) {
			log.error("Encountered Exception while converting String to Date Format!");
		}
		return date;
	}
	
	/**
	 * Converts a Date to String by specified format.
	 * 
	 * @param  date			Date that will be converted to String.
	 * @param  dateFormat	Format of the date.
	 * @return Converted Date
	 */

	public static final String convertDateToString(Date date, String dateFormat) {
		String dateString = null;
		try {
			log.trace("Parsing String Date.");
			DateFormat format = new SimpleDateFormat(dateFormat);
			dateString = format.format(date);
			log.trace("Successfully parsed String Date!");
		} catch (Exception e) {
			log.error("Encountered Exception while converting Date to String!");
		}
		return dateString;
	}
	
	/**
	 * Converts a Month String (e. g. "Sep") to its integer equivalent.
	 * 
	 * @param  month		Month String that will be converted to integer.
	 * @param  monthFormat	Format of Month String.
	 * @return Month in integer
	 */

	public static final int convertMonthStringToInteger(String month, String monthFormat) {
		Date date = null;
		try {
			date = new SimpleDateFormat(monthFormat, Locale.ENGLISH).parse(month);
		} catch (ParseException e) {
			log.error("Encountered ParseException while converting String to Date Format!");
		} catch (Exception e) {
			log.error("Encountered Exception while converting String to Date Format!");
		}
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date); 
		return cal.get(Calendar.MONTH) + 1;
	}
	
}
