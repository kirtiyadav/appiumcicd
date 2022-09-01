package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import io.appium.java_client.MobileElement;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {

    LoginPage loginpage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    JSONObject loginUsers;
    TestUtils utils = new TestUtils();


    @BeforeClass
    public void beforeClass() throws IOException {
        InputStream dataais=null;
        try{
            String dataFileName = "data/loginUsers.json";
            dataais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener jsonTokener = new JSONTokener(dataais);
            loginUsers = new JSONObject(jsonTokener);

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(dataais!=null){
                dataais.close();
            }
        }

        closeApp();
        launchApp();


    }

    @BeforeMethod
    public void beforeMethod(Method m) {
        utils.log().info("login before Method");
        loginpage = new LoginPage();
        utils.log().info("******************" + m.getName() + " test **********************");
        //productsPage = loginpage.login(loginUsers.getJSONObject("validuser").getString("username"), loginUsers.getJSONObject("validuser").getString("password"));

    }

    @AfterMethod
    public void afterMethod(){
        utils.log().info("Login After Method");
    }

   /* @AfterMethod
    public void afterMethod(){
        settingsPage = productsPage.pressSettingsBtn();
        loginpage = settingsPage.pressLogoutBtn();
    }*/

    @Test
    public void invalidUsername(){

            loginpage.enterUsername(loginUsers.getJSONObject("invaliduser").getString("username"));
            loginpage.enterPassword(loginUsers.getJSONObject("invaliduser").getString("password"));
            loginpage.pressLoginBtn();
            String errorText = loginpage.getErrText()+"hkjh";
            String actualErrText = getStrings().get("invalid_username_or_password"); //"Username and password do not match any user in this service.";
            Assert.assertEquals(errorText, actualErrText);

        }



    @Test
    public void invalidPassword(){
        loginpage.enterUsername(loginUsers.getJSONObject("invalidpassword").getString("username"));
        loginpage.enterPassword(loginUsers.getJSONObject("invalidpassword").getString("password"));
        loginpage.pressLoginBtn();
        String errorText = loginpage.getErrText()+"hkdf";
        String expectedErrText = getStrings().get("invalid_username_or_password");
        Assert.assertEquals(errorText, expectedErrText);
    }

    @Test
    public void successfulLogin(){
        loginpage.enterUsername(loginUsers.getJSONObject("validuser").getString("username"));
        loginpage.enterPassword(loginUsers.getJSONObject("validuser").getString("password"));
        productsPage = loginpage.pressLoginBtn();

        String productTitle = productsPage.getProductTitle();
        String expectedProductTitle =getStrings().get("product_title");
        Assert.assertEquals(productTitle, expectedProductTitle);
    }


}
