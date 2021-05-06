package com.github.abagabagon.verifico.automation.mobile;

import org.openqa.selenium.By;

/**
 * Interface for Mobile Automation Commands
 * 
 * @author albagabagon
 *
 */

public interface MobileAutomation {
	
	/*#######################################################*/
	/*                   DEVICE ACTIONS                      */
	/*#######################################################*/
	
	/**
	 * Opens Mobile Application.
	 */

	public void openApplication();
	
	/**
	 * Closes Mobile Application.
	 */
	
	public void closeApplication();
	
	/**
	 * Get the current device orientation.
	 * 
	 * @return The current device orientation.
	 */
	
	public Object getOrientation();
	
	/**
	 * Sets the device's orientation
	 * 
	 * @param orientation Desired device orientation.
	 */
	
	public void setOrientation(Object orientation);
	
	/**
	 * Get the current device geo location.
	 * 
	 * @return The current device geo location.
	 */
	
	public Object getGeolocation();
	
	/**
	 * Sets the device's geo location
	 * 
	 * @param location Desired device geo location.
	 */
	
	public void setGeolocation(Object location);
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	/**
	 * Taps the specified Mobile Element. Used for Elements that are tappable.
	 * 
	 * @param locator Object used to locate Mobile Element to be tapped.
	 */

	public void tap(By locator);
	
	/**
	 * Taps the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToTap		Object from same row to tap if text is found on that row.
	 */
	
	public void tapFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToTap);
	
	/**
	 * Long presses the specified Mobile Element. Used for Elements that are tappable.
	 * 
	 * @param locator	Object used to locate Mobile Element to be long pressed.
	 * @param duration	Number of seconds to long press Mobile Element.
	 */

	public void longPress(By locator, long duration);
	
	/**
	 * Long presses the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToLongPress	Object from same row to tap if text is found on that row.
	 * @param duration			Number of seconds to long press Mobile Element.
	 */
	
	public void longPressFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToLongPress, long duration);
	
	/**
	 * Clears value of a text box/area Mobile Element.
	 * 
	 * @param locator Object used to locate Mobile Element to clear value of.
	 */

	public void clear(By locator);
	
	/**
	 * Simulates typing into a text box/area Mobile Element, which may set its value.
	 * 
	 * @param locator 	Object used to locate Mobile Element to type into.
	 * @param inputText Text to enter.
	 */

	public void type(By locator, String inputText);
	
	/**
	 * Type input text at the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToFill		Object from same row to tap if text is found on that row.
	 * @param inputText			Text value to input
	 */
	
	public void typeFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToFill, String inputText);
	
	/**
	 * Get the visible innerText of this Mobile Element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param	locator Object used to locate Mobile Element to get text from.
	 * @return	Retrieved Mobile Element Text.
	 */
	
	public String getText(By locator);
	
	/**
	 * Get text of the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText		Object from which to check the specified text to check.
	 * @param textToCheck			Text to check at the specified object.
	 * @param objectToGetTextFrom	Object from same row to get text from if text is found on that row.
	 * @return	Retrieved Mobile Element text value.
	 */
	
	public String getTextFromTableBasedOnText(By objectToCheckText, String textToCheck, By objectToGetTextFrom);
	
	/**
	 * Get the value of the specified attribute of the Mobile Element.
	 * 
	 * @param	locator Object used to locate Mobile Element to get attribute value from.
	 * @param	attribute Mobile Element attribute to get value from.
	 * @return	Retrieved Mobile Element attribute value.
	 */
	
	public String getAttributeValue(By locator, String attribute);

	/**
	 * Gets the value of the INPUT and TEXTAREA Mobile Elements.
	 * 
	 * @param	locator Object used to locate Mobile Element to get value from.
	 * @return	Retrieved value of the INPUT/TEXTAREA Mobile Element.
	 */
	
	public String getValue(By locator);
	
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
	 * Verifies the value of INPUT or TEXTAREA Mobile Elements if equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param expectedValue	Expected value of the Mobile Element Text Box.
	 * @return	<code>true</code> if value is equal to expected value.
	 * 			<code>false</code> if value is not equal to expected value.
	 */

	public boolean typed(By locator, String expectedValue);
	
	/**
	 * Verifies the value of INPUT or TEXTAREA Mobile Elements if not equal to the expected
	 * value.
	 * 
	 * @param locator	Object used to locate Mobile Element to assert the value from.
	 * @param value		Expected value of the Mobile Element Text Box.
	 * @return	<code>true</code> if value is not equal to expected value.
	 * 			<code>false</code> if value is equal to expected value.
	 */

	public boolean didntType(By locator, String value);
	
	/**
	 * Verifies the value of the specified attribute if equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param attribute		Name of attribute to assert the value from.
	 * @param expectedValue	Expected value of the Mobile Element Attribute.
	 * @return	<code>true</code> if value is equal to expected attribute value.
	 * 			<code>false</code> if value is not equal to expected attribute value.
	 */

	public boolean seeAttributeValue(By locator, String attribute, String expectedValue);
	
	/**
	 * Verifies the value of the specified attribute if not equal to the expected
	 * value.
	 * 
	 * @param locator	Object used to locate Mobile Element to assert the value from.
	 * @param attribute	Name of attribute to assert the value from.
	 * @param value		Expected value of the Mobile Element Attribute.
	 * @return	<code>true</code> if value is not equal to expected attribute value.
	 * 			<code>false</code> if value is equal to expected attribute value.
	 */

	public boolean dontSeeAttributeValue(By locator, String attribute, String value);

	/**
	 * Verifies Mobile Element with text if equal to expected text value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param expectedValue	Expected text value
	 * @return	<code>true</code> if value is equal to expected value.
	 * 			<code>false</code> if value is not equal to expected value.
	 */

	public boolean seeText(By locator, String expectedValue);
	
	/**
	 * Verifies Mobile Element with text if not equal to expected text value.
	 * 
	 * @param locator	Object used to locate Mobile Element to assert the value from.
	 * @param value		Expected text value
	 * @return	<code>true</code> if value is not equal to expected value.
	 * 			<code>false</code> if value is equal to expected value.
	 */

	public boolean dontSeeText(By locator, String value);
	
	/**
	 * Verifies if Mobile Element is displayed on Web Page.
	 * 
	 * @param	locator Object used to locate Mobile Element to check.
	 * @return	<code>true</code> if Mobile Element is displayed on Web Page.
	 * 			<code>false</code> if Mobile Element is not displayed on Web Page.
	 */
	
	public boolean see(By locator);
	
	/**
	 * Verifies Mobile Element is not displayed on the Web Page.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.  
	 * @return	<code>true</code> if Mobile Element is not displayed on Web Page.
	 * 			<code>false</code> if Mobile Element is displayed on Web Page.
	 */

	public boolean dontSee(By locator);
	
	/**
	 * Verifies Mobile Element is enabled on the Web Page.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.
	 * @return	<code>true</code> if Mobile Element is enabled on Web Page.
	 * 			<code>false</code> if Mobile Element is not enabled on Web Page.
	 */

	public boolean seeEnabled(By locator);
	
	/**
	 * Verifies Mobile Element is disabled on the Web Page.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.
	 * @return	<code>true</code> if Mobile Element is disabled on Web Page.
	 * 			<code>false</code> if Mobile Element is not disabled on Web Page.
	 */

	public boolean seeDisabled(By locator);

}
