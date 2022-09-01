package com.qa;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


//@Listeners(ExtentITestListenerAdapter.class)
public class BaseTest {

    protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    protected static ThreadLocal <Properties> props = new ThreadLocal<Properties>();

    protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal<HashMap<String, String>> strings = new ThreadLocal<HashMap<String,String>>();
    protected static ThreadLocal <String> deviceName = new ThreadLocal<String>();
    TestUtils utils = new TestUtils();

    private static AppiumDriverLocalService server;

    public AppiumDriver getDriver(){
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2){
        driver.set(driver2);
    }

    public Properties getProps() {

        return props.get();
    }

    public void setProps(Properties props2) {
        props.set(props2);
    }

    public HashMap<String, String> getStrings() {

        return strings.get();
    }

    public void setStrings(HashMap<String, String> strings2) {

        strings.set(strings2);
    }

    public String getDateTime(){

        return dateTime.get();
    }

    public void setDateTime(String dateTime2) {

        dateTime.set(dateTime2);
    }

    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }

    public BaseTest() {

        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }


    @BeforeMethod
    public void beforeMethod(){
        utils.log("Super Before Method");
        ((CanRecordScreen) getDriver()).startRecordingScreen();
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result){
        utils.log("Super After Method");
     String media =  ((CanRecordScreen) getDriver()).stopRecordingScreen();

       if(result.getStatus()==2){
           Map<String,String> params =  result.getTestContext().getCurrentXmlTest().getAllParameters();

           String dir = "videos"+File.separator+params.get("platformName")+"_"+params.get("platformVersion")+"_"+params.get("deviceName")
                   +File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();

           File videoDir = new File(dir);

           synchronized (videoDir){
               if(!videoDir.exists()){
                   videoDir.mkdirs();
               }
           }




           try {
               FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
               stream.write(Base64.getDecoder().decode(media));
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }


    }

    @BeforeSuite
    public void beforeSuite() throws Exception {
        ThreadContext.put("ROUTINGKEY", "ServerLogs");
        server = getAppiumServerDefault();
        //if(!server.isRunning()){
        if(!checkIfAppiumIsRunning(4723)){
            server.start();
             server.clearOutPutStreams(); // to keep appium logs from printing in the console
            utils.log().info("Appium Server Started");
        }else {
            utils.log().info("Appium Server is already running");
        }


            }

            public boolean checkIfAppiumIsRunning(int port) throws Exception{

        boolean isAppiumServerRunning = false;
                ServerSocket socket;
                try{
                    socket = new ServerSocket(port);
                    socket.close();
                }catch (IOException ex){
                    System.out.println("1");
                    isAppiumServerRunning = true;
                }
                finally {
                    socket = null;
                }
                return  isAppiumServerRunning;
            }

    @AfterSuite
    public void afterSuite(){
        server.stop();
        utils.log().info("Appium Server Stopped");
    }

    public AppiumDriverLocalService getAppiumServerDefault(){
        return  AppiumDriverLocalService.buildDefaultService();
    }

    @Parameters({"emulator", "platformName", "udid", "systemPort", "chromeDriverPort", "deviceName"})
    @BeforeTest
    public void beforeTest(String emulator, String platformName, String udid, String systemPort, String chromeDriverPort, String deviceName) throws Exception {

        utils.log().info("This is an info message");
        utils.log().error("This is an error message");
        utils.log().debug("This is a debug message");
        utils.log().warn("This is a warning message");

        utils = new TestUtils();
        setDateTime(utils.dateTime());
        setDeviceName(deviceName);
        URL url;
        InputStream inputStream=null; //FileInputStream could be used if the config file were not in the classpath
        InputStream stringis=null;
        Properties props = new Properties();
        AppiumDriver driver;


        String strFile = "logs" + File.separator+deviceName;
        File logFile = new File(strFile);
        if(!logFile.exists()){
            logFile.mkdirs();
        }
        ThreadContext.put("ROUTINGKEY", strFile);

        try{

            props = new Properties();
            String propFileName = "config.properties";
            String xmlFileName = "strings/strings.xml";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            setProps(props);

            stringis = getClass().getClassLoader().getResourceAsStream(xmlFileName);

            setStrings(utils.parseStringXML(stringis));


            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("platformName", platformName);
            desiredCapabilities.setCapability("deviceName", deviceName);
            desiredCapabilities.setCapability("udid", udid);
            desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));
             desiredCapabilities.setCapability("noReset", true);
//            desiredCapabilities.setCapability("fullReset", false);
            desiredCapabilities.setCapability("ignoreHiddenApiPolicyError", false);
            desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
            desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
//            appURL = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));
//            String appURL = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
            String appURL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                    + File.separator + "resources" + File.separator + "app" + File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
            utils.log("=====App URL is "+appURL);
            desiredCapabilities.setCapability("app", appURL);




           /* if(emulator.equalsIgnoreCase(true)){
                desiredCapabilities.setCapability("platformVersion", platformVersion);
                desiredCapabilities.setCapability("avd", deviceName);
            }
            else
            {
                desiredCapabilities.setCapability("udid", udid);
            }*/
            desiredCapabilities.setCapability("systemPort", systemPort);
            desiredCapabilities.setCapability("chromeDriverPort", chromeDriverPort);

//             desiredCapabilities.setCapability(MobileCapabilityType.UDID, "emulator-5554");




            url = new URL(props.getProperty("appiumURL")+"4723/wd/hub");
            driver = new AndroidDriver(url, desiredCapabilities);

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            setDriver(driver);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {

            if(inputStream!=null){
                inputStream.close();
            }
            if(stringis!=null){
                stringis.close();
            }
        }


    }

    public void waitForVisibility(MobileElement e){
        WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.WAIT);
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    public void click(MobileElement e){
        waitForVisibility(e);
        e.click();
    }

    public void click(MobileElement e, String msg){
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.click();
    }

    public void sendKeys(String text, MobileElement e){
        e.clear();
        waitForVisibility(e);
        e.sendKeys(text);

    }

    public void sendKeys(String text, MobileElement e, String msg){
        e.clear();
        waitForVisibility(e);
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        e.sendKeys(text);

    }

    public String getAttribute(MobileElement e, String attribute){

        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public String getAttribute(MobileElement e, String attribute, String msg){
        utils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg);
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }



    public void closeApp(){
        ((InteractsWithApps) getDriver()).closeApp();
    }

    public void launchApp(){
        ((InteractsWithApps) getDriver()).launchApp();
    }

    public MobileElement scrollToElement(){

        return (MobileElement) ((FindsByAndroidUIAutomator) getDriver()).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().description(\"test-Inventory item page\")).scrollIntoView("+"new UiSelector().description(\"test-Price\"));");
       // return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().description(true)).scrollIntoView("+"new UiSelector().description(\"test-Price\"));");


    }



    @AfterTest
    public void afterTest(){

        getDriver().quit();
    }
}
