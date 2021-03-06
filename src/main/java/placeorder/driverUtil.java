package placeorder;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;


public class driverUtil {
	
	public Actions action ;
	
	public JavascriptExecutor js ;
	
	constantData data_obj ;

	public static void SwitchtoLatestWindow(WebDriver driver)//switching to the latest window
	{
		Set<String> handles = driver.getWindowHandles();
		
		for(String web : handles)
		{
			driver.switchTo().window(web);
		}
	}
	
	
	public static void SwitchtoWindowByTitle(WebDriver driver, String title)//switching to the window using title
	{
		Set<String> handles = driver.getWindowHandles();
		
		for(String web : handles)
		{
			driver.switchTo().window(web);
			String str = driver.getTitle();
			
			if(str.contains(title))
			{
				driver.switchTo().window(web);
				break;
			}
		}
		
	}
	
	public void frameSet(WebDriver driver)
	{
		 
		int size = driver.findElements(By.tagName("iframe")).size();
		
		if(size>0) {
		WebElement iframe = new WebDriverWait(driver,15).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[contains(@src,'https://ogs.google.com/widget/app')]")));
		driver.switchTo().frame(iframe);
		System.out.println("Frame set");
		}
		else
		{
			System.out.println("No Frames");
		}
		 
	}
	
	public WebDriver chrome()
	{
		System.setProperty("webdriver.chrome.driver","C:\\Users\\UNITS\\Downloads\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;

	}
	
	public WebDriver firefox()
	{
		
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\UTIS CPU 31\\Downloads\\geckodriver-v0.24.0-win64\\geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return driver;
		
	}
	
	public void Click(WebElement element) throws InterruptedException
	{
		
		int count=0;
					
		while(count<10)
		{
			try
			
			{
				element.click();
				break ;
			}
			catch(StaleElementReferenceException ex)
			{
				if(count++<10)
				{
					System.out.println("Retrying Click Method"+count);
					Thread.sleep(1000);
				}
			
			}
		}
	}
		
		public boolean Isdisplayed(WebElement element) throws InterruptedException
		{
			
			int count=0;
			while(count<10)
			{
				try
				
				{
					element.isDisplayed();
					break ;
				}
				catch(StaleElementReferenceException ex)
				{
					if(count++<10)
					{
						System.out.println("Retrying isDisplayed Method "+count);
						Thread.sleep(1000);
						return Isdisplayed(element);
					}				
				}
			}
			return true;
	}
	
	public void Clear(WebElement element) throws InterruptedException
	{
		int count=0;
		
		while(count<10)
		{
			try
			
			{
				element.clear();
				break ;
			}
			catch(StaleElementReferenceException ex)
			{
				if(count++<10)
				{
					System.out.println("Retrying Clear Method "+count);
					Thread.sleep(1000);
				}
			
			}
		}
	
		
	}
	
	public void Sendkeys(WebElement element, String keys) throws InterruptedException
	{
		
		int count=0;
		while(count<10)
		{
			try
			
			{
				element.sendKeys(keys);
				break ;
			}
			catch(StaleElementReferenceException ex)
			{
				if(count++<10)
				{
					System.out.println("Retrying Sendkeys Method "+count);
					Thread.sleep(1000);
				}
			
			}
		}
	}
	
	public void AcceptAlertifPresent(WebDriver driver) throws InterruptedException {
        
		int count = 0;
		
		while(count<5)
		{
			try
			{
				driver.switchTo().alert().accept();
				break ;
			}
			
			catch(NoAlertPresentException ex)
			{
				count++;
				System.out.println("Retrying Alert accept"+count);
				Thread.sleep(1000);
			}
		}
		
    }
	
	
	public void jClick(WebElement element) throws InterruptedException
	{
		if(waitForElementToLoad(element))
			{
				js = (JavascriptExecutor)data_obj.driver;
					
				js.executeScript("arguments[0].click();", element);
			}
	}
	
	public void jClear(WebDriver driver, WebElement element)
	{
		js = (JavascriptExecutor)driver;
		
		js.executeScript("arguments[0].clear();", element);
	}
	

	public void snapShots(WebDriver driver, String filePath) throws IOException
	{
		TakesScreenshot scrsht = (TakesScreenshot) driver ;
		
		File source = scrsht.getScreenshotAs(OutputType.FILE);
		
		File dest = new File(filePath);
		
		FileUtils.copyFile(source, dest);
	}
	
	
	public void sendPDFReportByGMail(String from, String pass, String to, String subject, String body) {

		Properties props = System.getProperties();

		String host = "smtp.gmail.com";

		props.put("mail.smtp.starttls.enable", "true");

		props.put("mail.smtp.host", host);

		props.put("mail.smtp.user", from);

		props.put("mail.smtp.password", pass);

		props.put("mail.smtp.port", "587");

		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);

		MimeMessage message = new MimeMessage(session);

		try {

		    //Set from address

		message.setFrom(new InternetAddress(from));

		message.addRecipients(RecipientType.TO, to);

		//Set subject

		message.setSubject(subject);

		message.setText(body);

		BodyPart objMessageBodyPart = new MimeBodyPart();

		objMessageBodyPart.setText("Please Find The Attached Report File!");

		Multipart multipart = new MimeMultipart();

		multipart.addBodyPart(objMessageBodyPart);

		objMessageBodyPart = new MimeBodyPart();

		//Set path to the pdf report file

		String filename = System.getProperty("C:")+"Desktop\\test.pdf";

		//Create data source to attach the file in mail

		FileDataSource source = new FileDataSource(filename);

		objMessageBodyPart.setDataHandler(new DataHandler(source));

		objMessageBodyPart.setFileName(filename);

		multipart.addBodyPart(objMessageBodyPart);

		message.setContent(multipart);

		Transport transport = session.getTransport("smtp");

		transport.connect(host, from, pass);

		transport.sendMessage(message, message.getAllRecipients());

		transport.close();

		}

		catch (AddressException ae) {

		ae.printStackTrace();

		}

		catch (MessagingException me) {

		me.printStackTrace();

		}

		}
	
