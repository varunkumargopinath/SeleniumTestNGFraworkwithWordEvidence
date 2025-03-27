package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import PagesDemoWebShop.HomePage;
import PagesDemoWebShop.LoginPage;
import PagesDemoWebShop.shoppingCartPage;
import Utility.BaseMethods;
public class playground extends BaseMethods {
	String str[] = null;
	

	@Test
	public void Playground() {
		try {
			LoginPage logingpage= new LoginPage(driver);
			
			
			clickOnElement(logingpage.LoginLink,"Login");
			enterText(logingpage.email,"varunkumar@test.com", "Email");
			enterText(logingpage.password,"<Enter your password>", "Password");
			clickOnElement(logingpage.logInBtn,"Log In");
			
			HomePage homepage = new HomePage(driver);
			clickOnElement(homepage.booksLinkTopMenu, "Books top menu");
			clickOnElement(homepage.firstBookAddtoCart, "Add to cart");
			clickOnElement(homepage.shoppingcart, "Shopping Cart");
			
			shoppingCartPage shoppingcartpage= new shoppingCartPage(driver);
			verifyText(shoppingcartpage.bookUnitPrice,"10.00","Unit Price");
			verifyText(shoppingcartpage.TotalPrice, "12", "Total Price");

			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
			
		}

	}

	

	

}
