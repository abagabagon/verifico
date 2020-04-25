package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;

public class OperatingSystem {
	
	private static Logger log;
	
	static {
		log = LogManager.getLogger(OperatingSystem.class);
		log.debug("Initializing OperatingSystem Class.");
		log.debug("Successfully initialized OperatingSystem Class.");
	}
	
	/**
	 * Gets Operating System of Host System.
	 * 
	 * @return
	 */
	
	public static final Platform getOS() {
		log.debug("Detecting Operating System.");
		String operatingSystemName = System.getProperty("os.name").toLowerCase();
		Platform operatingSystem = null;
		
		if (operatingSystemName.contains("win")) {
			operatingSystem = Platform.WINDOWS;
		} else if (operatingSystemName.contains("mac")) {
			operatingSystem = Platform.MAC;
		} else if(operatingSystemName.contains("nux")) {
			operatingSystem = Platform.LINUX;
		} else {
			log.fatal("Unsupported Operating System. Please report issue to QA Team.");
		}
		
		log.debug("Detected Operating System: " + operatingSystem);
		return operatingSystem;
	}

}
