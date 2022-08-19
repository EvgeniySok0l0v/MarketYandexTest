package org.example;

import org.example.util.StringUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * YandexMarket class
 */
public class YandexMarket {

    private static final long MAX_VALUE = 1000000000000000L;
    private static final String URL = "https://market.yandex.ru/";
    private static final int SEVEN_SEC = 7000;
    private static final int TWO_SEC = 2000;
    //LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexMarket.class);

    /**
     * Main script
     *
     * @param manufacture - brand
     * @param lowerValue - lower value of price
     * @param upperValue - upper value of price
     * @param driver - webDriver
     * @return - boolean value
     * @throws InterruptedException - exc
     */
    public boolean findLaptopsByManufactureAndPrice(
            String manufacture,
            long lowerValue,
            long upperValue,
            WebDriver driver) throws InterruptedException {

        try {
            driver.get(URL);
            driver.manage().window().maximize();
            //usually have 2 captcha
            Thread.sleep(SEVEN_SEC*2);
        /*
            set location Moscow, cos my location Homel Belarus
            and have problem with price
        */
            WebElement currentLocation = driver.findElement(By.xpath(
                    "//button[@class='_1KpjX _3UND8']"
            ));

            if(currentLocation.getText().equals("Москва")){
                LOGGER.info("Москва епт");
            } else {
                currentLocation.click();
                Thread.sleep(SEVEN_SEC);
                WebElement location = driver.findElement(By.xpath(
                        "//div[@class='_31fu1']//input[@type='text']"
                ));

        /*
            clear don't work, I don,t know why
            location.clear();
            other solution
        */
                location.click();
                int length = location.getAttribute("value").length();
                for (int i = 0; i < length; i++){
                    location.sendKeys(Keys.BACK_SPACE);
                }

                Thread.sleep(TWO_SEC);
                location.sendKeys("Москва");
                Thread.sleep(TWO_SEC);

                driver.findElement(By.xpath(
                        "//li[@data-suggestion-index='1']"
                )).click();
                driver.findElement(By.xpath(
                        "//button[@class='_2AMPZ _1N_0H _3_b2k _1Llz3']"
                )).click();
            }

            //#############################################################################
            //can be double captcha
            Thread.sleep(SEVEN_SEC*2);

            //click to Каталог
            driver.findElement(By.id("catalogPopupButton")).click();

            //Action for move to element Компьютеры
            Actions action = new Actions(driver);
            Thread.sleep(SEVEN_SEC);

            WebElement computers = driver.findElement(By.xpath(
                    "//li[@data-zone-data='{\"id\":\"97009164\"}']" +
                            "/a[@href='/catalog--kompiuternaia-tekhnika/54425']" +
                            "/img[@src='//avatars.mds.yandex.net/get-marketcms/" +
                            "879900/img-265ee528-3e72-4769-91ab-ae15cf38da8e.svg/svg']"));

            action.moveToElement(computers);
            action.perform();

            Thread.sleep(TWO_SEC);
            //click to Ноутбуки
            driver.findElement(By.xpath(
                    "//a[@href='/catalog--noutbuki/54544/list?hid=91013']")).click();
            //double sleep cos may have 2 captcha
            Thread.sleep(SEVEN_SEC*2);
            //click to Показать все(Производители)
            driver.findElement(By.xpath(
                    "//span[@class='_2Pukk']")).click();
            Thread.sleep(TWO_SEC);

        /*
            send keys for search Производитель
         */
            WebElement searchManufacture = driver.findElement(By.xpath(
                    "//div[@class='PndfQ']//input[@type='text']"));

            searchManufacture.click();
            searchManufacture.clear();
            searchManufacture.sendKeys(manufacture);
            Thread.sleep(TWO_SEC);

            //set check box
            List<WebElement> listOfManufactures = driver.findElements(By.xpath(
                    "//div[@class='_2XVtn']"
            ));

            WebElement setCheck = listOfManufactures
                    .stream()
                    .filter(v -> v.getText().equals(manufacture))
                    .findFirst()
                    .orElseThrow();

            setCheck.findElement(By.className("_2XaWK")).click();

            Thread.sleep(TWO_SEC);

            //Check price value
            if(lowerValue > upperValue || lowerValue < 0 || upperValue > MAX_VALUE){
                LOGGER.warn("Incorrect price value");
                return false;
            } else {
                //set price values
                WebElement lowerValueInput = driver.findElement(By.xpath(
                        "//span[@data-auto='filter-range-min']//input"));
                lowerValueInput.clear();
                lowerValueInput.sendKeys(String.valueOf(lowerValue));
                WebElement upperValueInput = driver.findElement(
                        By.xpath(
                                "//span[@data-auto='filter-range-max']//input"));
                upperValueInput.clear();
                upperValueInput.sendKeys(String.valueOf(upperValue));
            }
            //double sleep for download all page
            Thread.sleep(SEVEN_SEC*2);
        } catch (Exception e){
            LOGGER.info(e.getMessage());
        }

        return workWithHrefs(manufacture, lowerValue, upperValue, driver);
    }

