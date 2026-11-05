package com.goldbharath.objectrepository.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.goldbharath.genericutilities.ExcelFileUtility;

public class UploadTransactionDetailsForGoldCoinsPage {

	//Finding WebElements Using @FindBy Annotations
	
    @FindBy(xpath="//h4[.=' Upload transaction details For Gold Coins ']/following-sibling::button[@class='btn-close']")private WebElement CloseBtn;
    
    @FindBy(xpath="(//input[@type='file'])[2]")private WebElement ChooseFileBtn;
    
//    @FindBy(xpath="//h4[.=' Upload transaction details For Gold Coins ']/../following-sibling::div//input[@type='file']")private WebElement ChooseFileBtn;
    
    @FindBy(xpath="//h4[.=' Upload transaction details For Gold Coins ']/../following-sibling::div//input[@placeholder='Enter transaction id']")private WebElement TransactionIdEdt;
    
    @FindBy(xpath="//h4[.=' Upload transaction details For Gold Coins ']/../following-sibling::div//button[.=' Upload ']")private WebElement UploadBtn;
    
    @FindBy(xpath="//p[.='Coin transaction uploaded successfully']")private WebElement SuccessMsg;
  
    
	//Rule-2:Create a constructor to initilise these elements
    
	public UploadTransactionDetailsForGoldCoinsPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	
	//Rule-3:Provide getters to access these variables
	

	public WebElement getCloseBtn() {
		return CloseBtn;
	}

	
	public WebElement getChooseFileBtn() {
		return ChooseFileBtn;
	}


	public WebElement getTransactionIdEdt() {
		return TransactionIdEdt;
	}


	public WebElement getSuccessMsg() {
		return SuccessMsg;
	}


	public WebElement getUploadBtn() {
		return UploadBtn;
	}
	
	// Business Library
	
	public void uploadingTransactionDetails(String TransactionImage, String TransactionIDNum) throws Exception
	{	    		
		Thread.sleep(2000);
		ChooseFileBtn.sendKeys(TransactionImage);
		Thread.sleep(2000);
		TransactionIdEdt.sendKeys(TransactionIDNum);
		Thread.sleep(2000);
		UploadBtn.click();
		Thread.sleep(2000);
		if(SuccessMsg.isDisplayed())
		{
			System.out.println("Transaction Successfully Updated");
		}
		else
		{
			System.out.println("Transaction Not Updated");
		}
	}
	
	public void ReUploadingTransactionDetails(WebDriver driver ,String TransactionImage, String TransactionIDNum) throws Exception
	{
		Thread.sleep(2000);
		WebElement Reason = driver.findElement(By.xpath("//h4[normalize-space()='Upload transaction details']/parent::*/following-sibling::div//b[contains(.,'R')]"));
		Thread.sleep(2000);
		System.out.println("Reason is "+Reason.getText());
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[.=' Upload transaction details ']/../following-sibling::div//b[contains(.,'Rejection Reason :')]/../../following-sibling::input[@type='file']")).sendKeys(TransactionImage);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[.=' Upload transaction details ']/../following-sibling::div//b[contains(.,'Rejection Reason :')]/../../following-sibling::input[@formcontrolname='transation_id']")).sendKeys(TransactionIDNum);
		Thread.sleep(2000);
	    driver.findElement(By.xpath("(//button[.=' Upload '])[1]")).click();
		Thread.sleep(2000);
		if(SuccessMsg.isDisplayed())
		{
			System.out.println("Transaction Successfully Updated");
		}
		else
		{
			System.out.println("Transaction Not Updated");
		}
	}
	
	
}
