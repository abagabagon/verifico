package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	private static Logger log;
	private static int count = 1;
	private static int maxTry;

	static {
		log = LogManager.getLogger(Tests.class);
	}

	@Override
	public boolean retry(ITestResult arg0) {
		if (!arg0.isSuccess()) {
			maxTry = 3;
			log.info("Will retry test execution (" + count + "/" + maxTry + ")");
			if (count < maxTry) {
				count++;
				arg0.setStatus(ITestResult.FAILURE);
				return true;
			} else {
				log.info("Max retry reached.");
				count = 1;
				arg0.setStatus(ITestResult.FAILURE);
			}
		} else {
			arg0.setStatus(ITestResult.SUCCESS);
			count = 1;
		}
		return false;
	}

}