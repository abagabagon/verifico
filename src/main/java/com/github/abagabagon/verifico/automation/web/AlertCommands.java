package com.github.abagabagon.verifico.automation.web;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

public class AlertCommands extends Commands {

	private enum AlertActions {
		ACCEPT, CANCEL, TYPE
	}

	public AlertCommands(WebDriver driver, WaitCommands wait) {
		super(driver, wait);
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
	}

	/**
	 * Accepts Javascript Alert
	 */

	public final void acceptAlert() {
		this.log.debug("Performing " + String.valueOf(AlertActions.ACCEPT).replace('_', ' ') + " Javascript Alert.");
		Alert alert = this.wait.waitForAlertToBePresent();
		alert.accept();
	}

	/**
	 * Cancels Javascript Alert
	 */

	public final void cancelAlert() {
		this.log.debug("Performing " + String.valueOf(AlertActions.CANCEL).replace('_', ' ') + " Javascript Alert.");
		Alert alert = this.wait.waitForAlertToBePresent();
		alert.dismiss();
	}

	/**
	 * Simulates typing at Javascript Alert Text Box
	 *
	 * @param inputText Text to enter.
	 */

	public final void typeAlert(String inputText) {
		this.log.debug("Performing " + String.valueOf(AlertActions.TYPE).replace('_', ' ') + " Javascript Alert.");
		Alert alert = this.wait.waitForAlertToBePresent();
		alert.sendKeys(inputText);
	}

}