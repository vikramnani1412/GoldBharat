package com.goldbharath.objectrepository.user;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.goldbharath.genericutilities.WebDriverUtility;

public class KycAsLLpPage {

	//Finding WebElements Using @FindBy Annotations

    // Not an exact @class match: Angular can add ng-* classes. Also matches any <select> that has the LLP option.
    private static final String TYPE_DRPDWN_XPATH =
            "(//select[contains(@class,'my-select')] | //select[option[@value='LLP']])[1]";

    @FindBy(xpath=TYPE_DRPDWN_XPATH)private WebElement TypeDrpDwn;
    
    @FindBy(xpath="//option[@value='LLP']")private WebElement SelectLLp;
    
    @FindBy(xpath="//input[@formcontrolname='director_name']")private WebElement DirectorNameEdt;
    
    @FindBy(xpath="//input[@formcontrolname='director_mobile']")private WebElement DirectorMobileNumberEdt;
    
    @FindBy(xpath="//input[@formcontrolname='director_aadhar']")private WebElement DirectorAadharNumberEdt;
    
    @FindBy(xpath="//label[.='Upload Aadhar Back Side']/preceding-sibling::div/input[@type='file']")private WebElement UploadAadharFrontSideImage;
    
    @FindBy(xpath="//label[normalize-space()='Company PAN Number']/preceding-sibling::div[1]/input[@type='file']")private WebElement UploadAadharBackSideImage;
    
    @FindBy(xpath="//input[@placeholder='Enter PAN Number']")private WebElement PanNumberEdt;
    
    @FindBy(xpath="//label[normalize-space()='Upload PAN']/following-sibling::div[1]//input[@type='file']")private WebElement UploadPanImage;
    
    @FindBy(xpath="//input[@placeholder='Enter GST Number']")private WebElement EnterGSTNumberEdt;
    
    @FindBy(xpath="//label[normalize-space()='Upload GST Number']/following-sibling::div[1]/input[@type='file']")private WebElement UploadGstImage;
    
    @FindBy(xpath="//input[@formcontrolname='director_cin']")private WebElement DirectorCinEdt;
    
    @FindBy(xpath="//label[normalize-space()='Upload CIN']/following-sibling::div[1]//input[@type='file']")private WebElement UploadCINDoc;
    
    @FindBy(xpath="//label[normalize-space()='Upload Your Image']/following-sibling::div[1]//input[@type='file']")private WebElement UploadYourImage;
    
    @FindBy(xpath="//input[@placeholder='Enter Bank Name']")private WebElement EnterBankNameEdt;
    
    @FindBy(xpath="//input[@placeholder='Enter A/C Number']")private WebElement EnterAcNumberEdt;
    
    @FindBy(xpath="//input[@placeholder='Enter IFSC Code']")private WebElement EnterIFSCCodeEdt;
    
    @FindBy(xpath="//label[.='Account Holder Name']/following-sibling::div/input[@placeholder='Enter Name']")private WebElement AccountHolderNameEdt;
    
    @FindBy(xpath="//button[.='Submit']")private WebElement SubmitBtn;
    
    @FindBy(xpath="//a[.='Click Here']")private WebElement ClickHereLnk;
    
	//Rule-2:Create a constructor to initilise these elements
    
