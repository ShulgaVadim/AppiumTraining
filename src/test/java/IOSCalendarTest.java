import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.LongPressOptions;
import org.junit.*;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.offset.ElementOption.element;

public class IOSCalendarTest {

    IOSDriver driver;
    Event testEvent = new Event("Test Event", 60, 70, "Chicago");

    @Before
    public void setUp() throws MalformedURLException {
        URL driverURL = new URL("http://0.0.0.0:4723/wd/hub");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "15.2");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("udid", "985A2290-A9D8-4A7C-89B2-73C6211EAC93");
        caps.setCapability("bundleId", "com.apple.mobilecal");
        caps.setCapability("deviceName", "iPhone Simulator");
        caps.setCapability("noReset", "true");
        caps.setCapability("newCommandTimeout", 100);
        driver = new IOSDriver(driverURL, caps);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Test
    public void createEvent() {
        driver.resetApp();
        driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Today'`]")).click();
        driver.findElement(MobileBy.iOSNsPredicateString("label == 'Add'")).click();
        driver.findElement(MobileBy.iOSNsPredicateString("name == 'Title'")).sendKeys(testEvent.getEventName());

        //enter location
        driver.findElement(MobileBy.iOSNsPredicateString("name == 'Location or Video Call'")).click();
        driver.findElement(MobileBy.AccessibilityId("Enter Location or Video Call")).sendKeys(testEvent.getLocation());
        driver.findElement(MobileBy.AccessibilityId("Done")).click();
//        driver.hideKeyboard(HideKeyboardStrategy.PRESS_KEY, "Done");

        //enter start date
        driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeOther[`label == 'Date and Time Picker'`][1]/XCUIElementTypeButton[2]")).click();
        List<MobileElement> wheelsStart = driver.findElements(MobileBy.iOSClassChain("**/XCUIElementTypePickerWheel"));
        wheelsStart.get(0).sendKeys(testEvent.getStartHours());
        wheelsStart.get(1).sendKeys(testEvent.getIosStartMin());
        wheelsStart.get(2).sendKeys(testEvent.getStartDayTime());

        //enter end date
        driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeOther[`label == 'Date and Time Picker'`][2]/XCUIElementTypeButton[2]")).click();
        List<MobileElement> wheelsEnd = driver.findElements(MobileBy.iOSClassChain("**/XCUIElementTypePickerWheel"));
        wheelsEnd.get(0).sendKeys(testEvent.getEndHours());
        wheelsEnd.get(1).sendKeys(testEvent.getIosEndMin());
        wheelsEnd.get(2).sendKeys(testEvent.getEndDayTime());
        driver.findElement(MobileBy.iOSNsPredicateString("label == 'Add' AND visible == 1")).click();

        //validation
        Assert.assertTrue("The event is missed in the list of events", !driver.findElements(MobileBy.iOSClassChain(String.format("**/XCUIElementTypeButton[`name CONTAINS[cd] '%s'`]", testEvent.getEventName()))).isEmpty());
        String expectedDetails = driver.findElement(MobileBy.iOSClassChain(String.format("**/XCUIElementTypeButton[`name CONTAINS[cd] '%s'`]", testEvent.getEventName()))).getText();
        String actualDetails = testEvent.getIosEventDetails();

        Assert.assertEquals("Expected event details don't match to actual ones", expectedDetails, actualDetails);
    }

    @After
    public void deleteEvent() {
        driver.closeApp();
        driver.launchApp();
        if (!driver.findElements(MobileBy.iOSClassChain(String.format("**/XCUIElementTypeButton[`name CONTAINS[cd] '%s'`]", testEvent.getEventName()))).isEmpty()) {
            driver.findElement(MobileBy.AccessibilityId("List")).click();
            driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Today'`]")).click();
            MobileElement event = (MobileElement) driver.findElement(MobileBy.iOSClassChain(String.format("**/XCUIElementTypeCell[`name CONTAINS[cd] '%s'`]", testEvent.getEventName())));
            new TouchAction(driver)
                    .longPress(LongPressOptions
                            .longPressOptions()
                            .withElement(element(event))
                            .withDuration(Duration.ofSeconds(3)))
                    .release()
                    .perform();

            driver.findElement(MobileBy.AccessibilityId("Delete Event")).click();
            driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Delete Event'`]")).click();
            driver.findElement(MobileBy.AccessibilityId("List")).click();
        }
    }

    @After
    public void driverTearDown() throws IOException {
        Runtime.getRuntime().exec("xcrun simctl shutdown 985A2290-A9D8-4A7C-89B2-73C6211EAC93");
        driver.quit();
    }
}

