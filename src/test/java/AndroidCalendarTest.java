import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AndroidCalendarTest {
    AndroidDriver driver;
    Event testEvent = new Event("Test Event", 60, 150);

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
        caps.setCapability("newCommandTimeout", 100);

        driver = new AndroidDriver(driverURL, caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void createEvent() {
        driver.findElement(MobileBy.id("floating_action_button")).click();
        driver.findElement(MobileBy.AccessibilityId("Event button")).click();
        driver.findElement(MobileBy.id("title_edit_text")).sendKeys(testEvent.getEventName());
        driver.findElement(MobileBy.xpath("//android.widget.Button[contains(@content-desc, 'Start time')]")).click();

        //enter start date
        driver.findElement(MobileBy.id("android:id/toggle_mode")).click();
        driver.findElement(MobileBy.id("android:id/input_hour")).sendKeys(testEvent.getStartHours());
        driver.findElement(MobileBy.id("android:id/input_minute")).sendKeys(testEvent.getStartMin());
        driver.findElement(MobileBy.id("android:id/button1")).click();
        driver.findElement(MobileBy.xpath("//android.widget.Button[contains(@content-desc, 'End time')]")).click();

        //enter end date
        driver.findElement(MobileBy.id("android:id/toggle_mode")).click();
        driver.findElement(MobileBy.id("android:id/input_hour")).sendKeys(testEvent.getEndHours());
        driver.findElement(MobileBy.id("android:id/input_minute")).sendKeys(testEvent.getEndMin());
        driver.findElement(MobileBy.id("android:id/button1")).click();
        driver.findElement(MobileBy.id("save")).click();

        //validation
        Assert.assertTrue("The event is missed in the list of events", !driver.findElements(MobileBy.xpath(String.format("//android.view.View[contains(@content-desc, '%s')]", testEvent.getEventName()))).isEmpty());
        driver.findElement(MobileBy.xpath(String.format("//android.view.View[contains(@content-desc, '%s')]", testEvent.getEventName()))).click();
        String actualName = driver.findElement(MobileBy.id("title")).getText();
        String actualTime = driver.findElement(MobileBy.id("time")).getText();
        String expectedTime = testEvent.getAndroidEventDetails();

        Assertions.assertAll(
                () -> assertEquals(testEvent.getEventName(), actualName, "Event name is incorrect"),
                () -> assertEquals(expectedTime, actualTime, "Event time is incorrect")
        );
    }

    @After
    public void deleteEvent() {
        driver.launchApp();
        if (!driver.findElements(MobileBy.xpath(String.format("//android.view.View[contains(@content-desc, '%s')]", testEvent.getEventName()))).isEmpty()) {
            driver.findElement(MobileBy.xpath(String.format("//android.view.View[contains(@content-desc, '%s')]", testEvent.getEventName()))).click();
            driver.findElement(MobileBy.AccessibilityId("More options")).click();
            driver.findElement(MobileBy.xpath("//android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView")).click();
            driver.findElement(MobileBy.id("android:id/button1")).click();
        }
    }

    @After
    public void driverTearDown() {
        driver.quit();
    }
}

