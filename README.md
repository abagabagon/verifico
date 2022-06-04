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

For Web Application Automation, create an instance of the `WebDriverFactory` and `WaitCommands` Classes to initialize and get WebDrivers and initialize waits respectively:

```java

// WebDriverFactory
WebDriverFactory driverFactory = new WebDriverFactory();
driverFactory.setChromeDriver();
WebDriver driver = driverFactory.getChromeDriver();


// WaitCommand
int implicitWaitDuration = 10;
int explicitWaitDuration = 5;
WaitCommands wait = new WaitCommands(driver, implicitWaitDuration, explicitWaitDuration);

```

Web Application Automation Commands are accessible into classes from which related commands are grouped. Available Command Classes are shown below:

## **Browser Commands**

Browser Commands contains functions relating to actions being done by the user at the Web Browser. Class name for this is `BrowserCommands`

`BrowserCommands` Class can be instantiated below using the instantiated `WebDriverFactory` and `WaitCommands`:

```java
JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
BrowserCommands browser = new BrowserCommands(driver, javascriptExecutor, wait);
browser.goTo("https://www.google.com/");
browser.maximize();
browser.refresh();
```

Browser Commands available are as follows:

| Command             | Description                                                  |
| ------------------- | ------------------------------------------------------------ |
| openTab             | Opens Tab                                                    |
| goTo                | Navigates to the Url specified                               |
| switchTabByTitle    | Switches to a Tab based on Page Title                        |
| switchTabByURL      | Switches to a Tab based on Page URL                          |
| switchTabToOriginal | Switches back to Original Tab                                |
| back                | Navigates one item back from the browser's history           |
| forward             | Navigates one item forward from the browser's history        |
| refresh             | Refreshes current page                                       |
| maximize            | Maximizes Browser Window                                     |
| deleteAllCookies    | Deletes all cookies                                          |
| deleteCookie        | Deletes cookie with the specified name                       |
| scroll              | Scrolls Page                                                 |
| closeTab            | Closes Tab of a Web Browser                                  |
| closeBrowser        | Closes Web Browser                                           |
| count               | Counts instance of the Web Element of the specified Locator  |

### **User Actions**

