package sprint1;

import org.testng.annotations.Test;

import com.goldbharath.genericutilities.DistributorBaseClass;
import com.goldbharath.genericutilities.ExcelFileUtility;
import com.goldbharath.genericutilities.JavaUtility;
import com.goldbharath.genericutilities.PropertyFileUtility;
import com.goldbharath.genericutilities.WebDriverUtility;
import com.goldbharath.objectrepository.user.BuyGoldPage;
import com.goldbharath.objectrepository.user.DashboardPage;
import com.goldbharath.objectrepository.user.GoldBarsOrderHistoryPage;
import com.goldbharath.objectrepository.user.UploadTransactionDetailsForGoldBarsPage;
import com.goldbharath.objectrepository.user.whatWouldYouLikeToBuyPage;


public class Two_GoldBharathBuyingGoldBarsAsProprietorDistributorFullWorkFlowTest extends DistributorBaseClass
{

	WebDriverUtility wUtil = new WebDriverUtility();
	JavaUtility jUtil = new JavaUtility();
	ExcelFileUtility eUtil = new ExcelFileUtility();
	PropertyFileUtility pUtil = new PropertyFileUtility();
	
	@Test
	public void DistributorBuyingGoldBarsTest() throws Exception
	{
		final String transactionImage = System.getProperty("user.dir") + "/src/test/resources/assets/Customer_Transaction_History_Report_Example.png";
		final String transactionIdNum = eUtil.readDataFromExcel("User", 1, 0);
		int GoldWeight = 1;
		
		wUtil.waitForPageLoad(driver);
		DashboardPage dbPage = new DashboardPage(driver);
		dbPage.clickOnBuyGoldBtn(driver);
		Thread.sleep(2000);
		whatWouldYouLikeToBuyPage wPage = new whatWouldYouLikeToBuyPage(driver);
		wPage.getGoldBarsEle().click();
		Thread.sleep(2000);
		BuyGoldPage bPage = new BuyGoldPage(driver);
		bPage.buyGold(GoldWeight);
		GoldBarsOrderHistoryPage gboPage = new GoldBarsOrderHistoryPage(driver);
		gboPage.getPurchasedGoldDetails(driver);
		UploadTransactionDetailsForGoldBarsPage uPage = new UploadTransactionDetailsForGoldBarsPage(driver);
		GoldBarsOrderHistoryPage goPage = new GoldBarsOrderHistoryPage(driver);
		String OrderID = goPage.getOrderIdEle().getText();
		uPage.uploadingTransactionDetails(driver, transactionImage, transactionIdNum, OrderID);
		String OrderId = goPage.getOrderStatusAsPerOrderID(driver, OrderID);
		System.out.println(OrderId);
	}
	
	
	
}


	

