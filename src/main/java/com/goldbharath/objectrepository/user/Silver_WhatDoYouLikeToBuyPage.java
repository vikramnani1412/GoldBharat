package com.goldbharath.objectrepository.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Silver_WhatDoYouLikeToBuyPage {

	//Finding WebElements Using @FindBy Annotations

    @FindBy(xpath="(//h5[.='What would you like to buy?']/following-sibling::button[@aria-label='Close'])[2]")private WebElement CloseBtn;
    
    @FindBy(xpath="//h6[.='Silver Coins']")private WebElement SilverCoinsEle;
    
    @FindBy(xpath="//h6[.='Silver Bars']")private WebElement SilverBarsEle;
    
    
	//Rule-2:Create a constructor to initilise these elements
    
	public Silver_WhatDoYouLikeToBuyPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}


	//Rule-3:Provide getters to access these variables
	

	public WebElement getCloseBtn() {
		return CloseBtn;
	}


	public WebElement getSilverCoinsEle() {
		return SilverCoinsEle;
	}


	public WebElement getSilverBarsEle() {
		return SilverBarsEle;
	}

	
}
