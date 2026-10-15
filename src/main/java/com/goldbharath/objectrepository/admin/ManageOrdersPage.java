package com.goldbharath.objectrepository.admin;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.goldbharath.genericutilities.WebDriverUtility;

public class ManageOrdersPage {

	//Finding WebElements Using @FindBy Annotations
	
    @FindBy(xpath="//span[.='Dashboard']")private WebElement DashboardLnk;
    
    @FindBy(xpath="//span[.='Manage Distributor']")private WebElement ManageDistributorLnk;
    
    @FindBy(xpath="//span[.='Manage Order']")private WebElement ManageOrderLnk;
    
    @FindBy(xpath="//span[.='Coins']")private WebElement CoinsLnk;
    
    @FindBy(xpath="//i[@class='fa-solid fa-arrow-left icon-vertical ng-star-inserted']")private WebElement DashBoardCloseBtn;
    
    @FindBy(xpath="//button[.=' Buy Order ']")private WebElement BuyOrderBtn;
  
    @FindBy(xpath="//button[.=' Book Order ']")private WebElement BookOrderBtn;
  
    @FindBy(xpath="//button[.=' Coins ']")private WebElement CoinsBtn;
    
    @FindBy(xpath="//input[@placeholder='Search by Order No., Name, State, City....']")private WebElement SearchEdt;
    
    @FindBy(xpath="//button[.=' Export to Excel ']")private WebElement ExportToExcelBtn;
	
    @FindBy(xpath="//i[@class='fa-solid fa-bell user-icon']")private WebElement NotificationsIcon;
    
    @FindBy(xpath="//button[@class='logout']")private WebElement LogoutBtn;
    
    
    /////////////// In Coins Page Coins View Btn
    
    @FindBy(xpath="//td[contains(normalize-space(.),'GB-53352')]/following-sibling::td/button[@title='View Coin Details']")private WebElement QtyPurchasedViewBtnBasedOnOrderId;
    
    @FindBy(xpath="(//div[@class='modal-header-icon']/following-sibling::h5/following-sibling::button[@class='btn-close'])[1]")private WebElement QtyPurchasedViewPageCloseBtn;
    
    ///////////////
    
   
    @FindBy(xpath="//td[contains(normalize-space(.),'GB-83077')]/following-sibling::td/button[.=' Confirm Payment ']")private WebElement CofirmPaymentBtnBasedOnOrderId;
    
    @FindBy(xpath="//td[contains(normalize-space(.),'GB-54937')]/following-sibling::td/following-sibling::td/button[normalize-space()='Reject Order']")private WebElement RejectOrderBtnBasedOnOrderId;
    
    @FindBy(xpath="//a[text()=' Previous ']")private WebElement PreviousPageLnk;
    
    @FindBy(xpath="//a[text()=' Next ']")private WebElement NextPageLnk;
    
    // Page no has to make dynamic
    @FindBy(xpath="//span[.='1']")private WebElement ExactPageBasedOnIndex;
    
//////////////////////////////////////////////After Clicking On Quantity Purchased View Btn Coins Page
    
    @FindBy(xpath="(//div[@class='modal-header-icon']/following-sibling::h5[.='Coin']/following-sibling::button[@class='btn-close'])[1]")private WebElement ClosePageBtn;
    
////////////////////////////////////////////// After Clicking On Confirm Payment Order Details Page
    
    @FindBy(xpath="//button[normalize-space()='Accept']")private WebElement AcceptBtn;
    
    @FindBy(xpath="//button[normalize-space()='Decline']")private WebElement DeclineBtn;
    
    @FindBy(xpath="//button[normalize-space()='close']")private WebElement CloseBtn;
    
//////////////////////////////////////////////After Clicking On Reject Order Button
 
    @FindBy(xpath="//button[.='Yes']")private WebElement YesBtn;
    
    @FindBy(xpath="//button[.='No']")private WebElement NoBtn;
    
