package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import automation.web.WebAutomation;

public class Pages {
	
	@SuppressWarnings("unused")
	private static WebAutomation I;
	private static Logger log;
	
	public Pages(WebAutomation I) {
		Pages.I = I;
		Pages.log = LogManager.getLogger(this.getClass());
		log.debug("Initializing Pages Class.");
		log.debug("Successfully initialized Pages Class.");
	}
	
	@SuppressWarnings("static-method")
	public final void initializePages() {
		log.info("Initializing Pages.");
		log.info("Successfully initialized Pages.");
	}
}
