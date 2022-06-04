package com.github.abagabagon.verifico.automation.web;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebElementFactory {

	private Logger log;
	private WaitCommands wait;

	public WebElementFactory(WaitCommands wait) {
		this.log = LogManager.getLogger(this.getClass());
		this.wait = wait;
	}

	private enum ValueCheckType {
		TEXT, ATTRIBUTE
	}

	private WebElement getElementBasedOnReference(ValueCheckType valueCheckType, List<WebElement> elementList, String attribute, String searchValue) {
		int size = elementList.size();
		boolean flgTextFound = false;
		boolean status = false;
		String retrievedValue = null;
		WebElement element = null;
		for(int i = 0; i < size; i++) {
			element = elementList.get(i);
			switch(valueCheckType) {
			case TEXT:
				retrievedValue = element.getText().trim();
				status = retrievedValue.contains(searchValue);
				break;
			case ATTRIBUTE:
				retrievedValue = element.getAttribute(attribute).trim();
				status = retrievedValue.contains(searchValue);
				break;
			default:
				this.log.fatal("Unsupported Value Check Type.");
			}
			if (status) {
				flgTextFound = true;
				this.log.debug(searchValue + "\" " + valueCheckType + " value is found from the Web Element List: \""
						+ elementList.toString() + "\".");
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error(searchValue + "\" " + valueCheckType
					+ " value is not found from one of the Web Elements from the Web Element List: \""
					+ elementList.toString()
					+ "\".");
		}
		return element;
	}

	private WebElement getElementBasedOnReference(ValueCheckType valueCheckType, List<WebElement> parentList, By referenceElement, By elementToCreate, String attribute, String searchValue) {
		int size = parentList.size();
		boolean flgTextFound = false;
		boolean status = false;
		String retrievedValue = null;
		WebElement elementReference = null;
		WebElement elementCreated = null;
		for(int i = 0; i < size; i++) {
			WebElement parentElement = parentList.get(i);
			elementReference = this.wait.waitForNestedElementToBePresent(parentElement, referenceElement);
			switch(valueCheckType) {
			case TEXT:
				retrievedValue = elementReference.getText().trim();
				status = retrievedValue.contains(searchValue);
				break;
			case ATTRIBUTE:
				retrievedValue = elementReference.getAttribute(attribute).trim();
				status = retrievedValue.contains(searchValue);
				break;
			default:
				this.log.fatal("Unsupported Value Check Type.");
			}
			if (status) {
				flgTextFound = true;
				elementCreated = this.wait.waitForNestedElementToBePresent(parentElement, elementToCreate);
				this.log.debug(searchValue + "\" " + valueCheckType + " value is found from the Web Element List: \""
						+ parentList.toString() + "\".");
				break;
			}
		}
		if (!flgTextFound) {
			this.log.error(searchValue + "\" " + valueCheckType
					+ " value is not found from one of the Web Elements from the Web Element List: \""
					+ parentList.toString() + "\".");
		}
		return elementCreated;
	}

	/**
	 * Creates Web Element based on the specified Locator.
	 *
	 * @param locator Locator of Web Element to create.
	 * @return Created Web Element based on the Locator.
	 */

	public final WebElement createElement(By locator) {
		WebElement element = this.wait.waitForElementToBePresent(locator);
		return element;
	}

	/**
	 * Creates Web Element based on the specified Child Locator within the context
	 * of the Web Element of the specified Parent Locator.
	 *
	 * @param parent Locator of Parent Web Element
	 * @param child  Locator of Child Web Element to create.
	 * @return Created Web Element based on the Locator.
	 */

	public final WebElement createElement(By parent, By child) {
		WebElement nestedElement = this.wait.waitForNestedElementToBePresent(parent, child);
		return nestedElement;
	}

	/**
	 * Creates Web Element based on the specified Child Locator within the context
	 * of the Parent Web Element.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of Child Web Element to create.
	 * @return Created Web Element based on the Locator.
	 */

	public final WebElement createElement(WebElement parent, By child) {
		WebElement nestedElement = this.wait.waitForNestedElementToBePresent(parent, child);
		return nestedElement;
	}

	/**
	 * Creates Web Element from the specified List Web Element by iterating thru all
	 * Web Elements and checking if the element's text matches the text reference
	 * value. Web Element that first matches the text will be returned.
	 *
	 * Note: Command is useful for manipulating a specific Web Element from a List
	 * Web Element.
	 *
	 * @param elementList        List Web Element to create a Web Element from based
	 *                           on the text reference value.
	 * @param textReferenceValue Text reference to check for each Web Element of the
	 *                           List Web Element.
	 * @return Created Web Element from the List Web Element based on the text
	 *         reference value.
	 */

	public final WebElement createElement(List<WebElement> elementList, String textReferenceValue) {
		WebElement element = this.getElementBasedOnReference(ValueCheckType.TEXT, elementList, null, textReferenceValue);
		return element;
	}

	/**
	 * Creates Web Element from the specified List Web Element by iterating thru all
	 * Web Elements and checking if the element's attribute value matches the
	 * attribute reference value. Web Element that first matches the attribute value
	 * will be returned.
	 *
	 * Note: Command is useful for manipulating a specific Web Element from a List
	 * Web Element.
	 *
	 * @param elementList             List Web Element to create a Web Element from
	 *                                based on the attribute reference value.
	 * @param attribute               Attribute of the Web Element to check value
	 *                                of.
	 * @param attributeReferenceValue Attribute reference to check for each Web
	 *                                Element of the List Web Element.
	 * @return Created Web Element from the List Web Element based on the attribute
	 *         reference value.
	 */

	public final WebElement createElement(List<WebElement> elementList, String attribute, String attributeReferenceValue) {
		WebElement element = this.getElementBasedOnReference(ValueCheckType.ATTRIBUTE, elementList, attribute, attributeReferenceValue);
		return element;
	}

	/**
	 * Creates a nested Child Web Element from one of the Web Elements of the
	 * specified List Web Element by iterating thru all Web Elements and checking if
	 * the text of the nested Child Reference Web Element matches the text reference
	 * value. Child Web Element to create under a Web Element from the List Web
	 * Element that matches the text of the Child Reference Element will be
	 * returned.
	 *
	 * <br>
	 * <table summary="Employee Table" border="1">
	 * <thead>
	 * <tr>
	 * <th>Employee Name</th>
	 * <th>Link</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>John Smith</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>Jane Doe</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>John Appleseed</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <br>
	 *
	 * At the example above, user may want to manipulate the link specific to an
	 * employee name. Using the command, the parameters should be set as follows:
	 *
	 * <ul>
	 * <li>The Parent List Web Element is the List Web Element for the rows of the
	 * table.</li>
	 * <li>The Child Web Element Reference is the employee name column value.</li>
	 * <li>The Child Web Element to Create is the link column.</li>
	 * <li>The Text Reference Value will depend on the specific row the user intends
	 * to manipulate (e. g. "Jane Doe").</li>
	 * </ul>
	 *
	 *
	 * Note: As shown above, command is useful for manipulating a specific Web
	 * Elements in a table.
	 *
	 * @param parentList           List Web Element to create a Child Web Element
	 *                             from based on the text reference value of the
	 *                             Child Web Element Reference.
	 * @param childReference       Locator of the Child Web Element Reference.
	 * @param childElementToCreate Locator of the Child Web Element to create.
	 * @param textReferenceValue   Text reference to check at the Child Web Element
	 *                             Reference for each Web Element of the Parent List
	 *                             Web Element.
	 * @return Created Web Element from the List Web Element based on the Child Web
	 *         Element Reference text value.
	 */

	public final WebElement createElement(List<WebElement> parentList, By childReference, By childElementToCreate, String textReferenceValue) {
		WebElement element = this.getElementBasedOnReference(ValueCheckType.TEXT, parentList, childReference, childElementToCreate, null, textReferenceValue);
		return element;
	}

	/**
	 * Creates a nested Child Web Element from one of the Web Elements of the
	 * specified List Web Element by iterating thru all Web Elements and checking if
	 * the attribute value of the nested Child Reference Web Element matches the
	 * attribute reference value. Child Web Element to create under a Web Element
	 * from the List Web Element that matches the attribute value of the Child
	 * Reference Element will be returned.
	 *
	 * <br>
	 * <table summary="Employee Table" border="1">
	 * <thead>
	 * <tr>
	 * <th>Employee Name</th>
	 * <th>Link</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>John Smith</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>Jane Doe</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>John Appleseed</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <br>
	 *
	 * At the example above, user may want to manipulate the link specific to an
	 * employee name. Using the command, the parameters should be set as follows:
	 *
	 * <ul>
	 * <li>The Parent List Web Element is the List Web Element for the rows of the
	 * table.</li>
	 * <li>The Child Web Element Reference is the employee name column value.</li>
	 * <li>The Child Web Element to Create is the link column.</li>
	 * <li>The attribute will be the reference attribute of the Child Web Element
	 * Reference.</li>
	 * <li>The Attribute Reference Value will depend on the specific row the user
	 * intends to manipulate.</li>
	 * </ul>
	 *
	 *
	 * Note: As shown above, command is useful for manipulating a specific Web
	 * Elements in a table.
	 *
	 * @param parentList              List Web Element to create a Child Web Element
	 *                                from based on the text reference value of the
	 *                                Child Web Element Reference.
	 * @param childReference          Locator of the Child Web Element Reference.
	 * @param childElementToCreate    Locator of the Child Web Element to create.
	 * @param attribute               Attribute of the Child Web Element Reference
	 *                                to check value of.
	 * @param attributeReferenceValue Attribute reference value to check at the
	 *                                Child Web Element Reference for each Web
	 *                                Element of the Parent List Web Element.
	 * @return Created Web Element from the List Web Element based on the Child Web
	 *         Element Reference attribute value.
	 */

	public final WebElement createElement(List<WebElement> parentList, By childReference, By childElementToCreate, String attribute, String attributeReferenceValue) {
		WebElement element = this.getElementBasedOnReference(ValueCheckType.ATTRIBUTE, parentList, childReference, childElementToCreate, attribute, attributeReferenceValue);
		return element;
	}

	/**
	 * Under the context of an ancestor Web Element, creates a nested Child Web
	 * Element from one of the Web Elements of the specified List Web Element by
	 * iterating thru all Web Elements and checking if the text of the nested Child
	 * Reference Web Element matches the text reference value. Child Web Element to
	 * create under a Web Element from the List Web Element that matches the text of
	 * the Child Reference Element will be returned.
	 *
	 * <br>
	 * <table summary="Employee Table" border="1">
	 * <thead>
	 * <tr>
	 * <th>Employee Name</th>
	 * <th>Link</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>John Smith</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>Jane Doe</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>John Appleseed</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <br>
	 *
	 * <table summary="Resigned Employee Table" border="1">
	 * <thead>
	 * <tr>
	 * <th>Resigned Employee Name</th>
	 * <th>Link</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>John Hamilton</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>George Washington</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * At the example above, user may want to manipulate the first table and the
	 * link specific to an employee name. Using the command, the parameters should
	 * be set as follows:
	 *
	 * <ul>
	 * <li>The Ancestor Web Element is Web Element of the first table.</li>
	 * <li>The Parent List Web Element is the List Web Element for the rows of the
	 * table.</li>
	 * <li>The Child Web Element Reference is the employee name column value.</li>
	 * <li>The Child Web Element to Create is the link column.</li>
	 * <li>The Text Reference Value will depend on the specific row the user intends
	 * to manipulate (e. g. "Jane Doe").</li>
	 * </ul>
	 *
	 *
	 * Note: As shown above, command is useful for manipulating a specific Web
	 * Elements in a table.
	 *
	 * @param ancestor             Web Element that limits context of the page
	 * @param parentList           List Web Element to create a Child Web Element
	 *                             from based on the text reference value of the
	 *                             Child Web Element Reference.
	 * @param childReference       Locator of the Child Web Element Reference.
	 * @param childElementToCreate Locator of the Child Web Element to create.
	 * @param textReferenceValue   Text reference to check at the Child Web Element
	 *                             Reference for each Web Element of the Parent List
	 *                             Web Element.
	 * @return Created Web Element from the List Web Element based on the Child Web
	 *         Element Reference text value.
	 */

	public final WebElement createElement(WebElement ancestor, By parentList, By childReference, By childElementToCreate, String textReferenceValue) {
		List<WebElement> parentElementList = this.wait.waitForNestedListElementToBeVisible(ancestor, parentList);
		WebElement element = this.getElementBasedOnReference(ValueCheckType.TEXT, parentElementList, childReference, childElementToCreate, null, textReferenceValue);
		return element;
	}

	/**
	 * Under the context of an ancestor Web Element, creates a nested Child Web
	 * Element from one of the Web Elements of the specified List Web Element by
	 * iterating thru all Web Elements and checking if the attribute value of the
	 * nested Child Reference Web Element matches the attribute reference value.
	 * Child Web Element to create under a Web Element from the List Web Element
	 * that matches the attribute value of the Child Reference Element will be
	 * returned.
	 *
	 * <br>
	 * <table summary="Employee Table" border="1">
	 * <thead>
	 * <tr>
	 * <th>Employee Name</th>
	 * <th>Link</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>John Smith</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>Jane Doe</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>John Appleseed</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <br>
	 *
	 * <table summary="Resigned Employee Table" border="1">
	 * <thead>
	 * <tr>
	 * <th>Resigned Employee Name</th>
	 * <th>Link</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>John Hamilton</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * <tr>
	 * <td>George Washington</td>
	 * <td><a>Link</a></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * At the example above, user may want to manipulate the first table and the
	 * link specific to an employee name. Using the command, the parameters should
	 * be set as follows:
	 *
	 * <ul>
	 * <li>The Ancestor Web Element is Web Element of the first table.</li>
	 * <li>The Parent List Web Element is the List Web Element for the rows of the
	 * table.</li>
	 * <li>The Child Web Element Reference is the employee name column value.</li>
	 * <li>The Child Web Element to Create is the link column.</li>
	 * <li>The attribute will be the reference attribute of the Child Web Element
	 * Reference.</li>
	 * <li>The Attribute Reference Value will depend on the specific row the user
	 * intends to manipulate.</li>
	 * </ul>
	 *
	 *
	 * Note: As shown above, command is useful for manipulating a specific Web
	 * Elements in a table.
	 *
	 * @param ancestor                Web Element that limits context of the page
	 * @param parentList              List Web Element to create a Child Web Element
	 *                                from based on the text reference value of the
	 *                                Child Web Element Reference.
	 * @param childReference          Locator of the Child Web Element Reference.
	 * @param childElementToCreate    Locator of the Child Web Element to create.
	 * @param attribute               Attribute of the Child Web Element Reference
	 *                                to check value of.
	 * @param attributeReferenceValue Attribute reference value to check at the
	 *                                Child Web Element Reference for each Web
	 *                                Element of the Parent List Web Element.
	 * @return Created Web Element from the List Web Element based on the Child Web
	 *         Element Reference attribute value.
	 */

	public final WebElement createElement(WebElement ancestor, By parentList, By childReference, By childElementToCreate, String attribute, String attributeReferenceValue) {
		List<WebElement> parentElementList = this.wait.waitForNestedListElementToBeVisible(ancestor, parentList);
		WebElement element = this.getElementBasedOnReference(ValueCheckType.ATTRIBUTE, parentElementList, childReference, childElementToCreate, attribute, attributeReferenceValue);
		return element;
	}

	/**
	 * Creates List Web Element based on the specified Locator.
	 *
	 * @param locator Locator of List Web Element to create.
	 * @return Created List Web Element based on the Locator.
	 */

	public final List<WebElement> createListElement(By locator) {
		List<WebElement> elements = this.wait.waitForListElementToBePresent(locator);
		return elements;
	}

	/**
	 * Creates List Web Element based on the specified Child Locator within the
	 * context of the Web Element of the specified Parent Locator.
	 *
	 * @param parent Locator of Parent Web Element
	 * @param child  Locator of Child List Web Element to create.
	 * @return Created Web Element based on the Locator.
	 */

	public final List<WebElement> createListElement(By parent, By child) {
		List<WebElement> nestedElement = this.wait.waitForNestedListElementToBePresent(parent, child);
		return nestedElement;
	}

	/**
	 * Creates List Web Element based on the specified Child Locator within the
	 * context of the Parent Web Element.
	 *
	 * @param parent Parent Web Element
	 * @param child  Locator of Child List Web Element to create.
	 * @return Created Web Element based on the Locator.
	 */

	public final List<WebElement> createListElement(WebElement parent, By child) {
		List<WebElement> nestedElement = this.wait.waitForNestedListElementToBeVisible(parent, child);
		return nestedElement;
	}

}