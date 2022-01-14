import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AndroidCalendarTest {
    AndroidDriver driver;
    Event testEvent = new Event("Test Event");


    @Before
    public void setUp() throws MalformedURLException {
        URL driverURL = new URL("http://0.0.0.0:4723/wd/hub");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformNme", "Android");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("udid", "emulator-5554");
        caps.setCapability("appPackage", "com.google.android.calendar");
        caps.setCapability("appActivity", "com.android.calendar.LaunchActivity");
        caps.setCapability("noReset", "true");

        driver = new AndroidDriver(driverURL, caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void createEvent() {

        driver.findElement(By.id("com.google.android.calendar:id/floating_action_button")).click();
        driver.findElement(MobileBy.AccessibilityId("Event button")).click();
        driver.findElement(By.id("com.google.android.calendar:id/title_edit_text")).sendKeys(testEvent.getEventName());
        driver.findElement(By.xpath("//android.widget.Button[contains(@content-desc, 'Start time')]")).click();

        //enter start date
        driver.findElement(By.id("android:id/toggle_mode")).click();
        driver.findElement(By.id("android:id/input_hour")).sendKeys(testEvent.getStartHours());
        driver.findElement(By.id("android:id/input_minute")).sendKeys(testEvent.getStartMin());
        driver.findElement(By.id("android:id/button1")).click();
        driver.findElement(By.xpath("//android.widget.Button[contains(@content-desc, 'End time')]")).click();

        //enter end date
        driver.findElement(By.id("android:id/toggle_mode")).click();
        driver.findElement(By.id("android:id/input_hour")).sendKeys(testEvent.getEndHours());
        driver.findElement(By.id("android:id/input_minute")).sendKeys(testEvent.getEndMin());
        driver.findElement(By.id("android:id/button1")).click();

        driver.findElement(By.id("com.google.android.calendar:id/save")).click();
        driver.findElement(By.xpath(String.format("//android.view.View[contains(@content-desc, '%s')]", testEvent.getEventName()))).click();

        String actualName = driver.findElement(By.id("com.google.android.calendar:id/title")).getText();
        String actualTime = driver.findElement(By.id("com.google.android.calendar:id/time")).getText();
        String expectedTime = testEvent.getAndroidEventDetails();


        Assertions.assertAll(
                () -> assertEquals(testEvent.getEventName(), actualName),
                () -> assertEquals(expectedTime, actualTime)
        );
    }


    @After
    public void driverTearDown() {
        driver.findElement(MobileBy.AccessibilityId("More options")).click();
        driver.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView")).click();
        driver.findElement(By.id("android:id/button1")).click();
        driver.quit();
    }
}


