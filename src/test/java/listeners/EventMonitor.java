/*******************************************************************************
 * @name    EventMonitor
 * @author  Arnel L. Bagabagon Jr.
 * @version 0.1
 * @since   02/06/2019
 * 
 * This class implements the WebDriverEventListener Interface which monitors
 * events (Click, SendKeys, etc.) performed by Selenium WebDriver and execute
 * implemented code depending on the event (mostly used for logs).
 * 
 ******************************************************************************/

package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class EventMonitor implements WebDriverEventListener {

	private static Logger log;

	public void initializeEventMonitor(EventFiringWebDriver driver) {
		driver.register(this);
		log = LogManager.getLogger(EventMonitor.class.getName());
	}
	
	@Override
	public void afterAlertAccept(WebDriver driver) { // For further improvement
		log.info("Successfully accepted alert.");
	}
	
	@Override
	public void afterAlertDismiss(WebDriver driver) { // For further improvement
		log.info("Successfully dismissed alert.");
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {

	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {

	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		log.trace("Successfully created WebElement/WebElement List.");
	}
	
	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> arg0, X arg1) {
		log.info("Successfully taken screenshot.");
	}
	
	@Override
	public void afterGetText(WebElement arg0, WebDriver arg1, String arg2) {

	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		log.info("Successfully navigated back to " + driver.getCurrentUrl());
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		log.info("Successfully navigated forward to " + driver.getCurrentUrl());
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		log.info("Successfully navigated to " + url);
	}
	
	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		log.info("Successfully refreshed " + driver.getCurrentUrl());
	}
	
	@Override
	public void afterScript(String arg0, WebDriver arg1) { // For further improvement
		log.info("Successfully executed the script: " + arg0);
	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		log.info("Successfully switched to " + windowName);
	}

	@Override
	public void beforeAlertAccept(WebDriver driver) {
		log.info("Accepting alert.");
	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		log.info("Dismissing alert.");
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		
	}
	
	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		log.trace("Creating WebElement/WebElement List.");
	}
	
	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> arg0) {
		log.info("Taking screenshot.");
	}

	@Override
	public void beforeGetText(WebElement arg0, WebDriver arg1) {
		
	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		log.info("Navigating back from " + arg0.getCurrentUrl());
	}

	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		log.info("Navigating forward from " + arg0.getCurrentUrl());
	}
	
	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		log.info("Refreshing " + driver.getCurrentUrl());
	}
	
	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		log.info("Navigating to " + arg0);
	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		log.info("Switching to " + windowName);
	}

	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		log.info("Executing the script: " + arg0);
	}

	@Override
	public void onException(Throwable arg0, WebDriver arg1) {

	}
}