| Command                                                                               | Description                                                                                                                                                       |
| ------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Point                                                                                 | Points mouse to the specified Web Element.                                                                                                                        |
| Point                                                                                 | Points the specified Child Web Element within the Parent Web Element.                                                                                             |
| Point On List Element Based On Text                                                   | Points the specified Web Element from an Element List based on text value.                                                                                        |
| Point On List Element Based On Attribute Value                                        | Points the specified Web Element from an Element List based on attribute value.                                                                                   |
| Point On List Element                                                                 | Points the specified Web Element from an Element List based on index.                                                                                             |
| Point On Table Row Element Based On Table Row Element Text                            | Points the specified Web Element from a row in a table based on a text value from the same row.                                                                   |
| Point On Table Row Element Based On Table Row Attribute Value                         | Points the specified Web Element from a row in a table based on a attribute value from the same row.                                                              |
| Point On Table Row Element Based On Table Row Index                                   | Points the specified Web Element from a row in a table based on row index.                                                                                        |
| Click                                                                                 | Clicks the specified Web Element. Used for Elements that are clickable.                                                                                           |
| Click                                                                                 | Clicks the specified Child Web Element within the Parent Web Element.                                                                                             |
| ClickJS                                                                               | Clicks the specified Web Element (utilizing Javascript). Used for Elements that are clickable.                                                                    |
| ClickJS                                                                               | Clicks the specified Child Web Element within the Parent Web Element (utilizing Javascript).                                                                      |
| Click And Hold                                                                        | Clicks and holds the specified Web Element. Used for Elements that are clickable.                                                                                 |
| Click And Hold                                                                        | Clicks and holds the specified Child Web Element within the Parent Web Element.                                                                                   |
| Click On List Element Based On Text                                                   | Clicks the specified Web Element from an Element List based on text value. Used for Elements that are clickable.                                                  |
| ClickJS On List Element Based On Text                                                 | Clicks the specified Web Element from an Element List based on text value (utilizing Javascript). Used for Elements that are clickable.                           |
| Click On List Element Based On Attribute Value                                        | Clicks the specified Web Element from an Element List based on attribute value. Used for Elements that are clickable.                                             |
| ClickJS On List Element Based On Attribute Value                                      | Clicks the specified Web Element from an Element List based on attribute value (utilizing Javascript). Used for Elements that are clickable.                      |
| Click On List Element                                                                 | Clicks the specified Web Element from an Element List based on index. Used for Elements that are clickable.                                                       |
| ClickJS On List Element                                                               | Clicks the specified Web Element from an Element List based on index (utilizing Javascript). Used for Elements that are clickable.                                |
| Click On Table Row Element Based On Table Row Element Text                            | Clicks the specified Web Element from a row in a table based on a text value from the same row. Used for Elements that are clickable.                             |
| ClickJS On Table Row Element Based On Table Row Element Text                          | Clicks the specified Web Element from a row in a table based on a text value from the same row (utilizing Javascript). Used for Elements that are clickable.      |
| Click On Table Row Element Based On Table Row Attribute Value                         | Clicks the specified Web Element from a row in a table based on a attribute value from the same row. Used for Elements that are clickable.                        |
| ClickJS On Table Row Element Based On Table Row Attribute Value                       | Clicks the specified Web Element from a row in a table based on a attribute value from the same row (utilizing Javascript). Used for Elements that are clickable. |
| Double Click                                                                          | Double-clicks the specified Web Element. Used for Elements that are clickable.                                                                                    |
| Double Click                                                                          | Double-clicks the specified Child Web Element within the Parent Web Element.                                                                                      |
| Double Click On List Element Based On Text                                            | Double-clicks the specified Web Element from an Object List based on text value. Used for Elements that are clickable.                                            |
| Double Click On List Element Based On Attribute Value                                 | Double-clicks the specified Web Element from an Object List based on attribute value. Used for Elements that are clickable.                                       |
| Double Click On List Element                                                          | Double-clicks the specified Web Element from an Object List based on index. Used for Elements that are clickable.                                                 |
| Double Click On Table Row Element Based On Table Row Element Text                     | Double-clicks the specified Web Element from a row in a table based on text value from the same row. Used for Elements that are clickable.                        |
| Double Click On Table Row Element Based On Table Row Attribute Value                  | Double-clicks the specified Web Element from a row in a table based on attribute value from the same row. Used for Elements that are clickable.                   |
| Drag And Drop                                                                         | Drags a specified Web Element and drops it at target element. Used for Elements that can be dragged.                                                              |
| Type                                                                                  | Simulates typing into a text box/area Web Element.                                                                                                                |
| Type                                                                                  | Simulates typing into a text box/area Child Web Element within the Parent Web Element.                                                                            |
| Type On Table Row Element Based On Table Row Element Text                             | Type input text at the specified Web Element from a row in a table based on text value from the same row.                                                         |
| Type On Table Row Element Based On Table Row Element Attribute Value                  | Type input text at the specified Web Element from a row in a table based on an attribute value of an element from the same row.                                   |
| Press                                                                                 | Simulates pressing of characters into a text box/area Web Element.                                                                                                |
| Press                                                                                 | Simulates pressing of characters into a text box/area Child Web Element within the Parent Web Element.                                                            |
| Press On Table Row Element Based On Table Row Element Text                            | Press specified keys at the specified Web Element from a row in a table based on text value from the same row.                                                    |
| Press On Table Row Element Based On Table Row Element Attribute Value                 | Press specified keys at the specified Web Element from a row in a table based on an attribute value of an element from the same row.                              |
| Clear                                                                                 | Clears value of a text box/area Web Element.                                                                                                                      |
| Clear                                                                                 | Clears value of the specified Child Web Element within the Parent Web Element.                                                                                    |
| Clear Table Row Element Based On Table Row Element Text                               | Clears value of the specified Web Element from a row in a table based on text value from the same row.                                                            |
| Clear Table Row Element Based On Table Row Element Attribute Value                    | Clears value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.                                      |
| Select                                                                                | Selects a Drop-down/Multi-select List Web Element Option.                                                                                                         |
| Deselect                                                                              | Deselects a Multi-select Web Element Option.                                                                                                                      |
| Get Text                                                                              | Gets the visible innerText of this Web Element, including sub-elements, without any leading or trailing whitespace.                                               |
| Get Text                                                                              | Gets the visible innerText of the specified Child Web Element within the Parent Web Element.                                                                      |
| Get Text From Table Row Element Based On Table Row Element Text                       | Gets the text of the specified Web Element from a row in a table based on text value from the same row.                                                           |
| Get Text From Table Row Element Based On Table Row Element Attribute Value            | Gets the text of the specified Web Element from a row in a table based on an attribute value of an element from the same row.                                     |
| Get Value                                                                             | Gets the value of the INPUT and TEXTAREA Web Element.                                                                                                             |
| Get Value                                                                             | Gets the value of the INPUT and TEXTAREA Child Web Element within the Parent Web Element.                                                                         |
| Get Value From Table Row Element Based On Table Row Element Text                      | Gets value of the specified Web Element from a row in a table based on text value from the same row.                                                              |
| Get Value From Table Row Element Based On Table Row Element Attribute Value           | Gets the value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.                                    |
| Get Attribute Value                                                                   | Gets the value of the specified attribute of the Web Element.                                                                                                     |
| Get Attribute Value                                                                   | Gets the value of the specified attribute of the Child Web Element within the Parent Web Element.                                                                 |
| Get Attribute Value From Table Row Element Based On Table Row Element Text            | Gets the attribute value of the specified Web Element from a row in a table based on text value from the same row.                                                |
| Get Attribute Value From Table Row Element Based On Table Row Element Attribute Value | Gets the attribute value of the specified Web Element from a row in a table based on an attribute value of an element from the same row.                          |
| Get DropDown List Value                                                               | Gets the selected option of the Drop-down List Web Element                                                                                                        |
| Accept Alert                                                                          | Accepts Javascript Alert                                                                                                                                          |
| Cancel Alert                                                                          | Cancels Javascript Alert                                                                                                                                          |
| Type Alert                                                                            | Simulates typing at Javascript Alert Text Box                                                                                                                     |
| Count                                                                                 | Counts instances of a Web Element in a Page.                                                                                                                      |
| Wait                                                                                  | Waits for a specific time (Seconds)                                                                                                                               |

