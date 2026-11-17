package com.goldbharath.objectrepository.user;

import java.time.Duration;
import java.util.List;

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

public class KycAsLtdPage {

	//Finding WebElements Using @FindBy Annotations

    // Not an exact @class match (Angular may add ng-* classes). Also matches any <select> that has the LTD option.
    private static final String TYPE_DRPDWN_XPATH =
            "(//select[contains(@class,'my-select')] | //select[option[@value='LTD']])[1]";

    @FindBy(xpath=TYPE_DRPDWN_XPATH)private WebElement TypeDrpDwn;
    
    @FindBy(xpath="//option[@value='LTD']")private WebElement SelectLtd;
    
    @FindBy(xpath="//input[@formcontrolname='director_nameone']")private WebElement DirectorNameEdt;
    
    @FindBy(xpath="//input[@formcontrolname='director_mobile']")private WebElement DirectorMobileNumberEdt;
    
    @FindBy(xpath="//input[@formcontrolname='director_aadharone']")private WebElement DirectorAadharNumberEdt;
    
    @FindBy(xpath="//label[.='Upload Aadhar Back Side(First Director)']/preceding-sibling::div/input[@type='file']")private WebElement UploadAadharFrontSideImage;
    
    @FindBy(xpath="//label[normalize-space()='Compony PAN Number(First Director)']/preceding-sibling::div[1]/input[@type='file']")private WebElement UploadAadharBackSideImage;
    
    @FindBy(xpath="//input[@placeholder='Enter PAN Number']")private WebElement PanNumberEdt;
    
    @FindBy(xpath="//label[normalize-space()='Upload PAN Card(First Director)']/following-sibling::div[1]//input[@type='file']")private WebElement UploadPanImage;
    
    @FindBy(xpath="//input[@formcontrolname='director_nametwo']")private WebElement SecondDirectorNameEdt;
    
    @FindBy(xpath="//input[@placeholder='Enter GST Number']")private WebElement EnterGSTNumberEdt;
    
    @FindBy(xpath="//label[normalize-space()='Upload GST']/following-sibling::div[1]/input[@type='file']")private WebElement UploadGstImage;
    
    @FindBy(xpath="//input[@placeholder='Enter CIN Number']")private WebElement CompanyCinEdt;
    
    @FindBy(xpath="//label[normalize-space()='Upload CIN']/following-sibling::div[1]//input[@type='file']")private WebElement UploadCINimage;
    
    @FindBy(xpath="//label[normalize-space()='Upload Your Image']/following-sibling::div[1]//input[@type='file']")private WebElement UploadYourImage;
    
    @FindBy(xpath="//input[@placeholder='Enter Bank Name']")private WebElement EnterBankNameEdt;
    
    @FindBy(xpath="//input[@placeholder='Enter A/C Number']")private WebElement EnterAcNumberEdt;
    
    @FindBy(xpath="//input[@placeholder='Enter IFSC Code']")private WebElement EnterIFSCCodeEdt;
    
    @FindBy(xpath="//label[.='Account Holder Name']/following-sibling::div/input[@placeholder='Enter Name']")private WebElement AccountHolderNameEdt;
    
    @FindBy(xpath="//button[.='Submit']")private WebElement SubmitBtn;
    
    @FindBy(xpath="//a[.='Click Here']")private WebElement ClickHereLnk;
    
	//Rule-2:Create a constructor to initilise these elements
    
	public KycAsLtdPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	
	//Rule-3:Provide getters to access these variables
	

	public WebElement getTypeDrpDwn() {
		return TypeDrpDwn;
	}

