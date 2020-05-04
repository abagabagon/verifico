# **VERIFICO**
Verifico is a Test Automation Framework that aims to automate both Web and Mobile Applications.

## **Background**
The Verifico Project is the result of a need to attain knowledge in Test Automation and develop a working Test Automation Framework.

## **Built With**
The Test Automation Framework is built with the following:
* [Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Programming Language
* [Selenium](https://www.seleniumhq.org/download/) - Web Test Automation Tool
* [Appium](http://appium.io/downloads.html) - Mobile Test Automation Tool
* [TestNG](http://testng.org/doc/download.html) - Testing Framework
* [Log4J2](https://logging.apache.org/log4j/2.0/download.html) - Logging Tool
* [ExtentReports](http://relevantcodes.com/extentreports-for-selenium/) - Reporting Tool
* [Apache POI](https://poi.apache.org/download.html) - Test Data Access (API for MS Office)
* [Maven](https://maven.apache.org/download.cgi) - Build Management Tool / Dependency Management

## **Usage**
### **Automation**
The following are the supported automation tools:
#### **Web Automation**
##### **Selenium**

```
WebAutomation I = new SeleniumWebAutomation();
```

#### **Mobile Web Automation**

```
WebAutomation I = new AppiumWebAutomation(Mobile.Android, "http://127.0.0.1:4723/wd/hub", "6.0.1", "YT9117XT7C");
```

##### **Appium**
#### **Mobile Automation**
##### **Appium** (Development In-progress)