### **Verifications**

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

## **Mobile Automation Commands**

### **Device Actions**

| Command           | Description                          |
| ----------------- | ------------------------------------ |
| Open Application  | Opens Mobile Application.            |
| Close Application | Closes Mobile Application.           |
| Get Orientation   | Get the current device orientation.  |
| Set Orientation   | Sets the device's orientation        |
| Get Geolocation   | Get the current device geo location. |
| Set Geolocation   | Sets the device's geo location       |
| Reset App         | Resets application data.             |


### **User Actions**

| Command                                                                    | Description                                                                                                                                        |
| -------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------- |
| Swipe                                                                      | Swipe to the specified Mobile Element position / Simulates swiping based on the specified coordinates.                                             |
| Swipe Up                                                                   | Simulates swipe up.                                                                                                                                |
| Swipe Down                                                                 | Simulates swipe down.                                                                                                                              |
| Tap                                                                        | Taps the specified Mobile Element. Used for Elements that are tappable.                                                                            |
| Tap On List Element Based On Text                                          | Taps the specified Mobile Element from an Element List based on text value. Used for Elements that are tappable.                                   |
| Tap On List Element Based On Attribute Value                               | Taps the specified Mobile Element from an Element List based on attribute value. Used for Elements that are tappable.                              |
| Tap On Table Row Element Based On Table Row Element Text                   | Taps the specified Mobile Element from a row in a table based on a text value from the same row. Used for Elements that are tappable.              |
| Tap On Table Row Element Based On Table Row Attribute Value                | Taps the specified Mobile Element from a row in a table based on a attribute value from the same row. Used for Elements that are tappable.         |
| Long Press                                                                 | Long presses the specified Mobile Element. Used for Elements that are tappable.                                                                    |
| Long Press On List Element Based On Text                                   | Long presses the specified Mobile Element from an Element List based on text value. Used for Elements that are tappable.                           |
| Long Press On List Element Based On Attribute Value                        | Long presses the specified Mobile Element from an Element List based on attribute value. Used for Elements that are tappable.                      |
| Long Press On Table Row Element Based On Table Row Element Text            | Long presses the specified Mobile Element from a row in a table based on a text value from the same row. Used for Elements that are tappable.      |
| Long Press On Table Row Element Based On Table Row Attribute Value         | Long presses the specified Mobile Element from a row in a table based on a attribute value from the same row. Used for Elements that are tappable. |
| Clear                                                                      | Clears value of a text box/area Mobile Element.                                                                                                    |
| Clear Table Row Element Based On Table Row Element Text                    | Clears value of the specified Mobile Element from a row in a table based on text value from the same row.                                          |
| Type                                                                       | Simulates typing into a text box/area Mobile Element, which may set its value.                                                                     |
| Type On Table Row Element Based On Table Row Element Text                  | Type input text at the specified Mobile Element from a row in a table based on text value from the same row.                                       |
| Get Text                                                                   | Get the visible innerText of this Mobile Element, including sub-elements, without any leading or trailing whitespace.                              |
| Get Text From Table Row Element Based On Table Row Element Text            | Gets the text of the specified Mobile Element from a row in a table based on text value from the same row.                                         |
| Get Attribute Value                                                        | Get the value of the specified attribute of the Mobile Element.                                                                                    |
| Get Attribute Value From Table Row Element Based On Table Row Element Text | Gets the attribute value of the specified Mobile Element from a row in a table based on text value from the same row.                              |
| Count                                                                      | Counts instances of a Mobile Element in a Screen.                                                                                                  |
| Wait                                                                       | Waits for a specific time (Seconds)                                                                                                                |

