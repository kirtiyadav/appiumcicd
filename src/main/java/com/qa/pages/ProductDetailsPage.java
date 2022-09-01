package com.qa.pages;

import com.qa.MenuPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductDetailsPage extends MenuPage {


    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
    private MobileElement backtoProductsDtlBtn;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    private MobileElement SLBTitle;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    private MobileElement SLBText;

    @AndroidFindBy(accessibility = "test-Price")
    private MobileElement SLBPrice;


    public String getSLBTitle() {
        return getAttribute(SLBTitle, "text", "Title is "+SLBTitle.getText());
    }

    public String getSLBText() {
        return getAttribute(SLBText, "text");
    }

    public ProductsPage clickBacktoProductsBtn() {
        click(backtoProductsDtlBtn, "Clicked on "+ backtoProductsDtlBtn.getText());
        return new ProductsPage();
    }

    public String getPrice() {
        return getAttribute(SLBPrice, "text", "Price is : "+SLBPrice.getText());
    }

    public ProductDetailsPage scrollToSLBPrice() {
        scrollToElement();
        return this;

    }


}
