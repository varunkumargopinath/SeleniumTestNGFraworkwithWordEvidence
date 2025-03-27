package PagesDemoWebShop;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class shoppingCartPage {
	
	WebDriver driver;
	
	@FindBy(xpath = "//span[@class=\"product-unit-price\"]")
	public WebElement bookUnitPrice;
	
	@FindBy(xpath = "//*[@class=\"product-price order-total\"]")
	public WebElement TotalPrice;
	
	public shoppingCartPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
}
