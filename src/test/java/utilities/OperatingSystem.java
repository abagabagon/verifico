package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	/**
	 * Gets current System Date.
	 * 
	 * @param  dateFormat format of the date. Follows format of
	 *                    SimpleDateFormat.
	 * @return current System Date.
	 */

	public static final String getCurrentDate(String dateFormat) {
		log.trace("Getting Current Date");
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		log.trace("Retrieved Date: " + sdf.format(date).toString());
		return sdf.format(date);
	}

}
