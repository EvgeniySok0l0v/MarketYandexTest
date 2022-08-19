package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class Main {
    private static final int LOWER_VALUE = 25000;
    private static final int UPPER_VALUE = 30000;
    private static final String MANUFACTURE_NAME = "Lenovo";
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/home/ev/geckoDriver/geckodriver-v0.31.0-linux64/geckodriver");
        WebDriver driver = new FirefoxDriver();
        String appUrl = "https://market.yandex.ru/";

        driver.get(appUrl);

        driver.manage().window().maximize();

        Thread.sleep(7000);

        WebElement catalog = driver.findElement(By.id("catalogPopupButton"));
        catalog.click();
        Actions action = new Actions(driver);
        Thread.sleep(7000);

        WebElement computers = driver.findElement(
                By.xpath("//li[@data-zone-data='{\"id\":\"97009164\"}']/a[@href='/catalog--kompiuternaia-tekhnika/54425']"));

        action.moveToElement(computers);
        action.perform();
        Thread.sleep(7000);

        WebElement laptop = driver.findElement(By.xpath("//a[@href='/catalog--noutbuki/54544/list?hid=91013']"));
        laptop.click();

        Thread.sleep(7000);

        WebElement showAll = driver.findElement(By.xpath("//span[@class='_2Pukk']"));
        showAll.click();
        Thread.sleep(500);
        WebElement searchManufacture = driver.findElement(By.xpath("//div[@class='PndfQ']//input[@type='text']"));

        searchManufacture.click();
        searchManufacture.clear();
        searchManufacture.sendKeys(MANUFACTURE_NAME);
        Thread.sleep(7000);
        //click checkBox
        driver.findElement(By.xpath("//div[@data-filter-value-id='152981']//span[@class='_2XaWK']")).click();
        Thread.sleep(7000);

        WebElement lowerValueInput = driver.findElement(
                By.xpath("//span[@data-auto='filter-range-min']//input"));
        lowerValueInput.clear();
        lowerValueInput.sendKeys(String.valueOf(LOWER_VALUE));
        WebElement upperValueInput = driver.findElement(
                By.xpath("//span[@data-auto='filter-range-max']//input"));
        upperValueInput.clear();
        upperValueInput.sendKeys(String.valueOf(UPPER_VALUE));
        Thread.sleep(20000);

        Thread.sleep(7000);
        driver.close();
        System.out.println("Test script executed successfully.");

        System.exit(0);
    }
}
