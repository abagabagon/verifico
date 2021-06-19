package com.github.abagabagon.verifico.automation.web;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

/**
 * Interface for Web Automation Commands
 * 
 * @author albagabagon
 *
 */

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
	 * Navigates to the Url specified.
	 * 
	 * @param url Url of the Web Page desired to navigate to.
	 */

	public void goTo(String url);
	
	/**
	 * Switches to a Tab based on Page Title.
	 * 
	 * @param 	expectedTitle Expected Page Title to switch into.
	 * @return	<code>true</code> if switch is successful.
	 * 			<code>false</code> if switch is unsuccessful.
	 */
	
	public boolean switchTabByTitle(String expectedTitle);
	
	/**
	 * Switches to a Tab based on Page URL.
	 * 
	 * @param 	url Expected Page URL to switch into.
	 * @return	<code>true</code> if switch is successful.
	 * 			<code>false</code> if switch is unsuccessful.
	 */

	public boolean switchTabByURL(String url);
	
	/**
	 * Switches back to Original Tab
	 */
	
	public void switchTabToOriginal();
	
	/**
	 * Navigates one item back from the browser's history.
	 */

	public void back();
	
	/**
	 * Navigates one item forward from the browser's history.
	 */

	public void forward();
	
	/**
	 * Refreshes current page.
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
	
	public void maximize();
	
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
	 * Points mouse to the specified Web Element.
	 * 
	 * @param locator Object used to locate Web Element to point the mouse into.
	 */
	
	public void point(By locator);
	
	/**
	 * Points the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to point
	 */
	
	public void point(By parent, By child);
	
	/**
	 * Points the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to point
	 */
	
	public void point(By parent, int index, By child);
	
	/**
	 * Points the specified Web Element from an Element List based on index.
	 * 
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to point to from the element list
	 */
	
	public void pointOnListElement(By objectList, int index);
	
	/**
	 * Points the specified Web Element from an Element List based on index within the Parent Web Element.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to point to from the element list
	 */
	
	public void pointOnListElement(By parent, By objectList, int index);
	
	/**
	 * Points the specified Web Element from an Element List based on index within the Parent Web Element.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to point to from the element list
	 */
	
	public void pointOnListElement(By parentList, int parentIndex, By objectList, int index);
	
	/**
	 * Points the specified Web Element from an Element List based on text value.
	 * 
	 * @param objectList	List Object Locator used to search text from and point whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void pointOnListElementBasedOnText(By objectList, String textToCheck);
	
	/**
	 * Points the specified Web Element from an Element List based on text value within the Parent Web Element.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search text from and point whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void pointOnListElementBasedOnText(By parent, By objectList, String textToCheck);
	
	/**
	 * Points the specified Web Element from an Element List based on text value within the Parent Web Element.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search text from and point whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void pointOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck);
	
	/**
	 * Points the specified Web Element from an Element List based on attribute value.
	 * 
	 * @param objectList	List Object Locator used to search attribute value from and point whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void pointOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck);
	
	/**
	 * Points the specified Web Element from an Element List based on attribute value within the Parent Web Element.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search attribute value from and point whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void pointOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Points the specified Web Element from an Element List based on attribute value within the Parent Web Element.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search attribute value from and point whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void pointOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Points the specified Web Element from a row in a table based on a text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToPoint		Object Locator relative to the Row Object to point if specified text is found on that row.
	 */
	
	public void pointOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToPoint);
	
	/**
	 * Points the specified Web Element from a row in a table based on a text value from the same row within the Parent Web Element.
	 * 
	 * @param parent				Locator of Parent Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToPoint		Object Locator relative to the Row Object to point if specified text is found on that row.
	 */
	
	public void pointOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToPoint);
	
	/**
	 * Points the specified Web Element from a row in a table based on a text value from the same row within the Parent Web Element.
	 * 
	 * @param parentList			Locator of Parent List Object
	 * @param parentIndex			Parent List Object Index
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToPoint		Object Locator relative to the Row Object to point if specified text is found on that row.
	 */
	
	public void pointOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToPoint);
	
	/**
	 * Points the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToPoint					Object Locator relative to the Row Object to point if specified attribute value is found on that row.
	 */
	
	public void pointOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToPoint);
	
	/**
	 * Points the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToPoint					Object Locator relative to the Row Object to point if specified attribute value is found on that row.
	 */
	
	public void pointOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToPoint);
	
	/**
	 * Points the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Parent List Object Index
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToPoint					Object Locator relative to the Row Object to point if specified attribute value is found on that row.
	 */
	
	public void pointOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToPoint);
	
	/**
	 * Clicks the specified Web Element. Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be clicked.
	 */

	public void click(By locator);
	
	
	/**
	 * Clicks the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to click
	 */
	
	public void click(By parent, By child);
	
	/**
	 * Clicks the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to click
	 */
	
	public void click(By parent, int index, By child);
	
	/**
	 * Clicks the specified Web Element (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be clicked.
	 */

	public void clickJS(By locator);
	
	/**
	 * Clicks the specified Child Web Element within the Parent Web Element (utilizing Javascript).
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to click
	 */
	
	public void clickJS(By parent, By child);
	
	/**
	 * Clicks the specified Child Web Element within the Parent Web Element (utilizing Javascript).
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to click
	 */
	
	public void clickJS(By parent, int index, By child);
	
	/**
	 * Clicks and holds the specified Web Element. Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be clicked and held.
	 */

	public void clickAndHold(By locator);
	
	/**
	 * Clicks and holds the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to click and hold
	 */
	
	public void clickAndHold(By parent, By child);
	
	/**
	 * Clicks the specified Web Element from an Element List based on index. Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to click on from the element list
	 */
	
	public void clickOnListElement(By objectList, int index);
	
	/**
	 * Clicks the specified Web Element from an Element List based on index within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to click on from the element list
	 */
	
	public void clickOnListElement(By parent, By objectList, int index);
	
	/**
	 * Clicks the specified Web Element from an Element List based on index within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to click on from the element list
	 */
	
	public void clickOnListElement(By parentList, int parentIndex, By objectList, int index);
	
	/**
	 * Clicks the specified Web Element from an Element List based on index (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to click on from the element list
	 */
	
	public void clickJSOnListElement(By objectList, int index);
	
	/**
	 * Clicks the specified Web Element from an Element List based on index (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to click on from the element list
	 */
	
	public void clickJSOnListElement(By parent, By objectList, int index);
	
	/**
	 * Clicks the specified Web Element from an Element List based on index (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to click on from the element list
	 */
	
	public void clickJSOnListElement(By parentList, int parentIndex, By objectList, int index);
	
	/**
	 * Clicks the specified Web Element from an Element List based on text value. Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator used to search text from and click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void clickOnListElementBasedOnText(By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on text value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search text from and click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void clickOnListElementBasedOnText(By parent, By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on text value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search text from and click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void clickOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on text value (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator used to search text from and click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void clickJSOnListElementBasedOnText(By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on text value (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search text from and click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void clickJSOnListElementBasedOnText(By parent, By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on text value (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search text from and click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void clickJSOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on attribute value. Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator used to search attribute value from and click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void clickOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on attribute value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search attribute value from and click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void clickOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on attribute value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search attribute value from and click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void clickOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on attribute value (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator used to search attribute value from and click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void clickJSOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on attribute value (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search attribute value from and click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void clickJSOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Clicks the specified Web Element from an Element List based on attribute value (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search attribute value from and click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void clickJSOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on a text value from the same row. Used for Elements that are clickable.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClick		Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	public void clickOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on a text value from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent				Locator of Parent Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClick		Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	public void clickOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on a text value from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList			Locator of Parent List Object
	 * @param parentIndex			Parent List Object Index
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClick		Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	public void clickOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on a text value from the same row (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClick		Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	public void clickJSOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on a text value from the same row (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent				Locator of Parent Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClick		Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	public void clickJSOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on a text value from the same row (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList			Locator of Parent List Object
	 * @param parentIndex			Parent List Object Index
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClick		Object Locator relative to the Row Object to click if specified text is found on that row.
	 */
	
	public void clickJSOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row. Used for Elements that are clickable.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClick					Object Locator relative to the Row Object to click if specified attribute value is found on that row.
	 */
	
	public void clickOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClick					Object Locator relative to the Row Object to click if specified attribute value is found on that row.
	 */
	
	public void clickOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Parent List Object Index
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClick					Object Locator relative to the Row Object to click if specified attribute value is found on that row.
	 */
	
	public void clickOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row (utilizing Javascript). Used for Elements that are clickable.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClick					Object Locator relative to the Row Object to click if specified attribute value is found on that row.
	 */
	
	public void clickJSOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClick					Object Locator relative to the Row Object to click if specified attribute value is found on that row.
	 */
	
	public void clickJSOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick);
	
	/**
	 * Clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row (utilizing Javascript) within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Parent List Object Index
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClick					Object Locator relative to the Row Object to click if specified attribute value is found on that row.
	 */
	
	public void clickJSOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClick);
	
	/**
	 * Double-clicks the specified Web Element. Used for Elements that are clickable.
	 * 
	 * @param locator Object used to locate Web Element to be double-clicked.
	 */

	public void doubleClick(By locator);
	
	/**
	 * Double-clicks the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to double-click
	 */
	
	public void doubleClick(By parent, By child);
	
	/**
	 * Double-clicks the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to double-click
	 */
	
	public void doubleClick(By parent, int index, By child);
	
	/**
	 * Double-clicks the specified Web Element from an Object List based on index. Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to double-click on from the element list
	 */
	
	public void doubleClickOnListElement(By objectList, int index);
	
	/**
	 * Double-clicks the specified Web Element from an Object List based on index within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to double-click on from the element list
	 */
	
	public void doubleClickOnListElement(By parent, By objectList, int index);
	
	/**
	 * Double-clicks the specified Web Element from an Object List based on index within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator.
	 * @param index			Index of the element to double-click on from the element list
	 */
	
	public void doubleClickOnListElement(By parentList, int parentIndex, By objectList, int index);
	
	/**
	 * Double-clicks the specified Web Element from an Object List based on text value. Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator used to search text from and double-click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void doubleClickOnListElementBasedOnText(By objectList, String textToCheck);
	
	/**
	 * Double-clicks the specified Web Element from an Object List based on text value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search text from and double-click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void doubleClickOnListElementBasedOnText(By parent, By objectList, String textToCheck);
	
	/**
	 * Double-clicks the specified Web Element from an Object List based on text value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search text from and double-click whichever element equals the specified text.
	 * @param textToCheck	Text to check at the specified object.
	 */
	
	public void doubleClickOnListElementBasedOnText(By parentList, int parentIndex, By objectList, String textToCheck);
	
	/**
	 * Double-clicks the specified Web Element from an Element List based on attribute value. Used for Elements that are clickable.
	 * 
	 * @param objectList	List Object Locator used to search attribute value from and double-click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void doubleClickOnListElementBasedOnAttributeValue(By objectList, String attribute, String valueToCheck);
	
	/**
	 * Double-clicks the specified Web Element from an Element List based on attribute value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent		Locator of Parent Object
	 * @param objectList	List Object Locator used to search attribute value from and double-click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void doubleClickOnListElementBasedOnAttributeValue(By parent, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Double-clicks the specified Web Element from an Element List based on attribute value within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList	Locator of Parent List Object
	 * @param parentIndex	Parent List Object Index
	 * @param objectList	List Object Locator used to search attribute value from and double-click whichever element equals the specified attribute value.
	 * @param attribute		Attribute to check at the specified object.
	 * @param valueToCheck	Attribute Value to check at the specified object.
	 */
	
	public void doubleClickOnListElementBasedOnAttributeValue(By parentList, int parentIndex, By objectList, String attribute, String valueToCheck);
	
	/**
	 * Double-clicks the specified Web Element from a row in a table based on text value from the same row. Used for Elements that are clickable.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToDoubleClick	Object Locator relative to the Row Object to double-click if specified text is found on that row.
	 */
	
	public void doubleClickOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoubleClick);
	
	/**
	 * Double-clicks the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent					Parent Object Index
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToDoubleClick	Object Locator relative to the Row Object to double-click if specified text is found on that row.
	 */
	
	public void doubleClickOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoubleClick);
	
	/**
	 * Double-clicks the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList				Locator of Parent List Object
	 * @param parentIndex				Parent List Object Index
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToDoubleClick	Object Locator relative to the Row Object to double-click if specified text is found on that row.
	 */
	
	public void doubleClickOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToDoubleClick);
	
	/**
	 * Double-clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row. Used for Elements that are clickable.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToDoubleClick			Object Locator relative to the Row Object to double-click if specified attribute value is found on that row.
	 */
	
	public void doubleClickOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoubleClick);
	
	/**
	 * Double-clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parent							Parent Object Index
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToDoubleClick			Object Locator relative to the Row Object to double-click if specified attribute value is found on that row.
	 */
	
	public void doubleClickOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoubleClick);
	
	/**
	 * Double-clicks the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element. Used for Elements that are clickable.
	 * 
	 * @param parentList				Locator of Parent List Object
	 * @param parentIndex				Parent List Object Index
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToDoubleClick			Object Locator relative to the Row Object to double-click if specified attribute value is found on that row.
	 */
	
	public void doubleClickOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToDoubleClick);
	
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
	 * @param inputText Text value to input.
	 */

	public void type(By locator, String inputText);
	
	/**
	 * Simulates typing into a text box/area Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to type to
	 * @param inputText	Text value to input.
	 */
	
	public void type(By parent, By child, String inputText);
	
	/**
	 * Simulates typing into a text box/area Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to type to
	 * @param inputText	Text value to input.
	 */
	
	public void type(By parent, int index, By child, String inputText);
	
	/**
	 * Type input text at the specified Web Element from a row in a table based on text value from the same row.
	 *
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTypeOn		Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param inputText				Text value to input
	 */
	
	public void typeOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText);
	
	/**
	 * Type input text at the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 *
	 * @param parent				Locator of Parent Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTypeOn		Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param inputText				Text value to input
	 */
	
	public void typeOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText);
	
	/**
	 * Type input text at the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 *
	 * @param parentList			Locator of Parent List Object
	 * @param parentIndex			Index of Parent List Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTypeOn		Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param inputText				Text value to input
	 */
	
	public void typeOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, String inputText);
	
	/**
	 * Type input text at the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 *
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToTypeOn					Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param inputText							Text value to input
	 */
	
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText);
	
	/**
	 * Type input text at the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 *
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToTypeOn					Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param inputText							Text value to input
	 */
	
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText);
	
	/**
	 * Type input text at the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 *
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Index of Parent List Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToTypeOn					Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param inputText							Text value to input
	 */
	
	public void typeOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToTypeOn, String inputText);
	
	/**
	 * Simulates pressing of characters into a text box/area Web Element.
	 * 
	 * @param locator 	Object used to locate Web Element to type into.
	 * @param keyButton	Key Button to press
	 */
	
	public void press(By locator, Keys keyButton);
	
	/**
	 * Simulates pressing of characters into a text box/area Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to type to
	 * @param keyButton	Key Button to press
	 */
	
	public void press(By parent, By child, Keys keyButton);
	
	/**
	 * Simulates pressing of characters into a text box/area Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to type to
	 * @param keyButton	Key Button to press
	 */
	
	public void press(By parent, int index, By child, Keys keyButton);
	
	/**
	 * Press specified keys at the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTypeOn		Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param keyButton				Key Button to press.
	 */
	
	public void pressOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton);
	
	/**
	 * Press specified keys at the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 * 
	 * @param parent				Locator of Parent Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTypeOn		Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param keyButton				Key Button to press.
	 */
	
	public void pressOnTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton);
	
	/**
	 * Press specified keys at the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 * 
	 * @param parentList			Locator of Parent List Object
	 * @param parentIndex			Index of Parent List Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToTypeOn		Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param keyButton				Key Button to press.
	 */
	
	public void pressOnTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToTypeOn, Keys keyButton);
	
	/**
	 * Press specified keys at the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToTypeOn					Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param keyButton							Key Button to press.
	 */
	
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton);
	
	/**
	 * Press specified keys at the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToTypeOn					Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param keyButton							Key Button to press.
	 */
	
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton);
	
	/**
	 * Press specified keys at the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Index of Parent List Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToTypeOn					Object Locator relative to the Row Object to type on if specified text is found on that row.
	 * @param keyButton							Key Button to press.
	 */
	
	public void pressOnTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToTypeOn, Keys keyButton);
	
	/**
	 * Clears value of a text box/area Web Element. Text entry Web Elements are INPUT and
	 * TEXTAREA Web Elements.
	 * 
	 * @param locator Object used to locate Web Element to clear value of.
	 */

	public void clear(By locator);
	
	/**
	 * Clears value of the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to clear value of.
	 */
	
	public void clear(By parent, By child);
	
	/**
	 * Clears value of the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to clear value of.
	 */
	
	public void clear(By parent, int index, By child);
	
	/**
	 * Clears value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClear		Object Locator relative to the Row Object to clear value if specified text is found on that row.
	 */
	
	public void clearTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear);
	
	/**
	 * Clears value of the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 * 
	 * @param parent				Locator of Parent Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClear		Object Locator relative to the Row Object to clear value if specified text is found on that row.
	 */
	
	public void clearTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear);
	
	/**
	 * Clears value of the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 * 
	 * @param parentList			Locator of Parent List Object
	 * @param parentIndex			Index of Parent List Object
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToClear		Object Locator relative to the Row Object to clear value if specified text is found on that row.
	 */
	
	public void clearTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToClear);
	
	/**
	 * Clears value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClear					Object Locator relative to the Row Object to clear value if specified text is found on that row.
	 */
	
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClear);
	
	/**
	 * Clears value of the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClear					Object Locator relative to the Row Object to clear value if specified text is found on that row.
	 */
	
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClear);
	
	/**
	 * Clears value of the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Index of Parent List Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attribute							Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToClear					Object Locator relative to the Row Object to clear value if specified text is found on that row.
	 */
	
	public void clearTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attribute, String valueToCheck, By rowObjectToClear);

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
	 * Gets the visible innerText of this Web Element, including sub-elements, without
	 * any leading or trailing whitespace.
	 * 
	 * @param	locator Object used to locate Web Element to get text from.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getText(By locator);
	
	/**
	 * Gets the visible innerText of the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to get text from.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getText(By parent, By child);
	
	/**
	 * Gets the visible innerText of the specified Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to get text from.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getText(By parent, int index, By child);
	
	/**
	 * Gets the text of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToGetTextFrom	Object Locator relative to the Row Object to get text from if specified text is found on that row.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getTextFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom);
	
	/**
	 * Gets the text of the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 * 
	 * @param parent					Locator of Parent Object
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToGetTextFrom	Object Locator relative to the Row Object to get text from if specified text is found on that row.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getTextFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom);
	
	/**
	 * Gets the text of the specified Web Element from a row in a table based on text value from the same row within the Parent Web Element.
	 * 
	 * @param parentList				Locator of Parent List Object
	 * @param parentIndex				Index of Parent List Object
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToGetTextFrom	Object Locator relative to the Row Object to get text from if specified text is found on that row.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getTextFromTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetTextFrom);
	
	/**
	 * Gets the text of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetTextFrom			Object Locator relative to the Row Object to get text from if specified text is found on that row.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom);
	
	/**
	 * Gets the text of the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetTextFrom			Object Locator relative to the Row Object to get text from if specified text is found on that row.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom);
	
	/**
	 * Gets the text of the specified Web Element from a row in a table based on an attribute value of an element from the same row within the Parent Web Element.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Index of Parent List Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetTextFrom			Object Locator relative to the Row Object to get text from if specified text is found on that row.
	 * @return	Retrieved Web Element Text.
	 */
	
	public String getTextFromTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetTextFrom);

	/**
	 * Gets the value of the INPUT and TEXTAREA Web Element.
	 * 
	 * @param	locator Object used to locate Web Element to get value from.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValue(By locator);
	
	/**
	 * Gets the value of the INPUT and TEXTAREA Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to get text from.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValue(By parent, By child);
	
	/**
	 * Gets the value of the INPUT and TEXTAREA Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to get text from.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValue(By parent, int index, By child);
	
	/**
	 * Gets the value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToGetValueFrom	Object Locator relative to the Row Object to get value from if specified text is found on that row.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom);
	
	/**
	 * Gets the value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param parent					Locator of Parent Object
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToGetValueFrom	Object Locator relative to the Row Object to get value from if specified text is found on that row.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValueFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom);
	
	/**
	 * Gets the value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param parentList				Locator of Parent List Object
	 * @param parentIndex				Index of Parent List Object
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText		Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck				Text to check at the specified object to check Text.
	 * @param rowObjectToGetValueFrom	Object Locator relative to the Row Object to get value from if specified text is found on that row.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValueFromTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetValueFrom);
	
	/**
	 * Gets the value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetValueFrom			Object Locator relative to the Row Object to get value from if specified text is found on that row.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom);
	
	/**
	 * Gets the value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetValueFrom			Object Locator relative to the Row Object to get value from if specified text is found on that row.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom);
	
	/**
	 * Gets the value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Index of Parent List Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetValueFrom			Object Locator relative to the Row Object to get value from if specified text is found on that row.
	 * @return	Retrieved value of the INPUT/TEXTAREA Web Element.
	 */
	
	public String getValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetValueFrom);
	
	/**
	 * Gets the value of the specified attribute of the Web Element.
	 * 
	 * @param	locator 	Object used to locate Web Element to get attribute value from.
	 * @param   attribute 	Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValue(By locator, String attribute);
	
	/**
	 * Gets the value of the specified attribute of the Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to get text from.
	 * @param attribute Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValue(By parent, By child, String attribute);
	
	/**
	 * Gets the value of the specified attribute of the Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param index		Index of Parent Object
	 * @param child		Locator of Child Object to get text from.
	 * @param attribute Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValue(By parent, int index, By child, String attribute);
	
	/**
	 * Gets the attribute value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText				Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck						Text to check at the specified object to check Text.
	 * @param rowObjectToGetAttributeValueFrom	Object Locator relative to the Row Object to get attribute value from if specified text is found on that row.
	 * @param attribute 						Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute);
	
	/**
	 * Gets the attribute value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText				Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck						Text to check at the specified object to check Text.
	 * @param rowObjectToGetAttributeValueFrom	Object Locator relative to the Row Object to get attribute value from if specified text is found on that row.
	 * @param attribute 						Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By parent, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute);
	
	/**
	 * Gets the attribute value of the specified Web Element from a row in a table based on text value from the same row.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Index of Parent List Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText				Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck						Text to check at the specified object to check Text.
	 * @param rowObjectToGetAttributeValueFrom	Object Locator relative to the Row Object to get attribute value from if specified text is found on that row.
	 * @param attribute 						Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementText(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToGetAttributeValueFrom, String attribute);
	
	/**
	 * Gets the attribute value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetAttributeValueFrom	Object Locator relative to the Row Object to get attribute value from if specified text is found on that row.
	 * @param attribute 						Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute);
	
	/**
	 * Gets the attribute value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param parent							Locator of Parent Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetAttributeValueFrom	Object Locator relative to the Row Object to get attribute value from if specified text is found on that row.
	 * @param attribute 						Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parent, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute);
	
	/**
	 * Gets the attribute value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.
	 * 
	 * @param parentList						Locator of Parent List Object
	 * @param parentIndex						Index of Parent List Object
	 * @param rowObjectList						Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckAttributeValue	Object Locator relative to the Row Object from which to check the specified attribute value.
	 * @param attributeToCheck					Attribute to check at the specified object to check value.
	 * @param valueToCheck						Attribute Value to check at the specified object to check value.
	 * @param rowObjectToGetAttributeValueFrom	Object Locator relative to the Row Object to get attribute value from if specified text is found on that row.
	 * @param attribute 						Attribute of Web Element to get the value from.
	 * @return	Retrieved Web Element attribute value.
	 */
	
	public String getAttributeValueFromTableRowElementBasedOnTableRowElementAttributeValue(By parentList, int parentIndex, By rowObjectList, By rowObjectToCheckAttributeValue, String attributeToCheck, String valueToCheck, By rowObjectToGetAttributeValueFrom, String attribute);
	
	/**
	 * Gets the selected option of the Drop-down List Web Element.
	 * 
	 * @param	locator Object used to locate Web Element to get selected option from.
	 * @return	Retrieved value of the Drop-down List Web Element.
	 */

	public String getDropDownListValue(By locator);
	
	/**
	 * Gets the selected option of the Drop-down List Child Web Element within the Parent Web Element.
	 * 
	 * @param parent	Locator of Parent Object
	 * @param child		Locator of Child Object to get value from.
	 * @return	Retrieved value of the Drop-down List Web Element.
	 */
	
	public String getDropDownListValue(By parent, By child);
	
	/**
	 * Accepts Javascript Alert
	 */

	public void acceptAlert();
	
	/**
	 * Cancels Javascript Alert
	 */

	public void cancelAlert();
	
	/**
	 * Simulates typing at Javascript Alert Text Box
	 * 
	 * @param inputText Text to enter.
	 */

	public void typeAlert(String inputText);
	
	/**
	 * Counts instances of a Web Element in a Page.
	 * 
	 * @param locator Object used to locate Web Element to count.
	 * @return Instance Count of the Web Element in the Page.
	 */
	
	public int count(By locator);
	
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
	 * Verifies Page URL of Web Page if partially equal to the expected URL.
	 * 
	 * @param	expectedUrl Expected Page URL to compare into
	 * @return	<code>true</code> if Page URL is partially equal to expected URL.
	 * 			<code>false</code> if Page URL is not partially equal to expected URL.
	 */

	public boolean seePartialUrl(String expectedUrl);
	
	/**
	 * Verifies Page URL of Web Page if not partially equal to the expected URL.
	 * 
	 * @param	expectedUrl Expected Page URL to compare into
	 * @return	<code>true</code> if Page URL is not partially equal to expected URL.
	 * 			<code>false</code> if Page URL is partially equal to expected URL.
	 */

	public boolean dontSeePartialUrl(String expectedUrl);
	
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
	 * Verifies partial Page Title of Web Page if equal to the expected Title.
	 * 
	 * @param	expectedPartialTitle Expected Partial Page Title to compare into
	 * @return	<code>true</code> if Page Title is equal to expected Title.
	 * 			<code>false</code> if Page Title is not equal to expected Title.
	 */
	
	public boolean seePartialTitle(String expectedPartialTitle);
	
	/**
	 * Verifies partial Page Title of Web Page if not equal to the expected Title.
	 * 
	 * @param	partialTitle Expected Partial Page Title to compare into
	 * @return	<code>true</code> if Page Title is not equal to expected Title.
	 * 			<code>false</code> if Page Title is equal to expected Title.
	 */
	
	public boolean dontSeePartialTitle(String partialTitle);

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
	
	public boolean partialTyped(By locator, String expectedPartialValue);
	
	public boolean didntPartiallyType(By locator, String partialValue);
	
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
	 * Verifies the value of the specified attribute if partially equal to the expected
	 * value.
	 * 
	 * @param locator		Object used to locate Web Element to assert the value from.
	 * @param attribute		Name of attribute to assert the value from.
	 * @param expectedValue	Expected value of the Web Element Attribute.
	 * @return	<code>true</code> if value is partially equal to expected attribute value.
	 * 			<code>false</code> if value is partially not equal to expected attribute value.
	 */

	public boolean seePartialAttributeValue(By locator, String attribute, String expectedValue);
	
	/**
	 * Verifies the value of the specified attribute if not partially equal to the specified
	 * value.
	 * 
	 * @param locator	Object used to locate Web Element to assert the value from.
	 * @param attribute	Name of attribute to assert the value from.
	 * @param value		Value of the Web Element Attribute.
	 * @return	<code>true</code> if value is partially not equal to expected attribute value.
	 * 			<code>false</code> if value is partially equal to expected attribute value.
	 */

	public boolean dontSeePartialAttributeValue(By locator, String attribute, String value);

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
	 * @param locator		Object used to locate Web Element to assert the text value from.
	 * @param expectedValue	Expected text value
	 * @return	<code>true</code> if value is equal to expected text value.
	 * 			<code>false</code> if value is not equal to expected text value.
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

	public boolean seeTextOfListElement(By locator, String textValue);
	
	/**
	 * Verifies if text value does not exist from texts on Web Element List.
	 * 
	 * @param locator	Object used to locate Web Element List to assert the text value from.
	 * @param textValue	Text value to verify
	 * @return	<code>true</code> if text value does not exist from texts of Web Element List.
	 * 			<code>false</code> if text value exists from texts of Web Element List.
	 */

	public boolean dontSeeTextOfListElement(By locator, String textValue);
	
	public boolean seeAttributeValueOfListElement(By locator, String attribute, String expectedValue);
	
	public boolean dontSeeAttributeValueOfListElement(By locator, String attribute, String value);

	public boolean seePartialAttributeValueOfListElement(By locator, String attribute, String expectedPartialValue);
	
	public boolean dontSeePartialAttributeValueOfListElement(By locator, String attribute, String partialValue);
	
	/**
	 * Verifies if text value exists from text of a Web Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedValue				Expected text value
	 * @return	<code>true</code> if value is equal to expected text value.
	 * 			<code>false</code> if value is not equal to expected text value.
	 */

	public boolean seeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies if text value does not exist from text of a Web Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param value						Text value to verify
	 * @return	<code>true</code> if value is not equal to expected text value.
	 * 			<code>false</code> if value is equal to expected text value.
	 */

	public boolean dontSeeTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value);
	
	public boolean seeAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String attribute, String expectedValue);
	
	public boolean dontSeeAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String attribute, String value);
	
	/**
	 * Verifies Web Element with text if equal to expected text value from a row in a table based on text value from the same row.
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
	 * Verifies Web Element with text if not equal to specified text value from a row in a table based on text value from the same row.
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
	
	public boolean seeAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String expectedValue);
	
	public boolean dontSeeAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String value);
	
	/**
	 * Verifies text value if it exists in a Web Element List from a row in a table based on text value from the same row.
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
	 * Verifies text value if it does not exist in a Web Element List from a row in a table based on text value from the same row.
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
	
	public boolean seeAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
	public boolean dontSeeAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
	/**
	 * Verifies Web Element with text if equal to expected partial text value.
	 * 
	 * @param locator		Object used to locate Web Element to assert the value from.
	 * @param expectedValue	Expected partial text value
	 * @return	<code>true</code> if value is equal to expected value.
	 * 			<code>false</code> if value is not equal to expected value.
	 */

	public boolean seePartialText(By locator, String expectedValue);
	
	/**
	 * Verifies Web Element with text if not equal to specified partial text value.
	 * 
	 * @param locator	Object used to locate Web Element to assert the value from.
	 * @param value		Partial text value to check
	 * @return	<code>true</code> if value is not equal to specified partial text value.
	 * 			<code>false</code> if value is equal to specified partial text value.
	 */

	public boolean dontSeePartialText(By locator, String value);
	
	/**
	 * Verifies if partial text value exists from texts on Web Element List.
	 * 
	 * @param locator			Object used to locate Web Element List to assert the text value from.
	 * @param partialTextValue	Text value to verify
	 * @return	<code>true</code> if partial text value exists from texts of Web Element List.
	 * 			<code>false</code> if partial text value does not exist from texts of Web Element List.
	 */

	public boolean seePartialTextOfListElement(By locator, String partialTextValue);
	
	/**
	 * Verifies if partial text value does not exist from texts on Web Element List.
	 * 
	 * @param locator			Object used to locate Web Element List to assert the text value from.
	 * @param partialTextValue	Text value to verify
	 * @return	<code>true</code> if partial text value does not exist from texts of Web Element List.
	 * 			<code>false</code> if partial text value exists from texts of Web Element List.
	 */

	public boolean dontSeePartialTextOfListElement(By locator, String partialTextValue);
	
	/**
	 * Verifies if partial text value exists from text of a Web Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param expectedPartialValue		Expected partial text value
	 * @return	<code>true</code> if value is equal to expected partial text value.
	 * 			<code>false</code> if value is not equal to expected partial text value.
	 */

	public boolean seePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String expectedPartialValue);
	
	/**
	 * Verifies if partial text value does not exist from text of a Web Element in a Table Row.
	 * 
	 * @param rowObjectList				Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToSeeTextFrom	Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @param value						Text value to verify
	 * @return	<code>true</code> if value is not equal to expected partial text value.
	 * 			<code>false</code> if value is equal to expected partial text value.
	 */

	public boolean dontSeePartialTextOfTableRowElement(By rowObjectList, By rowObjectToSeeTextFrom, String value);
	
	public boolean seePartialAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String attribute, String expectedPartialValue);
	
	public boolean dontSeePartialAttributeValueOfTableRowElement(By rowObjectList, By rowObjectToSeePartialTextFrom, String attribute, String value);
	
	/**
	 * Verifies Web Element with text if equal to expected partial text value from a row in a table based on text value from the same row.
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
	 * Verifies Web Element with text if not equal to specified partial text value from a row in a table based on text value from the same row.
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
	
	public boolean seePartialAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String expectedValue);
	
	public boolean dontSeePartialAttributeValueOfTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSeeTextFrom, String attribute, String value);
	
	/**
	 * Verifies partial text value if it exists in a Web Element List from a row in a table based on text value from the same row.
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
	 * Verifies partial text value if it does not exist in a Web Element List from a row in a table based on text value from the same row.
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
	
	public boolean seePartialAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
	public boolean dontSeePartialAttributeValueOfTableRowListElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectListToSeeTextFrom, String expectedValue);
	
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
	 * Verifies Web Element is displayed from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToSee		Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @return	<code>true</code> if Web Element is displayed on Web Page Table.
	 * 			<code>false</code> if Web Element is not displayed on Web Page Table
	 */

	public boolean seeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
	/**
	 * Verifies Web Element is not displayed from a row in a table based on text value from the same row.
	 * 
	 * @param rowObjectList			Object Locator that corresponds to the Rows from the Table.
	 * @param rowObjectToCheckText	Object Locator relative to the Row Object from which to check the specified text.
	 * @param textToCheck			Text to check at the specified object to check Text.
	 * @param rowObjectToSee		Object Locator relative to the Row Object to assert text value from if specified text is found on that row.
	 * @return	<code>true</code> if Web Element is not displayed on Web Page Table.
	 * 			<code>false</code> if Web Element is displayed on Web Page Table.
	 */

	public boolean dontSeeTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
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
	
	public boolean seeEnabledOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
	public boolean dontSeeEnabledOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
	public boolean seeSelectedOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
	public boolean dontSeeSelectedOnTableRowElementBasedOnTableRowElementText(By rowObjectList, By rowObjectToCheckText, String textToCheck, By rowObjectToSee);
	
	/**
	 * Verifies Web Element Instance count is equal to expected count.
	 * 
	 * @param locator	Object used to locate Web Element to assert.
	 * @param count		Expected Web Element instance count.
	 * @return	<code>true</code> if count is equal to expected count.
	 * 			<code>false</code> if count is not equal to expected count.
	 */
	
	public boolean counted(By locator, int count);
	
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
