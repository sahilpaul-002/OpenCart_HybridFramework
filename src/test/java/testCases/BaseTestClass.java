package testCases;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

public class BaseTestClass {
	public Properties p;
	public static WebDriver driver;
	public Logger logger;
	
	@SuppressWarnings("deprecation")
	@Parameters({"os", "browser"}) //Get system configuration for testNG file
	@BeforeClass(groups= {"Sanity", "Regression", "Master", "DataDriven"})
	void setup(String os, String br) throws InterruptedException, IOException
	{
		//Loading config.properties file
		FileReader file = new FileReader("./src/test/resources/config.properties");
		p = new Properties();
		p.load(file);
		
		// logger will dynamically get the test class during test execution with the help of this.getClass()
		logger = LogManager.getLogger(this.getClass());
		
		//Get the execution environment value for config file
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			//Get os value form testNG xml file and set the os
			if(os.equalsIgnoreCase("windows"))
			{
				capabilities.setPlatform(Platform.WIN10);
			}
			else if (os.equalsIgnoreCase("mac"))
			{
				capabilities.setPlatform(Platform.MAC);
			}
			else if (os.equalsIgnoreCase("linux"))
			{
				capabilities.setPlatform(Platform.LINUX);
			}
			else
			{
				System.out.println("No match for this Operating System");
				return;
			}
			
			//Get browser value form testNG xml file and set the browser
			switch(br.toLowerCase())
			{
			case "chrome" :
				capabilities.setBrowserName("chrome");
				break;
			case "edge" : 
				capabilities.setBrowserName("MicrosoftEdge");
				break;
			case "firefox" :
				capabilities.setBrowserName("firefox");
				break;
			default :
				System.out.println("Invalid browser name.");
				return;
			}
			
			//Create the driver instance
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
		}
		
		//Get the execution environment value for config file
		if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{
			//Selecting browser and running the test cases on local system
			switch(br.toLowerCase())
			{
			case "chrome" :
				driver = new ChromeDriver();
				break;
			case "edge" : 
				driver = new EdgeDriver();
				break;
			case "firefox" :
				driver = new FirefoxDriver();
				break;
			default :
				System.out.println("Invalid browser name.");
				return;
			}
		}
		
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		driver.manage().deleteAllCookies();
		
		driver.get(p.getProperty("appURL"));
		//driver.get("https://tutorialsninja.com/demo/index.php?route=common/home");
		driver.manage().window().maximize();
		Thread.sleep(3000);
	}
	
	@AfterClass(groups= {"Sanity", "Regression", "Master", "DataDriven"})
	void tearDown()
	{
		driver.quit();
	}
	
	public String randomString() 
	{
		@SuppressWarnings("deprecation")
		String generatedString = RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
	
	public String randomNumber() 
	{
		@SuppressWarnings("deprecation")
		String generatedNumber = RandomStringUtils.randomNumeric(10);
		return generatedNumber;
	}
	
	public String randomAlphaNumeric()
	{
		@SuppressWarnings("deprecation")
		String generatedAlpha = RandomStringUtils.randomAlphabetic(3);
		@SuppressWarnings("deprecation")
		String generatedNumber = RandomStringUtils.randomNumeric(10);
		String generatedString = generatedAlpha+generatedNumber;
		return generatedString;
	}
	
	public String captureScreen(String tName)
	{
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new Date());
		
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath = System.getProperty("user.dir")+"\\screenshots\\"+ tName + "_" + timeStamp + ".png";
		File targetFile = new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
		
		return targetFilePath;
	}
}
