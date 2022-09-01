package com.qa;

import com.qa.pages.SettingsPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class MenuPage extends BaseTest{

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView")
    private MobileElement settingBtn;

    public SettingsPage pressSettingsBtn(){
        click(settingBtn);
        return new SettingsPage();
    }
}
