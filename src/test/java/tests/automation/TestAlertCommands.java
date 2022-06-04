package tests.automation;

import java.time.Duration;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.github.abagabagon.verifico.automation.web.AlertCommands;
import com.github.abagabagon.verifico.automation.web.BrowserCommands;
import com.github.abagabagon.verifico.automation.web.KeyboardCommands;
import com.github.abagabagon.verifico.automation.web.MouseCommands;
import com.github.abagabagon.verifico.automation.web.ValueAssertions;
import com.github.abagabagon.verifico.automation.web.WaitCommands;

import pages.PagePracticePage;
import tests.BrowserFactory;
import tests.Tests;

public class TestAlertCommands extends Tests {

	@Parameters({"browser"})
	@BeforeTest
	void setupWebDriver(String browser) {
		this.browserFactory = new BrowserFactory();
		this.driver = browserFactory.getWebDriver(browser);
		this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		this.driver.manage().window().maximize();
	}

	@BeforeTest(dependsOnMethods = { "setupWebDriver" })
	void setupWaitCommands() {
		this.wait = new WaitCommands(this.driver, 10, 5);
	}

	@BeforeTest(dependsOnMethods = { "setupWaitCommands" })
	void setupBrowserCommands() {
		this.browser = new BrowserCommands(this.driver, this.wait);
	}

	@BeforeTest(dependsOnMethods = { "setupWaitCommands" })
	void setupAlertCommands() {
		this.alert = new AlertCommands(this.driver, this.wait);
	}

	@BeforeTest(dependsOnMethods = { "setupWaitCommands" })
	void setupMouseCommands() {
		this.mouse = new MouseCommands(this.driver, this.wait);
	}

	@BeforeTest(dependsOnMethods = { "setupWaitCommands" })
	void setupKeyboardCommands() {
		this.keyboard = new KeyboardCommands(this.driver, this.wait);
	}

	@BeforeTest(dependsOnMethods = { "setupWaitCommands" })
	void setupValueAssertions() {
		this.value = new ValueAssertions(this.driver, this.wait);
	}

	@BeforeMethod(dependsOnMethods = { "setupSoftAssert" })
	void goToPracticePage() {
		this.browser.goTo(URL);
	}

	@AfterTest
	void teardownWebDriver() {
		this.driver.quit();
	}

	@Test(groups = "ALERT_COMMANDS", description = "Verify Accepting of Alert", enabled = true)
	void AC1() {
		this.keyboard.type(PagePracticePage.ENTER_NAME_TEXTBOX, "Arnel");
		this.mouse.click(PagePracticePage.ALERT_BUTTON);
		softAssert.assertTrue(
				this.value.seeAlertMessage("Hello Arnel, share this practice page and share your knowledge"));
		this.alert.acceptAlert();
		softAssert.assertAll();
	}

	@Test(groups = "ALERT_COMMANDS", description = "Verify Accepting of Alert with Multiple Options", enabled = true)
	void AC2() {
		this.keyboard.type(PagePracticePage.ENTER_NAME_TEXTBOX, "Arnel");
		this.mouse.click(PagePracticePage.CONFIRM_BUTTON);
		softAssert.assertTrue(this.value.seeAlertMessage("Hello Arnel, Are you sure you want to confirm?"));
		this.alert.acceptAlert();
		softAssert.assertAll();
	}

	@Test(groups = "ALERT_COMMANDS", description = "Verify Cancelling of Alert with Multiple Options", enabled = true)
	void AC3() {
		this.keyboard.type(PagePracticePage.ENTER_NAME_TEXTBOX, "Arnel");
		this.mouse.click(PagePracticePage.CONFIRM_BUTTON);
		softAssert.assertTrue(this.value.seeAlertMessage("Hello Arnel, Are you sure you want to confirm?"));
		this.alert.cancelAlert();
		softAssert.assertAll();
	}

}
