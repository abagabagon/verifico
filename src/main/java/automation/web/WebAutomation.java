package automation.web;

import enums.Browser;

public interface WebAutomation {
	
	/*#######################################################*/
	/*                   BROWSER ACTIONS                     */
	/*#######################################################*/
	
	/**
	 * Opens Web Browser.
	 */

	public void openBrowser(Browser browser);
	
	/**
	 * Opens Tab.
	 */
	
	public void openTab(String url);
	
	/**
	 * Navigate to the Url specified.
	 * 
	 * @param url Url of the Web Page desired to navigate to.
	 */

	public void goTo(String url);
	
	/**
	 * Switch to a Tab based on Page Title.
	 * 
	 * @param 	expectedTitle Expected Page Title to switch into.
	 * @return	<code>true</code> if switch is successful.
	 * 			<code>false</code> if switch is unsuccessful.
	 */
	
	public boolean switchTabByTitle(String expectedTitle);
	
	/**
	 * Switch to a Tab based on Page URL.
	 * 
	 * @param 	url Expected Page URL to switch into.
	 * @return	<code>true</code> if switch is successful.
	 * 			<code>false</code> if switch is unsuccessful.
	 */

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
	
	/**
	 * Scrolls Page
	 * 
	 * @param pixel
	 */
	
	public void scrollPage(String pixelHorizontal, String pixelVertical);
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	/**
	 * Hover to the specified element.
	 * 
	 * @param locator Object used to locate element to hover into.
	 */
	
	public void mouseHover(Object locator);
	
	/**
	 * Clicks the specified element. Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate element to be clicked.
	 */

	public void click(Object locator);
	
	/**
	 * Clicks the specified element (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate element to be clicked.
	 */
	
	public void clickJS(Object locator);
	
	/**
	 * Clicks the specified element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToClick		Object from same row to click if text is found on that row.
	 */
	
