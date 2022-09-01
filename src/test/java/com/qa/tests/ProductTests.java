package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTests extends BaseTest {

    LoginPage loginpage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    ProductDetailsPage productDetailsPage;

    JSONObject loginUsers;
    TestUtils utils = new TestUtils();


    @BeforeClass
    public void beforeClass() throws IOException {
        InputStream dataais=null;
        try {
            String dataFileName = "data/loginUsers.json";
            dataais = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener jsonTokener = new JSONTokener(dataais);
            loginUsers = new JSONObject(jsonTokener);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataais != null) {
                dataais.close();
            }
        }
        closeApp();
        launchApp();


    }

    @BeforeMethod
    public void beforeMethod(Method m) {
        loginpage = new LoginPage();
        utils.log().info("******************" + m.getName() + " test **********************");
        productsPage = loginpage.login(loginUsers.getJSONObject("validuser").getString("username"), loginUsers.getJSONObject("validuser").getString("password"));

    }

    //@AfterMethod
    public void afterMethod(){
        settingsPage = productsPage.pressSettingsBtn();
        loginpage = settingsPage.pressLogoutBtn();
    }

    @Test
    public void validateProductOnProductsPage() {
        SoftAssert sa = new SoftAssert();


        String slbTitle = productsPage.getSLBTitle();
        sa.assertEquals(slbTitle, getStrings().get("products_page_slb_title"));

        String slbPrice = productsPage.getSLBPrice();
        sa.assertEquals(slbPrice, getStrings().get("SLB_price"));

        sa.assertAll();




    }

    @Test
    public void validateProductOnProductDetailsPage() {
        SoftAssert sa = new SoftAssert();

        productDetailsPage = productsPage.clickSLBTitle();

        String slbTitle = productDetailsPage.getSLBTitle();
        sa.assertEquals(slbTitle, getStrings().get("product_details_page_slb_title"));

        String slbText = productDetailsPage.getSLBText();
        sa.assertEquals(slbText, getStrings().get("SLB_text"));

        productDetailsPage.scrollToSLBPrice();

        String slbPrice = productDetailsPage.getPrice();
        sa.assertEquals(slbPrice, getStrings().get("product_details_SLB_price"));

        sa.assertAll();



    }



}
