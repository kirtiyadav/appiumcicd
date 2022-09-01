package com.qa.pages;

import com.qa.MenuPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsPage extends MenuPage {


    @AndroidFindBy(xpath = "//android.widget.ScrollView[@content-desc=\"test-PRODUCTS\"]/preceding-sibling::android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    private MobileElement productTitleText;

    @AndroidFindBy(xpath =  "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
    private MobileElement SLBTitle;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
    private MobileElement SLBPrice;


    public String getProductTitle(){

        return getAttribute(productTitleText, "text", "Product Title is : "+productTitleText.getText());
    }

    public String getSLBTitle(){

        return getAttribute(SLBTitle, "text", "SLB Title is : "+SLBTitle.getText());
    }

    public String getSLBPrice(){

        return getAttribute(SLBPrice, "text", "SLB Price : "+SLBPrice.getText());
    }

    public ProductDetailsPage clickSLBTitle(){
        click(SLBTitle, "Clicked on "+SLBTitle.getText());
        return new ProductDetailsPage();
    }

}
