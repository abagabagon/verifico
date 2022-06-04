[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maven Central](https://img.shields.io/maven-central/v/com.github.abagabagon/verifico.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.abagabagon%22%20AND%20a:%22verifico%22)

# **Background**
One of the disadvantages of Selenium and Appium is the steep learning curve required for users to be able to implement it. One also have to go over issues such as automation test flakiness, unhandled exceptions, etc. so they could learn how to properly implement commands of Selenium and Appium. The project is built in order to eliminate this issue.

# **Components**

| Component         | Java API                                                                                                       |
| ----------------- | -------------------------------------------------------------------------------------------------------------- |
| Web Automation    | [Selenium](https://www.seleniumhq.org/download/)                                                               |
| Mobile Automation | [Appium](http://appium.io/)                                                                                    |
| Logging Tool      | [Log4J2](https://logging.apache.org/log4j/2.0/download.html)                                                   |
| Reporting Tool    | [ExtentReports](http://relevantcodes.com/extentreports-for-selenium/)                                          |
| Excel             | [Apache POI](https://poi.apache.org/download.html)                                                             |
| MySQL             | [MySQL Connector](https://dev.mysql.com/doc/connectors/en/)                                                    |
| MSSQL             | [MSSQL Connector](https://docs.microsoft.com/en-us/sql/connect/sql-connection-libraries?view=sql-server-ver15) |
| MariaDB           | [MariaDB Connector](https://mariadb.com/docs/connect/programming-languages/java/)                              |

# **Web Automation**

For Web Application Automation, create an instance of the `WebDriverFactory` Class to initialize and get WebDrivers:

```java
WebDriverFactory driverFactory = new WebDriverFactory();
driverFactory.setChromeDriver();
WebDriver driver = driverFactory.getChromeDriver();
```

Supported WebDrivers are as follows
* Google Chrome
* Mozilla Firefox
* Safari
* Microsoft Edge
* Internet Explorer

Wait Commands are also available at the `WaitCommands` Class by which functions are utilized by other Automation Command Classes. User will only need to initialize Implicit and Explict Waits. The instance of the `WaitCommands` Class will be utilized by Automation Command Classes which are discussed on below other sections.

```
int implicitWaitDuration = 10;
int explicitWaitDuration = 5;
WaitCommands wait = new WaitCommands(driver, implicitWaitDuration, explicitWaitDuration);
```

Web Application Automation Commands are accessible into classes from which related commands are grouped. Note that for commands that manipulates Web Elements, variations of the commands are created to accommodate actions for target Web Elements that are nested (e. g. tables, lists, etc.). Available Command Classes are shown below:

## **Browser Commands**

Browser Commands contains functions relating to actions being done by the user at the Web Browser. Class name for this is `BrowserCommands`.

`BrowserCommands` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands`:

```java
BrowserCommands browser = new BrowserCommands(driver, wait);
browser.goTo("https://www.google.com/");
browser.maximize();
browser.refresh();
```

| Command					| Description												|
| --------------------------	|-------------------------------------------------------------	|
| `openTab`					| Opens Tab													|
| `goTo`					| Navigates to the Url specified								|
| `switchTabByTitle`		| Switches to a Tab based on Page Title						|
| `switchTabByURL`			| Switches to a Tab based on Page URL							|
| `switchTabToOriginal`	| Switches back to Original Tab								|
| `back`					| Navigates one item back from the browser's history			|
| `forward`					| Navigates one item forward from the browser's history		|
| `refresh`					| Refreshes current page										|
| `maximize`				| Maximizes Browser Window									|
| `deleteAllCookies`		| Deletes all cookies										|
| `deleteCookie`			| Deletes cookie with the specified name						|
| `scroll`					| Scrolls Page												|
| `closeTab`				| Closes Tab of a Web Browser									|
| `closeBrowser`			| Closes Web Browser											|
| `count`					| Counts instance of the Web Element of the specified Locator	|

## **Mouse Commands**

Mouse Commands contains functions pertaining to mouse actions done by a user at a Web Page. Class for this is `MouseCommands`.

`MouseCommands` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands` Classes:

```java
MouseCommands mouse = new MouseCommands(driver, wait);
browser.goTo("https://www.google.com/");
mouse.click(By.xpath("//button[@name='Search']"));
```

| Command			| Description																					|
| ------------------	| -------------------------------------------------------------------------------------------------	|
| `point`			| Points mouse to the Web Element of the specified Locator.										|
| `click`			| Clicks the Web Element of the specified Locator.												|
| `clickJS`			| Clicks the Web Element of the specified Locator using Javascript.								|
| `clickAndHold`	| Clicks and holds the Web Element of the specified Locator.										|
| `doubleClick`		| Double-clicks the Web Element of the specified Locator.											|
| `dragAndDrop`		| Drags a Web Element and drops it at target Web Element. Used for Elements that can be dragged.	|

## **Keyboard Commands**

Keyboard Commands contains functions pertaining to keyboard actions done by a user in a Web Page. Class for this is `KeyboardCommands`.

`KeyboardCommands` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands` Classes:

```java
KeyboardCommands keyboard = new KeyboardCommands(driver, wait);
browser.goTo("https://www.google.com/");
keyboard.type(By.xpath("//input[@name='username']"), "user@sample.com");
keyboard.type(By.xpath("//input[@name='password']"), "password123");
```

| Command	| Description																												|
| ----------	| ------------------------------------------------------------------------------------------------------------------------------	|
| `type`	| Types the specified input text to the Web Element of the specified Locator. Applicable for INPUT and TEXTAREA Web Elements.	|
| `press`	| Simulates pressing of characters into the Web Element of the specified Locator.												|
| `clear`	| Clears value of the Web Element of the specified Locator. Applicable for INPUT and TEXTAREA Web Elements.						|

## **Select Commands**

Select Commands contains functions pertaining to actions done by a user on drop-down elements in a Web Page. Class for this is `SelectCommands`.

`SelectCommands` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands` Classes:

```java
SelectCommands select = new SelectCommands(driver, wait);
browser.goTo("https://www.google.com/");
select.select(By.xpath("//select[@type='member-type']"), "Guest");
```

| Command		| Description															|
| --------------	| ------------------------------------------------------------------------	|
| `select`		| Selects a Drop-down List Web Element Option of the specified Locator.	|
| `deselect`	| De-selects a Drop-down List Web Element Option of the specified Locator.	|

## **Get Commands**

Get Commands contains functions pertaining to get value actions done by a user in a Web Page. Class for this is `GetCommands`.

`GetCommands` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands` Classes:

```java
GetCommands get = new GetCommands(driver, wait);
browser.goTo("https://www.google.com/");
get.getText(By.xpath("//h1[@name='Login Header']"));
```

| Command					| Description																|
| --------------------------	| ----------------------------------------------------------------------------	|
| `getText`					| Gets the text of the Web Element of the specified Locator.					|
| `getAttributeValue`		| Gets the attribute value of the Web Element of the specified Locator.		|
| `getDropDownListValue`	| Gets the drop-down list value of the Web Element of the specified Locator.	|

## **Alert Commands**

Alert Commands contains functions pertaining to get value actions done by a user in a Web Page. Class for this is `AlertCommands`.

`AlertCommands` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands` Classes:

```java
AlertCommands alert = new AlertCommands(driver, wait);
mouse.click(By.xpath("//input[@id='alertbtn']"));
alert.typeAlert("John");
alert.acceptAlert();
```

| Command		| Description									|
| --------------	| -----------------------------------------------	|
| `acceptAlert`	| Accepts Javascript Alert						|
| `cancelAlert`	| Cancels Javascript Alert						|
| `typeAlert`	| Simulates typing at Javascript Alert Text Box	|

## **Value Assertions**

Value Assertions contains functions pertaining to checking of values done by a user in a Web Page. Class for this is `ValueAssertions`.

`ValueAssertions` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands` Classes:

```java
ValueAssertions value = new ValueAssertions(driver, wait);
browser.goTo("https://www.google.com/");
Assert.assertTrue(value.seeUrl("https://www.google.com/"));
keyboard.type(By.xpath("//input[@id='search-box']"), "verifico");
Assert.assertTrue(value.seeAttributeValue(By.xpath("//input[@id='search-box']"), "value", "verifico"));
```

| Command							| Description																											|
| -----------------------------------	| --------------------------------------------------------------------------------------------------------------------------	|
| `seeUrl`							| Verifies Page URL of Web Page if equal to the expected URL.																|
| `dontSeeUrl`						| Verifies Page URL of Web Page if not equal to the specified URL.															|
| `seePartialUrl`					| Verifies Page URL of Web Page if partially equal to the expected URL.													|
| `dontSeePartialUrl`				| Verifies Page URL of Web Page if not partially equal to the expected URL.												|
| `seeTitle`						| Verifies Page Title of Web Page if equal to the expected Title.															|
| `dontSeeTitle`					| Verifies Page Title of Web Page if not equal to the specified Title.														|
| `seePartialTitle`					| Verifies Page Title of Web Page if partially equal to the expected Title.												|
| `dontSeePartialTitle`			| Verifies Page Title of Web Page if not partially equal to the specified Title.											|
| `seeAttributeValue`				| Verifies the attribute value of the Web Element of the specified Locator if equal to the expected value.					|
| `dontSeeAttributeValue`			| Verifies the attribute value of the Web Element of the specified Locator if not equal to the expected value.				|
| `seePartialAttributeValue`		| Verifies the attribute value of the Web Element of the specified Locator if partially equal to the expected value.			|
| `dontSeePartialAttributeValue`	| Verifies the attribute value of the Web Element of the specified Locator if not partially equal to the expected value.		|
| `seeText`							| Verifies the text value of the Web Element of the specified Locator if equal to the expected value.						|
| `dontSeeText`						| Verifies the text value of the Web Element of the specified Locator if not equal to the expected value.					|
| `seePartialText`					| Verifies the text value of the Web Element of the specified Locator if partially equal to the expected value.				|
| `dontSeePartialText`				| Verifies the text value of the Web Element of the specified Locator if not partially equal to the expected value.			|
| `seeDropDownValue`				| Verifies the dropdown value of the Web Element of the specified Locator if equal to the expected value.					|
| `dontSeeDropdownValue`			| Verifies the dropdown value of the Web Element of the specified Locator if not equal to the expected value.				|
| `seePartialDropdownValue`		| Verifies the dropdown value of the Web Element of the specified Locator if equal to the expected value.					|
| `dontSeePartialDropdownValue`	| Verifies the dropdown value of the Web Element of the specified Locator if not partially equal to the expected value.		|
| `counted`							| Verifies Web Element Instance count is equal to expected count.															|
| `seeAlertMessage`					| Verifies Javascript Alert Message displayed if equal to expected message													|

## **State Assertions**

State Assertions contains functions pertaining to checking the state of Web Elements done by a user in a Web Page. Class for this is `StateAssertions`.

`StateAssertions` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands` Classes:

```java
StateAssertions state = new StateAssertions(driver, wait);
browser.goTo("https://www.practicesite.com/");
Assert.assertTrue(state.see(By.xpath("//input[@name='show-hide-text']")));
mouse.click(By.xpath(//input[@id='hide-textbox']));
Assert.assertTrue(state.dontSee(By.xpath("//input[@name='show-hide-text']")));
```

| Command			| Description																																|
| ------------------	| -----------------------------------------------------------------------------------------------------------------------------------------------	|
| `see`				| Verifies if Web Element of specified Locator is displayed on Web Page.																		|
| `dontSee`			| Verifies if Web Element of specified Locator is not displayed on Web Page.																	|
| `seeEnabled`		| Verifies if Web Element of specified Locator is enabled on Web Page.																			|
| `seeDisabled`		| Verifies if Web Element of specified Locator is disabled on Web Page.																		|
| `seeSelected`		| Verifies if Web Element of specified Locator within the context of the Web Element of the specified Parent Locator is selected on Web Page.		|
| `seeDeselected`	| Verifies if Web Element of specified Locator is deselected on Web Page.																		|

# **Excel Data**

```java
String filePath = "./src/resources/data/excel/staff-accounts.XLS";
ExcelData excelData = new ExcelData(filePath);
```

# **SQL Data**

```java
String dbServer = "127.0.0.1";
String dbName = "northwind";
String user = "abagabagon";
String password = "ABCabc123";
boolean isSslUsed = true;

SQLData sqlData = new SQLData(SQL.MySQL, dbServer, dbName, user, password, isSslUsed);
```
