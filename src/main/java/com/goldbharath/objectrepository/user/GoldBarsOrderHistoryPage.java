package com.goldbharath.objectrepository.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.goldbharath.genericutilities.WebDriverUtility;

public class GoldBarsOrderHistoryPage {

	//Finding WebElements Using @FindBy Annotations

	@FindBy(xpath="//tr[td[normalize-space(.)='1']]")private WebElement AllGoldCoinsOrderDetailsBasedOnSerialNoEle;
	
	@FindBy(xpath="(//td)[1]")private WebElement SerialNoEle;
	
	@FindBy(xpath="(//td)[2]")private WebElement OrderIdEle;
	
	@FindBy(xpath="//tr[@class='ng-star-inserted']/td[.='GB-98160']")private WebElement OrderIdDynamicEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[1]")private WebElement DateAndTimeAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[3]")private WebElement QtyGramsAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[4]")private WebElement PricePerGramAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[5]")private WebElement TotalPriceAsPerOrderIDEle;
	
	@FindBy(xpath="(//tr[@class='ng-star-inserted']/td[.='GB-98160']/following-sibling::td)[6]")private WebElement PenalityAsPerOrderIDEle;
	
	// Update Payment Document Btn Based on OrderID //////// Need to Make Dynamic
	@FindBy(xpath="//tr[td[normalize-space()='GB-91636']]//button[normalize-space()='Update Payment Document']")private WebElement UpdatePaymentDocumentBtnAsPerOrderId;
	
	// Order Status Ele Based on OrderID //////// Need to Make Dynamic
	@FindBy(xpath="//tr/td/following-sibling::td[.='GB-91636']/following-sibling::td[last()]")private WebElement OrderStatusAsPerOrderIdEle;
	
	@FindBy(xpath="//tr/td[.='1']/following-sibling::td[.='GB-55065']/following-sibling::td/p[.=' Document Submitted ']")private WebElement DocumentsSubmitedSuccessMsg;
	
	// Upto here done want to develop Excess eles whwn it comes
	
    
	//Rule-2:Create a constructor to initilise these elements
	public GoldBarsOrderHistoryPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}


	//Rule-3:Provide getters to access these variables

	public WebElement getAllGoldCoinsOrderDetailsBasedOnSerialNoEle() {
		return AllGoldCoinsOrderDetailsBasedOnSerialNoEle;
	}

	
	public WebElement getSerialNoEle() {
		return SerialNoEle;
	}


	public WebElement getOrderIdEle() {
		return OrderIdEle;
	}


	public WebElement getOrderIdDynamicEle() {
		return OrderIdDynamicEle;
	}


	public WebElement getDateAndTimeAsPerOrderIDEle() {
		return DateAndTimeAsPerOrderIDEle;
	}


	public WebElement getQtyGramsAsPerOrderIDEle() {
		return QtyGramsAsPerOrderIDEle;
	}


	public WebElement getPricePerGramAsPerOrderIDEle() {
		return PricePerGramAsPerOrderIDEle;
	}


	public WebElement getTotalPriceAsPerOrderIDEle() {
		return TotalPriceAsPerOrderIDEle;
	}


	public WebElement getPenalityAsPerOrderIDEle() {
		return PenalityAsPerOrderIDEle;
	}


	public WebElement getUpdatePaymentDocumentBtnAsPerOrderId() {
		return UpdatePaymentDocumentBtnAsPerOrderId;
	}


	public WebElement getDocumentsSubmitedSuccessMsg() {
		return DocumentsSubmitedSuccessMsg;
	}


	public WebElement getOrderStatusAsPerOrderIdEle() {
		return OrderStatusAsPerOrderIdEle;
	}
	
		
	// Business Library
	
	public void getPurchasedGoldDetails(WebDriver driver)
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		wUtil.waitForElementToBeVisible(null, OrderIdEle);
//		String OrderId =  driver.findElement(By.xpath("//tr[@class='ng-star-inserted']/td[.='"+OrderID+"']")).getText();
		String OrderId =  OrderIdEle.getText();
		String Quantity = driver.findElement(By.xpath("(//tr[@class='ng-star-inserted']/td[.='"+OrderId+"']/following-sibling::td)[3]")).getText();
		String PricePerGram =  driver.findElement(By.xpath("(//tr[@class='ng-star-inserted']/td[.='"+OrderId+"']/following-sibling::td)[4]")).getText();
		String TotalPrice = driver.findElement(By.xpath("(//tr[@class='ng-star-inserted']/td[.='"+OrderId+"']/following-sibling::td)[5]")).getText();
		System.out.println("Distributor Buyed Gold Successfully @OrderId is "+OrderId+" --- @Quantity in Grams is "+Quantity+" --- @Price per Gram is "+PricePerGram+" --- @Total Price is "+TotalPrice);
		driver.findElement(By.xpath("//tr[td[normalize-space()='"+OrderId+"']]//button[normalize-space()='Update Payment Document']")).click();
		
	}
	
	public String getOrderStatusAsPerOrderID(WebDriver driver, String OrderID) throws Exception
	{
		Thread.sleep(2000);
		String OrderStatus = driver.findElement(By.xpath("//tr/td/following-sibling::td[.='"+OrderID+"']/following-sibling::td[last()]")).getText();
		return OrderStatus;
	}
		
}