### **Verifications**

| Command                                                                          | Description                                                                                                                                 |
| -------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| See Attribute Value                                                              | Verifies the value of the specified attribute if equal to the expected value.                                                               |
| Don't See Attribute Value                                                        | Verifies the value of the specified attribute if equal to the expected value.                                                               |
| See Partial Attribute Value                                                      | Verifies the value of the specified attribute if partially equal to the expected value.                                                     |
| Don't See Partial Attribute Value                                                | Verifies the value of the specified attribute if not partially equal to the expected value.                                                 |
| See Text                                                                         | Verifies Mobile Element with text if equal to expected text value.                                                                          |
| Don't See Text                                                                   | Verifies Mobile Element with text if not equal to specified text value.                                                                     |
| See Text Of List Element                                                         | Verifies if text value exists from texts on Mobile Element List.                                                                            |
| Don't See Text Of List Element                                                   | Verifies if text value does not exist from texts on Mobile Element List.                                                                    |
| See Text Of Table Row Element                                                    | Verifies if text value exists from text of a Mobile Element in a Table Row.                                                                 |
| Don't See Text Of Table Row Element                                              | Verifies if text value does not exist from text of a Mobile Element in a Table Row.                                                         |
| See Text Of Table Row Element Based On Table Row Element Text                    | Verifies Mobile Element with text if equal to expected text value from a row in a table based on text value from the same row.              |
| Don't See Text Of Table Row Element Based On Table Row Element Text              | Verifies Mobile Element with text if not equal to specified text value from a row in a table based on text value from the same row.         |
| See Text Of Table Row List Element Based On Table Row Element Text               | Verifies text value if it exists in a Mobile Element List from a row in a table based on text value from the same row.                      |
| Don't See Text Of Table Row List Element Based On Table Row Element Text         | Verifies text value if it does not exist in a Mobile Element List from a row in a table based on text value from the same row.              |
| See Partial Text                                                                 | Verifies Mobile Element with text if equal to expected partial text value.                                                                  |
| Don't See Partial Text                                                           | Verifies Mobile Element with text if not equal to specified partial text value.                                                             |
| See Partial Text Of List Element                                                 | Verifies if text value exists from texts on Mobile Element List.                                                                            |
| Don't See Partial Text Of List Element                                           | Verifies if text value does not exist from texts on Mobile Element List.                                                                    |
| See Partial Text Of Table Row Element                                            | Verifies if partial text value exists from text of a Mobile Element in a Table Row.                                                         |
| Don't See Partial Text Of Table Row Element                                      | Verifies if partial text value does not exist from text of a Mobile Element in a Table Row.                                                 |
| See Partial Text Of Table Row Element Based On Table Row Element Text            | Verifies Mobile Element with text if equal to expected partial text value from a row in a table based on text value from the same row.      |
| Don't See Partial Text Of Table Row Element Based On Table Row Element Text      | Verifies Mobile Element with text if not equal to specified partial text value from a row in a table based on text value from the same row. |
| See Partial Text Of Table Row List Element Based On Table Row Element Text       | Verifies partial text value if it exists in a Mobile Element List from a row in a table based on text value from the same row.              |
| Don't See Partial Text Of Table Row List Element Based On Table Row Element Text | Verifies partial text value if it does not exist in a Mobile Element List from a row in a table based on text value from the same row.      |
| see                                                                              | Verifies if Mobile Element is displayed on Web Page                                                                                         |
| Don't See                                                                        | Verifies Mobile Element is not displayed on the Web Page                                                                                    |
| See Table Row Element Based On Table Row Element Text                            | Verifies Mobile Element is displayed from a row in a table based on text value from the same row.                                           |
| Don't See Table Row Element Based On Table Row Element Text                      | Verifies Mobile Element is not displayed from a row in a table based on text value from the same row.                                       |
| See Enabled                                                                      | Verifies Mobile Element is enabled on the Web Page                                                                                          |
| See Disabled                                                                     | Verifies Mobile Element is disabled on the Web Page                                                                                         |

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
