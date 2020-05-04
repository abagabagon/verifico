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
### **Automation**
The following are the supported automation tools:
#### **Web Automation**
##### **Selenium**

```
WebAutomation I = new SeleniumWebAutomation();
```

Available commands require Objects as `By` Objects. So it's recommended to apply Page Object Model for its usage.

```
I.fill(PageLogin.EMAIL_ADDRESS_TEXTBOX, "abagabagon@yopmail.com");
I.fill(PageLogin.PASSWORD_TEXTBOX, "ABCabc123");
I.click(PageLogin.LOGIN_BUTTON);
```

#### **Mobile Web Automation**
##### **Appium**

```
URL appiumServer = new URL("http://127.0.0.1:4723/wd/hub");
String platformVersion = "6.0.1";
String deviceName = "YT9117XT7C";
WebAutomation I = new AppiumWebAutomation(Mobile.Android, appiumServer, platformVersion, deviceName);
```

Same with `Selenium`, available commands require Objects as `By` Objects.

#### **Mobile Automation**
##### **Appium** (Development In-progress)
