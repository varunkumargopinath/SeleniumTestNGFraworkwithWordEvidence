package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class initialization extends WordEvidenceBaseMethods {

	public static WebDriver driver;
	public String username, password = "";
	public static String browser;
	public static Properties locatorString, data;
	public static WebDriverWait wait;
	public JavascriptExecutor js;
	public Actions actions;
	public static ExtentReports report;
	public static ExtentTest logger;
	public static String recordList[];
	public static CSVParser csvread = null;
	public static CSVPrinter csvPrinter = null;

	public void reports() throws Exception {

		File file = new File(System.getProperty("user.dir") + "/Html Reports/Extent.html");
		new File(System.getProperty("user.dir") + "/Html Reports").mkdir();
		ExtentSparkReporter extent = new ExtentSparkReporter(file);
		System.out.println(System.getProperty("user.dir"));
		report = new ExtentReports();
		report.attachReporter(extent);
		extent.config().setDocumentTitle("QE Team");

	}

	public static String getScreenshot(WebDriver driver) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-ms");
		String path = "Screenshot/" + sdf.format(new Date()) + ".png";
		new File(System.getProperty("user.dir") + "/Html Reports/Screenshot").mkdir();
		File destination = new File(
				System.getProperty("user.dir") + "/Html Reports/Screenshot/" + sdf.format(new Date()) + ".png");
		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			logger.log(Status.FAIL, "Screen Shot Capture Failed: " + e.getMessage());
			System.out.println("Capture Failed " + e.getMessage());
		}
		return path;
	}

	public void initProperties() throws Exception {

		File locFile = new File(System.getProperty("user.dir") + "/locators.properties");
		File datafile = new File(System.getProperty("user.dir") + "/data.properties");
		FileInputStream locInput = null;
		FileInputStream dataInput = null;
		try {
			locInput = new FileInputStream(locFile);
			dataInput = new FileInputStream(datafile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		locatorString = new Properties();
		data = new Properties();
		try {
			locatorString.load(locInput);
			data.load(dataInput);

		} catch (IOException e) {
			e.printStackTrace();
		}
		locInput.close();
		dataInput.close();
	}

	public WebDriver launchBrowser(String browser) throws InterruptedException, IOException {
		this.browser=browser;
		switch (browser) {

		case "firefox":
			System.setProperty("webdriver.gecko.driver", ".\\geckodriver.exe");
			driver = new FirefoxDriver();
			break;

		case "chrome":
			//System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			options.addArguments("--incognito");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--ignore-ssl-errors=yes");
			options.addArguments("--ignore-certificate-errors");
			//options.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(options);
			break;

		case "edge":
			System.setProperty("webdriver.edge.driver", ".\\msedgedriver.exe");
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments("-inprivate");
			edgeOptions.addArguments("--start-maximized");
			// edgeOptions.addArguments("headless");
			edgeOptions.addArguments("disable-gpu");
			edgeOptions.addArguments("--remote-allow-origins=*");
			driver = new EdgeDriver(edgeOptions);
			break;
		}

		String env = System.getProperty("env");
		env="https://demowebshop.tricentis.com/";
		System.out.println(env);
		driver.get(env);

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;
	}

	public boolean waitForJSandJQueryToLoad() {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) (((JavascriptExecutor) driver)).executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					// no jQuery present
					return true;
				}
			}
		};

		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return (((JavascriptExecutor) driver)).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};

		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

	public WebElement creatLocator(String label) throws IOException {
		WebElement ele = null;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		js = (JavascriptExecutor) driver;
		try {
			String labelText = locatorString.getProperty(label);
			String labelTextArray[] = labelText.split("~");
			String method = labelTextArray[0];
			String loc = labelTextArray[1];

			switch (method) {

			case "id":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.id(loc)));
				ele = driver.findElement(By.id(loc));
				break;

			case "xpath":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(loc)));
				ele = driver.findElement(By.xpath(loc));
				break;

			case "linktext":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.linkText(loc)));
				ele = driver.findElement(By.linkText(loc));
				break;

			case "partiallinktext":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(loc)));
				ele = driver.findElement(By.partialLinkText(loc));
				break;

			case "css":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(loc)));
				ele = driver.findElement(By.cssSelector(loc));
				break;
			case "classname":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.className(loc)));
				ele = driver.findElement(By.className(loc));
				break;
			case "name":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.name(loc)));
				ele = driver.findElement(By.name(loc));
				break;
			case "tagname":
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(loc)));
				wait.until(ExpectedConditions.elementToBeClickable(By.tagName(loc)));
				ele = driver.findElement(By.tagName(loc));
				break;
			default:
				System.out.println("Invalid locator type");
				break;

			}

		} catch (Exception Ex) {
			System.out.println("*****************************************");
			System.out.println("Webelement creation failed for: " + label);
			Ex.printStackTrace();
			System.out.println("*****************************************");
			TakeScreenShot("Fail", "WebElement capture failed for " + label);
			Assert.fail("Webelement creation failed for: " + label);
		}
		return ele;
	}

	public WebDriver start(String browser) throws Exception {
		return launchBrowser(browser);
	}

	public void stop() {
		driver.quit();
	}

	public void clickOnElement(String label) {
		try {
			WebElement ele = creatLocator(label);
			wait.until(ExpectedConditions.visibilityOf(ele));
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", ele);
			EnterTestStepDescriptionWithScreenshotForInfo(new String[]{"Clicked on \""+label+"\""});
			//js.executeScript("arguments[0].style.backgroundColor = 'yellow';",ele);
			js.executeScript("arguments[0].click();", ele);
			//ele.click();
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Status.FAIL, e.getMessage());
			Assert.fail(e.getMessage());
		}
		
	}

	public void doubleClickOnElement(WebElement ele) throws InterruptedException, IOException {
		actions = new Actions(driver);
		actions.doubleClick(ele).build().perform();
		Thread.sleep(Integer.valueOf(data.getProperty("ONE_SECOND")));

	}

	public void selectbyDropDown(String locator, String text) throws InterruptedException, IOException {
		WebElement ele = creatLocator(locator);
		Select sel = new Select(ele);
		sel.selectByVisibleText(text);

	}

	public void sendText(String locator, String text) {
		try {
			WebElement ele = creatLocator(locator);
			wait.until(ExpectedConditions.visibilityOf(ele));
			ele.sendKeys(text);
			js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", ele);
			EnterTestStepDescriptionWithScreenshotForInfo(new String[]{"Enter \""+text+"\" in the \""+locator+"\" Field"});
			
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public Object[][] readDataFromExcel(String sheetName) {
		String path = System.getProperty("user.dir") + "/src/main/resources/DataInputAndOutput.xlsx";
		File file = new File(path);
		FileInputStream inputStream = null;
		Workbook wb = null;
		Object[][] Data = null;
		try {
			inputStream = new FileInputStream(file);
			wb = new XSSFWorkbook(inputStream);
			if (wb.getSheetIndex(sheetName) == -1)
				throw new IllegalArgumentException(sheetName + " Sheet not found");

			Sheet sh = wb.getSheet(sheetName);
			Row row = sh.getRow(0);
			Cell cell = null;

			int RowNum = sh.getLastRowNum();// count my number of Rows

			int ColNum = row.getLastCellNum(); // get last ColNum
			System.out.println("Rows = " + RowNum + "  Columns = " + ColNum);
			Data = new Object[RowNum][ColNum]; // pass my count data in array

			for (int i = 0; i < RowNum; i++) // Loop work for Rows
			{
				row = sh.getRow(i + 1);
				for (int j = 0; j < ColNum; j++) // Loop work for colNum
				{
					if (row == null)
						Data[i][j] = "";
					else {
						cell = row.getCell(j);
						if (cell == null)
							Data[i][j] = ""; // if it get Null value it pass no data
						else {
							cell.setCellType(CellType.STRING);
							Data[i][j] = cell.getStringCellValue();
							System.out.print(Data[i][j] + "\t");
						}
					}
				}
				System.out.println();
			}
			inputStream.close();
			return Data;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			logger.log(Status.FAIL, e.getMessage());
			Assert.fail(e.getMessage());
		}
		return Data;
	}

	public Object[][] readDataFromCSV(String fileName) {

		Object Data[][] = null;
		try {
			// To read CSV
			Reader reader = Files.newBufferedReader(Paths.get(".//src/main/resources/" + fileName));
			csvread = new CSVParser(reader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			int NoOfRows = new CSVParser(Files.newBufferedReader(Paths.get(".//src/main/resources/" + fileName)),
					CSVFormat.DEFAULT.withFirstRecordAsHeader()).getRecords().size();
			int NoOfColumns = csvread.getHeaderNames().size();
			Data = new Object[NoOfRows][NoOfColumns];
			System.out.println("Rows: " + NoOfRows + "\nColumns: " + NoOfColumns);

			for (CSVRecord csvRecord : csvread) {
				Data[(int) (csvRecord.getRecordNumber()) - 1] = csvRecord.toList().toArray();
				System.out.println(csvRecord.toList());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Status.FAIL, e.getMessage());
			Assert.fail(e.getMessage());
		}
		return Data;
	}

	public void WritetoExcelUsingSheetnameAndColumn(String sheetName, int RowNo, int ColumnNo, String Data) {

		File file = new File(System.getProperty("user.dir") + "/src/main/resources/DataInputAndOutput.xlsx");
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);

			try (Workbook wb = new XSSFWorkbook(inputStream)) {
				if (wb.getSheetIndex(sheetName) == -1) {
					throw new IllegalArgumentException(sheetName + " sheet not found");
				}
				Sheet sh = wb.getSheet(sheetName);
				Row row = sh.getRow(RowNo - 1);
				if (row == null) {
					row = sh.createRow(RowNo - 1);
				}
				Cell cell = row.getCell(ColumnNo - 1);
				if (cell == null)
					cell = row.createCell(ColumnNo - 1);
				cell.setCellValue(Data);

				FileOutputStream fos = new FileOutputStream(
						System.getProperty("user.dir") + "/src/main/resources/DataInputAndOutput.xlsx");
				wb.write(fos);
				inputStream.close();
				fos.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			logger.log(Status.FAIL, e.getMessage());
			Assert.fail(e.getMessage());
		}

	}

	public String ReadFromExcelUsingSheetnameAndColumn(String sheetName, int RowNo, int ColumnNo) {

		File file = new File(System.getProperty("user.dir") + "/src/main/resources/DataInputAndOutput.xlsx");
		FileInputStream inputStream;
		String value = "";
		try {
			inputStream = new FileInputStream(file);

			try (Workbook wb = new XSSFWorkbook(inputStream)) {
				if (wb.getSheetIndex(sheetName) == -1) {
					throw new IllegalArgumentException(sheetName + " sheet not found");
				}
				Sheet sh = wb.getSheet(sheetName);
				Row row = sh.getRow(RowNo - 1);
				if (row == null)
					throw new IllegalArgumentException(sheetName + " Row not found");

				Cell cell = row.getCell(ColumnNo - 1);

				if (cell == null)
					throw new IllegalArgumentException(sheetName + " Column not found");

				cell.setCellType(CellType.STRING);
				value = cell.getStringCellValue();
				inputStream.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			logger.log(Status.FAIL, e.getMessage());
			Assert.fail(e.getMessage());
		}
		return value;

	}

	public void ScrollToWebElement(String label) throws IOException {
		WebElement ele = null;
		try {
			String labelText = locatorString.getProperty(label);
			String labelTextArray[] = labelText.split("~");
			String method = labelTextArray[0];
			String loc = labelTextArray[1];

			switch (method) {

			case "id":
				ele = driver.findElement(By.id(loc));
				break;
			case "xpath":
				ele = driver.findElement(By.xpath(loc));
				break;
			case "linktext":
				ele = driver.findElement(By.linkText(loc));
				break;
			case "partiallinktext":
				ele = driver.findElement(By.partialLinkText(loc));
				break;
			case "css":
				ele = driver.findElement(By.cssSelector(loc));
				break;
			case "classname":
				ele = driver.findElement(By.className(loc));
				break;
			case "name":
				ele = driver.findElement(By.name(loc));
				break;
			case "tagname":
				ele = driver.findElement(By.tagName(loc));
				break;
			default:
				System.out.println("Invalid locator type");
				break;
			}

			js.executeScript("arguments[0].scrollIntoView();", ele);
			Thread.sleep(Integer.valueOf(data.getProperty("TWO_SECOND")));
		} catch (Exception Ex) {
			System.out.println("*****************************************");
			System.out.println("Scroll for Webelement  failed for: " + label);
			Ex.printStackTrace();
			System.out.println("*****************************************");
			TakeScreenShot("Fail", "Scroll for WebElement  failed for " + label);
			Assert.fail("Scroll for Webelement  failed for: " + label);
		}

	}

	public void TakeScreenShot(String status, String str) throws IOException {
		if (status.equalsIgnoreCase("pass"))
			logger.log(Status.PASS, str, MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(driver)).build());
		else if (status.equalsIgnoreCase("fail")) {
			logger.log(Status.FAIL, str, MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(driver)).build());
			Assert.fail(str);
		} else
			logger.log(Status.INFO, str, MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(driver)).build());
	}

	public boolean isElementDisplay(String label) {
		try {
			WebElement ele = creatLocator(label);
			ele.isDisplayed();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return true;
	}

	public String getTextfromElement(String label) {
		String returnVal = null;
		try {
			WebElement ele = creatLocator(label);
			returnVal = ele.getText();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return returnVal;
	}

	public void clearTextField(String label) throws InterruptedException {
		try {
			WebElement ele = creatLocator(label);

			ele.clear();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public List<WebElement> creatLocatorListElement(String label) {
		List<WebElement> eleList = null;

		try {
			String labelText = locatorString.getProperty(label);

			String labelTextArray[] = labelText.split("~");
			String method = labelTextArray[0];
			String loc = labelTextArray[1];
			switch (method) {
			case "xpath":
				eleList = driver.findElements(By.xpath(loc));
				break;
			case "css":
				eleList = driver.findElements(By.cssSelector(loc));
				break;
			default:
				System.out.println("Invalid locator type");
				break;
			}
		} catch (Exception Ex) {
			System.out.println("*****************************************");
			System.out.println(Ex);
			System.out.println("List of Webelement creation failed for: " + label);
			System.out.println("*****************************************");
		}
		return eleList;
	}

	public void refreshUntilTextBecome(String refreshBtnLocator, String Textlocator, String valueIsToBe)
			throws IOException, InterruptedException {

		int NoOfTimes = Integer.valueOf(data.getProperty("MaxNoOfTimesRefreshButtonToBePressed"));
		Thread.sleep(Integer.valueOf(data.getProperty("FIVE_SECOND")));
		int count = 0;
		boolean flag = true;
		do {
			flag = true;
			clickOnElement(refreshBtnLocator);
			Thread.sleep(Integer.valueOf(data.getProperty("FOUR_SECOND")));
			List<WebElement> eleList = creatLocatorListElement(Textlocator);
			js.executeScript("arguments[0].scrollIntoView();", eleList.get(0));
			for (int i = 0; i < eleList.size(); i++) {
				Thread.sleep(Integer.valueOf(data.getProperty("ONE_SECOND")));
				js.executeScript("arguments[0].scrollIntoView();", eleList.get(i));
				if (!eleList.get(i).getText().equalsIgnoreCase(valueIsToBe)) {
					flag = false;
					break;
				}
			} 	
			if (flag)
				break;

			if (count > NoOfTimes) {
				TakeScreenShot("fail", "Maximum No Of Times Refresh Button Pressing  reached:" + NoOfTimes);
				break;
			}
			count++;
		} while (true);

		if (flag)

			TakeScreenShot("Pass", "Order status turns to " + valueIsToBe);
		else
			TakeScreenShot("Fail", "Order status doesn't change");

	}

	public void getListofDropdownOptionsClick(String label, String opts) throws InterruptedException {
		try {
			List<WebElement> listofelement = creatLocatorListElement(label);
			for (int i = 0; i < listofelement.size(); i++) {
				if (listofelement.get(i).getText().equalsIgnoreCase(opts)) {
					listofelement.get(i).click();
					break;
				}
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public int getExcelColumnCellDataNumber(String path, int startcolNum, int searchColNum, String orderEntity,
			int expColNum) {
		File file = new File(path);
		FileInputStream inputStream = null;
		Workbook wb = null;
		Sheet sheet;
		int rowCounter = 0, count = 0;
		int resultCount = 0;
		try {
			inputStream = new FileInputStream(file);
			wb = new XSSFWorkbook(inputStream);
			if (wb.getSheetIndex("Sheet1") == -1) {
				throw new IllegalArgumentException("Sheet1" + " sheet not found");
			}
			sheet = wb.getSheet("Sheet1");
			int rowCounts = sheet.getPhysicalNumberOfRows();
			for (rowCounter = startcolNum; rowCounter < rowCounts; rowCounter++) {
				System.out.println(
						"Expected val:" + sheet.getRow(rowCounter).getCell(searchColNum).getStringCellValue().trim());
				System.out.println("actual val" + orderEntity.trim());
				if ((sheet.getRow(rowCounter).getCell(searchColNum).getStringCellValue().trim())
						.equals(orderEntity.trim())) {
					count = rowCounter;
					System.out.println("get value of No of order lines :"
							+ sheet.getRow(count).getCell(expColNum).getNumericCellValue());
					resultCount = (int) sheet.getRow(count).getCell(expColNum).getNumericCellValue();
					break;
				}
			}
			System.out.println("Result Count of respective column :" + resultCount);
			inputStream.close();
			wb.close();
			// file.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return resultCount;
	}

	public void selectbyDropDownValue(String label, String text) throws InterruptedException {
		try {
			WebElement ele = creatLocator(label);
			Select sel = new Select(ele);
			sel.selectByValue(text);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public String getInputTextFieldValue(String label) {
		String returnVal = null;
		try {
			WebElement ele = creatLocator(label);
			returnVal = ele.getAttribute("value");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return returnVal;
	}

	public boolean checkLocatorPresence(String label) throws IOException {
		WebElement ele = null;

		try {
			String labelText = locatorString.getProperty(label);
			String labelTextArray[] = labelText.split("~");
			String method = labelTextArray[0];
			String loc = labelTextArray[1];
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(5));
			switch (method) {

			case "id":
				w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(loc)));
				w.until(ExpectedConditions.elementToBeClickable(By.id(loc)));
				ele = driver.findElement(By.id(loc));
				break;

			case "xpath":
				w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(loc)));
				w.until(ExpectedConditions.elementToBeClickable(By.xpath(loc)));
				ele = driver.findElement(By.xpath(loc));
				break;

			case "linktext":
				w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.linkText(loc)));
				w.until(ExpectedConditions.elementToBeClickable(By.linkText(loc)));
				ele = driver.findElement(By.linkText(loc));
				break;

			case "partiallinktext":
				w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.partialLinkText(loc)));
				w.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(loc)));
				ele = driver.findElement(By.partialLinkText(loc));
				break;

			case "css":
				w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(loc)));
				w.until(ExpectedConditions.elementToBeClickable(By.cssSelector(loc)));
				ele = driver.findElement(By.cssSelector(loc));
				break;
			case "classname":
				ele = driver.findElement(By.className(loc));
				break;
			case "name":
				ele = driver.findElement(By.name(loc));
				break;
			case "tagname":
				ele = driver.findElement(By.tagName(loc));
				break;
			default:
				System.out.println("Invalid locator type");
				throw new IllegalArgumentException("Invalid locator type:" + label);

			}
		} catch (Exception Ex) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}
		return true;
	}

	public int getCellDataFromExcel(String path, String colVal) throws IOException {
		int rowExactNumber = 0;
		double cellVal = 0;
		File fi = new File(path);
		FileInputStream inputStream = null;
		Workbook wb;
		Cell cell;
		int i;
		int convtIntVal = 0;
		int rowCount;
		Sheet sh;
		try {
			inputStream = new FileInputStream(fi);
			wb = new XSSFWorkbook(inputStream);
			if (wb.getSheetIndex("Sheet1") == -1) {
				throw new IllegalArgumentException("Sheet1" + " sheet not found");
			}
			sh = wb.getSheet("Sheet1");
			rowCount = sh.getLastRowNum() - sh.getFirstRowNum();

			for (i = 6; i < rowCount; i++) {
				// System.out.println(sh.getRow(8).getCell(0).getStringCellValue());
				if (((sh.getRow(i).getCell(0).getStringCellValue().trim()).equals(colVal.trim()))) {
					System.out.println(" Record is found in the list");
					rowExactNumber = i;
					System.out.println("Exact No" + rowExactNumber);
					cell = sh.getRow(rowExactNumber).getCell(1);
					cellVal = cell.getNumericCellValue();
					// System.out.println("cell val"+cellVal.intValue());
					convtIntVal = (int) Math.round(cellVal);
					/*
					 * for(k=1;k<5;k++) { cell = sh.getRow(rowExactNumber).getCell(k); cellVal=
					 * cell.getNumericCellValue(); convtIntVal= (int) Math.round(cellVal); }
					 */
					// arrayOfCellData.add(convtIntVal);

				}
			}
			System.out.println("CellValues:" + convtIntVal);
			wb.close();
			inputStream.close();
			fi.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return convtIntVal;
	}

	public void getListofDropdownOptionsClickFromOptionFirst(String label, String opts) throws InterruptedException {
		try {
			List<WebElement> listofelement = creatLocatorListElement(label);
			for (int i = 1; i <= listofelement.size() - 1; i++) {
				if (listofelement.get(i).getText().contains(opts)) {
					listofelement.get(i).click();
					break;
				}
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public String fetchNofromString(String str) {

		List<String> lst = new ArrayList<String>();
		String temp = "";
		for (char c : str.toCharArray()) {

			if (Character.isDigit(c)) {
				temp += c;
			} else {
				if (temp.equals(""))
					continue;
				else
					lst.add(temp);
				temp = "";
			}

		}
		if (!temp.equals(""))
			lst.add(temp);
		String MaxStr = "";
		for (String s : lst) {
			if (s.length() > MaxStr.length())
				MaxStr = s;
		}
		return MaxStr;
	}
}
