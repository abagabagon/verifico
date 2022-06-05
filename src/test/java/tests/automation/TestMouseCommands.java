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
import com.github.abagabagon.verifico.automation.web.StateAssertions;
import com.github.abagabagon.verifico.automation.web.ValueAssertions;
import com.github.abagabagon.verifico.automation.web.WaitCommands;

import pages.PageLogin;
import pages.PagePractice;
import tests.BrowserFactory;
import tests.Tests;

public class TestMouseCommands extends Tests {

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

	@BeforeTest(dependsOnMethods = { "setupWaitCommands" })
	void setupStateAssertions() {
		this.state = new StateAssertions(this.driver, this.wait);
	}

	@BeforeMethod(dependsOnMethods = { "setupSoftAssert" })
	void goToPracticePage() {
		this.browser.goTo(PagePractice.URL);
	}

	@AfterTest
	void teardownWebDriver() {
		this.driver.quit();
	}

	@Test(groups = "MOUSE_COMMANDS", description = "Verify Page Redirection on-click of Link", enabled = true)
	void MC1() {
		mouse.click(PagePractice.SIGN_IN_LINK);
		softAssert.assertTrue(value.seeUrl(PageLogin.URL));
		softAssert.assertAll();
	}

	@Test(groups = "MOUSE_COMMANDS", description = "Verify Radio Buttons are selected on-click of Link", enabled = true)
	void MC2() {
		mouse.click(PagePractice.BENZ_RADIO_BUTTON);
		softAssert.assertTrue(state.seeSelected(PagePractice.BENZ_RADIO_BUTTON));
		softAssert.assertTrue(state.seeDeselected(PagePractice.BMW_RADIO_BUTTON));
		softAssert.assertTrue(state.seeDeselected(PagePractice.HONDA_RADIO_BUTTON));
		mouse.click(PagePractice.BMW_RADIO_BUTTON);
		softAssert.assertTrue(state.seeDeselected(PagePractice.BENZ_RADIO_BUTTON));
		softAssert.assertTrue(state.seeSelected(PagePractice.BMW_RADIO_BUTTON));
		softAssert.assertTrue(state.seeDeselected(PagePractice.HONDA_RADIO_BUTTON));
		mouse.click(PagePractice.HONDA_RADIO_BUTTON);
		softAssert.assertTrue(state.seeDeselected(PagePractice.BENZ_RADIO_BUTTON));
		softAssert.assertTrue(state.seeDeselected(PagePractice.BMW_RADIO_BUTTON));
		softAssert.assertTrue(state.seeSelected(PagePractice.HONDA_RADIO_BUTTON));
		softAssert.assertAll();
	}

}
