package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import automation.web.WebAutomation;

public class Pages {
	
	@SuppressWarnings("unused")
	private static WebAutomation I;
	private static Logger log;
	
	public Pages(WebAutomation I) {
		log = LogManager.getLogger(this.getClass());
		log.debug("Initializing Pages Class.");
		Pages.I = I;
		log.debug("Successfully initialized Pages Class.");
	}
	
	public final void initializePages() {
		log.info("Initializing Pages.");
		log.info("Successfully initialized Pages.");
	}
}
