package sprint1;

import org.testng.annotations.Test;

import com.goldbharath.genericutilities.DistributorBaseClass;
import com.goldbharath.genericutilities.WebDriverUtility;
import com.goldbharath.objectrepository.user.DashboardPage;

public class Two_DistributorCheckingOrderDispatchedDetailsTest
        extends DistributorBaseClass {

	WebDriverUtility wUtil = new WebDriverUtility();
	
	@Test(dependsOnMethods = {"sprint1.Two_AdminConfirmingFullWorkFlowTest.adminConfirmingPaymentAndVerifyingOrder"})
    public void checkingDispatchDetails() throws Exception {

		DashboardPage dPage = new DashboardPage(driver);
        dPage.getNotificationsIcon().click();
		
        wUtil.waitUntilPageLoad(driver);
        
		String orderId =
                Two_GoldBharathBuyingGoldBarsAsProprietorDistributorFullWorkFlowTest.OrderId;
        Thread.sleep(2000);
        
        dPage.checkingDispatchNotificationBasedOnOrderID(driver, orderId);
    }
}