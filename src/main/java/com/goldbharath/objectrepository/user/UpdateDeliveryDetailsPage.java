package com.goldbharath.objectrepository.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.goldbharath.genericutilities.WebDriverUtility;

public class UpdateDeliveryDetailsPage {

	//Finding WebElements Using @FindBy Annotations

    @FindBy(xpath="//h3[.=' Update Delivery Details ']/following-sibling::button[@class='btn-close']")private WebElement CloseBtn;
    
    @FindBy(xpath="//div[@class='admin_login_title']")private WebElement DeliveryMsgAndDateTime;
    
    @FindBy(xpath="//select[@class='input-filed']")private WebElement OrderPickedByDrpDwn;
    
    // If Others Selected
    
    @FindBy(xpath="//input[@placeholder=' Enter Your Name']")private WebElement NameEdt;
    
    @FindBy(xpath="//input[@placeholder=' Enter Your Number']")private WebElement PhoneNumberEdt;
    
    @FindBy(xpath="//input[@placeholder='Ex:Aadhar/Pan/License']")private WebElement IDnumberEdt;
   
    @FindBy(xpath="//label[.='Upload Id Card']/following-sibling::input[@type='file']")private WebElement IdChooseFileBtn;

    @FindBy(xpath="//button[.=' Submit ']")private WebElement SubmitBtn;
	//Rule-2:Create a constructor to initilise these elements
    
	public UpdateDeliveryDetailsPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}


	//Rule-3:Provide getters to access these variables
	
	public WebElement getCloseBtn() {
		return CloseBtn;
	}

	public WebElement getDeliveryMsgAndDateTime() {
		return DeliveryMsgAndDateTime;
	}

	public WebElement getOrderPickedByDrpDwn() {
		return OrderPickedByDrpDwn;
	}
	

	public WebElement getNameEdt() {
		return NameEdt;
	}


	public WebElement getPhoneNumberEdt() {
		return PhoneNumberEdt;
	}


	public WebElement getIDnumberEdt() {
		return IDnumberEdt;
	}


	public WebElement getIdChooseFileBtn() {
		return IdChooseFileBtn;
	}


	public WebElement getSubmitBtn() {
		return SubmitBtn;
	}
	
	// Business Library
	
	public void updatingDeliveryDetails(WebDriver driver, String Name, String PhoneNo, String IdNo, String IdCardPath) throws Exception
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		
		Thread.sleep(2000);
		String Msg = DeliveryMsgAndDateTime.getText();
		System.out.println("Delivery Message is "+Msg);
		wUtil.handleDropdownByIndex(OrderPickedByDrpDwn, 1);
		Thread.sleep(2000);
		try {
			NameEdt.sendKeys(Name);
			Thread.sleep(1000);
			PhoneNumberEdt.sendKeys(PhoneNo);
			Thread.sleep(1000);
			IDnumberEdt.sendKeys(IdNo);
			Thread.sleep(1000);
			IdChooseFileBtn.sendKeys(IdCardPath);
		} catch (Exception e) {
			Thread.sleep(2000);
		}
		finally {
			if(SubmitBtn.isDisplayed())
			{
				Thread.sleep(2000);
				SubmitBtn.click();
				Thread.sleep(2000);
				driver.navigate().refresh();
			}
			else
			{
				System.out.println("Submit Button is not Displayed");
			}
		}
	}
	
}
