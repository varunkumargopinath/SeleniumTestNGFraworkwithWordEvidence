package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import Utility.BaseMethods;

public class playground extends BaseMethods {
	String str[] = null;

	@Test
	public void Playground() {
		try {
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
			
		}

	}

	

	

}
