package loggers.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import loggers.LoggerUtility;

public class Log4J2Logger implements LoggerUtility {
	
	private Logger log;

	@Override
	public void setLogger() {
		this.log = LogManager.getLogger();
	}

	@Override
	public Object getLogger() {
		return this.log;
	}

	@Override
	public void trace(String message) {
		this.log.trace(message);
	}

	@Override
	public void debug(String message) {
		this.log.debug(message);
	}

	@Override
	public void info(String message) {
		this.log.info(message);
	}

	@Override
	public void warn(String message) {
		this.log.warn(message);
	}

	@Override
	public void error(String message) {
		this.log.error(message);
		
	}

	@Override
	public void fatal(String message) {
		this.log.fatal(message);
	}

}
