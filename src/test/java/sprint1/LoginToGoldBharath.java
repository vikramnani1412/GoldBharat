package sprint1;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.goldbharath.objectrepository.user.LoginPage;
import com.goldbharath.objectrepository.user.WelcomePage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginToGoldBharath {

	@Test
	public void login() throws Exception
	{
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		driver.get("http://stg.goldbharat.com/");
		Thread.sleep(2000);
		WelcomePage wPage = new WelcomePage(driver);
		wPage.clickOnLoginLink();
		
		LoginPage lpage = new LoginPage(driver);
		lpage.LoginToApplication(driver, "9999999999");
	}
	
}
