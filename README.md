# VERIFICO
Verifico is a Test Automation Framework that aims to automate both Web and Mobile Applications.

## Built With
The Test Automation Framework is built with the following:
* [Eclipse](https://www.eclipse.org/downloads/) - Integrated Development Enviroment (IDE)
* [Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Programming Language
* [Selenium](https://www.seleniumhq.org/download/) - Test Automation Tool
* [TestNG](http://testng.org/doc/download.html) - Testing Framework
* [Log4J2](https://logging.apache.org/log4j/2.0/download.html) - Logging Tool
* [ExtentReports](http://relevantcodes.com/extentreports-for-selenium/) - Reporting Tool
* [Apache POI](https://poi.apache.org/download.html) - Test Data Access (API for MS Office)
* [Maven](https://maven.apache.org/download.cgi) - Build Management Tool / Dependency Management

## Structure
The structure of the Test Automation Framework follows the the idea of Page Object Model (POM) which separates web application locators/reusable methods from test codes so code can be organized and refactoring will not be an issue hence, a separation of "main" packages/classes from "pages" packages/classes. The "test" packages/classes will include all the procedures for performing the automated test cases.
```
automation
  web
    appium
    selenium
  mobile
    appium
data
  excel
  sql
enums
loggers
reporters
settings
test
utilities
```

### Automation
TO DO

### Data
TO DO

### Enums
TO DO

### Reporters
TO DO

### Settings
TO DO

### Test
TO DO

### Utilities
TO DO

## Process Flow
The Test Automation Framework heavily relies on the TestNG Testing Framework for its Process Flow.

### BeforeSuite
TO DO

### OnStart
TO DO

### BeforeTest
TO DO

### BeforeMethod
TO DO

### OnTestStart
TO DO

### OnTestSuccess
TO DO

### AfterMethod
TO DO

### AfterTest
TO DO

### OnFinish
TO DO

### AfterSuite
TO DO
