import org.example.YandexMarket;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestSuite {

    private YandexMarket yandexMarket;
    private static WebDriver driver;

    @BeforeClass
    public static void before(){
        System.setProperty("webdriver.gecko.driver", "/home/ev/geckoDriver/geckodriver-v0.31.0-linux64/geckodriver");
        driver = new FirefoxDriver();
    }

    @Test
    public void correctWork() throws InterruptedException {
        yandexMarket = new YandexMarket();
        Assert.assertTrue(yandexMarket.findLaptopsByManufactureAndPrice(
                "Lenovo", 25000, 30000, driver));
    }

    @Test
    public void incorrectWorkOne() throws InterruptedException {
        yandexMarket = new YandexMarket();
        Assert.assertFalse(yandexMarket.findLaptopsByManufactureAndPrice(
                "Lenovo", -25000, 50000, driver));
    }

    @Test
    public void incorrectWorkTwo() throws InterruptedException {
        yandexMarket = new YandexMarket();
        Assert.assertFalse(yandexMarket.findLaptopsByManufactureAndPrice(
                "Lenovo", 25000, 10000000000000000L, driver));
    }

    @Test
    public void incorrectWorkThree() throws InterruptedException {
        yandexMarket = new YandexMarket();
        Assert.assertFalse(yandexMarket.findLaptopsByManufactureAndPrice(
                "Lenovo", 25000, 10000, driver));
    }


    @AfterClass
    public static void after(){
        driver.close();
        //System.exit(0);
    }
}
