package ru.netology.domain;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppOrderTest {
    private WebDriver driver;
    private WebElement form;
    private static ChromeOptions options;

    @BeforeAll
    static void setUpAll() {
        if (System.getProperty("os.name").equals("Linux")){
            System.setProperty("webdriver.chrome.driver", "./driver/linux/chromedriver");
        } else if (System.getProperty("os.name").contains("Windows")) {
            System.setProperty("webdriver.chrome.driver", "driver/windows/chromedriver.exe");
        }
        options = new ChromeOptions();
        options.addArguments("--headless");
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
        form = driver.findElement(By.cssSelector("form.form"));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSubmitRequest() {
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Медведев Николай");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79251234122");
        form.findElement(By.cssSelector(".checkbox__box")).click();
        form.findElement(By.cssSelector("button.button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorIfEmptyName() {
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79251234122");
        form.findElement(By.cssSelector(".checkbox__box")).click();
        form.findElement(By.cssSelector("button.button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnErrorIfNameIsInEnglish() {
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Medvedev Nikolay");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79251234122");
        form.findElement(By.cssSelector(".checkbox__box")).click();
        form.findElement(By.cssSelector("button.button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }
}
