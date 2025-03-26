package PagesDemoWebShop;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage  {

	WebDriver driver;

	@FindBy(xpath="//a[contains(text(),'Log in')]")
	public WebElement LoginLink;
	
	@FindBy(id="Email")
	public WebElement email;
	
	@FindBy(name="Password")
	public WebElement password;
	
	@FindBy(xpath = "//input[@value=\"Log in\"]")
	public WebElement logInBtn;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

}
