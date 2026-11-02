package com.goldbharath.objectrepository.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.goldbharath.genericutilities.WebDriverUtility;

public class BookSilverPage {

	//Finding WebElements Using @FindBy Annotations

    @FindBy(xpath="//h5[.='Book Silver']/following-sibling::button[@class='btn-close']")private WebElement CloseBtn;
    
    @FindBy(xpath="//h5[.='Book Silver']/../following-sibling::div//h4[contains(.,'₹')]")private WebElement CurrentLivePriceEle;
    
    @FindBy(xpath="//h5[.='Book Silver']/../following-sibling::div//p[contains(.,'Price will be locked for')]")private WebElement PriceLockedTimeEle;
    
    @FindBy(xpath="//h5[.='Book Silver']/../following-sibling::div//select[@formcontrolname='quantity_purchased']")private WebElement QuantityDrpDwn;
    
    @FindBy(xpath="//h5[normalize-space()='Book Silver']/../..//input[@placeholder='Enter your amount']")private WebElement AmountEdt;
  
    @FindBy(xpath="//h5[normalize-space()='Book Silver']/../..//button[.='Book']")private WebElement BookBtn;
    
    @FindBy(xpath="//p[.='Booked Order Successfully']")private WebElement BookedOrderSuccessfullyMsg;
    
    
	//Rule-2:Create a constructor to initilise these elements
    
	public BookSilverPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}


	//Rule-3:Provide getters to access these variables
	

	public WebElement getCloseBtn() {
		return CloseBtn;
	}


	public WebElement getCurrentLivePriceEle() {
		return CurrentLivePriceEle;
	}


	public WebElement getPriceLockedTimeEle() {
		return PriceLockedTimeEle;
	}


	public WebElement getQuantityDrpDwn() {
		return QuantityDrpDwn;
	}


	public WebElement getAmountEdt() {
		return AmountEdt;
	}


	public WebElement getBookedOrderSuccessfullyMsg() {
		return BookedOrderSuccessfullyMsg;
	}


	public WebElement getBookBtn() {
		return BookBtn;
	}
	
	// Business Library
	
	public void fetchingSilverLivePriceAndBookingSilver(WebDriver driver, String Quantity) throws Exception
	{
	    WebDriverUtility wUtil = new WebDriverUtility();
	    
	    Thread.sleep(2000);
	    String CurrentLockedPrice = CurrentLivePriceEle.getText();
	    System.out.println(CurrentLockedPrice);
	    
	    wUtil.handleDropdownByValue(QuantityDrpDwn, Quantity);
	    Thread.sleep(2000);
	//  wUtil.handleDropdownByIndex(AmountEdt, 5);
	    
	    // Clean the price text: strip currency symbols, commas, spaces
	    String cleanedPrice = CurrentLockedPrice.replaceAll("[^0-9.]", "");
	    double priceValue = Double.parseDouble(cleanedPrice);
	    double amountToSend = priceValue - 10;

	    // Convert back to a plain string for sendKeys (no decimals if the field expects a whole number)
	    String Amount = String.valueOf((long) amountToSend);
	    wUtil.scrollExactToParticularWebElement(driver, AmountEdt);
	    
	    AmountEdt.sendKeys(Amount);
	    Thread.sleep(2000);
	    
	    BookBtn.click();
	    Thread.sleep(3000);
	    
	    if (BookedOrderSuccessfullyMsg.isDisplayed())
	    {
			System.out.println(BookedOrderSuccessfullyMsg.getText());
		}
	}
	
	
}
