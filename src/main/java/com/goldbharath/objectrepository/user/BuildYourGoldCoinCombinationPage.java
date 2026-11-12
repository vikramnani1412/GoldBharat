package com.goldbharath.objectrepository.user;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.goldbharath.genericutilities.WebDriverUtility;

public class BuildYourGoldCoinCombinationPage {

	//Finding WebElements Using @FindBy Annotations

	@FindBy(xpath="//span[.=' Current weight ']/following-sibling::p[contains(text(),' g')]")private WebElement CurrentWeightTxt;
	
    @FindBy(xpath="//img[@alt='Gold Coin']/following-sibling::span[contains(.,'1 g')]")private WebElement GramsText;
    
    @FindBy(xpath="//img[@alt='Gold Coin']/following-sibling::span[contains(.,'1 g')]/../following-sibling::div/button[.=' - ']")private WebElement GramsDecreaseBtn;
    
    @FindBy(xpath="//img[@alt='Gold Coin']/following-sibling::span[contains(.,'1 g')]/../following-sibling::div//button[.=' + ']")private WebElement GramsIncreaseBtn;
    
    @FindBy(xpath="//div[@class='selection-card']")private WebElement TotalSelectedWeightsTxt;
  
    @FindBy(xpath="//button[.=' Continue to review ']")private WebElement ContinueBtn;  
    
    
	//Rule-2:Create a constructor to initilise these elements
    
	public BuildYourGoldCoinCombinationPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	//Rule-3:Provide getters to access these variables
	
	public WebElement getCurrentWeightTxt() {
		return CurrentWeightTxt;
	}

	public WebElement getGramsText() {
		return GramsText;
	}

	public WebElement getGramsDecreaseBtn() {
		return GramsDecreaseBtn;
	}

	public WebElement getGramsIncreaseBtn() {
		return GramsIncreaseBtn;
	}

	public WebElement getTotalSelectedWeightsTxt() {
		return TotalSelectedWeightsTxt;
	}

	public WebElement getContinueBtn() {
		return ContinueBtn;
	}

	// Business Library
	
//	public void increaseGoldGramsAccToWeight(WebDriver driver, String gram, int quantity) {
//
//	    if (driver == null) {
//	        throw new IllegalArgumentException("WebDriver cannot be null.");
//	    }
//
//	    if (gram == null || gram.trim().isEmpty()) {
//	        throw new IllegalArgumentException("Gold gram value cannot be empty.");
//	    }
//
//	    if (quantity <= 0) {
//	        throw new IllegalArgumentException("Quantity must be greater than zero.");
//	    }
//
//	    String goldGram = gram.trim();
//
//	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//
//	    By plusButton = By.xpath(
//	        "//img[@alt='Gold Coin']" +
//	        "/following-sibling::span[normalize-space(.)='" + goldGram + " g']" +
//	        "/../following-sibling::div//button[normalize-space()='+']"
//	    );
//
//	    for (int i = 1; i <= quantity; i++) {
//
//	        WebElement button = wait.until(
//	            ExpectedConditions.elementToBeClickable(plusButton)
//	        );
//
//	        button.click();
//
//	        System.out.println(
//	            "Added " + goldGram + "g Gold Coin: " + i + "/" + quantity
//	        );
//	    }
//	}
	
	
//	public void increaseGoldGramsAccToWeight(WebDriver driver, String gram, int quantity) throws Exception 
//	{
//		Thread.sleep(1000);
//	    for (int i = 0; i < quantity; i++) 
//	    {
//	        driver.findElement(By.xpath("//img[@alt='Gold Coin']/following-sibling::span[contains(.,'" + gram + " g')]/../following-sibling::div//button[normalize-space()='+']")).click();
//	        Thread.sleep(2000);
//	    }
//	}
	
	
	public void increaseGoldGramsAccToWeight(WebDriver driver, String gram, int quantity) {
	    if (driver == null) throw new IllegalArgumentException("WebDriver cannot be null.");
	    if (gram == null || gram.trim().isEmpty()) throw new IllegalArgumentException("Gold gram value cannot be empty.");
	    if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    By plusButton = By.xpath("//div[contains(@class,'coin-card')][.//div[contains(@class,'coin-size')]//span[normalize-space()='" + gram.trim() + " g']]//div[contains(@class,'qty')]/button[normalize-space()='+']");

	    for (int i = 1; i <= quantity; i++) {
	        wait.until(ExpectedConditions.elementToBeClickable(plusButton)).click();
	        System.out.println("Added " + gram.trim() + "g Gold Coin: " + i + "/" + quantity);
	    }
	}
	
	
	public void decreaseGoldGramsAccToWeight(WebDriver driver, String gram, int quantity) throws Exception 
	{
		Thread.sleep(2000);
	    for (int i = 0; i < quantity; i++) 
	    {
	        driver.findElement(By.xpath("//img[@alt='Gold Coin']/following-sibling::span[contains(.,'" + gram + " g')]/../following-sibling::div//button[normalize-space()='-']")).click();
	        Thread.sleep(2000);
	    }
	}
	
	
	
}
