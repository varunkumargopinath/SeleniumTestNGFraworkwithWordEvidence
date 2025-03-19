package Tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import Utility.BaseMethods;

public class BrowserlaunchPlayground extends BaseMethods {
	

	@Test
	public void SampleRun() {
		try {
			StartReportReader("fdsad");
		WebElement iframe= driver.findElement(By.xpath("(//frame)[3]"));
		System.out.println("gsdgs"+driver.findElement(By.xpath("(//frame)[3]")).getAttribute("innerHTML"));
		
		driver.findElement(By.xpath("(//frame)[3]"));
			driver.switchTo().frame(iframe);
			
			driver.findElement(By.xpath("//input[@name='mytext3']")).sendKeys("sameple text");
			String s= driver.findElement(By.id("id3")).getAttribute("innerHTML");
			System.out.println(s);
			
			
			Thread.sleep(7000);
			

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			logger.log(Status.FAIL, e.getMessage());
		}

	}

	

	
}
