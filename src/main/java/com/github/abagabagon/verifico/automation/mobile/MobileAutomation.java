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
	
	/**
	 * Resets application data.
	 */
	
	public void resetApp();
	
	/*#######################################################*/
	/*                    USER ACTIONS                       */
	/*#######################################################*/
	
	/**
	 * Simulates swiping based on the specified coordinates.
	 * 
	 * @param startX	X coordinate for the start scroll swipe position
	 * @param startY	Y coordinate for the start scroll swipe position
	 * @param endX		X coordinate for the end scroll swipe position
	 * @param endY		Y coordinate for the end scroll swipe position
	 */
	
	public void swipe(int startX, int startY, int endX, int endY);
	
	/**
	 * Simulates swipe up.
	 */
	
	public void swipeUp();
	
	/**
	 * Simulates swipe down.
	 */
	
	public void swipeDown();
	
	/**
	 * Swipe to the specified Mobile Element position.
	 * 
	 * @param locator Object used to locate Mobile Element.
	 */
	
	public void swipe(By locator);
	
	/**
	 * Taps the specified Mobile Element. Used for Elements that are tappable.
	 * 
	 * @param locator Object used to locate Mobile Element to be tapped.
	 */

	public void tap(By locator);
	
	/**
	 * Taps the specified Mobile Element from an Element List based on text value. Used for Elements that are tappable.
	 * 
	 * @param objectList	List Object Locator used to search text from and tap whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void tapOnListElementBasedOnText(By objectList, String textToCheck);
	
	/**
	 * Taps the specified Mobile Element from an Element List based on attribute value. Used for Elements that are tappable.
	 * 
	 * @param objectList	List Object Locator used to search attribute value from and tap whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void tapOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck);
	
	/**
	 * Taps the specified Mobile Element from a row in a table based on a text value from the same row. Used for Elements that are tappable.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTap		Object Locator relative to the Row Object to tap if specified text is found on that row.
	 */
	
	public void tapOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTap);
	
	/**
	 * Taps the specified Mobile Element from a row in a table based on a attribute value from the same row. Used for Elements that are tappable.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute				Attribute to check at the specified object to check Text.
	 * @param valueToCheck			Attribute Value to check at the specified object to check Text.
	 * @param rowObjectToTap		Object Locator relative to the Row Object to tap if specified attribute value is found on that row.
	 */
	
	public void tapOnTableRowElementBasedOnTableRowAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTap);
	
	/**
	 * Long presses the specified Mobile Element. Used for Elements that are tappable.
	 * 
	 * @param locator	Object used to locate Mobile Element to be long pressed.
	 * @param duration	Number of seconds to long press Mobile Element.
	 */

	public void longPress(By locator, long duration);
	
	/**
	 * Long presses the specified Mobile Element from an Element List based on text value. Used for Elements that are tappable.
	 * 
	 * @param objectList	List Object Locator used to search text from and tap whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 * @param duration		Number of seconds to long press Mobile Element.
	 */
	
	public void longPressOnListElementBasedOnText(By objectList, String textToCheck, long duration);
	
	/**
	 * Long presses the specified Mobile Element from an Element List based on attribute value. Used for Elements that are tappable.
	 * 
	 * @param objectList	List Object Locator used to search attribute value from and tap whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 * @param duration		Number of seconds to long press Mobile Element.
	 */
	
	public void longPressOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck, long duration);
	
	/**
	 * Long presses the specified Mobile Element from a row in a table based on a text value from the same row. Used for Elements that are tappable.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTap		Object Locator relative to the Row Object to tap if specified text is found on that row.
	 * @param duration				Number of seconds to long press Mobile Element.
	 */
	
	public void longPressOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTap, long duration);
	
	/**
	 * Long presses the specified Mobile Element from a row in a table based on a attribute value from the same row. Used for Elements that are tappable.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute				Attribute to check at the specified object to check Text.
	 * @param valueToCheck			Attribute Value to check at the specified object to check Text.
	 * @param rowObjectToTap		Object Locator relative to the Row Object to tap if specified attribute value is found on that row.
	 * @param duration				Number of seconds to long press Mobile Element.
	 */
	
	public void longPressOnTableRowElementBasedOnTableRowAttributeValue(By rowObjectList, By rowObjectToCheckText, String attribute, String valueToCheck, By rowObjectToTap, long duration);
	
	/**
	 * Clears value of a text box/area Mobile Element.
	 * 
	 * @param locator Object used to locate Mobile Element to clear value of.
	 */

	public void clear(By locator);
	
	/**
	 * Clears value of the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClear		Object Locator relative to the Row Object to clear value if specified text is found on that row.
	 */
	
	public void clearTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear);
	
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
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTypeOn		Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param inputText				Text value to input
	 */
	
	public void typeOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText);
	
	/**
	 * Get the visible innerText of this Mobile Element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param	locator Object used to locate Mobile Element to get text from.
	 * @return	Retrieved Mobile Element Text.
	 */
	
	public String getText(By locator);
	
	/**
	 * Gets the text of the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToGetTextFrom	Object Locator relative to the Row Object to get text from if specified text is found on that row.
	 * @return	Retrieved Mobile Element Text.
	 */
	
	public String getTextFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom);
	
	/**
	 * Get the value of the specified attribute of the Mobile Element.
	 * 
	 * @param	locator Object used to locate Mobile Element to get attribute value from.
	 * @param	attribute Mobile Element attribute to get value from.
	 * @return	Retrieved Mobile Element attribute value.
	 */
	
	public String getAttributeValue(By locator, String attribute);
	
	/**
	 * Gets the attribute value of the specified Mobile Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText				Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck						Text to check at the specified object to check Text.
	 * @param rowObjectToGetAttributeValueFrom	Object Locator relative to the Row Object to get attribute value from if specified text is found on that row.
	 * @param attribute 						Attribute of Mobile Element to get the value from.
	 * @return	Retrieved Mobile Element attribute value.
	 */
	
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute);
	
	/**
	 * Waits for a specific time (Seconds).
	 * 
	 * @param duration Duration of time to wait (Seconds).
	 */

	public void wait(int duration);
	
	/**
	 * Counts instances of a Mobile Element in a Page.
	 * 
	 * @param locator Object used to locate Mobile Element to count.
	 * @return Instance Count of the Mobile Element in the Page.
	 */
	
	public int count(By locator);
	
	/*#######################################################*/
	/*                     VERIFICATIONS                     */
	/*#######################################################*/
	
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
	 * Verifies the value of the specified attribute if partially equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param attribute		Name of attribute to assert the value from.
	 * @param expectedValue	Expected value of the Mobile Element Attribute.
	 * @return	<code>true</code> if value is partially equal to expected attribute value.
	 * 			<code>false</code> if value is partially not equal to expected attribute value.
	 */

	public boolean seePartialAttributeValue(By locator, String attribute, String expectedValue);
	
	/**
	 * Verifies the value of the specified attribute if not partially equal to the specified
	 * value.
	 * 
	 * @param locator	Object used to locate Mobile Element to assert the value from.
	 * @param attribute	Name of attribute to assert the value from.
	 * @param value		Value of the Mobile Element Attribute.
	 * @return	<code>true</code> if value is partially not equal to expected attribute value.
	 * 			<code>false</code> if value is partially equal to expected attribute value.
	 */

	public boolean dontSeePartialAttributeValue(By locator, String attribute, String value);

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
	 * Verifies if text value exists from texts on Mobile Element List.
	 * 
	 * @param locator	Object used to locate Mobile Element List to assert the text value from.
	 * @param textValue	Text value to verify
	 * @return	<code>true</code> if text value exists from texts of Mobile Element List.
	 * 			<code>false</code> if text value does not exist from texts of Mobile Element List.
	 */

	public boolean seeTextOfListElement(By locator, String textValue);
	
	/**
	 * Verifies if text value does not exist from texts on Mobile Element List.
	 * 
	 * @param locator	Object used to locate Mobile Element List to assert the text value from.
	 * @param textValue	Text value to verify
	 * @return	<code>true</code> if text value does not exist from texts of Mobile Element List.
	 * 			<code>false</code> if text value exists from texts of Mobile Element List.
	 */

	public boolean dontSeeTextOfListElement(By locator, String textValue);
	
	/**
	 * Verifies if text value exists from text of a Mobile Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedValue				Expected text value
	 * @return	<code>true</code> if value is equal to expected text value.
	 * 			<code>false</code> if value is not equal to expected text value.
	 */

	public boolean seeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies if text value does not exist from text of a Mobile Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param value						Text value to verify
	 * @return	<code>true</code> if value is not equal to expected text value.
	 * 			<code>false</code> if value is equal to expected text value.
	 */

	public boolean dontSeeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value);
	
	/**
	 * Verifies Mobile Element with text if equal to expected text value from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedValue				Expected text value
	 * @return	<code>true</code> if value is equal to expected text value.
	 * 			<code>false</code> if value is not equal to expected text value.
	 */

	public boolean seeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies Mobile Element with text if not equal to specified text value from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param value						Text value to check
	 * @return	<code>true</code> if value is not equal to expected value.
	 * 			<code>false</code> if value is equal to expected value.
	 */

	public boolean dontSeeTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String value);
	
	/**
	 * Verifies text value if it exists in a Mobile Element List from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList					Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText			Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck					Text to check at the specified object to check Text.
	 * @param rowObjectListToSeeTextFrom	Object List Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedValue			Expected text value
	 * @return	<code>true</code> if value is equal to expected text value.
	 * 			<code>false</code> if value is not equal to expected text value.
	 */

	public boolean seeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies text value if it does not exist in a Mobile Element List from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList					Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText			Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck					Text to check at the specified object to check Text.
	 * @param rowObjectListToSeeTextFrom	Object List Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedValue			Expected text value
	 * @return	<code>true</code> if value is not equal to expected text value.
	 * 			<code>false</code> if value is equal to expected text value.
	 */

	public boolean dontSeeTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies Mobile Element with text if equal to expected partial text value.
	 * 
	 * @param locator		Object used to locate Mobile Element to assert the value from.
	 * @param expectedValue	Expected partial text value
	 * @return	<code>true</code> if value is equal to expected value.
	 * 			<code>false</code> if value is not equal to expected value.
	 */

	public boolean seePartialText(By locator, String expectedValue);
	
	/**
	 * Verifies Mobile Element with text if not equal to specified partial text value.
	 * 
	 * @param locator	Object used to locate Mobile Element to assert the value from.
	 * @param value		Partial text value to check
	 * @return	<code>true</code> if value is not equal to specified partial text value.
	 * 			<code>false</code> if value is equal to specified partial text value.
	 */

	public boolean dontSeePartialText(By locator, String value);
	
	/**
	 * Verifies if partial text value exists from texts on Mobile Element List.
	 * 
	 * @param locator			Object used to locate Mobile Element List to assert the text value from.
	 * @param partialTextValue	Text value to verify
	 * @return	<code>true</code> if partial text value exists from texts of Mobile Element List.
	 * 			<code>false</code> if partial text value does not exist from texts of Mobile Element List.
	 */

	public boolean seePartialTextOfListElement(By locator, String partialTextValue);
	
	/**
	 * Verifies if partial text value does not exist from texts on Mobile Element List.
	 * 
	 * @param locator			Object used to locate Mobile Element List to assert the text value from.
	 * @param partialTextValue	Text value to verify
	 * @return	<code>true</code> if partial text value does not exist from texts of Mobile Element List.
	 * 			<code>false</code> if partial text value exists from texts of Mobile Element List.
	 */

	public boolean dontSeePartialTextOfListElement(By locator, String partialTextValue);
	
	/**
	 * Verifies if partial text value exists from text of a Mobile Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedPartialValue		Expected partial text value
	 * @return	<code>true</code> if value is equal to expected partial text value.
	 * 			<code>false</code> if value is not equal to expected partial text value.
	 */

	public boolean seePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedPartialValue);
	
	/**
	 * Verifies if partial text value does not exist from text of a Mobile Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param value						Text value to verify
	 * @return	<code>true</code> if value is not equal to expected partial text value.
	 * 			<code>false</code> if value is equal to expected partial text value.
	 */

	public boolean dontSeePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value);
	
	/**
	 * Verifies Mobile Element with text if equal to expected partial text value from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert partial text value from if specified text is found on that row.
	 * @param expectedValue				Expected partial text value
	 * @return	<code>true</code> if value is equal to expected partial text value.
	 * 			<code>false</code> if value is not equal to expected partial text value.
	 */

	public boolean seePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies Mobile Element with text if not equal to specified partial text value from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert partial text value from if specified text is found on that row.
	 * @param expectedValue		Partial text value to check
	 * @return	<code>true</code> if value is not equal to specified partial text value.
	 * 			<code>false</code> if value is equal to specified partial text value.
	 */

	public boolean dontSeePartialTextOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies partial text value if it exists in a Mobile Element List from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList					Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText			Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck					Text to check at the specified object to check Text.
	 * @param rowObjectListToSeeTextFrom	Object List Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedValue			Expected text value
	 * @return	<code>true</code> if value is equal to expected text value.
	 * 			<code>false</code> if value is not equal to expected text value.
	 */

	public boolean seePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies partial text value if it does not exist in a Mobile Element List from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList					Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText			Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck					Text to check at the specified object to check Text.
	 * @param rowObjectListToSeeTextFrom	Object List Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedValue			Expected text value
	 * @return	<code>true</code> if value is not equal to expected text value.
	 * 			<code>false</code> if value is equal to expected text value.
	 */

	public boolean dontSeePartialTextOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies if Mobile Element is displayed on Screen.
	 * 
	 * @param	locator Object used to locate Mobile Element to check.
	 * @return	<code>true</code> if Mobile Element is displayed on Screen.
	 * 			<code>false</code> if Mobile Element is not displayed on Screen.
	 */
	
	public boolean see(By locator);
	
	/**
	 * Verifies Mobile Element is not displayed on the Screen.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.  
	 * @return	<code>true</code> if Mobile Element is not displayed on Screen.
	 * 			<code>false</code> if Mobile Element is displayed on Screen.
	 */

	public boolean dontSee(By locator);
	
	/**
	 * Verifies Mobile Element is displayed from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToSee		Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @return	<code>true</code> if Mobile Element is displayed on Screen Table.
	 * 			<code>false</code> if Mobile Element is not displayed on Screen Table
	 */

	public boolean seeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
	/**
	 * Verifies Mobile Element is not displayed from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToSee		Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @return	<code>true</code> if Mobile Element is not displayed on Screen Table.
	 * 			<code>false</code> if Mobile Element is displayed on Screen Table.
	 */

	public boolean dontSeeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
	/**
	 * Verifies Mobile Element is enabled on the Screen.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.
	 * @return	<code>true</code> if Mobile Element is enabled on Screen.
	 * 			<code>false</code> if Mobile Element is not enabled on Screen.
	 */

	public boolean seeEnabled(By locator);
	
	/**
	 * Verifies Mobile Element is disabled on the Screen.
	 * 
	 * @param locator Object used to locate Mobile Element to assert.
	 * @return	<code>true</code> if Mobile Element is disabled on Screen.
	 * 			<code>false</code> if Mobile Element is not disabled on Screen.
	 */

	public boolean seeDisabled(By locator);

}
