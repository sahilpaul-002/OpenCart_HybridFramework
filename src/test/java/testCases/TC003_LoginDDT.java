package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LogInPage;
import pageObjects.MyAccountPage;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseTestClass{
	//If the dataProvider  is present in different package then need to import the class 
	@Test(dataProvider = "LoginCredentials", dataProviderClass = DataProviders.class, groups="Datadriven")
	public void verifyLogInDDT(String email, String pwd, String exp) throws InterruptedException
	{
		logger.info("Starting logginDDT test");
		
		try
		{
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			Thread.sleep(1000);
			hp.clickLogIn();
			Thread.sleep(1000);
			
			LogInPage lp = new LogInPage(driver);
			lp.setEmail(email);
			lp.setPassword(pwd);
			Thread.sleep(1000);
			lp.clickLogIn();
			Thread.sleep(2000);
			
			MyAccountPage maccp = new MyAccountPage(driver);
			boolean targetPage = maccp.isMyAccountPageExist();
			Thread.sleep(1000);
			
			if(exp.equalsIgnoreCase("Valid"))
			{
				if(targetPage == true)
				{
					maccp.clickLogOut();
					Assert.assertTrue(true);
				}
				else
				{
					Assert.fail();
				}
			}
			else if(exp.equalsIgnoreCase("Invalid"))
			{
				if(targetPage == true)
				{
					maccp.clickLogOut();
					Assert.fail();
				}
				else
				{
					Assert.assertTrue(true);
				}
			}
		}
		catch (Exception e)
		{
			Assert.fail();
		}
		
		logger.info("Finished loginDDT test");
	}
}