    ////////////////////////////// Last Verify Order Button In Confirm Dispatch Page
    
    @FindBy(xpath="//td[.='GB-16679']/following-sibling::td/button[.=' Verify Order ']")private WebElement VerifyOrderBtn;
    
    @FindBy(xpath="//input[@placeholder='Enter delivery message']")private WebElement DeliveryMessageEdt;
    
    @FindBy(xpath="//input[@placeholder='Enter delivery time']")private WebElement DeliveryDateTimeEdt;
    
    // Confirm Dispatch Page No Btn
    @FindBy(xpath="//button[normalize-space()='No']")private WebElement InConfirmDispatchPageNoBtn;
    
    // After Dispatch
    @FindBy(xpath="//td[.='GB-66957']/following-sibling::td/button[.=' Dispatched ']")private WebElement DispatchedConfirmText;
  
    
    //Rule-3:Create a constructor to initilise these elements
    
  	public ManageOrdersPage(WebDriver driver)
  	{
  		PageFactory.initElements(driver, this);
  	}
  	
  	
  	//Rule-4:Provide getters to access these variables
    
  	public WebElement getDashboardLnk() {
		return DashboardLnk;
	}



	public WebElement getManageDistributorLnk() {
		return ManageDistributorLnk;
	}



	public WebElement getManageOrderLnk() {
		return ManageOrderLnk;
	}



	public WebElement getCoinsLnk() {
		return CoinsLnk;
	}



	public WebElement getDashBoardCloseBtn() {
		return DashBoardCloseBtn;
	}



	public WebElement getBuyOrderBtn() {
		return BuyOrderBtn;
	}



	public WebElement getBookOrderBtn() {
		return BookOrderBtn;
	}



	public WebElement getCoinsBtn() {
		return CoinsBtn;
	}



	public WebElement getSearchEdt() {
		return SearchEdt;
	}



	public WebElement getVerifyOrderBtn() {
		return VerifyOrderBtn;
	}


	public WebElement getDeliveryMessageEdt() {
		return DeliveryMessageEdt;
	}


	public WebElement getDeliveryDateTimeEdt() {
		return DeliveryDateTimeEdt;
	}


	public WebElement getInConfirmDispatchPageNoBtn() {
		return InConfirmDispatchPageNoBtn;
	}


	public WebElement getExportToExcelBtn() {
		return ExportToExcelBtn;
	}



	public WebElement getNotificationsIcon() {
		return NotificationsIcon;
	}



	public WebElement getLogoutBtn() {
		return LogoutBtn;
	}



	public WebElement getQtyPurchasedViewBtnBasedOnOrderId() {
		return QtyPurchasedViewBtnBasedOnOrderId;
	}



	public WebElement getQtyPurchasedViewPageCloseBtn() {
		return QtyPurchasedViewPageCloseBtn;
	}


	public WebElement getCofirmPaymentBtnBasedOnOrderId() {
		return CofirmPaymentBtnBasedOnOrderId;
	}



	public WebElement getRejectOrderBtnBasedOnOrderId() {
		return RejectOrderBtnBasedOnOrderId;
	}



	public WebElement getDispatchedConfirmText() {
		return DispatchedConfirmText;
	}


	public WebElement getPreviousPageLnk() {
		return PreviousPageLnk;
	}



	public WebElement getNextPageLnk() {
		return NextPageLnk;
	}



	public WebElement getExactPageBasedOnIndex() {
		return ExactPageBasedOnIndex;
	}



	public WebElement getClosePageBtn() {
		return ClosePageBtn;
	}



	public WebElement getAcceptBtn() {
		return AcceptBtn;
	}



	public WebElement getDeclineBtn() {
		return DeclineBtn;
	}



	public WebElement getCloseBtn() {
		return CloseBtn;
	}
	


	public WebElement getYesBtn() {
		return YesBtn;
	}



	public WebElement getNoBtn() {
		return NoBtn;
	}
  	
  	// Business Library
  	
