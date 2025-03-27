package PagesDemoWebShop;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage  {

	WebDriver driver;

	@FindBy(xpath ="(//a[contains(text(),'Books')])[1]")
	public WebElement booksLinkTopMenu;
	
	@FindBy(xpath="(//*[@value=\"Add to cart\"])[1]")
	public WebElement firstBookAddtoCart;
	
	@FindBy(xpath = "//span[text()='Shopping cart']")
	public WebElement shoppingcart;
	
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

}
