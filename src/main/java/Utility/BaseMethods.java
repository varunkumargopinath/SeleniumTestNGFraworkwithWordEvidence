package Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class BaseMethods extends initialization {
	public initialization init = new initialization();
	public static boolean CsvPrinterTrigger = false;
	public static boolean FlagForAllTestPass = true;
	public static String browser="chrome";
	
	@BeforeTest
	public void startup() throws Exception {
		init.initProperties();
		init.reports();
		init.start(browser);
		StartWordReader();
		//String username = System.getProperty("uname");
		//String password = System.getProperty("pass");
		//login(username, password);
	}

	@AfterTest
	public void tearDown() throws IOException {
		initialization.report.flush();
		if (CsvPrinterTrigger)
			csvPrinter.flush();
		//init.stop();
	}

	@AfterMethod
	public void tearDownMethod(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(Status.FAIL, "Test Fail",
					MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(driver)).build());
			initialization.report.flush();
			FlagForAllTestPass = false;
		
		

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS, "Test Pass",
					MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(driver)).build());
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP, "This Test Cases got skipped Retryinng.... ",
					MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(driver)).build());
		}
		//init.stop();
	}

	@AfterSuite
	public void afterSuite(ITestContext context) throws IOException {
		System.out.println(context.getSuite().getXmlSuite().getFileName());
		if (FlagForAllTestPass) {
			File file = new File(context.getSuite().getXmlSuite().getFileName());
			System.out.println(file.getName());
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			String TodayDate = sdf.format(cal.getTime());

			File f = new File(System.getProperty("user.dir") + "/Saved Reports/" + TodayDate);
			if (f.mkdir() == true)
				System.out.println("Created Directory for Todays Date: " + TodayDate);

			File HtmlReportFile = new File(System.getProperty("user.dir") + "/Html Reports/extent.html");
			Path DestinationPath = Paths.get(System.getProperty("user.dir") + "/Saved Reports/" + TodayDate + "/"
					+ file.getName().substring(0, file.getName().length() - 4) + ".html");
			File DestinationCopy = new File(DestinationPath.toString());
			int i = 1;
			while (true) {

				StringBuilder str = new StringBuilder(DestinationPath.getFileName().toString());
				str.insert(str.length() - 5, "(" + i + ")");
				File temp = new File(DestinationPath.getParent().toFile(), str.toString());
				if (!DestinationCopy.exists()) {
					FileUtils.copyFile(HtmlReportFile, DestinationCopy);
					break;
				}
				DestinationCopy = temp;
				i++;
			}
		}
		
		
		//word evidence write
		TestEnvDetailsTable.createRow().getCell(0).setParagraph(EnterTextParagraph("End Time", true, "000000", 11));
		TestEnvDetailsTable.getRow(5).getCell(1)
				.setParagraph(EnterTextParagraph(getCurrentISTTime(), false, "000000", 11));
		
		Evidence.write(new FileOutputStream("Evidence.docx"));
		Evidence.close();
	}

	public void login(String username, String password) throws IOException, InterruptedException {

		// base64 used to decode the password
		Base64 decoder = new Base64();
		byte[] decodedBytes = decoder.decode(password);
		password = new String(decodedBytes);

		sendText("usernameText", username);
		sendText("passwordText", password);
		clickOnElement("loginBtn");
		clickOnElement("HomePageBtn");
	}

	public void logout() throws IOException, InterruptedException {
		Thread.sleep(Integer.valueOf(data.getProperty("THREE_SECOND")));
		doubleClickOnElement(creatLocator("profileBtn"));
		clickOnElement("logout");
		clickOnElement("logout_confirmBtn");
	}

	public void StartReportReader(String mtdName) {
		logger = initialization.report.createTest(mtdName);
		logger.info(mtdName + " - Validation started");
	}

	public void EndReportReader(String mtdName) {
		logger.info(mtdName + " - Validation Ends");
	}

}
