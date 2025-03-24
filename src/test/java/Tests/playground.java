package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import PagesDemoWebShop.LoginPage;
import Utility.BaseMethods;
public class playground extends BaseMethods {
	String str[] = null;
	

	@Test
	public void Playground() {
		try {
			LoginPage logingpage= new LoginPage(driver);
			clickOnElement(logingpage.LoginLink,"Login");
			
			
//			clickOnElement("AddtoCartFrstEle");
//			clickOnElement("shoppingcart");
//			clickOnElement("RemoveCheckbox");
//			clickOnElement("updateshoppingcart");
//			clickOnElement("DemoWebshopHomeBtn");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			
		}

	}

	

	

}
