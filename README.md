# **VERIFICO**
Verifico is a Test Automation Framework that aims to automate both Web and Mobile Applications.

## **Background**
The Verifico Project is the result of a need to attain knowledge in Test Automation and develop a working Test Automation Framework.

## **Built With**
The Test Automation Framework is built with the following:
* [Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Programming Language (1.8)
* [Selenium](https://www.seleniumhq.org/download/) - Web Test Automation Tool
* [Appium](http://appium.io/downloads.html) - Mobile Test Automation Tool
* [TestNG](http://testng.org/doc/download.html) - Testing Framework
* [Log4J2](https://logging.apache.org/log4j/2.0/download.html) - Logging Tool
* [ExtentReports](http://relevantcodes.com/extentreports-for-selenium/) - Reporting Tool
* [Apache POI](https://poi.apache.org/download.html) - Test Data Access (API for MS Office)
* [Maven](https://maven.apache.org/download.cgi) - Build Management Tool / Dependency Management

## **Usage**
### **Web Automation**
1. **Selenium (Web)**

```java
WebAutomation I = new SeleniumWebAutomation();
```

Available commands require Objects as `By` Objects. So it's recommended to apply Page Object Model for its usage.

```java
I.fill(PageLogin.EMAIL_ADDRESS_TEXTBOX, "abagabagon@yopmail.com");
I.fill(PageLogin.PASSWORD_TEXTBOX, "ABCabc123");
I.click(PageLogin.LOGIN_BUTTON);
```

2. **Appium (Mobile Web)**

```java
URL appiumServer = new URL("http://127.0.0.1:4723/wd/hub");
String platformVersion = "6.0.1";
String deviceName = "YT9117XT7C";

WebAutomation I = new AppiumWebAutomation(Mobile.Android, appiumServer, platformVersion, deviceName);
```

Same with `Selenium`, available commands require Objects as `By` Objects.

### **Mobile Automation**
1. **Appium** (Development In-progress)

### **Excel Data**

Data retrieval from Excel Files is also included which can be used for providing test data to automated tests. `Apache POI` is utilized to be able to do just that. Supported file formats are as follows:

1. **XLS Excel Files**

```java
String filePath = "./src/resources/data/excel/staff-accounts.XLS";

ExcelData excelData = new XLSExcelData(filePath);
```

2. **XLSX Excel Files**

```java
String filePath = "./src/resources/data/excel/staff-accounts.XLSX";

ExcelData excelData = new XLSXExcelData(filePath);
```

Originally intended for use in a TestNG Test Framework, it can be used for retrieving test data for tests in a `@DataProvider` Method.

```java
@DataProvider(name = "Accounts")
public Object[][] getAccountsData() {
	ExcelData excelData = new XLSXExcelData("./src/resources/data/excel/staff-accounts.XLSX");
	Object[][] data = excelData.getSheetData("Manager", false);
	return data;
}
```

### **SQL Data**

Data retrieval from SQL is also included. It can be used for back end data verification. Supported SQL Types are as follows:

1. **MySQL Data**

```java
String dbServer = "127.0.0.1";
String dbName = "northwind";
String user = "abagabagon";
String password = "ABCabc123";

SQLData sqlData = new MySQLData(dbServer, dbName, user, password);
```

2. **MSSQL Data**

```java
String dbServer = "127.0.0.1";
String dbName = "northwind";
String user = "abagabagon";
String password = "ABCabc123";

SQLData sqlData = new MSSQLData(dbServer, dbName, user, password);
```
