package com.goldbharath.objectrepository.admin;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.goldbharath.genericutilities.WebDriverUtility;

public class BuyOrderPage {

//Finding WebElements Using @FindBy Annotations
	
    @FindBy(xpath="//span[.='Dashboard']")private WebElement DashboardLnk;
    
    @FindBy(xpath="//span[.='Manage Distributor']")private WebElement ManageDistributorLnk;
    
    @FindBy(xpath="//span[.='Manage Order']")private WebElement ManageOrderLnk;
    
    @FindBy(xpath="//span[.='Coins']")private WebElement CoinsLnk;
    
    @FindBy(xpath="//i[@class='fa-solid fa-arrow-left icon-vertical ng-star-inserted']")private WebElement DashBoardCloseBtn;
    
    @FindBy(xpath="//i[@class='fa-solid fa-bell user-icon']")private WebElement NotificationsIcon;
    
    @FindBy(xpath="//button[@class='logout']")private WebElement LogoutBtn;
    
    @FindBy(xpath="//button[.=' Buy Order ']")private WebElement BuyOrderLnk;
    
    @FindBy(xpath="//button[.=' Book Order ']")private WebElement BookOrderLnk;
    
    @FindBy(xpath="//button[.=' Coins ']")private WebElement CoinsLnkk;
    
    @FindBy(xpath="//input[contains(@placeholder, 'Search')]")private WebElement SearchEdt;
    
    @FindBy(xpath="//button[.= ' Export to Excel ']")private WebElement ExportToExcelBtn;
    
    // Confirm Btn Based on Order ID Have to make it dynamic
    @FindBy(xpath="//td[.='GB-44491']/following-sibling::td/button[.=' Confirm Payment ']")private WebElement ConfirmPaymentBtnBasedOnOrderID;
    
    @FindBy(xpath="//button[.=' Accept ']")private WebElement AcceptBtn;
    
    // View Receipt Txt Based On OrderID have to make Dynamic
    @FindBy(xpath="//td[.='GB-50493']/following-sibling::td/a[.='View Receipt']")private WebElement ViewReceiptTxtBasedOnOrderID;
    
    // Verify Order Btn Based On OrderID have to make Dynamic
    @FindBy(xpath="//td[.='GB-50493']/following-sibling::td/button[.=' Verify Order ']")private WebElement VerifyOrderBtnBasedOnOrderID;
    
    @FindBy(xpath="//input[@placeholder='Enter delivery message']")private WebElement EnterDeliveryMessageEdt;
    
    @FindBy(xpath="//input[@type='datetime-local']")private WebElement DateEdt;
    
    @FindBy(xpath="//button[.='Yes']")private WebElement YesBtn;
    
    @FindBy(xpath="//button[.='No']")private WebElement NoBtn;  // Want to check from here
    
    @FindBy(xpath="//button[.=' Decline ']")private WebElement DeclineBtn;
    
    @FindBy(xpath="//h6[.='Enter Reason for Decline :']/following-sibling::div/textarea[@placeholder='Type something here...']")private WebElement ReasonForDeclineEdt;
    
    @FindBy(xpath="//button[.=' Submit ']")private WebElement SubmitBtn;
    
    @FindBy(xpath="//button[.=' close ']")private WebElement CloseBtn;
    
    // Page Number Have to make it dynamic
    @FindBy(xpath="//span//span[contains(.,'1')]")private WebElement EditBtn;
    
    @FindBy(xpath = "//a[text()=' Previous ']")private WebElement PreviousLnk;
    
    @FindBy(xpath = "//a[text()=' Next ']")private WebElement NextLnk;
    
    
    //Rule-2:Create a constructor to initilise these elements
    
  	public BuyOrderPage(WebDriver driver)
  	{
  		PageFactory.initElements(driver, this);
  	}


  	//Rule-3:Provide getters to access these variables
	

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


	public WebElement getNotificationsIcon() {
		return NotificationsIcon;
	}


