package pages;

import org.openqa.selenium.By;

public class PagePracticePage {

	public static final By LETS_KODE_IT_IMAGE_LINK = By.xpath("//img[contains(@src,'letskodeit')]/parent::a");
	public static final By HOME_LINK = By.xpath("//a[text()='HOME']");
	public static final By ALL_COURSES_LINK = By.xpath("//a[text()='ALL COURSES'][@href='/courses']");
	public static final By SUPPORT_LINK = By.xpath("//a[text()='SUPPORT'][@href='/support']");
	public static final By SIGN_IN_LINK = By.linkText("Sign In");
	public static final By PRACTICE_PAGE_HEADER = By.xpath("//h1[@data-uniqid='1621702280245']");

	public static final By RADIO_BUTTON_EXAMPLE_DIVISION = By.id("radio-btn-example");
	public static final By RADIO_BUTTON_EXAMPLE_TEXT = By.xpath("//legend[text()='Radio Button Example']");
	public static final By BMW_RADIO_BUTTON = By.id("bmwradio");
	public static final By BENZ_RADIO_BUTTON = By.id("benzradio");
	public static final By HONDA_RADIO_BUTTON = By.id("hondaradio");

	public static final By SELECT_CLASS_EXAMPLE_DIVISION = By.id("select-class-example");
	public static final By SELECT_CLASS_EXAMPLE_TEXT = By.xpath("//legend[text()='Select Class Example']");
	public static final By CAR_DROPDOWN = By.id("carselect");

	public static final By MULTIPLE_SELECT_EXAMPLE_DIVISION = By.id("multi-select-example-div");
	public static final By MULTIPLE_SELECT_EXAMPLE_TEXT = By.xpath("//legend[text()='Multiple Select Example']");
	public static final By FRUIT_MULTISELECT = By.id("multiple-select-example");

	public static final By CHECKBOX_EXAMPLE_DIVISION = By.id("checkbox-example-div");
	public static final By CHECKBOX_EXAMPLE_TEXT = By.xpath("//legend[text()='Checkbox Example']");
	public static final By BMW_CHECKBOX = By.id("bmwcheck");
	public static final By BENZ_CHECKBOX = By.id("benzcheck");
	public static final By HONDA_CHECKBOX = By.id("hondacheck");

	public static final By SWITCH_WINDOW_EXAMPLE_DIVISION = By.id("open-window-example-div");
	public static final By SWITCH_WINDOW_EXAMPLE_TEXT = By.xpath("//legend[text()='Switch Window Example']");
	public static final By OPEN_WINDOW_BUTTON = By.id("openwindow");

	public static final By SWITCH_TAB_EXAMPLE_DIVISION = By.id("open-tab-example-div");
	public static final By SWITCH_TAB_EXAMPLE_TEXT = By.xpath("//legend[text()='Switch Tab Example']");
	public static final By OPEN_TAB_LINK = By.linkText("Open Tab");

	public static final By SWITCH_TO_ALERT_EXAMPLE_DIVISION = By.id("alert-example-div");
	public static final By SWITCH_TO_ALERT_EXAMPLE_TEXT = By.xpath("//legend[text()='Switch To Alert Example']");
	public static final By ENTER_NAME_TEXTBOX = By.name("enter-name");
	public static final By ALERT_BUTTON = By.id("alertbtn");
	public static final By CONFIRM_BUTTON = By.id("confirmbtn");

}
