package com.goldbharath.objectrepository.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GoldCoinsOrdersPage {

	//Finding WebElements Using @FindBy Annotations

	@FindBy(xpath="//tr[td[normalize-space(.)='1']]")private WebElement AllGoldCoinsOrderDetailsBasedOnSerialNoEle;
	
	@FindBy(xpath="(//td)[1]")private WebElement SerialNoEle;
	
	@FindBy(xpath="(//td)[2]")private WebElement OrderIdEle;
	
	@FindBy(xpath="(//td)[3]")private WebElement DateAndTimeEle;
	
	@FindBy(xpath="(//td)[4]")private WebElement CoinsWeightQtyEle;
	
	@FindBy(xpath="(//td)[5]")private WebElement TotalWeightInGramsEle;
	
	@FindBy(xpath="(//td)[6]")private WebElement PurchasedPriceEle;
	
	@FindBy(xpath="(//td)[7]")private WebElement MakingChargesEle;
	
	@FindBy(xpath="(//td)[8]")private WebElement TotalPriceEle;
	
	@FindBy(xpath="(//td)[10]")private WebElement OrderStatusEle;
	
	@FindBy(xpath="((//td)[1]/following-sibling::td)[1]")private WebElement OrderIdBasedOnSerialNumEle;
	
	
	// Want to make it Dynamic From Here
	@FindBy(xpath="(//td[.='GB-56646']/following-sibling::td/following-sibling::td)[1]")private WebElement TotalCoinsWeightAndQuantityBasedOnOrderIDEle;
	
	@FindBy(xpath="//td[normalize-space()='GB-56646']/following-sibling::td[6]")private WebElement TotalPriceBasedOnOrderIDEle;
	
	@FindBy(xpath="//td[normalize-space()='GB-43827']/following-sibling::td[.=' Update Payment Document ']")private WebElement UpdatePaymentDocumentBtn;
	
	@FindBy(xpath="//tr[td[normalize-space()='GB-93537']]//p[normalize-space()='Update Payment Document']")private WebElement UpdatePaymentDocumentBtnBasedOnOrderId;
	
    @FindBy(xpath="//td[.='GB-56646']/..")private WebElement OrderHistoryRowBasedOnOrderID;
    
    // Documents Submitted Msg
    @FindBy(xpath="//tr[td[normalize-space()='GB-92649']]//p[normalize-space()='Document Submitted']")private WebElement DocumentsSubmittedMsg;
    
    // Order Status Msg
    @FindBy(xpath="//tr[td[normalize-space()='GB-56646']]//p[normalize-space()='Order Accepted']")private WebElement OrderAcceptedMsg;
    
    
	//Rule-2:Create a constructor to initilise these elements
	public GoldCoinsOrdersPage(WebDriver driver)
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


	public WebElement getDateAndTimeEle() {
		return DateAndTimeEle;
	}


	public WebElement getCoinsWeightQtyEle() {
		return CoinsWeightQtyEle;
	}


	public WebElement getTotalCoinsWeightAndQuantityBasedOnOrderIDEle() {
		return TotalCoinsWeightAndQuantityBasedOnOrderIDEle;
	}


	public WebElement getTotalPriceBasedOnOrderIDEle() {
		return TotalPriceBasedOnOrderIDEle;
	}


	public WebElement getOrderHistoryRowBasedOnOrderID() {
		return OrderHistoryRowBasedOnOrderID;
	}


	public WebElement getDocumentsSubmittedMsg() {
		return DocumentsSubmittedMsg;
	}


	public WebElement getOrderAcceptedMsg() {
		return OrderAcceptedMsg;
	}


	public WebElement getTotalWeightInGramsEle() {
		return TotalWeightInGramsEle;
	}


	public WebElement getPurchasedPriceEle() {
		return PurchasedPriceEle;
	}


	public WebElement getMakingChargesEle() {
		return MakingChargesEle;
	}


	public WebElement getTotalPriceEle() {
		return TotalPriceEle;
	}


	public WebElement getOrderStatusEle() {
		return OrderStatusEle;
	}


	public WebElement getOrderIdBasedOnSerialNumEle() {
		return OrderIdBasedOnSerialNumEle;
	}


	public WebElement getUpdatePaymentDocumentBtn() {
		return UpdatePaymentDocumentBtn;
	}


	public WebElement getUpdatePaymentDocumentBtnBasedOnOrderId() {
		return UpdatePaymentDocumentBtnBasedOnOrderId;
	}

	
	
	// Business Library
	
	
	public void GetAllOrderDetailsBasedOnSerialNo(WebDriver driver, String SerialNo) throws Exception
	{
		Thread.sleep(2000);
		String OrderDetails = driver.findElement(By.xpath("//tr[td[normalize-space(.)='"+SerialNo+"']]")).getText();
		System.out.println(OrderDetails);
	}
	
	public String GetOrderIdBasedOnSerialNo(WebDriver driver, String SerialNo) throws Exception
	{
		Thread.sleep(2000);
		String OrderId = driver.findElement(By.xpath("(//td)["+SerialNo+"]/following-sibling::td")).getText();
		System.out.println(OrderId);
		return OrderId;
	}
	
	public void GetOrderIdCoinsWeightBasedOnSerialNo(WebDriver driver, String SerialNo) throws Exception
	{
		Thread.sleep(2000);
		String OrderId = driver.findElement(By.xpath("(//td)["+SerialNo+"]/following-sibling::td")).getText();
		Thread.sleep(2000);
		String Quantity = driver.findElement(By.xpath("(//td)["+SerialNo+"]/following-sibling::td/following-sibling::td/following-sibling::td")).getText();
		Thread.sleep(2000);
		System.out.println(OrderId+"------->"+Quantity);
	}
	
	public void GetOrderIdCoinsWeightAndTotalPriceBasedOnSerialNo(WebDriver driver, String SerialNo) throws Exception
	{
		Thread.sleep(2000);
		String OrderId = driver.findElement(By.xpath("(//td)["+SerialNo+"]/following-sibling::td")).getText();
		Thread.sleep(2000);
		String Quantity = driver.findElement(By.xpath("(//td)["+SerialNo+"]/following-sibling::td/following-sibling::td/following-sibling::td")).getText();
		Thread.sleep(2000);
		String TotalPrice = driver.findElement(By.xpath("(//td)["+SerialNo+"]/following-sibling::td/following-sibling::td/following-sibling::td/following-sibling::td/following-sibling::td/following-sibling::td/following-sibling::td")).getText();
		Thread.sleep(2000);
		System.out.println(OrderId+"------->"+Quantity+"------->"+TotalPrice);
	}
	
	public void getTotalGoldHistoryWhatWeBuyedBasedOnOnlyOrderID(WebDriver driver, String OrderID) throws Exception
	{
		Thread.sleep(2000);
		String History = driver.findElement(By.xpath("//td[.='"+OrderID+"']/..")).getText();
		System.out.println(History);
	}
	
	public void clickOnUpdatePaymentDocumentBtnBasedOnOrderID(WebDriver driver, String OrderID) throws InterruptedException
	{
		Thread.sleep(2000);
		driver.findElement(By.xpath("//tr[td[normalize-space()='"+OrderID+"']]//p[normalize-space()='Update Payment Document']")).click();
	}
	
	public void successMsgValidation(WebDriver driver, String OrderID) throws Exception
	{
		WebElement Success = driver.findElement(By.xpath("//td[normalize-space()='"+OrderID+"']/following-sibling::td/p[normalize-space()='Document Submitted']"));
		Thread.sleep(2000);
		if(Success.isDisplayed())
		{
			System.out.println("Transaction Details Uploaded Successfully");
		}
		else
		{
			System.out.println("Transaction Details Not Uploaded");
		}
	}
	
}
