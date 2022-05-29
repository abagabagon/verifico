package com.github.abagabagon.verifico.automation.web.selenium;

import java.time.Duration;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Commands {

	private Logger log;
	private WebDriver driver;
	private WaitCommands wait;
	
	public Commands(WebDriver driver, WaitCommands wait) {
		this.log = LogManager.getLogger(this.getClass());
		this.driver = driver;
		this.wait = wait;
	}
	
	protected int count(By locator) {
		this.wait.waitForPage();
		this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		List<WebElement> element = this.driver.findElements(locator);
		int size = element.size();
		this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		return size;
	}
	
	protected void wait(int duration) {
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