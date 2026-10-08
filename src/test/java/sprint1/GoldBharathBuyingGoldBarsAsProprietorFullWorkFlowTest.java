package sprint1;

import org.testng.annotations.Test;

import com.goldbharath.genericutilities.DistributorBaseClass;
import com.goldbharath.genericutilities.ExcelFileUtility;
import com.goldbharath.genericutilities.JavaUtility;
import com.goldbharath.genericutilities.PropertyFileUtility;
import com.goldbharath.genericutilities.WebDriverUtility;
import com.goldbharath.objectrepository.user.BuyGoldPage;
import com.goldbharath.objectrepository.user.DashboardPage;
import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;


public class GoldBharathBuyingGoldBarsAsProprietorFullWorkFlowTest extends DistributorBaseClass {

	WebDriverUtility wUtil = new WebDriverUtility();
	JavaUtility jUtil = new JavaUtility();
	ExcelFileUtility eUtil = new ExcelFileUtility();
	PropertyFileUtility pUtil = new PropertyFileUtility();
	
	@Test
	public void DistributorBuyingGoldBarsTest() throws Exception
	{
		int GoldWeight = 2;
		
		wUtil.waitForPageLoad(driver);
		Thread.sleep(2000);
		DashboardPage dbPage = new DashboardPage(driver);
		dbPage.clickOnBuyGoldBtn(driver);
		Thread.sleep(2000);
		whatWouldYouLikeToBuyPage wPage = new whatWouldYouLikeToBuyPage(driver);
		wPage.getGoldBarsEle().click();
		Thread.sleep(2000);
		BuyGoldPage bPage = new BuyGoldPage(driver);
		bPage.buyGold(GoldWeight);
		
	}
}


	

