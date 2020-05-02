package automation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import automation.mobile.AppiumMobileAutomation;
import automation.mobile.MobileAutomation;
import automation.web.AppiumWebAutomation;
import automation.web.SeleniumWebAutomation;
import automation.web.WebAutomation;
import enums.MobileAutomationTool;
import enums.WebAutomationTool;

public class Automation {
	
	private Logger log;
	
	public Automation() {
		this.log = LogManager.getLogger(Automation.class);
	}
	
	/**
	 * 
	 * @param automationTool
	 * @return
	 */
	
	public final WebAutomation getWebAutomation(WebAutomationTool tool) {
		this.log.debug("Initializing Web Automation.");
		WebAutomation webAutomation = null;
		switch(tool) {
		case SELENIUM:
			webAutomation = new SeleniumWebAutomation();
			break;
		case APPIUM:
			webAutomation = new AppiumWebAutomation();
			this.log.fatal("Appium Web Automation is not yet implemented. Please report issue to QA Team.");
			break;
		default:
			this.log.fatal("Unsupported Web Browser. Please report issue to QA Team.");
		}
		this.log.debug("Successfully initialized Web Automation.");
		return webAutomation;
	}
	
	/**
	 * 
	 * @param automationTool
	 * @return
	 */
	
	public final MobileAutomation getMobileAutomation(MobileAutomationTool tool) {
		this.log.debug("Initializing Mobile Automation.");
		MobileAutomation mobileAutomation = null;
		switch(tool) {
		case APPIUM:
			mobileAutomation = new AppiumMobileAutomation();
			this.log.fatal("Appium Mobile Automation is not yet implemented. Please report issue to QA Team.");
			break;
		default:
			this.log.fatal("Unsupported Web Browser. Please report issue to QA Team.");
		}
		this.log.debug("Successfully initialized Mobile Automation.");
		return mobileAutomation;
	}

}
