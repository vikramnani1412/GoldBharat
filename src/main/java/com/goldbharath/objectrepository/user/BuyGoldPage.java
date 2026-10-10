package com.goldbharath.objectrepository.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.goldbharath.genericutilities.WebDriverUtility;

public class BuyGoldPage {

	//Finding WebElements Using @FindBy Annotations

    @FindBy(xpath="//h5[.='Buy Gold']/following-sibling::button[@class='btn-close']")private WebElement CloseBtn;
    
    @FindBy(xpath="//h5[.='Buy Gold']/../following-sibling::div//h4")private WebElement CurrentLivePriceEle;
    
    @FindBy(xpath="//h5[.='Buy Gold']/../following-sibling::div//p[contains(.,'Price will be locked for')]")private WebElement PriceLockedTimeEle;
    
    @FindBy(xpath="//h5[.='Buy Gold']/../following-sibling::div//select[@formcontrolname='quantity_purchased']")private WebElement QuantityDrpDwn;
  
    @FindBy(xpath="//h5[.='Buy Gold']/../following-sibling::div//button[.='Buy']")private WebElement BuyBtn;
    
    @FindBy(xpath="//p[.='Success!']/following-sibling::p")private WebElement SuccessMsg;
    
    
	//Rule-2:Create a constructor to initilise these elements
    
	public BuyGoldPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}


	public WebElement getCloseBtn() {
		return CloseBtn;
	}
	
	
	//Rule-3:Provide getters to access these variables
	

	public WebElement getCurrentLivePriceEle() {
		return CurrentLivePriceEle;
	}


	public WebElement getPriceLockedTimeEle() {
		return PriceLockedTimeEle;
	}


	public WebElement getQuantityDrpDwn() {
		return QuantityDrpDwn;
	}


	public WebElement getSuccessMsg() {
		return SuccessMsg;
	}


	public WebElement getBuyBtn() {
		return BuyBtn;
	}
	
	// Business Library
	
	public void buyGold(int DropDownIndex) throws Exception
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		
		Thread.sleep(2000);
		String Price = CurrentLivePriceEle.getText();
		Thread.sleep(2000);
		String Time = PriceLockedTimeEle.getText();
		Thread.sleep(2000);
		System.out.println(Price+" This Price is Locked for "+Time+" Time");
		Thread.sleep(1000);
		wUtil.handleDropdownByIndex(QuantityDrpDwn, DropDownIndex);
		Thread.sleep(2000);
		BuyBtn.click();
		Thread.sleep(2000);
		String Success = SuccessMsg.getText();
		System.out.println(Success);
		
	}
	
}
