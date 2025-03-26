package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import PagesDemoWebShop.HomePage;
import PagesDemoWebShop.LoginPage;
import Utility.BaseMethods;
public class playground extends BaseMethods {
	String str[] = null;
	

	@Test
	public void Playground() {
		try {
			LoginPage logingpage= new LoginPage(driver);
			clickOnElement(logingpage.LoginLink,"Login");
			enterText(logingpage.email,"varunkumar@test.com", "Email");
			enterText(logingpage.password,"varunkumar", "Password");
			clickOnElement(logingpage.logInBtn,"Log In");
			HomePage homepage = new HomePage(driver);
			clickOnElement(homepage.booksLinkTopMenu, "Books top menu");
			clickOnElement(homepage.firstBookAddtoCart, "Add to cart");
			

			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			
		}

	}

	

	

}
