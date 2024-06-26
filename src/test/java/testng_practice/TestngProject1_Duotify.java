package testng_practice;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class TestngProject1_Duotify {

    WebDriver driver;

    @BeforeTest (alwaysRun = true)
    public void beforeTest(){
        TestngUtils.writeSignUpDataToFile("src/test/java/testng_practice/signUpData.csv",
                "src/test/java/testng_practice/validLogin.csv", 5);
        TestngUtils.writeInvalidLoginDataToFile("src/test/java/testng_practice/invalidLogin.csv", 5);
    }

    @BeforeMethod
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

    @Test (priority = 1, dataProvider = "getSignUpData")
    public void testSignUpAndLogin(String userName, String firstName, String lastName, String email, String password) throws InterruptedException {

        driver.get("http://duotify.us-east-2.elasticbeanstalk.com/register.php");
        Thread.sleep(1000);

        String expectedTitle = "Welcome to Duotify!";
        Assert.assertEquals(driver.getTitle(), expectedTitle);

        driver.findElement(By.xpath(" //span[@id='hideLogin']")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("username")).sendKeys(userName, Keys.TAB,
                firstName, Keys.TAB, lastName, Keys.TAB,
                email, Keys.TAB, email, Keys.TAB,
                password, Keys.TAB, password);

        driver.findElement(By.xpath("//button[@name='registerButton']")).click();
        Thread.sleep(1000);

        String expectedUrl = "http://duotify.us-east-2.elasticbeanstalk.com/browse.php?";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);

        String fullName = driver.findElement(By.id("nameFirstAndLast")).getText();
        Assert.assertEquals(fullName, firstName + " " + lastName);

        driver.findElement(By.id("nameFirstAndLast")).click();
        Thread.sleep(1000);

        String userNameCenter = driver.findElement(By.xpath("//div[@class='userInfo']")).getText();
        Assert.assertEquals(userNameCenter, firstName + " " + lastName);

        //logout
        driver.findElement(By.id("rafael")).click();
        Thread.sleep(1000);

        //logout URL
        String expectedUrl1 = "http://duotify.us-east-2.elasticbeanstalk.com/register.php";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl1);
    }


        @Test (priority = 2, dataProvider = "getValidLoginData")
        public void testValidLogin (String username, String password) throws InterruptedException {

            driver.get("http://duotify.us-east-2.elasticbeanstalk.com/register.php");
            Thread.sleep(1000);

            driver.findElement(By.id("loginUsername")).sendKeys(username);
            driver.findElement(By.id("loginPassword")).sendKeys(password);
            driver.findElement(By.name("loginButton")).click();

            Thread.sleep(1000);

            String actualText = driver.findElement(By.xpath("//h1[@class='pageHeadingBig']")).getText();
            String expectedText = "You Might Also Like";
            Assert.assertEquals(actualText, expectedText);

            //logout
            driver.findElement(By.id("nameFirstAndLast")).click();
            Thread.sleep(1000);
            driver.findElement(By.id("rafael")).click();
            Thread.sleep(1000);

            //logout URL
            String expectedUrl2 = "http://duotify.us-east-2.elasticbeanstalk.com/register.php";
            Assert.assertEquals(driver.getCurrentUrl(), expectedUrl2);
        }


        @DataProvider
        public Object[][] getSignUpData () {
            return TestngUtils.readData("src/test/java/testng_practice/signUpData.csv");
        }

    @DataProvider
    public Object[][] getValidLoginData () {
        return TestngUtils.readData("src/test/java/testng_practice/validLogin.csv");
    }

    }
