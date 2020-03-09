package automation.web;

import java.util.List;

import enums.Browser;

public interface WebAutomation {
	
	/*#######################################################*/
	/*                   BROWSER ACTIONS                     */
	/*#######################################################*/
	
	/**
	 * Closes Web Browser.
	 */

	public void openBrowser(Browser browser);
	
	public void openTab(String url);
	
	/**
	 * Navigate to the Url specified.
	 * 
	 * @param url Url of the Web Page desired to navigate to.
	 */

	public void goTo(String url);
	
	public boolean switchTabByTitle(String expectedTitle);

	public boolean switchTabByURL(String url);
	
	/**
	 * Function for switching back to Original Tab
	 * 
	 */
	
	public void switchTabToOriginal();
	
	/**
	 * Navigate one item back from the browser's history.
	 */

	public void back();
	
	/**
	 * Navigate one item forward from the browser's history.
	 */

	public void forward();
	
	/**
	 * Refresh current page.
	 */

	public void refresh();
	
	/**
	 * Closes Tab of a Web Browser.
	 */

	public void closeTab();
	
	/**
	 * Closes Web Browser.
	 */

	public void closeBrowser();
	
	/**
	 * Maximizes Browser Window.
	 */
	
	public void maximizeBrowserWindow();
	
	/**
	 * Deletes all cookies.
	 */
	
	public void deleteAllCookies();
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	/**
	 * 
	 * @param locator
	 */
	
	public void mouseHover(Object locator);
	
	/**
	 * Clicks the element specified. Used for Elements that are clickable (excluding
	 * Radio Buttons & Check Boxes).
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 */

	public void click(Object locator);
	
	public void clickJS(Object locator);
	
	/**
	 * Simulates typing into an element, which may set its value. Text entry
	 * elements are INPUT and TEXTAREA elements.
	 * 
	 * @param map       Object containing a WebPage Object's Name, Type, XPath,
	 *                  WebElement & By Locator.
	 * @param inputText Text to enter.
	 */

	public void fill(Object locator, String inputText);
	
	/**
	 * Clears value of a text area element. Text entry elements are INPUT and
	 * TEXTAREA elements.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 */

	public void clear(Object locator);
	
	/**
	 * Checks of element exists on Web Page.
	 * 
	 * @param  locator
	 * @return 
	 */
	
	public boolean see(Object locator);
	
	/**
	 * 
	 * @param expectedTitle
	 * @return
	 */
	
	public boolean seeTitle(String expectedTitle);
	
	/**
	 * 
	 * @param expectedUrl
	 * @return
	 */

	public boolean seeUrl(String expectedUrl);
	
	/**
	 * Function to know if the element is clickable or not.
	 * 
	 * @param locator 
	 */
	
	public Boolean isClickable(String locator);

	/**
	 * Selects a Drop-down List WebElement Option.
	 * 
	 * @param map    Object containing a WebPage Object's Name, Type, XPath,
	 *               WebElement & By Locator.
	 * @param option Option to be selected.
	 */

	public void select(Object locator, String option);
	
	/**
	 * Deselects a Multi-select WebElement Option.
	 * 
	 * @param map    Object containing a WebPage Object's Name, Type, XPath,
	 *               WebElement & By Locator.
	 * @param option Option to be selected.
	 */

	public void deselect(Object locator, String option);
	
	/**
	 * Get a Web Element in a form of Object from a Web Page.
	 * 
	 * @param  locator Object that locates the specific Web Element from the Web Page.
	 * @return List of Elements in a form of Object.
	 */
	
	public Object getElement(Object locator);
	
	/**
	 * Get a list of Web Elements in a form of Object from a Web Page.
	 * 
	 * @param  locator Object that locates the specific Web Element from the Web Page.
	 * @return List of Elements in a form of Object.
	 */
	
	public List<Object> getElements(Object locator);
	
	/**
	 * Get the visible innerText of this element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved WebElement Text.
	 */
	
	public String getText(Object locator);
	
