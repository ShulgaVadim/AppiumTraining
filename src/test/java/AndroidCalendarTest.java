import Screens.Android.CalendarAndroidScreen;
import Screens.Android.EventDetailsScreen;
import Screens.Android.NewEventScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.junit.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AndroidCalendarTest {
    AndroidDriver driver;
    CalendarAndroidScreen calendarAndroidScreen;
    NewEventScreen newEventScreen;
    EventDetailsScreen eventDetailsScreen;
    Event testEvent = new Event("Test Event", 1, 8, "Paris");

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
        calendarAndroidScreen = new CalendarAndroidScreen(driver);
        newEventScreen = new NewEventScreen(driver);
        eventDetailsScreen = new EventDetailsScreen(driver);
    }

    @Test
    public void createEventTest() {
        calendarAndroidScreen
                .tapAddNewEventButton()
                .selectEventButton();
        newEventScreen
                .enterEventTitle(testEvent.getEventName())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin())
                .enterLocation(testEvent.getLocation())
                .saveEvent();
        Assert.assertTrue("The event is missed in the list of events", calendarAndroidScreen.isEventCreated(testEvent.getEventName()));
        calendarAndroidScreen
                .openEvent(testEvent.getEventName());

        Assertions.assertAll(
                () -> assertEquals(testEvent.getEventName(), eventDetailsScreen.getActualName(), "Event name is incorrect"),
                () -> assertEquals(testEvent.getAndroidEventDetails(), eventDetailsScreen.getActualTime(), "Event time is incorrect"),
                () -> assertEquals(testEvent.getLocation(), eventDetailsScreen.getActualLocation(testEvent.getLocation()), "Location is incorrect")
        );
    }

    @Test
    public void createEventWithPushN() {
        calendarAndroidScreen
                .tapAddNewEventButton()
                .selectEventButton();
        newEventScreen
                .enterEventTitle(testEvent.getEventName())
                .enterStartDate(testEvent.getStartHours(), testEvent.getStartMin())
                .enterEndDate(testEvent.getEndHours(), testEvent.getEndMin())
                .addNotification()
                .saveEvent();
        Assert.assertTrue("The event is missed in the list of events", calendarAndroidScreen.isEventCreated(testEvent.getEventName()));
        driver.openNotifications();
        Assertions.assertAll(
                () -> assertTrue(calendarAndroidScreen.validateTitleFromPush(testEvent.getEventName()), "Event title is not correct"),
                () -> assertTrue(calendarAndroidScreen.validateTimeFromPush(testEvent.getAndroidTimeFromPush()), "Event time is not correct"))
        ;
    }

    @After
    public void deleteEvent() {
        driver.launchApp();
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        calendarAndroidScreen.deleteEvent(testEvent.getEventName());
    }


    @After
    public void driverTearDown() throws IOException {
        driver.quit();
        Runtime.getRuntime().exec("adb -s emulator-5554 emu kill");
    }
}

