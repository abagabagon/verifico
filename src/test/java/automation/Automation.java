package automation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import automation.mobile.MobileAutomation;
import automation.mobile.appium.AppiumMobileAutomation;
import automation.web.WebAutomation;
import automation.web.appium.AppiumWebAutomation;
import automation.web.selenium.SeleniumWebAutomation;

public class Automation {
	
	private Logger log;
	
	public Automation() {
		this.log = LogManager.getLogger(Automation.class);
		this.log.debug("Initializing Automation Class.");
		this.log.debug("Successfully initialized Automation Class.");
	}
	
	/**
	 * 
	 * @param automationTool
	 * @return
	 */
	
	public final WebAutomation getWebAutomation(String automationTool) {
		this.log.debug("Initializing Web Automation.");
		WebAutomation I = null;
		automationTool = automationTool.trim().toUpperCase();
		switch(automationTool) {
		case "SELENIUM":
			I = new SeleniumWebAutomation();
			break;
		case "APPIUM":
			I = new AppiumWebAutomation();
			this.log.fatal("Appium Web Automation is not yet implemented. Please report issue to QA Team.");
			break;
		default:
			this.log.fatal("Unsupported Web Browser. Please report issue to QA Team.");
		}
		this.log.debug("Successfully initialized Web Automation.");
		return I;
	}
	
	/**
	 * 
	 * @param automationTool
	 * @return
	 */
	
	public final MobileAutomation getMobileAutomation(String automationTool) {
		this.log.debug("Initializing Mobile Automation.");
		MobileAutomation I = null;
		automationTool = automationTool.trim().toUpperCase();
		switch(automationTool) {
		case "APPIUM":
			I = new AppiumMobileAutomation();
			this.log.fatal("Appium Mobile Automation is not yet implemented. Please report issue to QA Team.");
			break;
		default:
			this.log.fatal("Unsupported Web Browser. Please report issue to QA Team.");
		}
		this.log.debug("Successfully initialized Mobile Automation.");
		return I;
	}

}
