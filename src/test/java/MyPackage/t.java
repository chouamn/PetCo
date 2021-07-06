package MyPackage;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class t {
	WebDriver driver;

	@BeforeClass
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
	}

	@Test(priority = 1, dataProvider = "LoginData")
	public void loginTest(String zip1, String zip2) throws InterruptedException {
		driver.get("https://www.petco.com/shop/en/petcostore");
	//	Thread.sleep(4000);//necessary
		
	
		
//	driver.switchTo().frame(5);
 //   Thread.sleep(4000);
//	driver.findElement(By.xpath("//button[contains(text(),'Continue to Site')]")).click();;
//	driver.switchTo().defaultContent();
		
//		int numOfFrames = driver.findElements(By.xpath("//iframe")).size();
//		System.out.println(numOfFrames);
//		for(int i =0; i<numOfFrames; i++) {
//			System.out.println("Start for loop"+Integer.toString(i));
//			driver.switchTo().frame(i);
//			System.out.println("in frame "+Integer.toString(i));
//			boolean exists = (driver.findElements(By.xpath("//button[contains(text(),'Continue to Site')]")).size() > 0);
//			System.out.println(exists);
//			if(exists) {
//				System.out.println(i);
//			}
//			System.out.println("end for loop");
//			WebElement closeDiscount = driver.findElement(By.xpath("//button[contains(text(),'Continue to Site')]"));
//		}
		
		
		
		
		
		WebElement SearchBox = driver.findElement(By.id("header-search"));
		SearchBox.click();
		Thread.sleep(2000);
		SearchBox.sendKeys("ok");
		SearchBox.sendKeys(Keys.ENTER);
		
		
		WebElement Store = driver.findElement(
				By.xpath("//*[@id=\"petco-header-render-node\"]/header/div[3]/div[1]/div[1]/ul/li[1]/button"));
		Store.click();
		Thread.sleep(2000);

		WebElement StoreZip = driver.findElement(By.id("change-store-search"));
		StoreZip.sendKeys(zip1);

		WebElement SearchBtn = driver.findElement(By.xpath(
				"//*[@id=\"petco-header-render-node\"]/header/div[3]/div[1]/div[1]/ul/li[1]/div/div[3]/div[1]/form/button"));
		SearchBtn.click();
		Thread.sleep(2000);
		WebElement SelectStoreBtn = driver.findElement(By.xpath("//button[contains(text(), 'Select Store')]"));
		SelectStoreBtn.click();
		Thread.sleep(2000);
		WebElement DeliveryLocation = driver.findElement(
				By.xpath("//*[@id=\"petco-header-render-node\"]/header/div[3]/div[1]/div[1]/ul/li[2]/button"));
		DeliveryLocation.click();
		WebElement DeliveryZip = driver.findElement(By.id("update-delivery-zip-code"));
		DeliveryZip.sendKeys(zip2);
		Thread.sleep(5000);
		WebElement UpdateLocationBtn = driver.findElement(By.xpath("//button[contains(text(),'Update Location')]"));
		UpdateLocationBtn.click();
		Thread.sleep(2000);
	}

	@DataProvider(name = "LoginData")
	public String[][] getData() throws IOException {
		String path = ".\\datafiles\\testdata.xlsx";
		XLUtility xlutil = new XLUtility(path);

		int totalrows = xlutil.getRowCount("Sheet1");
		int totalcols = xlutil.getCellCount("Sheet1", 1);

		String loginData[][] = new String[totalrows][totalcols];

		for (int i = 1; i <= totalrows; i++) // 1
		{
			for (int j = 0; j < totalcols; j++) // 0
			{
				loginData[i - 1][j] = xlutil.getCellData("Sheet1", i, j);
			}

		}

		return loginData;
	}

	@DataProvider(name = "ItemData")
	public String[][] getData2() throws IOException {
		String path = ".\\datafiles\\testdata2.xlsx";
		XLUtility xlutil = new XLUtility(path);

		int totalrows = xlutil.getRowCount("Sheet2");
		int totalcols = xlutil.getCellCount("Sheet2", 1);

		String itemData[][] = new String[totalrows][totalcols];

		for (int i = 1; i <= totalrows; i++) // 1
		{
			for (int j = 0; j < totalcols; j++) // 0
			{
				itemData[i - 1][j] = xlutil.getCellData("Sheet2", i, j);
			}

		}

		return itemData;
	}

	@Test(priority = 2, dataProvider = "ItemData")
	public void checkoutTest(String SKU, String QTY, String delOption, String zipcode) throws InterruptedException {
		System.out.println("Searching item");
		WebElement SearchBox = driver.findElement(By.xpath("//input[@id='header-search' and @type='search']"));
		String barcode = SKU;
		System.out.println(barcode);
		Actions act = new Actions(driver);
		act.moveToElement(SearchBox);
		act.click(SearchBox).build().perform();
		System.out.println("line 118");
		Thread.sleep(3000);
		
		try {
			SearchBox.click();
		}
		catch(StaleElementReferenceException e) {
			SearchBox = driver.findElement(By.xpath("//input[@id='header-search' and @type='search']"));
			SearchBox.click();
		}
		
		try {
			SearchBox.sendKeys(barcode);
		}
		catch(StaleElementReferenceException e) {
			SearchBox = driver.findElement(By.xpath("//input[@id='header-search' and @type='search']"));
			SearchBox.sendKeys(barcode);
		}
		
		
		System.out.println("line 150");
		SearchBox.sendKeys(Keys.ENTER);
		WebElement quantity = driver
				.findElement(By.xpath("//*[@id=\"__next\"]/main/div/div[3]/form/div[7]/div/button[2]"));
		WebElement FreePickupBtn = driver
				.findElement(By.xpath("//*[@id=\"__next\"]/main/div/div[3]/form/fieldset/label[1]/input"));
		int quant = Integer.parseInt(QTY);
		int cartQty = 1;
		String Dlv = "Pickup";
		while (cartQty < quant) {
			quantity.click();
			cartQty++;
			if (delOption.equalsIgnoreCase(Dlv)) {
				FreePickupBtn.click();
				Thread.sleep(2000);
			}

		}
		Thread.sleep(5000);
		WebElement addToCart = driver.findElement(By.xpath("//*[@id=\"__next\"]/main/div/div[3]/form/div[7]/button"));
		addToCart.click();

	}

	@Test(priority = 3)
	public void checkoutCart() throws InterruptedException {
		WebElement cart = driver
				.findElement(By.xpath("//*[@id=\"petco-header-render-node\"]/header/div[2]/div/nav[2]/ul/li[2]/a"));
		cart.click();
		WebElement proceed = driver.findElement(By.id("continueReviewPage"));
		proceed.click();
		WebElement guestEmail = driver.findElement(By.id("guestEmail"));
		guestEmail.click();
		guestEmail.sendKeys("houseforsale@gmail.com");
		WebElement continueBtn = driver.findElement(By.id("continueToReview"));
		continueBtn.click();
        Thread.sleep(3000);
        
        
		WebElement firstName = driver.findElement(By.id("firstName"));
		firstName.sendKeys("PETCO");

		WebElement lastName = driver.findElement(By.id("lastName"));
		lastName.sendKeys("Headquarter");

		WebElement address = driver.findElement(By.id("address"));
		address.sendKeys("1000 Main St");
		WebElement city = driver.findElement(By.id("city"));
		city.sendKeys("Ramona");
		WebElement state = driver.findElement(By.id("state"));
		Select dropdown = new Select(state);
		dropdown.selectByValue("CA");
		
		WebElement zip = driver.findElement(By.id("zip"));
		zip.sendKeys("92065");
		WebElement phoneNumber = driver.findElement(By.id("phoneNumber"));
		phoneNumber.sendKeys("2482387607");
		WebElement submit = driver.findElement(By.xpath(".//*[contains(@class,'SaveAndContinueButton')]"));
		submit.click();
		Thread.sleep(3000);//necessary
		WebElement submit2 = driver.findElement(By.xpath(".//*[contains(@class,'SaveAndContinueButton')]"));
		submit2.click();
		
//		pick up in store
		WebElement name = driver.findElement(By.id("firstName"));
		name.sendKeys("PETCO");
		WebElement lName = driver.findElement(By.id("lastName"));
		lName.sendKeys("Headquarter");
		WebElement phone = driver.findElement(By.id("phoneNumber"));
		phone.sendKeys("2482387607");
		WebElement submit3 = driver.findElement(By.xpath(".//*[contains(@class,'SaveAndContinueButton')]"));
		submit3.click();
		
//		repeat delivery and save
		WebElement submit4 = driver.findElement(By.xpath(".//*[contains(@class,'SaveAndContinueButton')]"));
		submit4.click();
		
//		Payment
		WebElement addCard = driver.findElement(By.xpath(".//*[contains(@class,'InvertedButton')]"));
		addCard.click();
		
		System.out.println("checkpoint");
		Thread.sleep(3000);
		
		driver.switchTo().frame(0);
		
		WebElement fullName = driver.findElement(By.id("FullName"));
		fullName.sendKeys("Petco");
		WebElement AccountNumber = driver.findElement(By.id("AccountNumber"));
		AccountNumber.sendKeys("5178059586640261");
		WebElement expMonth = driver.findElement(By.id("ExpirationMonth"));
		expMonth.click();
		Select dropdown1 = new Select(expMonth);
		dropdown1.selectByValue("5");
		
		

		
		
		WebElement ExpirationYearBtn = driver.findElement(By.id("ExpirationYear"));
		Select dropdown2 = new Select(ExpirationYearBtn);
		dropdown2.selectByValue("2025");
		
		
		WebElement CVVBtn = driver.findElement(By.id("SecurityCode"));
		CVVBtn.sendKeys("681");
		WebElement ContinueBtn = driver.findElement(By.xpath("//input[@id='SaveButton' and @type = 'submit']"));
		ContinueBtn.click();
		
		
		WebElement Continue4 = driver.findElement(By.xpath("//button[contains(text(),'Save & Continue')]"));
		Continue4.click();
	//	WebElement PlaceOrderBtn = driver
		//		.findElement(By.xpath("//button[contains (text(), ‘Place Order’ )]"));
				//.findElement(By.xpath("//*[@id=\"__next\"]/div/main/div[2]/div[1]/div[4]/div/div/button"));
		//System.out.println(PlaceOrderBtn.isEnabled());

	}

	@AfterClass
	void tearDown() {
	//	driver.close();
	}

}
