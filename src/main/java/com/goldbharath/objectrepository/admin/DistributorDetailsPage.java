package com.goldbharath.objectrepository.admin;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DistributorDetailsPage {

	// Rule-1:-Finding WebElements Using @FindBy Annotations

    @FindBy(xpath="//button[.='Reject']")private WebElement RejectBtn;
    
    @FindBy(xpath="//button[.='Accept']")private WebElement AcceptBtn;
    
    @FindBy(xpath="//strong[.='Proprietor PAN:']/../preceding-sibling::div//button[.='Download']")private WebElement PanDownloadBtn;
    
    @FindBy(xpath="//strong[.='Proprietor GST:']/../preceding-sibling::div//button[.='Download']")private WebElement GstDownloadBtn;
    
    @FindBy(xpath="//strong[.='KYC Status']/following-sibling::span[.='Rejected']")private WebElement RejectedStatus;
    
    
	//Rule-2:Create a constructor to initilise these elements
    
	public DistributorDetailsPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	
	//Rule-3:Provide getters to access these variables
	
	public WebElement getRejectBtn() {
		return RejectBtn;
	}


	public WebElement getAcceptBtn() {
		return AcceptBtn;
	}


	public WebElement getPanDownloadBtn() {
		return PanDownloadBtn;
	}


	public WebElement getRejectedStatus() {
		return RejectedStatus;
	}


	public WebElement getGstDownloadBtn() {
		return GstDownloadBtn;
	}
	
	// Business Library
	public void acceptingDistributorKycDetails() throws Exception
	{
		Thread.sleep(2000);
		AcceptBtn.click();
	}
	
	public void rejectingDistributorKycDetails() throws Exception
	{
		Thread.sleep(2000);
		RejectBtn.click();
	}
	
//	public void checkingRejectedStatus() throws Exception
//	{
//		Thread.sleep(2000);
//		if(RejectedStatus.isDisplayed())
//		{
//			System.out.println("Distributor Rejected By ADMIN");
//		}
//		else
//		{
//			System.out.println("Distributor Not Rejected");
//		}
//	}
	
	
	public void checkingRejectedStatus(WebDriver driver) throws Exception
	{
	    boolean isRejected;
	    try {
	        isRejected = new WebDriverWait(driver, Duration.ofSeconds(20))
	                .until(ExpectedConditions.visibilityOf(RejectedStatus)).isDisplayed();
	    } catch (org.openqa.selenium.TimeoutException e) {
	        isRejected = false;
	    }

	    Assert.assertTrue(isRejected, "Distributor is not rejected by ADMIN");
	    System.out.println("Distributor Rejected By ADMIN");
	}
	
}