	public WebElement getLogoutBtn() {
		return LogoutBtn;
	}


	public WebElement getBuyOrderLnk() {
		return BuyOrderLnk;
	}


	public WebElement getBookOrderLnk() {
		return BookOrderLnk;
	}


	public WebElement getCoinsLnkk() {
		return CoinsLnkk;
	}


	public WebElement getSearchEdt() {
		return SearchEdt;
	}


	public WebElement getExportToExcelBtn() {
		return ExportToExcelBtn;
	}


	public WebElement getConfirmPaymentBtnBasedOnOrderID() {
		return ConfirmPaymentBtnBasedOnOrderID;
	}


	public WebElement getAcceptBtn() {
		return AcceptBtn;
	}


	public WebElement getDeclineBtn() {
		return DeclineBtn;
	}


	public WebElement getReasonForDeclineEdt() {
		return ReasonForDeclineEdt;
	}


	public WebElement getSubmitBtn() {
		return SubmitBtn;
	}


	public WebElement getCloseBtn() {
		return CloseBtn;
	}


	public WebElement getEditBtn() {
		return EditBtn;
	}


	public WebElement getPreviousLnk() {
		return PreviousLnk;
	}


	public WebElement getNextLnk() {
		return NextLnk;
	}
  	
  	// Business Library
  	
	public void confirmingPaymentBasedOnOrderID(WebDriver driver, String OrderID, String DeliveryMsg, String dateTime) throws Exception
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		
		String[] parts = dateTime.split(" ");

        String date = parts[0];
        String time = parts[1];
		
		Thread.sleep(2000);
		driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/button[.=' Confirm Payment ']")).click();
		Thread.sleep(2000);
		AcceptBtn.click();
		Thread.sleep(2000);
		WebElement viewReceiptTxt = driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/a[.='View Receipt']"));
		
		if(viewReceiptTxt.isDisplayed())
		{
			Robot r = new Robot();
			
			driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/button[.=' Verify Order ']")).click();
			Thread.sleep(2000);
			EnterDeliveryMessageEdt.sendKeys(DeliveryMsg);
			Thread.sleep(2000);
			DateEdt.sendKeys(dateTime);
			Thread.sleep(2000);
			// Press and release Tab 3 times
			for (int i = 0; i < 3; i++) {
			    Thread.sleep(1000);
			    r.keyPress(KeyEvent.VK_TAB);
			    Thread.sleep(1000);
			    r.keyRelease(KeyEvent.VK_TAB);
			}

			// Press and release Down Arrow 3 times
			for (int i = 0; i < 3; i++) {
			    Thread.sleep(1000);
			    r.keyPress(KeyEvent.VK_DOWN);
			    Thread.sleep(1000);
			    r.keyRelease(KeyEvent.VK_DOWN);
			}

			// Press and release Tab once
			Thread.sleep(1000);
			r.keyPress(KeyEvent.VK_TAB);
			Thread.sleep(1000);
			r.keyRelease(KeyEvent.VK_TAB);

			// Press and release Down Arrow 3 times
			for (int i = 0; i < 3; i++) {
			    Thread.sleep(1000);
			    r.keyPress(KeyEvent.VK_DOWN);
			    Thread.sleep(1000);
			    r.keyRelease(KeyEvent.VK_DOWN);
			}
			
			
			Thread.sleep(2000);
			YesBtn.click();
			Thread.sleep(2000);
		}
		
	}
	
	
	
	
	public void decliningPaymentBasedOnOrderID(WebDriver driver, String OrderID, String Reason) throws Exception
	{
		Thread.sleep(2000);
		driver.findElement(By.xpath("//td[.='"+OrderID+"']/following-sibling::td/button[.=' Confirm Payment ']")).click();
		Thread.sleep(2000);
		DeclineBtn.click();
		Thread.sleep(2000);
		ReasonForDeclineEdt.sendKeys(Reason);
		Thread.sleep(2000);
		SubmitBtn.click();
	}
	
}
