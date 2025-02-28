package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;

public class TC001_AccountRegistrationTest extends BaseTestClass{
	
	@Test(groups={"Regression", "Master"})
	void verufyAccountRegistration() throws InterruptedException
	{
		logger.info("Starting test");
		
		try
		{
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			logger.info("Clicked on my account link");
			Thread.sleep(2000);
			hp.clickRegister();
			logger.info("Clicked on registration link");
			Thread.sleep(2000);
			
			AccountRegistrationPage arp = new AccountRegistrationPage(driver);
			logger.info("Providing customer detials");
			arp.setFirstName(randomString().toUpperCase());
			arp.setLastName(randomString().toUpperCase());
			arp.setEmail(randomString()+"@gmail.com");
			arp.setTelephone(randomNumber());
			
			String password = randomAlphaNumeric();
			arp.setPassword(password);
			arp.setConfirmPassword(password);
			
			arp.clickPrivacyPolicy();
			arp.clickContiue();
			Thread.sleep(2000);
			
			logger.info("Validating expected message");
			String confmsg = arp.getConfirmationMsg();
			Assert.assertEquals(confmsg, "Your Account Has Been Created!");
			Thread.sleep(2000);
		}
		catch (Exception e)
		{
			logger.error("Test failed");
			Assert.fail();
		}
		
		logger.info("Finished test case");
	}
}