	public KycAsLLpPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}

	//Rule-3:Provide getters to access these variables
	

	public WebElement getTypeDrpDwn() {
		return TypeDrpDwn;
	}

	public WebElement getSelectLLp() {
		return SelectLLp;
	}

	public WebElement getDirectorNameEdt() {
		return DirectorNameEdt;
	}

	public WebElement getDirectorMobileNumberEdt() {
		return DirectorMobileNumberEdt;
	}

	public WebElement getDirectorAadharNumberEdt() {
		return DirectorAadharNumberEdt;
	}

	public WebElement getUploadAadharFrontSideImage() {
		return UploadAadharFrontSideImage;
	}

	public WebElement getUploadAadharBackSideImage() {
		return UploadAadharBackSideImage;
	}

	public WebElement getPanNumberEdt() {
		return PanNumberEdt;
	}

	public WebElement getUploadPanImage() {
		return UploadPanImage;
	}

	public WebElement getEnterGSTNumberEdt() {
		return EnterGSTNumberEdt;
	}

	public WebElement getUploadGstImage() {
		return UploadGstImage;
	}

	public WebElement getDirectorCinEdt() {
		return DirectorCinEdt;
	}

	public WebElement getUploadCINDoc() {
		return UploadCINDoc;
	}

	public WebElement getUploadYourImage() {
		return UploadYourImage;
	}

	public WebElement getEnterBankNameEdt() {
		return EnterBankNameEdt;
	}

	public WebElement getEnterAcNumberEdt() {
		return EnterAcNumberEdt;
	}

	public WebElement getEnterIFSCCodeEdt() {
		return EnterIFSCCodeEdt;
	}

	public WebElement getAccountHolderNameEdt() {
		return AccountHolderNameEdt;
	}
	
	public WebElement getClickHereLnk() {
		return ClickHereLnk;
	}

	public WebElement getSubmitBtn() {
		return SubmitBtn;
	}
	
	
	// Business Library
	
	// Waits for the KYC form, then selects LLP in the business-type dropdown.
	private void selectLLpType(WebDriver driver) throws Exception
	{
		try {
			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TYPE_DRPDWN_XPATH)));
		} catch (TimeoutException e) {
			String pageText = "";
			try { pageText = driver.findElement(By.tagName("body")).getText().replaceAll("\\s+", " "); } catch (Exception ignored) { }
			if (pageText.length() > 300) { pageText = pageText.substring(0, 300); }
			throw new NoSuchElementException("KYC business-type dropdown not found. URL: " + driver.getCurrentUrl()
					+ " | Page text: " + pageText, e);
		}

		try {
			new Select(TypeDrpDwn).selectByValue("LLP");
		} catch (Exception e) {
			// fallback to the original click-click approach
			TypeDrpDwn.click();
			Thread.sleep(2000);
			SelectLLp.click();
		}
		Thread.sleep(2000);
	}
	
	public void LLpKyc(WebDriver driver, String DirectorName, String MobileNo, String AadharNo, String PanNo, String GstNo, String CIN, String BankName, String AccountNo, String IfscCode, String AcHolderName) throws Exception
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		
		String PanimagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/PAN_Card.jpeg";
		String AadharImagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/Aadhaar_Card.jpeg";
		
		
		Thread.sleep(2000);
		selectLLpType(driver);
		Thread.sleep(2000);
		DirectorNameEdt.sendKeys(DirectorName);
		Thread.sleep(2000);
		DirectorMobileNumberEdt.sendKeys(MobileNo);
		Thread.sleep(2000);
		DirectorAadharNumberEdt.sendKeys(AadharNo);
		Thread.sleep(2000);
		UploadAadharFrontSideImage.sendKeys(AadharImagePath);
		Thread.sleep(2000);
		UploadAadharBackSideImage.sendKeys(AadharImagePath);
		Thread.sleep(2000);
		PanNumberEdt.sendKeys(PanNo);
		Thread.sleep(2000);
		UploadPanImage.sendKeys(PanimagePath);
		Thread.sleep(2000);
		EnterGSTNumberEdt.sendKeys(GstNo);
		Thread.sleep(2000);
		UploadGstImage.sendKeys(PanimagePath);
		Thread.sleep(2000);
		DirectorCinEdt.sendKeys(CIN);
		Thread.sleep(2000);
		UploadCINDoc.sendKeys(PanimagePath);
		Thread.sleep(2000);
		UploadYourImage.sendKeys(AadharImagePath);
		Thread.sleep(2000);
		EnterBankNameEdt.sendKeys(BankName);
		Thread.sleep(2000);
		EnterAcNumberEdt.sendKeys(AccountNo);
		Thread.sleep(2000);
		EnterIFSCCodeEdt.sendKeys(IfscCode);
		Thread.sleep(2000);
	    AccountHolderNameEdt.sendKeys(AcHolderName);
	    Thread.sleep(2000);
	    
	    wUtil.scrollToParticularWebElement(driver, SubmitBtn);
	    Thread.sleep(2000);
	    wUtil.clickOnParticularWebElementUsingActionsClass(driver, SubmitBtn);
	    Thread.sleep(2000);
	}
	
	
	public void LLpKycAfterRejected(WebDriver driver, String DirectorName, String MobileNo, String AadharNo, String PanNo, String GstNo, String CIN, String BankName, String AccountNo, String IfscCode, String AcHolderName) throws Exception
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		
		String PanimagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/PAN_Card.jpeg";
		String AadharImagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/Aadhaar_Card.jpeg";
		
		Thread.sleep(2000);
		ClickHereLnk.click();
		Thread.sleep(2000);
		selectLLpType(driver);
		Thread.sleep(2000);
		DirectorNameEdt.sendKeys(DirectorName);
		Thread.sleep(2000);
		DirectorMobileNumberEdt.sendKeys(MobileNo);
		Thread.sleep(2000);
		DirectorAadharNumberEdt.sendKeys(AadharNo);
		Thread.sleep(2000);
		UploadAadharFrontSideImage.sendKeys(AadharImagePath);
		Thread.sleep(2000);
		UploadAadharBackSideImage.sendKeys(AadharImagePath);
		Thread.sleep(2000);
		PanNumberEdt.sendKeys(PanNo);
		Thread.sleep(2000);
		UploadPanImage.sendKeys(PanimagePath);
		Thread.sleep(2000);
		EnterGSTNumberEdt.sendKeys(GstNo);
		Thread.sleep(2000);
		UploadGstImage.sendKeys(PanimagePath);
		Thread.sleep(2000);
		DirectorCinEdt.sendKeys(CIN);
		Thread.sleep(2000);
		UploadCINDoc.sendKeys(PanimagePath);
		Thread.sleep(2000);
		UploadYourImage.sendKeys(AadharImagePath);
		Thread.sleep(2000);
		EnterBankNameEdt.sendKeys(BankName);
		Thread.sleep(2000);
		EnterAcNumberEdt.sendKeys(AccountNo);
		Thread.sleep(2000);
		EnterIFSCCodeEdt.sendKeys(IfscCode);
		Thread.sleep(2000);
	    AccountHolderNameEdt.sendKeys(AcHolderName);
	    Thread.sleep(2000);
	    
	    wUtil.scrollToParticularWebElement(driver, SubmitBtn);
	    Thread.sleep(2000);
	    wUtil.clickOnParticularWebElementUsingActionsClass(driver, SubmitBtn);
	    Thread.sleep(2000);
	}
	
	
	
}