  	public void clickOnCoinsLnk(WebDriver driver)
  	{
  		WebDriverUtility wUtil = new WebDriverUtility();
  		wUtil.waitForPageLoad(driver);
  		
  		CoinsBtn.click();
  	}
  	
  	
  	//  //button[.=' Accept ']
  	//  //button[.=' Decline ']
  	//  //button[.=' close ']
  	public void checkingRecentlyBuyedCoinsDetails(WebDriver driver, String OrderID) throws Exception
  	{
  		Thread.sleep(2000);
  		CoinsBtn.click();
  		Thread.sleep(2000);
  		driver.findElement(By.xpath("//td[contains(normalize-space(.),'"+OrderID+"')]/following-sibling::td/button[@title='View Coin Details']")).click();
  		Thread.sleep(5000);
  		QtyPurchasedViewPageCloseBtn.click();
  		Thread.sleep(2000);
  		driver.findElement(By.xpath("//td[contains(normalize-space(.),'"+OrderID+"')]/following-sibling::td/button[.=' Confirm Payment ']")).click();
  	    Thread.sleep(2000);
  	    String Details = driver.findElement(By.xpath("//h4[text()='Order Details']/..")).getText();
  		Thread.sleep(2000);
  	    System.out.println("Order Details is "+Details);
  		Thread.sleep(2000);
  		AcceptBtn.click();
  	    
  	}
  	
  	public void allActionsInConfirmDispatchPageInLLpPage(String DeliveryMsg, String DeliveryDateTime) throws Exception 
  	{
  		Thread.sleep(2000);
  	    DeliveryMessageEdt.sendKeys(DeliveryMsg);
  	    Thread.sleep(2000);
  	    DeliveryDateTimeEdt.sendKeys(DeliveryDateTime);
  	    Thread.sleep(2000);
  	  Robot r = new Robot();

  	// Press and release TAB 3 times
  	for (int i = 0; i < 3; i++) {
  	    r.keyPress(KeyEvent.VK_TAB);
  	    r.keyRelease(KeyEvent.VK_TAB);
  	    Thread.sleep(1000);
  	}

  	// Press and release DOWN ARROW 3 times
  	for (int i = 0; i < 3; i++) {
  	    r.keyPress(KeyEvent.VK_DOWN);
  	    r.keyRelease(KeyEvent.VK_DOWN);
  	    Thread.sleep(1000);
  	}

  	// Press and release TAB once
  	r.keyPress(KeyEvent.VK_TAB);
  	r.keyRelease(KeyEvent.VK_TAB);
  	Thread.sleep(1000);

  	// Press and release DOWN ARROW 3 times
  	for (int i = 0; i < 3; i++) {
  	    r.keyPress(KeyEvent.VK_DOWN);
  	    r.keyRelease(KeyEvent.VK_DOWN);
  	    Thread.sleep(1000);
  	}

  	// Press and release ENTER
  	r.keyPress(KeyEvent.VK_ENTER);
  	r.keyRelease(KeyEvent.VK_ENTER);
  	
  	    Thread.sleep(2000);
  	    YesBtn.click();
  	}
  	
  	
  	
  	public void clickOnVerifyOrderBtnBasedOnOrderID(WebDriver driver, String OrderID) throws Exception
  	{
  		Thread.sleep(2000);
  		driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/button[.=' Verify Order ']")).click();
  		Thread.sleep(2000);
  	    
  	}
  	
  	public void confirmingOrderStatus(WebDriver driver, String OrderID) throws Exception
  	{
  		Thread.sleep(2000);
  		WebElement DispatchedConfirmMsg = driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/button[.=' Dispatched ']"));
  		Thread.sleep(2000);
        Assert.assertTrue(DispatchedConfirmMsg.isDisplayed(),
                "Problem occurred while dispatching the order.");

        System.out.println("Order dispatched successfully. Order ID: " + OrderID);
  	}
  	
  	
  	
}
