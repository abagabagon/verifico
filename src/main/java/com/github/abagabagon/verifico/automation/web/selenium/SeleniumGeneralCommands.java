package com.github.abagabagon.verifico.automation.web.selenium;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeleniumGeneralCommands {

	private Logger log;
	
	public SeleniumGeneralCommands() {
		this.log = LogManager.getLogger(this.getClass());
	}
	
	void isTextDisplayed() {
		
	}
	
	int getIndexBasedOnText() {
		return 0;
	}
	
	void isAttributeValueDisplayed() {
		
	}
	
	int getIndexBasedOnAttributeValue() {
		return 0;
	}
	
	void wait(int duration) {
		this.log.debug("I wait for " + duration + " Second(s).");
		try {
			Thread.sleep(duration * 1000);
		} catch (IllegalArgumentException e) {
			this.log.error("Encountered IllegalArgumentException while waiting for " + duration + ".");
		} catch (InterruptedException e) {
			this.log.error("Encountered InterruptedException while waiting for " + duration + ".");
		} catch (Exception e) {
			this.log.error("Encountered Exception while waiting for " + duration + ".");
			this.log.debug(ExceptionUtils.getStackTrace(e));
		}
	}
	
}