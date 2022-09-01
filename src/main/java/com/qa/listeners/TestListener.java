package com.qa.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener {

    TestUtils utils = new TestUtils();

    public void onTestFailure(ITestResult result) {

        if (result.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
           utils.log(sw.toString());

        }

        BaseTest base = new BaseTest();
        File file = base.getDriver().getScreenshotAs(OutputType.FILE); //Take screenshot. file is a File type object in
        // which screenshot will be collected

        byte[] encoded = null;

            try {
                encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
            } catch (IOException e) {
                e.printStackTrace();
            }



        Map<String, String> params = new HashMap<>();
        params = result.getTestContext().getCurrentXmlTest().getAllParameters();
        String imagepath = "Screenshots" + File.separator + params.get("platformName")+"_"+params.get("platformVersion"
        )+"_"+params.get("deviceName")+File.separator+ base.getDateTime()+File.separator+result.getTestClass().getRealClass()
                .getSimpleName()+File.separator+result.getName()+".png";

        String completeImagePath = System.getProperty("user.dir")+File.separator+imagepath;
        try {
            FileUtils.copyFile(file, new File(imagepath)); //FileUtils utility is used to copy a file. By default this will create the
            // file in root directory
            Reporter.log("This is a sample screenshot"); // To add screenshots in the report
            Reporter.log("<a href='"+completeImagePath+"'><img src='"+completeImagePath+"'height='400' width ='400'/></a>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExtentReport.getTest().log(Status.FAIL, "Test failed");

            ExtentReport.getTest().fail("Test case failed", MediaEntityBuilder.createScreenCaptureFromPath(completeImagePath).build());

            ExtentReport.getTest().fail("Test case failed", MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());

            ExtentReport.getTest().fail(result.getThrowable());


    }

    public void onTestStart(ITestResult result){
        BaseTest base = new BaseTest();
        ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
                .assignCategory(base.getDeviceName())
                .assignAuthor("Kirti Yadav");
    }

    public void onTestSuccess(ITestResult result){
        ExtentReport.getTest().log(Status.PASS, "Test Passed");
    }

    public void onTestSkipped(ITestResult result){
        ExtentReport.getTest().log(Status.SKIP, "Test Skipped");
    }

    public void onFinish(ITestContext context){
        ExtentReport.getReporter().flush();    }
}


