package com.github.abagabagon.verifico.utilities;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataGenerator {
	
	private static Logger log;
	
	static {
		log = LogManager.getLogger(DataGenerator.class);
		log.debug("Initializing DataGenerator Class.");
		log.debug("Successfully initialized DataGenerator Class.");
	}
	
	public static String getCurrentDateAndTime(String dateFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		ZonedDateTime dateTime = ZonedDateTime.now();
		return dateTime.format(formatter);
	}
	
	public static String getRandomAlphanumericStrings(int length) {
		String generatedString = RandomStringUtils.random(length, true, true);
		return generatedString;
	}
	
	public static String getRandomAlphabeticStrings(int length) {
		String generatedString = RandomStringUtils.random(length, true, false);
		return generatedString;
	}
	
	public static String getRandomNumericStrings(int length) {
		String generatedString = RandomStringUtils.random(length, false, true);
		return generatedString;
	}
	
	public static String getRandomSpecialCharacterStrings(int length) {
		String generatedString = RandomStringUtils.random(length, false, false);
		return generatedString;
	}
	
	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

}
