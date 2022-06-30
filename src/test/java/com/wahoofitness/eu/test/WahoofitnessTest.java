package com.wahoofitness.eu.test;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WahoofitnessTest {
	public WebDriver driver;
	SoftAssert soft;
	WebDriverWait wait;
	
	@Test(priority=1)
	public void PageTitleTest() {
//		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		//open app url
		driver.get("https://eu.wahoofitness.com/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.manage().window().maximize();
		
		soft=new SoftAssert();
		wait=new WebDriverWait(driver, Duration.ofSeconds(50));
		
		soft.assertEquals(driver.getCurrentUrl(),"https://eu.wahoofitness.com/");
		soft.assertTrue(driver.getPageSource().contains("SHOP YOUR FAVORITES ON SALE"));
		soft.assertAll();
	}
	
	@Test(priority = 2)
	public void addProductToCart() {
		WebElement shopButton = driver.findElement(By.xpath("//span[text()='SHOP']"));
		soft.assertTrue(shopButton.isEnabled());
		Actions builder=new Actions(driver);
		builder.moveToElement(shopButton).build().perform();
		
		// click on All product option
		WebElement allProductLink = driver.findElement(By.xpath("//a[contains(text(),'All Products')]"));
		allProductLink.click();		
			
		// adding random product
			List<WebElement> productOptions = driver.findElements(By.xpath("//div[@class=\"category-products\"]//li"));
			//waitTillElementsToBeVisible(driver, productOptions, 40);
			int count=productOptions.size();
			//int randomNum = 1 + (int)(Math.random() * count);
			
			for (int i = 1; i <=count; i++) {
				productOptions.get(2).click();
			}
			
//			for (WebElement product : productOptions) {
//				if (product.getText().equalsIgnoreCase("productName")) {
//					product.click();
//					break;
//				}
//	}
			
			WebElement addToCartButton = driver.findElement(By.xpath("//button[@class=\"button tocart btn-cart\"]"));
			addToCartButton.click();
			soft.assertTrue(driver.getPageSource().contains("Your Cart"));
			
			//navigate back to all product page and select another product
			
			driver.navigate().back();
			for (int i = 1; i <=count; i++) {
				productOptions.get(9).click();
			}
			
			addToCartButton.click();
			
		
			
			List<WebElement> ptoductsInCart = driver.findElements(By.xpath("//*[@class=\"product-item-photo\"]"));
			int cartProduct = ptoductsInCart.size();
			
			soft.assertEquals(cartProduct, 2);
			
			soft.assertAll();
}
	
	@Test(priority = 3, enabled = false)
	public void removeProductFromCart() {
		
		WebElement removeButton = driver.findElement(By.xpath("(//*[text()='Remove'])[2]"));
		soft.assertTrue(removeButton.isEnabled());
		removeButton.click();
		
		driver.findElement(By.xpath("//*[@class=\"action-primary action-accept\"]")).click();
		
		soft.assertTrue(driver.getPageSource().contains("Item was removed successfully"));
		
		soft.assertAll();
	}
	
	
	@Test(priority = 4, enabled = false)
	public void viewAndEditCart() {
		WebElement editCart = driver.findElement(By.xpath("//*[text()='View and Edit Cart']"));
		soft.assertTrue(editCart.isEnabled());
		editCart.click();
		
		soft.assertTrue(driver.getPageSource().contains("Your Cart"));
		
		WebElement qtyBox = driver.findElement(By.xpath("//*[@class=\"input-text qty\"]"));
		qtyBox.clear();
		qtyBox.sendKeys("3");
		driver.findElement(By.xpath("//*[text()='Update Cart']")).click();
		soft.assertAll();
		
	}
	
	
	@Test(priority = 5, enabled = false)
	public void checkoutProduct() {
		soft.assertTrue(driver.getPageSource().contains("Checkout"));
		driver.findElement(By.id("id=\"customer-email\"")).sendKeys("ads.shinde@in.in");
		driver.findElement(By.xpath("//*[@name=\"firstname\"]")).sendKeys("Ashish");
		driver.findElement(By.xpath("//*[@name=\"lastname\"]")).sendKeys("Shinde");
		driver.findElement(By.xpath("//*[@name=\"street[0]\"]")).sendKeys("25 link road");
		driver.findElement(By.xpath("//*[@name=\"city\"]")).sendKeys("Pandharpur");
		driver.findElement(By.xpath("//*[@name=\"postcode\"]")).clear();
		driver.findElement(By.xpath("//*[@name=\"postcode\"]")).sendKeys("526365");
		driver.findElement(By.xpath("//*[@name=\"telephone\"]")).sendKeys("8888999945");
		driver.findElement(By.xpath("//*[@name=\"cardnumber\"]")).sendKeys("8956 2356 2323 5689");
		driver.findElement(By.xpath("//*[@name=\"exp-date\"]")).sendKeys("06/25");
		driver.findElement(By.xpath("//*[@name=\"cvc\"]")).clear();
		driver.findElement(By.xpath("//*[@name=\"cvc\"]")).sendKeys("568");
		driver.findElement(By.xpath("//*[@class=\"action primary checkout amasty\"]")).click();
		soft.assertTrue(driver.getPageSource().contains("Please verify your card information."));
		
		soft.assertAll();
	}
	
	@AfterClass
	public void tearDown() {
		driver.quit();
	}
	
	
	
	// wait methods
	
	/**
	 * Wait for the element to be clickable ignoring the
	 * StaleElementReferenceException
	 * 
	 * @param driver
	 * @param element  - provide locator value of element till it is visible on
	 *                 application and then click that element.
	 * @param waitTime - provide maximum wait time in seconds for driver
	 */
	public static boolean waitForElementToBeClickableBool(WebDriver driver, WebElement element, int waitTime) {
		boolean flag = false;
		try {
			new WebDriverWait(driver, Duration.ofSeconds(waitTime)).ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.elementToBeClickable(element));
			flag = true;
			return flag;

		} catch (Exception e) {
			return flag;
		}
	}
	
	/**
	 * Wait for the element to be visible ignoring the
	 * StaleElementReferenceException
	 * 
	 * @param driver
	 * @param locator  - provide locator value of element till it is visible on
	 *                 application and then click that element.
	 * @param waitTime - provide maximum wait time in seconds for driver
	 */
	
	public static boolean waitForElementToBeVisible(WebDriver driver, WebElement element, int waitTime) {
		boolean flag = false;
		try {
			new WebDriverWait(driver, Duration.ofSeconds(waitTime)).ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.visibilityOfElementLocated((By) element));
			flag = true;
			return flag;
		} catch (Exception e) {
			return flag;
		}
	}
	
	/**
	 * Wait for all element to be visible ignoring the
	 * StaleElementReferenceException
	 * 
	 * @param driver
	 * @param locator  - provide locator value of element till it is visible on
	 *                 application and then click that element.
	 * @param waitTime - provide maximum wait time in seconds for driver
	 */
	
	public static boolean waitTillElementsToBeVisible(WebDriver driver, List<WebElement> element, int waitTime) {
		boolean flag = false;
		try {
			new WebDriverWait(driver, Duration.ofSeconds(waitTime)).ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy((By) element));
			flag = true;
			return flag;
		} catch (Exception e) {
			return flag;
		}
	}
	
	
}

