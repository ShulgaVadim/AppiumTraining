import Screens.IoS.CalendarIosScreen;
import Screens.IoS.NewEventScreen;
import io.appium.java_client.ios.IOSDriver;
import org.junit.*;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class IOSCalendarTest {

    IOSDriver driver;
    CalendarIosScreen calendarIosScreen;
    NewEventScreen newEventScreen;
    Event testEvent = new Event("Test Event", 2, 5, "Chicago");

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
        calendarIosScreen = new CalendarIosScreen(driver);
        newEventScreen = new NewEventScreen(driver);
    }

    @Test
    public void createEvent() {
        driver.resetApp();
        calendarIosScreen
                .tapTodayButton()
                .tapAddNewEventButton();
        newEventScreen
                .enterTitle(testEvent.getEventName())
                .enterLocation(testEvent.getLocation())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin(), testEvent.getStartDayTime())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin(), testEvent.getEndDayTime())
                .tapAddButton();
        Assert.assertTrue("The event is missed in the list of events", calendarIosScreen.isEventCreated(testEvent.getEventName()));
        Assert.assertEquals("Expected event details don't match to actual ones", calendarIosScreen.getExpectedDetails(testEvent.getEventName()), testEvent.getIosEventDetails());
    }

    @Test
    public void createEventWithAlert() {
        driver.resetApp();
        calendarIosScreen
                .tapTodayButton()
                .tapAddNewEventButton();
        newEventScreen
                .enterTitle(testEvent.getEventName())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin(), testEvent.getStartDayTime())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin(), testEvent.getEndDayTime())
                .addAlert()
                .tapAddButton();
        Assert.assertTrue("The event is missed in the list of events", calendarIosScreen.isEventCreated(testEvent.getEventName()));
        calendarIosScreen.showNotifications();
        Assert.assertEquals("Expected event details don't match to actual ones", calendarIosScreen.getDetailsFromAlert(testEvent.getEventName()), testEvent.getIosDetailsFromPush());
        calendarIosScreen.hideNotifications();
    }

    @After
    public void deleteEvent() {
        driver.closeApp();
        driver.launchApp();
        calendarIosScreen.deleteEvent(testEvent.getEventName());
    }

    @After
    public void driverTearDown() throws IOException {
        Runtime.getRuntime().exec("xcrun simctl shutdown 985A2290-A9D8-4A7C-89B2-73C6211EAC93");
        driver.quit();
    }
}


