package utilities;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testCases.BaseTestClass;

public class ExtentReportManager implements ITestListener{
	public ExtentSparkReporter sparkReporter; // UI of the report
	public ExtentReports extent; //Populate common info on the report
	public ExtentTest test; //Creating test case entries in the report and update status of the test methods
	
	String reportName;
	
	public void onStart(ITestContext testContext) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss").format(new Date()); //time stamp
		
		
		reportName = "Test_Report_" + timeStamp + ".html";
		
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"/reports/"+reportName); //Create ExtentReport in the specified path
	    
	    sparkReporter.config().setDocumentTitle("OpenCart Automation Test Report"); //Title of the report
	    sparkReporter.config().setReportName("OpenCart Functional Testing"); //Name of the report
	    sparkReporter.config().setTheme(Theme.DARK);
	    
	    extent = new ExtentReports();
	    extent.attachReporter(sparkReporter);
	    
	    extent.setSystemInfo("Appilcation", "OpenCart");
	    extent.setSystemInfo("Environment", "QA");
	    extent.setSystemInfo("Tester Name", System.getProperty("user.name"));
	    
	    String os = testContext.getCurrentXmlTest().getParameter("os");//Get the os parameter value from testNG xml file
	    extent.setSystemInfo("Operating System",os);
	    
	    String br = testContext.getCurrentXmlTest().getParameter("browser");//Get the browser parameter value from testNG xml file
	    extent.setSystemInfo("Browser", br);
	    
	    List<String> includeGroups = testContext.getCurrentXmlTest().getIncludedGroups();//Get the included groups from testNG xml file
	    if(!includeGroups.isEmpty())
	    	extent.setSystemInfo("Groups", includeGroups.toString());
	  }
	
//	public void onTestStart(ITestResult result) {  
//	  }
	
	public void onTestSuccess(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); //Create a new empty in the report
		test.assignCategory(result.getMethod().getGroups()); //To display groups in report
	    test.log(Status.PASS, result.getName()+" - test case passed"); //Update status pass
	  }
	
	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); //Create a new empty in the report
		test.assignCategory(result.getMethod().getGroups()); //To display groups in report
		
	    test.log(Status.FAIL, result.getName()+" - test case failed"); //Update status fail
	    test.log(Status.INFO, result.getThrowable().getMessage());
	    
	    try
	    {
	    	String imgPath = new BaseTestClass().captureScreen(result.getName());
	    	test.addScreenCaptureFromPath(imgPath);
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	  }
	
	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName()); //Create a new empty in the report
		test.assignCategory(result.getMethod().getGroups()); //To display groups in report
	    test.log(Status.SKIP, result.getName()+" - test case got skipped"); //Update status skip
	    test.log(Status.INFO, result.getThrowable().getMessage());
	  }
	
	public void onFinish(ITestContext context) {
	    extent.flush();
	    
	    String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+reportName;
	    File extentReport = new File(pathOfExtentReport);
	    
	    try
	    {
	    	Desktop.getDesktop().browse(extentReport.toURI());
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    /*
	    // Send report through email
	    try
	    {
	    	@SuppressWarnings("deprecation")
			URL url = new URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+reportName);
	    	
	    	//Create the email message
	    	ImageHtmlEmail email = new ImageHtmlEmail();
	    	email.setDataSourceResolver(new DataSourceUrlResolver(url));
	    	email.setHostName("smtp.googlemail.com");
	    	email.setSmtpPort(465);
	    	email.setAuthenticator(new DefaultAuthenticator("paulcode1234@gmail.com","mail.password.1234"));
	    	email.setSSLOnConnect(true);
	    	email.setFrom("paulcode1234@gmail.com"); //Sender
	    	email.setSubject("Test Results");
	    	email.setMsg("Please find the attached report .....");
	    	email.addTo("sahilpaul.002@gmail.com"); //Receiver
	    	email.attach(url, "extent report", "please check the report");
	    	email.send();	
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	    */
	  }
}
