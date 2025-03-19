package Tests;

import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import Utility.BaseMethods;

public class playground extends BaseMethods {
	String str[] = null;

	@Test
	public void Playground() {
		try {
			StartReportReader("playground");
			clickOnElement("Login");
			sendText("Email", "varunkumar@test.com");
			sendText("Password", "varunkumar");
			clickOnElement("LoginBtn");
			clickOnElement("books");
			clickOnElement("AddtoCartFrstEle");
			clickOnElement("shoppingcart");
			clickOnElement("RemoveCheckbox");
			clickOnElement("updateshoppingcart");
			clickOnElement("DemoWebshopHomeBtn");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			logger.log(Status.FAIL, e.getMessage());
		}

	}

	

	

}
