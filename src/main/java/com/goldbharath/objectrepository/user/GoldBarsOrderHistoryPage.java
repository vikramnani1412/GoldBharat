package com.goldbharath.objectrepository.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GoldBarsOrderHistoryPage {

	//Finding WebElements Using @FindBy Annotations

		@FindBy(xpath="//tr[td[normalize-space(.)='1']]")private WebElement AllGoldCoinsOrderDetailsBasedOnSerialNoEle;
		
		@FindBy(xpath="(//td)[1]")private WebElement SerialNoEle;
		
		@FindBy(xpath="(//td)[2]")private WebElement OrderIdEle;
		
		@FindBy(xpath="(//td)[3]")private WebElement DateAndTimeEle;
		
		@FindBy(xpath="(//td)[4]")private WebElement CoinsWeightQtyEle;
		
		@FindBy(xpath="(//td)[5]")private WebElement TotalWeightInGramsEle;
		
		@FindBy(xpath="(//td)[6]")private WebElement PricePerGramEle;
		
		@FindBy(xpath="(//td)[7]")private WebElement TotalPriceEle;
		
		@FindBy(xpath="(//td)[8]")private WebElement PenalityEle;
		
		// Update Payment Document Btn Based on OrderID //////// Need to Make Dynamic
		@FindBy(xpath="//tr[td[normalize-space()='GB-91636']]//button[normalize-space()='Update Payment Document']")private WebElement UpdatePaymentDocumentBtn;
		
		// Order Status Ele Based on OrderID //////// Need to Make Dynamic
		@FindBy(xpath="//tr/td/following-sibling::td[.='GB-91636']/following-sibling::td[last()]")private WebElement OrderStatusEle;
		
		// Upto here done want to develop Excess eles
		
		
		
	    
	    
		//Rule-2:Create a constructor to initilise these elements
		public GoldBarsOrderHistoryPage(WebDriver driver)
		{
			PageFactory.initElements(driver, this);
		}
		
		
		//Rule-3:Provide getters to access these variables
	
}
