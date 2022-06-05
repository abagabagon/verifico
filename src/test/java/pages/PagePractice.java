package pages;

import org.openqa.selenium.By;

public class PagePractice {

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

	public static final By WEB_TABLE_EXAMPLE_DIVISION = By.id("table-example-div");
	public static final By WEB_TABLE_EXAMPLE_TEXT = By.xpath("//legend[text()='Web Table Example']");
	public static final By AUTHOR_COLUMN_NAME = By.xpath("//table[@id='product']//th[text()='Author ']");
	public static final By COURSE_COLUMN_NAME = By.xpath("//table[@id='product']//th[text()='Course ']");
	public static final By PRICE_COLUMN_NAME = By.xpath("//table[@id='product']//th[text()='Price ']");
	public static final By PRODUCT_TABLE_ROWS = By.xpath("//table[@id='product']//td[@class='author-name']/parent::tr");
	public static final By AUTHOR_COLUMN_VALUE = By.xpath("./td[@class='author-name']");
	public static final By COURSE_COLUMN_VALUE = By.xpath("./td[@class='course-name']");
	public static final By PRICE_COLUMN_VALUE = By.xpath("./td[@class='price']");

	public static final By ENABLED_DISABLED_EXAMPLE_DIVISION = By.id("enabled-example-div");
	public static final By ENABLED_DISABLED_EXAMPLE_TEXT = By.xpath("//legend[text()='Enabled/Disabled Example']");
	public static final By DISABLE_BUTTON = By.id("disabled-button");
	public static final By ENABLE_BUTTON = By.id("enabled-button");
	public static final By ENABLE_DISABLE_TEXTBOX = By.id("enabled-example-input");

	public static final By ELEMENT_DISPLAYED_EXAMPLE_DIVISION = By.id("hide-show-example-div");
	public static final By ELEMENT_DISPLAYED_EXAMPLE_TEXT = By.xpath("//legend[text()='Element Displayed Example']");
	public static final By HIDE_BUTTON = By.id("hide-textbox");
	public static final By SHOW_BUTTON = By.id("show-textbox");
	public static final By HIDE_SHOW_TEXTBOX = By.id("displayed-text");

	public static final By MOUSE_HOVER_EXAMPLE_DIVISION = By.id("mouse-hover-example-div");
	public static final By MOUSE_HOVER_EXAMPLE_TEXT = By.xpath("//legend[text()='Mouse Hover Example']");
	public static final By MOUSE_HOVER_BUTTON = By.id("mousehover");
	public static final By TOP_LINK = By.linkText("Top");
	public static final By RELOAD_LINK = By.linkText("Reload");

	public static final String URL = "https://courses.letskodeit.com/practice";

}
