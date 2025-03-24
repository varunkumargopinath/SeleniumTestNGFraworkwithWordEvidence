package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class initialization extends WordEvidenceBaseMethods {

	public static WebDriver driver;
	public static String browser;
	public static Properties data;
	public static WebDriverWait wait;
	public JavascriptExecutor js;
	public Actions actions;
	public static String recordList[];

	public static String getScreenshot(WebDriver driver) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		return src.getPath();
	}

	public void initProperties() throws IOException {
		File datafile = new File(System.getProperty("user.dir") + "/data.properties");

		if (!datafile.exists()) {
			throw new FileNotFoundException("Properties file not found: " + datafile.getAbsolutePath());
		}

		data = new Properties();

		// Try-with-resources to ensure automatic closing
		try (FileInputStream dataInput = new FileInputStream(datafile)) {
			data.load(dataInput);
		} catch (IOException e) {
			e.printStackTrace(); // Consider using a logger instead of printing stack trace
			throw e; // Re-throw exception if needed
		}
	}

	public WebDriver launchBrowser(String browser) throws InterruptedException, IOException {
		initialization.browser = browser;

		switch (initialization.browser) {

		case "firefox":
			driver = new FirefoxDriver();
			break;

		case "chrome":
			// System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--headless");
			options.addArguments("--start-maximized");
			options.addArguments("--incognito");
			options.addArguments("--disable-gpu");
			options.addArguments("--remote-allow-origins=*");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--ignore-ssl-errors=yes");
			options.addArguments("--ignore-certificate-errors");

			driver = new ChromeDriver(options);
			break;

		case "edge":
			// System.setProperty("webdriver.edge.driver", ".\\msedgedriver.exe");
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments("-inprivate");
			edgeOptions.addArguments("--start-maximized");
			// edgeOptions.addArguments("headless");
			edgeOptions.addArguments("disable-gpu");
			edgeOptions.addArguments("--remote-allow-origins=*");
			driver = new EdgeDriver(edgeOptions);
			break;
		}

		String env = "https://demowebshop.tricentis.com/";
		System.out.println(env);
		driver.get(env);
		//Timeouts in milliseconds fetched from data.properties file
		driver.manage().timeouts().implicitlyWait(Integer.valueOf(data.getProperty("ImpliciteWaitTime")),
				TimeUnit.MILLISECONDS);
		
		return driver;
	}

	public void stop() {
		try {
			Thread.sleep(3000);
			driver.quit();
		} catch (Exception e) {
		}
	}

	public void clickOnElement(WebElement ele,String EvidenceLogName) {
		try {
			wait= new WebDriverWait(driver, Duration.ofMillis(Integer.valueOf(data.getProperty("WebdriverWaitTime"))));
			wait.until(ExpectedConditions.visibilityOf(ele));
			//wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(ele),
			//		ExpectedConditions.elementToBeClickable(ele), ExpectedConditions.elementToBeSelected(ele)));
			js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", ele);
			EnterTestStepDescriptionWithScreenshotForInfo(new String[] { "Clicked on \"" + EvidenceLogName + "\"" });
			// js.executeScript("arguments[0].click();", ele);
			ele.click();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

	public void doubleClickOnElement(WebElement ele) throws InterruptedException, IOException {
		actions = new Actions(driver);
		actions.doubleClick(ele).build().perform();
		Thread.sleep(Integer.valueOf(data.getProperty("ONE_SECOND")));

	}

//	public void sendText(String locator, String text) {
//		try {
//			WebElement ele = creatLocator(locator);
//			wait.until(ExpectedConditions.visibilityOf(ele));
//			ele.sendKeys(text);
//			js = (JavascriptExecutor) driver;
//			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", ele);
//			EnterTestStepDescriptionWithScreenshotForInfo(
//					new String[] { "Enter \"" + text + "\" in the \"" + locator + "\" Field" });
//
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//	}
//
//	public String getTextfromElement(String label) {
//		String returnVal = null;
//		try {
//			WebElement ele = creatLocator(label);
//			returnVal = ele.getText();
//
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//		return returnVal;
//	}
//
//	public void ScrollToWebElement(String label) throws IOException {
//		WebElement ele = null;
//		try {
//			String labelText = locatorString.getProperty(label);
//			String labelTextArray[] = labelText.split("~");
//			String method = labelTextArray[0];
//			String loc = labelTextArray[1];
//
//			switch (method) {
//
//			case "id":
//				ele = driver.findElement(By.id(loc));
//				break;
//			case "xpath":
//				ele = driver.findElement(By.xpath(loc));
//				break;
//			case "linktext":
//				ele = driver.findElement(By.linkText(loc));
//				break;
//			case "partiallinktext":
//				ele = driver.findElement(By.partialLinkText(loc));
//				break;
//			case "css":
//				ele = driver.findElement(By.cssSelector(loc));
//				break;
//			case "classname":
//				ele = driver.findElement(By.className(loc));
//				break;
//			case "name":
//				ele = driver.findElement(By.name(loc));
//				break;
//			case "tagname":
//				ele = driver.findElement(By.tagName(loc));
//				break;
//			default:
//				System.out.println("Invalid locator type");
//				break;
//			}
//
//			js.executeScript("arguments[0].scrollIntoView();", ele);
//			Thread.sleep(Integer.valueOf(data.getProperty("TWO_SECOND")));
//		} catch (Exception Ex) {
//			System.out.println("*****************************************");
//			System.out.println("Scroll for Webelement  failed for: " + label);
//			Ex.printStackTrace();
//			System.out.println("*****************************************");
//			Assert.fail("Scroll for Webelement  failed for: " + label);
//		}
//
//	}
//
//	public void clearTextField(String label) throws InterruptedException {
//		try {
//			WebElement ele = creatLocator(label);
//
//			ele.clear();
//
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//
//	}

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
			Assert.fail(e.getMessage());
		}
		return value;

	}

}
