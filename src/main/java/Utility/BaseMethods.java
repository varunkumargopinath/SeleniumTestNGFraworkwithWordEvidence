package Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class BaseMethods extends initialization {
	public initialization init = new initialization();
	public static boolean FlagForAllTestPass = true;

	@BeforeTest
	@Parameters({ "browser", "environment" })
	public void startup(String browser, String environment, ITestContext testNamefromXMLFile) throws Exception {
		init.initProperties();
		driver = init.launchBrowser(browser);
		StartWordReader(testNamefromXMLFile.getName(), environment);
	}

	@AfterMethod
	public void tearDownMethod(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {

			FlagForAllTestPass = false;

		} else if (result.getStatus() == ITestResult.SUCCESS) {

		} else if (result.getStatus() == ITestResult.SKIP) {

		}
		// init.stop();
	}

	@AfterTest
	public void afterTest(ITestContext TestName) throws IOException {
		// Adding End time in top table in the evidence after Suite
		TestEnvDetailsTable.createRow().getCell(0).setParagraph(EnterTextParagraph("End Time", true, "000000", 11));
		TestEnvDetailsTable.getRow(5).getCell(1)
				.setParagraph(EnterTextParagraph(getCurrentISTTime(), false, "000000", 11));

		// Get Current date and time
		SimpleDateFormat sdf = new SimpleDateFormat(" dd-MM-yyyy HH-mm-ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String TodayDateTime = sdf.format(cal.getTime());

		// Make directory if not present
		new File(System.getProperty("user.dir") + "/Word Evidence/Passed/").mkdir();
		new File(System.getProperty("user.dir") + "/Word Evidence/Failed/").mkdir();

		// Save Evidence file for passed or failed
		if (FlagForAllTestPass) {
			Path passedPath = Paths.get(System.getProperty("user.dir") + "/Word Evidence/Passed/" + TestName.getName()
					+ TodayDateTime + ".docx");
			Evidence.write(new FileOutputStream(passedPath.toString()));
			Evidence.close();
		} else {
			Path passedPath = Paths.get(System.getProperty("user.dir") + "/Word Evidence/Failed/" + TestName.getName()
					+ TodayDateTime + ".docx");
			Evidence.write(new FileOutputStream(passedPath.toString()));
			Evidence.close();
		}
		softassert.assertAll();
	}

	@AfterSuite
	public void afterSuite() {
		// init.stop();
	}
}
