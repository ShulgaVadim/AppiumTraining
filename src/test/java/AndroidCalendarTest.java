import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AndroidCalendarTest {
    AndroidDriver driver;
    Event testEvent = new Event("Test Event", 60, 150, "Paris");

    @Before
    public void setUp() throws MalformedURLException {
        URL driverURL = new URL("http://0.0.0.0:4723/wd/hub");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("udid", "emulator-5554");
        caps.setCapability("deviceName", "Pixel_2_API_29");
        caps.setCapability("avd", "Pixel_2_API_29");
        caps.setCapability("appPackage", "com.google.android.calendar");
        caps.setCapability("appActivity", "com.android.calendar.LaunchActivity");
        caps.setCapability("noReset", "true");
        caps.setCapability("newCommandTimeout", 100);

        driver = new AndroidDriver(driverURL, caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void createEvent() {
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Create new event\")")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Event\")")).click();
        driver.findElement(MobileBy.id("title")).sendKeys(testEvent.getEventName());

        //enter start date
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Start time\")")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.ImageButton\")")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_hour\")")).sendKeys(testEvent.getStartHours());
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_minute\")")).sendKeys(testEvent.getStartMin());
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\")")).click();

        //enter end date
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"End time\")")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.ImageButton\")")).click();
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_hour\")")).sendKeys(testEvent.getEndHours());
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"android:id/input_minute\")")).sendKeys(testEvent.getEndMin());
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\")")).click();

        //enter location
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").text(\"Add location\")")).click();
        driver.findElement(MobileBy.id("search_text")).sendKeys(testEvent.getLocation());
        driver.getKeyboard().pressKey(Keys.ENTER);
        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Save\")")).click();

        //validation
        Assert.assertTrue("The event is missed in the list of events", !driver.findElements(MobileBy.AndroidUIAutomator(String.format("new UiSelector().descriptionContains(\"%s\")", testEvent.getEventName()))).isEmpty());
        driver.findElement(MobileBy.AndroidUIAutomator(String.format("new UiSelector().descriptionContains(\"%s\")", testEvent.getEventName()))).click();
        String actualName = driver.findElement(MobileBy.id("title")).getText();
        String actualTime = driver.findElement(MobileBy.id("time")).getText();
        String expectedTime = testEvent.getAndroidEventDetails();
        String actualLocation = testEvent.getLocation();
        String expectedLocation = driver.findElement(MobileBy.AndroidUIAutomator(String.format("new UiSelector().resourceId(\"com.google.android.calendar:id/first_line_text\").textContains(\"%s\")", testEvent.getLocation()))).getText();

        Assertions.assertAll(
                () -> assertEquals(testEvent.getEventName(), actualName, "Event name is incorrect"),
                () -> assertEquals(expectedTime, actualTime, "Event time is incorrect"),
                () -> assertEquals(expectedLocation, actualLocation, "Location is incorrect")
        );
    }

    @After
    public void deleteEvent() {
        driver.launchApp();
        if (!driver.findElements(MobileBy.AndroidUIAutomator(String.format("new UiSelector().descriptionContains(\"%s\")", testEvent.getEventName()))).isEmpty()) {
            MobileElement event = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(String.format("new UiSelector().descriptionContains(\"%s\")", testEvent.getEventName())));
            Dimension elementSize = event.getSize();
            int y = event.getCenter().getY();
            int startX = (int) (0.2 * elementSize.getWidth());
            int endX = (int) (0.8 * elementSize.getWidth());
            new TouchAction(driver)
                    .press(PointOption.point(startX, y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                    .moveTo(PointOption.point(endX, y))
                    .release()
                    .perform();
            driver.findElement(MobileBy.id("android:id/button1")).click();
        }
    }

    @After
    public void driverTearDown() throws IOException {
        driver.quit();
        Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
    }
}