	public void clickFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToClick);
	
	/**
	 * Simulates typing into a text box/area element, which may set its value. Text entry
	 * elements are INPUT and TEXTAREA elements.
	 * 
	 * @param locator 	Object used to locate element to type into.
	 * @param inputText Text to enter.
	 */

	public void fill(Object locator, String inputText);
	
	/**
	 * Fill the specified element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToFill		Object from same row to click if text is found on that row.
	 * @param inputText			Text value to input
	 */
	
	public void fillFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToFill, String inputText);
	
	/**
	 * Simulates pressing of characters into a text box/area element.
	 * 
	 * @param locator 	Object used to locate element to type into.
	 * @param keyButton	Key Button to press
	 */
	
	public void press(Object locator, Object keyButton);
	
	/**
	 * Clears value of a text box/area element. Text entry elements are INPUT and
	 * TEXTAREA elements.
	 * 
	 * @param locator Object used to locate element to clear value of.
	 */

	public void clear(Object locator);
	
	/**
	 * Checks if element exists on Web Page.
	 * 
	 * @param	locator Object used to locate element to check.
	 * @return	<code>true</code> if element exists on Web Page.
	 * 			<code>false</code> if element does not exist on Web Page.
	 */
	
	public boolean see(Object locator);
	
	/**
	 * Checks Page Title of Web Page if equal to the expected Title.
	 * 
	 * @param	expectedTitle Expected Page Title to compare into
	 * @return	<code>true</code> if Page Title is equal to expected Title.
	 * 			<code>false</code> if Page Title is not equal to expected Title.
	 */
	
	public boolean seeTitle(String expectedTitle);
	
	/**
	 * Checks Page URL of Web Page if equal to the expected URL.
	 * 
	 * @param	expectedUrl Expected Page URL to compare into
	 * @return	<code>true</code> if Page URL is equal to expected URL.
	 * 			<code>false</code> if Page URL is not equal to expected URL.
	 */

	public boolean seeUrl(String expectedUrl);

	
	/**
	 * Function to know if the element is clickable or not.
	 * 
	 * @param locator Object used to locate element to check.
	 * @return	<code>true</code> if element is clickable.
	 * 			<code>false</code> if element is not clickable.
	 */
	
	public Boolean isClickable(String locator);

	/**
	 * Selects a Drop-down List WebElement Option.
	 * 
	 * @param locator	Object used to locate element to select an option from.
	 * @param option	Option to be selected.
	 */

	public void select(Object locator, String option);
	
	/**
	 * Deselects a Multi-select WebElement Option.
	 * 
	 * @param locator	Object used to locate element to select an option from.
	 * @param option	Option to be selected.
	 */

	public void deselect(Object locator, String option);
	
	/**
	 * Get the visible innerText of this element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param	locator Object used to locate element to get text from.
	 * @return	Retrieved WebElement Text.
	 */
	
	public String getText(Object locator);
	
	/**
	 * Get text of the specified element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText		Object from which to check the specified text to check.
	 * @param textToCheck			Text to check at the specified object.
	 * @param objectToGetTextFrom	Object from same row to get text from if text is found on that row.
	 */
	
	public String getTextFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToGetTextFrom);
	
	/**
	 * Get the value of the specified attribute of the element.
	 * 
	 * @param	locator Object used to locate element to get attribute value from.
	 * @return	Retrieved WebElement attribute value.
	 */
	
	public String getAttributeValue(Object locator, String attribute);

	/**
	 * Gets the value of the INPUT and TEXTAREA WebElements.
	 * 
	 * @param	locator Object used to locate element to get value from.
	 * @return	Retrieved value of the INPUT/TEXTAREA WebElement.
	 */
	
	public String getValue(Object locator);
	
	/**
	 * Gets the selected option of the Drop-down List WebElement.
	 * 
	 * @param	locator Object used to locate element to get selected option from.
	 * @return	Retrieved value of the Drop-down List WebElement.
	 */

	public String getDropDownListValue(Object locator);
	
	/**
	 * Waits for a specific time (Seconds).
	 * 
	 * @param duration Duration of time to wait (Seconds).
	 */

	public void wait(int duration);
	
	/*#######################################################*/
	/*                      ASSERTIONS                       */
	/*#######################################################*/

	/**
	 * Asserts the value of INPUT or TEXTAREA WebElements if equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate element to assert the value from.
	 * @param expectedValue	Expected value of the WebElement Text Box.
	 */

	public void assertValue(Object locator, String expectedValue);

	/**
	 * Asserts Drop-down List WebElement Value if equal to expected text value.
	 * 
	 * @param locator		Object used to locate element to assert the value from.
	 * @param expectedText	Expected drop-down list value
	 */

	public void assertDropDownListValue(Object locator, String expectedValue);

	/**
	 * Asserts WebElement with text if equal to expected text value.
	 * 
	 * @param locator		Object used to locate element to assert the value from.
	 * @param expectedText	Expected text value
	 */

	public void assertText(Object locator, String expectedValue);
	
	/**
	 * Asserts WebElement is displayed on the Web Page.
	 * 
	 * @param locator Object used to locate element to assert.
	 */
	
	public void assertDisplayed(Object locator);
	
	/**
	 * Asserts WebElement is not displayed on the Web Page.
	 * 
	 * @param @param locator Object used to locate element to assert.   
	 */

	public void assertNotDisplayed(Object locator);
	
	/**
	 * Asserts WebElement is enabled on the Web Page.
	 * 
	 * @param locator Object used to locate element to assert.
	 */

	public void assertEnabled(Object locator);
	
	/**
	 * Asserts WebElement is disabled on the Web Page.
	 * 
	 * @param locator Object used to locate element to assert.
	 */

	public void assertDisabled(Object locator);
	
	/**
	 * Asserts WebElement is selected on the Web Page. Used for Check Boxes and Radio Buttons.
	 * 
	 * @param locator Object used to locate element to assert.
	 */

	public void assertSelected(Object locator);
	
	/**
	 * Asserts WebElement is not selected on the Web Page. Used for Check Boxes and Radio Buttons.
	 * 
	 * @param locator Object used to locate element to assert.
	 */

	public void assertNotSelected(Object locator);
	
	/**
	 * Asserts if current Web Page's Title is equal to the expected Title.
	 * 
	 * @param expectedTitle Expected Web Page Title.
	 */

	public void assertTitle(String expectedTitle);
	
	/**
	 * Asserts if current Web Page's URL is equal to the expected URL.
	 * 
	 * @param expectedUrl Expected Web Page URL
	 */

	public void assertUrl(String expectedUrl);
	
	/**
	 * Asserts if current Web Page's URL is partially equal to the specified partial URL.
	 * 
	 * @param partialUrl Expected Web Page URL
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
