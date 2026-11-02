package com.goldbharath.objectrepository.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.goldbharath.genericutilities.JavaUtility;
import com.goldbharath.genericutilities.WebDriverUtility;

public class BookSilverOrderHistoryPage {

	//Finding WebElements Using @FindBy Annotations

    @FindBy(xpath="//select[@class='formselect-gold-value']")private WebElement selectOrderTypeDrpDwn;
	
	@FindBy(xpath="//tr[td[normalize-space(.)='1']]")private WebElement FirstIndexTotalSilverOrderDetailsBasedOnSerialNoEle;
	
	@FindBy(xpath="(//td)[1]")private WebElement IndexSerialNoEle;
	
	@FindBy(xpath="(//td)[2]")private WebElement FrstOrderIdEle;
	
	@FindBy(xpath="//tr[@class='ng-star-inserted']/td[.='GB-98160']")private WebElement OrderIdDynamicEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[1]")private WebElement DateAndTimeAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[3]")private WebElement QtyGramsAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[4]")private WebElement PricePerGramAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[5]")private WebElement TotalPriceAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[6]")private WebElement PenalityAsPerOrderIDEle;
	
	// Update Payment Document Btn Based on OrderID //////// Need to Make Dynamic
	@FindBy(xpath="//tr[td[normalize-space()='GB-58085']]//button[normalize-space()='Update Payment Document']")private WebElement UpdatePaymentDocumentBtnAsPerOrderId;
	
	// Order Status Ele Based on OrderID //////// Need to Make Dynamic
	@FindBy(xpath="//tr/td/following-sibling::td[.='GB-11105']/following-sibling::td[last()]")private WebElement OrderStatusAsPerOrderIdEle;
	
	@FindBy(xpath="//tr/td[.='GB-11105']/following-sibling::td/p[.=' Document Submitted ']")private WebElement DocumentsSubmitedSuccessMsg;
	
	@FindBy(xpath="//td[.='1']/following-sibling::td[.='100']")private WebElement FirstQuantityEle;
	
	
	//Rule-2:Create a constructor to initilise these elements
	public BookSilverOrderHistoryPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	//Rule-3:Provide getters to access these variables
	
	public void checkingSilverRecentlyBookingHistory(WebDriver driver, int Quantity) throws Exception
	{
		WebDriverUtility wUtil= new WebDriverUtility();
		JavaUtility jUtil = new JavaUtility();
		
		Thread.sleep(2000);
		wUtil.handleDropdownByIndex(selectOrderTypeDrpDwn, 2);
		Thread.sleep(2000);
		WebElement Qty = driver.findElement(By.xpath("//td[.='1']/following-sibling::td[.=' Silver ']/following-sibling::td[.='"+Quantity+"']"));
		if(Qty.isDisplayed())
		{
			System.out.println(Quantity+" of SILVER Booked Successfully ");
		}
		
	}
	
	public void checkingSilverBuyingHistory()
	{
		
	}
	
}
