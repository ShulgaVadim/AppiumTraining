import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IOSCalendarTest {

    IOSDriver driver;
    Event testEvent = new Event("Test Event");

    @Before
    public void setUp() throws MalformedURLException {
        URL driverURL = new URL("http://0.0.0.0:4723/wd/hub");
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformNme", "iOS");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("udid", "985A2290-A9D8-4A7C-89B2-73C6211EAC93");
        caps.setCapability("bundleId", "com.apple.mobilecal");
        caps.setCapability("deviceName", "sim_11_ios");
        caps.setCapability("noReset", "true");
        driver = new IOSDriver(driverURL, caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void createEvent() {
        driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Today\"`]")).click();
        driver.findElement(MobileBy.AccessibilityId("Add")).click();
        driver.findElement(MobileBy.AccessibilityId("Title")).sendKeys(testEvent.getEventName());

        //enter start date
        driver.findElement(MobileBy.xpath("(//XCUIElementTypeOther[@name='Date and Time Picker'])[1]/XCUIElementTypeButton[2]")).click();
        List<WebElement> wheelsStart = driver.findElements(MobileBy.xpath("//XCUIElementTypePickerWheel"));
        String startMins = String.valueOf(5 * (Math.round(Integer.parseInt(testEvent.getStartMin()) / 5)));//Time picker for minutes is a multiple of 5
        wheelsStart.get(0).sendKeys(testEvent.getStartHours());
        wheelsStart.get(1).sendKeys(startMins);
        wheelsStart.get(2).sendKeys(testEvent.getStartDayTime());

        //enter end date
        driver.findElement(MobileBy.xpath("(//XCUIElementTypeOther[@name='Date and Time Picker'])[2]/XCUIElementTypeButton[2]")).click();
        List<WebElement> wheelsEnd = driver.findElements(MobileBy.xpath("//XCUIElementTypePickerWheel"));
        String endMins = String.valueOf(5 * (Math.round(Integer.parseInt(testEvent.getStartMin()) / 5))); //Time picker for minutes is a multiple of 5
        wheelsEnd.get(0).sendKeys(testEvent.getEndHours());
        wheelsEnd.get(1).sendKeys(endMins);
        wheelsEnd.get(2).sendKeys(testEvent.getEndDayTime());
        driver.findElement(MobileBy.xpath("(//XCUIElementTypeButton[@name=\"Add\"])[2]")).click();

        //open created event
        WebElement event = driver.findElement(MobileBy.xpath(String.format("//XCUIElementTypeButton[contains(@name, '%s')]", testEvent.getEventName())));
        HashMap<String, String> scrollObject = new HashMap<>();
        scrollObject.put("direction", "up");
        scrollObject.put("index", "13");
        driver.executeScript("mobile: swipe", scrollObject);
        event.click();

        //Validation
        String expectedName = driver.findElement(By.xpath(String.format("//XCUIElementTypeCell[@name='%s']/XCUIElementTypeOther/following-sibling::*[1]", testEvent.getEventName()))).getText();
        String expectedStartDate = driver.findElement(By.xpath(String.format("//XCUIElementTypeCell[@name='%s']/XCUIElementTypeOther/following-sibling::*[3]", testEvent.getEventName()))).getText().substring(5, 12);
        String expectedEndDate = driver.findElement(By.xpath(String.format("//XCUIElementTypeCell[@name='%s']/XCUIElementTypeOther/following-sibling::*[3]", testEvent.getEventName()))).getText().substring(16);

        String actualName = testEvent.getEventName();
        String actualStartDate = testEvent.getStartHours() + ":" + startMins + " " + testEvent.getStartDayTime();
        String actualEndDate = testEvent.getEndHours() + ":" + endMins + " " + testEvent.getEndDayTime();

        Assertions.assertAll(
                () -> assertEquals(expectedName, actualName),
                () -> assertEquals(expectedStartDate, actualStartDate),
                () -> assertEquals(expectedEndDate, actualEndDate)
        );
    }

    @After
    public void driverTearDown() {
        driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Delete Event'`]")).click();
        driver.findElement(MobileBy.iOSClassChain("**/XCUIElementTypeButton[`label == 'Delete Event'`][1]")).click();
        driver.quit();
    }
}

