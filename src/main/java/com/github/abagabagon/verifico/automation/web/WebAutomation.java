package com.github.abagabagon.verifico.automation.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public interface WebAutomation {
	
	/*#######################################################*/
	/*                   BROWSER ACTIONS                     */
	/*#######################################################*/
	
	/**
	 * Opens Web Browser.
	 */

	public void openBrowser();
	
	/**
	 * Opens Tab.
	 * 
	 * @param url Url of the Web Page desired to navigate to.
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
	 * @param pixelHorizontal	Horizontal Pixel Value to scroll to
	 * @param pixelVertical 	Vertical Pixel Value to scroll to
	 */
	
	public void scroll(String pixelHorizontal, String pixelVertical);
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	/**
	 * Point mouse to the specified Web Element.
	 * 
	 * @param locator Object used to locate Web Element to point the mouse into.
	 */
	
	public void point(By locator);
	
	/**
	 * Point mouse to the specified Web Element Location in the Web Page.
	 * 
	 * @param locator Object used to locate Web Element to point mouse into.
	 */
	
	public void pointJS(By locator);
	
	/**
	 * Clicks the specified Web Element. Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be clicked.
	 */

	public void click(By locator);
	
	/**
	 * Clicks the specified Web Element (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be clicked.
	 */
	
	public void clickJS(By locator);
	
	/**
	 * Clicks and holds the specified Web Element. Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be clicked and held.
	 */

	public void clickAndHold(By locator);
	
	/**
	 * Clicks the specified Web Element from an Object List based on text value.
	 * 
	 * @param objectList	Object from which to check the specified text to check.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void clickFromListBasedOnText(By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToClick		Object from same row to click if text is found on that row.
	 */
	
	public void clickFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToClick);
	
	/**
	 * Double clicks the specified Web Element. Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be double-clicked.
	 */

	public void doubleClick(By locator);
	
	/**
	 * Double clicks the specified Web Element from an Object List based on text value.
	 * 
	 * @param objectList	Object from which to check the specified text to check.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void doubleClickFromListBasedOnText(By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText		Object from which to check the specified text to check.
	 * @param textToCheck			Text to check at the specified object.
	 * @param objectToDoubleClick	Object from same row to double click if text is found on that row.
	 */
	
	public void doubleClickFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToDoubleClick);
	
	/**
	 * Drags a specified Web Element and drops it at target element. Used for Elements that can be dragged.
	 * 
	 * @param sourceObject Object used to locate Web Element to be dragged.
	 * @param targetObject Object used to locate Web Element where the dragged Web Element will be dropped into.
	 */

	public void dragAndDrop(By sourceObject, By targetObject);
	
	/**
	 * Simulates typing into a text box/area Web Element, which may set its value. Text entry
	 * Web Elements are INPUT and TEXTAREA Web Elements.
	 * 
	 * @param locator 	Object used to locate Web Element to type into.
	 * @param inputText Text to enter.
	 */

	public void type(By locator, String inputText);
	
	/**
	 * Simulates typing into a text box/area Web Element, which may set its value (utilizing Javascript). Text entry
	 * Web Elements are INPUT and TEXTAREA Web Elements.
	 * 
	 * @param locator 	Object used to locate Web Element to type into.
	 * @param inputText Text to enter.
	 */

	public void typeJS(By locator, String inputText);
	
	/**
	 * Type input text at the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToFill		Object from same row to click if text is found on that row.
	 * @param inputText			Text value to input
	 */
	
	public void typeFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, String inputText);
	
	/**
	 * Simulates pressing of characters into a text box/area Web Element.
	 * 
	 * @param locator 	Object used to locate Web Element to type into.
	 * @param keyButton	Key Button to press
	 */
	
	public void press(By locator, Keys keyButton);
	
	/**
	 * Press specified keys at the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToFill		Object from same row to click if text is found on that row.
	 * @param keyButton			Key Button to press.
	 */
	
	public void pressFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, Keys keyButton);
	
	/**
	 * Clears value of a text box/area Web Element. Text entry Web Elements are INPUT and
	 * TEXTAREA Web Elements.
	 * 
	 * @param locator Object used to locate Web Element to clear value of.
	 */

	public void clear(By locator);
	
	/**
	 * Clears value of a text box/area Web Element (utilizing Javascript). Text entry Web Elements are INPUT and
	 * TEXTAREA Web Elements.
	 * 
	 * @param locator Object used to locate Web Element to clear value of.
	 */

	public void clearJS(By locator);
	
	/**
	 * Clears value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToClear		Object from same row to clear if text is found on that row.
	 */
	
	public void clearFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToClear);

	/**
	 * Selects a Drop-down List Web Element Option.
	 * 
	 * @param locator	Object used to locate Web Element to select an option from.
	 * @param option	Option to be selected.
	 */

	public void select(By locator, String option);
	
	/**
	 * Selects a Drop-down List Web Element Option.
	 * 
	 * @param locator		Object used to locate Web Element to select an option from.
	 * @param optionList	Object used to locate Web Element List of options to select.
	 * @param option		Option to be selected.
	 */

	public void select(By locator, By optionList, String option);
	
	/**
	 * Deselects a Multi-select Web Element Option.
	 * 
	 * @param locator	Object used to locate Web Element to select an option from.
	 * @param option	Option to be selected.
	 */

	public void deselect(By locator, String option);
	
	/**
	 * Get the visible innerText of this Web Element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param	locator Object used to locate Web Element to get text from.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getText(By locator);
	
	/**
	 * Get text of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param 	objectToCheckText		Object from which to check the specified text to check.
	 * @param 	textToCheck				Text to check at the specified object.
	 * @param 	objectToGetTextFrom		Object from same row to get text from if text is found on that row.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getTextFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetTextFrom);

	/**
	 * Gets the value of the INPUT and TEXTAREA Web Element.
	 * 
	 * @param	locator Object used to locate Web Element to get value from.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValue(By locator);
	
	/**
	 * Get value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param 	objectToCheckText		Object from which to check the specified text to check.
	 * @param 	textToCheck				Text to check at the specified object.
	 * @param 	objectToGetValueFrom	Object from same row to get value from if text is found on that row.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValueFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetValueFrom);
	
	/**
	 * Get the value of the specified attribute of the Web Element.
	 * 
	 * @param	locator 	Object used to locate Web Element to get attribute value from.
	 * @param   attribute 	Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValue(By locator, String attribute);
	
	/**
	 * Get value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param	objectToCheckText		Object from which to check the specified text to check.
	 * @param 	textToCheck				Text to check at the specified object.
	 * @param 	objectToGetValueFrom	Object from same row to get attribute value from if text is found on that row.
	 * @param 	attribute 				Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValueFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetValueFrom, String attribute);
	
	/**
	 * Gets the selected option of the Drop-down List Web Element.
	 * 
	 * @param	locator Object used to locate Web Element to get selected option from.
	 * @return	Retrieved value of the Drop-down List Web Element.
	 */

	public String getDropDownListValue(By locator);
	
	/**
	 * Waits for a specific time (Seconds).
	 * 
	 * @param duration Duration of time to wait (Seconds).
	 */

	public void wait(int duration);
	
	/*#######################################################*/
	/*                     VERIFICATIONS                     */
	/*#######################################################*/
	
	/**
	 * Verifies Page URL of Web Page if equal to the expected URL.
	 * 
	 * @param	expectedUrl Expected Page URL to compare into
	 * @return	<code>true</code> if Page URL is equal to expected URL.
	 * 			<code>false</code> if Page URL is not equal to expected URL.
	 */

	public boolean seeUrl(String expectedUrl);
	
	/**
	 * Verifies Page URL of Web Page if not equal to the specified URL.
	 * 
	 * @param	url Expected Page URL to compare into
	 * @return	<code>true</code> if Page URL is not equal to specified URL.
	 * 			<code>false</code> if Page URL is equal to specified URL.
	 */

	public boolean dontSeeUrl(String url);
	
	/**
	 * Verifies Page Title of Web Page if equal to the expected Title.
	 * 
	 * @param	expectedTitle Expected Page Title to compare into
	 * @return	<code>true</code> if Page Title is equal to expected Title.
	 * 			<code>false</code> if Page Title is not equal to expected Title.
	 */
	
	public boolean seeTitle(String expectedTitle);
	
	/**
	 * Verifies Page Title of Web Page if not equal to the expected Title.
	 * 
	 * @param	title Expected Page Title to compare into
	 * @return	<code>true</code> if Page Title is not equal to expected Title.
	 * 			<code>false</code> if Page Title is equal to expected Title.
	 */
	
	public boolean dontSeeTitle(String title);

	/**
	 * Verifies the value of INPUT or TEXTAREA Web Elements if equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Web Element to assert the value from.
	 * @param expectedValue	Expected value of the Web Element Text Box.
	 * @return	<code>true</code> if value is equal to expected value.
	 * 			<code>false</code> if value is not equal to expected value.
	 */

	public boolean typed(By locator, String expectedValue);
	
	/**
	 * Verifies the value of INPUT or TEXTAREA Web Elements if not equal to the specified
	 * value.
	 * 
	 * @param locator	Object used to locate Web Element to assert the value from.
	 * @param value		Value of the Web Element Text Box.
	 * @return	<code>true</code> if value is not equal to specified value.
	 * 			<code>false</code> if value is equal to specified value.
	 */

	public boolean didntType(By locator, String value);
	
	/**
	 * Verifies the value of the specified attribute if equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Web Element to assert the value from.
	 * @param attribute		Name of attribute to assert the value from.
	 * @param expectedValue	Expected value of the Web Element Attribute.
	 * @return	<code>true</code> if value is equal to expected attribute value.
	 * 			<code>false</code> if value is not equal to expected attribute value.
	 */

	public boolean seeAttributeValue(By locator, String attribute, String expectedValue);
	
	/**
	 * Verifies the value of the specified attribute if not equal to the specified
	 * value.
	 * 
	 * @param locator	Object used to locate Web Element to assert the value from.
	 * @param attribute	Name of attribute to assert the value from.
	 * @param value		Value of the Web Element Attribute.
	 * @return	<code>true</code> if value is not equal to expected attribute value.
	 * 			<code>false</code> if value is equal to expected attribute value.
	 */

	public boolean dontSeeAttributeValue(By locator, String attribute, String value);

	/**
	 * Verifies Drop-down List Web Element Selected Value if equal to expected text value.
	 * 
	 * @param locator		Object used to locate Web Element to assert the value from.
	 * @param expectedValue	Expected drop-down list value
	 * @return	<code>true</code> if selected value is equal to expected drop-down list value.
	 * 			<code>false</code> if selected value is not equal to expected drop-down list value.
	 */

	public boolean selectedDropDown(By locator, String expectedValue);

	/**
	 * Verifies Web Element with text if equal to expected text value.
	 * 
	 * @param locator		Object used to locate Web Element to assert the value from.
	 * @param expectedValue	Expected text value
	 * @return	<code>true</code> if value is equal to expected value.
	 * 			<code>false</code> if value is not equal to expected value.
	 */

	public boolean seeText(By locator, String expectedValue);
	
	/**
	 * Verifies Web Element with text if not equal to specified text value.
	 * 
	 * @param locator	Object used to locate Web Element to assert the value from.
	 * @param value		Text value to check
	 * @return	<code>true</code> if value is not equal to expected value.
	 * 			<code>false</code> if value is equal to expected value.
	 */

	public boolean dontSeeText(By locator, String value);
	
	/**
	 * Verifies if text value exists from texts on Web Element List.
	 * 
	 * @param locator	Object used to locate Web Element List to assert the text value from.
	 * @param textValue	Text value to verify
	 * @return	<code>true</code> if text value exists from texts of Web Element List.
	 * 			<code>false</code> if text value does not exist from texts of Web Element List.
	 */

	public boolean seeTextFromList(By locator, String textValue);
	
	/**
	 * Verifies if text value does not exist from texts on Web Element List.
	 * 
	 * @param locator	Object used to locate Web Element List to assert the text value from.
	 * @param textValue	Text value to verify
	 * @return	<code>true</code> if text value does not exist from texts of Web Element List.
	 * 			<code>false</code> if text value exists from texts of Web Element List.
	 */

	public boolean dontSeeTextFromList(By locator, String textValue);
	
	/**
	 * Verifies if Web Element is displayed on Web Page.
	 * 
	 * @param	locator Object used to locate Web Element to check.
	 * @return	<code>true</code> if Web Element is displayed on Web Page.
	 * 			<code>false</code> if Web Element is not displayed on Web Page.
	 */
	
	public boolean see(By locator);
	
	/**
	 * Verifies Web Element is not displayed on the Web Page.
	 * 
	 * @param locator Object used to locate Web Element to assert.  
	 * @return	<code>true</code> if Web Element is not displayed on Web Page.
	 * 			<code>false</code> if Web Element is displayed on Web Page.
	 */

	public boolean dontSee(By locator);
	
	/**
	 * Verifies Web Element is enabled on the Web Page.
	 * 
	 * @param locator Object used to locate Web Element to assert.
	 * @return	<code>true</code> if Web Element is enabled on Web Page.
	 * 			<code>false</code> if Web Element is not enabled on Web Page.
	 */

	public boolean seeEnabled(By locator);
	
	/**
	 * Verifies Web Element is disabled on the Web Page.
	 * 
	 * @param locator Object used to locate Web Element to assert.
	 * @return	<code>true</code> if Web Element is disabled on Web Page.
	 * 			<code>false</code> if Web Element is not disabled on Web Page.
	 */

	public boolean seeDisabled(By locator);
	
	/**
	 * Verifies Web Element is selected on the Web Page. Used for Check Boxes and Radio Buttons.
	 * 
	 * @param locator Object used to locate Web Element to assert.
	 * @return	<code>true</code> if Web Element is selected on Web Page.
	 * 			<code>false</code> if Web Element is not selected on Web Page.
	 */

	public boolean selected(By locator);
	
	/**
	 * Verifies Web Element is not selected on the Web Page. Used for Check Boxes and Radio Buttons.
	 * 
	 * @param locator Object used to locate Web Element to assert.
	 * @return	<code>true</code> if Web Element is not selected on Web Page.
	 * 			<code>false</code> if Web Element is selected on Web Page.
	 */

	public boolean deselected(By locator);
	
	/**
	 * Verifies Javascript Alert Message displayed if equal to
	 * expected message.
	 * 
	 * @param expectedMessage Expected message displayed.
	 * @return	<code>true</code> if message is equal to expected alert message.
	 * 			<code>false</code> if message is not equal to expected alert message.
	 */

	public boolean seeAlertMessage(String expectedMessage);

}
