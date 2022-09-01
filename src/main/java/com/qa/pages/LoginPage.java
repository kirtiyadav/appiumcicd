package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.FindsByAccessibilityId;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class LoginPage extends BaseTest {


    @AndroidFindBy(accessibility = "test-Username")
    private MobileElement usernameField;

    @AndroidFindBy(accessibility = "test-Password")
    private MobileElement password;

    @AndroidFindBy(accessibility = "test-LOGIN")
    private MobileElement loginButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    private MobileElement errorMessage;

    public LoginPage enterUsername(String username) {
        sendKeys(username, usernameField, "Login with "+ username);
        return this;

    }
    public LoginPage enterPassword(String pwd){
            sendKeys(pwd, password, "Entering password : "+pwd);
            return this;
        }

        public ProductsPage pressLoginBtn(){
        click(loginButton, "Clicked on "+ loginButton.getText());
        return new ProductsPage();
    }

    public String getErrText(){

        return getAttribute(errorMessage, "text", "Error text is : "+errorMessage.getText());
    }

    public ProductsPage login(String username, String password){
        enterUsername(username);
        enterPassword(password);
        return pressLoginBtn();
    }




}