	public void devLogin(WebDriver driver) throws FindFailed {
		
		/*
		 * This Works with Firefox but not in chrome.
		 * Anyway this cannot be supported in remote systems and long runs.
		 * Screen s = new Screen().getPrimaryScreen();
		 * 
		 * Pattern name = new
		 * Pattern("C:\\Users\\UTIS CPU 31\\Downloads\\metallica\\devname.PNG");
		 * 
		 * Pattern password = new
		 * Pattern("C:\\Users\\UTIS CPU 31\\Downloads\\metallica\\devpassword.PNG");
		 * 
		 * Pattern signin = new
		 * Pattern("C:\\Users\\UTIS CPU 31\\Downloads\\metallica\\signin.PNG");
		 * 
		 * s.type(name,"storefront");
		 * 
		 * s.type(password,"Blu3Ac0rn$");
		 * 
		 * s.click(signin);
		 */
		
		
		
	}
	
	public void metlogin(WebDriver driver) {
		// TODO Auto-generated method stub
		WebElement lsign = driver.findElement(By.xpath("//span[text()='Login']"));
		
		lsign.click();
		
		WebElement email = driver.findElement(By.xpath("//input[contains(@id,'dwfrm_login_username_')]"));
		
		email.sendKeys("ithikasha@unitedtechno.com");
		
		WebElement pwd = driver.findElement(By.xpath("//input[contains(@id,'dwfrm_login_password_')]"));
		
		pwd.sendKeys("Ithi@utis07");
		
		WebElement login = driver.findElement(By.xpath("//button[contains(@name,'dwfrm_login_login')]"));
		
		login.click();
		
	}
	
	public boolean waitForElementToLoad(WebElement element) throws InterruptedException
    {
		int maxWaitTime=180 ;
		boolean isElementLoaded = false;
        int waitTime = 0;
        while (waitTime < maxWaitTime)
        {
            if (isElementDisplayedAndEnabled(element))
            {
                isElementLoaded = true;
                break;
            }
            Thread.sleep(1000);
            waitTime++;
        }
        return isElementLoaded;
    }


	public boolean isElementDisplayedAndEnabled(WebElement element)
    {
        boolean isEnabledAndDisplayed = false;
        try
        {
            isEnabledAndDisplayed = (element.isDisplayed() && element.isEnabled());
            return isEnabledAndDisplayed;
        } catch (Exception e)
        {
            return isEnabledAndDisplayed;
        }
    }


	public void WaitAndClick(WebElement element) throws InterruptedException 
	{
		int count=0;
	
		while(!element.isEnabled())
		{
			System.out.println("Waiting for element- "+(++count)+"secs");
			Thread.sleep(1000);
		}
	
		element.click();
	}
}
