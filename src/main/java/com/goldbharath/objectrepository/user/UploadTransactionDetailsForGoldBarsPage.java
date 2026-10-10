package com.goldbharath.objectrepository.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class UploadTransactionDetailsForGoldBarsPage {

//Finding WebElements Using @FindBy Annotations
	
    @FindBy(xpath="//h4[.=' Upload transaction details ']/following-sibling::button[@class='btn-close']")private WebElement CloseBtn;
    
    @FindBy(xpath="(//input[@type='file'])[1]")private WebElement ChooseFileBtn;
    
//    @FindBy(xpath="//h4[.=' Upload transaction details ']/../following-sibling::div//input[@type='file']")private WebElement ChooseFileBtn;
    
    @FindBy(xpath="//h4[.=' Upload transaction details ']/../following-sibling::div//input[@placeholder='Enter transaction id']")private WebElement TransactionIdEdt;
    
    @FindBy(xpath="//h4[.=' Upload transaction details ']/../following-sibling::div//button[.=' Upload ']")private WebElement UploadBtn;
    
    @FindBy(xpath="//p[.='Transaction uploaded successfully']")private WebElement SuccessMsg;
  
    @FindBy(xpath="//tr/td[.='1']/following-sibling::td[.='GB-55065']/following-sibling::td/p[.=' Document Submitted ']")private WebElement DocumentsSubmitedSuccessMsg;
    
	//Rule-2:Create a constructor to initilise these elements
    
	public UploadTransactionDetailsForGoldBarsPage(WebDriver driver)
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


	public WebElement getUploadBtn() {
		return UploadBtn;
	}


	public WebElement getDocumentsSubmitedSuccessMsg() {
		return DocumentsSubmitedSuccessMsg;
	}


	public WebElement getSuccessMsg() {
		return SuccessMsg;
	}
	
	// Business Library
	
	public void uploadingTransactionDetails(WebDriver driver, String TransactionImage, String TransactionIDNum, String OrderID) throws Exception
	{	    		
		Thread.sleep(2000);
		ChooseFileBtn.sendKeys(TransactionImage);
		Thread.sleep(2000);
		TransactionIdEdt.sendKeys(TransactionIDNum);
		Thread.sleep(2000);
		UploadBtn.click();
		Thread.sleep(2000);
		String MSG = driver.findElement(By.xpath("//tr/td[.='1']/following-sibling::td[.='"+OrderID+"']/following-sibling::td/p[.=' Document Submitted ']")).getText();
		Assert.assertTrue(MSG.trim().contains("Document Submitted"), "Documents were not successfully submitted");
		System.out.println("Documents Submitted Successfully");
	}
	
}
