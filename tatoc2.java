package mavenproj.tatoc2;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
public class tatoc2 
{
	WebDriver driver;

	@BeforeClass
	public void init()
	{
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\shivangigautam\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver=new ChromeDriver();
	}
	@Test
	public void Step_01_launchURL()
	{
		driver.get("http://10.0.1.86/tatoc/basic/grid/gate");
	}
	@Test
	public void Step_02_clickGreenbox()
	{ 
		Assert.assertEquals(driver.findElement(By.className("greenbox")).isDisplayed(), true);
		this.driver.findElement(By.className("greenbox")).click();
		String expectedUrl = "http://10.0.1.86/tatoc/basic/frame/dungeon";
		Assert.assertEquals(expectedUrl,  driver.getCurrentUrl(), "Did'nt navigate to the correct page");
	}

	@Test(dependsOnMethods= "Step_02_clickGreenbox")
	public void frameDungeon()
	{
		driver.switchTo().frame("main");

		Assert.assertEquals(driver.findElement(By.id("answer")).isDisplayed(),true); //check if the frame is displayed or not
		String box1= driver.findElement(By.id("answer")).getAttribute("class"); //get colour of second box
		int n=0;
		while(n==0)
		{
			driver.findElement(By.cssSelector("a")).click();  //click the repaint box2	
			driver.switchTo().frame("child");  //goto child for box2
			String box2=driver.findElement(By.id("answer")).getAttribute("class");
			driver.switchTo().parentFrame();      //go back to the parent frame to repaint the box
			if(box1.equals(box2))
			{
				driver.findElements(By.cssSelector("a")).get(1).click(); 
				n=1;//clicking proceed
			}	
		}
			
			String expectedUrl = "http://10.0.1.86/tatoc/basic/drag";
			Assert.assertEquals(expectedUrl,  driver.getCurrentUrl(), "Did'nt navigate to the correct page");

		
		
	}
	@Test(dependsOnMethods= "frameDungeon")
	public void drag()
	{
		Actions actions=new Actions(driver);
		Assert.assertEquals(this.driver.findElement(By.id("dropbox")).isDisplayed(),true);   //check if the dropbox is displayed or not
		Assert.assertEquals(this.driver.findElement(By.id("dragbox")).isDisplayed(),true);   //check if the dragbox is displayed or not

		WebElement drop = driver.findElement(By.id("dropbox"));  //get dropbox
		WebElement drag = driver.findElement(By.id("dragbox"));  //get dragbox

		actions.dragAndDrop(drag, drop);  //drag and drop in the box
		actions.build().perform();
		driver.findElement(By.cssSelector("a")).click();
		String expectedUrl ="http://10.0.1.86/tatoc/basic/windows";  //redirects to the page of PopUp Window
		Assert.assertEquals(expectedUrl,  driver.getCurrentUrl(), "Did'nt navigate to the correct page");
	}
	@Test(dependsOnMethods= "drag")
		public void popUp()
		{	
          String parentWindowHandler= driver.getWindowHandle();   //store your parent window handler
          driver.findElement(By.cssSelector("a")).click();       //click on popup window
		  String subWindowHandler=null;
		  Set<String> handles= driver.getWindowHandles();    //get all the window handles
		  System.out.println(handles);
		  Iterator<String> iterator= handles.iterator();
		  while(iterator.hasNext())
		  {
			  subWindowHandler=iterator.next();
		  }
		  driver.switchTo().window(subWindowHandler);  //switch to popup window
		  driver.findElement(By.id("name")).sendKeys("qainfotech");
		  driver.findElement(By.id("submit")).click();
		  driver.switchTo().window(parentWindowHandler);  //switch back to parent window
		  driver.findElement(By.linkText("Proceed")).click();
		  //driver.findElement(By.cssSelector("a")).getAttribute().click();  //click on proceed
		  String expectedUrl ="http://10.0.1.86/tatoc/basic/cookie";  //redirects to the page of cookie handling
			Assert.assertEquals(expectedUrl,  driver.getCurrentUrl(), "Did'nt navigate to the correct page");
		}
	@Test(dependsOnMethods="popUp")
		public void cookie() {

		driver.findElement(By.linkText("Generate Token")).click();
		String token1 = driver.findElement(By.id("token")).getText();
		String Token2 = token1.substring(token1.indexOf(":")+2);
		Cookie cookie = new Cookie("Token",Token2);
		driver.manage().addCookie(cookie);
		driver.findElement(By.linkText("Proceed")).click();
		
		String expectedUrl = ("http://10.0.1.86/tatoc/end");
		Assert.assertEquals(expectedUrl, driver.getCurrentUrl());
	}
			@AfterClass
			public void closeSession() 
			{
		//		driver.quit();
			}
	}
	