    /**
     * Method for work with hrefs
     *
     * @param driver - WebDriver
     * @return boolean value
     * @throws InterruptedException - exc
     */
    private boolean workWithHrefs(
            String manufacture,
            long lowerValue,
            long upperValue,
            WebDriver driver) throws InterruptedException {
        /*
            scroll page for download all hrefs
         */
        try {
            long lastHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");

            while (true) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(TWO_SEC);

                long newHeight = (long) ((JavascriptExecutor) driver).executeScript("return document.body.scrollHeight");
                if (newHeight == lastHeight) {
                    break;
                }
                lastHeight = newHeight;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //number of hrefs on a page
        int numberOfHrefs = driver.findElements(By.xpath(
                "//article[@data-autotest-id='product-snippet']")).size();
        Thread.sleep(SEVEN_SEC*2);
        //list with hrefs
        List<String> hrefs = new ArrayList<>(numberOfHrefs);
        //list with laptops brand and price
        List<Laptop> laptops = new ArrayList<>(numberOfHrefs);
        ;

        Thread.sleep(SEVEN_SEC);
        //loop for add hrefs
        for(int i = 1; i <= 3; i++){
            String href = driver.findElement(By.xpath(
                    "//div[@data-index='" + i + "']//a")).getAttribute("href");
            hrefs.add(href);
        }

        //loop for add brand and price for laptop by hrefs
        for(String href : hrefs){
            driver.get(href);
            Laptop laptop = new Laptop();
            Thread.sleep(SEVEN_SEC);

            try{
                //get brand
                WebElement brand = driver.findElement(By.xpath(
                        "//div[@class='cia-vs']//span[@class='_3MLiq']"
                ));
                laptop.setName(brand.getText());

                //get price
                WebElement price = driver.findElement(By.xpath(
                        "//div[@class='_3NaXx _3kWlK']//span"
                ));
                laptop.setPrice(StringUtil.convertStringToLong(price.getText()));
                laptops.add(laptop);
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }

        }

        return checkerForBrandAndPrice(manufacture,lowerValue,upperValue,laptops);
    }

    /**
     * Method for check every laptops from list
     *
     * @param manufacture - brand
     * @param lowerValue - lower value of price
     * @param upperValue - upper value of price
     * @param laptops - list of laptops
     * @return - boolean value
     */
    private boolean checkerForBrandAndPrice(
            String manufacture,
            long lowerValue,
            long upperValue,
            List<Laptop> laptops){

        return laptops
                .stream()
                .allMatch(
                        laptop -> laptop.getName().equals(manufacture)
                                && laptop.getPrice() >= lowerValue
                                && laptop.getPrice() <= upperValue);
    }
}