	/**
	 * Get the visible innerText of this element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved WebElement Text.
	 */
	
	public String getAttributeValue(Object locator, String attribute);

	/**
	 * Gets the value of the INPUT and TEXTAREA WebElements.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved value of the INPUT/TEXTAREA WebElement.
	 */
	
	public String getValue(Object locator);
	
	/**
	 * Gets the selected option of the Drop-down List WebElement.
	 * 
	 * @param map Object containing a WebPage Object's Name, Type, XPath, WebElement
	 *            & By Locator.
	 * @return Retrieved value of the Drop-down List WebElement.
	 */

	public String getDropDownListValue(Object locator);
	
	/**
	 * Waits for a specific time (Seconds).
	 * 
	 * @param locator By locator of target WebElement to be present.
	 */

	public void wait(int duration);
	
	/*#######################################################*/
	/*                      ASSERTIONS                       */
	/*#######################################################*/

	/**
	 * Checks the value of INPUT or TEXTAREA WebElements if equal to the expected
	 * value.
	 * 
	 * @param map           Object containing a WebPage Object's Name, Type, XPath,
	 *                      WebElement & By Locator.
	 * @param expectedValue Expected value of the WebElement Text Box.
	 */

	public void assertValue(Object locator, String expectedValue);

	/**
	 * Asserts Drop-down List WebElement Value if equal to expected text value.
	 * 
	 * @param map          Map Object containing WebElement, By, XPath & other String
	 *                     Objects relating to an Element.
	 * @param expectedText expected drop-down list value
	 */

	public void assertDropDownListValue(Object locator, String expectedValue);

	/**
	 * Asserts WebElement with text if equal to expected text value.
	 * 
	 * @param map          Map Object containing WebElement, By, XPath & other String
	 *                     Objects relating to an Element.
	 * @param expectedText expected text value
	 */

	public void assertText(Object locator, String expectedValue);
	
	/**
	 * Asserts WebElement is present on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */
	
	public void assertDisplayed(Object locator);
	
	/**
	 * Asserts WebElement is not displayed on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 *            
	 */

	public void assertNotDisplayed(Object locator);
	
	/**
	 * Asserts WebElement is enabled on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */

	public void assertEnabled(Object locator);
	
	/**
	 * Asserts WebElement is not disabled on the Web Page.
	 * 
	 * @param map Map Object containing WebElement, By, XPath & other String
	 *            Objects relating to an Element.
	 */

	public void assertDisabled(Object locator);
	
	/**
	 * Asserts WebElement is selected on the Web Page.
	 * 
	 * @param element WebElement to be verified.
	 * @param type    Type of option to be verified (can either be Index,
	 *                VisibleText or Value)
	 */

	public void assertSelected(Object locator);
	
	/**
	 * Asserts WebElement is not selected on the Web Page.
	 * 
	 * @param element WebElement to be verified.
	 * @param type    Type of option to be verified (can either be Index,
	 *                VisibleText or Value)
	 */

	public void assertNotSelected(Object locator);
	
	/**
	 * Checks if current Web Page's Title is equal to the expected Title.
	 * 
	 * @param expectedTitle expected Web Page Title.
	 * @param pageName      name of the Web Page.
	 */

	public void assertTitle(String expectedTitle);
	
	/**
	 * Asserts if current Web Page's Url is equal to the expected Url.
	 * 
	 * @param expectedUrl expected Url
	 * @param pagename    name of the Web Page.
	 */

	public void assertUrl(String expectedUrl);
	
	/**
	 * Asserts if current Web Page's Url is equal to the expected Url.
	 * 
	 * @param expectedUrl expected Url
	 * @param pagename    name of the Web Page.
	 */

	public void assertPartialUrl(String partialUrl);
	
	/**
	 * Asserts Javascript Alert Message displayed if equal to
	 * expected message.
	 * 
	 * @param expectedMessage Expected message displayed.
	 */

	public void assertAlertMessage(String expectedMessage);

}
