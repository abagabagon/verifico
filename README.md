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
// WebDriverFactory
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
// WaitCommand
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

Browser Commands available are as follows:

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
keyboard.type(By.xpath("//input[@name='username']"));
keyboard.type(By.xpath("//input[@name='password']"));
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
| -----------	| -----------------------------------------------	|
| `acceptAlert`	| Accepts Javascript Alert						|
| `cancelAlert`	| Cancels Javascript Alert						|
| `typeAlert`	| Simulates typing at Javascript Alert Text Box	|

## **Value Assertions**

| Command                                                                          | Description                                                                                                                              |
| -------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| See Url                                                                          | Verifies Page URL of Web Page if equal to the expected URL.                                                                              |
| Don't See Url                                                                    | Verifies Page URL of Web Page if not equal to the specified URL.                                                                         |
| See Partial Url                                                                  | Verifies Page URL of Web Page if partially equal to the expected URL.                                                                    |
| Don't See Partial Url                                                            | Verifies Page URL of Web Page if not partially equal to the expected URL.                                                                |
| See Title                                                                        | Verifies Page Title of Web Page if equal to the expected Title.                                                                          |
| Don't See Title                                                                  | Verifies Page Title of Web Page if not equal to the specified Title.                                                                     |
| See Partial Title                                                                | Verifies Page Title of Web Page if partially equal to the expected Title.                                                                |
| Don't See Partial Title                                                          | Verifies Page Title of Web Page if not partially equal to the specified Title.                                                           |
| Typed                                                                            | Verifies the value of INPUT or TEXTAREA Web Elements if equal to the expected value.                                                     |
| Didn't Type                                                                      | Verifies the value of INPUT or TEXTAREA Web Elements if not equal to the specified value.                                                |
| See Attribute Value                                                              | Verifies the value of the specified attribute if equal to the expected value.                                                            |
| Don't See Attribute Value                                                        | Verifies the value of the specified attribute if equal to the expected value.                                                            |
| See Partial Attribute Value                                                      | Verifies the value of the specified attribute if partially equal to the expected value.                                                  |
| Don't See Partial Attribute Value                                                | Verifies the value of the specified attribute if not partially equal to the expected value.                                              |
| Selected DropDown                                                                | Verifies Drop-down List Web Element Value if equal to expected text value.                                                               |
| See Text                                                                         | Verifies Web Element with text if equal to expected text value.                                                                          |
| Don't See Text                                                                   | Verifies Web Element with text if not equal to specified text value.                                                                     |
| See Text Of List Element                                                         | Verifies if text value exists from texts on Web Element List.                                                                            |
| Don't See Text Of List Element                                                   | Verifies if text value does not exist from texts on Web Element List.                                                                    |
| See Text Of Table Row Element                                                    | Verifies if text value exists from text of a Web Element in a Table Row.                                                                 |
| Don't See Text Of Table Row Element                                              | Verifies if text value does not exist from text of a Web Element in a Table Row.                                                         |
| See Text Of Table Row Element Based On Table Row Element Text                    | Verifies Web Element with text if equal to expected text value from a row in a table based on text value from the same row.              |
| Don't See Text Of Table Row Element Based On Table Row Element Text              | Verifies Web Element with text if not equal to specified text value from a row in a table based on text value from the same row.         |
| See Text Of Table Row List Element Based On Table Row Element Text               | Verifies text value if it exists in a Web Element List from a row in a table based on text value from the same row.                      |
| Don't See Text Of Table Row List Element Based On Table Row Element Text         | Verifies text value if it does not exist in a Web Element List from a row in a table based on text value from the same row.              |
| See Partial Text                                                                 | Verifies Web Element with text if equal to expected partial text value.                                                                  |
| Don't See Partial Text                                                           | Verifies Web Element with text if not equal to specified partial text value.                                                             |
| See Partial Text Of List Element                                                 | Verifies if text value exists from texts on Web Element List.                                                                            |
| Don't See Partial Text Of List Element                                           | Verifies if text value does not exist from texts on Web Element List.                                                                    |
| See Partial Text Of Table Row Element                                            | Verifies if partial text value exists from text of a Web Element in a Table Row.                                                         |
| Don't See Partial Text Of Table Row Element                                      | Verifies if partial text value does not exist from text of a Web Element in a Table Row.                                                 |
| See Partial Text Of Table Row Element Based On Table Row Element Text            | Verifies Web Element with text if equal to expected partial text value from a row in a table based on text value from the same row.      |
| Don't See Partial Text Of Table Row Element Based On Table Row Element Text      | Verifies Web Element with text if not equal to specified partial text value from a row in a table based on text value from the same row. |
| See Partial Text Of Table Row List Element Based On Table Row Element Text       | Verifies partial text value if it exists in a Web Element List from a row in a table based on text value from the same row.              |
| Don't See Partial Text Of Table Row List Element Based On Table Row Element Text | Verifies partial text value if it does not exist in a Web Element List from a row in a table based on text value from the same row.      |
| See                                                                              | Verifies if Web Element is displayed on Web Page                                                                                         |
| Don't See                                                                        | Verifies Web Element is not displayed on the Web Page                                                                                    |
| See Table Row Element Based On Table Row Element Text                            | Verifies Web Element is displayed from a row in a table based on text value from the same row.                                           |
| Don't See Table Row Element Based On Table Row Element Text                      | Verifies Web Element is not displayed from a row in a table based on text value from the same row.                                       |
| See Enabled                                                                      | Verifies Web Element is enabled on the Web Page                                                                                          |
| See Disabled                                                                     | Verifies Web Element is disabled on the Web Page                                                                                         |
| Selected                                                                         | Verifies Web Element is selected on the Web Page. Used for Check Boxes and Radio Buttons                                                 |
| Deselected                                                                       | Verifies Web Element is not selected on the Web Page. Used for Check Boxes and Radio Buttons                                             |
| Counted                                                                          | Verifies Web Element Instance count is equal to expected count.                                                                          |
| See Alert Message                                                                | Verifies Javascript Alert Message displayed if equal to expected message                                                                 |

                                                                                      |

# **Excel Data**

```java
Excel excelType = Excel.XLS;
String filePath = "./src/resources/data/excel/staff-accounts.XLS";

Verifico verifico = new Verifico();
ExcelData excelData = verifico.getExcelData(excelType, filePath);
```

# **SQL Data**

```java
SQL sqlType = "MySQL";
String dbServer = "127.0.0.1";
String dbName = "northwind";
String user = "abagabagon";
String password = "ABCabc123";

Verifico verifico = new Verifico();
SQLData sqlData = verifico.getSQLData(sqlType, dbServer, dbName, user, password);
```