	public WebElement getSelectLtd() {
		return SelectLtd;
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

	public WebElement getSecondDirectorNameEdt() {
		return SecondDirectorNameEdt;
	}

	public WebElement getEnterGSTNumberEdt() {
		return EnterGSTNumberEdt;
	}

	public WebElement getUploadGstImage() {
		return UploadGstImage;
	}

	public WebElement getCompanyCinEdt() {
		return CompanyCinEdt;
	}

	public WebElement getUploadCINimage() {
		return UploadCINimage;
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

	// Waits for the KYC form, then selects LTD in the business-type dropdown.
	private void selectLtdType(WebDriver driver) throws Exception
	{
		try {
			new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TYPE_DRPDWN_XPATH)));
		} catch (TimeoutException e) {
			throw new NoSuchElementException("KYC business-type dropdown not found. URL: " + driver.getCurrentUrl()
					+ " | Page text: " + pageText(driver), e);
		}
		try {
			new Select(TypeDrpDwn).selectByValue("LTD");
		} catch (Exception e) {
			TypeDrpDwn.click();
			Thread.sleep(2000);
			SelectLtd.click();
		}
		Thread.sleep(2000);
	}

	private String pageText(WebDriver driver)
	{
		try {
			String t = driver.findElement(By.tagName("body")).getText().replaceAll("\\s+", " ");
			return t.length() > 300 ? t.substring(0, 300) : t;
		} catch (Exception e) {
			return "";
		}
	}

	// Fails right here (instead of three tests later) if the form is still on screen after Submit.
	private void verifySubmitted(WebDriver driver) throws Exception
	{
		for (int i = 0; i < 10; i++) {
			boolean formStillThere = false;
			try {
				formStillThere = !driver.findElements(By.xpath("//input[@formcontrolname='director_nameone']")).isEmpty()
						&& !driver.findElements(By.xpath("//button[.='Submit']")).isEmpty();
			} catch (Exception ignored) { }
			if (!formStillThere) {
				return;
			}
			Thread.sleep(1000);
		}
		StringBuilder invalid = new StringBuilder();
		List<WebElement> bad = driver.findElements(By.xpath(
				"//*[@formcontrolname and contains(@class,'ng-invalid')]"));
		for (WebElement e : bad) {
			invalid.append(e.getAttribute("formcontrolname")).append(", ");
		}
		throw new IllegalStateException("Ltd KYC form was NOT accepted after Submit. Invalid fields: ["
				+ invalid + "] | URL: " + driver.getCurrentUrl() + " | Page text: " + pageText(driver));
	}
	
	public void LtdKyc(WebDriver driver, String Name, String MobileNo, String AadharNo, String PanNo, String SecondDirectorName, String GstNo, String CIN, String BankName, String AccountNo, String IfscCode, String AcHolderName) throws Exception
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		String PanimagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/PAN_Card.jpeg";
		String AadharImagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/Aadhaar_Card.jpeg";
		
		
		Thread.sleep(2000);
		selectLtdType(driver);
		Thread.sleep(2000);
		DirectorNameEdt.sendKeys(Name);
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
		SecondDirectorNameEdt.sendKeys(SecondDirectorName);
		Thread.sleep(2000);
		EnterGSTNumberEdt.sendKeys(GstNo);
		Thread.sleep(2000);
		UploadGstImage.sendKeys(PanimagePath);
		Thread.sleep(2000);
		CompanyCinEdt.sendKeys(CIN);
		Thread.sleep(2000);
		UploadCINimage.sendKeys(PanimagePath);
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
	    verifySubmitted(driver);
	    
//	    SubmitBtn.click();
	}
	
	public void LtdKycAfterRejected(WebDriver driver, String Name, String MobileNo, String AadharNo, String PanNo, String SecondDirectorName, String GstNo, String CIN, String BankName, String AccountNo, String IfscCode, String AcHolderName) throws Exception
	{
		WebDriverUtility wUtil = new WebDriverUtility();
		String PanimagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/PAN_Card.jpeg";
		String AadharImagePath = System.getProperty("user.dir")+ "/src/test/resources/assets/Aadhaar_Card.jpeg";
		
		Thread.sleep(2000);
		ClickHereLnk.click();
		Thread.sleep(2000);
		selectLtdType(driver);
		Thread.sleep(2000);
		DirectorNameEdt.sendKeys(Name);
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
		SecondDirectorNameEdt.sendKeys(SecondDirectorName);
		Thread.sleep(2000);
		EnterGSTNumberEdt.sendKeys(GstNo);
		Thread.sleep(2000);
		UploadGstImage.sendKeys(PanimagePath);
		Thread.sleep(2000);
		CompanyCinEdt.sendKeys(CIN);
		Thread.sleep(2000);
		UploadCINimage.sendKeys(PanimagePath);
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
	    verifySubmitted(driver);
	    
//	    SubmitBtn.click();
	}
	
	
		
	
}