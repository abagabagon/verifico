package automation.mobile;

import enums.TestStatus;

public interface MobileAutomation {
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	/**
	 * Opens Mobile Application.
	 */

	public void openApplication();
	
	/**
	 * Taps the specified Mobile Element. Used for Elements that are tappable.
	 * 
	 * @param locator Object used to locate Mobile Element to be tapped.
	 */

	public void tap(Object locator);
	
	/**
	 * Taps the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToTap		Object from same row to tap if text is found on that row.
	 */
	
	public void tapFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToTap);
	
	/**
	 * Long presses the specified Mobile Element. Used for Elements that are tappable.
	 * 
	 * @param locator	Object used to locate Mobile Element to be long pressed.
	 * @param duration	Number of seconds to long press Mobile Element.
	 */

	public void longPress(Object locator, long duration);
	
	/**
	 * Clears value of a text box/area Mobile Element.
	 * 
	 * @param locator Object used to locate Mobile Element to clear value of.
	 */

	public void clear(Object locator);
	
	/**
	 * Simulates typing into a text box/area Mobile Element, which may set its value.
	 * 
	 * @param locator 	Object used to locate Mobile Element to type into.
	 * @param inputText Text to enter.
	 */

	public void type(Object locator, String inputText);
	
	/**
	 * Type input text at the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText	Object from which to check the specified text to check.
	 * @param textToCheck		Text to check at the specified object.
	 * @param objectToFill		Object from same row to tap if text is found on that row.
	 * @param inputText			Text value to input
	 */
	
	public void typeFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToFill, String inputText);
	
	/**
	 * Get the visible innerText of this Mobile Element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param	locator Object used to locate Mobile Element to get text from.
	 * @return	Retrieved Mobile Element Text.
	 */
	
	public String getText(Object locator);
	
	/**
	 * Get text of the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param objectToCheckText		Object from which to check the specified text to check.
	 * @param textToCheck			Text to check at the specified object.
	 * @param objectToGetTextFrom	Object from same row to get text from if text is found on that row.
	 */
	
	public String getTextFromTableBasedOnText(Object objectToCheckText, String textToCheck, Object objectToGetTextFrom);
	
	/**
	 * Get the value of the specified attribute of the Mobile Element.
	 * 
	 * @param	locator Object used to locate Mobile Element to get attribute value from.
	 * @return	Retrieved Mobile Element attribute value.
	 */
	
	public String getAttributeValue(Object locator, String attribute);

	/**
	 * Gets the value of the INPUT and TEXTAREA Mobile Elements.
	 * 
	 * @param	locator Object used to locate Mobile Element to get value from.
	 * @return	Retrieved value of the INPUT/TEXTAREA Mobile Element.
	 */
	
	public String getValue(Object locator);
	
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
	 * Verifies if the Mobile Element is tappable or not.
	 * 
	 * @param locator Object used to locate Mobile Element to check.
	 * @return	<code>PASSED</code> if Mobile Element is tappable.
	 * 			<code>FAILED</code> if Mobile Element is not tappable.
	 */
	
	public TestStatus verifyTappable(Object locator);

	/**
	 * Verifies the value of INPUT or TEXTAREA Mobile Elements if equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param expectedValue	Expected value of the Mobile Element Text Box.
	 * @return	<code>PASSED</code> if value is equal to expected value.
	 * 			<code>FAILED</code> if value is not equal to expected value.
	 */

	public TestStatus verifyValue(Object locator, String expectedValue);
	
	/**
	 * Verifies the value of the specified attribute if equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param attribute		Name of attribute to assert the value from.
	 * @param expectedValue	Expected value of the Mobile Element Attribute.
	 * @return	<code>PASSED</code> if value is equal to expected attribute value.
	 * 			<code>FAILED</code> if value is not equal to expected attribute value.
	 */

	public TestStatus verifyAttributeValue(Object locator, String attribute, String expectedValue);

	/**
	 * Verifies Mobile Element with text if equal to expected text value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param expectedValue	Expected text value
	 * @return	<code>PASSED</code> if value is equal to expected value.
	 * 			<code>FAILED</code> if value is not equal to expected value.
	 */

	public TestStatus verifyText(Object locator, String expectedValue);
	
	/**
	 * Verifies if Mobile Element is displayed on Web Page.
	 * 
	 * @param	locator Object used to locate Mobile Element to check.
	 * @return	<code>PASSED</code> if Mobile Element is displayed on Web Page.
	 * 			<code>FAILED</code> if Mobile Element is not displayed on Web Page.
	 */
	
	public TestStatus verifyDisplayed(Object locator);
	
	/**
	 * Verifies Mobile Element is not displayed on the Web Page.
	 * 
	 * @param @param locator Object used to locate Mobile Element to assert.  
	 * @return	<code>PASSED</code> if Mobile Element is not displayed on Web Page.
	 * 			<code>FAILED</code> if Mobile Element is displayed on Web Page.
	 */

	public TestStatus verifyNotDisplayed(Object locator);
	
	/**
	 * Verifies Mobile Element is enabled on the Web Page.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.
	 * @return	<code>PASSED</code> if Mobile Element is enabled on Web Page.
	 * 			<code>FAILED</code> if Mobile Element is not enabled on Web Page.
	 */

	public TestStatus verifyEnabled(Object locator);
	
	/**
	 * Verifies Mobile Element is disabled on the Web Page.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.
	 * @return	<code>PASSED</code> if Mobile Element is disabled on Web Page.
	 * 			<code>FAILED</code> if Mobile Element is not disabled on Web Page.
	 */

	public TestStatus verifyDisabled(Object locator);

}
