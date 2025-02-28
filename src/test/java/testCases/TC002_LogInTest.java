package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LogInPage;
import pageObjects.MyAccountPage;

public class TC002_LogInTest extends BaseTestClass{
	@Test(groups={"Sanity", "Master"})
	public void verifyLogIn()
	{
		try
		{
			logger.info("Starting loggin test");
			
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			Thread.sleep(1000);
			hp.clickLogIn();
			Thread.sleep(1000);
			
			LogInPage lp = new LogInPage(driver);
			lp.setEmail(p.getProperty("loginEmail"));
			lp.setPassword(p.getProperty("loginPassword"));
			//lp.setPassword(p.getProperty("loginWrongPassword"));
			Thread.sleep(1000);
			lp.clickLogIn();
			Thread.sleep(2000);
			
			MyAccountPage maccp = new MyAccountPage(driver);
			boolean targetPage = maccp.isMyAccountPageExist();
			Thread.sleep(1000);
			Assert.assertEquals(targetPage, true, "Login Failed");
		}
		catch (Exception e)
		{
			Assert.fail();
		}
		
		logger.info("Finished login test");
	}

}
