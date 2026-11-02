package Goldbharat;

import org.testng.annotations.Test;
import com.goldbharath.genericutilities.DistributorBaseClass;
import com.goldbharath.objectrepository.user.BookSilverPage;
import com.goldbharath.objectrepository.user.DashboardPage;

public class Seven_BookSilver extends DistributorBaseClass {

	@Test
	public void bookSilver() throws Exception
	{
		String Quantity = "500";
		
		Thread.sleep(2000);
		DashboardPage dPage = new DashboardPage(driver);
		dPage.getBookSilverBtn().click();
		Thread.sleep(2000);
		
		BookSilverPage bsPage = new BookSilverPage(driver);
		bsPage.fetchingSilverLivePriceAndBookingSilver(driver, Quantity);
		Thread.sleep(2000);
		
		dPage.getUserProfileImg().click();
		
	}
	
}
